/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * UriDefinition.java
 * @author liubingj
 */
public abstract class AbstractUriDefinition extends AbstractStepDefinition {
	@XmlAttribute
	protected String uri;

	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

}
