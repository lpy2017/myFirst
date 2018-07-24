package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IRollBackDao;
import com.dc.appengine.appmaster.dao.impl.ComponentUpdateDAO;
import com.dc.appengine.appmaster.dao.impl.RollBackDao;
import com.dc.appengine.appmaster.entity.AppSnapshot;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IMessageService;
import com.dc.appengine.appmaster.service.impl.RollBackService.RollBackKeyValue;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Service("componentRollBackService")
public class ComponentRollBackService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("instanceService")
	private IInstanceService instanceService;
	
	@Autowired
	@Qualifier("messageService")
	IMessageService messageService;
	
	@Autowired
	@Qualifier("componentUpdateDAO")
	ComponentUpdateDAO componentUpdateDAO;
	
	@Autowired
	@Qualifier("rollBackDao")
	IRollBackDao rollBackDao;
	
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	
	//运行状态
	private static final String RUNNING = "RUNNING";
	private static final String DEPLOYED = "DEPLOYED";
	private static final String UNDEPLOYED = "UNDEPLOYED";
	
	//当前操作
	private static final String DEPLOY = "deploy";
	private static final String START = "start";
	private static final String STOP = "stop";
	private static final String DESTROY = "destroy";
	
	//实例类型
	private static final String instanceType_snapShot = "snapShot";
	private static final String instanceType_current = "current";
	
	private static final HttpComponentsClientHttpRequestFactory httpRequestFactory;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(3000);
		httpRequestFactory.setConnectTimeout(3000);
		httpRequestFactory.setReadTimeout(3000);
	}
	
	public String rollBackFromSnapshot(String snapId, long appId, String type,String flowId){
//		List<Map<String,Object>> instanceList= new ArrayList<>();
		// 当前实例
		List<Map<String, Object>> currInstanceList = instanceService.getInstanceList(appId);
		// 过滤调状态为undeployed的实例
		filterFailedInstance(currInstanceList);
		// List<Instance> currInstances = loadInstances(appId,
		// currInstanceList);
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
		// 过滤调状态为undeployed的实例
		filterFailedInstance(snapshotInstanceList);

		//实例id相同的实例，且需要转换的实例对
		List<RollBackKeyValue> sameInstanceConverts = new ArrayList<RollBackKeyValue>();
		for (Map<String, Object> ssInstance : snapshotInstanceList) {
			for (Map<String, Object> crInstance : currInstanceList) {
				if (compareSameInstance(crInstance, ssInstance)) {
					sameInstanceConverts.add(new RollBackKeyValue(crInstance, ssInstance));
				}
			}
		}
		// 过滤实例id相同的实例
		filterList(sameInstanceConverts, snapshotInstanceList, currInstanceList);
		
		// 需要操作流程进行回滚的实例对list
		List<RollBackKeyValue> flowConverts = new ArrayList<RollBackKeyValue>();
	
		// 当前实例list已匹配完，快照实例还存在，则快照实例自己处理
		if (snapshotInstanceList.size() > 0) {
			for (Map<String, Object> ssInstance : snapshotInstanceList) {
				flowConverts.add(new RollBackKeyValue(null, ssInstance));
			}
		}
		// 快照实例list已匹配完，当前实例还存在，则当前实例自己处理
		if (currInstanceList.size() > 0) {
			for (Map<String, Object> currInstance : currInstanceList) {
				flowConverts.add(new RollBackKeyValue(currInstance, null));
			}
		}
		String json = componentUpdateDAO.findJsonByFlowId(flowId);
		Map<String, String> flowIdForOp = this.getSubFlowId(json);
		long key = componentUpdateDAO.findKeyByAppId(appId);
		Map<String, List<Object>> messageAll= new HashMap<>();
//		String suffixKey="_"+key;
		// 滚动升级
		if ("rollbackFlow".equals(type)) {
			// 状态可切换的情况
			for (RollBackKeyValue keyValue : sameInstanceConverts) {
				Map<String, Object> addParams= new HashMap<>();
				Map<String, Object> current = keyValue.getCurrent();
				Map<String, Object> target = keyValue.getTarget();
				String crstatus= current.get("status").toString();
				String sstatus= target.get("status").toString();
				addParams.put("isNew", false);
				addParams.put("isNeedDeploy", true);//默认是需要重新部署的，流程执行中，该字段可能无用
				addParams.put("sourceStatus", crstatus);
				addParams.put("desStatus", sstatus);
				addParams.put("op", "keep");
				if (!current.get("resVersionId").equals(target.get("resVersionId"))) {
					addParams.put("isVersionEqual", false);
				} else {
					addParams.put("isVersionEqual", true);
				}
				current.put("instanceType", instanceType_current);
				current.putAll(addParams);
				target.put("instanceType", instanceType_snapShot);
				target.putAll(addParams);
				//首先判断当前实例的状态，然后判断是否是同一版本，再判断是否需要部署，最后判断目标状态
				if(RUNNING.equals(crstatus)){//先停止，然后判断版本是否一致
					generationMessage(messageAll, flowIdForOp, null, current, STOP);
					if(!(Boolean)addParams.get("isVersionEqual")){
						generationMessage(messageAll, flowIdForOp, null, current, DESTROY);
						if((Boolean)addParams.get("isNeedDeploy")){
							generationMessage(messageAll, flowIdForOp, null, target, DEPLOY);
						}
					}
				}else if(DEPLOYED.equals(crstatus)){//判断版本是否一致
					if(!(Boolean)addParams.get("isVersionEqual")){
						generationMessage(messageAll, flowIdForOp, null, current, DESTROY);
						if((Boolean)addParams.get("isNeedDeploy")){
							generationMessage(messageAll, flowIdForOp, null, target, DEPLOY);
						}
					}
				}
				//判断目标状态是否是running
				if(RUNNING.equals(sstatus)){
					generationMessage(messageAll, flowIdForOp, null, target, START);
				}
//				instanceList.add(target);
			}
			// 处理操作流程的情况
			for (RollBackKeyValue keyValue : flowConverts) {
				Map<String, Object> current = keyValue.getCurrent();
				Map<String, Object> target = keyValue.getTarget();
				//减少的实例
				if (current != null) {
					String crstatus= current.get("status").toString();
					Map<String, Object> addParams= new HashMap<>();
					addParams.put("isNew", false);
					addParams.put("isNeedDeploy", false);
					addParams.put("sourceStatus", "" + current.get("status"));
					addParams.put("isVersionEqual", false);
					addParams.put("op", "delete");
					addParams.put("instanceType", instanceType_current);
//					current.remove("status");
//					instanceList.add(current);
					//首先判断当前实例的状态，然后判断是否是同一版本，再判断是否需要部署，最后判断目标状态
					if(RUNNING.equals(crstatus)){//先停止，然后判断版本是否一致
						generationMessage(messageAll, flowIdForOp, null, current, STOP);
						generationMessage(messageAll, flowIdForOp, null, current, DESTROY);
					}else if(DEPLOYED.equals(crstatus)){//判断版本是否一致
						generationMessage(messageAll, flowIdForOp, null, current, DESTROY);
					}
				}
				//新增实例
				if (target != null) {
					String sstatus= target.get("status").toString();
					Map<String, Object> addParams= new HashMap<>();
					addParams.put("isNew", true);
					addParams.put("desStatus", "" + target.get("status"));
					addParams.put("op", "add");
					addParams.put("instanceType", instanceType_snapShot);
					generationMessage(messageAll, flowIdForOp, null, target, DEPLOY);
					if(RUNNING.equals(sstatus)){
						generationMessage(messageAll, flowIdForOp, null, target, START);
					}
				}
			}
		}
		//更新实例版本、input和output
		String input=JSON.toJSONString(sameInstanceConverts.get(0).getTarget().get("input"));
		String output=JSON.toJSONString(sameInstanceConverts.get(0).getTarget().get("output"));
		String resourceVersionId=sameInstanceConverts.get(0).getTarget().get("resVersionId").toString();
		Map<String, Object> param = new HashMap<>();
		param.put("appId", appId);
		param.put("resourceVersionId", resourceVersionId);
		param.put("input", input);
		param.put("output", output);
		componentUpdateDAO.updateInputAndOutput(param);
		String result = invokeWorkFlow(flowId, messageAll);
		return result;	
	}
	
	public String invokeWorkFlow(String flowId,Map<String, List<Object>> message){
		if(message.isEmpty()){
			return MessageHelper.wrap("state",false,"message","回滚前后版本一致，不需要回滚！");
		}
		//存放消息
		this.putMessage(message);
		//启动
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		return startResult;
	}
	
	
	public void generationMessage(Map<String, List<Object>> messageAll,Map<String, String>flowIdForOp,String indentifysuffix,
			Map<String, Object> instance,String currentOp) {
		List<Map<String, Object>> instances= new ArrayList<Map<String, Object>>();
		instances.add(instance);
		Map<String, List<Object>> message = new HashMap<>();
		String instanceType = instance.get("instanceType").toString();
		List<Object> list =null;
		if(instanceType_snapShot.equals(instanceType)){
			list=messageService.messagesFromSnapShot(instances, currentOp);
		}else if(instanceType_current.equals(instanceType)){
			list=messageService.messagesForCurrentInstances(instances, currentOp);
		}
		if(list !=null && !list.isEmpty()){
			message.put(flowIdForOp.get(currentOp), list);
		}
		messageAll.putAll(message);
	}

	
	
	
	class RollBackKeyValue {
		private Map<String, Object> current;
		private Map<String, Object> target;

		public RollBackKeyValue(Map<String, Object> current, Map<String, Object> target) {
			this.current = current;
			this.target = target;
		}

		public Map<String, Object> getCurrent() {
			return current;
		}

		public void setCurrent(Map<String, Object> current) {
			this.current = current;
		}

		public Map<String, Object> getTarget() {
			return target;
		}

		public void setTarget(Map<String, Object> target) {
			this.target = target;
		}

	}
	
	private boolean compareSameInstance(Map<String, Object> crInstance, Map<String, Object> ssInstance) {
		if (crInstance.get("id").equals(ssInstance.get("id"))) {
			return true;
		}
		return false;
	}
	private void filterFailedInstance(List<Map<String, Object>> list) {
		List<Map<String, Object>> temps = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> instance : list) {
			String status = "" + instance.get("status");
			if ("UNDEPLOYED".equalsIgnoreCase("status")) {
				temps.add(instance);
			}
		}
		if (temps.size() > 0) {
			for (Map<String, Object> temp : temps) {
				list.remove(temp);
			}
		}
	}
	
	public void filterList(List<RollBackKeyValue> converts,List<Map<String,Object>>snapshotInstanceList,
			List<Map<String,Object>> currInstanceList){
		if (!converts.isEmpty()) {
			for (RollBackKeyValue keyValue : converts) {
				Map<String, Object> cr = keyValue.getCurrent();
				Map<String, Object> ss = keyValue.getTarget();
				if (snapshotInstanceList.contains(ss)) {
					snapshotInstanceList.remove(ss);
				}
				if (currInstanceList.contains(cr)) {
					currInstanceList.remove(cr);
				}
			}
		}
	}
	
	private Map<String, String> getSubFlowId(String json) {
		JSONObject jsonObj = JSON.parseObject(json);
		List<Map<String, Object>> nodes =(List<Map<String,Object>>) jsonObj.get("nodeDataArray");
		Map<String, String> flowIdForOp = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			if (node.get("subflowName") != null && STOP.equals(node.get("subflowName"))) {
				flowIdForOp.put(STOP, node.get("subflowid")+"_"+node.get("key"));
				continue;
			}
			if (node.get("subflowName") != null && DESTROY.equals(node.get("subflowName"))) {
				flowIdForOp.put(DESTROY, node.get("subflowid")+"_"+node.get("key"));
				continue;
			}
			if (node.get("subflowName") != null && DEPLOY.equals(node.get("subflowName"))) {
				flowIdForOp.put(DEPLOY, node.get("subflowid")+"_"+node.get("key"));
				continue;
			}
			if (node.get("subflowName") != null && START.equals(node.get("subflowName"))) {
				flowIdForOp.put(START, node.get("subflowid")+"_"+node.get("key"));
				continue;
			}
		}
		return flowIdForOp;
	}
	
	private Map<String, String> getInputAndOutput(String config) {
		Map<String, String> inputAndOutput = new HashMap<>();
		JSONObject jsonObj = JSON.parseObject(config);
		Map<String, Map<String, Object>> configMap = (Map<String, Map<String,Object>>) jsonObj.get("config");
		inputAndOutput.put("input", JSON.toJSONString(configMap.get("input")));
		inputAndOutput.put("output", JSON.toJSONString(configMap.get("output")));
		return inputAndOutput;
	}
	
	private void putMessage(Map<String, List<Object>> message) {
		RestTemplate rest = new RestTemplate(httpRequestFactory);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
		mvm.add("message", JSON.toJSONString(message));
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(
				mvm, requestHeaders);
		String messageResult = rest.postForObject(flowServerUrl
				+ "/cd/message.wf", httpEntity, String.class);
		log.debug("messageResult: {}", messageResult);
	}
}
