package com.dc.appengine.cloudui.ws.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.HttpRequestUtils;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.dcits.Common.entity.User;

//想办法实现cloudui统一转发请求，而不是重复编写这些方法
@Path("plugin")
public class PluginRestService {
	
	private static final Logger log = LoggerFactory.getLogger(PluginRestService.class);
	/*
	 * 上传插件包，注册插件，动态加载插件
	 * @RequestParam("pluginType") 插件类型
	 * @RequestParam("label") 插件标签
	 * @RequestParam("description") 插件描述
	 * @RequestParam("file") 插件文件流
	 */
	@SuppressWarnings("serial")
	@POST
	@Path("registPlugin")
	public String registPlugin(@FormDataParam("pluginName") String pluginName,@FormDataParam("label") String label,
			@FormDataParam("description") @DefaultValue("") String description,@FormDataParam("params") String params,
			@FormDataParam("preAction") @DefaultValue("") String preAction,@FormDataParam("postAction") @DefaultValue("")String postAction,
			@FormDataParam("invoke") @DefaultValue("") String invoke,
			@FormDataParam("agent") @DefaultValue("") String agent,
			FormDataMultiPart form,
			@Context HttpServletRequest request
			){
		InputStream fileInputStream=null;
		OutputStream fileOutputStream = null;
		File localFile=null;
		try {
			if(isEmpty(preAction)){
				return MessageHelper.wrap("result",false,"message","插件前处理方法为null！");
			}
			if(isEmpty(invoke)){
				return MessageHelper.wrap("result",false,"message","插件行为方法为null！");
			}
			if(isEmpty(agent)){
				return MessageHelper.wrap("result",false,"message","插件Agent方法为null！");
			}
			if(isEmpty(postAction)){
				return MessageHelper.wrap("result",false,"message","插件后处理方法为null！");
			}
			String tmpdir = System.getProperty("java.io.tmpdir");
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			String uuid = UUID.randomUUID().toString();
			File f = new File(tmpdir+uuid);
			f.mkdir();
			FormDataBodyPart fileBody= form.getField("file");
			if(fileBody == null){
				return MessageHelper.wrap("result",false,"message","插件包为空，请选择插件包！");
			}
			String filename =fileBody.getFormDataContentDisposition().getFileName(); 
			fileInputStream= fileBody.getValueAs(InputStream.class);
			String targetFile = tmpdir+File.separator+uuid+File.separator+filename;
			localFile=new File(targetFile);
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			fileInputStream=null;
			fileOutputStream=null;
			FileSystemResource resource = new FileSystemResource(new File(targetFile));
			Map<String, Object> param= new HashMap<String, Object>(){};
			Map<String, Object> paramsMap = new HashMap<String, Object>(){};
			paramsMap = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
			});
			param.put("pluginName", pluginName);
			param.put("label", JSON.toJSONString(new HashMap<>()));
			param.put("params", paramsMap);
			param.put("description", description);
			param.put("postAction", postAction);
			param.put("preAction", preAction);
			param.put("invoke", invoke);
			param.put("agent", agent);
			WebTarget webtarget = WSRestClient.getFrameWebTarget().path("plugin").path("registPlugin.wf");
			String url=webtarget.getUri()+"";
			RestTemplate restUtil = new RestTemplate();
			MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
			message.add("param", JSON.toJSONString(param));
			message.add("file", resource);
		    HttpHeaders headers =new HttpHeaders();
			//防止中文乱码
		    MediaType type = MediaType.parseMediaType("multipart/form-data; charset=UTF-8");
			headers.setContentType(type);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(message,headers);
			String returnMsg =  restUtil.postForObject(url,
		    		requestEntity, String.class);
			JSONObject result = JSONObject.parseObject(returnMsg);
			//保存标签
			if(result.getBoolean("result")&&label!=null&&!"".equals(label)){
				Form newForm= new Form();
				newForm.param("resourceId", pluginName);
				newForm.param("labels", label);
				returnMsg =  WSRestClient.getMasterWebTarget().path("labelManager")
						.path("getLabels4Plugin").request().
						post(Entity.entity(newForm,
								"application/x-www-form-urlencoded"), String.class);
			}
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			JSONObject jo = JSONObject.parseObject(returnMsg);
			final int operateResult = jo.getBoolean("result")?1:0;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "add");
					message.add("operateResult", operateResult);
					message.add("detail", "注册插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			return returnMsg;
		} catch (Exception e) {
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "add");
					message.add("operateResult", 0);
					message.add("detail", "注册插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			e.printStackTrace();
			log.error("插件注册失败", e);
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
				localFile.getParentFile().delete();
			}
		}
		return MessageHelper.wrap("result",false,"message","插件注册失败！");
	}
	
	@GET
	@Path("listPlugins")
	public String listPlugins(@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("pluginName") @DefaultValue("") String pluginName,
			@QueryParam("labelId") @DefaultValue("") String labelId,
			@QueryParam("sortName") @DefaultValue("") String sortName, 
		    @QueryParam("sortOrder") @DefaultValue("") String sortOrder){
		try {
			System.out.println("pluginName="+pluginName+" pageSize="+pageSize+ " pageNum="+pageNum);
			List<String> pluginNames = new ArrayList<>();
			String hasLabel="";
			if(!"".equals(labelId)){
				WebTarget target =null;
				if("none".equals(labelId)){
					target = WSRestClient.getMasterWebTarget().path("labelManager").path("getLabelReources").queryParam("code", 4);
					hasLabel="none";
				}else{
					target = WSRestClient.getMasterWebTarget().path("labelManager").path("getLabelReources").queryParam("labelId", labelId);
				}
				String labelRes = target.request().get(String.class);
				JSONArray list = JSONArray.parseArray(labelRes);
				for(int i=0;i<list.size();i++){
					Map map = (Map) list.get(i);
					pluginNames.add((String)map.get("resourceId"));
				}
			}
			String result = WSRestClient.getFrameWebTarget().path("plugin").
					path("listPlugins.wf").queryParam("pageSize", pageSize+"")
					.queryParam("pageNum", pageNum+"")
					.queryParam("pluginName", pluginName)
					.queryParam("pluginNames", JSONArray.toJSONString(pluginNames))
					.queryParam("hasLabel", hasLabel)
					.queryParam("sortName", sortName)
					.queryParam("sortOrder", sortOrder)
					.request().get(String.class);
			//获取插件的标签
			JSONObject page = JSONObject.parseObject(result);
			JSONArray rows = page.getJSONArray("rows");
			if(rows !=null && rows.size()>0){
				Form form= new Form();
				form.param("rows", rows.toJSONString());
				String newRows =  WSRestClient.getMasterWebTarget().path("labelManager")
						.path("getLabels4Plugin").request().
						post(Entity.entity(form,
								"application/x-www-form-urlencoded"), String.class);
				page.put("rows", JSONArray.parseArray(newRows));
				result = JSON.toJSONString(page);
			}
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件列表获取失败！", e.getMessage());
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	@GET
	@Path("getPlugin")
	public String getPlugin(@QueryParam("pluginName") @DefaultValue("") String pluginName){
		try {
			System.out.println(pluginName);
			String result = WSRestClient.getFrameWebTarget().path("plugin").
					path("getPlugin.wf")
					.queryParam("pluginName", pluginName).
					request().get(String.class);
			JSONArray rows = new JSONArray();
			rows.add(JSONObject.parse(result));
			Form form= new Form();
			form.param("rows", rows.toJSONString());
			String newRows =  WSRestClient.getMasterWebTarget().path("labelManager")
					.path("getLabels4Plugin").request().
					post(Entity.entity(form,
							"application/x-www-form-urlencoded"), String.class);
			rows = JSONArray.parseArray(newRows);
			result = JSON.toJSONString(rows.get(0));
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件获取失败！", e.getMessage());
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	
	@POST
	@Path("updatePlugin")
	public String updatePlugin(@FormDataParam("pluginName") String pluginName,
			@FormDataParam("params") String params,
			@FormDataParam("description") @DefaultValue("") String description,@FormDataParam("preAction") @DefaultValue("") String preAction,
			@FormDataParam("postAction") @DefaultValue("") String postAction,
			@FormDataParam("invoke") @DefaultValue("") String invoke,
			@FormDataParam("agent") @DefaultValue("") String agent,
			@Context HttpServletRequest request){
		try {
			if(isEmpty(preAction)){
				return MessageHelper.wrap("result",false,"message","插件前处理方法为null！");
			}
			if(isEmpty(invoke)){
				return MessageHelper.wrap("result",false,"message","插件行为方法为null！");
			}
			if(isEmpty(agent)){
				return MessageHelper.wrap("result",false,"message","插件Agent方法为null！");
			}
			if(isEmpty(postAction)){
				return MessageHelper.wrap("result",false,"message","插件后处理方法为null！");
			}
			Map<String, Object> paramsMap = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
			});
			Form form = new Form();
			form.param("pluginName", pluginName);
			form.param("params", JSON.toJSONString(paramsMap));
			form.param("label", JSON.toJSONString(new HashMap<>()));
			form.param("description", description);
			form.param("postAction", postAction);
			form.param("preAction", preAction);
			form.param("invoke", invoke);
			form.param("agent", agent);
			String result =  WSRestClient.getFrameWebTarget()
					.path("plugin").path("updatePlugin.wf")
					.request().
					post(Entity.entity(form,
							"application/x-www-form-urlencoded;charset=utf-8"),
							String.class);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			JSONObject jo = JSONObject.parseObject(result);
			final int operateResult = jo.getBoolean("result")?1:0;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "update");
					message.add("operateResult", operateResult);
					message.add("detail", "更新插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			return result;
		} catch (Exception e) {
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "update");
					message.add("operateResult", 0);
					message.add("detail", "更新插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插件更新失败", e);
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	@DELETE
	@Path("deletePlugin/{pluginName}")
	public String deletePlugin(@PathParam("pluginName") String pluginName,@Context HttpServletRequest request){
		try {
			String result =  WSRestClient.getFrameWebTarget()
					.path("plugin").path("deletePlugin").path(pluginName+".wf")
					.request().delete(String.class);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			JSONObject jo = JSONObject.parseObject(result);
			final int operateResult = jo.getBoolean("result")?1:0;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "delete");
					message.add("operateResult", operateResult);
					message.add("detail", "删除插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			return result;
		} catch (Exception e) {
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String plugin = pluginName;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "插件管理");
					message.add("resourceName", plugin);
					message.add("operateType", "delete");
					message.add("operateResult", 0);
					message.add("detail", "删除插件 :"+plugin);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插件删除失败", e);
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	public static Boolean isEmpty(Object param){
		if(param instanceof String){
			if(param ==null || "".equals(param)){
				return true;
			}else{
				return false;
			}
		}else if(param instanceof Map){
			if(param ==null || ((Map)param).isEmpty()){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
	
	public static void main(String[]args){
//		JSONArray test = new JSONArray();
//		List test1 = new ArrayList<>();
//		Map map = new HashMap<>();
//		map.put("test", "111");
//		Map map1 = new HashMap<>();
//		map1.put("test", "111");
//		test1.add(map);
//		test1.add(map1);
//		String test2 =JSON.toJSONString(test1);
//		System.out.println(test2);
		List<String> pluginNames = new ArrayList<>();
		pluginNames.add("CMD");
		
	}
}
