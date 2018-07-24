package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;

public class NodeMemoryChecker extends NodeResourceChecker<Integer> {

	@Override
	public boolean check(Map<String,String> strategy, MVNode node,
			boolean op) {
		if( op ){
			return checker.check(strategy, node, op) && this.docheck(strategy, node);
		} else {
			return checker.check(strategy, node, op) || this.docheck(strategy, node);
		}
	}

	@Override
	public boolean compare(Integer value, MVNode node) {
		return node.getMemory() >= value;
	} 

	@Override
	public Integer getStrategyValue(Map<String,String> strategy) {
		return Integer.parseInt( strategy.get( Item.CODE_XMX ) );
	}


}
