package com.dc.appengine.appmaster.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.impl.COPService;
import com.dc.appengine.appmaster.utils.AESUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/cop")
public class COPRestService {

	@Resource
	private COPService service;
	
	@Resource
	IBlueprintService blueprintService;
	
	@Resource
	IAudit auditService;
	
	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplateDao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintDao;
	
	private static final Logger log = LoggerFactory.getLogger(COPRestService.class);

	/**
	 * 查询蓝图模板列表
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * {
			"pageNumber": 1,
			"pageSize": 10,
			"startRowNum": 1,
			"endRowNum": 10,
			"total": 5,
			"totalPageNum": 1,
			rows: [{
				"id": "blueprintTemplateId",
				"name": "blueprintTemplateName",
				"description": "description of blueprintTemplate"
			}]
		}
	 */
	@RequestMapping(value = "blueprintTemplate", method = RequestMethod.GET)
	public Object getBlueprintTemplates(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum) {
		return service.getBlueprintTemplates(pageNum, pageSize);
	}
	
	/**
	 * 获取蓝图流程列表
	 * @param blueprintTemplateId
	 * @return
	 * [{
			"flowId": "flowId",
			"flowName": "flowName"
		}]
	 */
	@RequestMapping(value = "blueprintTemplate/{blueprintTemplateId}/flow", method = RequestMethod.GET)
	public Object getFlows(@PathVariable("blueprintTemplateId") String blueprintTemplateId) {
		return service.getFlows(blueprintTemplateId);
	}
	
	/**
	 * 获取蓝图模板详情
	 * @param blueprintTemplateId
	 * @return
	 * [{
			"resource": {
				"key": -1,
				"name": "资源映射层的名称",
				"description": "资源映射层的描述"
			},
			"components": [{
				"name": "componentName",
				"id": "组件的唯一标识",
				"versions": [{
					"id": "versionId",
					"name": "versionName",
					"config": {
						"input": {
							"key": "value"
						},
						"output": {
							"key": "value"
						}
					}
				}]
			}]
		}]
	 */
	@RequestMapping(value = "blueprintTemplate/{blueprintTemplateId}", method = RequestMethod.GET)
	public Object getBlueprintTemplate(@PathVariable("blueprintTemplateId") String blueprintTemplateId) {
		Map<String, Object> blueprintTemplate = service.getBlueprintTemplate(blueprintTemplateId);
		String info =  (String) blueprintTemplate.get("info");
		return refactoryNodeArray((List<Map<String,Object>>) JSON.parseObject(info).get("nodeDataArray"));
	}
	
	private List<Map<String, Object>> refactoryNodeArray(List<Map<String, Object>> nodeArray) {
		List<Map<String, Object>> blueprintTemplate = new ArrayList<>();
		for (Map<String, Object> node : nodeArray) {
			if (node.get("eleType") != null && "resource".equals(node.get("eleType"))) {
				Map<String, Object> layer = new HashMap<>();
				layer.put("key", node.get("key"));
				layer.put("name", node.get("text"));
				layer.put("description", node.get("des"));
				Map<String, Object> resource = new HashMap<>();
				resource.put("resource", layer);
				blueprintTemplate.add(resource);
			}
		}
		for (Map<String, Object> resource : blueprintTemplate) {
			List<Map<String, Object>> components = new ArrayList<>();
			for (Map<String, Object> node : nodeArray) {
				if (node.get("group") != null && ((Map<String, Object>) resource.get("resource")).get("key").equals(node.get("group"))) {
					Map<String, Object> component = new HashMap<>();
					component.put("name", node.get("text"));
					component.put("id", node.get("id"));
					List<Map<String, Object>> versions = service.getComponentVersions((String) node.get("id"));
					for (Map<String, Object> version : versions) {
						Map<String, Object> config = new HashMap<>();
						config.put("input", version.get("input"));
						config.put("output", version.get("output"));
						version.put("config", config);
						version.remove("input");
						version.remove("output");
					}
					component.put("versions", versions);
					components.add(component);
				}
			}
			resource.put("components", components);
		}
		return blueprintTemplate;
	}
	
