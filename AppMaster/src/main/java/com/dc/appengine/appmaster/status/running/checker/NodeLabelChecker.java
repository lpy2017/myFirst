package com.dc.appengine.appmaster.status.running.checker;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;



public class NodeLabelChecker extends NodeResourceChecker<Map<String,String>> {

	@Override
	public Map<String, String> getStrategyValue(Map<String, String> strategy) {
		if(!strategy.containsKey(Item.CODE_LABELS)){
			return Collections.EMPTY_MAP;
		}else{
			String labelsStr = strategy.get(Item.CODE_LABELS);
			if(labelsStr!=null && !labelsStr.equals("")){
				Map<String,String> result = JSON.parseObject(labelsStr, 
						new TypeReference<Map<String,String>>(){});
				return result;
			}else{
				return Collections.EMPTY_MAP;
			}
		}
	}

	@Override
	public boolean compare(Map<String, String> value,
			com.dc.appengine.appmaster.message.bean.MVNode node) {
		return false;
	}

	@Override
	public boolean check(Map<String, String> strategy,MVNode node, boolean op) {
		Map<String,String> labels = getStrategyValue(strategy);
		if(labels.size()==0){
			if(this.checker == null){
				return true;
			}else{
				return this.checker.check(strategy, node, false);
			}
		}else{
			List<Map> nodeLabels = node.getLabelList();
			Map<String,String> availableLabels = new HashMap<String,String>();
			if(nodeLabels!=null && nodeLabels.size()>0){
				for(Map map:nodeLabels){
					availableLabels.put((String)map.get("label_key"), 
							(String)map.get("label_value"));
				}
			}
			
			boolean b = CollectionUtils.containsAll(availableLabels.entrySet(),labels.entrySet());
			if(!b){
				return false;
			}
		}
		if(this.checker == null){
			return true;
		}else{
			return this.checker.check(strategy, node, false);
		}
	}

}