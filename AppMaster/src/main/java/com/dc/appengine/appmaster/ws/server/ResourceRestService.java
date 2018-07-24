package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Resource;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.entity.VersionFtl;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.ILabelsService;
import com.dc.appengine.appmaster.service.IPackageService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.ITSMBatchReleasePool;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.Utils;
import com.dcits.Common.entity.User;

@Controller
@RequestMapping("/ws/resource")
public class ResourceRestService {
	
	@Autowired
	@Qualifier("resourceService")
	IResourceService resourceService;
	
	@Autowired
	@Qualifier("packageService")
	IPackageService packageService;
	
	@Autowired
	@Qualifier("auditService")
	IAudit auditService;
	
	@Value(value = "${ftp.url}")
	String url;
	
	@Autowired
	@Qualifier("labelsService")
	ILabelsService labelsService;
	
	@Value(value = "${ftp.port}")
	int port;
	@Value(value = "${ftp.user}")
	String user;
	@Value(value = "${ftp.pwd}")
	String pwd;
	
	@Autowired
	@Qualifier("userService")
	IUserService userService;
	
	@Autowired
	@Qualifier("blueprintService")
	IBlueprintService blueprintService;
	
	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public String test(){
		Map<String,List<Object>> message = new HashMap<>();
		List<Object> list = new ArrayList<>();
		list.add("dffdfdf");
		list.add("dffdfdfdfdfdf");
		message.put("hello", list);
		RestTemplate rest = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Map<String,List<Object>>> requestEntity = new HttpEntity<>(message, requestHeaders);
	    String result = rest.postForObject("http://127.0.0.1:5091/masterl/ws/resource/testJson", requestEntity, String.class);
		return result;
	}
	
	@RequestMapping(value = "testJson", method = RequestMethod.POST,consumes = {"text/plain", "application/*"})
	@ResponseBody
	public String testJson(@RequestBody String message){
		System.out.println(message);
		return "success";
	}
	
	@RequestMapping(value = "saveComponentFlow", method = RequestMethod.POST)
	@ResponseBody
	public String saveComponentFlow(@RequestParam("blueprint_info") String  blueprint_info,
			@RequestParam("flowType") String flowType){
		BluePrint bp = JSON.parseObject(blueprint_info,BluePrint.class);
		long flowId = resourceService.saveFlow(bp, flowType);
		if(flowId == 0){
			return MessageHelper.wrap("result", false, "message", "流程保存失败");
		}else{
			return MessageHelper.wrap("result", true, "message", 
					"流程保存成功","flowId",flowId,"flowType",flowType);
		}
	}
	
	@RequestMapping(value = "saveComponentFlows", method = RequestMethod.POST,consumes = {"text/plain", "application/*"})
	@ResponseBody
	public String saveComponentFlows(@RequestBody String message){
		Map<String,Object> result = new HashMap<>();
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			Map<String,Long> save = resourceService.saveComponentFlows(map);
			result.putAll(save);
			result.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		return JSON.toJSONString(result);
		
	}
	
	@RequestMapping(value = "testRestTemplate", method = RequestMethod.POST)
	@ResponseBody
	public String testRestTemplate(@RequestParam("aaaa") String resourceType){
		return resourceType;
	}
	

	@RequestMapping(value = "registResource", method = RequestMethod.POST,consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	@ResponseBody
	public String registResource(
			@RequestParam("resourceType") String resourceType,
			@RequestParam("resourceName") String resourceName, 
			@RequestParam("registryId") int registryId,
			@RequestParam(name="resourceDesc",required=false,defaultValue="resourceDesc") String resourceDesc) {
		String id = UUID.randomUUID().toString();
		Resource r = new Resource(id, resourceName, resourceType, resourceDesc);
		boolean flag = resourceService.registResource(r);
		if(flag){
			return MessageHelper.wrap("result", true, "message", "资源注册成功","resourceId",id);

		}
		return MessageHelper.wrap("result", false, "message", "资源名称已存在，请重新注册");
	}
	
	@RequestMapping(value = "listResource", method = RequestMethod.GET)
	@ResponseBody
	public String listResource(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="resourceName",required = false) String resourceName, 
			@RequestParam(name="resourceType",required = false) String resourceType,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		Map<String,Object> condition = new HashMap<>();
		condition.put("resourceName", resourceName);
		condition.put("resourceType", resourceType);
		condition.put("sortName", SortUtil.getColunmName("component", sortName));
		condition.put("sortOrder", sortOrder);
		Page page = resourceService.listResource(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "saveResourceVersion", method = RequestMethod.POST)
	@ResponseBody
	public String saveResourceVersion(
			@RequestParam("resourceId") String resourceId,
			@RequestParam("versionName") String versionName,
			@RequestParam("deployId") String deployId,
			@RequestParam("startId") String startId,
			@RequestParam("stopId") String stopId,
			@RequestParam("destroyId") String destroyId,
			@RequestParam(name="deployTimeout",required=false,defaultValue="60000") int deployTimeout,
			@RequestParam(name="startTimeout",required=false,defaultValue="60000") int startTimeout,
			@RequestParam(name="stopTimeout",required=false,defaultValue="60000") int stopTimeout,
			@RequestParam(name="destroyTimeout",required=false,defaultValue="60000") int destroyTimeout,
			@RequestParam(name="versionDesc",required=false,defaultValue="versionDesc") String versionDesc,
			@RequestParam("registryId") int registryId, 
			@RequestParam("file") String ftpLocation){
		try {
			String versionId = UUID.randomUUID().toString();
			Version v = new Version(versionId, resourceId, ftpLocation, versionName, versionDesc,
					new Date(), deployTimeout, startTimeout, stopTimeout, destroyTimeout, registryId,null);
			resourceService.saveResourceVersion(v);
			Map<String, Long> saveMap = new HashMap<String, Long>();
			saveMap.put("deploy", Long.parseLong(deployId));
			saveMap.put("start", Long.parseLong(startId));
			saveMap.put("stop", Long.parseLong(stopId));
			saveMap.put("destroy", Long.parseLong(destroyId));
			resourceService.saveVersionFlows(versionId, saveMap);
			return MessageHelper.wrap("result", true, "message", "资源版本注册成功");
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "资源版本注册失败");
		}
	}
	
	@RequestMapping(value = "resourceDetail", method = RequestMethod.GET)
	@ResponseBody
	public String resourceDetail(@RequestParam("resourceName") String resourceName){
		Map<String,Object> resourceDetail = resourceService.getResourceDetail(resourceName);
		if(resourceDetail!=null && resourceDetail.size()>0){
			return JSON.toJSONString(resourceDetail,SerializerFeature.WriteDateUseDateFormat);//资源名称必须唯一
		}else{
			return "";
		}
	}
	
	
	@RequestMapping(value = "listResourceVersion", method = RequestMethod.GET)
	@ResponseBody
	public String listResourceVersion(@RequestParam("resourceId") String resourceId){
		return JSON.toJSONString(resourceService.listResourceVersion(resourceId));
	}
	
