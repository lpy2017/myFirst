/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * StateCache.java
 * @author liubingj
 */
public class StateCache {

	private static StateCache instance = new StateCache();

	private final Map< String, Object > content = new HashMap< String, Object >( 16 );
	
	private final ReentrantLock lock = new ReentrantLock();
	
	private StateCache() {
	}
	
	public static StateCache getInstance(){
		return instance;
	}

	public Map< String, Object > cloneContent() {
		Map< String, Object > clone = null;
		this.lock.lock();
		try {
			//StateCache.Attributes.NODETYPE, NodeEnv.getInstance().isDocker()?
			content.put(StateCache.Attributes.NODETYPE,"docker" );
			clone = new HashMap< String, Object >( content );
		} finally {
			this.lock.unlock();
		}
		return clone;
	}

	public void clear() {
		this.lock.lock();
		try {
			content.clear();
		} finally {
			this.lock.unlock();
		}
	}
	
	public void put( String key, Object value ) {
		this.lock.lock();
		try {
			content.put( key, value );
		} finally {
			this.lock.unlock();
		}
	}
	
	public void putAll( Map< String, Object > map ) {
		this.lock.lock();
		try {
			content.putAll( map );
		} finally {
			this.lock.unlock();
		}
	}

	/**
	 * 
	 * @param valueOf
	 * @return
	 */
	public Object get( String key ) {
		this.lock.lock();
		try {
			return content.containsKey( key ) ? content.get( key ) : null;
		} finally {
			this.lock.unlock();
		}
	}
	
	public class Attributes {
		
		private Attributes() {
		}
		
		public static final String NODE = "NODE";
		
		public static final String APPS = "APPS";
		
		public static final String CONTAINER = "CONTAINER";
		
		public static final String NODETYPE = "NODETYPE";
		
		public static final String DOCKER = "DOCKER";
	}
	
}
