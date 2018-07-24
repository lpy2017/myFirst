/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.handler.impl;

import java.net.URI;

import com.dc.appengine.node.flow.handler.SchemeHandler;


/**
 * EnvHandler.java
 * @author liubingj
 */
public class EnvHandler implements SchemeHandler {

	@SuppressWarnings( "unchecked" )
	public < R > R handle( URI uri ) throws Exception {
//		final String host = uri.getHost();
		String result = null;
//		if ( host.equals( "broker" ) ) {
//			result = parseBroker( uri.getPath().substring( 1 ) );
//		} else 
//		if ( host.equals( "master" ) ) {
//			result = NodeEnv.getInstance().getDefinition().getMasterAddress();
//		} else if ( host.equals( "loadBalance" ) ) {
//			result = NodeEnv.getInstance().getDefinition().getLoadBalanceAddress();
//		}
		return ( R ) URI.create( result );
	}
	
//	private String parseBroker( String path ) {
//		String queue = "";
//		if ( path.equals( "in" ) ) {
//			queue = NodeEnv.getInstance().getDefinition().getBroker().getIn();
//		} else {
//			queue = NodeEnv.getInstance().getDefinition().getBroker().getOut();
//		}
//		return queue;
//	}

}
