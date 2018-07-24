package com.dc.workflow.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageReceiver {
	
	public static boolean put(Map<String, List<Object>> message) {
		boolean flag = true;
		try {
			Set<String> flow_ids = message.keySet();
			for (String flow_id : flow_ids) {
				MessageHelper.put(flow_id, message.get(flow_id));
			}
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

}
