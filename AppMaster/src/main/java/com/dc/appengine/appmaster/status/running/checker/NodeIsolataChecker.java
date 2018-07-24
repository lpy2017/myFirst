package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;

public class NodeIsolataChecker extends NodeResourceChecker<Boolean> {

	@Override
	public boolean check(Map<String, String> strategy, MVNode node, boolean op) {
		if( this.docheck(strategy, node) ){
			if( checker != null ){
				return this.checker.check(strategy, node, true);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean compare(Boolean value, MVNode node) {
		return value.equals( node.getUsedLxc() );
	}

	@Override
	public Boolean getStrategyValue(Map<String, String> strategy) {
		return Boolean.parseBoolean( strategy.get( Item.CODE_STRONG_ISOLATA ) );
	}

}
