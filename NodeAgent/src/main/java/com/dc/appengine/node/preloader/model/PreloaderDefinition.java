/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

/**
 * PreloaderDefinition.java
 * 
 * @author liubingj
 */
@XmlRootElement( name = "preloader" )
public class PreloaderDefinition extends AbstractDefinition implements Comparable< PreloaderDefinition > {
	@XmlAttribute( name = "class" )
	private String classPath;
	@XmlAttribute
	private int priority;

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath( String classPath ) {
		this.classPath = classPath;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority( int priority ) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( PreloaderDefinition definition ) {
		return this.getPriority() - definition.getPriority();
	}

}
