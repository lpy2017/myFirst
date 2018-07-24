/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.processor;

import java.net.URI;

import com.dc.appengine.node.flow.SchemeFactory;
import com.dc.appengine.node.flow.handler.SchemeHandler;
import com.dc.appengine.node.flow.model.BeanUriDefinition;
import com.dc.appengine.node.preloader.AbstractProcessor;

/**
 * BeanUriProcessor.java
 * @author liubingj
 */
public abstract class BeanUriProcessor< T extends BeanUriDefinition > extends AbstractProcessor< T > {

	/**
	 * @param definition
	 */
	public BeanUriProcessor( T definition ) {
		super( definition );
	}
	
	protected void parseUri( URI uri ) throws Exception {
		final String scheme = uri.getScheme();
		final SchemeHandler handler = SchemeFactory.getInstance().get( scheme );
		callback( handler.handle( uri ) );
	}
	
	protected abstract < R > void callback( R r );

}
