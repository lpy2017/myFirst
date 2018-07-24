/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow;

import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.node.flow.handler.SchemeHandler;
import com.dc.appengine.node.flow.handler.impl.BeanHandler;
import com.dc.appengine.node.flow.handler.impl.BrokerHandler;
import com.dc.appengine.node.flow.handler.impl.EnvHandler;

/**
 * URIProcessEngine.java
 * @author liubingj
 */
public class SchemeFactory {

	private static final SchemeFactory INSTANCE = new SchemeFactory();
	
	private Map< String, SchemeHandler > processors = new HashMap< String, SchemeHandler >( 16 );

	static {
		INSTANCE.processors.put( "bean", new BeanHandler() );
		INSTANCE.processors.put( "broker", new BrokerHandler() );
		INSTANCE.processors.put( "env", new EnvHandler() );
	}

	private SchemeFactory() {
	}
	
	public static SchemeFactory getInstance() {
		return INSTANCE;
	}
	
	public SchemeHandler get( String key ) {
		return processors.get( key );
	}
	
}
