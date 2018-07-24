package com.dc.appengine.cloudui.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.FileUtil;
import com.dc.appengine.cloudui.utils.MD5Util;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.utils.Utils;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.dcits.Common.entity.User;

//想办法实现cloudui统一转发请求，而不是重复编写这些方法
@Path("resource")
public class ResourceRestService {
	
	private static final Logger log = LoggerFactory.getLogger(ResourceRestService.class);
	
	@POST
	@Path("saveComponentFlow")
	public String saveComponentFlow(
			@FormParam("blueprint_info") String blueprint_info
			,@FormParam("flowType") String flowType){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_info", blueprint_info);
		requestEntity.add("flowType", flowType);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveComponentFlow",
						requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("listResource")
	public String listResource(
			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("resourceName") String resourceName, 
			@QueryParam("resourceType") String resourceType,
			@QueryParam("sortName") @DefaultValue("") String sortName, 
			@QueryParam("sortOrder") @DefaultValue("") String sortOrder
			){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("listResource")
				.queryParam("pageSize", pageSize+"")
				.queryParam("pageNum", pageNum+"")
				.queryParam("resourceName", resourceName)
				.queryParam("resourceType", resourceType)
				.queryParam("sortName", sortName)
				.queryParam("sortOrder", sortOrder)
				.request().get(String.class);
	}
	
	@GET
	@Path("resourceDetail")
	public String resourceDetail(@QueryParam("resourceName") String resourceName){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("resourceDetail")
				.queryParam("resourceName", resourceName)
				.request().get(String.class);
	}
	
	@GET
	@Path("getResourceVersionDetail")
	public String getResourceVersionDetail(@QueryParam("resourceVersionId") String resourceVersionId){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("getResourceVersionDetail")
				.queryParam("resourceVersionId", resourceVersionId)
				.request().get(String.class);
	}
	
	@GET
	@Path("listResourceVersion")
	public String listResourceVersion(@QueryParam("resourceId") String resourceId){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("listResourceVersion")
				.queryParam("resourceId", resourceId)
				.request().get(String.class);
	}
	
	@GET
	@Path("getInputAndOutput")
	public String getInputAndOutput(@QueryParam("resourceVersionId") String resourceVersionId){
		//根据传入的资源版本id获取这个资源的输入输出
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("getInputAndOutput")
				.queryParam("resourceVersionId", resourceVersionId)
				.request().get(String.class);
	}
	
	@GET
	@Path("listAllResource")
	public String listAllResource(@QueryParam("resourceName") String resourceName,
			@QueryParam("labelId") String labelId){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("listAllResource").queryParam("resourceName", resourceName)
				.queryParam("labelId", labelId)
				.request().get(String.class);
	}
	
	@POST
	@Path("registResource")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String registResource(
			@FormParam("resourceType") @DefaultValue("") String resourceType,
			@FormParam("resourceName") String resourceName,
			@FormParam("resourceDesc") @DefaultValue("") String resourceDesc,
			@FormParam("registryId") String registryId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> message = new LinkedMultiValueMap<>();
		message.add("resourceType", resourceType);
		message.add("resourceName", resourceName);
		message.add("resourceDesc", resourceDesc);
		message.add("registryId", registryId);
		HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	    HttpEntity<MultiValueMap<String,String>> requestEntity = new HttpEntity<>(message, requestHeaders);
	    String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/registResource", requestEntity, String.class);
	    return result;
	}
	
	@POST
	@Path("uploadFile")
	public String uploadFile(
			@FormDataParam("file") InputStream fileInputStream,  
	        @FormDataParam("file") FormDataContentDisposition disposition){
		String tmpdir = System.getProperty("java.io.tmpdir");
		tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
		String uuid = UUID.randomUUID().toString();
		File f = new File(tmpdir+uuid);
		f.mkdir();
		String filename = disposition.getFileName();
		String targetFile = tmpdir+File.separator+uuid+File.separator+filename;
		Map<Object,Object> result = new HashMap<>();
		result.put("fileid", uuid+File.separator+filename);
		try(OutputStream out = new FileOutputStream(new File(targetFile))){
			IOUtils.copy(fileInputStream, out);
			byte[] b = Utils.readFileInZip(targetFile, "flows.properties");
			String s = new String(b);
			if(s.equals("error")){
				//文件有误
				result.put("result", false);
				result.put("message", "zip文件有错误");
			}else if(s.equals("notfound")){
				//没有包含flows.properties
				result.put("result", true);
				result.put("message", "zip中无flows.properties文件");
			}else{
				//包含flows.properties
				result.put("result", true);
				result.put("message", "ok");
				Properties flows = new Properties();
				flows.load(new StringReader(s));
				result.putAll(flows);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fileInputStream);
		}
		return JSON.toJSONString(result);
	}
	
	@GET
	@Path("listResourceVersionByPage")
	public String listResourceVersionByPage(
			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("resourceId") String resourceId,
			@QueryParam("versionName") String versionName){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("listResourceVersionByPage")
				.queryParam("resourceId", resourceId)
				.queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.queryParam("versionName", versionName)
				.request().get(String.class);
	}
	
	@POST
	@Path("addResourceVersion")
	public String addResourceVersion(
			@FormParam("deploy") @DefaultValue("") String deploy,
			@FormParam("start") @DefaultValue("") String start,
			@FormParam("stop") @DefaultValue("") String stop,
			@FormParam("destroy") @DefaultValue("") String destroy,
			@FormParam("fileid") String fileid,
			@FormParam("resourceId") String resourceId,
			@FormParam("versionDesc") String versionDesc,
			@FormParam("versionName") String versionName){
		RestTemplate restUtil = new RestTemplate();
		Map<String,String> map = new HashMap<>();
		map.put("deploy", deploy);
		map.put("start", start);
		map.put("stop", stop);
		map.put("destroy", destroy);
		HttpHeaders headers =new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(map),headers);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveComponentFlows",
				entity, String.class);
		Map<String,Object> r = JSON.parseObject(result, new TypeReference<Map<String,Object>>(){});
		boolean b = (boolean) r.get("result");
		if(b){
			String tmpdir = System.getProperty("java.io.tmpdir");
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			String zipFile = tmpdir+fileid;
			//替换文件，保存版本
//			Properties p = new Properties();
//			p.setProperty("deploy",r.get("deploy").toString());
//			p.setProperty("start",r.get("start").toString());
//			p.setProperty("stop",r.get("stop").toString());
//			p.setProperty("destroy",r.get("destroy").toString());
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			try {
//				p.store(baos, new Date().toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			boolean cover = Utils.coverFileInZip(zipFile, "flows.properties", baos.toByteArray());
//			if(!cover){
//				return MessageHelper.wrap("result",false,"message","替换flows文件出错");
//			}
//			else
			{
				//上传文件到ftp，记录路径，然后新增版本
				FileSystemResource resource = new FileSystemResource(new File(zipFile));  
			    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
			    param.add("file", resource);
			    String resp = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/uploadFile"
			    		,param, String.class);
			    Map<String,Object> response = JSON.parseObject(resp,new TypeReference<Map<String,Object>>(){});
			    boolean uploadSuccess = (boolean) response.get("result");
			    if(uploadSuccess)
			    {
			    	String ftpLocation = (String) response.get("file");
			    	MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
			    	mvm.add("file", ftpLocation);
			    	mvm.add("resourceId",resourceId);
			    	mvm.add("versionDesc", versionDesc);
			    	mvm.add("versionName", versionName);
			    	mvm.add("registryId", "3");
			    	mvm.add("deployId", "" + r.get("deploy"));
			    	mvm.add("startId", "" + r.get("start"));
			    	mvm.add("stopId", "" + r.get("stop"));
			    	mvm.add("destroyId", "" + r.get("destroy"));
			    	return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveResourceVersion"
			    			,mvm, String.class);
			    }
			    else{
			    	return MessageHelper.wrap("result",false,"message","上传ftp出错");
			    }
			}
		}else{
			String msg = (String) r.get("msg");
			return MessageHelper.wrap("result",false,"message",msg);
		}
	}
	
	@POST
	@Path("getResourceVersionFlows")
	public String getResourceVersionFlows(
			@FormParam("resourceVersionId") String resourceVersionId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("resourceVersionId", resourceVersionId);
		HttpHeaders headers =new HttpHeaders();
	    //防止中文乱码
	    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
	    return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/getResourceVersionFlows",
	    		requestEntity, String.class);
	}
	
	@POST
	@Path("updateResourceVersionFlows")
	public String updateResourceVersionFlows(
			@FormParam("deploy") @DefaultValue("") String deploy,
			@FormParam("start") @DefaultValue("") String start,
			@FormParam("stop") @DefaultValue("") String stop,
			@FormParam("destroy") @DefaultValue("") String destroy,
			@FormParam("resourceVersionId") String resourceVersionId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		Map<String,String> map = new HashMap<String, String>();
		map.put("deploy", deploy);
		map.put("start", start);
		map.put("stop", stop);
		map.put("destroy", destroy);
		body.add("resourceVersionFlows", JSON.toJSONString(map));
		body.add("resourceVersionId", resourceVersionId);
		HttpHeaders headers =new HttpHeaders();
	    //防止中文乱码
	    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
	    return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/updateResourceVersionFlows",
	    		requestEntity, String.class);
	}
	
	@DELETE
	@Path("deleteResourceVersion")
	public String deleteResourceVersion(@QueryParam("versionId") String id){
		return WSRestClient.getMasterWebTarget()
				.path("resource")
				.path("deleteResourceVersion")
				.queryParam("id", id).request().delete(String.class);
	}
	
	@DELETE
	@Path("deleteResource")
	public String deleteResource(@QueryParam("resourceId") String resourceId){
		return WSRestClient.getMasterWebTarget()
				.path("resource")
				.path("deleteResource")
				.queryParam("resourceId", resourceId).request().delete(String.class);
	}
	
	@POST
	@Path("getAppSubflowVars")
	public String getAppSubflowVars(
			@FormParam("appSubflowId") String appSubflowId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("appSubflowId", appSubflowId);
		HttpHeaders headers =new HttpHeaders();
	    //防止中文乱码
	    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
	    return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/getAppSubflowVars",
	    		requestEntity, String.class);
	}
	
	@POST
	@Path("addNewResourceVersion")
	public String addNewResourceVersion(
			@FormParam("fileid") String fileid,
			@FormParam("resourceId") String resourceId,
			@FormParam("versionDesc") String versionDesc,
			@FormParam("versionName") String versionName){
		String allVersions = this.listResourceVersion(resourceId);
		List<Map<String,Object>> versions = JSON.parseObject(allVersions, new TypeReference<List<Map<String,Object>>>(){});
		for(Map<String,Object> version:versions){
			if(version.get("versionName").equals(versionName)){
				//版本名称重复
				return MessageHelper.wrap("result",false,"message","该版本已存在");
			}
		}
		try{
			RestTemplate restUtil = new RestTemplate();
			String tmpdir = System.getProperty("java.io.tmpdir");
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			String zipFile = tmpdir+fileid;
			//上传文件到ftp，记录路径，然后新增版本
			File zip = new File(zipFile);
			FileSystemResource resource = new FileSystemResource(zip);  
		    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		    param.add("file", resource);
		    String resp = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/uploadFile"
		    		,param, String.class);
		    Map<String,Object> response = JSON.parseObject(resp,new TypeReference<Map<String,Object>>(){});
		    boolean uploadSuccess = (boolean) response.get("result");
		    String md5 = MD5Util.md5(new File(zipFile));
		    if(uploadSuccess){
		    	String ftpLocation = (String) response.get("file");
		    	MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
		    	mvm.add("file", ftpLocation);
		    	mvm.add("resourceId",resourceId);
		    	mvm.add("versionDesc", versionDesc);
		    	mvm.add("versionName", versionName);
		    	mvm.add("registryId", "3");
		    	mvm.add("md5", md5);
		    	return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveNewResourceVersion",mvm, String.class);
		    }
		    else{
		    	return MessageHelper.wrap("result",false,"message","上传ftp出错");
		    }
		}catch(Exception e){
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	//更新组件版本的组件包
	@POST
	@Path("updateResourceVersion")
	public String updateResourceVersion(
			@FormParam("resourceVersionId") String resourceVersionId,
			@FormParam("fileid") String fileid) throws Exception{
		RestTemplate restUtil = new RestTemplate();
		String tmpdir = System.getProperty("java.io.tmpdir");
		tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
		String zipFile = tmpdir+fileid;
		//上传文件到ftp，记录路径，然后新增版本
		FileSystemResource resource = new FileSystemResource(new File(zipFile));  
	    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
	    param.add("file", resource);
	    String resp = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/uploadFile"
	    		,param, String.class);
	    Map<String,Object> response = JSON.parseObject(resp,new TypeReference<Map<String,Object>>(){});
	    boolean uploadSuccess = (boolean) response.get("result");
	    String md5 = MD5Util.md5(new File(zipFile));
	    if(uploadSuccess){
	    	String ftpLocation = (String) response.get("file");
	    	MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
	    	mvm.add("file", ftpLocation);
	    	mvm.add("resourceVersionId", resourceVersionId);
	    	mvm.add("md5", md5);
	    	return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/updateResourceVersion"
	    			,mvm, String.class);
	    }else{
	    	return MessageHelper.wrap("result",false,"message","上传ftp出错");
	    }
	}
	
	@POST
	@Path("addNewResourceVersionFlow")
	public String addNewResourceVersionFlow(
			@FormParam("flowType") @DefaultValue("") String flowType,
			@FormParam("flowInfo") @DefaultValue("") String flowInfo,
			@FormParam("flowName") @DefaultValue("") String flowName,
			@FormParam("versionId") String versionId){
		RestTemplate restUtil = new RestTemplate();
		Map<String,String> map = new HashMap<>();
		map.put("flowType", flowType);
		map.put("flowInfo", flowInfo);
		map.put("flowName", flowName);
		map.put("versionId", versionId);
		HttpHeaders headers =new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(map),headers);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveNewComponentFlow",
				entity, String.class);
		return result;
	}
	
	@POST
	@Path("getNewVersionOperations")
	public String getNewVersionOperations(
			@FormParam("versionId") String versionId){
		RestTemplate restUtil = new RestTemplate();
		Map<String,String> map = new HashMap<>();
		map.put("versionId", versionId);
		HttpHeaders headers =new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(map),headers);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/getNewVersionOperations",
				entity, String.class);
		return result;
	}
	
	@POST
	@Path("getFlowInfoByVersionOperation")
	public String getFlowInfoByVersionOperation(
			@FormParam("flowId") String flowId){
		RestTemplate restUtil = new RestTemplate();
		Map<String,String> map = new HashMap<>();
		map.put("flowId", flowId);
		HttpHeaders headers =new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(map),headers);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/getFlowInfoByVersionOperation",
				entity, String.class);
		return result;
	}
	
	@POST
	@Path("updateFlowInfoByFlowId")
	public String updateFlowInfoByFlowId(
			@FormParam("flowId") String flowId,
			@FormParam("flowInfo") String flowInfo){
		RestTemplate restUtil = new RestTemplate();
		Map<String,String> map = new HashMap<>();
		map.put("flowId", flowId);
		map.put("flowInfo", flowInfo);
		HttpHeaders headers =new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(map),headers);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/updateFlowInfoByFlowId",
				entity, String.class);
		return result;
	}
	
	@POST
	@Path("saveScript")
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	public String saveScript(@Context HttpServletRequest request,
			FormDataMultiPart formData
			){
		InputStream fileInputStream=null;
		OutputStream fileOutputStream = null;
		File localFile=null;
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
//			String tmpdir = "F:/test/";
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			String uuid = UUID.randomUUID().toString();
			File f = new File(tmpdir+uuid);
			f.mkdir();
			FormDataBodyPart fileBody= formData.getField("file");
			if(fileBody == null){
				return MessageHelper.wrap("result",false,"message","资源包为空，请选择资源包！");
			}
			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
			fileInputStream= fileBody.getValueAs(InputStream.class);
			String targetFile = tmpdir+File.separator+uuid+File.separator+filename;
			Map<Object,Object> result = new HashMap<>();
			String filePath= uuid+File.separator+filename;
			localFile=new File(targetFile);
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			fileInputStream=null;
			fileOutputStream=null;
			FileSystemResource resource = new FileSystemResource(new File(targetFile));  
			String description= formData.getField("description")==null ? "":formData.getField("description").getValue();
			String name= formData.getField("name")==null ? "":formData.getField("name").getValue();
			RestTemplate restUtil = new RestTemplate();
			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
			message.add("description", description);
			message.add("name", name);
			message.add("file", resource);
			User user = (User) request.getSession().getAttribute("user");
			message.add("userId", user.getName());
		    return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/saveScript", message, String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(" 新增脚本包失败", e);
			return MessageHelper.wrap("result",false,"message","新增脚本包失败！");
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
				localFile.getParentFile().delete();
			}
		}
	}
	
	@DELETE
	@Path("deleteScriptResource")
	public String deleteScriptResource(@QueryParam("id") String id,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		return WSRestClient.getMasterWebTarget()
				.path("resource")
				.path("deleteScriptResource")
				.queryParam("id", id).queryParam("userId", user.getName()).request().delete(String.class);
	}
	
	@GET
	@Path("listScriptResourceByPage")
	public String listScriptResourceByPage(
			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("name")  @DefaultValue("") String name){
		return WSRestClient.getMasterWebTarget().path("resource")
				.path("listScriptResourceByPage")
				.queryParam("name", name)
				.queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.request().get(String.class);
	}
	
	@POST
	@Path("updateResourceVersionStatus")
	public String updateResourceVersionStatus(
			@FormParam("resourceVersionId")  String resourceVersionId,
			@FormParam("status")  String status){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
		mvm.add("resourceVersionId", resourceVersionId);
		mvm.add("status", status);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/updateResourceVersionStatus",
				mvm, String.class);
		return result;
	}
	
	@POST
	@Path("importResource")
	public String importResources(FormDataMultiPart formData,@Context HttpServletRequest request){
		
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
				return MessageHelper.wrap("result",false,"message","导入组件包为空，请选择组件包！");
			}
			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
			if(!filename.endsWith(".zip")){
				return MessageHelper.wrap("result",false,"message","组件包必须是.zip格式！");
			}
			fileInputStream= fileBody.getValueAs(InputStream.class);
			File targetFolder = new File(tmpdir+"im_resTmp"+File.separator+now);
			targetFolder.mkdirs();
			String targetFile = targetFolder.getPath()+File.separator+filename;
			localFile=new File(targetFile);
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			fileInputStream=null;
			fileOutputStream=null;
			
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
	    					if(fileName.endsWith(".zip")){
	    						continue;
	    					}
	    					String resourceString = org.apache.commons.io.IOUtils.toString(in,"UTF-8");
	    					Map<String,Object> subMap = JSON.parseObject(resourceString, new TypeReference<Map<String,Object>>(){});
	    					if(fileName.endsWith(".resource.json")){
	    						log.debug("导入的组件json为: " + resourceString);
	    						resourceList.add(subMap);
	    					}else if(fileName.endsWith(".flow.json")){
	    						log.debug("导入的组件流程json为: " + resourceString);
	    						flowList.add(subMap);
	    					}else if(fileName.endsWith(".version.json")){
	    						log.debug("导入的组件版本json为: " + resourceString);
	    						versionList.add(subMap);
	    					}else if(fileName.endsWith(".ftl.json")){
	    						log.debug("导入的组件ftl为: " + resourceString);
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
			if(resourceList.isEmpty()){
				return MessageHelper.wrap("result",false,"message","文件内容或格式不正确");
			}
			RestTemplate restUtil = new RestTemplate();
			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
			message.add("resource", JSON.toJSONString(resourceList));
			message.add("flow", JSON.toJSONString(flowList));
			message.add("version", JSON.toJSONString(versionList));
			message.add("ftl", JSON.toJSONString(ftlList));
			message.add("package", JSON.toJSONString(packageList));
			message.add("zip", JSON.toJSONString(zipMap));
			User user = (User) request.getSession().getAttribute("user");
			message.add("userId", user.getName());
		    return restUtil.postForObject(MasterEnv.MASTER_REST+"/resource/importResources", message, String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","导入组件包失败！");
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
				localFile.getParentFile().delete();
			}
		}
	}
	
/*	@GET
	@Path("exportResource")
	public void exportResources(@Context HttpServletResponse resp,
								@QueryParam ("ids") String ids){
		
		String result =  WSRestClient.getMasterWebTarget().path("resource").path("exportResource")
							.queryParam("ids", ids).request().get(String.class);
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
	
	public static void main(String[]args){
		try {
			RestTemplate restUtil = new RestTemplate();
//		Map<String,String> map = new HashMap<>();
			MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
			mvm.add("resourceVersionId", "e7e0b0d0-0429-44a6-8616-761b755fdb19");
			mvm.add("status", "01");
			String url= "http://10.1.108.33:5091/masterl/ws";
			String result = restUtil.postForObject(url+"/resource/updateResourceVersionStatus",
					mvm, String.class);
			System.out.println(result);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
