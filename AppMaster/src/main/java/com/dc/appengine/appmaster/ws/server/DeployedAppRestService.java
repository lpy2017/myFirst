package com.dc.appengine.appmaster.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ReportAsSingleViolation;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.ITemplateService;
import com.dc.appengine.appmaster.service.impl.ComponentRollBackService;
import com.dc.appengine.appmaster.service.impl.ComponentUpdateService;
import com.dc.appengine.appmaster.service.impl.InstanceService;
import com.dc.appengine.appmaster.service.impl.RollBackService;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Controller
@RequestMapping("/ws/deployedApp")
public class DeployedAppRestService {
	private static final Logger log = LoggerFactory
			.getLogger(DeployedAppRestService.class);
	
	private final ReentrantLock lock = new ReentrantLock();

	@Resource
	IApplicationService applicationService;
	@Resource
	IInstanceService instanceService;
	@Resource
	ITemplateService templateService;
	@Resource
	IResourceService resourceService;
	@Resource
	RollBackService rollBackService;
	@Resource
	IBlueprintService blueprintService;
	@Resource
	ComponentUpdateService componentUpdateService;
	@Resource
	ComponentRollBackService componentRollBackService;
	

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	public String test() {
		String message = "{'operation':'destroy','instanceId':'eab09fd7-eea0-4205-97b1-cea3f174e94d'" +
				",'deployPath':'/tt','componentInput':'ss'}";
		int blueInstanceId = 1;
		long userId = 1L;
		int pageSize = 10;
		int pageNum = 1;
		int appId = 315;
		String snapId = "eab09fd7-eea0-4205-97b1-cea3f174e94d";
		return instanceService.updateStatus(message);

	}

	@RequestMapping(value = "/listApps", method = RequestMethod.GET)
	@ResponseBody
	public String listOperationApps(@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("userId") long userId,
			@RequestParam("blueInstanceId") int blueInstanceId,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		try {
			Page page = applicationService.listOperationApps(pageSize, pageNum,
					userId, blueInstanceId, sortName, sortOrder);
			return JSON.toJSONString(page);
		} catch (Exception e) {
			log.error("failed to query operation application", e);
			return MessageHelper.wrap("total", 0, "rows", null);
		}
	}

	@RequestMapping(value = "/listInstances", method = RequestMethod.GET)
	@ResponseBody
	public String listInstances(@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("appId") long appId,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		try {
			Page page = instanceService.listInstances(pageSize, pageNum, appId,sortName,sortOrder);
			return JSON.toJSONString(page,
					SerializerFeature.WriteDateUseDateFormat);
		} catch (Exception e) {
			log.error("failed to query instance list", e);
			return MessageHelper.wrap("total", 0, "rows", null);
		}
	}

	@RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getAppInfo(@RequestParam("appId") String appId) {
		try {
			Application app = applicationService.getAppInfo(appId);
			return JSON.toJSONString(app);
		} catch (Exception e) {
			log.error("failed to query operation application", e);
			return MessageHelper.wrap("result", false, "message", "查询app信息失败");
		}
	}

	@RequestMapping(value = "/delInstance", method = RequestMethod.POST)
	@ResponseBody
	public String delInstance(@RequestParam("instanceId") String instanceId) {

		return instanceService.deleteInstance(instanceId);
	}

	@RequestMapping(value = "/listBlueInstances", method = RequestMethod.GET)
	@ResponseBody
	public String listBlueInstance(@RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(name = "key", required = false) String key,
			@RequestParam("userId") long userId) {
		if (key != null && key != "") {
			if (key.equals("_") || key.equals("%") || key.equals("^")
					|| key.equals("*")) {
				key = "\\" + key;
			}
		}

		return JSON.toJSONString(templateService.listDeployedTemplate(pageSize,
				pageNum, userId, key));
	}

