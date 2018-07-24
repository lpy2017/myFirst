package com.dc.appengine.appmaster.ws.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.ITemplateDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IBlueprintTemplateService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.service.impl.NodeLabelService;
import com.dc.appengine.appmaster.utils.BluePrint2Toolbar;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dcits.Common.entity.User;
@RestController
@RequestMapping("/ws/blueprint")
public class BlueprintRestService {
	private static final Logger log = LoggerFactory.getLogger(BlueprintRestService.class);

	@Resource
	IBlueprintService blueprintService;
	@Resource
	IApplicationService applicationService;
	@Resource
	NodeLabelService nodeLabelService;
	@Resource
	IResourceService resourceService;
	@Resource
	IInstanceService instanceService;
	@Resource
	IUserService userService;
	@Resource
	IBlueprintTemplateService blueprintTemplateService;
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	@Autowired
	@Qualifier("templateDao")
	ITemplateDao templateDao;
	
	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplateDao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintDao;
	
	@Resource
	IAudit auditService;
	
	private String HIGHLIGHT_VALUE = "value";
	private String HIGHLIGHT_HIGHLIGHT = "highlight";

	@RequestMapping(value = "/saveBlueprintTemplate", method = RequestMethod.POST)
	@ResponseBody
	public String saveBlueprintTemplate(@RequestParam("blueprint_info") String  blueprint_info,
			@RequestParam("blueprint_name") String  blueprint_name,
			@RequestParam("user_id") String  user_id,
			@RequestParam("blueprintDesc") String  blueprintDesc) {
		try{
			String template_id = UUIDGenerator.getUUID();
			//			BluePrint bp = JSON.parseObject(blueprint_info,BluePrint.class);
			//			bp.setBluePrintId(template_id);

			Map<String, String> param= new HashMap<String, String>();
			param.put("blueprint_id", template_id);
			param.put("blueprint_info", blueprint_info);
			param.put("blueprint_name", blueprint_name);
			param.put("blueprintDesc", blueprintDesc);
			param.put("user_id", user_id);
			blueprintService.saveBlueprintTemplate(param);
			return MessageHelper.wrap("result",true,"message","保存蓝图模版成功");
		}catch(Exception e){

			e.printStackTrace();
			log.error("保存蓝图模版失败", e);
			return MessageHelper.wrap("result",false,"message","保存蓝图模版失败");		

		}

	}

