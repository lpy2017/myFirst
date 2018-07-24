/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

import com.dc.appengine.node.configuration.model.ProcessorDefinition;

/**
 * 
 * @author liubingj
 */
public abstract class AbstractProcessor< T extends ProcessorDefinition > implements Processor {

	protected T definition;
	
	public AbstractProcessor( T definition ) {
		this.definition = definition;
	}

	public T getDefinition() {
		return definition;
	}

}
