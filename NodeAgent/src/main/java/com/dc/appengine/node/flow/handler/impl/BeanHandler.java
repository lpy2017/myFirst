/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.handler.impl;

import java.net.URI;

import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.flow.handler.SchemeHandler;
import com.dc.appengine.node.flow.model.BeanDefinition;

/**
 * BeanHandler.java
 * @author liubingj
 */
public class BeanHandler implements SchemeHandler {

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.flow.SchemeHandler#handle(java.net.URI, com.dc.appengine.Context)
	 */
	public void handle( URI uri, Context context ) throws Exception {
		
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.flow.SchemeHandler#handle(java.net.URI)
	 */
	@SuppressWarnings( "unchecked" )
	public < R > R handle( URI uri ) throws Exception {
		if ( uri == null ) {
			throw new IllegalArgumentException( "Uri can not be null." );
		}
		if ( ! uri.getScheme().equals( "bean" ) ) {
			throw new IllegalArgumentException( "The scheme not equals 'bean'" );
		}
		final BeanDefinition beanDef = new BeanDefinition();
		final String id = uri.getHost();
		if ( id == null || "".equals( id ) ) {
			throw new IllegalArgumentException( "The user-info of uri can not be null, because the id can not be null." );
		}
		beanDef.setId( id );
		beanDef.setClazz( uri.getUserInfo() );
		final String query = uri.getQuery();
		if ( query != null && ! "".equals( query ) ) {
			if ( query.startsWith( "method=" ) ) {
				final String method = query.substring( "method=".length() );
				beanDef.setMethod( method );
			} else {
				throw new IllegalArgumentException( "The query of uri must be start with 'method='." );
			}
		}
		beanDef.setSingleton( true );
		return ( R ) beanDef;
	}

}
