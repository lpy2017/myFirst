/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

/**
 * FlowDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "flows" )
@XmlAccessorType( XmlAccessType.FIELD )
public class FlowsDefinition extends AbstractDefinition {
	@XmlAttribute
	private int poolSize = 100 ;
	@XmlElementRef
	private List< FlowDefinition > flows = new ArrayList< FlowDefinition >( 10 );

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize( int poolSize ) {
		this.poolSize = poolSize;
	}

	public List< FlowDefinition > getFlows() {
		return flows;
	}

	public void setFlows( List< FlowDefinition > flows ) {
		this.flows = flows;
	}

	public Map< String, FlowDefinition > toMap() {
		Map< String, FlowDefinition > flowMap = new HashMap< String, FlowDefinition >( 16 );
		for ( FlowDefinition flow : flows ) {
			flowMap.put( flow.getName(), flow );
		}
		return flowMap;
	}
	
}
