package com.dc.appengine.appmaster.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.entity.StrategyItem;

public class StrategyHelper {
	private static Map<String, String> codeMap;

	static {
		initCodeMap();
	}

	public static void addItem(String key, Object value, List<StrategyItem> items) {
		StrategyItem item = new StrategyItem();

		String code = codeMap.get(key);
		if (code != null) {
			item.setItemCode(code);
			if (Item.CODE_ALONE.equals(code)) {
				Boolean isAlone=false;
				if(value instanceof Boolean){
					boolean shareHost=(Boolean)value;
					isAlone=!shareHost;
				}else if(value instanceof String){
					boolean shareHost=Boolean.parseBoolean((String)value);
					isAlone=!shareHost;
				}
				item.setValue(isAlone.toString());
			}else if(Item.CODE_OUTGOING_IPS.equals(code)){
				String val = JSON.toJSONString(value);
				item.setValue(val);
			}else if(Item.CODE_LABELS.equals(code)){
				String val = JSON.toJSONString(value);
				item.setValue(val);
			}else{
				item.setValue(value.toString());
			}
			items.add(item);
		}
	}
	
	public static Map<Object, Object> getValue(List<StrategyItem> items, String code) {
		if (items != null && !items.isEmpty()) {
			for (StrategyItem item : items) {
				if (code.equals(item.getItemCode())) {
					return MessageHelper.wrapMessage("result", true, "value", item.getValue());
				}
			}
		}
		return MessageHelper.wrapMessage("result", false, "value", null);
	}

	private static void initCodeMap() {
		codeMap = new HashMap<String, String>();
		codeMap.put("replicas", Item.CODE_INSTANCE_NUMBER);
		codeMap.put("sameHost", Item.CODE_SAMEHOST);
		codeMap.put("shareHost", Item.CODE_ALONE);
		codeMap.put("memory", Item.CODE_XMX);
		codeMap.put("cpus", Item.CODE_CPU_NUMBER);
		codeMap.put("shareCpu", Item.CODE_SHARED_CPU);
		codeMap.put("cpuQuota", Item.CODE_CPU_QUOTA);
		codeMap.put("nodeCluster", Item.CODE_CLUSTERID);
		codeMap.put("nodes", Item.CODE_NODEIDS);
		codeMap.put("resourceUpdateType", Item.CODE_RESOURCE_UPDATE_TYPE);
		codeMap.put("isolate", Item.CODE_STRONG_ISOLATA);
		codeMap.put("store", Item.CODE_DISK);
		codeMap.put("outgoing_ips", Item.CODE_OUTGOING_IPS);
		
		//模拟双向map
		codeMap.put("labels", Item.CODE_LABELS);
		codeMap.put(Item.CODE_LABELS, "labels");
		//模拟双向map
		
		codeMap.put(Item.CODE_INSTANCE_NUMBER, "replicas");
		codeMap.put(Item.CODE_SAMEHOST,"sameHost");
		codeMap.put(Item.CODE_ALONE, "shareHost");
		codeMap.put(Item.CODE_XMX, "memory");
		codeMap.put(Item.CODE_CPU_NUMBER, "cpus");
		codeMap.put(Item.CODE_SHARED_CPU, "shareCpu");
		codeMap.put(Item.CODE_CPU_QUOTA, "cpuQuota");
		codeMap.put(Item.CODE_CLUSTERID, "nodeCluster");
		codeMap.put(Item.CODE_NODEIDS,"nodes");
		codeMap.put(Item.CODE_RESOURCE_UPDATE_TYPE, "resourceUpdateType");
		codeMap.put(Item.CODE_STRONG_ISOLATA, "isolate");
		codeMap.put(Item.CODE_DISK,"store");
		codeMap.put(Item.CODE_OUTGOING_IPS, "outgoing_ips");
	}
	public static String getFrontDisplay(String key){
		return codeMap.get(key);
	}
}