	@RequestMapping(value = "listResourceVersionByPage", method = RequestMethod.GET)
	@ResponseBody
	public String listResourceVersionByPage(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="resourceId") String resourceId,
			@RequestParam(name="versionName",required=false,defaultValue="") String versionName){
		Map<String,Object> condition = new HashMap<>();
		condition.put("resourceId", resourceId);
		condition.put("versionName", versionName);
		Page page = resourceService.listResourceVersionByPage(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile uploadFile){
		if (uploadFile == null) {
			return MessageHelper.wrap("result", false, "message", "文件为空");
		}
		String fileName = uploadFile.getOriginalFilename();
		String uuid = UUID.randomUUID().toString();
		try (InputStream input = uploadFile.getInputStream()){
			boolean b = resourceService.uploadFile(input, uuid, fileName);
			if(b){
				return MessageHelper.wrap("result", true, "message", "文件上传成功","file",uuid+"/"+fileName);				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return MessageHelper.wrap("result", false, "message", "文件上传失败");
	}
	
	@RequestMapping(value = "deleteResourceVersion", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteResourceVersion(@RequestParam("id") String id){
		try {
			resourceService.deleteResourceVersion("", id);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除版本失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除版本成功");
	}
	
	@RequestMapping(value = "deleteResource", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteResource(@RequestParam("resourceId") String resourceId){
		try {
			resourceService.deleteResource(resourceId);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除资源失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除资源成功");
	}
	
	@RequestMapping(value = "getInputAndOutput", method = RequestMethod.GET)
	@ResponseBody
	public String getInputAndOutput(@RequestParam("resourceVersionId") String resourceVersionId){
		//根据传入的资源版本id获取这个资源的输入输出
		return JSON.toJSONString(resourceService.getInputAndOutput(resourceVersionId));
	}
	
	@RequestMapping(value = "listAllResource", method = RequestMethod.GET)
	@ResponseBody
	public String listAllResource(@RequestParam("resourceName") String resourceName,@RequestParam(name="labelId",required=false) String labelId){
		return JSON.toJSONString(resourceService.listAllResource(resourceName,labelId));
	}
	
	@RequestMapping(value = "getResourceVersionDetail", method = RequestMethod.GET)
	@ResponseBody
	public String getResourceVersionDetail(@RequestParam("resourceVersionId") String resourceVersionId){
		return JSON.toJSONString(resourceService.getResourceVersionDetail(resourceVersionId));
	}
	
	@RequestMapping(value = "updateResourceVersionStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateResourceVersionStatus(@RequestParam("resourceVersionId") String  resourceVersionId,
			@RequestParam("status") String status,
			@RequestParam(name="description", required=false) String description,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		String userId="";
		if(user !=null){
			userId=user.getName();
		}
		boolean result = resourceService.upateResourceVersionStatus(resourceVersionId, status,description,userId);
		if(result){
			return MessageHelper.wrap("result", true, "message", "状态保存成功！");
		}else{
			return MessageHelper.wrap("result", false, "message", "状态保存失败！");
		}
	}
	
	@RequestMapping(value = "getResourceVersionStatus", method = RequestMethod.GET)
	@ResponseBody
	public String getResourceVersionStatus(@RequestParam("resourceVersionId") String  resourceVersionId){
		String result = resourceService.getResourceVersionStatus(resourceVersionId);
		if(result!=null && !"".equals(result)){
			return result;
		}else{
			return MessageHelper.wrap("result", false, "message", "获取状态失败！");
		}
	}
	
	@RequestMapping(value = "getResourceVersionFlows", method = RequestMethod.POST)
	@ResponseBody
	public String getResourceVersionFlows(
			@RequestParam("resourceVersionId") String resourceVersionId){
		String flows = resourceService.getResourceVersionFlows(resourceVersionId);
		if(flows == null || flows.length() == 0){
			return MessageHelper.wrap("result", false, "message", "获取子流程失败！");
		}
		else{
			return MessageHelper.wrap("result", true, "message", "获取资子流程成功！", "data", flows);
		}
	}
	
	@RequestMapping(value = "updateResourceVersionFlows", method = RequestMethod.POST)
	@ResponseBody
	public String updateResourceVersionFlows(
			@RequestParam("resourceVersionId") String resourceVersionId,
			@RequestParam("resourceVersionFlows") String resourceVersionFlows){
		try{
			boolean result = resourceService.updateResourceVersionFlows(resourceVersionId, resourceVersionFlows);
			if(result){
				return MessageHelper.wrap("result", true, "message", "更新子流程成功！");
			}
			else{
				return MessageHelper.wrap("result", false, "message", "更新子流程失败！");
			}
		}catch(Exception e){
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "更新子流程失败！");
		}
	}
	
	@RequestMapping(value = "getAppSubflowVars", method = RequestMethod.POST)
	@ResponseBody
	public String getAppSubflowVars(
			@RequestParam("appSubflowId") String appSubflowId){
		try{
			String vars = resourceService.getAppSubflowVars(appSubflowId);
			if(vars == null || "".equals(vars)){
				return "";
			}
			else
				return vars;
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	

	@RequestMapping(value = "updateResourceVersion", method = RequestMethod.POST)
	@ResponseBody
	public String updateResourceVersion(
			@RequestParam("file")String ftpLocation, 
			@RequestParam("resourceVersionId")String resourceVersionId,
			@RequestParam("md5")String md5){
		boolean b = this.resourceService.updateResourceVersion(resourceVersionId,ftpLocation,md5);
		if(b){
			return MessageHelper.wrap("result", true, "message", "更新成功");
		}else{
			return MessageHelper.wrap("result", false, "message", "更新失败");
		}
	}
	
	@RequestMapping(value = "saveNewComponentFlow", method = RequestMethod.POST,consumes = {"text/plain", "application/*"})
	@ResponseBody
	public String saveNewComponentFlow(@RequestBody String message){
		//flowType、flowInfo、versionId
		Map<String,Object> result = new HashMap<>();
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			Long flowId = resourceService.saveNewComponentFlow(map);
			result.put("result", true);
			result.put("message", flowId);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "getNewVersionOperations", method = RequestMethod.POST)
	@ResponseBody
	public String getNewVersionOperations(@RequestBody String message){
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			Map<String, List<Map<String, String>>> maps = resourceService.getNewVersionOperations(map.get("versionId"));
			return JSON.toJSONString(maps);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	@RequestMapping(value = "getFlowInfoByVersionOperation", method = RequestMethod.POST)
	@ResponseBody
	public String getFlowInfoByVersionOperation(
			@RequestBody String message){
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			String flowInfo = resourceService.getFlowInfoByFlowId(map.get("flowId"));
			return MessageHelper.wrap("result", true, "message", flowInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateFlowInfoByFlowId", method = RequestMethod.POST)
	@ResponseBody
	public String updateFlowInfoByFlowId(@RequestBody String message){
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			boolean result = resourceService.updateFlowInfoByFlowId(map.get("flowId"), map.get("flowInfo"));
			if(result){
				return MessageHelper.wrap("result", true, "message", "更新成功");
			}
			else{
				return MessageHelper.wrap("result", false, "message", "没有对应的记录");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", e.getMessage());
		}
	}
	
	@RequestMapping(value = "saveScript", method = RequestMethod.POST)
	@ResponseBody
	public String saveScript(@RequestParam("file") MultipartFile uploadFile,
			@RequestParam("name") String name,@RequestParam("description") String description,@RequestParam("userId") String userId){
		if (uploadFile == null) {
			return MessageHelper.wrap("result", false, "message", "文件为空");
		}
		if(resourceService.checkResourceName(name)){
			return MessageHelper.wrap("result", false, "message", "资源名已存在，请填写其他名称！");
		}
		String fileName = uploadFile.getOriginalFilename();
		if(!fileName.endsWith(".zip")){
			return MessageHelper.wrap("result", false, "message", "请上传zip格式的包文件！");
		}
		String uuid = UUID.randomUUID().toString();
		try (InputStream input = uploadFile.getInputStream()){
			boolean b = resourceService.uploadFile(input, uuid, fileName);
			if(b){
				String path=uuid+"/"+fileName;
				Map<String,Object> param = new HashMap<>();
				param.put("id", uuid);
				param.put("resourceName", name);
				param.put("resourceDesc", description);
				param.put("resourcePath", path);
				resourceService.saveScriptResource(param);
				
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(userId, ResourceCode.SCRIPT, name, ResourceCode.Operation.ADD, 1, "脚本上传:" + fileName));
					}
				});
				//==============添加审计end=====================
				
				return MessageHelper.wrap("result", true, "message", "文件上传成功","file",uuid+"/"+fileName);				
			}
		} catch (IOException e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.SCRIPT, name, ResourceCode.Operation.ADD, 0, "脚本上传:" + fileName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
		}
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(userId, ResourceCode.SCRIPT, name, ResourceCode.Operation.ADD, 0, "脚本上传:" + fileName));
			}
		});
		//==============添加审计end=====================
		return MessageHelper.wrap("result", false, "message", "文件上传失败");
	}
	
	
	@RequestMapping(value = "listScriptResourceByPage", method = RequestMethod.GET)
	@ResponseBody
	public String listScriptResourceByPage(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="name",required=false) String name,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		Map<String,Object> condition = new HashMap<>();
		condition.put("resourceName", name);
		condition.put("sortName", SortUtil.getColunmName("script", sortName));
	    condition.put("sortOrder", sortOrder);
		Page page = resourceService.listScriptResource(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "deleteScriptResource", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteScriptResource(@RequestParam("id") String id,@RequestParam("userId") String userId){
		Map<String, Object> scriptMap = resourceService.findScriptById(id);
		String resourcePath = ""+scriptMap.get("RESOURCE_PATH");
		String resourceFile = resourcePath.split("/")[resourcePath.split("/").length-1];
		try {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.SCRIPT, ""+scriptMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 1, "删除脚本:" + resourceFile));
				}
			});
			//==============添加审计end=====================
			return resourceService.deleteScriptResource(id);
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.SCRIPT, ""+scriptMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 0, "删除脚本:" + resourceFile));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除脚本失败:message:"+e.getMessage());
		}
	}
	@RequestMapping(value = "saveNewResourceVersion", method = RequestMethod.POST)
	@ResponseBody
	public String saveNewResourceVersion(@Context HttpServletRequest request,
			@RequestParam("resourceId") String resourceId,
			@RequestParam("versionName") String versionName,
			@RequestParam(name="deployTimeout",required=false,defaultValue="60000") int deployTimeout,
			@RequestParam(name="startTimeout",required=false,defaultValue="60000") int startTimeout,
			@RequestParam(name="stopTimeout",required=false,defaultValue="60000") int stopTimeout,
			@RequestParam(name="destroyTimeout",required=false,defaultValue="60000") int destroyTimeout,
			@RequestParam(name="versionDesc",required=false,defaultValue="versionDesc") String versionDesc,
			@RequestParam("registryId") int registryId, 
			@RequestParam("file") String fileName,
			@RequestParam("md5") String md5,
			@RequestParam("type") int type,
			@RequestParam("input") String input,
			@RequestParam("output") String output){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			if(resourceName.indexOf("itsm") != -1 || resourceName.indexOf("ITSM") != -1 || resourceName.indexOf("Itsm") != -1){
				boolean isApp = false;
				boolean isDb = false;
				if(versionName.indexOf("app") != -1 || versionName.indexOf("App") != -1 || versionName.indexOf("APP") != -1 
						|| versionName.indexOf("class") != -1 || versionName.indexOf("Class") != -1 || versionName.indexOf("CLASS") != -1){
					isApp = true;
				}
				if(versionName.indexOf("sql") != -1 || versionName.indexOf("Sql") != -1 || versionName.indexOf("SQL") != -1 
						|| versionName.indexOf("db") != -1 || versionName.indexOf("Db") != -1 || versionName.indexOf("DB") != -1){
					isDb = true;
				}
				if(!isApp && !isDb){
					return MessageHelper.wrap("result", false, "message", "itsm组件[" + resourceName + "]版本名称[" + versionName + "]必须包含关键字[app/class]或[sql/db]中的一个，用以区分版本类型，请重新命名！");
				}
				if(isApp && isDb){
					return MessageHelper.wrap("result", false, "message", "itsm组件[" + resourceName + "]版本名称[" + versionName + "]关键字[app/class]和[sql/db]不能都包含，用以区分版本类型，请重新命名！");
				}
			}
			List<Version>  versions = resourceService.listResourceVersion(resourceId);
			for (Version version : versions) {
				if (versionName.equals(version.getVersionName())) {
					return MessageHelper.wrap("result", false, "message", versionName+"版本已存在");
				}
			}
			String resourcePath="";
			if(fileName==null||fileName.equals("")){
				
			}else{
				Map<String, Object> packageDetail = packageService.findPackageByName(fileName);
				resourcePath = "" + packageDetail.get("RESOURCE_PATH");
				if(resourcePath.indexOf("ENC(") == -1){
					resourcePath = buildResourcePath(packageDetail);
				}
			}
			String versionId = UUID.randomUUID().toString();
			input = SensitiveDataUtil.getEncryptConfig(input);
			output = SensitiveDataUtil.getEncryptConfig(output);
			Version v = new Version(versionId, resourceId, resourcePath, versionName, versionDesc,
					new Date(), deployTimeout, startTimeout, stopTimeout, destroyTimeout, registryId,md5,type,input,output);
			resourceService.saveResourceVersion(v);
			JSONObject params = new JSONObject();
			//#{id},#{userId},#{resouceVersionId},#{opType},#{status},now(),#{description}
			params.put("id", UUID.randomUUID().toString());
			params.put("userId", user.getName());
			params.put("resourceVersionId", versionId);
			params.put("opType", Constants.audit4Resource.OP_NEW);
			params.put("status", Constants.audit4Resource.STATUS_NEW);
			params.put("description", "新建");
			resourceService.saveAudit(params);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 1, "创建组件版本:" + versionName));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", true, "message", "资源版本注册成功");
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 0, "创建组件版本:" + versionName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "资源版本注册失败");
		}
	}

	@RequestMapping(value = "cloneResourceVersion", method = RequestMethod.POST)
	@ResponseBody
	public String cloneResourceVersion(@Context HttpServletRequest request,
			@RequestParam("resourceId") String resourceId,
			@RequestParam("versionName") String versionName,
			@RequestParam(name="deployTimeout",required=false,defaultValue="60000") int deployTimeout,
			@RequestParam(name="startTimeout",required=false,defaultValue="60000") int startTimeout,
			@RequestParam(name="stopTimeout",required=false,defaultValue="60000") int stopTimeout,
			@RequestParam(name="destroyTimeout",required=false,defaultValue="60000") int destroyTimeout,
			@RequestParam(name="versionDesc",required=false,defaultValue="versionDesc") String versionDesc,
			@RequestParam("registryId") int registryId, 
			@RequestParam("file") String fileName,
			@RequestParam("md5") String md5,
			@RequestParam("type") int type,
			@RequestParam("input") String input,
			@RequestParam("output") String output,
			@RequestParam("oldResourceVersionId") String oldResourceVersionId){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			if(resourceName.indexOf("itsm") != -1 || resourceName.indexOf("ITSM") != -1 || resourceName.indexOf("Itsm") != -1){
				boolean isApp = false;
				boolean isDb = false;
				if(versionName.indexOf("app") != -1 || versionName.indexOf("App") != -1 || versionName.indexOf("APP") != -1 
						|| versionName.indexOf("class") != -1 || versionName.indexOf("Class") != -1 || versionName.indexOf("CLASS") != -1){
					isApp = true;
				}
				if(versionName.indexOf("sql") != -1 || versionName.indexOf("Sql") != -1 || versionName.indexOf("SQL") != -1 
						|| versionName.indexOf("db") != -1 || versionName.indexOf("Db") != -1 || versionName.indexOf("DB") != -1){
					isDb = true;
				}
				if(!isApp && !isDb){
					return MessageHelper.wrap("result", false, "message", "itsm组件[" + resourceName + "]版本名称[" + versionName + "]必须包含关键字[app/class]或[sql/db]中的一个，用以区分版本类型，请重新命名！");
				}
				if(isApp && isDb){
					return MessageHelper.wrap("result", false, "message", "itsm组件[" + resourceName + "]版本名称[" + versionName + "]关键字[app/class]和[sql/db]不能都包含，用以区分版本类型，请重新命名！");
				}
			}
			List<Version>  versions = resourceService.listResourceVersion(resourceId);
			for (Version version : versions) {
				if (versionName.equals(version.getVersionName())) {
					return MessageHelper.wrap("result", false, "message", versionName+"版本已存在");
				}
			}
			String versionId = UUID.randomUUID().toString();
			input = SensitiveDataUtil.getEncryptConfig(input);
			output = SensitiveDataUtil.getEncryptConfig(output);
			String resourcePath = "";
			if(fileName !=null && !"".equals(fileName)){
				Map<String, Object> packageDetail = packageService.findPackageByName(fileName);
				resourcePath = "" + packageDetail.get("RESOURCE_PATH");
				if(resourcePath.indexOf("ENC(") == -1){
					resourcePath = buildResourcePath(packageDetail);
				}
			}
			Version v = new Version(versionId, resourceId, resourcePath, versionName, versionDesc,
					new Date(), deployTimeout, startTimeout, stopTimeout, destroyTimeout, registryId,md5,type,input,output);
			resourceService.saveResourceVersion(v);
			List<VersionFtl> vfs = resourceService.getVersionFtl(oldResourceVersionId);
			for (VersionFtl vf : vfs) {
				String id = UUID.randomUUID().toString();
				VersionFtl versionFtl = new VersionFtl(id, 
						versionId, vf.getTemplates(), vf.getFtlName(), vf.getFtlText());
				resourceService.saveVersionFtl(versionFtl);
			}
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.CLONE, 1, "组件版本克隆:" + versionName));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", true, "message", "资源版本克隆成功", "id", versionId);
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.CLONE, 0, "资源版本克隆:" + versionName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "资源版本克隆失败");
		}
	}
	
	@RequestMapping(value = "getNewResourceVersion", method = RequestMethod.GET)
	@ResponseBody
	public String getNewResourceVersion(@RequestParam("resourceVersionId") String resourceVersionId){
		return JSON.toJSONString(resourceService.getResourceVersion(resourceVersionId));
	}
	
	@RequestMapping(value = "updateVersion", method = RequestMethod.POST)
	@ResponseBody
	public String updateVersion(@Context HttpServletRequest request,
			@RequestParam("resourceVersionId")String resourceVersionId,
			@RequestParam("input")String input, 
			@RequestParam("output")String output,
			@RequestParam("file")String fileName){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			input = SensitiveDataUtil.getEncryptConfig(input);
			output = SensitiveDataUtil.getEncryptConfig(output);
		} catch (Exception e) {
			return MessageHelper.wrap("result", false, "message", "更新失败，reason[加密参数失败]!");
		}
		String resourcePath = "";
		if(fileName !=null && !"".equals(fileName)){
			Map<String, Object> packageDetail = packageService.findPackageByName(fileName);
			resourcePath = "" + packageDetail.get("RESOURCE_PATH");
			if(resourcePath.indexOf("ENC(") == -1){
				resourcePath = buildResourcePath(packageDetail);
			}
		}
		boolean b = this.resourceService.updateVersion(resourceVersionId,input,output,resourcePath);
		if(b){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Version v = resourceService.getResourceVersion(resourceVersionId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, v.getResourceName(), ResourceCode.Operation.UPDATE, 1, "更新组件版本:" + v.getVersionName()));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", true, "message", "更新成功");
		}else{
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Version v = resourceService.getResourceVersion(resourceVersionId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, v.getResourceName(), ResourceCode.Operation.UPDATE, 0, "更新组件版本:" + v.getVersionName()));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", false, "message", "更新失败");
		}
	}
	
	
	@RequestMapping(value = "saveVersionFtl", method = RequestMethod.POST)
	@ResponseBody
	public String saveVersionFtl(@Context HttpServletRequest request,
			@RequestParam("resourceVersionId") String resourceVersionId ,
			@RequestParam("ftlName") String ftlName,
			@RequestParam("templates") String templates,
			@RequestParam("ftlText") String ftlText){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			List<VersionFtl> vf = resourceService.getVersionFtl(resourceVersionId);
			String FtlFilePath = templates.substring(2, templates.indexOf(":")-1);
			for (VersionFtl versionFtl : vf) {
				String tem = versionFtl.getTemplates();
				String temFilePath = tem.substring(2,tem.indexOf(":")-1);
				if (FtlFilePath.equals(temFilePath)) {
					return MessageHelper.wrap("result", false, "message", "已存在相同模版文件！");
				}
				
			}
			String id = UUID.randomUUID().toString();
			VersionFtl versionFtl = new VersionFtl(id, 
					resourceVersionId, templates, ftlName, ftlText);
			resourceService.saveVersionFtl(versionFtl);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Version v = resourceService.getResourceVersion(resourceVersionId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ v.getResourceName(), ResourceCode.Operation.ADD, 1, "版本:"+v.getVersionName() +" 模版文件保存:" + ftlText));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", true, "message", "模版文件保存成功");
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Version v = resourceService.getResourceVersion(resourceVersionId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ v.getResourceName(), ResourceCode.Operation.ADD, 0, "版本:"+v.getVersionName() +"模版文件保存:" + ftlText));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "模版文件保存失败");
		}
	}
	@RequestMapping(value = "updateVersionFtl", method = RequestMethod.POST)
	@ResponseBody
	public String updateVersionFtl(@Context HttpServletRequest request,
			@RequestParam("id") String id ,
//			@RequestParam("ftlName") String ftlName,
//			@RequestParam("templates") String templates,
			@RequestParam("ftlText") String ftlText
			){
		final User user = (User) request.getSession().getAttribute("user");
		boolean b = this.resourceService.updateVersionFtl(id,ftlText);
		if(b){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, String> resoureMap = resourceService.getResourceByFtlId(id);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ resoureMap.get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 1, "版本:"+ resoureMap.get("VERSION_NAME")+" 更新ftl:" + ftlText));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", true, "message", "更新成功");
		}else{
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, String> resoureMap = resourceService.getResourceByFtlId(id);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ resoureMap.get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 0, "版本:"+ resoureMap.get("VERSION_NAME")+" 更新ftl:" + ftlText));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", false, "message", "更新失败");
		}
	}
	@RequestMapping(value = "getVersionFtl", method = RequestMethod.GET)
	@ResponseBody
	public String getVersionFtl(
			@RequestParam("resourceVersionId") String resourceVersionId){
		try {
			List<VersionFtl> vf = resourceService.getVersionFtl(resourceVersionId);
			
			return JSON.toJSONString(vf);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "获取模版信息失败");
		}
	}
	
	@RequestMapping(value = "deleteVersionFtl", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteVersionFtl(@RequestParam("id") String id,@Context HttpServletRequest request){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			resourceService.deleteVersionFtl(id);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, String> resoureMap = resourceService.getResourceByFtlId(id);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ resoureMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 1, "版本:"+ resoureMap.get("RESOURCE_NAME")+" 删除模版文件:" + resoureMap.get("FTL_NAME")));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, String> resoureMap = resourceService.getResourceByFtlId(id);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+ resoureMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 0, "版本:"+ resoureMap.get("RESOURCE_NAME")+" 删除模版文件:" + resoureMap.get("FTL_NAME")));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除模版文件失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除模版文件成功");
	}
	
	@RequestMapping(value = "getWorkpieceTree", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkpieceTree(
			@RequestParam(name="resourceVersionId") String resourceVersionId) throws IOException{
		try {
			String workpieceTree = resourceService.getWorkpieceTree(resourceVersionId);
			return MessageHelper.wrap("result", true, "message", workpieceTree);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "获取工件包文件列表失败:message:"+e.getMessage());
		}
		
	}
	@RequestMapping(value = "getWorkpieceTreeByUrl", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkpieceTreeByUrl(
			@RequestParam(name="resourcePath") String resourcePath) throws IOException{
		try {
			String workpieceTree = packageService.getPackageTree(resourcePath);
			return MessageHelper.wrap("result", true, "message", workpieceTree);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "获取工件包文件列表失败:message:"+e.getMessage());
		}
		
	}
	
	
	
	@RequestMapping(value = "getWorkpieceFile", method = RequestMethod.GET)
	@ResponseBody
	public String getWorkpieceFile(
			@RequestParam(name="resourceVersionId") String resourceVersionId,
			@RequestParam(name="filePath") String filePath) throws IOException{
		try {
			String fileSuffix = filePath.substring(filePath.lastIndexOf(".")+1,filePath.length());
			   for (String suffixs : FileUtil.binaryFileLists) {
				   if (fileSuffix.equals(suffixs)) {
					return MessageHelper.wrap("result", true, "message", "非文本文件无法展示");
				   }
			   	}
			   String fileText = resourceService.getWorkpieceFile(resourceVersionId,filePath);
//			byte[] b = fileText.getBytes(Charset.forName("UTF-8"));
			return MessageHelper.wrap("result", true, "message", fileText);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "获取工件包文件失败:message:"+e.getMessage());
		}
	}
	
	@RequestMapping(value = "judgeWorkpieceUrl", method = RequestMethod.GET)
	@ResponseBody
	public String judgeWorkpieceUrl(
			@RequestParam(name="url") String url) throws IOException{
			boolean isLegal = resourceService.judgeWorkpieceUrl(url);
			return MessageHelper.wrap("result", isLegal);
	}
	
	
	
	@RequestMapping(value = "registNewResource", method = RequestMethod.POST)
	@ResponseBody
	public String registNewResource(@Context HttpServletRequest request,
			@RequestParam("resourceType") String resourceType,
			@RequestParam("resourceName") String resourceName, 
			@RequestParam("registryId") int registryId,
			@RequestParam(name="resourceDesc",required=false,defaultValue="resourceDesc") String resourceDesc,
			@RequestParam(name="labelIds",required=false) String labelIds,
			@RequestParam(name = "icon", defaultValue = "") String icon
			) {
		final User user = (User) request.getSession().getAttribute("user");
		String id = UUID.randomUUID().toString();
		Resource r = new Resource(id, resourceName, resourceType, resourceDesc);
		r.setIcon(icon);
		if(resourceName == null || "".equals(resourceName.trim())){
			return MessageHelper.wrap("result", false, "message", "组件名称不允许为空，请重新填写！");
		}
		try{
			boolean flag = resourceService.registResource(r);
			if(flag){
				//打标签
				if(!JudgeUtil.isEmpty(labelIds)){
					labelsService.saveResourceLabelMap(id, labelIds);
				}
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, resourceName, ResourceCode.Operation.ADD, 1, "组件创建:" + resourceName));
					}
				});
				//==============添加审计end=====================
				return MessageHelper.wrap("result", true, "message", "组件创建成功", "resourceId", id);
			}
			else{
				return MessageHelper.wrap("result", false, "message", "组件名称已存在，请重新填写！");
			}
		}catch(Exception e){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, resourceName, ResourceCode.Operation.ADD, 0, "组件创建:" + resourceName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "组件创建失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "addNewResourceFlow", method = RequestMethod.POST)
	@ResponseBody
	public String addNewResourceFlow(@Context HttpServletRequest request,@RequestBody String message){
		Map<String,Object> result = new HashMap<>();
		Map<String,Object> map = JSON.parseObject(message, new TypeReference<Map<String,Object>>(){});
		String resourceId = "" + map.get("resourceId");
		final User user = (User) request.getSession().getAttribute("user");
		try {
			Long flowId = resourceService.addNewResourceFlow(map);
			result.put("result", true);
			result.put("message", "添加流程成功!");
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 1, "流程类型："+map.get("flowType")+" 添加流程:" + map.get("flowName")));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.ADD, 0, "流程类型："+map.get("flowType")+" 添加流程:"+ map.get("flowName")));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "添加流程失败:" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "getNewResourceOperationFlows", method = RequestMethod.POST)
	@ResponseBody
	public String getNewResourceOperationFlows(
			@RequestParam("resourceId") String resourceVersionId,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		String flows = resourceService.getNewResourceFlows(resourceVersionId,sortName,sortOrder);
		if(flows == null || flows.length() == 0){
			return MessageHelper.wrap("result", false, "message", "获取组件子流程失败！");
		}
		else{
			return MessageHelper.wrap("result", true, "message", "获取组件子流程成功！", "data", flows);
		}
	}
	
	@RequestMapping(value = "getNewFlowInfoByFlowId", method = RequestMethod.POST)
	@ResponseBody
	public String getNewFlowInfoByFlowId(
			@RequestBody String message){
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			String flowInfo = resourceService.getNewFlowInfoByFlowId(map.get("cdFlowId"));
			return MessageHelper.wrap("result", true, "message","获取流程信息成功" ,"data", flowInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateNewFlowInfoByFlowId", method = RequestMethod.POST)
	@ResponseBody
	public String updateNewFlowInfoByFlowId(@Context HttpServletRequest request,@RequestBody String message){
		final User user = (User) request.getSession().getAttribute("user");
		Map<String,String> map = JSON.parseObject(message, new TypeReference<Map<String,String>>(){});
		try {
			boolean result = resourceService.updateNewFlowInfoByFlowId(map.get("cdFlowId"), map.get("flowInfo"));
			if(result){
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						String id = map.get("cdFlowId");
						Map<String, Object>  resourceMap = resourceService.getNewFlowDetailByFlowId(id);
						String flowName = ""+resourceMap.get("FLOW_NAME");
						String resourceId = ""+resourceMap.get("RESOURCE_ID");
						String flowType = ""+resourceMap.get("FLOW_TYPE");
						List resourList = new ArrayList();
						resourList.add(resourceId);
						List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
						auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.UPDATE, 1, "流程类型："+flowType+"更新流程:" + flowName));
					}
				});
				//==============添加审计end=====================
				return MessageHelper.wrap("result", true, "message", "更新成功");
			}
			else{
				return MessageHelper.wrap("result", false, "message", "该流程不存在");
			}
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					String id = map.get("cdFlowId");
					Map<String, Object>  resourceMap = resourceService.getNewFlowDetailByFlowId(id);
					String flowName = ""+resourceMap.get("FLOW_NAME");
					String resourceId = ""+resourceMap.get("RESOURCE_ID");
					String flowType = ""+resourceMap.get("FLOW_TYPE");
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					User user = (User) request.getSession().getAttribute("user");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.UPDATE, 0, "流程类型："+flowType+"更新流程:" + flowName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", e.getMessage());
		}
	}
	
	@RequestMapping(value = "deleteNewResourceFlow", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteNewResourceFlow(@RequestParam("cdFlowId") String cdFlowId,@Context HttpServletRequest request){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			final Map<String, Object>  resourceMap = resourceService.getNewFlowDetailByFlowId(cdFlowId);
			resourceService.deleteNewResourceFlow(cdFlowId);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					String flowName = ""+resourceMap.get("FLOW_NAME");
					String resourceId = ""+resourceMap.get("RESOURCE_ID");
					String flowType = ""+resourceMap.get("FLOW_TYPE");
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 1, "流程类型："+flowType+" 删除流程:" + flowName));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object>  resourceMap = resourceService.getNewFlowDetailByFlowId(cdFlowId);
					String flowName = ""+resourceMap.get("FLOW_NAME");
					String resourceId = ""+resourceMap.get("RESOURCE_ID");
					String flowType = ""+resourceMap.get("FLOW_TYPE");
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.UPDATE, 0, "流程类型："+flowType+" 删除流程:" + flowName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除流程失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除流程成功");
	}
	
	@RequestMapping(value = "deleteNewResourceVersion", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteNewResourceVersion(@RequestParam("versionId") String versionId,@Context HttpServletRequest request){
		final User user = (User) request.getSession().getAttribute("user");
		final Version v = resourceService.getResourceVersion(versionId);
		try {
			resourceService.deleteNewResourceVersion(versionId);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, v.getResourceName(), ResourceCode.Operation.DELETE, 1, "删除版本:" + v.getVersionName()));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, v.getResourceName(), ResourceCode.Operation.DELETE, 0, "删除版本:" + v.getVersionName()));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除版本失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除版本成功");
	}
	
	@RequestMapping(value = "deleteNewResource", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteNewResource(@RequestParam("resourceId") String resourceId,@Context HttpServletRequest request){
		final User user = (User) request.getSession().getAttribute("user");
		List resourList = new ArrayList();
		resourList.add(resourceId);
		List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
		try {
			resourceService.deleteNewResource(resourceId);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 1, "删除组件:" + ""+returnResourceList.get(0).get("RESOURCE_NAME")));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List resourList = new ArrayList();
					resourList.add(resourceId);
					List<Map<String,Object>> returnResourceList = resourceService.exportResources(resourList);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.COMPONENT, ""+returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 0, "删除组件:" + ""+returnResourceList.get(0).get("RESOURCE_NAME")));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除组件失败:message:"+e.getMessage());
		}
		return MessageHelper.wrap("result", true, "message", "删除组件成功");
	}
	
	@RequestMapping(value = "getNewFlowVarsByFlowId", method = RequestMethod.POST)
	@ResponseBody
	public String getNewFlowVarsByFlowId(
			@RequestParam("cdFlowId") String cdFlowId){
		try{
			String vars = resourceService.getNewFlowVarsByFlowId(cdFlowId);
			if(vars == null || "".equals(vars)){
				return MessageHelper.wrap("result", false, "message", "获取组件变量失败");
			}
			else
				return MessageHelper.wrap("result", true, "message", "获取组件变量成功", "data", vars);
		}catch(Exception e){
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "获取组件变量失败");
		}
	}
	
	@RequestMapping(value = "listNewResourceVersionByPage", method = RequestMethod.GET)
	@ResponseBody
	public String listNewResourceVersionByPage(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="resourceId") String resourceId,
			@RequestParam(name="versionName",required=false,defaultValue="") String versionName,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		Map<String,Object> condition = new HashMap<>();
		condition.put("resourceId", resourceId);
		condition.put("versionName", versionName);
		condition.put("sortName", SortUtil.getColunmName("componentVersion", sortName));
	    condition.put("sortOrder", sortOrder);
		Page page = resourceService.listNewResourceVersionByPage(condition, pageNum, pageSize);
		List<String> list = resourceService.listResourceVersionStatuses(resourceId);
		boolean versionsRunning = false;
		if(list != null && list.size() > 0){
			for(String status : list){
				if("30".equals(status) || "40".equals(status) || "50".equals(status) || "60".equals(status) || "51".equals(status) || "61".equals(status) || "33".equals(status) || "43".equals(status)){
					versionsRunning = true;
					break;
				}
			}
		}
		page.getProperty().put("versionsRunning", versionsRunning);
		String resourceStatus = resourceService.getResourceStatus(resourceId);
		page.getProperty().put("resourceStatus", resourceStatus);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "getNewResourceVersionDetail", method = RequestMethod.GET)
	@ResponseBody
	public String getNewResourceVersionDetail(@RequestParam("resourceVersionId") String resourceVersionId){
		return JSON.toJSONString(resourceService.getNewResourceVersionDetail(resourceVersionId));
	}
	
	@RequestMapping(value="exportResource",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void exportResources(@Context HttpServletResponse resp,@RequestParam("ids") String ids){
		String result =  resourceService.exportResources(ids);
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
	}
	
	@RequestMapping(value="importResources",method = RequestMethod.POST)
	@ResponseBody
	public String importResources(@RequestParam("resource") String resource,
								  @RequestParam("flow") String flow,
								  @RequestParam("version") String version,
								  @RequestParam("ftl") String ftl,
								  @RequestParam("package") String packages,
								  @RequestParam("zip") String zip,
								  @RequestParam("userId") String userId){
		return resourceService.importResources(resource,flow,version,ftl,packages,zip,userId);
	}
	
	@RequestMapping(value="checkScriptExist",method = RequestMethod.GET)
	@ResponseBody
	public String checkScriptExist(@RequestParam("name") String name){
		return resourceService.checkResourceName(name).toString();
	}
	
	@RequestMapping(value="getCDAllFlows",method = RequestMethod.GET)
	@ResponseBody
	public String getCDAllFlows(){
		return JSON.toJSONString(resourceService.getCDAllFlows());
	}
	
	private Map<String,Object> lowerMap(Map<String,Object> map){
		Map<String,Object> newMap = new HashMap<String,Object>();
		newMap.put("id", map.get("ID"));
		newMap.put("resourceId", map.get("RESOURCE_ID"));
		newMap.put("flowType", map.get("FLOW_TYPE"));
		newMap.put("flowName",map.get("FLOW_NAME"));
		newMap.put("flowId", map.get("FLOW_ID"));
		newMap.put("flowInfo", map.get("FLOW_INFO_CONDITION"));
		return newMap;
	}
	
	private String buildResourcePath(Map<String, Object> packageDetail) {
		String password = null;
		try {
			password = "ENC(" + SensitiveDataUtil.encryptDesText(pwd) + ")";
		} catch (Exception e) {
			System.out.println("加密失败[" + e.getMessage() + "]");
			e.printStackTrace();
			password = pwd;
		}
		String id = "" + packageDetail.get("ID");
		String resourceName = "" + packageDetail.get("RESOURCE_NAME");
		String resourcePath = "ftp://" + user + ":" + password + "@" + url + File.separator + "packages"
									+ File.separator + id + File.separator + resourceName;
		return resourcePath;
	}
	
	@RequestMapping(value="findPackageByResourceId",method = RequestMethod.GET)
	@ResponseBody
	public String findPackageByResourceId(@RequestParam("resourceId") String resourceId){
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		List<Version> versionList = resourceService.listResourceVersion(resourceId);
		for(Version version : versionList){
			returnList.add(packageService.getWorkpiece(version.getResourcePath()));
		}
		return JSON.toJSONString(returnList,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value="addDescription",method = RequestMethod.POST)
	@ResponseBody
	public String addDescription(@RequestParam("resourceVersionId") String versionId,@RequestParam("status") String status,@RequestParam("description") String description,HttpServletRequest request){
		JSONObject result = new JSONObject();
		try {
			User user = (User) request.getSession().getAttribute("user");
			JSONObject params = new JSONObject();
			params.put("id", UUID.randomUUID().toString());
			params.put("userId", user.getName());
			params.put("resourceVersionId", versionId);
			params.put("opType", Constants.audit4Resource.OP_ADD_DESCRIPTION);
			params.put("status", status);
			params.put("description", description);
			result =  resourceService.saveAudit(params);
		} catch (Exception e) {
			result.put("result", false);
			result.put("message", "添加描述信息异常！");
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value = "bindComponentFlow", method = RequestMethod.POST)
	@ResponseBody
	public String bindComponentFlow(@RequestParam("componentId") String componentId,
			@RequestParam("bindType") int bindType,
			@RequestParam("blueprintId") int blueprintId,
			@RequestParam("blueprintFlow") String blueprintFlow
			){
		Map<String, Object> result = new HashMap<>();
		String typeName = "";
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("componentId", componentId);
			params.put("bindType", bindType);
			params.put("blueprintId", blueprintId);
			params.put("blueprintFlow", blueprintFlow);
			resourceService.bindComponentFlow(params);
			switch(bindType){
				case 0:
					typeName = "发布";
					break;
				case 1:
					typeName = "回退";
					break;
				case 2:
					typeName = "启动";
					break;
				case 3:
					typeName = "停止";
					break;
				default:
					typeName = "未知";
			}
			result.put("result", true);
			result.put("message", "绑定[" + typeName + "]流程成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "绑定[" + typeName + "]流程失败！reason:" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "executeComponentVersionFlow", method = RequestMethod.POST)
	@ResponseBody
	public String executeComponentVersionFlow(@RequestParam("componentVersionId") String componentVersionId,
			@RequestParam("componentExecuteType") int componentExecuteType,
			@Context HttpServletRequest request){
		String operation = "";
		if(componentExecuteType == 0){
			operation = "发布";
		}else if(componentExecuteType == 1){
			operation = "回滚";
		}
		else if(componentExecuteType == 2){
			operation = "启动";
		}
		else if(componentExecuteType == 3){
			operation = "停止";
		}
		else{
		}
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message", "执行" + operation + "操作失败：非法session，无法获取用户信息！");
		}
		Map<String, Object> result = new HashMap<>();
		try{
			Map<String, Object> params = new HashMap<>();
			params.put("userId", user.getName());
			params.put("componentVersionId", componentVersionId);
			params.put("componentExecuteType", componentExecuteType);
			return resourceService.executeComponentVersionFlow(params, null);
		}catch(Exception e){
			e.printStackTrace();
			result.put("result", false);
			result.put("message", operation + "失败！reason:" + e.getMessage());
			return JSON.toJSONString(result);
		}
	}
	
	@RequestMapping(value = "releaseComponentVersionsByBatch", method = RequestMethod.POST)
	@ResponseBody
	public String releaseComponentVersionsByBatch(@RequestParam("componentVersionIds") String componentVersionIds,
			@RequestParam("componentExecuteType") int componentExecuteType,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message", "批量发布失败：非法session，无法获取用户信息！");
		}
		Runnable batchRelease = new Runnable(){
			@Override
			public void run() {
				Map<String, Object> params = new HashMap<>();
				params.put("userId", user.getName());
				params.put("componentVersionIds", componentVersionIds);
				params.put("componentExecuteType", componentExecuteType);
				resourceService.releaseComponentVersionsByBatch(params);
			}
		};
		ITSMBatchReleasePool.getInstance().execute(batchRelease);
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("message", "批量" + (componentExecuteType == 0 ? "发布" : "回滚") + "开始执行！");
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "getResourceBindDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getResourceBindDetail(@RequestParam("resourceId") String resourceId){
		Map<String, Object> result = new HashMap<>();
		try{
			result = resourceService.getResourceBindDetail(resourceId);
			result.put("result", true);
		}catch(Exception e){
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "获取组件绑定信息失败。reason:" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="/listBlueprintByComponent",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintByComponent(@RequestParam("userId") long userId,
			@RequestParam(name="componentName") String componentName) {
		String resultStr = userService.getSonsOfUser(userId);
		Map<Long, String> map = JSON.parseObject(resultStr,
				new TypeReference<Map<Long, String>>() {
		});
		StringBuilder userIds = new StringBuilder();
		int index = 0;
		for (long unit : map.keySet()) {
			if (index != 0) {
				userIds.append(",");
			}
			index++;
			userIds.append(unit + "");
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("userIds", userIds.toString().split(","));
		List<Map<String,Object>> allBuleprints = blueprintService.listAllBlueprintInstances(condition);
		List<Map<String,Object>> targeBlueprints = new ArrayList<>();
		if (allBuleprints != null && allBuleprints.size() > 0) {
			for (Map<String, Object> blueprint : allBuleprints) {
				boolean appExist = false;
				JSONObject info = JSON.parseObject("" + blueprint.get("INFO"));
				JSONArray nodes = info.getJSONArray("nodeDataArray");
				for(int j = 0; j < nodes.size(); j++){
					JSONObject node = nodes.getJSONObject(j);
					if("component".equals(node.getString("eleType"))){
						String nodeName = node.getString("nodeName");
						if(nodeName.equals(componentName)){
							appExist = true;
							break;
						}
					}
				}
				if(!appExist){
					continue;
				}
				targeBlueprints.add(blueprint);
			}
		}
		return JSON.toJSONString(targeBlueprints, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value="exportPatches",method = RequestMethod.GET)
	@ResponseBody
	public void exportPatches(@Context HttpServletResponse resp,@RequestParam("ids") String ids){
		String result =  resourceService.exportPatches(ids);
		Map<String,String> resultMap = JSONObject.parseObject(result,Map.class);
		if("true".equals(String.valueOf(resultMap.get("result")))){
			String filePath = resultMap.get("message");
			File file = new File(filePath);
			resp.setHeader("content-type", "application/octet-stream");
			resp.setContentType("application/octet-stream");
			InputStream is = null;
			BufferedOutputStream bos =null;
			BufferedInputStream bis =null;
			try {
				resp.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
				
				OutputStream os = resp.getOutputStream();
				bos = new BufferedOutputStream(os);
				
				
				is = new FileInputStream(file);
				bis = new BufferedInputStream(is);
				
				int length = 0;
				byte[] temp = new byte[1 * 1024 * 10];
				
				while ((length = bis.read(temp)) != -1) {
					bos.write(temp, 0, length);
				}
				bos.flush();
//				bis.close();
//				bos.close();
//				is.close();	
			} catch (Exception e1) {
				e1.printStackTrace();
			}finally {
				try {
					if(bis !=null){
						bis.close();
					}
					if(bos !=null){
						bos.close();
					}
					if(is !=null){
						is.close();
					}
					file.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

	  }
	}
	
	@RequestMapping(value = "getResourceRunningStatus", method = RequestMethod.POST)
	@ResponseBody
	public String getResourceRunningStatus(@RequestParam("resourceId") String resourceId){
		Map<String, Object> result = new HashMap<>();
		boolean isRunning = false;
		try{
			List<String> list = resourceService.listResourceVersionStatuses(resourceId);
			if(list != null && list.size() > 0){
				for(String status : list){
					//String status = "" + item.get("STATUS");
					if("30".equals(status) || "40".equals(status)){
						isRunning = true;
						break;
					}
				}
			}
			result.put("result", true);
			result.put("running", isRunning);
		}catch(Exception e){
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "获取组件绑定信息失败。reason:" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "getAuditStatusById", method = RequestMethod.POST)
	@ResponseBody
	public String getAuditStatusById(@RequestParam("auditId") String auditId){
		Map<String, Object> result = new HashMap<>();
		try{
			String status = resourceService.getAuditStatusById(auditId);
			if(status != null){
				result.put("result", true);
				result.put("status", status);
			}
			else{
				result.put("result", false);
				result.put("message", "审计id[" + auditId + "]不存在！");
			}
		}catch(Exception e){
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "获取组件审计状态失败。reason:" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "executeNewComponentVersionFlow", method = RequestMethod.POST)
	@ResponseBody
	public String executeNewComponentVersionFlow(@RequestParam("componentVersionId") String componentVersionId,
			@RequestParam("componentExecuteType") int componentExecuteType,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message", "执行" + (componentExecuteType == 0 ? "发布" : "回退") + "操作失败：非法session，无法获取用户信息！");
		}
		ITSMBatchReleasePool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				Map<String, Object> params = new HashMap<>();
				params.put("userId", user.getName());
				params.put("componentVersionId", componentVersionId);
				params.put("componentExecuteType", componentExecuteType);
				resourceService.executeNewComponentVersionFlow(params);
			}
		});
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("message", (componentExecuteType == 0 ? "发布" : "回退") + "开始执行！");
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "releaseNewComponentVersionsByBatch", method = RequestMethod.POST)
	@ResponseBody
	public String releaseNewComponentVersionsByBatch(@RequestParam("componentVersionIds") String componentVersionIds,
			@RequestParam("componentExecuteType") int componentExecuteType,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message", "批量发布失败：非法session，无法获取用户信息！");
		}
		ITSMBatchReleasePool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				Map<String, Object> params = new HashMap<>();
				params.put("userId", user.getName());
				params.put("componentVersionIds", componentVersionIds);
				params.put("componentExecuteType", componentExecuteType);
				resourceService.releaseNewComponentVersionsByBatch(params);
			}
		});
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("message", "批量" + (componentExecuteType == 0 ? "发布" : "回滚") + "开始执行！");
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "saveResourceVersionByRestApi", method = RequestMethod.POST)
	@ResponseBody
	public String saveResourceVersionByRestApi(@RequestParam("resourceId") String resourceId,
			@RequestParam("versionName") String versionName,
			@RequestParam(name="versionDesc", required=false, defaultValue="versionDesc") String versionDesc,
			@RequestParam("file") String fileName,
			@RequestParam("type") int type){
		try {
			resourceService.saveResourceVersionByRestApi(resourceId, versionName, versionDesc, fileName, type);
			return MessageHelper.wrap("result", true, "message", "资源版本注册成功");
		} catch (Exception e) {
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			//==============添加组件版本审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity("restApiUser", ResourceCode.COMPONENT, resourceName, ResourceCode.Operation.ADD, 0, "创建组件版本:" + versionName));
				}
			});
			//==============添加组件版本审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "资源版本注册失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value="getResourceLatestVersionById",method = RequestMethod.GET)
	@ResponseBody
	public String getResourceLatestVersionById(@RequestParam("resourceId") String resourceId){
		return resourceService.getLatestVersionId(resourceId);
	}
	
	@RequestMapping(value = "listAllSystems", method = RequestMethod.GET)
	@ResponseBody
	public String listAllSystems(@RequestParam(name="systemName", required = false, defaultValue="") String systemName) {
		try {
			Map<String, Object> param = new HashMap<>();
			param.put("systemName", systemName);
			List<Resource> list = resourceService.listAllSystems(param);
			return MessageHelper.wrap("result", true, "message", "获取系统列表成功" , "data", list);
		} catch (Exception e) {
			return MessageHelper.wrap("result", false, "message", "获取系统列表失败:" + e.getMessage());
		}
	}
}
