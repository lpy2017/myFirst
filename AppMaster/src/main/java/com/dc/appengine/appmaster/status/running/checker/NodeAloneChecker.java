package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;
import com.dc.appengine.appmaster.status.running.list.InDBNodeSelector;

public class NodeAloneChecker extends NodeResourceChecker<Boolean> {
	
	private final Logger log = LoggerFactory.getLogger(NodeAloneChecker.class);
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
		//如果node已经被独占，返回false
		if( node.isAlone() ) {
			return false;
		} else {
			if( value ){
				return node.getInstanceNum() == 0 ;
			} else {
				return true;
			}
		}
	}

	@Override
	public Boolean getStrategyValue(Map<String, String> strategy) {
		return Boolean.parseBoolean( strategy.get( Item.CODE_ALONE ) );
	}

}
