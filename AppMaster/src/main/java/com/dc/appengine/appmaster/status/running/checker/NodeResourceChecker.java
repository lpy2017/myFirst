package com.dc.appengine.appmaster.status.running.checker;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.message.bean.MVNode;

public abstract class NodeResourceChecker<T> implements INodeResourceChecker<T> {
	private static final Logger log = LoggerFactory.getLogger(NodeResourceChecker.class);
	protected INodeResourceChecker<?> checker;
	
	public INodeResourceChecker<?> getChecker() {
		return checker;
	}

	public void setChecker(INodeResourceChecker<?> checker) {
		this.checker = checker;
	}
	
	public boolean docheck( Map<String,String> strategy, MVNode node ){
		T value = getStrategyValue( strategy );
		boolean result = compare( value, node );
		log.info("筛选node:check结果："+result+" value:"+value);
		return result;
	}
	
	public abstract T getStrategyValue( Map<String,String> strategy );
	public abstract boolean compare( T value, MVNode node);
	public abstract boolean check( Map<String,String> strategy, MVNode node, boolean op );
}