	public static String getResourceInfo(String blueprintInfo) {
		JSONObject info = JSON.parseObject(blueprintInfo);
		List<Map<String, Object>> nodes = (List<Map<String, Object>>) info.get("nodeDataArray");
		Map<String, Object> resource = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			if ("resource".equals(node.get("eleType"))) {
				resource.put(node.get("key") + "", node);
			}
		}
		return JSON.toJSONString(resource);
	}
	
	private Map<String, Object> checkLabel(String blueprintInfo) {
		JSONObject info = JSON.parseObject(blueprintInfo);
		List<Map<String, Object>> nodes = (List<Map<String, Object>>) info.get("nodeDataArray");
		Map<String, Object> resource = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			if ("resource".equals(node.get("eleType"))) {
				resource.put(node.get("key") + "", node);
			}
		}
		Set<String> keys = resource.keySet();
		Set<String> dict = new HashSet<>();
		List<Map<String, Object>> duplicatedLabel = new ArrayList<>();
		for (String key : keys) {
			Map<String, Object> node = (Map<String, Object>) resource.get(key);
			String labelType = (String) node.get("pooltype");
			String clusterId = (String) node.get("cluster_id");
			String nodeIps = (String) node.get("nodes");
			String nodeDisplay = (String) node.get("nodeDisplay");
			Map<String, Object> label = (Map<String, Object>) node.get("label");
			if(clusterId == null || nodeIps == null){
				Map<String, Object> result = new HashMap<>();
				result.put("result", false);
				result.put("message", "蓝图实例内资源池节点[" + nodeDisplay + "]的环境、节点都不允许为空，请双击资源池图标配置环境和节点！");
				return result;
			}
			String labelKey = (String) label.get("key");
			String labelValue = (String) label.get("value");
			int initSize = dict.size();
			dict.add(clusterId + labelType + labelKey + labelValue);
			int newSize = dict.size();
			if (initSize == newSize) {
				duplicatedLabel.add(node);
			}
		}
		if (duplicatedLabel.size() > 0) {
			//List<Object> poolKeys = new ArrayList<>();
			List<Object> poolNodeDisplays = new ArrayList<>();
			for (Map<String, Object> node : duplicatedLabel) {
				//poolKeys.add(node.get("key"));
				poolNodeDisplays.add(node.get("nodeDisplay"));
			}
			Map<String, Object> result = new HashMap<>();
			result.put("result", false);
			result.put("message", "蓝图实例内资源池节点" + poolNodeDisplays + "的[资源映射名称]存在重名，请重新命名！");
			return result;
		}
		for (String key : keys) {
			Map<String, Object> node = (Map<String, Object>) resource.get(key);
			String labelType = (String) node.get("pooltype");
			String clusterId = (String) node.get("cluster_id");
			Map<String, Object> label = (Map<String, Object>) node.get("label");
			String labelKey = (String) label.get("key");
			String labelValue = (String) label.get("value");
			Map<String, Object> param = new HashMap<>();
			param.put("clusterId", clusterId);
			param.put("key", labelKey);
			param.put("value", labelValue);
			param.put("type", labelType);
			int i = nodeLabelService.check(param);
			if (i > 0) {
				duplicatedLabel.add(node);
			}
		}
		if (duplicatedLabel.size() > 0) {
			//List<Object> poolKeys = new ArrayList<>();
			List<Object> poolNodeDisplays = new ArrayList<>();
			for (Map<String, Object> node : duplicatedLabel) {
				//poolKeys.add(node.get("key"));
				poolNodeDisplays.add(node.get("nodeDisplay"));
			}
			Map<String, Object> result = new HashMap<>();
			result.put("result", false);
			result.put("message", "蓝图实例内资源池节点" + poolNodeDisplays + "的[资源映射名称]已经存在，请重新命名！");
			return result;
		}
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		return result;
	}

	@RequestMapping(value = "/saveBlueprintInstance", method = RequestMethod.POST)
	@ResponseBody
	public String saveBlueprintInstance(
			@RequestParam("blueprint_info") String  blueprint_info,
			@RequestParam("blueprint_name") String  blueprint_name,
			@RequestParam("blueprint_desc") String  blueprint_desc,
			@RequestParam("blueprint_template_id") String  blueprint_template_id,
			@Context HttpServletRequest request) throws Throwable{
		try {
			//校验蓝图实例名唯一性
			List<String> blueInstanceNames = blueprintService.getBlueprints();
			if(blueInstanceNames.contains(blueprint_name)){
				return MessageHelper.wrap("result",false,"message","蓝图实例名称不可重复");
			}
			
			blueprint_info = formatUniqueLabel(blueprint_info, blueprint_name);
			
			// 校验环境和节点是否为空，校验资源层的label是否合法
			Map<String, Object> labelResult = this.checkLabel(blueprint_info);
			if (!(Boolean) labelResult.get("result")) {
				return JSON.toJSONString(labelResult);
			}
			
			String bluePrintInsId = UUIDGenerator.getUUID();
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
	
			//保存之前首先校验一下组件名唯一
			Set<String> names = new HashSet<>();
			for(Element e:bp.getNodeDataArray()){
				if(e.getEleType().equals(Element.COMPONENT)){
					String text = e.getText();
					if(names.contains(text)){
						return MessageHelper.wrap("result",false,"message","包含重名组件，组件名："+text);
					}else{
						names.add(text);
					}
				}
			}
			bp.setBluePrintId(bluePrintInsId);
			//保存蓝图实例
			Map<String,String> param = new HashMap<>();
			param.put("blue_instance_id", bluePrintInsId);
			param.put("blue_instance_name", blueprint_name);
			param.put("blue_instance_info", blueprint_info);
			param.put("blue_instance_desc", blueprint_desc);
			param.put("blueprint_template_id", blueprint_template_id);
			User user = (User) request.getSession().getAttribute("user");
			param.put("user_id", user.getId().toString());
			param.put("resource_pool_config", this.getResourceInfo(blueprint_info));
	
			blueprintService.saveBlueprintInstance(param);
			
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, blueprint_name, ResourceCode.Operation.ADD, 1, "生成蓝图实例:" + blueprint_name));
				}
			});
			//==============添加审计end=====================
			
			return MessageHelper.wrap("result",true,"message","保存蓝图实例成功");
		} catch (Exception e1) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					User user = (User) request.getSession().getAttribute("user");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, blueprint_name, ResourceCode.Operation.ADD, 0, "生成蓝图实例:" + blueprint_name));
				}
			});
			//==============添加审计end=====================
			e1.printStackTrace();
			return MessageHelper.wrap("result",false,"message","保存蓝图实例失败，message:"+e1.getMessage());
		}
	}

	@RequestMapping(value = "/updateAllBlueprint",method = RequestMethod.POST)
	@ResponseBody
	public String updateAllBlueprint(@RequestParam("blueprint_name") String blueprint_name,
			@RequestParam("blueprint_info") String  blueprint_info) {
		try {

			Map<String, String> param= new HashMap<String, String>();
			param.put("blueprint_info", blueprint_info);
			param.put("blueprint_name", blueprint_name);
			int a= blueprintService.updateAllBlueprint(param);

			return MessageHelper.wrap("result",true,"message","更新蓝图信息成功");
		} catch (Exception e) {
			log.error("failed to update blueprint info", e);
			return MessageHelper.wrap("result",false,"message","更新蓝图信息失败");
		}
	}

	@RequestMapping(value = "/getAllBlueprint",method = RequestMethod.GET)
	@ResponseBody
	public String getAllBlueprint(@RequestParam("blueprint_name") String blueprint_name) {
		try {
			String blueprint = blueprintService.getAllBlueprint(blueprint_name);
			if(blueprint == null){
				return MessageHelper.wrap("result",false,"message","获取蓝图信息失败");
			}
			return MessageHelper.wrap("result",true,"message",blueprint);

		} catch (Exception e) {
			log.error("failed to query blueprint info", e);
			return MessageHelper.wrap("result",false,"message","获取蓝图信息失败");
		}
	}


	@RequestMapping(value = "/delBlueprintTemplate",method = RequestMethod.POST)
	@ResponseBody
	public String delBlueprintTemplate(@RequestParam("blueprint_id") String blueprint_id) {
		try {
			//			int instances = blueprintService.getBlueprintInstanceNum(blueprint_id);
			//			if (instances > 0) {
			//				return MessageHelper.wrap("result",false,"message","存在蓝图实例，无法删除");
			//			}else{
			//				//				blueprintService.delBluePrintType(blueprint_id);
			blueprintService.delBlueprintTemplate(blueprint_id);
			return MessageHelper.wrap("result",true,"message","删除蓝图模版成功");
			//			}
		} catch (Exception e) {
			log.error("failed to delete blueprint info", e);
			return MessageHelper.wrap("result",false,"message","删除蓝图模版失败");
		}
	}

	@RequestMapping(value = "/findLabelsByCluster",method = RequestMethod.GET)
	@ResponseBody
	public String findLabelsByCluster(@RequestParam("cluster_id") String cluster_id) {
		try {
			Map<String, Object> param= new HashMap<String, Object>();

			param.put("cluster_id", cluster_id);
			param.put("type", "0");
			List<Map<String, Object>> labels;
			labels = nodeLabelService.findLabelsByCluster(param);
			String message = JSON.toJSONString(labels);
			return MessageHelper.wrap("result",true,"message",message);
		} catch (Exception e) {
			log.error("failed to findLabelsByCluster", e);
			return MessageHelper.wrap("result",false,"message","获取标签失败");
		}
	}
	//findNodesByLabel
	@RequestMapping(value = "/findNodesByLabel",method = RequestMethod.GET)
	@ResponseBody
	public String findNodesByLabel(@RequestParam("label_key") String label_key,
			@RequestParam("cluster_id") String cluster_id,
			@RequestParam("label_value") String label_value) {
		try {

			//	{
			//  "cluster_id": "uuid",
			//	"type": "0",
			//   "key": "db",
			//   "value": "mysql"
			//	}

			Map<String, Object> param= new HashMap<String, Object>();

			param.put("cluster_id", cluster_id);
			param.put("type", "0");
			param.put("key", label_key);
			param.put("value", label_value);
			List<Map<String, Object>> nodes;
			nodes = nodeLabelService.findNodesByLabel(param);
			String message = JSON.toJSONString(nodes);
			return MessageHelper.wrap("result",true,"message",message);
		} catch (Exception e) {
			log.error("failed to findLabelsByCluster", e);
			return MessageHelper.wrap("result",false,"message","获取node节点失败");
		}
	}




	@RequestMapping(value = "/sendDeployMessage", method = RequestMethod.GET)
	@ResponseBody
	public String sendDeployTemplate(@RequestParam("instance_id") String instance_id){

		try {
			return JSON.toJSONString(blueprintService.generateDployInstance(instance_id));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","执行部署操作失败");
		}
	}

	@RequestMapping(value = "/startBlueprintInstance", method = RequestMethod.GET)
	@ResponseBody
	public String startBlueprintInstance(@RequestParam("instance_id") String instanceId){
		try {
			return JSON.toJSONString(blueprintService.startBlueprintInstance(instanceId));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","执行启动操作失败");
		}

	}

	@RequestMapping(value = "/stopBlueprintInstance", method = RequestMethod.GET)
	@ResponseBody
	public String stopBlueprintInstance(@RequestParam("instance_id") String instanceId){
		try {
			return JSON.toJSONString(blueprintService.stopBlueprintInstance(instanceId));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","执行停止操作失败");
		}

	}

	@RequestMapping(value = "/destroyBlueprintInstance", method = RequestMethod.GET)
	@ResponseBody
	public String destroyBlueprintInstance(@RequestParam("instance_id") String instanceId){
		try {
			return JSON.toJSONString(blueprintService.destroyBlueprintInstance(instanceId));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","执行卸载操作失败");
		}
	}

	@RequestMapping(value = "/testGenerateFlow", method = RequestMethod.POST)
	@ResponseBody
	public String testGenerateFlow(@RequestParam("blueprint_info") String blueprint_info){
		BluePrint bp = JSON.parseObject(blueprint_info,BluePrint.class);
		try {
			//blueprintService.generateStartFlow(bp,UUID.randomUUID().toString());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "success";
	}

	@RequestMapping(value = "/listBlueprints",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprints(
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
		Page page = blueprintService.listBlueprints(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}

	@RequestMapping(value = "/listAllBlueprintInstances",method = RequestMethod.GET)
	@ResponseBody
	public String listAllBlueprintInstances(@RequestParam("userId") long userId){
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
		List<Map<String,Object>> result = blueprintService.listAllBlueprintInstances(condition);
		return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
	}

	@RequestMapping(value = "/listBlueprintInstances",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintInstances(
			@RequestParam("pageSize") int pageSize, 
			@RequestParam("pageNum") int pageNum,
			@RequestParam(name="blueprintId",required = false) String blueprintId,
			@RequestParam(name="instanceName",required = false) String instanceName){
		Map<String,Object> condition = new HashMap<>();
		condition.put("blueprintId", blueprintId);
		condition.put("instanceName", instanceName);
		Page page = blueprintService.listBlueprintInstances(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}

	@RequestMapping(value = "/startBlueprintInstanceOne", method = RequestMethod.GET)
	@ResponseBody
	public String startBlueprintInstanceOne(@RequestParam("appId") long appId){

		return JSON.toJSONString(blueprintService.opBlueprintInstanceOne(appId,"start"));
	}

	@RequestMapping(value = "/stopBlueprintInstanceOne", method = RequestMethod.GET)
	@ResponseBody
	public String stopBlueprintInstanceOne(@RequestParam("appId") long appId){

		return JSON.toJSONString(blueprintService.opBlueprintInstanceOne(appId,"stop"));
	}

	@RequestMapping(value = "/destroyBlueprintInstanceOne", method = RequestMethod.GET)
	@ResponseBody
	public String destroyBlueprintInstanceOne(@RequestParam("appId") long appId){
		//卸载
		return JSON.toJSONString(blueprintService.opBlueprintInstanceOne(appId,"destroy"));
	}

	@RequestMapping(value = "/updateBlueprintInstanceOne", method = RequestMethod.GET)
	@ResponseBody
	public String updateBlueprintInstanceOne(@RequestParam("appId") long appId){
		//卸载前记住要卸载的组件信息以及实例信息
		Application app = applicationService.getAppInfo(appId+"");
		List<Map<String,Object>> instances = instanceService.listInstancesByAppId(app.getId());
		blueprintService.opBlueprintInstanceOne(appId,"destroy");
		System.out.println(new Date());
		try {
			Thread.sleep(15000);                 //1000 毫秒，也就是1秒.
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		System.out.println(new Date());
		//存储卸载的实例 再部署
		if(instances != null && instances.size() != 0){
			for(Map<String,Object> instance : instances){
				Instance ins = new Instance();
				ins.setId(instance.get("instanceId").toString());
				ins.setAppId(String.valueOf(appId));
				ins.setNodeId(instance.get("nodeId").toString());
				ins.setAppVersionId(instance.get("resId").toString());
				ins.setComponentInput(JSON.parseObject(instance.get("inputTemp").toString(), new TypeReference<Map<String, String>>() {
				}));
				ins.setComponentOutput(JSON.parseObject(instance.get("outputTemp").toString(), new TypeReference<Map<String, String>>() {
				}));
				instanceService.saveInstance(ins);
			}
		}
		applicationService.saveApplication(app);
		blueprintService.testDeployOne(instances,app);
		return MessageHelper.wrap("result",true,"message","成功");
	}

	@RequestMapping(value = "/deployBlueprintInstanceOne", method = RequestMethod.GET)
	@ResponseBody
	public String deployBlueprintInstanceOne(@RequestParam("appId") long appId){
		//blueprintService.testDeployOne();
		Application app = applicationService.getAppInfo(appId+"");
		List<Map<String,Object>> instanceDeploy = new ArrayList<>();
		List<Map<String,Object>> instances = instanceService.listInstancesByAppId(appId);
		for(Map<String,Object> m : instances){
			String status = m.get("status").toString();
			if(status.equals("UNDEPLOYED")){
				instanceDeploy.add(m);
			}
		}
		if(instanceDeploy == null || instanceDeploy.size() <= 0){
			return MessageHelper.wrap("result",false,"message","已部署状态");
		}

		return JSON.toJSONString(blueprintService.testDeployOne(instanceDeploy,app));
	}

	@RequestMapping(value = "/getBluePrintTree", method = RequestMethod.GET)
	@ResponseBody
	public String getBluePrintTree(@RequestParam("blueprintInstanceId") String  blueprintInstanceId,
			@RequestParam("flowId") String  flowId,@RequestParam("op") String  op){
		String result = blueprintService.getBluePrintTree(Integer.valueOf(blueprintInstanceId),flowId,op);
		return result;
	}
	@RequestMapping(value = "/delBlueprintInstance",method = RequestMethod.POST)
	@ResponseBody
	public String delBlueprintInstance(@Context HttpServletRequest request,@RequestParam("blueprintInstanceId") int blueprintInstanceId) {
		final Map<String, Object> blueprintMap = blueprintService.getBlueprintInstanceById(blueprintInstanceId);
		final User user = (User) request.getSession().getAttribute("user");
		try {
			blueprintService.delBlueInstance(blueprintInstanceId);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.DELETE, 1, "删除蓝图实例:" + blueprintMap.get("INSTANCE_NAME")));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","删除蓝图实例成功");
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.DELETE, 0, "删除蓝图实例:" + blueprintMap.get("INSTANCE_NAME")));
				}
			});
			//==============添加审计end=====================
			log.error("failed to delete blueprintinstance info", e);
			return MessageHelper.wrap("result",false,"message","删除蓝图实例失败");
		}
	}

	@RequestMapping(value = "/generateSpecificBPFlow",method = RequestMethod.POST)
	@ResponseBody
	public String generateSpecificBPFlow(@RequestParam("bp") String bp){
		BluePrint b = JSON.parseObject(bp,BluePrint.class);
		try {
			return MessageHelper.wrap("result",true,"data",blueprintService.generateSpecificBPFlows(b));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","生成蓝图流程失败");
		}
	}

	@RequestMapping(value="/getFlowInfo",method = RequestMethod.GET)
	@ResponseBody
	public String getFlowInfo(@RequestParam("flowId") String flowId){
		try {
			return blueprintService.getFlowInfo(flowId);
		} catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return MessageHelper.wrap("result",false,"message","获取流程信息失败");
		}
	}

	@RequestMapping(value="/logRecord",method = RequestMethod.PUT)
	@ResponseBody
	public String logRecord(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String logJson){
		try {
			return blueprintService.logRecord(logJson);
		} catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
			return MessageHelper.wrap("result",false,"message","保存流程节点日志信息失败！");
		}
	}

	@RequestMapping(value="viewBluePrintInstanceFlow",method = RequestMethod.GET)
	@ResponseBody
	public String viewBluePrintInstanceFlow(
			@RequestParam("flowId") String flowId){
		return (String) blueprintService.getBlueprintType(flowId).get("FLOW_INFO"); 
	}

	@RequestMapping(value="/prepareSubFlowMessage",method = RequestMethod.POST)
	@ResponseBody
	public String prepareSubFlowMessage(@RequestBody Map subFlowInfo){
		try {
			return blueprintService.prepareSubFlowMessage(subFlowInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,
					"message","准备子流程消息遇到异常:"+e.getMessage());
		}
	}
	
	@RequestMapping(value="/prepareSubFlowMessageTest",method = RequestMethod.POST)
	@ResponseBody
	public String prepareSubFlowMessageTest(@RequestBody Map subFlowInfo){
		try {
			return blueprintService.prepareSubFlowMessageTest(subFlowInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,
					"message","准备子流程消息遇到异常:"+e.getMessage());
		}
	}
	
	@RequestMapping(value="/prepareNewSubFlowMessage",method = RequestMethod.POST)
	@ResponseBody
	public String prepareNewSubFlowMessage(@RequestBody Map subFlowInfo){
		try {
			return blueprintService.prepareNewSubFlowMessage(subFlowInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,
					"message","准备子流程消息遇到异常:"+e.getMessage());
		}
	}

	@RequestMapping(value="/addBlueprintFlow",method = RequestMethod.POST)
	@ResponseBody
	public String addBlueprintFlow(@RequestParam("flowType") String flowType,
			@RequestParam("flowInfo") String flowInfo,
			@RequestParam("blueprintInstanceId") String blueprintInstanceId){
		return blueprintService.addBlueprintFlow(blueprintInstanceId,flowType,flowInfo);
	}

	@RequestMapping(value="/listBpInstanceFlows",method = RequestMethod.GET)
	@ResponseBody
	public String listBpInstanceFlows(@RequestParam("blueprintInstanceId")String blueprintInstanceId){
		return blueprintService.listBpInstanceFlows(blueprintInstanceId);
	}

	@RequestMapping(value="/getFlowsByInstance",method = RequestMethod.GET)
	@ResponseBody
	public String getFlowsByInstance(@RequestParam("blueprintInstanceId") String blueprintInstanceId) {
		return JSON.toJSONString(blueprintService.getFlowsByBlueInstanceId(blueprintInstanceId));
	}

	@RequestMapping(value="/executeBlueprintFlow",method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String executeBlueprintFlow(@RequestParam("cdFlowId") String cdFlowId, 
				@RequestParam("blueprintInstanceId") String blueprintInstanceId,
				@RequestParam(name="extendedAttributes", defaultValue="{}") String extendedAttributes,
				@Context HttpServletRequest request){
		Map<String, Object> bpInfo = blueprintDao.getBlueprintInstance(blueprintInstanceId);
		Map<String,Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		User user = (User) request.getSession().getAttribute("user");
		String result = null;
//		if(user == null){
//			return result = MessageHelper.wrap("result",false,"message","执行蓝图流程失败：非法session，无法获取用户信息！");
//		}
		Map<String, String> param = new HashMap<>();
		if(user == null){
			param.put("_userName", "external");
		} else {
			param.put("_userName", user.getName());
		}
//		param.put("_userName", user.getName());
		
		try {
			JSON.parseObject(extendedAttributes, new TypeReference<Map<String, Object>>() {});
			param.put("_extendedAttributes", extendedAttributes);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = MessageHelper.wrap("result",false,"message","extendedAttributes[" + extendedAttributes + "]的json格式语法错误，请检查！");
			return result;
		}
		
		try {
			result = blueprintService.executeBlueprintFlow(cdFlowId, blueprintInstanceId, param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = MessageHelper.wrap("result",false,"message","执行蓝图流程失败：" + e.getMessage());
		}
		JSONObject resultJson = JSON.parseObject(result);
		int operateResult = resultJson.getBoolean("result") ? 1 : 0;
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				String userName = null;
				if (user == null) {
					userName = "external";
				} else {
					userName = user.getName();
				}
				auditService.save(new AuditEntity(userName, ResourceCode.BLUEPRINTFLOW, "" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.EXECUTE, operateResult, "蓝图实例[" + bpInfo.get("INSTANCE_NAME") + "]执行蓝图流程[" + flowInfo.get("FLOW_NAME") + "]"));
			}
		});
		return result;
	}

	@RequestMapping(value="updateBlueprintFlow",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintFlow(
			@RequestParam("flowInfo") String flowInfo,
			@RequestParam("flowId") String flowId){
		return blueprintService.updateBlueprintFlow(flowInfo,flowId);
	}
	@RequestMapping(value="getFlowNodeState",method = RequestMethod.POST)
	@ResponseBody
	public String getFlowNodeState(@RequestParam("nodes") String nodes,@RequestParam("flowId") String flowId){
		return blueprintService.getFlowNodeState(nodes,flowId);
	}

	@RequestMapping(value="checkBlueprintFlowUnique",method = RequestMethod.GET)
	@ResponseBody
	public String checkBlueprintFlowUnique(@RequestParam("bpInstanceId")String bpInstanceId,@RequestParam("type")String type){
		boolean b = blueprintService.checkBlueprintFlowUnique(bpInstanceId,type);
		if(b){
			return MessageHelper.wrap("result",true,"message","类型可用");
		}else{
			return MessageHelper.wrap("result",false,"message","类型已存在");
		}
	}

	@RequestMapping(value="getBlueprintTemplate",method=RequestMethod.GET)
	@ResponseBody
	public String getBlueprintTemplate(@RequestParam("bpName")String bpName){
		return JSON.toJSONString(blueprintService.getBlueprintTemplate(bpName));
	}

	@RequestMapping(value="getBlueprintComponents",method = RequestMethod.POST)
	@ResponseBody
	public String getBlueprintComponents(
			@RequestParam("blueprintId")String blueprintId){
		try {
			return MessageHelper.wrap("result",true,"message",JSON.toJSONString(blueprintService.getBlueprintComponents(blueprintId)));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","获取组件失败:" + e.getMessage());
		}
	}

	@RequestMapping(value="getBlueprintComponentConfig",method = RequestMethod.POST)
	@ResponseBody
	public String getBlueprintComponentConfig(@RequestParam("blueprintId")String blueprintId,
			@RequestParam("componentId")String componentId,
			@RequestParam("version")String version,
			@RequestParam("resourceVersionId")String resourceVersionId){

		try {
			return MessageHelper.wrap("result",true,"message",JSON.toJSONString(blueprintService.getBlueprintComponentConfig(blueprintId,componentId,version,resourceVersionId)));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","获取组件配置失败");
		}

	}
	@RequestMapping(value="computeBlueprintValues",method = RequestMethod.POST)
	@ResponseBody
	public String computeBlueprintValues(@RequestParam("blueprintId")String blueprintId){
		
		try {
			return MessageHelper.wrap("result",true,"message",JSON.toJSONString(new TreeMap<String, String>(blueprintService.computeBlueprintValues(blueprintId))));
		} catch (Exception e) {
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","获取组件配置失败");
		}
		
	}
	@RequestMapping(value="updateBlueprintComponentConfig",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintComponentConfig(@Context HttpServletRequest request,@RequestParam("blueprintId")String blueprintId,
			@RequestParam("componentId")int componentId,
			@RequestParam("currentVersion")String currentVersion,
			@RequestParam("currentInput")String currentInput,
			@RequestParam("currentOutput")String currentOutput,
			@RequestParam("targetVersion")String targetVersion,
			@RequestParam("targetInput")String targetInput,
			@RequestParam("targetOutput")String targetOutput,
			@RequestParam(name="smartFlag",required=false,defaultValue="0")String smartFlag,
			@RequestParam(name="executeFlag",required=false,defaultValue="1")String executeFlag){
		try {			
			//required=false,defaultValue="60000"
			currentInput = SensitiveDataUtil.getEncryptConfig(currentInput);
			currentOutput = SensitiveDataUtil.getEncryptConfig(currentOutput);
			targetInput = SensitiveDataUtil.getEncryptConfig(targetInput);
			targetInput = SensitiveDataUtil.getEncryptConfig(targetInput);
			Map<String, Object> param= new HashMap<String, Object>();
			param.put("componentId", componentId);
			param.put("currentVersion", currentVersion);
			param.put("currentInput", currentInput);
			param.put("currentOutput", currentOutput);
			param.put("targetVersion", targetVersion);
			param.put("targetInput", targetInput);
			param.put("targetOutput", targetOutput);
			param.put("smartFlag", smartFlag);
			param.put("executeFlag", executeFlag);
			blueprintService.updateBlueprintComponentConfig(param);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueprintId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.UPDATE, 1, "更新配置:" + JSON.toJSONString(param,true)));
				}
			});
			//==============添加审计end=====================
			Map<String, String>BluePrintConfig = 	new HashMap<String, String>();
			BluePrintConfig =  blueprintService.computeBlueprintValues(blueprintId);
			
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					blueprintService.uploadComponentFile(blueprintId,componentId,currentVersion,targetVersion);
				}
			};
			Thread thread = new Thread(runnable);
			thread.start();
			
			return MessageHelper.wrap("result",true,"message",JSON.toJSONString(new TreeMap<String, String>(BluePrintConfig)));
		} catch (Exception e) {
			log.error("failed to update blueprint info", e);
			return MessageHelper.wrap("result",false,"message","更新组件配置失败");
		}

	}
	@RequestMapping(value="updateBlueprintConfig",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintConfig(@Context HttpServletRequest request,@RequestParam("blueprintId")String blueprintId,
			@RequestParam("configValue")String configValue){
		final User user = (User) request.getSession().getAttribute("user");
		try {			
			Map<String, Object> BpInsparam= new HashMap<String, Object>();
			String encryptConfigValue = SensitiveDataUtil.getEncryptConfig(configValue);
			BpInsparam.put("blueprintId", blueprintId);
			BpInsparam.put("configValue", encryptConfigValue);
			blueprintService.updateBpInsKeyConfig(BpInsparam);
			
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueprintId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.UPDATE, 1, "更新动态变量配置:" + configValue));
				}
			});
			//==============添加审计end=====================
			
			return MessageHelper.wrap("result",true,"message","更新蓝图配置成功");
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueprintId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.UPDATE, 0, "更新动态变量配置:" + configValue));
				}
			});
			//==============添加审计end=====================
			log.error("failed to update blueprint info", e);
			return MessageHelper.wrap("result",false,"message","更新蓝图配置失败");
		}
		
	}
	
	@RequestMapping(value="getBlueprintResourcePoolConfigs",method = RequestMethod.POST)
	@ResponseBody
	public String getBlueprintResourcePoolConfigs(@RequestParam("blueprintId")String blueprintId){
		try {			
			String resourcePoolConfigs = blueprintService.getBlueprintResourcePoolConfigs(blueprintId);
			return MessageHelper.wrap("result", true,"message" ,"获取蓝图资源池配置成功", "data", resourcePoolConfigs);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result",false,"message","获取蓝图资源池配置失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value="updateBlueprintResourcePoolConfigs",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintResourcePoolConfigs(@Context HttpServletRequest request,@RequestParam("blueprintId")String blueprintId,
			@RequestParam("resourcePoolConfig")String resourcePoolConfig){
		final User user = (User) request.getSession().getAttribute("user");
		try {
			Map<String, String> map= new HashMap<String, String>();
			map.put("blueprintId", blueprintId);
			map.put("resourcePoolConfig", resourcePoolConfig);
			blueprintService.updateBlueprintResourcePoolConfigs(map);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstanceById(Integer.parseInt(blueprintId));
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.UPDATE, 1, "更新蓝图资源池配置:" + resourcePoolConfig));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","更新蓝图资源池配置成功");
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstanceById(Integer.parseInt(blueprintId));
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.UPDATE, 0, "更新蓝图资源池配置:" + resourcePoolConfig));
				}
			});
			//==============添加审计end=====================
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result",false,"message","更新蓝图资源池配置失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value="getBlueprintResourcePoolIns",method = RequestMethod.POST)
	@ResponseBody
	public String getBlueprintResourcePoolIns(@RequestBody String message){
		try {
			Map pool = blueprintService.getBlueprintResourcePoolIns(message);
			StringBuffer sb = new StringBuffer();
			List<String> ips = (List<String>)pool.get("ips");
			for(int i = 0; i < ips.size(); i++){
				sb.append(ips.get(i));
				if(i != ips.size() - 1){
					sb.append(",");
				}
			}
			return MessageHelper.wrap("result", true, "message", "获取动态ins成功", "ins", "" + pool.get("ins"), "ips", sb.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取动态ins失败:" + e.getMessage());
		}
	}
	
//	@RequestMapping(value="getFlowInstanceIds",method = RequestMethod.GET)
//	@ResponseBody
//	public String getFlowInstanceIds(@RequestParam("bluePrintInsId") String bluePrintInsId
//			,@RequestParam("flowName") String flowName){
//		String result = blueprintService.getFlowInstanceIds(bluePrintInsId,flowName);
//		return result;
//	}
	
	@RequestMapping(value="getBrotherBlueprintInstance",method = RequestMethod.GET)
	@ResponseBody
	public String getBrotherBlueprintInstance(
			 @RequestParam("blueprintId") String blueprintId
			,@RequestParam("blueprintInsId") String blueprintInsId){
		return blueprintService.getBrotherBlueprintInstance(blueprintId,blueprintInsId);
	}
	
	@RequestMapping(value="getBlueprintInstanceKV",method = RequestMethod.GET)
	@ResponseBody
	public String getBlueprintInstanceKV(@RequestParam("bluePrintInsId") String bluePrintInsId){
		return blueprintService.getBlueprintInstanceKV(bluePrintInsId);
	}
	
	@RequestMapping(value="existRunningInstance",method = RequestMethod.GET)
	@ResponseBody
	public String existRunningInstance(@RequestParam("cdFlowId") String cdFlowId,@RequestParam("blueprintInstanceId") String blueprintInstanceId){
		return blueprintService.existRunningInstance(cdFlowId,blueprintInstanceId);
	}
	
	@RequestMapping(value = "/saveSnapshotofBlueprintInstance", method = RequestMethod.POST)
	@ResponseBody
	public String saveSnapshotofBlueprintInstance(@Context HttpServletRequest request,
				@RequestParam("blueInstanceId") String blueInstanceId,
				@RequestParam("snapshotName") String snapshotName,
				@RequestParam("userId") int userId) {
		final User user = (User) request.getSession().getAttribute("user");
		try {
			if(snapshotName == null || "".equals(snapshotName.trim())){
				return MessageHelper.wrap("result", false, "message", "快照名称不允许为空，请重新命名！");
			}
			int i = blueprintService.checkSnapshotNameOfBlueprintInstance(blueInstanceId, snapshotName, userId);
			if(i > 0){
				return MessageHelper.wrap("result", false, "message", "当前蓝图实例已经存在[" + snapshotName + "]同名快照，请重新命名！");
			}
			else{
				int j = blueprintService.saveSnapshotOfBlueprintInstance(blueInstanceId, snapshotName, userId);
				if(j == 1){
					//==============添加审计start===================
					ThreadPool.service.execute(new Runnable(){
						@Override
						public void run(){
							Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueInstanceId);
							auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.ADD, 1, "保存蓝图快照:" + snapshotName));
						}
					});
					//==============添加审计end=====================
					return MessageHelper.wrap("result", true, "message", "保存蓝图快照成功！");
				}
				else{
					//==============添加审计start===================
					ThreadPool.service.execute(new Runnable(){
						@Override
						public void run(){
							Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueInstanceId);
							auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.ADD, 0, "保存蓝图快照:" + snapshotName));
						}
					});
					//==============添加审计end=====================
					return MessageHelper.wrap("result", false, "message", "保存蓝图快照失败！ reason[数据库保存快照错误！]");
				}
			}
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					Map<String, Object> blueprintMap = blueprintService.getBlueprintInstance(blueInstanceId);
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, ""+blueprintMap.get("INSTANCE_NAME"), ResourceCode.Operation.ADD, 0, "保存蓝图快照:" + snapshotName));
				}
			});
			//==============添加审计end=====================
			return MessageHelper.wrap("result", false, "message", "保存蓝图快照失败！ reason[" + e.getMessage() + "]");
		}
	}
	
	@RequestMapping(value = "/listSnapshotOfBlueprintInstance", method = RequestMethod.POST)
	@ResponseBody
	public String listSnapshotOfBlueprintInstance(
				@RequestParam("blueInstanceId") String blueInstanceId,
				@RequestParam("userId") int userId,
				@RequestParam(name="sortOrder",required=false) String sortOrder,
				@RequestParam(name="sortName",required=false) String sortName) {
		try {
			List<Map<String, String>> lists = blueprintService.listSnapshotOfBlueprintInstance(blueInstanceId, userId,sortName,sortOrder);
			if(lists.size() == 0){
				return MessageHelper.wrap("result", false, "message", "当前蓝图实例不存在快照！");
			}
			else{
				return MessageHelper.wrap("result", true, "data", JSON.toJSONString(lists));
			}
		} catch (Exception e) {
			return MessageHelper.wrap("result", false, "message", "获取快照列表失败！ reason[" + e.getMessage() + "]");
		}
	}
	
	@RequestMapping(value="/executeBlueprintRollBackFlow",method = RequestMethod.GET)
	@ResponseBody
	public String executeBlueprintRollBackFlow(
			@RequestParam("cdFlowId") String cdFlowId, 
			@RequestParam("blueprintInstanceId") String blueprintInstanceId,
			@RequestParam("snapshotId") String snapshotId,
			@RequestParam("userId") int userId,
			@Context HttpServletRequest request){
		Map<String, Object> bpInfo = blueprintDao.getBlueprintInstance(blueprintInstanceId);
		Map<String,Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		User user = (User) request.getSession().getAttribute("user");
		String result = null;
		if(user == null){
			return result = MessageHelper.wrap("result",false,"message","执行蓝图回滚流程失败：非法session，无法获取用户信息！");
		}
		Map<String, String> param = new HashMap<>();
		param.put("_userName", user.getName());
		param.put("_blueprintSnapshotId", snapshotId);
		try {
			result = blueprintService.executeBlueprintFlow(cdFlowId, blueprintInstanceId, param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = MessageHelper.wrap("result",false,"message","执行蓝图回滚流程失败：" + e.getMessage());
		}
		JSONObject resultJson = JSON.parseObject(result);
		int operateResult = resultJson.getBoolean("result") ? 1 : 0;
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTFLOW, "" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.ROLLBACK, operateResult, "蓝图实例[" + bpInfo.get("INSTANCE_NAME") + "]执行蓝图回滚流程[" + flowInfo.get("FLOW_NAME") + "]"));
			}
		});
		return result;
	}
	
	@RequestMapping(value = "/getRcResourcePoolByBlueprintInstance", method = RequestMethod.POST)
	@ResponseBody
	public String getRcResourcePoolByBlueprintInstance(
				@RequestParam("blueInstanceId") String blueInstanceId,
				@RequestParam("userId") int userId) {
		try {
			//String rp = blueprintService.getRcResourcePoolConfigByBlueprintInstance(blueInstanceId, userId);
			String rp = blueprintService.getRcResourcePoolByBlueprintInstance(blueInstanceId, userId);
			return MessageHelper.wrap("result", true, "message", "获取伸缩资源池配置成功", "data", rp);
		} catch (Exception e) {
			return MessageHelper.wrap("result", false, "message", "获取伸缩资源池配置失败！ reason[" + e.getMessage() + "]");
		}
	}
	
	@RequestMapping(value="/executeBlueprintRCFlow",method = RequestMethod.POST)
	@ResponseBody
	public String executeBlueprintRCFlow(
			@RequestParam("blueInstanceId") String blueInstanceId,
			@RequestParam("cdFlowIds") String cdFlowIds,
			@Context HttpServletRequest request){
		Map<String, Object> blueprintMap = blueprintService.getBlueprintInstanceById(Integer.parseInt(blueInstanceId));
		String instanceId = "" + blueprintMap.get("INSTANCE_ID");
		Map<String, String> rcFlows = JSON.parseObject(cdFlowIds, new TypeReference<Map<String,String>>(){});
		String increaseFlow = rcFlows.get("increase");
		String reduceFlow = rcFlows.get("reduce");
		if(reduceFlow != null && !"".equals(reduceFlow.trim())){
			blueprintService.updateBlueprintReducePoolConfig(instanceId);
		}
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message","伸缩流程执行失败：非法session，无法获取用户信息！");
		}
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				if(increaseFlow != null && !"".equals(increaseFlow.trim())){
					Map<String, String> inParams = new HashMap<String, String>();
					String[] increaseFlows = increaseFlow.split(",");
					inParams.put("_rc_increase", "true");
					inParams.put("_userName", user.getName());
					for(String inFlow : increaseFlows){
						Map<String,Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(inFlow);
						String result = null;
						try{
							result = blueprintService.executeBlueprintFlow(inFlow, instanceId, inParams);
						}catch(Exception e){
							log.error(e.getMessage(), e);
							result = MessageHelper.wrap("result",false,"message","执行蓝图扩展流程失败：" + e.getMessage());
						}
						JSONObject resultJson = JSON.parseObject(result);
						int operateResult = resultJson.getBoolean("result") ? 1 : 0;
						ThreadPool.service.execute(new Runnable(){
							@Override
							public void run(){
								auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTFLOW, "" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.INCREASE, operateResult, "蓝图实例[" + blueprintMap.get("INSTANCE_NAME") + "]执行蓝图扩展流程[" + flowInfo.get("FLOW_NAME") + "]"));
							}
						});
					}
				}
				if(reduceFlow != null && !"".equals(reduceFlow.trim())){
					Map<String, String> reParams = new HashMap<String, String>();
					String[] reduceFlows = reduceFlow.split(",");
					reParams.put("_rc_reduce", "true");
					reParams.put("_userName", user.getName());
					for(String reFlow : reduceFlows){
						Map<String,Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(reFlow);
						String result = null;
						try{
							result = blueprintService.executeBlueprintFlow(reFlow, instanceId, reParams);
						}catch(Exception e){
							log.error(e.getMessage(), e);
							result = MessageHelper.wrap("result",false,"message","执行蓝图收缩流程失败：" + e.getMessage());
						}
						JSONObject resultJson = JSON.parseObject(result);
						int operateResult = resultJson.getBoolean("result") ? 1 : 0;
						ThreadPool.service.execute(new Runnable(){
							@Override
							public void run(){
								auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTFLOW, "" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.REDUCE, operateResult, "蓝图实例[" + blueprintMap.get("INSTANCE_NAME") + "]执行蓝图收缩流程[" + flowInfo.get("FLOW_NAME") + "]"));
							}
						});
					}
				}
			}
		});
		thread.start();
		return MessageHelper.wrap("result", true, "message", "伸缩流程开始执行！请查看流程监控！");
	}
	
	/*
	 * blueprintId 蓝图实例id
	 * appName 组件名称(带key)
	 * flowType 流程类型
	 * flowName 流程名
	 * cdFlowId 子流程id
	 * versionConfig 组件版本配置
	 * snapshotId 快照id
	 */
	@RequestMapping(value="/addAndExecSecondFlow",method = RequestMethod.POST)
	@ResponseBody
	public String addAndExecSecondFlow(@RequestParam("blueprintId") String blueprintId,
									   @RequestParam("appName") String appName,
									   @RequestParam("flowType") String flowType,
									   @RequestParam("flowName") String flowName,
									   @RequestParam("cdFlowId") String cdFlowId,
									   @RequestParam("versionConfig") String versionConfig,
									   @RequestParam("snapshotId") String snapshotId,
									   @Context HttpServletRequest request){

		Map<String,Object> returnMap = new HashMap<String,Object>();
		Map<String, Object> blueprintTemplate = blueprintService.getBlueprintTemplateByInsId(blueprintId);
		//获取蓝图大info
		String bpInfo = "" + blueprintTemplate.get("INFO");
		String blueprintTemplateId = "" + blueprintTemplate.get("ID");
		
		Map<String, Object> instanceInfo = blueprintDao.getBlueprintInstance(blueprintId);
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return MessageHelper.wrap("result",false,"message","执行蓝图组件维护失败：非法session，无法获取用户信息！");
		}
		String componentName = appName.substring(0, appName.indexOf("-"));
		
		//查找是否已存在流程
		String secondFlowName = blueprintTemplate.get("NAME")+"-"+appName+"-"+flowType+"-"+flowName;
		Map<String, Object> param = new HashMap();
		param.put("flowName", secondFlowName);
		param.put("appName", appName);
		Map<String,Object> flowMap = blueprintTemplateService.getFlowByFlowName(param);
		if(flowMap==null){
			BluePrint2Toolbar tool = new BluePrint2Toolbar();
			String flowInfoGroup = tool.generateGroupInfoWithStartAndEnd(bpInfo,appName,cdFlowId,versionConfig,flowType);
			System.out.println("蓝图流程转换前为："+ flowInfoGroup);
			String flowInfo;
			try {
				flowInfo = tool.convertRuntimeInfo(flowInfoGroup);
			} catch (Exception e) {
				returnMap.put("result", false);
				returnMap.put("message", "组件二级流程中间态转换失败， reason[" + e.getMessage() + "]");
				return JSON.toJSONString(returnMap);
			}
			System.out.println("蓝图流程转换后为："+ flowInfo);
			
			String returnStr = blueprintTemplateService.addBlueprintTemplateFlow(blueprintTemplateId, secondFlowName, flowInfo, flowInfoGroup, appName);
			Map<String,Object> flowResult = JSON.parseObject(returnStr,Map.class);
			if(Boolean.valueOf(""+flowResult.get("result"))){
				Map<String, String> flowParam = new HashMap<>();
				flowParam.put("_userName", user.getName());
				Map<String,Object> newFlowMap = blueprintTemplateService.getFlowByFlowName(param);
				String subCdFlowId = newFlowMap.get("ID").toString();
				if(!StringUtils.isEmpty(snapshotId)){//回滚流程
					flowParam.put("_blueprintSnapshotId", snapshotId);
				}
				String result = null;
				try {
					result = blueprintService.executeBlueprintFlow(subCdFlowId, blueprintId, flowParam);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					result = MessageHelper.wrap("result",false,"message","执行蓝图组件维护失败：" + e.getMessage());
				}
				JSONObject resultJson = JSON.parseObject(result);
				int operateResult = resultJson.getBoolean("result") ? 1 : 0;
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTAPPMANAGE, componentName, ResourceCode.Operation.MANAGE, operateResult, "蓝图实例[" + instanceInfo.get("INSTANCE_NAME") + "]对组件[" + componentName + "]执行类型[" + flowType + "]流程[" + flowName + "]的维护操作"));
					}
				});
				return result;
			}else{
				returnMap.put("result", false);
				returnMap.put("message", "generate flow fail");
			}
		}else{
			String subCdFlowId = flowMap.get("ID").toString();
			
			BluePrint2Toolbar tool = new BluePrint2Toolbar();
			String flowInfoGroup = tool.generateGroupInfoWithStartAndEnd(bpInfo,appName,cdFlowId,versionConfig,flowType);
			System.out.println("蓝图流程转换前为："+ flowInfoGroup);
			String flowInfo;
			try {
				flowInfo = tool.convertRuntimeInfo(flowInfoGroup);
			} catch (Exception e) {
				returnMap.put("result", false);
				returnMap.put("message", "组件二级流程中间态转换失败， reason[" + e.getMessage() + "]");
				return JSON.toJSONString(returnMap);
			}
			System.out.println("蓝图流程转换后为："+ flowInfo);
			
			String returnStr = blueprintTemplateService.updateBlueprintTemplateFlow(flowInfoGroup, flowInfo, subCdFlowId);
			Map<String,Object> flowResult = JSON.parseObject(returnStr,Map.class);
			if(Boolean.valueOf(""+flowResult.get("result"))){
				Map<String, String> flowParam = new HashMap<>();
				flowParam.put("_userName", user.getName());
				if(!StringUtils.isEmpty(snapshotId)){//回滚流程
					flowParam.put("_blueprintSnapshotId", snapshotId);
				}
				String result = null;
				try {
					result = blueprintService.executeBlueprintFlow(subCdFlowId, blueprintId, flowParam);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					result = MessageHelper.wrap("result",false,"message","执行蓝图组件维护失败：" + e.getMessage());
				}
				JSONObject resultJson = JSON.parseObject(result);
				int operateResult = resultJson.getBoolean("result") ? 1 : 0;
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTAPPMANAGE, componentName, ResourceCode.Operation.MANAGE, operateResult, "蓝图实例[" + instanceInfo.get("INSTANCE_NAME") + "]对组件[" + componentName + "]执行类型[" + flowType + "]流程[" + flowName + "]的维护操作"));
					}
				});
				return result;
			}else{
				returnMap.put("result", false);
				returnMap.put("message", "generate flow fail");
			}
		}
		return JSON.toJSONString(returnMap);
	}

	@RequestMapping(value="/getSecondFlowsByInstance",method = RequestMethod.GET)
	@ResponseBody
	public String getSecondFlowsByInstance(@RequestParam("blueprintInstanceId") String blueprintInstanceId,
										   @RequestParam("appName") String appName) {
		return JSON.toJSONString(blueprintService.getSecondFlowsInstanceList(blueprintInstanceId,appName));
	}
	
	@RequestMapping(value="/getPluginConfigByBlueprintFlow",method = RequestMethod.POST)
	@ResponseBody
	public String getPluginConfigByBlueprintFlow(@RequestBody String message) {
		JSONObject json = JSON.parseObject(message);
		String blueprintFlowId = json.getString("blueprintFlowId");
		String pluginKey = json.getString("pluginKey");
		return JSON.toJSONString(blueprintService.getPluginConfigByBlueprintFlow(blueprintFlowId,pluginKey));
	}
	
	@RequestMapping(value="/listBlueprintByNameAndTemplateAndApp",method = RequestMethod.GET)
	@ResponseBody
	public String listBlueprintByNameAndTemplateAndApp(
			@Context HttpServletRequest request,
			@RequestParam(name="pageSize", required=false, defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum", required=false, defaultValue="1") int pageNum,
			@RequestParam(name="blueprintName", required=false, defaultValue="") String blueprintName,
			@RequestParam(name="templateName", required=false, defaultValue="") String templateName,
			@RequestParam(name="appName", required=false, defaultValue="") String appName,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
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
		if (blueprintName.equals("%") || blueprintName.equals("_") ||  blueprintName.equals("[") 
				||  blueprintName.equals("]") ||  blueprintName.equals("-") ||  blueprintName.equals("^") 
				||  blueprintName.equals("!") || blueprintName.equals("*")) {
			blueprintName = "\\" + blueprintName;
		}
		if (templateName.equals("%") || templateName.equals("_") ||  templateName.equals("[") 
				||  templateName.equals("]") ||  templateName.equals("-") ||  templateName.equals("^") 
				||  templateName.equals("!") || templateName.equals("*")) {
			templateName = "\\" + templateName;
		}
		Map<String, Object> params = new HashMap<>();
		params.put("userIds", userIds);
		params.put("blueprintName", blueprintName);
		params.put("templateName", templateName);
		List<Map<String, Object>> allBuleprints = blueprintService.listBlueprintByNameAndTemplateAndApp(params);
		SortUtil.sort(allBuleprints, SortUtil.getColunmName("bluePrintIns", sortName), sortOrder);
		List<Map<String,Object>> targeBlueprints = new ArrayList<>();
		if (allBuleprints != null && allBuleprints.size() > 0) {
			for (Map<String, Object> blueprint : allBuleprints) {
				if(!"".equals(appName)){
					boolean appExist = false;
					JSONObject info = JSON.parseObject("" + blueprint.get("INFO"));
					JSONArray nodes = info.getJSONArray("nodeDataArray");
					for(int j = 0; j < nodes.size(); j++){
						JSONObject node = nodes.getJSONObject(j);
						if("component".equals(node.getString("eleType"))){
							String nodeName = node.getString("nodeName");
							if(nodeName.toLowerCase().indexOf(appName.toLowerCase()) > -1){
								appExist = true;
								break;
							}
						}
					}
					if(!appExist){
						continue;
					}
				}
				targeBlueprints.add(blueprint);
				int blueInstanceId = (int) blueprint.get("ID");
				// 更新时间
				String updateTime = templateDao.getUpdateTime(blueInstanceId);
				blueprint.put("updateTime", updateTime);
				// 增加ip列表
				JSONObject resourcePoolInfo = JSON.parseObject((String) blueprint.get("RESOURCE_POOL_CONFIG"));
				Set<Entry<String, Object>> resourcePools = resourcePoolInfo.entrySet();
				Set<String> ips = new HashSet<>();
				try {
					for (Entry<String, Object> resourcePool : resourcePools) {
						Map<String, Object> resourcePoolMap = (Map<String, Object>) resourcePool.getValue();
						JSONArray nodes = null;
						Object nodesObject = resourcePoolMap.get("nodes");
						if(nodesObject != null){
							//COP
							if(nodesObject instanceof JSONArray){
								nodes = (JSONArray)nodesObject;
							}
							//master
							else if(nodesObject instanceof String){
								nodes = JSON.parseArray((String) nodesObject);
							}
							else{
							}
						}
						if(nodes != null){
							for (Object node : nodes) {
								ips.add((String) ((Map<String, Object>) node).get("ip"));
							}
						}
					}
				} catch (Exception e) {
					log.error("", e);
				}
				blueprint.put("ips", ips);
			}
		}
		List<Map<String,Object>> pageBlueprints = new ArrayList<>();
		if(targeBlueprints.size() > 0){
			int targetPageNum = (targeBlueprints.size())/pageSize;
			int targetPageSize = (targeBlueprints.size())%pageSize;
			if(targetPageNum < pageNum - 1){
				//无满足
			}
			if(targetPageNum == pageNum - 1){
				for(int m = targetPageNum*pageSize; m < targetPageNum*pageSize + targetPageSize; m++){
					pageBlueprints.add(targeBlueprints.get(m));
				}
			}
			else if((targetPageNum >= pageNum)){
				for(int n = (pageNum - 1)*pageSize; n < pageNum*pageSize; n++){
					pageBlueprints.add(targeBlueprints.get(n));
				}
			}
			else{
				//无
			}
		}
		Page page = new Page(pageSize, targeBlueprints.size());
		page.setPageNumber(pageNum);
		page.setRows(pageBlueprints);
		return JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value="updateBlueprintResourcePoolConfigs4COP",method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintResourcePoolConfigs4COP(@RequestParam("blueprintId")String blueprintId,
			@RequestParam("resourcePoolConfig")String resourcePoolConfig){
		try {
			Map<String, String> map= new HashMap<String, String>();
			map.put("blueprintId", blueprintId);
			map.put("resourcePoolConfig", resourcePoolConfig);
			blueprintService.updateBlueprintResourcePoolConfigs4COP(map);
			return MessageHelper.wrap("result",true,"message","更新蓝图资源池配置成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result",false,"message","更新蓝图资源池配置失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "compareConfig", method = RequestMethod.GET)
	public Object compareConfig(@RequestParam("blueprintInstanceUUID1") String blueprintInstanceUUID1, @RequestParam("blueprintInstanceUUID2") String blueprintInstanceUUID2, @RequestParam("blueprintInstanceId1") String blueprintInstanceId1, @RequestParam("blueprintInstanceId2") String blueprintInstanceId2) {
		return configCompare(blueprintInstanceUUID1, blueprintInstanceUUID2, blueprintInstanceId1, blueprintInstanceId2);
	}
	
	@RequestMapping(value="getBlueprintInstanceById",method=RequestMethod.GET)
	@ResponseBody
	public String getBlueprintInstanceById(@RequestParam("blueprintId") int blueprintId){
		return JSON.toJSONString(blueprintService.getBlueprintInstanceById(blueprintId));
	}
	
	@RequestMapping(value = "/cloneBlueprintInstance", method = RequestMethod.POST)
	@ResponseBody
	public String cloneBlueprintInstance(
			@RequestParam("blueprint_info") String blueprint_info,
			@RequestParam("blueprint_name") String blueprint_name,
			@RequestParam("blueprint_desc") String blueprint_desc,
			@RequestParam("blueprint_clone_id") int blueprint_clone_id,
			@Context HttpServletRequest request) throws Throwable{
		try {
			//获取被克隆的蓝图实例
			Map<String, Object> cloneDetail = blueprintService.getBlueprintInstanceById(blueprint_clone_id);
			String cloneInstancdId = "" + cloneDetail.get("INSTANCE_ID");
			String cloneInstanceName = "" + cloneDetail.get("INSTANCE_NAME");
			String cloneTemplateId = "" + cloneDetail.get("TEMPLATE_ID");
			
			//校验克隆出的蓝图实例名唯一性
			List<String> blueInstanceNames = blueprintService.getBlueprints();
			if(blueInstanceNames.contains(blueprint_name)){
				return MessageHelper.wrap("result",false,"message","存在重名的蓝图实例，请重新命名！");
			}
			
			//处理资源层的label
			blueprint_info = formatUniqueLabel(blueprint_info, blueprint_name);
			
			//校验克隆出的蓝图实例的环境和节点是否为空，校验资源层的label是否合法
			Map<String, Object> labelResult = this.checkLabel(blueprint_info);
			if (!(Boolean) labelResult.get("result")) {
				return JSON.toJSONString(labelResult);
			}
			
			String bluePrintInsId = UUIDGenerator.getUUID();
			BluePrint bp = JSON.parseObject(blueprint_info, BluePrint.class);
			
			//校验克隆出的蓝图实例的节点nodeDisplay是否重名
			Set<String> nodeDisplaySet = new HashSet<>();
			for(Element node : bp.getNodeDataArray()){
				String nodeDisplay = node.getNodeDisplay();
				if(nodeDisplaySet.contains(nodeDisplay)){
					return MessageHelper.wrap("result", false, "message", "蓝图中节点[" + nodeDisplay + "]存在重名，请重新命名！");
				}else{
					nodeDisplaySet.add(nodeDisplay);
				}
			}
	
			//校验克隆出的蓝图实例组件名是否唯一
			Set<String> names = new HashSet<>();
			for(Element e:bp.getNodeDataArray()){
				if(e.getEleType().equals(Element.COMPONENT)){
					String text = e.getText();
					if(names.contains(text)){
						return MessageHelper.wrap("result",false,"message","蓝图中组件[" + text + "]存在重名，请重新命名！");
					}else{
						names.add(text);
					}
				}
			}
			bp.setBluePrintId(bluePrintInsId);
			
			//保存蓝图实例
			Map<String,String> param = new HashMap<>();
			param.put("blue_instance_id", bluePrintInsId);
			param.put("blue_instance_name", blueprint_name);
			param.put("blue_instance_info", blueprint_info);
			param.put("blue_instance_desc", blueprint_desc);
			param.put("blueprint_template_id", cloneTemplateId);
			User user = (User) request.getSession().getAttribute("user");
			param.put("user_id", user.getId().toString());
			param.put("resource_pool_config", this.getResourceInfo(blueprint_info));
			blueprintService.saveBlueprintInstance(param);
			
			//克隆蓝图实例配置及其组件配置(不包括组件实例配置)
			blueprintService.cloneBlueprintInstanceConfig(cloneInstancdId,bluePrintInsId);

			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, blueprint_name, ResourceCode.Operation.CLONE, 1, "由蓝图实例[" + cloneInstanceName + "]克隆新的蓝图实例[" + blueprint_name + "]"));
				}
			});
			//==============添加审计end=====================
			
			return MessageHelper.wrap("result",true,"message","克隆蓝图实例成功");
		} catch (Exception e1) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					User user = (User) request.getSession().getAttribute("user");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.BLUEPRINTINSTANCE, blueprint_name, ResourceCode.Operation.CLONE, 0, "克隆新的蓝图实例[" + blueprint_name + "]"));
				}
			});
			//==============添加审计end=====================
			e1.printStackTrace();
			return MessageHelper.wrap("result",false,"message","克隆蓝图实例失败，message:"+e1.getMessage());
		}
	}
	
	private Map<String, Object> configCompare(String blueprintInstanceUUID1, String blueprintInstanceUUID2, String blueprintInstanceId1, String blueprintInstanceId2) {
		String resourcePoolConfig1 = blueprintService.getBlueprintResourcePoolConfigs(blueprintInstanceId1);
		String resourcePoolConfig2 = blueprintService.getBlueprintResourcePoolConfigs(blueprintInstanceId2);
		List<Application> components1 = blueprintService.getBlueprintComponents(blueprintInstanceUUID1);
		List<Application> components2 = blueprintService.getBlueprintComponents(blueprintInstanceUUID2);
		String blueprintInstanceConfig1 = blueprintService.getBlueprintInstanceKV(blueprintInstanceUUID1);
		String blueprintInstanceConfig2 = blueprintService.getBlueprintInstanceKV(blueprintInstanceUUID2);
		Map<String, Object> one = new HashMap<>();
		Map<String, Object> two = new HashMap<>();
		Map<String, Object> pools = comparePoolConfig(resourcePoolConfig1, resourcePoolConfig2);
		Map<String, Object> components = compareComponentConfig(components1, components2);
		Map<String, Object> blueprintInstances = compareBlueprintInstanceConfig(blueprintInstanceConfig1, blueprintInstanceConfig2);
		one.put("pools", pools.get("one"));
		one.put("components", components.get("one"));
		one.put("blueprintInstance", blueprintInstances.get("one"));
		two.put("pools", pools.get("two"));
		two.put("components", components.get("two"));
		two.put("blueprintInstance", blueprintInstances.get("two"));
		Map<String, Object> result = new HashMap<>();
		result.put("one", one);
		result.put("two", two);
		return result;
	}
	
	/**
	 * 配置报文格式如下：
	 * {
			"-1": {
				"nodeName": "静态资源池",
				"eleType": "resource",
				"loc": "-433.66319444444434 -195.55555555555537",
				"cluster_name": "chy_env1",
				"color": "#01c5c9",
				"source": "app/images/designer-icons/static.png",
				"label": {
					"value": "ssdf",
					"key": "compresmap"
				},
				"nodeDisplay": "pool1",
				"ins": 1,
				"cluster_id": "08364b72-b574-4612-8e17-8eb5e3ed4756",
				"des": "静态资源池",
				"nodes": "[{\\"ip\\":\\"10.126.3.216\\"}]",
				"text": "静态资源池",
				"category": "OfNodes",
				"isGroup": true,
				"key": -1,
				"pooltype": "1"
			},
			"-2": {
				"nodeName": "静态资源池",
				"eleType": "resource",
				"loc": "-90.88541666666663 -265.55555555555543",
				"cluster_name": "chy_env1",
				"color": "#01c5c9",
				"source": "app/images/designer-icons/static.png",
				"label": {
					"value": "sfwef",
					"key": "compresmap"
				},
				"nodeDisplay": "pool2",
				"ins": 1,
				"cluster_id": "08364b72-b574-4612-8e17-8eb5e3ed4756",
				"des": "静态资源池",
				"nodes": "[{\\"ip\\":\\"10.126.3.216\\"}]",
				"text": "静态资源池",
				"category": "OfNodes",
				"isGroup": true,
				"key": -2,
				"pooltype": "1"
			}
		}
	 * @param config1
	 * @param config2
	 * @return
	 */
	private Map<String, Object> comparePoolConfig(String config1, String config2) {
		JSONObject jsonObj1 = JSON.parseObject(config1);
		JSONObject jsonObj2 = JSON.parseObject(config2);
		Set<String> keys = jsonObj1.keySet();
		for (String key : keys) {
			Map<String, Object> map1 = jsonObj1.getJSONObject(key);
			Map<String, Object> map2 = jsonObj2.getJSONObject(key);
			//配置比对不显示"资源映射名称"配置
			map1.remove("label");
			map2.remove("label");
			if (map1 != null && map2 != null) {
				setHighlight(map1, map2, "cluster_name");
//				setHighlight(map1, map2, "nodeDisplay");
				setHighlight(map1, map2, "des");
				setHighlight(map1, map2, "nodes");
//				Map<String, Object> map1Label = (Map<String, Object>) map1.get("label");
//				Map<String, Object> map2Label = (Map<String, Object>) map2.get("label");
//				setHighlight(map1Label, map2Label, "value");
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("one", jsonObj1);
		result.put("two", jsonObj2);
		return result;
	}
	
	private boolean setHighlight(Map<String, Object> map1, Map<String, Object> map2, String key) {
		try {
			if (map1.containsKey(key) && map2.containsKey(key) && map1.get(key).equals(map2.get(key))) {
				Map<String, Object> map1Value = new HashMap<>();
				map1Value.put(HIGHLIGHT_VALUE, map1.get(key));
				map1Value.put(HIGHLIGHT_HIGHLIGHT, false);
				map1.put(key, map1Value);
				Map<String, Object> map2Value = new HashMap<>();
				map2Value.put(HIGHLIGHT_VALUE, map2.get(key));
				map2Value.put(HIGHLIGHT_HIGHLIGHT, false);
				map2.put(key, map2Value);
			} else {
				if (map1.containsKey(key)) {
					Map<String, Object> map1Value = new HashMap<>();
					map1Value.put(HIGHLIGHT_VALUE, map1.get(key));
					map1Value.put(HIGHLIGHT_HIGHLIGHT, true);
					map1.put(key, map1Value);
				}
				if (map2.containsKey(key)) {
					Map<String, Object> map2Value = new HashMap<>();
					map2Value.put(HIGHLIGHT_VALUE, map2.get(key));
					map2Value.put(HIGHLIGHT_HIGHLIGHT, true);
					map2.put(key, map2Value);
				}
			}
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}
	
	/**
	 * 报文格式
	 * [{
			"appName": "chycmd1-3",
			"blueInstanceId": 141,
			"clusterId": "08364b72-b574-4612-8e17-8eb5e3ed4756",
			"componentName": "chycmd1",
			"currentInput": "{\\"aaa\\":\\"101\\",\\"bbb\\":\\"102\\"}",
			"currentOutput": "{}",
			"currentVersion": "09b9214a-46d2-43c5-bf4b-cad40e34826b",
			"currentVersionName": "1.0",
			"deployId": 0,
			"destroyId": 0,
			"id": 257,
			"instancesNumber": 0,
			"key": -3,
			"nodeDisplay": "chya1",
			"rcFlag": false,
			"resourceId": "f0ec741f-641a-43de-9ae4-cb8ada304d34",
			"smartFlag": 0,
			"startId": 0,
			"stopId": 0,
			"targetInput": "{}",
			"targetOutput": "{}",
			"targetVersion": "",
			"userId": 1,
			"versionid": "-1"
		}]
	 * @param list1
	 * @param list2
	 * @return
	 */
	private Map<String, Object> compareComponentConfig(List<Application> list1, List<Application> list2) {
		Map<String, Application> map1 = new HashMap<>();
		for (Application application : list1) {
			map1.put(application.getAppName(), application);
		}
		Map<String, Application> map2 = new HashMap<>();
		for (Application application : list2) {
			map2.put(application.getAppName(), application);
		}
		JSONObject jsonObj1 = JSON.parseObject(JSON.toJSONString(map1));
		JSONObject jsonObj2 = JSON.parseObject(JSON.toJSONString(map2));
		Set<String> keys = jsonObj1.keySet();
		for (String key : keys) {
			Map<String, Object> component1 = jsonObj1.getJSONObject(key);
			Map<String, Object> component2 = jsonObj2.getJSONObject(key);
//			setHighlight(component1, component2, "nodeDisplay");
			setHighlight(component1, component2, "currentVersionName");
			setHighlight(component1, component2, "targetVersionName");
			translateFlag(component1, "executeFlag");
			translateFlag(component2, "executeFlag");
			setHighlight(component1, component2, "executeFlag");
			setHighlight4ComponentConfig(component1, component2, "currentInput");
			setHighlight4ComponentConfig(component1, component2, "currentOutput");
			setHighlight4ComponentConfig(component1, component2, "targetInput");
			setHighlight4ComponentConfig(component1, component2, "targetOutput");
		}
		Map<String, Object> result = new HashMap<>();
		result.put("one", jsonObj1);
		result.put("two", jsonObj2);
		return result;
	}

	private boolean setHighlight4ComponentConfig(Map<String, Object> component1, Map<String, Object> component2, String key) {
		if (component1.containsKey(key) && component2.containsKey(key)) {
			Map<String, Object> config1 = JSON.parseObject((String) component1.get(key));
			Map<String, Object> config2 = JSON.parseObject((String) component2.get(key));
			compareMap(config1, config2);
			component1.put(key, config1);
			component2.put(key, config2);
			return true;
		}
		if (component1.containsKey(key) && !component2.containsKey(key)) {
			Map<String, Object> config1 = JSON.parseObject((String) component1.get(key));
			Map<String, Object> config2 = new HashMap<>();
			compareMap(config1, config2);
			component1.put(key, config1);
			return true;
		}
		if (!component1.containsKey(key) && component2.containsKey(key)) {
			Map<String, Object> config1 = new HashMap<>();
			Map<String, Object> config2 = JSON.parseObject((String) component2.get(key));
			compareMap(config1, config2);
			component2.put(key, config2);
			return true;
		}
		return true;
	}
	
	private boolean compareMap(Map<String, Object> map1, Map<String, Object> map2) {
		Set<String> keys1 = map1.keySet();
		for (String key : keys1) {
			map1.put(key, encryptText("" + map1.get(key)));
			map2.put(key, encryptText("" + map2.get(key)));
			boolean flag = true;
			if (map2.containsKey(key) && map1.get(key).equals(map2.get(key))) {
				flag = false;
			}
			Map<String, Object> value = new HashMap<>();
			value.put(HIGHLIGHT_HIGHLIGHT, flag);
			value.put(HIGHLIGHT_VALUE, map1.get(key));
			map1.put(key, value);
		}
		Set<String> keys2 = map2.keySet();
		for (String key : keys2) {
			boolean flag = true;
			if (map1.containsKey(key) && ((Map<String, Object>) map1.get(key)).get(HIGHLIGHT_VALUE).equals(map2.get(key))) {
				flag = false;
			}
			Map<String, Object> value = new HashMap<>();
			value.put(HIGHLIGHT_HIGHLIGHT, flag);
			value.put(HIGHLIGHT_VALUE, map2.get(key));
			map2.put(key, value);
		}
		return true;
	}
	
	// 配置比对密码类以密文展示
	private String encryptText(String text){
		if(text.startsWith("DEC(") && text.endsWith(")")){
			text = text.substring(4, text.length() - 1);
			try {
				text = SensitiveDataUtil.encryptDesText(text);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(text.startsWith("ENC(") && text.endsWith(")")){
			text = text.substring(4, text.length() - 1);
		}
		return text;
	}
	
	// smartFlag、executeFlag翻译成中文
	private void translateFlag(Map<String, Object> component, String flag) {
		if(component.containsKey(flag)){
			int on_off = Integer.parseInt("" + component.get(flag));
			if(on_off == 0){
				component.put(flag, "关");
			}
			else if(on_off == 1){
				component.put(flag, "开");
			}
			else{
			}
		}
	}
	
	private boolean isNull(String str) {
		if (str != null && !str.equals("null")) {
			return false;
		}
		return true;
	}
	
	private Map<String, Object> compareBlueprintInstanceConfig(String config1, String config2) {
		if (!isNull(config1) && !isNull(config2)) {
			JSONObject jsonObj1 = JSON.parseObject(config1);
			JSONObject jsonObj2 = JSON.parseObject(config2);
			compareMap(jsonObj1, jsonObj2);
			Map<String, Object> result = new HashMap<>();
			result.put("one", jsonObj1);
			result.put("two", jsonObj2);
			return result;
		}
		if (!isNull(config1) && isNull(config2)) {
			JSONObject jsonObj1 = JSON.parseObject(config1);
			JSONObject jsonObj2 = new JSONObject();
			compareMap(jsonObj1, jsonObj2);
			Map<String, Object> result = new HashMap<>();
			result.put("one", jsonObj1);
			result.put("two", jsonObj2);
			return result;
		}
		if (isNull(config1) && !isNull(config2)) {
			JSONObject jsonObj1 = new JSONObject();
			JSONObject jsonObj2 = JSON.parseObject(config2);
			compareMap(jsonObj1, jsonObj2);
			Map<String, Object> result = new HashMap<>();
			result.put("one", jsonObj1);
			result.put("two", jsonObj2);
			return result;
		}
		if (isNull(config1) && isNull(config2)) {
			JSONObject jsonObj1 = new JSONObject();
			JSONObject jsonObj2 = new JSONObject();
			Map<String, Object> result = new HashMap<>();
			result.put("one", jsonObj1);
			result.put("two", jsonObj2);
			return result;
		}
		Map<String, Object> result = new HashMap<>();
		return result;
	}
	
	public static String formatUniqueLabel(String blueprint_info, String blueprint_name) {
		Map<String, Object> info = JSON.parseObject(blueprint_info, new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>> nodes = (List<Map<String, Object>>)info.get("nodeDataArray");
		if(nodes != null && nodes.size() > 0){
			for(Map<String, Object> node : nodes){
				String type = "" + node.get("eleType");
				if("resource".equals(type)){
					Object label = node.get("label");
					if(label != null){
						Map<String, String> kv = (Map<String, String>)label;
						String value = kv.get("value");
						//旧的或不规范标签
						if(value.indexOf("Label") == -1){
							kv.put("value", blueprint_name + "Label" + value);
						}
						else{
							//新建或克隆(编辑了node)蓝图实例标签
							if(value.startsWith("Label")){
								kv.put("value", blueprint_name + value);
							}
							//复制info新建或克隆(没编辑node)蓝图实例标签
							else if(!value.startsWith(blueprint_name)){
								kv.put("value", blueprint_name + value.substring(value.indexOf("Label"), value.length()));
							}
							else{
							}
						}
					}
				}
			}
			return JSON.toJSONString(info);
		}
		else{
			return blueprint_info;
		}
	}
	
}