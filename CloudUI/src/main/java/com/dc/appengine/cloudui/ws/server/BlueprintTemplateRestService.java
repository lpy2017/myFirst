package com.dc.appengine.cloudui.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.BluePrint2Toolbar;
import com.dc.appengine.cloudui.utils.FileUtil;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.dcits.Common.entity.User;

@Path("blueprintTemplate")
public class BlueprintTemplateRestService {

	private static final Logger log = LoggerFactory.getLogger(BlueprintTemplateRestService.class);
	
	@GET
	@Path("listBlueprintTemplates")
	public String listBlueprintTemplates(@Context HttpServletRequest request,
										 @QueryParam("pageSize") @DefaultValue("10") int pageSize, 
										 @QueryParam("pageNum") @DefaultValue("1") int pageNum,
										 @QueryParam("blueprintName") @DefaultValue("") String blueprintName){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		RestTemplate restUtil = new RestTemplate();
		String query = "pageSize="+pageSize+"&pageNum="+pageNum+"&blueprintName="+blueprintName+"&userId="+user.getId();
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/listBlueprintTemplates?"+query, String.class);
		return result;
	}
	
	@POST
	@Path("saveBlueprintTemplate")
	public String saveBlueprintTemplate(@FormParam("blueprint_info") String  blueprint_info,
										@FormParam("blueprint_name") String  blueprint_name,
										@FormParam("user_id") String  user_id,
										@FormParam("blueprintDesc") @DefaultValue("") String  blueprintDesc,
										@FormParam("op") String op,
										@Context HttpServletRequest request){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_info", blueprint_info);
		requestEntity.add("blueprint_name", blueprint_name);
		requestEntity.add("blueprintDesc", blueprintDesc);
		requestEntity.add("user_id", user_id);
		requestEntity.add("op", op);
		User user = (User) request.getSession().getAttribute("user");
		requestEntity.add("userId", user.getName());
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/saveBlueprintTemplate",
						requestEntity, String.class);
		return result;
	}

	@POST
	@Path("delBlueprintTemplate")
	public String delBlueprintTemplate(@FormParam("blueprintId") String  blueprint_id,@Context HttpServletRequest request){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_id", blueprint_id);
		User user = (User) request.getSession().getAttribute("user");
		requestEntity.add("userId", user.getName());
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/delBlueprintTemplate",
						requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("checkBlueprintTemplateFlowUnique")
	@Produces("application/json; charset=UTF-8")
	public String checkBlueprintFlowUnique(@QueryParam("blueprintId") String blueprintId,
										   @QueryParam("flowName") String flowName) throws RestClientException, UnsupportedEncodingException{
		if(flowName==null ||flowName.equals("")){
			return "{\"result\":false,\"message\":\"" + "" + "\"}";
		}
		RestTemplate restUtil = new RestTemplate();
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprintTemplate/checkBlueprintFlowUnique?blueprintId="+blueprintId+"&flowName="+flowName, String.class);
		return result;
	}
	
	@POST
	@Path("addBlueprintTemplateFlow")
	public String addBlueprintFlow(@Context HttpServletRequest request,
			@FormParam("flowName")String flowName,
			@FormParam("flowInfoGroup")String flowInfoGroup,
			@FormParam("blueprintId")String blueprintId,
			@FormParam("appName")String appName){
		BluePrint2Toolbar tool=new BluePrint2Toolbar();
		String flowInfo;
		try {
			flowInfo = tool.convertRuntimeInfo(flowInfoGroup);
		} catch (Exception e) {
			return MessageHelper.wrap("result",false,"message","蓝图流程中间态转换失败。 reason[" + e.getMessage() + "]");
		}
		System.out.println("蓝图流程转换后为："+ flowInfo);
		
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("flowName", flowName);
		requestEntity.add("flowInfo", flowInfo);
		requestEntity.add("flowInfoGroup", flowInfoGroup);
		requestEntity.add("blueprintId", blueprintId);
		requestEntity.add("appName", appName);
		User user = (User) request.getSession().getAttribute("user");
		requestEntity.add("userId", user.getName());
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/addBlueprintTemplateFlow",requestEntity, String.class);
		return result;
	}
	
	@POST
	@Path("updateBlueprintTemplateFlow")
	public String updateBlueprintTemplateFlow(@FormParam("flowInfoGroup")String flowInfoGroup,
											  @FormParam("cdFlowId") String cdFlowId,
											  @Context HttpServletRequest request){
		BluePrint2Toolbar tool=new BluePrint2Toolbar();
		String flowInfo;
		try {
			flowInfo = tool.convertRuntimeInfo(flowInfoGroup);
		} catch (Exception e) {
			return MessageHelper.wrap("result",false,"message","蓝图流程中间态转换失败。 reason[" + e.getMessage() + "]");
		}
		System.out.println("蓝图流程转换后为："+ flowInfo);
		
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("cdFlowId", cdFlowId);
		requestEntity.add("flowInfoGroup", flowInfoGroup);
		requestEntity.add("flowInfo", flowInfo);
		User user = (User) request.getSession().getAttribute("user");
		requestEntity.add("userId", user.getName());
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/updateBlueprintTemplateFlow",
				requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("listBlueprintTemplateFlow")
	public String listBlueprintTemplateFlow(@QueryParam("blueprintId") String blueprintId,
			@QueryParam("sortName") @DefaultValue("") String sortName,
            @QueryParam("sortOrder") @DefaultValue("") String sortOrder){
		RestTemplate restUtil = new RestTemplate();
		String query = "blueprintId="+blueprintId+"&sortName="+sortName+"&sortOrder="+sortOrder;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprintTemplate/listBlueprintTemplateFlow?"+query, String.class);
		return result;
	}
	
	@POST
	@Path("delBlueprintTemplateFlow")
	public String delBlueprintTemplateFlow(@FormParam("cdFlowId") String  cdFlowId,@Context HttpServletRequest request){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("cdFlowId", cdFlowId);
		User user = (User) request.getSession().getAttribute("user");
		requestEntity.add("userId", user.getName());
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/delBlueprintTemplateFlow",
						requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("viewBluePrintTemplateFlow")
	public String viewBluePrintTemplateFlow(@QueryParam("cdFlowId")String cdFlowId){
		RestTemplate restUtil = new RestTemplate();
		String queryStr = "cdFlowId="+cdFlowId;
		return restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/viewBluePrintTemplateFlow?"+queryStr,String.class);
	}
	
	@POST
	@Path("importBlueprint")
	public String importBlueprint(FormDataMultiPart formData,@Context HttpServletRequest request){
		
		InputStream fileInputStream=null;
		OutputStream fileOutputStream = null;
		File localFile=null;
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String now = format.format(new Date());
			FormDataBodyPart fileBody= formData.getField("file");
			if(fileBody == null){
				return MessageHelper.wrap("result",false,"message","导入蓝图包为空，请选择蓝图包！");
			}
			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
			if(!filename.endsWith(".zip")){
				return MessageHelper.wrap("result",false,"message","蓝图包必须是.zip格式！");
			}
			fileInputStream= fileBody.getValueAs(InputStream.class);
			File targetFolder = new File(tmpdir+"im_bpTmp"+File.separator+now);
			targetFolder.mkdirs();
			String targetFile = targetFolder.getPath()+File.separator+filename;
			localFile=new File(targetFile);
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			fileInputStream=null;
			fileOutputStream=null;
			
			List<Map<String,Object>> templateList = new ArrayList();
			List<Map<String,Object>> blueprintflowList = new ArrayList();
			List<Map<String,Object>> resourceList = new ArrayList();
			List<Map<String,Object>> flowList = new ArrayList();
			List<Map<String,Object>> versionList = new ArrayList();
			List<Map<String,Object>> ftlList = new ArrayList();
			List<Map<String,Object>> packageList = new ArrayList();
			Map<String,String> zipMap = FileUtil.unZip(targetFile);
			
			ZipInputStream zin = null;
			try {
		        ZipFile zf = new ZipFile(targetFile,Charset.forName("GBK"));
		        InputStream ins = new BufferedInputStream(new FileInputStream(targetFile));  
		        zin = new ZipInputStream(ins,Charset.forName("GBK"));  
		        ZipEntry ze;  
		        while ((ze = zin.getNextEntry()) != null) {  
		            if (!ze.isDirectory()) {
		            	String fileName = ze.getName();
		            	long size = ze.getSize();  
		                System.out.println("file - " + fileName + " : " + size + " bytes");  
	    				try (InputStream in = zf.getInputStream(ze)) {
	    					if(fileName.endsWith(".zip")||fileName.endsWith(".war")||
	    							fileName.endsWith(".jar")||fileName.endsWith(".tar")||fileName.endsWith(".gz")){
	    						continue;
	    					}
	    					String templateString = org.apache.commons.io.IOUtils.toString(in,"UTF-8");
	    					Map<String,Object> subMap = JSON.parseObject(templateString, new TypeReference<Map<String,Object>>(){});
	    					Map<String,Object> temMap = new HashMap<String, Object>();
	    					if(fileName.endsWith(".template.json")){
	    						temMap.put(fileName, subMap);
	    						log.debug("导入的蓝图模板为: " + templateString);
	    						templateList.add(temMap);
	    					}else if(fileName.endsWith(".blueprintFlow.json")){
	    						log.debug("导入的蓝图模板流程为: " + templateString);
	    						
	    						//模型转换
	    						String flowInfoGroup = (String) subMap.get("FLOW_INFO_GROUP");
	    						log.info("== 导入前的flowinfogroup为 ：" + flowInfoGroup);
	    						BluePrint2Toolbar tool=new BluePrint2Toolbar();
	    						String flowInfo = tool.convertRuntimeInfo(flowInfoGroup);
	    						System.out.println("===导入后蓝图流程转换后为："+ flowInfo);
	    						subMap.put("FLOW_INFO", flowInfo);
	    						temMap.put(fileName, subMap);
	    						
	    						blueprintflowList.add(temMap);
	    					}else if(fileName.endsWith(".resource.json")){
	    						log.debug("导入的组件json为: " + templateString);
	    						resourceList.add(subMap);
	    					}else if(fileName.endsWith(".flow.json")){
	    						log.debug("导入的组件流程json为: " + templateString);
	    						flowList.add(subMap);
	    					}else if(fileName.endsWith(".version.json")){
	    						log.debug("导入的组件版本json为: " + templateString);
	    						versionList.add(subMap);
	    					}else if(fileName.endsWith(".ftl.json")){
	    						log.debug("导入的组件ftl为: " + templateString);
	    						ftlList.add(subMap);
	    					}else if(fileName.endsWith(".workpiece.json")){
	    						log.debug("导入的组件中工件描述为: " + subMap);
	    						packageList.add(subMap);
	    					}
	    				}
		            }  
		        }  
		        zin.closeEntry();  			
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(zin!=null)
					try {
						zin.closeEntry();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		
			if(templateList.isEmpty()){
				return MessageHelper.wrap("result",false,"message","文件内容或格式不正确");
			}
			RestTemplate restUtil = new RestTemplate();
			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
			message.add("template", JSON.toJSONString(templateList));
			message.add("blueprintFlow", JSON.toJSONString(blueprintflowList));
			message.add("resource", JSON.toJSONString(resourceList));
			message.add("flow", JSON.toJSONString(flowList));
			message.add("version", JSON.toJSONString(versionList));
			message.add("ftl", JSON.toJSONString(ftlList));
			message.add("package", JSON.toJSONString(packageList));
			message.add("zip", JSON.toJSONString(zipMap));
			User user = (User) request.getSession().getAttribute("user");
			message.add("userId", user.getName());
		    return restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprintTemplate/importBlueprint", message, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","导入的蓝图模板失败！");
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
				localFile.getParentFile().delete();
			}
		}
	}
	
	/*@GET
	@Path("exportBlueprint")
	public void exportBlueprint(@Context HttpServletResponse resp, @QueryParam ("ids") String ids,@Context HttpServletRequest request){

		User user = (User) request.getSession().getAttribute("user");
		String userId = user.getName();
		
		String result =  WSRestClient.getMasterWebTarget().path("blueprintTemplate").path("exportBlueprint")
							.queryParam("ids", ids).queryParam("userId", userId).request().get(String.class);
		Map<String,String> resultMap = JSONObject.parseObject(result,Map.class);
		if("true".equals(String.valueOf(resultMap.get("result")))){
			String filePath = resultMap.get("message");
			File file = new File(filePath);
			resp.setHeader("content-type", "application/octet-stream");
			resp.setContentType("application/octet-stream");
			try {
				resp.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
				
				OutputStream os = resp.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(os);
				
				InputStream is = null;
				
				is = new FileInputStream(File.separator+filePath);
				BufferedInputStream bis = new BufferedInputStream(is);
				
				int length = 0;
				byte[] temp = new byte[1 * 1024 * 10];
				
				while ((length = bis.read(temp)) != -1) {
					bos.write(temp, 0, length);
				}
				bos.flush();
				bis.close();
				bos.close();
				is.close();	
			} catch (Exception e1) {
				e1.printStackTrace();
			}

	  }
	}*/
}
