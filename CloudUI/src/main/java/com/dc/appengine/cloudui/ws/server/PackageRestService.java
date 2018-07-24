package com.dc.appengine.cloudui.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.FileUtil;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.dcits.Common.entity.User;

@Path("packageResource")
public class PackageRestService {
	
	private static final Logger log = LoggerFactory.getLogger(PackageRestService.class);
	/*
	 * 上传工件包
	 * @RequestParam("file") 工件包文件流
	 */
//	@POST
//	@Path("upload")
//	public String uploadPackage(FormDataMultiPart formData){
//		
//		InputStream fileInputStream=null;
//		OutputStream fileOutputStream = null;
//		File localFile=null;
//		try {
//			String tmpdir = System.getProperty("java.io.tmpdir");
//			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
//			String uuid = UUID.randomUUID().toString();
//			File f = new File(tmpdir+"packages"+File.separator+uuid);
//			f.mkdir();
//			FormDataBodyPart fileBody= formData.getField("file");
//			if(fileBody == null){
//				return MessageHelper.wrap("result",false,"message","工件包为空，请选择工件包！");
//			}
//			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
//			if(!filename.endsWith(".zip")){
//				return MessageHelper.wrap("result",false,"message","工件包必须是.zip格式！");
//			}
//			fileInputStream= fileBody.getValueAs(InputStream.class);
//			String targetFile = tmpdir+"packages"+File.separator+uuid+File.separator+filename;
//			localFile=new File(targetFile);
//			fileOutputStream = new FileOutputStream(localFile);
//			IOUtils.copy(fileInputStream, fileOutputStream);
//			IOUtils.closeQuietly(fileOutputStream);
//			IOUtils.closeQuietly(fileInputStream);
//			fileInputStream=null;
//			fileOutputStream=null;
//			FileSystemResource resource = new FileSystemResource(new File(targetFile));  
//			String description= formData.getField("description")==null ? "":formData.getField("description").getValue();
//			//String name= formData.getField("name")==null ? "":formData.getField("name").getValue();
//			RestTemplate restUtil = new RestTemplate();
//			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
//			message.add("description", description);
//			message.add("name", filename);
//			message.add("file", resource);
//		    return restUtil.postForObject(MasterEnv.MASTER_REST+"/packageResource/upload", message, String.class);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log.error(" 新增工件包失败", e);
//			return MessageHelper.wrap("result",false,"message","新增工件包失败！");
//		}finally{
//			IOUtils.closeQuietly(fileOutputStream);
//			IOUtils.closeQuietly(fileInputStream);
//			if(localFile !=null && localFile.exists()){
//				localFile.delete();
//				localFile.getParentFile().delete();
//			}
//		}
//	}
	
//	@DELETE
//	@Path("deletePackage")
//	public String deletePackage(@QueryParam("id") String id){
//		
//		return WSRestClient.getMasterWebTarget().path("packageResource").path("deletePackage")
//												.queryParam("id", id).request()
//												.delete(String.class);
//	}
	
//	@GET
//	@Path("listPackagesByPage")
//	public String listPackagesByPage(
//			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
//			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
//			@QueryParam("name")  @DefaultValue("") String name){
//		
//		return WSRestClient.getMasterWebTarget().path("packageResource").path("listPackagesByPage")
//												.queryParam("name", name)
//												.queryParam("pageSize", pageSize)
//												.queryParam("pageNum", pageNum)
//												.request().get(String.class);
//	}
	
//	@GET
//	@Path("download")
//	public void downloadPackage(@Context HttpServletResponse resp,@QueryParam ("id") String id){
//
//		String result =  WSRestClient.getMasterWebTarget().path("packageResource").path("download")
//							.queryParam("id", id).request().get(String.class);
//		Map<String,String> resultMap = JSONObject.parseObject(result,Map.class);
//		if("true".equals(String.valueOf(resultMap.get("result")))){
//			String filePath = resultMap.get("message");
//			File file = new File(filePath);
//			resp.setHeader("content-type", "application/octet-stream");
//			resp.setContentType("application/octet-stream");
//			try {
//				resp.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
//				
//				OutputStream os = resp.getOutputStream();
//				BufferedOutputStream bos = new BufferedOutputStream(os);
//				
//				InputStream is = null;
//				
//				is = new FileInputStream(File.separator+filePath);
//				BufferedInputStream bis = new BufferedInputStream(is);
//				
//				int length = 0;
//				byte[] temp = new byte[1 * 1024 * 10];
//				
//				while ((length = bis.read(temp)) != -1) {
//					bos.write(temp, 0, length);
//				}
//				bos.flush();
//				bis.close();
//				bos.close();
//				is.close();	
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//
//	  }
//	}
	
