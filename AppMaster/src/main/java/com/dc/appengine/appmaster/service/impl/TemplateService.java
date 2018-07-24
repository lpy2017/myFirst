package com.dc.appengine.appmaster.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.pattern.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.dao.IRollBackDao;
import com.dc.appengine.appmaster.dao.ITemplateDao;
import com.dc.appengine.appmaster.dao.impl.BluePrintTypeDao;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.dao.impl.RollBackDao;
import com.dc.appengine.appmaster.dao.impl.flowSaveDao;
import com.dc.appengine.appmaster.entity.AppSnapshot;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.ITemplateService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.ConfigHelper;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dc.appengine.appmaster.utils.Utils;

@Service("templateService")
public class TemplateService implements ITemplateService {
	@Autowired
	@Qualifier("templateDao")
	ITemplateDao templateDao;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("bluePrintTypeDao")
	private BluePrintTypeDao bluePrintTypeDao;

	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;

	@Autowired
	@Qualifier("instanceDao")
	private IInstanceDao instanceDao;

	@Autowired
	@Qualifier("instanceService")
	private IInstanceService instanceService;

	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;

	@Autowired
	@Qualifier("configservice")
	private ConfigsService configsServcie;

	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;
	
	@Autowired
	@Qualifier("blueprintService")
	private IBlueprintService blueprintService;
	
	@Autowired
	@Qualifier("rollBackDao")
	private IRollBackDao rollBackDao;
	
	@Autowired
	@Qualifier("flowsaveDao")
	private flowSaveDao flowsaveDao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao dao;
	
	String flowServerUrl = "http://10.1.108.33:8090/frame";
	
	private static final Logger log = LoggerFactory.getLogger(TemplateService.class);

	// 已部署模板列表
	public Page listDeployedTemplate(int pageSize, int pageNum, long userId,
			String key) {
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
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userIds", userIds.toString().split(","));
		m.put("blueInstanceName", key);
		Page page = new Page(pageSize, templateDao.countDeployedTemNum(m));
		page.setStartRowNum(pageSize * (pageNum - 1));
		page.setEndRowNum(pageSize * pageNum);
		page.setObjCondition(m);
		List<Map<String, Object>> blueInstanceList = templateDao
				.getDeployedTemplate(page);
		if (blueInstanceList != null && blueInstanceList.size() > 0) {
			for (Map<String, Object> ma : blueInstanceList) {
				// 集群
				int blueInstanceId = (int) ma.get("ID");
				/*String clusterName = templateDao
						.getClusterNameById(blueInstanceId);
				ma.put("clusterName", clusterName);
				
				// 组件个数
				int appNum = templateDao
						.getAppNumByBlueInstanceId(blueInstanceId);
				ma.put("appNum", appNum);*/
				// 更新时间
				String updateTime = templateDao.getUpdateTime(blueInstanceId);
				ma.put("updateTime", updateTime);
				// 状态
				String status = instanceService
						.getBlueInstanceStatus(blueInstanceId);
				ma.put("status", status);
				//为了兼容旧版本没有nodeName
				String info = "" + ma.get("INFO");
				Map<String, Object> json = JSON.parseObject(info,new TypeReference<Map<String, Object>>(){});
				List<Map<String, Object>>  nodeDataArray =  (List<Map<String, Object>>) json.get("nodeDataArray");
				for(Map<String, Object> nodeData : nodeDataArray){
					Object nodeName = nodeData.get("nodeName");
					if(nodeName == null || "".equals(("" + nodeName).trim())){
						String componentName = "";
						String text = "" + nodeData.get("text");
						//组件名不允许有"-"
						int mark = text.indexOf("-");
						if(mark != -1){
							componentName = text.substring(0, mark);
						}
						else{
							componentName = text;
						}
						nodeData.put("nodeName", componentName);
					}
				}
				json.put("nodeDataArray", nodeDataArray);
				ma.put("INFO", JSON.toJSONString(json));
				// 增加ip列表
				JSONObject resourcePoolInfo = JSON.parseObject((String) ma.get("RESOURCE_POOL_CONFIG"));
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
				ma.put("ips", ips);
			}
		}
		page.setRows(blueInstanceList);
		return page;
	}

