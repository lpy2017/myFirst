/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

/**
 * AbstractDefinition.java
 * @author liubingj
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class AbstractDefinition {
	@XmlValue
	protected String value;

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

}
