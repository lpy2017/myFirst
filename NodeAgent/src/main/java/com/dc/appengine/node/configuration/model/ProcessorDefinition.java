/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration.model;

import com.dc.appengine.node.preloader.Processor;

/**
 * ProcessorDefinition.java
 * @author liubingj
 */
public abstract class ProcessorDefinition extends AbstractDefinition {

	public abstract Processor createProcessor();

}
