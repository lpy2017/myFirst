package com.dc.appengine.appmaster.status.running.checker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.StrategyItem;

public class StrategyItemsUtils {
	
	public static Map<String,String> ItemsToMap( List<StrategyItem> strategyItems ){
		Map<String,String> map = new HashMap<String,String>();
		if( strategyItems != null ){
			for( StrategyItem si : strategyItems ){
				map.put( si.getItemCode(), si.getValue() ); 
			}
		}
		return map;
	}
}
