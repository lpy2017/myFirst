/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.flow.processor.ToProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * ToDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "to" )
public class ToDefinition extends AbstractUriDefinition {

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.model.ProcessorDefinition#createProcessor()
	 */
	public Processor createProcessor() {
		return new ToProcessor( this );
	}

}
