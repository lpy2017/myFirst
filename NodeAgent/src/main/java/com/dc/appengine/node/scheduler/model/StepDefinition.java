package com.dc.appengine.node.scheduler.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.AbstractDefinition;

@XmlRootElement( name = "step" )
public class StepDefinition extends AbstractDefinition {
	@XmlAttribute( name = "class" )
	private String clazz;
	@XmlAttribute
	private String method;
	@XmlAttribute
	private boolean singleton;

	public String getClazz() {
		return clazz;
	}

	public void setClazz( String clazz ) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod( String method ) {
		this.method = method;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton( boolean singleton ) {
		this.singleton = singleton;
	}
	
}
