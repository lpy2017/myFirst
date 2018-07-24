/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.dispatcher.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

/**
 * OperationDefinition.java
 * 
 * @author liubingj
 */
@XmlRootElement(name = "operation")
public class OperationDefinition extends AbstractDefinition {
	@XmlAttribute
	private String name;
	@XmlAttribute(name = "class")
	private String classPath;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

}
