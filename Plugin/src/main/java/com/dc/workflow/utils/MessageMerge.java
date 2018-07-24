package com.dc.workflow.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MessageMerge {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public synchronized String merge(String single, String all) {
		JSONObject jsonSingleObj = JSON.parseObject(single);
		JSONObject jsonAllObj = JSON.parseObject(all);
		Map<String, Object> message = (Map<String, Object>) jsonSingleObj.get("message");
		String componentName = (String) message.get("componentName");
		if (jsonAllObj.containsKey(componentName)) {
			List<Object> list = (List<Object>) jsonAllObj.get(componentName);
			list.add(message);
		} else {
			List<Object> list = new ArrayList<>();
			list.add(message);
			jsonAllObj.put(componentName, list);
		}
		return JSON.toJSONString(jsonAllObj);
	}
	
	public synchronized String merge(String[] messages) {
		try {
			log.info("message merge input:{}", JSON.toJSONString(messages));
			Map<String, Object> all = new HashMap<>();
			for (String msgStr : messages) {
				JSONObject jsonObj = JSON.parseObject(msgStr);
				if (jsonObj.containsKey("message")) {
					Object msg = jsonObj.get("message");
					if(msg instanceof String){
						String str = (String)msg;
						if("".equals(str.trim())){
							continue;
						}
					}
					if(msg instanceof Map){
						Map<String, Object> message = (Map<String, Object>) jsonObj.get("message");
						if (message.containsKey("componentName")) {
							String componentName = (String) message.get("componentName");
							if (all.containsKey(componentName)) {
								List<Object> list = (List<Object>) all.get(componentName);
								list.add(message);
							} else {
								List<Object> list = new ArrayList<>();
								list.add(message);
								all.put(componentName, list);
							}
						}
					}
				}
			}
			log.info("message merge output:{}", JSON.toJSONString(all));
			return JSON.toJSONString(all);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}

}
