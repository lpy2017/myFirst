package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;

public class NodeDiskChecker extends NodeResourceChecker<Integer> {

	@Override
	public boolean check(Map<String, String> strategy, MVNode node, boolean op) {
		boolean flag = true;
		if( getStrategyValue( strategy ) != null ) {
			flag = this.docheck(strategy, node);
		}
		if( flag && op ){
			if( checker != null ){
				return this.checker.check(strategy, node, true );
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean compare(Integer value, MVNode node) {
		return node.getDisk() >= value;
	}

	@Override
	public Integer getStrategyValue(Map<String, String> strategy) {
		if( strategy.get( Item.CODE_DISK ) != null ){
			return Integer.parseInt( strategy.get( Item.CODE_DISK ) );
		}
		return null;
	}

}