	public String getBlueInstanceDetail(int blueInstanceId) {
		Map<String, Object> detail = templateDao
				.getBlueInstanceDetail(blueInstanceId);
		if (detail != null && detail.size() != 0) {
			// 集群
//			String clusterName = templateDao.getClusterNameById(blueInstanceId);
//			detail.put("clusterName", clusterName);
			// 更新时间
			String updateTime = templateDao.getUpdateTime(blueInstanceId);
			detail.put("updateTime", updateTime);
			// 组件个数
			int appNum = templateDao.getAppNumByBlueInstanceId(blueInstanceId);
			detail.put("appNum", appNum);
			// 状态
			String status = instanceService
					.getBlueInstanceStatus(blueInstanceId);
			detail.put("status", status);
		}
		return JSON.toJSONString(detail);
	}

	// 操作蓝图实例
	@Transactional
	@Override
	public String operateBlueInstance(String blueInstanceId, String type) {
		// TODO Auto-generated method stub
		// 通过blueId查询deploy的流程id（flowId）
		Map<String, Object> m = new HashMap<>();
		m.put("blueInstanceId", blueInstanceId);
		m.put("flowType", type);
		// 通过蓝图实例id查找对应的flowId
		int flowId = bluePrintTypeDao.getFlowIdByBlueId(m);
		// 用户id
		long userId = bluePrintTypeDao
				.getUserIdByBlueInstanceId(blueInstanceId);
		// 调用与工作流交互的接口
		if (type.equals("stop")) {
			// 部署
			// 调用方法 返回值 message
			String message = "";
			if (message.contains("true")) {
				// 部署成功

				// 创建应用以及实例
				Application app = new Application();
				app.setAppName("cx_app1");
				app.setBlueInstanceId(Long.valueOf(blueInstanceId));
				app.setAppType("应用组件");
				app.setClusterId("22911622-73cf-4f56-808d-1a032e9ddabd");
				app.setDeployId(flowId);
				app.setDescription("应用的创建一");
				app.setUserId(userId);
				long appId = applicationDao.save(app);
				// 创建实例
				Instance ins = new Instance();
				ins.setAppId(String.valueOf(appId));
				ins.setIp("10.0.0.1");
				ins.setStatus("deployed");
				UUIDGenerator uuid = new UUIDGenerator();
				String insId = uuid.getUUID();
				ins.setId(insId);
				instanceDao.saveInstance(ins);
				return MessageHelper.wrap("result", true, "massage", "部署成功");
			}
			return MessageHelper.wrap("result", false, "massage", "部署失败");
		} else if (type.equals("start")) {
			// 启动
			String message = "";
			if (message.contains("true")) {
				// 启动成功

			}
			return MessageHelper.wrap("result", false, "massage", "启动失败");
		} else if (type.equals("destroy")) {
			// 卸载
			String message = "";
			if (message.contains("true")) {
				// 卸载成功

				// 删除实例

				String instanceId = "";// 从message中获取
				long appId = 4;
				int insTemp = instanceDao.delInstance(instanceId);
				// 删除应用
				int appTemp = applicationDao.delApp(appId);
				return MessageHelper.wrap("result", true, "massage", "卸载成功");
			}
			return MessageHelper.wrap("result", false, "massage", "卸载失败");
		} else {
			return MessageHelper.wrap("result", false, "massage", "类型不正确");
		}

	}

	public String test() {
		String blueInstanceId = "11";
		int flowId = 1;
		long userId = 1;
		// 创建实例
		long appId = 7;
		Instance ins = new Instance();
		ins.setAppId(String.valueOf(appId));
		ins.setNodeId("122243252df");
		ins.setStatus("deployed");
		UUIDGenerator uuid = new UUIDGenerator();
		String insId = uuid.getUUID();
		ins.setId(insId);
		instanceDao.saveInstance(ins);

		String instanceId = "";// 从message中获取
		int insTemp = instanceDao.delInstance(insId);
		// 删除应用
		int appTemp = applicationDao.delApp(appId);
		return "";
	}

