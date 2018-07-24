package com.dc.appengine.appmaster.service.impl;

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
import com.dc.appengine.appmaster.dao.impl.ComponentUpdateDAO;
import com.dc.appengine.appmaster.service.IMessageService;

@Service("componentUpdateService")
public class ComponentUpdateService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("messageService")
	IMessageService messageService;
	
	@Autowired
	@Qualifier("componentUpdateDAO")
	ComponentUpdateDAO componentUpdateDAO;
	
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	
	private final String DEPLOY = "deploy";
	private final String START = "start";
	private final String STOP = "stop";
	private final String DESTROY = "destroy";
	
	private static final HttpComponentsClientHttpRequestFactory httpRequestFactory;

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(3000);
		httpRequestFactory.setConnectTimeout(3000);
		httpRequestFactory.setReadTimeout(3000);
	}
	
	private Map<String, String> getSubFlowId(String json) {
		JSONObject jsonObj = JSON.parseObject(json);
		List<Map<String, Object>> nodes =(List<Map<String,Object>>) jsonObj.get("nodeDataArray");
		Map<String, String> OpDict = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			if (node.get("subflowName") != null && STOP.equals(node.get("subflowName"))) {
				OpDict.put(STOP, (String) node.get("subflowid"));
				OpDict.put(STOP + "_key", node.get("key") + ""); 
				continue;
			}
			if (node.get("subflowName") != null && DESTROY.equals(node.get("subflowName"))) {
				OpDict.put(DESTROY, (String) node.get("subflowid"));
				OpDict.put(DESTROY + "_key", node.get("key") + "");
				continue;
			}
			if (node.get("subflowName") != null && DEPLOY.equals(node.get("subflowName"))) {
				OpDict.put(DEPLOY, (String) node.get("subflowid"));
				OpDict.put(DEPLOY + "_key", node.get("key") + "");
				continue;
			}
			if (node.get("subflowName") != null && START.equals(node.get("subflowName"))) {
				OpDict.put(START, (String) node.get("subflowid"));
				OpDict.put(START + "_key", node.get("key") + "");
				continue;
			}
		}
		return OpDict;
	}
	
	private Map<String, String> getInputAndOutput(String config) {
		Map<String, String> inputAndOutput = new HashMap<>();
		JSONObject jsonObj = JSON.parseObject(config);
		inputAndOutput.put("input", JSON.toJSONString(jsonObj.get("input")));
		inputAndOutput.put("output", JSON.toJSONString(jsonObj.get("output")));
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
	
	public String update(long appId, String resourceVersionId, String config, String flowId) {
		log.debug("appId = {}", appId);
		log.debug("resourceVersionId = {}", resourceVersionId);
		log.debug("config = {}", config);
		log.debug("flowId = {}", flowId);
		String json = componentUpdateDAO.findJsonByFlowId(flowId);
		log.debug("json = {}", json);
		Map<String, String> OpDict = this.getSubFlowId(json);
//		long key = componentUpdateDAO.findKeyByAppId(appId);
//		String keySuffix = "_" + key;
		List<String> instances = componentUpdateDAO.findInstancesByAppId(appId);
		Map<String, List<Object>> message = new HashMap<>();
		message.put(OpDict.get(STOP) + "_" + OpDict.get(STOP + "_key"), messageService.messages(instances, STOP));
		message.put(OpDict.get(DESTROY) + "_" + OpDict.get(DESTROY + "_key"), messageService.messages(instances, DESTROY));
		Map<String, String> inputAndOutput = this.getInputAndOutput(config);
		Map<String, Object> param = new HashMap<>();
		param.put("appId", appId);
		param.put("resourceVersionId", resourceVersionId);
		param.put("input", inputAndOutput.get("input"));
		param.put("output", inputAndOutput.get("output"));
		componentUpdateDAO.updateInputAndOutput(param);
		message.put(OpDict.get(DEPLOY) + "_" + OpDict.get(DEPLOY + "_key"), messageService.messages(instances, DEPLOY));
		message.put(OpDict.get(START) + "_" + OpDict.get(START + "_key"), messageService.messages(instances, START));
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		log.debug("start flow result: {}", startResult);
		return startResult;
	}

}
