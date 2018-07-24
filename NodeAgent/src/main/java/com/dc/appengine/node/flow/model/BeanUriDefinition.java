/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * AbstractBeanUriDefinition.java
 * @author liubingj
 */
public class BeanUriDefinition extends BeanDefinition {
	@XmlAttribute
	protected String uri;

	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

}
