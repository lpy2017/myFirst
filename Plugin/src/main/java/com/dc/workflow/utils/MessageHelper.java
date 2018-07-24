package com.dc.workflow.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MessageHelper {
	
	private final static Logger log = LoggerFactory.getLogger(MessageHelper.class);
	
//	private static ConcurrentHashMap<String, BlockingQueue<Object>> bucket = new ConcurrentHashMap<String, BlockingQueue<Object>>();
	private static Map<String, Map<String, JSONObject>> bucket = new ConcurrentHashMap<>();
	
//	public static boolean put(String flow_id, List<Object> flow_info) {
//		boolean flag = true;
//		try {
//			BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(flow_info.size());
//			for (Object object : flow_info) {
//				queue.offer(object);
//			}
//			bucket.put(flow_id, queue);
//		} catch (Exception e) {
//			flag = false;
//		}
//		return flag;
//	}
	
	public static boolean put(String key, List<Object> messages) {
		boolean flag = true;
		try {
			Map<String, JSONObject> messageDict = new ConcurrentHashMap<>();
			for (Object message : messages) {
				JSONObject jsonObj = JSONObject.parseObject(JSON.toJSONString(message));
				if(jsonObj.get("hostIp") != null){
					String hostIp = (String)jsonObj.get("hostIp");
					//封装出的空消息只带hostIp一个key，传给frame前remove掉，告诉frame消息为空
					if(jsonObj.get("instanceId") == null){
						jsonObj.remove("hostIp");
					}
					messageDict.put(hostIp, jsonObj);
				}
				//current+target情况时获取hostIp需要深一层寻找
				else{
					JSONObject current = (JSONObject) jsonObj.get("current");
					messageDict.put((String) current.get("hostIp"), jsonObj);
				}
			}
			bucket.put(key, messageDict);
		} catch (Exception e) {
			log.error("", e);
			flag = false;
		}
		return flag;
	}
	
//	public static Object take(String flow_id) {
//		Object obj = null;
//		try {
//			if (bucket.get(flow_id) == null) {
//				return null;
//			}
//			if (bucket.get(flow_id).isEmpty()) {
//				bucket.remove(flow_id);
//				return null;
//			}
//			obj = bucket.get(flow_id).take();
//			if (bucket.get(flow_id).isEmpty()) {
//				bucket.remove(flow_id);
//			}
//		} catch (Exception e) {
//		}
//		return obj;
//	}
	
	public static Object take(String key, String ip) {
		log.debug("take message begin, key = {}, ip = {}", key, ip);
		Object obj = null;
		try {
			synchronized (MessageHelper.class) {
				Map<String, JSONObject> messages = bucket.get(key);
				if (messages == null || messages.isEmpty()) {
					return null;
				}
				if (ip == null || "".equals(ip)) {
					Set<String> keys = messages.keySet();
					String[] keysArray = keys.toArray(new String[0]);
					ip = keysArray[0];
				}
				obj = messages.get(ip);
				messages.remove(ip);
				if (messages.isEmpty()) {
					bucket.remove(key);
				}
				return obj;
			}
		} catch (Exception e) {
			log.error("", e);
		}
		log.debug("take message end, key = {}, ip = {}, message = {}", key, ip, obj);
		return obj;
	}
	
	public static Object print() {
		return bucket;
	}
	
//	public static boolean clear(String id_pre) {
//		boolean flag = true;
//		try {
//			Enumeration<String> keys = bucket.keys();
//			List<String> keylist = new ArrayList<>();
//			while (keys.hasMoreElements()) {
//				String key = keys.nextElement();
//				if (key.contains(id_pre)) {
//					keylist.add(key);
//				}
//			}
//			for (String key : keylist) {
//				bucket.remove(key);
//			}
//		} catch (Exception e) {
//			flag = false;
//		}
//		return flag;
//	}
	
	public static boolean clear(String key) {
		boolean flag = true;
		try {
			bucket.remove(key);
		} catch (Exception e) {
			log.error("", e);
			flag = false;
		}
		return flag;
	}

}