	public String saveSnapshot(int blueInstanceId, long userId) {
		// 创建蓝图实例快照
		String snapId = UUID.randomUUID().toString();
		List<Map<String, Object>> appList = instanceDao
				.getAppList(blueInstanceId);
		String snapInfo = "";
		if (appList != null && appList.size() > 0) {
			for (Map<String, Object> app : appList) {
				// 遍历app列表 分别加入instanceList
				int appId = (int) app.get("id");
				List<Map<String, Object>> instanceList = instanceDao
						.getInstanceList(appId);
				app.put("instances", instanceList);
				// 资源版本信息
				String resourceVersionId = (String) app
						.get("resourceVersionId");
				Version resourceVersion = resourceDao
						.getResourceVersion(resourceVersionId);
				Map<String, Object> resource = new HashMap<>();
				resource.put("resourceId", resourceVersion.getResourceId());
				resource.put("destroyTimeOut",
						resourceVersion.getDestroyTimeout());
				resource.put("id", resourceVersion.getId());
				resource.put("registryId", resourceVersion.getRegistryId());
				resource.put("resourceName", resourceVersion.getResourceName());
				resource.put("resourcePath", resourceVersion.getResourcePath());
				resource.put("startTimeOut", resourceVersion.getStartTimeout());
				resource.put("stopTimeOut", resourceVersion.getStopTimeout());
				resource.put("updateTime", resourceVersion.getUpdateTime());
				resource.put("versionDesc", resourceVersion.getVersionDesc());
				resource.put("versionName", resourceVersion.getVersionName());
				resource.put("deployTimeOut",
						resourceVersion.getDeployTimeout());
				app.put("resourceInfo", resource);
				// 获取配置信息
				String configId = (String) app.get("configId");
				List<Map<String, Object>> s = configsServcie
						.getConfigList(Integer.valueOf(configId));
				app.put("configList", s);
			}
			snapInfo = JSON.toJSONString(appList,
					SerializerFeature.WriteDateUseDateFormat);
		} else {
			snapInfo = " ";
		}
		Map<String, Object> param = new HashMap<>();
		param.put("id", snapId);
		param.put("blueInstanceId", blueInstanceId);
		param.put("snapInfo", snapInfo);
		param.put("userId", userId);
		templateDao.saveSnapShot(param);
		return MessageHelper.wrap("result", true, "massage", "创建快照成功");
	}

	// 快照列表ffffff(blueInstanceId 这里为appId )
	public String listSnapshots(int pageSize, int pageNum, long userId,
			int appId) {
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
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userIds", userIds.toString().split(","));
		m.put("appId", appId);
		Page page = new Page(pageSize, templateDao.countSnapshotNum(m));
		page.setStartRowNum(pageSize * (pageNum - 1));
		page.setEndRowNum(pageSize * pageNum);
		page.setObjCondition(m);
		page.setRows(templateDao.listSnapshots(page));
		return JSON
				.toJSONString(page, SerializerFeature.WriteDateUseDateFormat);
	}

	// 删除快照
	public String deleteSnapshot(String snapId) {
		templateDao.deleteSnap(snapId);
		return MessageHelper.wrap("result", true, "massage", "删除快照成功");
	}