	/**
	 * 批量注册主机
	 * @param body
	 * [{
			"name": "hostName",
			"ip": "hostIp",
			"userName": "userName of Host",
			"userPassword": "userPassword of Host",
			"clusterId": "clusterId"
		}]
	 * @return
	 * [{
			"name": "hostName",
			"ip": "hostIp",
			"userName": "userName of Host",
			"userPassword": "加密后的密码",
			"clusterId": "clusterId",
			"id": "hostId"
		}]
	 */
	@RequestMapping(value = "nodes", method = RequestMethod.POST)
	public Object addNodes(@RequestBody List<Map<String, Object>> body) {
		for (Map<String, Object> node : body) {
			node.put("id", UUID.randomUUID().toString());
			String password = (String) node.get("userPassword");
			if (password != null) {
				node.put("userPassword", AESUtil.defaultEncrypt(password));
			}
		}
		service.multiInsertNodes(body);
		return body;
	}
	
	/**
	 * 触发流程
	 * @param blueprintTemplateId
	 * @param flowId
	 * @param body
	 * {
			"blueprintInstanceConfig": {
				"key": "value"
			},
			"resources": [{
				"resource": {
					"key": -1,
					"clusterId": "clusterId",
					"hosts": ["hostId"]
				},
				"components": [{
					"name": "componentName",
					"currentVersion": {
						"id": "versionId",
						"config": {
							"input": {
								"key": "value"
							},
							"output":{}
						}
					},
					"targetVersion": {
						"id": "versionId",
						"config": {
							"input": {
								"key": "value"
							},
							"output":{}
						}
					}
				}]
			}]
		}
	 * @return
	 * {
			"instanceId": "instanceId"
		}
	 */
	@RequestMapping(value = "flow/{blueprintTemplateId}/{flowId}", method = RequestMethod.POST)
	public Object triggerFlow(@PathVariable("blueprintTemplateId") String blueprintTemplateId, @PathVariable("flowId") String flowId, @RequestBody Map<String, Object> body) {
		List<Map<String, Object>> resources = (List<Map<String,Object>>) body.get("resources");
		Map<String, Object> layerMap = saveLayer(resources);
		Map<String, Object> blueprintInstance = saveBlueprintInstance(blueprintTemplateId, layerMap);
		String instanceId = (String) blueprintInstance.get("instanceId");
		int id = (Integer) blueprintInstance.get("id");
		saveConfig(body, instanceId, id);
		trigger(instanceId, flowId);
		Map<String, Object> result = new HashMap<>();
		result.put("instanceId", instanceId);
		result.put("id", id);
		return result;
	}
	