	@RequestMapping(value = "/getBlueInstanceDetailInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getBlueInstanceDetailInfo(
			@RequestParam("blueInstanceId") int blueInstanceId) {
		return templateService.getBlueInstanceDetail(blueInstanceId);
	}

	// 蓝图操作
	/**
	 * param: blueInstanceId 蓝图实例id type 操作类型
	 */
	@RequestMapping(value = "/operateBlue", method = RequestMethod.GET)
	@ResponseBody
	public String deployBlue(
			@RequestParam("blueInstanceId") String blueInstanceId,
			@RequestParam("type") String type) {

		try {
			String result = templateService.operateBlueInstance(blueInstanceId,
					type);
			return result;
		} catch (Exception e) {
			log.error("failed to deploy bluePrint", e);
			return MessageHelper.wrap("result", false, "massage", "exception");
		}
	}

	/**
	 * message消息体： { "operation": "", "instanceId": "", "deployPath": "",
	 * "componentName": "", "componentInput": {}, "pluginInput": {},
	 * "configUrl":"http://127.0.0.1:5091/masterl", "configVersionId":"",
	 * "configTemplate":{} }
	 * 
	 * @param request
	 * @param response
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/postOpUpdate", method = RequestMethod.PUT)
	@ResponseBody
	public String updateInstance(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String message) {
		return instanceService.updateStatus(message);
	}

	// 蓝图实例快照
	@RequestMapping(value = "/saveSnapshot", method = RequestMethod.POST)
	@ResponseBody
	public String saveSnapshot(
			@RequestParam("blueInstanceId") int blueInstanceId,
			@RequestParam("userId") long userId) {
		try {
			return templateService.saveSnapshotOfBlueInstance(blueInstanceId,
					userId);
		} catch (Exception e) {
			log.error("failed to saveSnapshot", e);
			return MessageHelper.wrap("result", false, "message", "exception:" + e.getMessage());
		}
	}

	// 应用快照生成删除列表等
	@RequestMapping(value = "/saveSnapshotOfApp", method = RequestMethod.POST)
	@ResponseBody
	public String saveSnapshotOfApp(@RequestParam("appId") int appId,
			@RequestParam("userId") long userId,
			@RequestParam("snapshotName") String snapshotName) {
		try {
			return templateService.saveSnapshotOfApp(appId, userId, snapshotName);
		} catch (Exception e) {
			log.error("failed to saveSnapshot", e);
			return MessageHelper.wrap("result", false, "message", "exception");
		}
	}

	@RequestMapping(value = "/listSnapshotsOfApp", method = RequestMethod.GET)
	@ResponseBody
	public String listSnapshotsOfApp(@RequestParam("appId") int appId,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("userId") long userId) {
		try {
			return templateService.listSnapshots(pageSize, pageNum, userId,
					appId);
		} catch (Exception e) {
			log.error("failed to listSnapshots", e);
			return MessageHelper.wrap("result", false, "massage", "exception");
		}
	}

	@RequestMapping(value = "/deleteSnapshotOfApp/{snapId}", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteSnapshotOfApp(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("snapId") String snapId) {
		try {
			return templateService.deleteSnapshot(snapId);
		} catch (Exception e) {
			log.error("failed to deleteSnapshot", e);
			return MessageHelper.wrap("result", false, "massage", "exception");
		}
	}
	
	@RequestMapping(value = "/listAppsOfCluster", method = RequestMethod.POST)
	@ResponseBody
	public String listAppsOfCluster(@RequestParam("clusterId") String clusterId,
			@RequestParam("userId") long userId){
		return applicationService.listAppsOfCluster(clusterId,userId);
	}
	
	@RequestMapping(value = "/getAppVersionList", method = RequestMethod.GET)
	@ResponseBody
	public String getAppVersionList(@RequestParam("appId") long appId,
			@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum){
		return instanceService.getAppVersionList(pageSize,pageNum,appId);
	}
	
	//这里的configId其实是组件版本id
	@RequestMapping(value = "/getConfigInfoByVersionId", method = RequestMethod.GET)
	@ResponseBody
	public String getConfigInfoByVersionId(@RequestParam("configId") String configId,
			@RequestParam("appId") long appId){
		String componentInput = instanceService.getConfigInfoByVersionId(configId,appId);
		return componentInput;
	}
	
	//更新保存一行配置参数
	@RequestMapping(value = "/updateLine", method = RequestMethod.POST)
	@ResponseBody
	public String updateLine(@RequestParam("resId") String resId,
			@RequestParam("appId") long appId,
			@RequestParam("key") String key,
			@RequestParam("value") String value){		
		return instanceService.updateComponentInputTemp(resId,appId,key,value);
	}
	
	//删除一行配置参数
	@RequestMapping(value="/removeLine", method = RequestMethod.GET)
	@ResponseBody
	public String removeLine(@RequestParam("resId") String resId,
			@RequestParam("appId") long appId,
			@RequestParam("key") String key,
			@RequestParam("value") String value){
		return instanceService.removeLine(resId,appId,key,value);
	}
	
	//获取组件的版本列表
	@RequestMapping(value="/getResourceVersion", method = RequestMethod.GET)
	@ResponseBody
	public String getResourceVersion(@RequestParam("appId") long appId){
		Application app = applicationService.getApp(appId);
		String resourceId = resourceService.getResourceIdByName(app.getAppName());
		//resourceService.listResourceVersion(resourceId);
		return JSON.toJSONString(resourceService.listResourceVersion(resourceId));
	}
	
	//更新組件實例的版本
	@RequestMapping(value="/updateResVersionInstance", method=RequestMethod.POST)
	@ResponseBody
	public String updateResVersionInstance(long appId,String resVersionId,String oldResVersionId){
		
		return instanceService.updateResVersionInstance(appId,resVersionId,oldResVersionId);
	}
	
	//获取组建的版本列表
	@RequestMapping(value="/getComponentVersionList", method=RequestMethod.GET)
	@ResponseBody
	public String getComponentVersionList(@RequestParam("appName") String appName){
		String resourceId = resourceService.getResourceIdByName(appName);
		//resourceService.listResourceVersion(resourceId);
		return JSON.toJSONString(resourceService.listResourceVersion(resourceId));
	}
	
	//快照回滚
	@RequestMapping(value = "/appSnapshotRollBack", method = RequestMethod.POST)
	@ResponseBody
	public String appSnapshotRollBack(@RequestParam("snapId") String snapId,
			@RequestParam("appId") long appId,
			@RequestParam("type") int type) {
		try {
			return rollBackService.appSnapshotRollBack(snapId, appId, type);
		} catch (Exception e) {
			log.error("failed to saveSnapshot", e);
			return MessageHelper.wrap("result", false, "massage", "exception");
		}
	}
	
	//快照信息
	@RequestMapping(value = "/getSnapshotInsInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getSnapshotInsInfo(@RequestParam("snapId") String snapId,
			@RequestParam("appId") long appId){
		return JSON.toJSONString(templateService.getSnapshotInsInfo(snapId,appId));
	}
	
	//快照回滚结果
	@RequestMapping(value = "/getSnapshotRollResultInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getSnapshotRollResultInfo(@RequestParam("snapId") String snapId,
			@RequestParam("appId") long appId){
		return JSON.toJSONString(templateService.getSnapshotRollResultInfo(snapId,appId));
	}
	
	@RequestMapping(value = "/updateAppAllIns", method = RequestMethod.POST)
	@ResponseBody
	public String updateApp(@RequestParam("appId") long appId,
			@RequestParam("resourceVersionId") String resourceVersionId,
			@RequestParam("config") String config,
			@RequestParam("json") String json){
		String appName = applicationService.getApp(appId).getAppName();
		long key = applicationService.getApp(appId).getKey();
		//获取旧版本id，FlowId，config
		List<Map<String,Object>> insList = instanceService.getInstanceList(appId);
		String oldVersionId = insList.get(0).get("resVersionId").toString();
		String input = insList.get(0).get("inputTemp").toString();
		String output = insList.get(0).get("outputTemp").toString();
		Map<String,Object> configMap = new HashMap<>();
		configMap.put("input", JSON.parseObject(input,new TypeReference<Map<String, Object>>() {}));
		configMap.put("output", JSON.parseObject(output,new TypeReference<Map<String, Object>>() {}));
		Map<String,Object> detail = resourceService.getResourceVersionDetail(oldVersionId);
		String flowInfo = detail.get("flows").toString();
		Map<String, String> flowMap = JSON.parseObject(flowInfo.replace("=", ":"),new TypeReference<Map<String, String>>() {});
		String flowIdStop = flowMap.get("stop");
		String flowIdDestroy = flowMap.get("destroy");
		String oldVersionName = detail.get("version").toString();
		//新的版本、配置、flowId
		Map<String,Object> detail_new = resourceService.getResourceVersionDetail(resourceVersionId);
		String flowInfo_new = detail_new.get("flows").toString();
		Map<String, String> flowMap_new = JSON.parseObject(flowInfo_new.replace("=", ":"),new TypeReference<Map<String, String>>() {});
		String flowIdStart = flowMap_new.get("start");
		String flowIdDeploy = flowMap_new.get("deploy");
		String newVersionName = detail_new.get("version").toString();
		Map<String,Object> jsonMap = JSON.parseObject(json,new TypeReference<Map<String, Object>>(){});
		List<Map<String,Object>> jsonList = JSON.parseObject(jsonMap.get("nodeDataArray").toString(),new TypeReference<List<Map<String, Object>>>(){});
		Map<String ,Object> configNewMap = JSON.parseObject(config,new TypeReference<Map<String, Object>>(){});
		for(Map<String,Object> m : jsonList){
			if(m.containsKey("componetName")){
				m.put("componetName", appName);
				//m.put("key",key);
				if(m.get("subflowName").toString().equals("stop")){
					m.put("subflowid", flowIdStop);
					m.put("versionId", oldVersionId);
					m.put("versionName", oldVersionName);
					m.put("config",configMap);
					m.put("desc",appName+"\n"+oldVersionName+"\n"+"stop");
				}else if(m.get("subflowName").toString().equals("destroy")){
					m.put("subflowid", flowIdDestroy);
					m.put("versionId", oldVersionId);
					m.put("versionName", oldVersionName);
					m.put("config",configMap);
					m.put("desc",appName+"\n"+oldVersionName+"\n"+"destroy");
				}else if(m.get("subflowName").toString().equals("deploy")){
					m.put("subflowid", flowIdDeploy);
					m.put("versionId", resourceVersionId);
					m.put("versionName", newVersionName);
					m.put("config",configNewMap);
					m.put("desc",appName+"\n"+newVersionName+"\n"+"deploy");
				}else if(m.get("subflowName").toString().equals("start")){
					m.put("subflowid", flowIdStart);
					m.put("versionId", resourceVersionId);
					m.put("versionName", newVersionName);
					m.put("config",configNewMap);
					m.put("desc",appName+"\n"+newVersionName+"\n"+"start");
				}
				
			}
			int insNum = instanceService.getInstanceList(appId).size();
			if(m.get("category").toString().equals("gateway")
					&& m.get("flowcontroltype").toString().equals("8")){
				m.put("_loopCount", "<" + insNum);
				m.put("loopCount", "<" + insNum);
				m.put("text", "i 小于 " + insNum);
			}
		}
		jsonMap.put("nodeDataArray",jsonList);
		return JSON.toJSONString(jsonMap);
	}
	
	//回滚json转化
	@RequestMapping(value = "/rollbackJsonTo", method = RequestMethod.POST)
	@ResponseBody
	public String rollbackJsonTo(@RequestParam("appId") long appId,
			@RequestParam("snapId") String snapId,
			@RequestParam("json") String json){
		String appName = applicationService.getApp(appId).getAppName();
		//获取旧版本id，FlowId，
		List<Map<String,Object>> insList = instanceService.getInstanceList(appId);
		String oldVersionId = insList.get(0).get("resVersionId").toString();
		Map<String,Object> detail = resourceService.getResourceVersionDetail(oldVersionId);
		String flowInfo = detail.get("flows").toString();
		Map<String, String> flowMap = JSON.parseObject(flowInfo.replace("=", ":"),new TypeReference<Map<String, String>>() {});
		String flowIdStop = flowMap.get("stop");
		String flowIdDestroy = flowMap.get("destroy");
		String oldVersionName = detail.get("version").toString();
		
		// 快照实例
		List<Map<String, Object>> snapshotInstanceList = templateService.getSnapshotInsInfo(snapId,appId);
		String newVersionId = snapshotInstanceList.get(0).get("resVersionId").toString();
		//新的版本、配置、flowId
		Map<String,Object> detail_new = resourceService.getResourceVersionDetail(newVersionId);
		String flowInfo_new = detail_new.get("flows").toString();
		Map<String, String> flowMap_new = JSON.parseObject(flowInfo_new.replace("=", ":"),new TypeReference<Map<String, String>>() {});
		String flowIdStart = flowMap_new.get("start");
		String flowIdDeploy = flowMap_new.get("deploy");
		String newVersionName = detail_new.get("version").toString();
		Map<String,Object> jsonMap = JSON.parseObject(json,new TypeReference<Map<String, Object>>(){});
		List<Map<String,Object>> jsonList = JSON.parseObject(jsonMap.get("nodeDataArray").toString(),new TypeReference<List<Map<String, Object>>>(){});
		for(Map<String,Object> m : jsonList){
			if(m.containsKey("componetName")){
				m.put("componetName", appName);
				//m.put("key",key);
				if(m.get("subflowName").toString().equals("stop")){
					m.put("subflowid", flowIdStop);
					m.put("versionId", oldVersionId);
					m.put("versionName", oldVersionName);
					m.put("desc",appName+"\n"+oldVersionName+"\n"+"stop");
				}else if(m.get("subflowName").toString().equals("destroy")){
					m.put("subflowid", flowIdDestroy);
					m.put("versionId", oldVersionId);
					m.put("versionName", oldVersionName);
					m.put("desc",appName+"\n"+oldVersionName+"\n"+"destroy");
				}else if(m.get("subflowName").toString().equals("deploy")){
					m.put("subflowid", flowIdDeploy);
					m.put("versionId", newVersionId);
					m.put("versionName", newVersionName);
					m.put("desc",appName+"\n"+newVersionName+"\n"+"deploy");
				}else if(m.get("subflowName").toString().equals("start")){
					m.put("subflowid", flowIdStart);
					m.put("versionId", newVersionId);
					m.put("versionName", newVersionName);
					m.put("desc",appName+"\n"+newVersionName+"\n"+"start");
				}
						
			}
			int insNum = snapshotInstanceList.size();
			if(m.get("category").toString().equals("gateway")
					&& m.get("flowcontroltype").toString().equals("8")){
				m.put("_loopCount", "<" + insNum);
				m.put("loopCount", "<" + insNum);
				m.put("text", "i 小于 " + insNum);
			}
		}
		jsonMap.put("nodeDataArray",jsonList);
		return JSON.toJSONString(jsonMap);
	}
	
	@RequestMapping(value = "/saveAppUpdateOrRollbackFlow", method = RequestMethod.POST)
	@ResponseBody
	public String saveAppUpdateOrRollbackFlow(@RequestParam("appId") long appId,
			@RequestParam("userId") String userId,
			@RequestParam("json") String json,
			@RequestParam("type") String type,
			@RequestParam("snapId") String snapId){
		//给json添加标识
		String jsonAll = templateService.jsonToCorrectJson(json,appId);
		JSONObject jsonObj = JSON.parseObject(jsonAll);
		List<Map<String, Object>> nodes =(List<Map<String,Object>>) jsonObj.get("nodeDataArray");
		String resourceVersionId = "";
		String config = "";
		for(Map<String,Object> m : nodes){
			if(m.containsKey("componetName") && m.get("subflowName").equals("deploy")){
				resourceVersionId = m.get("versionId").toString();
				config = m.get("config").toString();
				break;
			}
		}
		Map<String, String> OpDict = new HashMap<>();
		//然后生成flowid
		String flowId = blueprintService.cdflowToSmartflow(jsonAll);
		if(flowId != null){
			//存库ma_application_flow_op userId,jsonAll,appId,type,flowId
			String id = UUID.randomUUID().toString();
			Map<String,Object> m = new HashMap<>();
			m.put("id", id);
			m.put("appId", appId);
			m.put("flowId", flowId);
			m.put("json", jsonAll);
			m.put("type", type);
			m.put("userId", userId);
			//存库
			templateService.saveUpdateInfo(m);
			if(type.equals("updateFlow") || type.equals("updateQuickFlow")){
				//调用升级接口
				String temp = componentUpdateService.update(appId, resourceVersionId, config, flowId);
				System.out.println(temp);
				Map<String,Object> m1 = JSON.parseObject(temp,new TypeReference<Map<String, Object>>(){});
				m1.put("flowId", flowId);
				System.out.println(JSON.toJSONString(m1));
				return JSON.toJSONString(m1);
			}else if(type.equals("rollbackFlow") || type.equals("rollbackQuickFlow")){
				//调用回滚接口
				String temp = componentRollBackService.rollBackFromSnapshot(snapId, appId, type, flowId);
				System.out.println(temp);
				Map<String,Object> m1 = JSON.parseObject(temp,new TypeReference<Map<String, Object>>(){});
				m1.put("flowId", flowId);
				System.out.println(JSON.toJSONString(m1));
				return JSON.toJSONString(m1);
			}
			
			
		}
		return MessageHelper.wrap("result", false, "massage", "json error exception");
	}
	
	@RequestMapping(value = "/listApps", method = RequestMethod.POST)
	@ResponseBody
	public String listApps(@RequestParam("blue_instance_id") long blue_instance_id,
			@RequestParam("userId") long userId){
		return applicationService.listApps(blue_instance_id,userId);
	}
	
	// 按实例制作快照，如属于同一组件的其他实例制作快照，则在已作的快照内合并当前实例快照，快照id不变
	@RequestMapping(value = "/saveSnapshotOfInstance", method = RequestMethod.POST)
	@ResponseBody
	public String saveSnapshotOfInstance(@RequestParam("instanceId") String instanceId) {
		lock.lock();//获取锁，防止同一组件的多个实例并发制作快照
		try {
			return templateService.saveSnapshotOfInstance(instanceId);
		} catch (Exception e) {
			log.error("failed to saveSnapshot", e);
			return MessageHelper.wrap("result", false, "snapId", null, "message", "制作快照失败:" + e.getMessage());
		} finally{
			lock.unlock();//释放锁
		}
	}
	
	// 蓝图操作
	/**
	 * param: blueInstanceId 蓝图实例id
	 */
	@RequestMapping(value = "/getFlowIdByBlueInstanceId", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowIdByBlueInstanceId(
			@RequestParam("blueInstanceId") String blueInstanceId) {

		try {
			List result = templateService.getFlowIdByBlueInstanceId(blueInstanceId);
			return JSON.toJSONString(result);
		} catch (Exception e) {
			log.error("failed to deploy bluePrint", e);
			return MessageHelper.wrap("result", false, "massage", "exception");
		}
	}
}