/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.flow.processor.BeanProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * BeanDefiniton.java
 * @author liubingj
 */
@XmlRootElement( name = "bean" )
public class BeanDefinition extends AbstractStepDefinition {
	@XmlAttribute
	protected String id;
	@XmlAttribute( name = "class" )
	protected String clazz;
	@XmlAttribute
	protected String method;
	@XmlAttribute
	protected boolean singleton = true;
	@XmlElementRef
	protected List< PropertyDefinition > properties = new ArrayList< PropertyDefinition >( 10 );

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public List< PropertyDefinition > getProperties() {
		return properties;
	}

	public void setProperties( List< PropertyDefinition > properties ) {
		this.properties = properties;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz( String clazz ) {
		this.clazz = clazz;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton( boolean singleton ) {
		this.singleton = singleton;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.model.ProcessorDefinition#createProcessor()
	 */
	public Processor createProcessor() {
		return new BeanProcessor( this );
	}

	public String getMethod() {
		return method;
	}

	/**
	 * @param query
	 */
	public void setMethod( String method ) {
		this.method = method;
	}

}
