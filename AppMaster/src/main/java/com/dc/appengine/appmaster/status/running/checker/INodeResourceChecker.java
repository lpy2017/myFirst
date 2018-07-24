package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import com.dc.appengine.appmaster.message.bean.MVNode;

public interface INodeResourceChecker<T> {
	
	boolean check( Map<String,String> strategy, MVNode node, boolean op );
	boolean docheck( Map<String,String> strategy, MVNode node );
	T getStrategyValue( Map<String,String> strategy );
	boolean compare( T value, MVNode node);
	
}
