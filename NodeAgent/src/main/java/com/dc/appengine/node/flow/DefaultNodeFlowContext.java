package com.dc.appengine.node.flow;
/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.dc.appengine.node.configuration.Context;

/**
 * 默认的Node流程上下文
 * @author liubingj
 */
public class DefaultNodeFlowContext implements Context {

	private static final long serialVersionUID = 8669909339539724565L;

	private String id;
	
	private long startTime;
	
	private Map< String, Object > attributes = new HashMap< String, Object >( 16 ); 
	
	private Object payload;
	
	public DefaultNodeFlowContext() {
		this.id = Maker.make();
		this.startTime = System.currentTimeMillis();
	}
	
	public DefaultNodeFlowContext( Object payload ) {
		this();
		this.payload = payload;
	}
	
	public DefaultNodeFlowContext( Map< String, Object > attribute, Object payload ) {
		this();
		this.attributes.putAll( attribute );
		this.payload = payload;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#getId()
	 */
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#getStartTime()
	 */
	public long getStartTime() {
		return this.startTime;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#getAttributes()
	 */
	public Map< String, Object > getAttributes() {
		return this.attributes;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#getAttribute(java.lang.String)
	 */
	@SuppressWarnings( "unchecked" )
	public < T > T getAttribute( String key ) {
		return ( T ) this.attributes.get( key );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#setAttributes(java.util.Map)
	 */
	public void setAttributes( Map< String, Object > attributes ) {
		this.attributes.putAll( attributes );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#setAttribute(java.lang.String, java.lang.Object)
	 */
	public < T > void setAttribute( String key, T value ) {
		this.attributes.put( key, value );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#getPayload()
	 */
	@SuppressWarnings( "unchecked" )
	public < T > T getPayload() {
		return ( T ) this.payload;
	}
	
	/* (non-Javadoc)
	 * @see com.dc.appengine.Context#setPayload(java.lang.Object)
	 */
	public < T > void setPayload( T payload ) {
		this.payload = payload;
	}
	
	private static class Maker {
		
		static String make() {
			UUID uuid = null;
			synchronized ( Maker.class ) {
				uuid = UUID.randomUUID();
			}
			return uuid.toString();
		}
		
	}

}
