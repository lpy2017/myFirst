/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.processor;

import java.net.URI;

import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.model.ProcessorDefinition;
import com.dc.appengine.node.flow.SchemeFactory;
import com.dc.appengine.node.flow.handler.SchemeHandler;
import com.dc.appengine.node.flow.model.ToDefinition;
import com.dc.appengine.node.preloader.AbstractProcessor;

/**
 * ToProcessor.java
 * @author liubingj
 */
public class ToProcessor extends AbstractProcessor< ToDefinition > {

	/**
	 * @param definition
	 */
	public ToProcessor( ToDefinition definition ) {
		super( definition );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.processor.Processor#process(java.lang.Object)
	 */
	public < T, R > R process( T t ) throws Exception {
		final String _uri = getDefinition().getUri();
		final URI uri = URI.create( _uri );
		final SchemeHandler handler = SchemeFactory.getInstance().get( uri.getScheme() );
		final ProcessorDefinition processorDef = handler.handle( uri );
		final Context context = ( Context ) t;
		processorDef.createProcessor().process( context );
		return null;
	}

}