	// 恢复快照
	public String recoverySnapshot(String snapId) {
		// 获取到快照信息，进行解析
		Map<String, Object> snapshot = templateDao.getSnapshot(snapId);
		//String blueInstanceId = snapshot.get("").toString();
		Map<String, Object> app = JSON.parseObject(snapshot.get("snapInfo").toString(),
				new TypeReference<Map<String, Object>>() {
				});
		List<Map<String, Object>> insMessageList = getMessage(snapId);
		String blueInstanceId = app.get("blueInstanceId").toString();
		int appId = (int) snapshot.get("blueInstanceId");
		Application appNow = applicationDao.getAppInfo(appId + "");
		String blueId = templateDao.getBlueInstanceDetail(Integer.valueOf(blueInstanceId)).get("blueId").toString();
		long key = appNow.getKey();
		String key_mes = blueId + "_" + key;
		List<Map<String,Object>> stopList = new ArrayList<>();
		List<Map<String,Object>> destroyList = new ArrayList<>();
		List<Map<String,Object>> deployList = new ArrayList<>();
		List<Map<String,Object>> startList = new ArrayList<>();
		for (Map<String, Object> m : insMessageList) {
			String op = m.get("op").toString();
			if(op.equals("stop")){
				//调用停止
				stopList.add(m);
			}else if(op.equals("destroy")){
				//卸载
				destroyList.add(m);
			}else if(op.equals("deploy")){
				//部署
				deployList.add(m);
			}else if(op.equals("start")){
				//启动
				startList.add(m);
			}
		}
		Map<String, List<? extends Object>> message = new HashMap<String, List<? extends Object>>();
		message.put(key_mes, stopList);
		message.put(key_mes, destroyList);
		message.put(key_mes, deployList);
		message.put(key_mes, startList);
		this.putMessage(message);
		
		List<Map<String, Object>> nowInstances = instanceDao
				.getInstanceList(appId);
		String resId = nowInstances.get(0).get("resVersionId").toString();
		Map<String, Object> resourceDetail = resourceService.getResourceVersionDetail(resId);
		Map<String,Object> flow = JSON.parseObject(resourceDetail.get("flows").toString(),
				new TypeReference<Map<String,Object>>() {
				});;
				
		long deployFlowId = (long) flow.get("deploy");
		long destroyFlowId = (long) flow.get("destroy");
		long startFlowId = (long) flow.get("start");
		long stopFlowId = (long) flow.get("stop");
		
		//调用停止流程
		RestTemplate rest = new RestTemplate();
		String stopUrl = flowServerUrl+"/WFService/startPdByPidAndUsers.wf?pdId="+stopFlowId+"&initiator=admin";
		String stopResult = rest.getForObject(stopUrl, String.class);
		
		//卸载
		String destroyUrl = flowServerUrl+"/WFService/startPdByPidAndUsers.wf?pdId="+destroyFlowId+"&initiator=admin";
		String destroyResult = rest.getForObject(destroyUrl, String.class);
				//部署
		String deployUrl = flowServerUrl+"/WFService/startPdByPidAndUsers.wf?pdId="+deployFlowId+"&initiator=admin";
		String deployResult = rest.getForObject(deployUrl, String.class);
		//启动
		String startUrl = flowServerUrl+"/WFService/startPdByPidAndUsers.wf?pdId="+startFlowId+"&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		
		System.out.println(startResult);
		System.out.println(stopResult);
		System.out.println(destroyResult);
		System.out.println(deployResult);
		System.out.println(JSON.toJSONString(message));
		
		return MessageHelper.wrap("result", false, "massage",
				"instances is null");
	}

	private void putMessage(Map<String, List<? extends Object>> message){
		RestTemplate rest = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
		mvm.add("message", JSON.toJSONString(message));
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(mvm,requestHeaders);
		String messageResult = rest.postForObject(flowServerUrl+"/cd/message.wf", httpEntity, String.class);
	}
	
