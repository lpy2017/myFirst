package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintTemplateService;
import com.dc.appengine.appmaster.service.IPackageService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dcits.Common.entity.User;

@Controller
@RequestMapping("/ws/blueprintTemplate")
public class BlueprintTemplateRestService {
	
	private static final Logger log = LoggerFactory.getLogger(BlueprintTemplateRestService.class);
	
	@Resource
	IBlueprintTemplateService blueprintTemplateService;
	
	@Resource
	IUserService userService;
	
	@Resource
	IAudit auditService;
	
	@Resource
	IResourceService resourceService;
	
	@Resource
	IPackageService packageService;
	
	@RequestMapping(value = "/listBlueprintTemplates",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintTemplates(
			@RequestParam("pageSize") int pageSize, 
			@RequestParam("pageNum") int pageNum,
			@RequestParam(name="blueprintName",required = false) String blueprintName,
			@RequestParam("userId") long userId){
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
		condition.put("blueprintName", blueprintName);
		condition.put("userIds", userIds.toString().split(","));
		Page page = blueprintTemplateService.listBlueprintTemplates(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "/saveBlueprintTemplate", method = RequestMethod.POST)
	@ResponseBody
	public String saveBlueprintTemplate(@RequestParam("blueprint_info") String blueprint_info,
										@RequestParam("blueprint_name") String  blueprint_name,
										@RequestParam("user_id") String  user_id,
										@RequestParam("blueprintDesc") String  blueprintDesc,
										@RequestParam("op") String op,
										@RequestParam("userId") String userId) {
		try{
			BluePrint bp = JSON.parseObject(blueprint_info, BluePrint.class);
			//校验节点nodeDisplay是否重名
			Set<String> nodeDisplaySet = new HashSet<>();
			for(Element node : bp.getNodeDataArray()){
				String nodeDisplay = node.getNodeDisplay();
				if(nodeDisplaySet.contains(nodeDisplay)){
					return MessageHelper.wrap("result", false, "message", "蓝图中节点[" + nodeDisplay + "]存在重名，请重新命名!");
				}else{
					nodeDisplaySet.add(nodeDisplay);
				}
			}
			
			String template_id = UUIDGenerator.getUUID();

			Map<String, String> param= new HashMap<String, String>();
			param.put("blueprint_id", template_id);
			param.put("blueprint_info", blueprint_info);
			param.put("blueprint_name", blueprint_name);
			param.put("blueprintDesc", blueprintDesc);
			param.put("user_id", user_id);
			
			List<Map<String, Object>> templateList = blueprintTemplateService.getBlueprintTemplate(param);
			if("add".equalsIgnoreCase(op)){
				if(templateList.isEmpty()){
					blueprintTemplateService.saveBlueprintTemplate(param,op);
				}else{
					return MessageHelper.wrap("result",false,"message","存在同名的蓝图模版！");
				}
			}else{
				if(!templateList.isEmpty()){
					blueprintTemplateService.saveBlueprintTemplate(param,op);
				}
			}
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, blueprint_name, "add".equalsIgnoreCase(op)?ResourceCode.Operation.ADD:ResourceCode.Operation.UPDATE, 1, "蓝图模板:" + blueprint_name));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","保存蓝图模版成功");
		}catch(Exception e){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, blueprint_name, "add".equalsIgnoreCase(op)?ResourceCode.Operation.ADD:ResourceCode.Operation.UPDATE, 0, "蓝图模板:" + blueprint_name));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			log.error("保存蓝图模版失败", e);
			return MessageHelper.wrap("result",false,"message","保存蓝图模版失败");		
			
		}
	
	}
	
	@RequestMapping(value = "/delBlueprintTemplate",method = RequestMethod.POST)
	@ResponseBody
	public String delBlueprintTemplate(@RequestParam("blueprint_id") String blueprint_id,@RequestParam("userId") String userId) {
		try {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List blueprintList = new ArrayList();
					blueprintList.add(blueprint_id);
					List<Map<String, Object>> returnBlueprint = blueprintTemplateService.exportBlueprint(blueprintList);
					String name = ""+returnBlueprint.get(0).get("NAME");
					auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, name, ResourceCode.Operation.DELETE, 1, "删除蓝图模板:" + name));
				}
			});
			//==============添加审计end=====================
			return blueprintTemplateService.delBlueprintTemplate(blueprint_id);
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					List blueprintList = new ArrayList();
					blueprintList.add(blueprint_id);
					List<Map<String, Object>> returnBlueprint = blueprintTemplateService.exportBlueprint(blueprintList);
					String name = ""+returnBlueprint.get(0).get("NAME");
					auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, name, ResourceCode.Operation.DELETE, 0, "删除蓝图模板:" + name));
				}
			});
			//==============添加审计end=====================
			log.error("failed to delete blueprint info", e);
			return MessageHelper.wrap("result",false,"message","删除蓝图模版失败");
		}
	}
	
	@RequestMapping(value="checkBlueprintTemplateFlowUnique",method = RequestMethod.GET)
	@ResponseBody
	public String checkBlueprintTemplateFlowUnique(@RequestParam("blueprintId") String blueprintId,
										   		   @RequestParam("flowName") String flowName){
		boolean b = blueprintTemplateService.checkBlueprintFlowUnique(blueprintId,flowName);
		if(b){
			return MessageHelper.wrap("result",true,"message","类型可用");
		}else{
			return MessageHelper.wrap("result",false,"message","类型已存在");
		}
	}
	
	@RequestMapping(value="/addBlueprintTemplateFlow",method = RequestMethod.POST)
	@ResponseBody
	public String addBlueprintTemplateFlow(@RequestParam("flowName") String flowName,
										   @RequestParam("flowInfo") String flowInfo,
										   @RequestParam("flowInfoGroup") String flowInfoGroup,
										   @RequestParam("blueprintId") String blueprintId,
										   @RequestParam("appName") String appName,
										   @RequestParam("userId") String userId){
		List blueprintList = new ArrayList();
		blueprintList.add(blueprintId);
		List<Map<String, Object>> returnBlueprint = blueprintTemplateService.exportBlueprint(blueprintList);
		String name = ""+returnBlueprint.get(0).get("NAME");
		
		String result = blueprintTemplateService.addBlueprintTemplateFlow(blueprintId,flowName,flowInfo,flowInfoGroup,appName);
		JSONObject jo = JSONObject.parseObject(result);
		boolean isSucc = jo.getBoolean("result");
		String msg = jo.getString("message");
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, name, ResourceCode.Operation.ADD, isSucc?1:0, "新增蓝图流程：["+flowName+"] " + msg));
			}
		});
		//==============添加审计end=====================
		return result;
	}
	
	@RequestMapping(value="updateBlueprintTemplateFlow",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintTemplateFlow(@RequestParam("flowInfoGroup") String flowInfoGroup,
											  @RequestParam("flowInfo") String flowInfo,
											  @RequestParam("cdFlowId") String cdFlowId,
											  @RequestParam("userId") String userId){
		Map<String,Object> old = blueprintTemplateService.getBlueprintTemplateFlowInfo(cdFlowId);
		String flowName = old.get("FLOW_NAME").toString();
		String bluePrintInsId = old.get("BLUEPRINT_INSTANCE_ID").toString();
		List blueprintList = new ArrayList();
		blueprintList.add(bluePrintInsId);
		List<Map<String, Object>> returnBlueprint = blueprintTemplateService.exportBlueprint(blueprintList);
		String bpName = ""+returnBlueprint.get(0).get("NAME");
		
		String result = blueprintTemplateService.updateBlueprintTemplateFlow(flowInfoGroup,flowInfo,cdFlowId);
		JSONObject jo = JSONObject.parseObject(result);
		boolean isSucc = jo.getBoolean("result");
		String msg = jo.getString("message");
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, bpName, ResourceCode.Operation.UPDATE, isSucc?1:0, "更新蓝图流程：["+flowName+"] " + msg));
			}
		});
		//==============添加审计end=====================
		return result;
	}
	
	@RequestMapping(value="/listBlueprintTemplateFlow",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintTemplateFlow(@RequestParam("blueprintId")String blueprintId,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		return blueprintTemplateService.listBlueprintTemplateFlow(blueprintId,sortName,sortOrder);
	}
	
	@RequestMapping(value = "/delBlueprintTemplateFlow",method = RequestMethod.POST)
	@ResponseBody
	public String delBlueprintTemplateFlow(@RequestParam("cdFlowId") String cdFlowId,@RequestParam("userId") String userId) {
		Map<String,Object> old = blueprintTemplateService.getBlueprintTemplateFlowInfo(cdFlowId);
		String flowName = old.get("FLOW_NAME").toString();
		String bluePrintInsId = old.get("BLUEPRINT_INSTANCE_ID").toString();
		List blueprintList = new ArrayList();
		blueprintList.add(bluePrintInsId);
		List<Map<String, Object>> returnBlueprint = blueprintTemplateService.exportBlueprint(blueprintList);
		String bpName = ""+returnBlueprint.get(0).get("NAME");
		try {
			
			boolean b = blueprintTemplateService.delBlueprintTemplateFlow(cdFlowId);
			if(b){
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, bpName, ResourceCode.Operation.DELETE, 1, "删除蓝图流程：["+flowName+"] "));
					}
				});
				//==============添加审计end=====================
				return MessageHelper.wrap("result",true,"message","删除蓝图模版流程成功");
			}else{
				return MessageHelper.wrap("result",false,"message","删除蓝图模版流程失败-是否已存在蓝图实例");
			}
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, bpName, ResourceCode.Operation.DELETE, 0, "删除蓝图模版流程：["+flowName+"] "+e.getMessage()));
				}
			});
			//==============添加审计end=====================
			log.error("failed to delete blueprint info", e);
			return MessageHelper.wrap("result",false,"message","删除蓝图模版流程失败");
		}
	}
	
	@RequestMapping(value="viewBluePrintTemplateFlow",method = RequestMethod.GET)
	@ResponseBody
	public String viewBluePrintTemplateFlow(@RequestParam("cdFlowId") String cdFlowId){
		Map<String, Object> blueprintTemplateFlowInfo = blueprintTemplateService.getBlueprintTemplateFlowInfo(cdFlowId);
		if(blueprintTemplateFlowInfo!=null){
			return (String) blueprintTemplateFlowInfo.get("FLOW_INFO_GROUP"); 
		}
		return null;
	}
	
	@RequestMapping(value="exportBlueprint",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void exportBlueprint(@Context HttpServletResponse resp,@RequestParam("ids") String ids){
		String result =  blueprintTemplateService.exportBlueprint(ids);
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
	
	@RequestMapping(value="importBlueprint",method = RequestMethod.POST)
	@ResponseBody
	public String importBlueprint(@RequestParam("template") String template,
								  @RequestParam("blueprintFlow") String blueprintFlow,
								  @RequestParam("resource") String resource,
								  @RequestParam("flow") String flow,
								  @RequestParam("version") String version,
								  @RequestParam("ftl") String ftl,
								  @RequestParam("package") String packages,
								  @RequestParam("zip") String zip,
								  @RequestParam("userId") String userId){
		
		try {
			return blueprintTemplateService.importBlueprint(template,blueprintFlow,resource,flow,version,ftl,packages,zip,userId);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message",e.getMessage());
		}
	}
	
	@RequestMapping(value="listBlueprintTemplateByNameAndApp",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintTemplateByNameAndApp(
			@Context HttpServletRequest request,
			@RequestParam(name="pageSize", required=false, defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum", required=false, defaultValue="1") int pageNum, 
			@RequestParam(name="templateName", required=false, defaultValue="") String templateName,
			@RequestParam(name="appName", required=false, defaultValue="") String appName,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName
			){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message","用户未登录！");
		}
		String resultStr = userService.getSonsOfUser(user.getId());
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {});
		Long[] userIds = new Long[map.size()];
		int i = 0;
		for (long userId : map.keySet()) {
			userIds[i] = userId;
			i++;
		}
		if (templateName.equals("%") || templateName.equals("_") ||  templateName.equals("[") 
				||  templateName.equals("]") ||  templateName.equals("-") ||  templateName.equals("^") 
				||  templateName.equals("!") || templateName.equals("*")) {
			templateName = "\\" + templateName;
		}
		Map<String,Object> params = new HashMap<>();
		params.put("userIds", userIds);
		params.put("templateName", templateName);
		List<Map<String,Object>> allTemplates = blueprintTemplateService.listBlueprintTemplateByNameAndApp(params);
		SortUtil.sort(allTemplates, SortUtil.getColunmName("bluePrint", sortName), sortOrder);
		List<Map<String,Object>> targetTemplates = new ArrayList<>();
		if(allTemplates != null){
			if("".equals(appName)){
				targetTemplates = allTemplates;
			}
			else{
				for(Map<String,Object> template : allTemplates){
					JSONObject info = JSON.parseObject("" + template.get("INFO"));
					JSONArray nodes = info.getJSONArray("nodeDataArray");
					for(int j = 0; j < nodes.size(); j++){
						JSONObject node = nodes.getJSONObject(j);
						if("component".equals(node.getString("eleType"))){
							String nodeName = node.getString("nodeName");
							if(nodeName.toLowerCase().indexOf(appName.toLowerCase()) > -1){
								targetTemplates.add(template);
								break;
							}
						}
					}
				}
			}
		}
		List<Map<String,Object>> pageTemplates = new ArrayList<>();
		if(targetTemplates.size() > 0){
			int targetPageNum = (targetTemplates.size())/pageSize;
			int targetPageSize = (targetTemplates.size())%pageSize;
			if(targetPageNum < pageNum - 1){
				//无满足
			}
			if(targetPageNum == pageNum - 1){
				for(int m = targetPageNum*pageSize; m < targetPageNum*pageSize + targetPageSize; m++){
					pageTemplates.add(targetTemplates.get(m));
				}
			}
			else if((targetPageNum >= pageNum)){
				for(int n = (pageNum - 1)*pageSize; n < pageNum*pageSize; n++){
					pageTemplates.add(targetTemplates.get(n));
				}
			}
			else{
				//无
			}
		}
		Page page = new Page(pageSize, targetTemplates.size());
		page.setPageNumber(pageNum);
		page.setRows(pageTemplates);
		return JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value="getWorkpieceByBlueprintName",method = RequestMethod.GET)
	@ResponseBody
	public String getWorkpieceByBlueprintName(@RequestParam(name="blueprint_name") String blueprint_name){
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
		Set<String> appSet = blueprintTemplateService.getAppNameByBlueprintName(blueprint_name);
		for(String appName : appSet){
			String resourceId = resourceService.getResourceIdByName(appName);
			List<Version> versionList = resourceService.listResourceVersion(resourceId);
			for(Version version : versionList){
				returnList.add(packageService.getWorkpiece(version.getResourcePath()));
			}
		}
		return JSON.toJSONString(returnList,SerializerFeature.WriteDateUseDateFormat);
	}
	@RequestMapping(value="listBlueprintTemplatesByComponentName",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintTemplatesByComponentName(
			@Context HttpServletRequest request,
			@RequestParam(name="resourceName") String resourceName
			){
		User user = (User) request.getSession().getAttribute("user");
//		if(user == null){
//			return MessageHelper.wrap("result",false,"message","用户未登录！");
//		}
//		String resultStr = userService.getSonsOfUser(user.getId());
		String resultStr = userService.getSonsOfUser(1);
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {});
		Long[] userIds = new Long[map.size()];
		int i = 0;
		for (long userId : map.keySet()) {
			userIds[i] = userId;
			i++;
		}
		Map<String,Object> params = new HashMap<>();
		params.put("userIds", userIds);
		params.put("templateName", "");
		List<Map<String,Object>> allTemplates = blueprintTemplateService.listBlueprintTemplateByNameAndApp(params);
		List<Map<String,Object>> targetTemplates = new ArrayList<>();
		if(allTemplates != null){
			for(Map<String,Object> template : allTemplates){
				JSONObject info = JSON.parseObject("" + template.get("INFO"));
				JSONArray nodes = info.getJSONArray("nodeDataArray");
				for(int j = 0; j < nodes.size(); j++){
					JSONObject node = nodes.getJSONObject(j);
					if("component".equals(node.getString("eleType"))){
						String nodeName = node.getString("nodeName");
						if(nodeName.toLowerCase().indexOf(resourceName.toLowerCase()) > -1){
							targetTemplates.add(template);
//							template.remove("INFO");
							break;
						}
					}
				}
			}
		}
		return JSON.toJSONString(targetTemplates, SerializerFeature.WriteDateUseDateFormat);
	}
}
