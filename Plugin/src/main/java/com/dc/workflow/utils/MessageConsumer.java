package com.dc.workflow.utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.utils.ConfigHelper;

public class MessageConsumer {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public String take(String json) {
		log.debug("message take input:{}", json);
		try {
			JSONObject jobj = JSON.parseObject(json);
			// 向master发送组织消息的请求 begin
			String tokenId = jobj.getString("tokenId");
			String flowId = (String) jobj.get("parPdId");
			String flowInstanceId = (String) jobj.get("parinstanceId");
			String flowKey = (String) jobj.get("flowInstanceId");
			JSONObject allParam = (JSONObject) jobj.get("insvarMap");
			//String messageIp = allParam.getString("_message_ip");
			String messageIp = (String) jobj.get("_message_ip");
			String cdInstanceId = "";
			String regetmsg = jobj.getString("regetmsg");
			Object message = jobj.get("message");
			if(message instanceof String){
				String oldMessage = (String)message;
				if("".equals(oldMessage)){
					//正常的组织消息或者流程开始节点组的消息为空的干预
				}
			}
			else if(message instanceof Map){
				Map<String, Object> map = (Map<String, Object>)message;
				//流程的某个插件出错的干预
				if("true".equals(regetmsg)){
					if(map.containsKey("instanceId")){
						cdInstanceId = "" + map.get("instanceId");
					}
				}
			}
			else{
			}
			Map<String, Object> masterParam = new HashMap<>();
			masterParam.put("tokenId", tokenId);
			masterParam.put("flowId", flowId);
			masterParam.put("flowKey", flowKey);
			masterParam.put("flowInstanceId", flowInstanceId);
			masterParam.put("cdInstanceId", cdInstanceId);
			masterParam.put("regetmsg", regetmsg);
			masterParam.put("insvarMap", allParam);
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(ConfigHelper.getValue("masterRest") + "/ws/blueprint/prepareSubFlowMessage");
			log.debug("send to master begin {}", JSON.toJSONString(masterParam));
			target.request().post(Entity.entity(JSON.toJSONString(masterParam), MediaType.APPLICATION_JSON), String.class);
			log.debug("send to master end {}", JSON.toJSONString(masterParam));
			// 向master发送组织消息的请求 end
//			String flow_instance_id = (String) jobj.get("flowInstanceId");
//			String insvarMap = null;
			Object msg = MessageHelper.take(flowInstanceId + "_" + flowKey, messageIp);
//			if(msg == null){
//				msg = "";
//			}
			jobj.put("message", msg);
//			if (jobj.containsKey("cdMessageInsvarMap")) {
//				insvarMap = (String) jobj.get("cdMessageInsvarMap");
//			} else {
//				insvarMap = (String) jobj.get("insvarMap");
//			}
//			jobj.put("message", MessageHelper.take(flow_instance_id));
//			jobj.put("cdMessageInsvarMap", insvarMap);
			log.debug("message take output:{}", jobj);
			return JSON.toJSONString(jobj);
		} catch (Exception e) {
			log.error("", e);
		}
		return json;
	}
	
	public String after(String json) {
		log.debug("message after input:{}", json);
		try {
			JSONObject jobj = JSON.parseObject(json);
			JSONObject insvarMap = (JSONObject) jobj.get("insvarMap");
			Object msg = jobj.get("message");
			if(msg != null){
				Map<String, Object> message = null;
				//正常子流程、智能子流程
				if(msg instanceof Map){
					//正常子流程
					if(((Map<String, Object>) msg).size() > 0){
						message = (Map<String, Object>) msg;
					}
				}
				//异常子流程
				else if(msg instanceof String){
					String msgString = (String)msg;
					//有message内容的异常子流程
					if(!"".equals(msgString)){
						message = JSON.parseObject(msgString, new TypeReference<Map<String, Object>>(){});
					}
				}
				else{
				}
				//有message内容回调
				if(message != null){
					message.put("insvarMap", insvarMap);
//					jobj.put((String) message.get("componentName"), JSON.parseObject(JSON.toJSONString(message)));
					Client client = ClientBuilder.newClient();
					WebTarget target = client.target(ConfigHelper.getValue("masterRest") + "/ws/deployedApp/postOpUpdate");
					target.request().put(Entity.entity(JSON.toJSONString(message), MediaType.APPLICATION_JSON), String.class);
				}
			}
			json = JSON.toJSONString(jobj);
			log.debug("message after output:{}", json);
		} catch (Exception e) {
			log.error("", e);
		}
		return json;
	}
	
	//调用master接口动态获取自动任务节点(静态资源池)的ins
	public String getAutoTaskIns(String json) {
		log.info("resource pool info:", json);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(ConfigHelper.getValue("masterRest") + "/ws/blueprint/getBlueprintResourcePoolIns");
		String ins = target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON), String.class);
		//{"result":"true","message":"成功","ins":"1"}
		//{"result":"false","message":"失败"}
		return ins;
	}
	
	public static void main(String[] args){
		HashMap map = new HashMap<String, String>();
		map.put("nodeKey", "-1");
		map.put("blueprintId", "12345");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://10.1.108.33:5091/masterl" + "/ws/blueprint/getBlueprintResourcePoolIns");
		String ins = target.request().post(Entity.entity(JSON.toJSONString(map), MediaType.APPLICATION_JSON), String.class);
	}

}
