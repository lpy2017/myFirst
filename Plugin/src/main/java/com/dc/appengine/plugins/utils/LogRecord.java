package com.dc.appengine.plugins.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class LogRecord {
	private static Logger log = LoggerFactory.getLogger(LogRecord.class);
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	public  static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
	public static Map<String,String> generateParams(String json){
		Map<String, String>result= new HashMap<>();
		Map<String, Object> paramMap = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
		});
		Map<String,Object> messageMap = null;
		if(paramMap.get("message") instanceof String){
			messageMap =JSON.parseObject(paramMap.get("message").toString(), new TypeReference<Map<String, Object>>() {
			});
		}else{
			messageMap = (Map<String,Object>)paramMap.get("message");
		}
		String flowId=messageMap.get("flowId")==null?"":messageMap.get("flowId").toString();
		String instanceId=messageMap.get("instanceId")==null?"":messageMap.get("instanceId").toString();
		String nodeId=messageMap.get("nodeId")==null?"":messageMap.get("nodeId").toString();
		result.put("flowId", flowId);
		result.put("instanceId", instanceId);
		result.put("nodeId", nodeId);
		return result;
	}
	
	public static void send2Master(String tokenId, String message) {
		Map<String, Object>form= new HashMap<>();
		form.put("state", "");//master不再存节点状态，节点的异常状态由工作流存储
		form.put("id", tokenId);
		form.put("flowNodeLog", message);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(ConfigHelper.getValue("masterRest") + "/ws/blueprint/logRecord");
		String result = target.request().put(Entity.entity(JSON.toJSONString(form), MediaType.APPLICATION_JSON), String.class);
		log.debug(result);
	}

	public static String getCurrentTime() {
		
//		long t = System.currentTimeMillis();
//		int offset = (int) (t % (24 * 60 * 60 * 1000) + TimeZone.getDefault().getRawOffset()) / 1000;
//		int hour = offset / 3600;
//		int minute = (offset % 3600) / 60;
//		int second = offset % 60;
//		StringBuilder sb = new StringBuilder();
//		sb.append(hour);
//		sb.append(":");
//		if (minute < 10) {
//			sb.append("0");
//		}
//		sb.append(minute);
//		sb.append(":");
//		if (second < 10) {
//			sb.append("0");
//		}
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(new Date())).append("--");
		return sb.toString();
	}

	public static void main(String[] args) {
		try {
			int i = 0;
			int j = 3;
			int k = 3 / 0;
		} catch (Exception e) {
			e.printStackTrace();
			getStackTrace(e);
		}

	}

}