	private Map<String, Object> getLabelMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("key", "compresmap");
		map.put("value", "cop_" + UUID.randomUUID().toString());
		map.put("type", "1");
		return map;
	}
	
	private Map<String, Object> saveLayer(List<Map<String, Object>> resources) {
		List<Map<String, Object>> labels = new ArrayList<>();
		Map<String, Object> layerMap = new HashMap<>();
		for (Map<String,Object> resource : resources) {
			Map<String, Object> layer = (Map<String, Object>) resource.get("resource");
			Map<String, Object> label = getLabelMap();
			label.put("nodes", layer.get("hosts"));
			labels.add(label);
			label.put("clusterId", layer.get("clusterId"));
			layerMap.put("" + layer.get("key"), label);
		}
		layerMap.put("insertResult", service.insertLabels(labels));
		return layerMap;
	}
	
	private String template2Instance(String info, Map<String, Object> layerMap) {
		JSONObject infoMap = JSON.parseObject(info);
		List<Map<String, Object>> nodes = (List<Map<String,Object>>) infoMap.get("nodeDataArray");
		for (Map<String, Object> node : nodes) {
			if ("resource".equals(node.get("eleType"))) {
				Map<String, Object> labelMap = (Map<String, Object>) layerMap.get("" + node.get("key"));
				node.put("cluster_id", labelMap.get("clusterId"));
				Map<String, Object> label = new HashMap<>();
				label.put("key", labelMap.get("key"));
				label.put("value", labelMap.get("value"));
				node.put("label", label);
				List<Map<String, Object>> nodeIps = service.getNodes((List<String>) labelMap.get("nodes"));
				node.put("nodes", JSON.toJSONString(nodeIps));
				node.put("ins", nodeIps.size());
			}
		}
		infoMap.put("nodeDataArray", nodes);
		return JSON.toJSONString(infoMap);
	}
	
	private String getResourceInfo(String blueprintInfo) {
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
	
	private Map<String, Object> saveBlueprintInstance(String blueprintTemplateId, Map<String, Object> layerMap) {
		Map<String, Object> blueprintTemplate = service.getBlueprintTemplate(blueprintTemplateId);
		String info = template2Instance((String) blueprintTemplate.get("info"), layerMap);
		String bluePrintInsId = UUIDGenerator.getUUID();
		String name = "cop_" + bluePrintInsId;
		Map<String,String> param = new HashMap<>();
		param.put("blue_instance_id", bluePrintInsId);
		param.put("blue_instance_name", name);
		param.put("blue_instance_desc", "cop实例");
		param.put("blue_instance_info", info);
		param.put("blueprint_template_id", blueprintTemplateId);
		param.put("user_id", "1");
		param.put("resource_pool_config", this.getResourceInfo(info));
		try {
			blueprintService.saveBlueprintInstance(param);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
		return service.getBlueprintInstance(bluePrintInsId);
	}
	
	private void saveConfig(Map<String, Object> body, String instanceId, int id) {
		Map<String, Object> instanceConfig = (Map<String, Object>) body.get("blueprintInstanceConfig");
		Map<String, Object> blueprintInstance = new HashMap<>();
		blueprintInstance.put("instanceId", instanceId);
		blueprintInstance.put("config", JSON.toJSONString(instanceConfig));
		service.updateBlueprintInstance(blueprintInstance);
		List<Map<String, Object>> resources = (List<Map<String, Object>>) body.get("resources");
		for (Map<String, Object> resource : resources) {
			List<Map<String, Object>> components = (List<Map<String,Object>>) resource.get("components");
			for (Map<String, Object> component : components) {
				Map<String, Object> param = new HashMap<>();
				param.put("blueprintInstanceId", id);
				param.put("componentName", component.get("name"));
				Map<String, Object> currentVersion = (Map<String, Object>) component.get("currentVersion");
				Map<String, Object> currentConfig = (Map<String, Object>) currentVersion.get("config");
				param.put("currentVersion", currentVersion.get("id"));
				param.put("currentInput", JSON.toJSONString(currentConfig.get("input")));
				param.put("currentOutput", JSON.toJSONString(currentConfig.get("output")));
				Map<String, Object> targetVersion = (Map<String, Object>) component.get("targetVersion");
				Map<String, Object> targetConfig = (Map<String, Object>) targetVersion.get("config");
				param.put("targetVersion", targetVersion.get("id"));
				param.put("targetInput", JSON.toJSONString(targetConfig.get("input")));
				param.put("targetOutput", JSON.toJSONString(targetConfig.get("output")));
				service.updateComponentConfig(param);
				blueprintService.uploadComponentFile(instanceId, (Integer) service.getComponent(param).get("id"), (String) currentVersion.get("id"), (String) targetVersion.get("id"));
			}
		}
	}
	
	private void trigger(String instanceId, String flowId) {
		Map<String, String> params = new HashMap<>();
		params.put("_userName", "cop");
		Map<String, Object> bpInfo = blueprintDao.getBlueprintInstance(instanceId);
		Map<String,Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(flowId);
		String result = null;
		try {
			result = blueprintService.executeBlueprintFlow(flowId, instanceId, params);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = MessageHelper.wrap("result",false,"message","执行蓝图流程失败：" + e.getMessage());;
		}
		JSONObject resultJson = JSON.parseObject(result);
		int operateResult = resultJson.getBoolean("result") ? 1 : 0;
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity("cop", ResourceCode.BLUEPRINTFLOW, "" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.EXECUTE, operateResult, "cop调用蓝图实例[" + bpInfo.get("INSTANCE_NAME") + "]执行蓝图流程[" + flowInfo.get("FLOW_NAME") + "]"));
			}
		});
		log.info("触发流程返回结果为：{}", result);
	}

}
