/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.processor;

import com.dc.appengine.node.flow.model.AbstractStepDefinition;
import com.dc.appengine.node.flow.model.FlowDefinition;
import com.dc.appengine.node.preloader.AbstractProcessor;

/**
 * FlowProcessor.java
 * @author liubingj
 */
public class FlowProcessor extends AbstractProcessor< FlowDefinition > {

	/**
	 * @param definition
	 */
	public FlowProcessor( FlowDefinition definition ) {
		super( definition );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.processor.Processor#process(java.lang.Object)
	 */
	public < T, R > R process( T t ) throws Exception {
		for ( AbstractStepDefinition to : getDefinition().getTos() ) {
			to.createProcessor().process( t );
		}
		return null;
	}

}
