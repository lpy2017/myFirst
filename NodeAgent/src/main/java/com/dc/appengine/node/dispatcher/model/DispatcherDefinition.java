/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.dispatcher.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.ProcessorDefinition;
import com.dc.appengine.node.dispatcher.DispatcherProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * DispatchDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "dispatcher" )
public class DispatcherDefinition extends ProcessorDefinition {
	@XmlElement( name = "operation" )
	private List< OperationDefinition > operations = new ArrayList< OperationDefinition >( 10 );

	public List< OperationDefinition > getOperations() {
		return operations;
	}

	public void setOperations( List< OperationDefinition > operations ) {
		this.operations = operations;
	}
	
	public Processor createProcessor() {
		return new DispatcherProcessor( this );
	}

}