	@POST
	@Path("importPackage")
	public String importPackage(FormDataMultiPart formData,@Context HttpServletRequest request){
		
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
				return MessageHelper.wrap("result",false,"message","导入工件为空，请选择工件！");
			}
			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
			if(!filename.endsWith(".zip")){
				return MessageHelper.wrap("result",false,"message","工件包必须是.zip格式！");
			}
			fileInputStream= fileBody.getValueAs(InputStream.class);
			File targetFolder = new File(tmpdir+"im_pkTmp"+File.separator+now);
			targetFolder.mkdirs();
			String targetFile = targetFolder.getPath()+File.separator+filename;
			localFile=new File(targetFile);
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			fileInputStream=null;
			fileOutputStream=null;
			
			
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
		                System.err.println("file - " + fileName + " : " + size + " bytes");  
	    				try (InputStream in = zf.getInputStream(ze)) {
	    					if(fileName.endsWith(".zip")){//跳过zip包
	    						continue;
	    					}
	    					String packageString = org.apache.commons.io.IOUtils.toString(in,"UTF-8");
	    					List<Map<String,Object>> subList = JSON.parseObject(packageString, new TypeReference<List<Map<String,Object>>>(){});
	    					if(fileName.endsWith("workpiece.json")){
	    						log.debug("导入的工件为: " + packageString);
	    						packageList.addAll(subList);
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
		
			RestTemplate restUtil = new RestTemplate();
			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
			message.add("packageList", JSON.toJSONString(packageList));
			message.add("zipMap", zipMap);
			User user = (User) request.getSession().getAttribute("user");
			message.add("userId", user.getName());
		    return restUtil.postForObject(MasterEnv.MASTER_REST+"/packageResource/importPackage", message, String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","导入工件包失败！");
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
//				localFile.getParentFile().delete();
			}
		}
	}
	
//	@GET
//	@Path("exportPackage")
//	public void exportPackage(@Context HttpServletResponse resp,
//							  @QueryParam ("ids") String ids){
//
//		String result =  WSRestClient.getMasterWebTarget().path("packageResource").path("exportPackage")
//							.queryParam("ids", ids).request().get(String.class);
//		Map<String,String> resultMap = JSONObject.parseObject(result,Map.class);
//		if("true".equals(String.valueOf(resultMap.get("result")))){
//			String filePath = resultMap.get("message");
//			File file = new File(filePath);
//			resp.setHeader("content-type", "application/octet-stream");
//			resp.setContentType("application/octet-stream");
//			try {
//				resp.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
//				
//				OutputStream os = resp.getOutputStream();
//				BufferedOutputStream bos = new BufferedOutputStream(os);
//				
//				InputStream is = null;
//				
//				is = new FileInputStream(File.separator+filePath);
//				BufferedInputStream bis = new BufferedInputStream(is);
//				
//				int length = 0;
//				byte[] temp = new byte[1 * 1024 * 10];
//				
//				while ((length = bis.read(temp)) != -1) {
//					bos.write(temp, 0, length);
//				}
//				bos.flush();
//				bis.close();
//				bos.close();
//				is.close();	
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//
//	  }
//	}
}
