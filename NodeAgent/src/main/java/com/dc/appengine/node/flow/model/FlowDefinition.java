/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.ProcessorDefinition;
import com.dc.appengine.node.flow.processor.FlowProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * FlowDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "flow" )
public class FlowDefinition extends ProcessorDefinition {
	@XmlElementRef
	private List< AbstractStepDefinition > tos = new ArrayList< AbstractStepDefinition >( 10 );
	@XmlAttribute
	private boolean synchronous;
	@XmlAttribute
	private String name;

	public List< AbstractStepDefinition > getTos() {
		return tos;
	}

	public void setTos( List< AbstractStepDefinition > tos ) {
		this.tos = tos;
	}

	public boolean isSynchronous() {
		return synchronous;
	}

	public void setSynchronous( boolean synchronous ) {
		this.synchronous = synchronous;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.model.ProcessorDefinition#createProcessor()
	 */
	public Processor createProcessor() {
		return new FlowProcessor( this );
	}
	
}