	public List<Map<String, Object>> getMessage(String snapId) {
		// 获取到快照信息，进行解析
		Map<String, Object> snapshot = templateDao.getSnapshot(snapId);
		String snapInfo = "";
		if (snapshot != null && snapshot.size() > 0) {
			snapInfo = snapshot.get("snapInfo").toString();
		} else {
			return null;
		}
		Map<String, Object> app = JSON.parseObject(snapInfo,
				new TypeReference<Map<String, Object>>() {
				});
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ip = addr.getHostAddress().toString();// 获得本机IP
		// 实例的masterurl信息
		String masterUrl = "http://" + ip + ":"
				+ ConfigHelper.getValue("server.port") + "/";
		// app为应用的相关信息
		// 配置id
		String oldConfigId = app.get("configId").toString();
		// 快照里的实例列表
		List<Map<String, Object>> oldInstances = (List<Map<String, Object>>) app
				.get("instances");
		// 当前运行态的应用相关信息以及实例列表
		int appId = (int) app.get("appId");
		Application appNow = applicationDao.getAppInfo(appId + "");
		List<Map<String, Object>> nowInstances = instanceDao
				.getInstanceList(appId);
		//test
		for(Map<String, Object> oldInstance : oldInstances){
			String oldResId = oldInstance.get("resVersionId").toString();
			int count = 0;
			for(Map<String, Object> nowInstance : nowInstances){
				String nowResId = nowInstance.get("resVersionId").toString();
				if(oldResId.equals(nowResId)){
					count ++ ;
				}
			}
			if(count > 0){
				
			}
			
		}
		
		// 将目前的实例全部卸载删除
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> nowInstance : nowInstances) {
			String instanceId = nowInstance.get("id").toString();
			String nowInstanceStatus = nowInstance.get("status").toString();
			String nowResId = nowInstance.get("resVersionId").toString();
			Map<String, Object> tempMap = instanceService
					.findMessage(instanceId);
			Version resourceVersion = resourceDao.getResourceVersion(nowResId);
			String nodeIp = templateDao.getNodeIpByNodeId(nowInstance.get(
					"nodeId").toString());
			String op = "";
			if (nowInstanceStatus.equals("DEPLOYED")) {
				op = "destroy";
			} else if (nowInstanceStatus.equals("RUNNING")) {
				op = "stop";
			}
			Map<String, Object> resourceVersionDetail = resourceService.getResourceVersionDetail(nowResId);
			
			Map<String, Object> mapIns1 = new HashMap<>();
			mapIns1.put("instanceId", instanceId);
			mapIns1.put("nodeIp", tempMap.get("ip").toString());
			mapIns1.put("resouceUrl", resourceVersionDetail.get("url"));
			mapIns1.put("deployPath", tempMap.get("deploy_path").toString());
			mapIns1.put("componentName", tempMap.get("app_name").toString());
			mapIns1.put("componentInput", "");
			mapIns1.put("pluginInput", "");
			mapIns1.put("configUrl", masterUrl);
			mapIns1.put("configVersionId", tempMap.get("versionId").toString());
			mapIns1.put("configTemplate",
					resourceVersionDetail.get("templates"));
			if (op.equals("destroy")) {
				mapIns1.put("op", op);
				list.add(mapIns1);
			} else if (op.equals("stop")) {
				mapIns1.put("op", op);
				list.add(mapIns1);
				Map<String,Object> mapIns2 = new HashMap<>();
				mapIns2.putAll(mapIns1);
				mapIns2.put("op", "destroy");
				list.add(mapIns2);
			}

		}
		// 部署快照中的所有实例
		for (Map<String, Object> oldInstance : oldInstances) {
			String instanceId = oldInstance.get("id").toString();
			String instanceStatus = oldInstance.get("status").toString();
			String oldResId = oldInstance.get("resVersionId").toString();
			Version resourceVersion = resourceDao.getResourceVersion(oldResId);
			String nodeIp = templateDao.getNodeIpByNodeId(oldInstance.get(
					"nodeId").toString());
			//创建实例,重新生成instanceid
			Instance instance  = new Instance() ;
			instance.setId(UUID.randomUUID().toString());
			instance.setAppId(String.valueOf(appId));
			instance.setNodeId(oldInstance.get(
					"nodeId").toString());
			instance.setAppVersionId(oldResId);
			instanceService.saveInstance(instance);
			
			String op = "";
			if (instanceStatus.equals("DEPLOYED")) {
				op = "deploy";
			} else if (instanceStatus.equals("RUNNING")) {
				op = "start";
			}
			Map<String, Object> tempMap = instanceService
					.findMessage(instanceId);
			Map<String, Object> resourceVersionDetail = resourceService
					.getResourceVersionDetail(oldResId);
			Map<String, Object> mapIns1 = new HashMap<>();
			mapIns1.put("instanceId", instanceId);
			mapIns1.put("nodeIp", nodeIp);
			mapIns1.put("resouceUrl", resourceVersionDetail.get("url"));
			mapIns1.put("deployPath", oldInstance.get("deployPath").toString());
			mapIns1.put("componentName", app.get("appName").toString());
			mapIns1.put("componentInput", "");
			mapIns1.put("pluginInput", "");
			mapIns1.put("configUrl", masterUrl);
			mapIns1.put("configVersionId", tempMap.get("versionId")
					.toString());
			mapIns1.put("configTemplate",
					resourceVersionDetail.get("templates"));
			if (op.equals("deploy")) {
				mapIns1.put("op", op);
				list.add(mapIns1);
			} else if (op.equals("start")) {
				mapIns1.put(op, op);
				list.add(mapIns1);
				mapIns1.put(op, "deploy");
				list.add(mapIns1);
			}
		}
		/*
		 * //快照中所有的实例版本及其个数 Map<String,Object> oldResNum =
		 * getResMap(oldInstances); Map<String,Object> nowResNum =
		 * getResMap(nowInstances); if(oldResNum != null){ Iterator i =
		 * oldResNum.entrySet().iterator(); while (i.hasNext()) { Object obj =
		 * i.next(); String key = obj.toString(); int oldnum = (int)
		 * oldResNum.get(key); int nownum = (int) nowResNum.get(key); if(nownum
		 * <= oldnum){
		 * 
		 * } }
		 * 
		 * }
		 */
		return list;
	}

	public Map<String, Object> getResMap(List<Map<String, Object>> oldInstances) {
		// 资源id列表
		List<String> oldResouceIds = new ArrayList<>();
		List<String> resouceIdsList = new ArrayList();
		Map<String, Object> mapNum = new HashMap<>();// {resVersionId, num}
		for (Map<String, Object> oldInstance : oldInstances) {
			String resId = oldInstance.get("resVersionId").toString();
			oldResouceIds.add(resId);
		}
		// 对资源id列表去重//
		Set set = new HashSet();
		for (String cd : oldResouceIds) {
			if (set.add(cd)) {
				resouceIdsList.add(cd);
			}
		}
		// 去重后的
		for (String a : oldResouceIds) // 统计元素重复次数
		{
			String count = mapNum.get(a).toString();
			if (count == null) {
				mapNum.put(a, "1");
			} else {
				mapNum.put(a, (Integer.parseInt(count) + 1) + "");
			}
		}
		return mapNum;

	}

	// 生成快照（应用）
	public String saveSnapshotOfApp(int appId, long userId,String snapshotName) {
		int temp = templateDao.findSnapshotOfAppByName(snapshotName,appId);
		if(temp > 0){
			return MessageHelper.wrap("result", false,"message","该名称已存在，创建失败");
		}
		// 创建蓝图实例快照
		String snapId = UUID.randomUUID().toString();
		Application application = applicationDao.findByAppId(appId + "");
		// 分别加入instanceList
		List<Map<String, Object>> instanceList = instanceDao
				.getInstanceList(appId);
		//处理instance中json为map，防止转换时把json当做字符串处理
		for(Map<String, Object> instance : instanceList){
			jsonString2Map(instance);
		}
		Map<String, Object> param = new HashMap<>();
		param.put("id", snapId);
		param.put("appId", appId);
		param.put("snapshotName", snapshotName);
		param.put("appName", application.getAppName());
		param.put("snapInfo", JSON.toJSONString(instanceList,
				SerializerFeature.WriteDateUseDateFormat));
		param.put("userId", userId);
		param.put("blueInstanceId", application.getBlueInstanceId());
		param.put("key", application.getKey());
		templateDao.saveSnapShot(param);
		return MessageHelper.wrap("result", true, "snapId", snapId,"message","创建成功");
	}

	@Override
	public String saveSnapshotOfBlueInstance(int blueInstanceId, long userId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> appList = applicationDao.getAppByBlueInstanceId(blueInstanceId, userId);
		List<String> appSnapId = new ArrayList<>();
		if(appList != null && appList.size() > 0){
			for(Map<String,Object> m : appList){
				int appId = (int) m.get("appId");
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				String snapshot = "蓝图实例" + blueInstanceId + "_组件" + appId + "_快照" + "_" + format.format(date);
				String snapshotAppId = JSON.parseObject(saveSnapshotOfApp(appId, userId,snapshot),
						new TypeReference<Map<String, Object>>() {
						}).get("snapId").toString();
				appSnapId.add(snapshotAppId);
			}
		}
		String snapId = UUID.randomUUID().toString();
		Map<String, Object> param = new HashMap<>();
		param.put("id", snapId);
		param.put("blueInstanceId", blueInstanceId);
		param.put("snapInfo", JSON.toJSONString(appSnapId));
		param.put("userId", userId);
		templateDao.saveSnapShotOfBlueInstance(param);
		return MessageHelper.wrap("result", true, "snapId", snapId,"message","创建成功");
	}

	@Override
	public List<Map<String, Object>> getSnapshotInsInfo(String snapId,
			long appId) {
		AppSnapshot appSnapshot = rollBackDao.getSnapshotOfApp(snapId, appId);
		String snapshotInfo = appSnapshot.getSnapshotInfo();
		// 组件在快照保存时实例的input/output是json，故整体保存存在json嵌套，而JSON.toJSONString(List<Map<String,
		// Object>>())格式转换时对于嵌套的json当做字符串处理
		snapshotInfo = snapshotInfo.replaceAll("\"\\{", "{").replaceAll("}\"", "}");
		// 快照保存时JSON.toJSONString(List<Map<String,Object>>())把实例的input/output里面的json字符串的引号做了转义
		snapshotInfo = snapshotInfo.replaceAll("\\\\\"","\"");
		// 快照实例
		List<Map<String, Object>> snapshotInstanceList = JSON.parseObject(snapshotInfo,
				new TypeReference<List<Map<String, Object>>>() {
				});
		return snapshotInstanceList;
	}

	@Override
	public List<Map<String, Object>> getSnapshotRollResultInfo(String snapId,
			long appId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> snapshotInstanceList = getSnapshotInsInfo(snapId,appId);
		if(snapshotInstanceList != null && snapshotInstanceList.size() > 0){
			for(Map<String,Object> ins : snapshotInstanceList){
				String snapInsId = ins.get("id").toString();
				String snapInsStatus = ins.get("status").toString();
				String snapInsNodeId = ins.get("nodeId").toString();
				String snapInsResVersionId = ins.get("resVersionId").toString();
				String snapInsInput = ins.get("input").toString();
				String snapInsOutput = ins.get("output").toString();
				
				//通过insId查询库，是否存在实例
				Map<String,Object> instance = instanceDao.getInstanceBuInsId(snapInsId);
				if(instance != null){
					boolean inputFlag = Utils.compareMap(snapInsInput,instance.get("input").toString());
					boolean outPutFlag = Utils.compareMap(snapInsOutput,instance.get("output").toString());
					if(instance.get("nodeId").toString().equals(snapInsNodeId) && instance.get("status").toString().equals(snapInsStatus) &&
					instance.get("resVersionId").toString().equals(snapInsResVersionId) &&
					inputFlag && outPutFlag){
						ins.put("result","SUCCESS!");
					}else{
						ins.put("result","ROLLBACK RUNNING");
					}
				}
				
			}
			return snapshotInstanceList;
		}
		return null;
	}

	@Override
	public String jsonToCorrectJson(String json,long appId) {
		// TODO Auto-generated method stub
		Map<String,Object> jsonMap = JSON.parseObject(json,new TypeReference<Map<String, Object>>(){});
		jsonMap.put("issub", false);
		jsonMap.put("type","upgradeOrRollback");
		jsonMap.put("_instanceDelete", "false");
		String blueprintId = applicationDao.findByAppId(appId+"").getBlueInstanceId()+"";
		jsonMap.put("bluePrintId", blueprintId);
		return JSON.toJSONString(jsonMap);
	}

	@Override
	public void saveUpdateInfo(Map<String, Object> m) {
		// TODO Auto-generated method stub
		flowsaveDao.saveUpdateInfo(m);
	}

	//如果加入了RC，以实例为粒度作快照并追加快照信息的机制会混乱，以组件为粒度的快照没影响；
	@Override
	public String saveSnapshotOfInstance(String instanceId) {
		String snapId = null;
		//根据实例id获取实例信息
		Map<String, Object> instanceInfo = instanceDao.getInstanceDetailById(instanceId);
		if(instanceInfo == null){
			return MessageHelper.wrap("result", false, "snapId", null, "massage", "实例:"+instanceId+"信息不存在");
		}
		//处理instanceInfo中json为map，防止转换时把json当做字符串处理
		jsonString2Map(instanceInfo);
		String appId = "" + instanceInfo.get("appId");
		//根据组件id获取组件信息
		Application application = applicationDao.findByAppId(appId);
		//根据组件id获取已有快照信息
		List<AppSnapshot> appSnapshots = rollBackDao.getSnapshotByAppId(Integer.parseInt(appId));
		AppSnapshot snap = null;
		//遍历已有快照，如果某个快照信息中不存在此实例，则合并此快照；如果所有快照都存在此实例，则作新快照
		for(AppSnapshot snapshot : appSnapshots){
			boolean instanceExist = false;
			String snapshotInfo = snapshot.getSnapshotInfo();
			List<Map<String, Object>> snapshotInstanceList = JSON.parseObject(snapshotInfo,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for(Map<String, Object> snapshotInstance : snapshotInstanceList){
				if(snapshotInstance.get("id").equals(instanceId)){
					instanceExist = true;
					break;
				}
			}
			if(!instanceExist){
				//此处默认能够追加的快照只有一个，不存在多个场景(除非此接口被任意调用或RC改变实例数)
				snap = snapshot;
				break;
			}
		}
		instanceInfo.remove("appId");//为了和组件粒度的快照信息统一
		//新增快照
		if(snap == null){
			List<Map<String, Object>> instanceInfos = new ArrayList<Map<String, Object>>();
			instanceInfos.add(instanceInfo);
			snapId = UUID.randomUUID().toString();
			Map<String, Object> param = new HashMap<>();
			param.put("id", snapId);
			param.put("appId", appId);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			param.put("snapshotName", application.getAppName() + "_" + "snapshot" + "_" + format.format(date));
			param.put("appName", application.getAppName());
			//以数组方式存入数据库
			param.put("snapInfo", JSON.toJSONString(instanceInfos, SerializerFeature.WriteDateUseDateFormat));
			param.put("userId", "1");//默认admin
			param.put("blueInstanceId", application.getBlueInstanceId());
			param.put("key", application.getKey());
			templateDao.saveSnapShot(param);
		}
		//在已有快照追加实例信息
		else{
			snapId = snap.getSnapshotId();
			String snapInfo = snap.getSnapshotInfo();
			List<Map<String, Object>> snapInstances = JSON.parseObject(snapInfo,
					new TypeReference<List<Map<String, Object>>>() {
					});
			if(snapInstances == null){
				snapInstances = new ArrayList<Map<String, Object>>();
			}
			//把当前实例信息追加到已有快照信息后面
			snapInstances.add(instanceInfo);
			rollBackDao.updateSnapshotInfoById(JSON.toJSONString(snapInstances, SerializerFeature.WriteDateUseDateFormat), snapId);
		}
		return MessageHelper.wrap("result", true, "snapId", snapId);
	}

	//处理instanceInfo中json为map，防止转换时把json当做字符串处理
	private void jsonString2Map(Map<String, Object> instanceInfo) {
		Map<String, String> inputMap = json2Map(instanceInfo.get("input"));
		instanceInfo.put("input", inputMap);
		Map<String, String> outputMap = json2Map(instanceInfo.get("output"));
		instanceInfo.put("output", outputMap);
		Map<String, String> inputTempMap = json2Map(instanceInfo.get("inputTemp"));
		instanceInfo.put("inputTemp", inputTempMap);
		Map<String, String> outputTempMap = json2Map(instanceInfo.get("outputTemp"));
		instanceInfo.put("outputTemp", outputTempMap);
	}
	
	private Map<String, String> json2Map(Object jsonString){
		if(jsonString == null || "".equals(jsonString)){
			return new HashMap<String, String>();
		}
		return JSON.parseObject("" + jsonString, new TypeReference<Map<String, String>>(){});
	}
	
	public List getFlowIdByBlueInstanceId(String blueprintInstanceId){
		return bluePrintTypeDao.getFlowIdByBlueInstanceId(blueprintInstanceId);
	}
}
