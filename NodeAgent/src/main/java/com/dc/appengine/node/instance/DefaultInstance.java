/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "default")
public class DefaultInstance extends InstanceModel {
	public DefaultInstance() {
		super();
	}

	public DefaultInstance(String id) {
		super(id);
	}

}
