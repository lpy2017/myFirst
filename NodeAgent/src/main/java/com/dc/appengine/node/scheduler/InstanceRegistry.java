/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * InstanceRegistry.java
 * @author liubingj
 */
public class InstanceRegistry {

	private static final InstanceRegistry INSTANCE = new InstanceRegistry();
	
	private Map< String, Object > instances = new ConcurrentHashMap< String, Object >( 16 );
	
	private InstanceRegistry() {
	}
	
	public static InstanceRegistry getInstance() {
		return INSTANCE;
	}

	public void put( String key, Object value ) {
		instances.put( key, value );
	}
	
	public Object get( String clazz ) {
		return instances.get( clazz );
	}
	
	public boolean contains( String clazz ) {
		return instances.containsKey( clazz );
	}

}
