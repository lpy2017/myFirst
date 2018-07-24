/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

/**
 * PropertyDefinition.java
 * 
 * @author liubingj
 */
@XmlRootElement( name = "property" )
public class PropertyDefinition extends AbstractDefinition {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String value;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

}
