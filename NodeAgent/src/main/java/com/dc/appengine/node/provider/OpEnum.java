/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.provider;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum OpEnum {
	@XmlEnumValue(value = "services_catalog")
	SERVICES_CATALOG,

	@XmlEnumValue(value = "salt_stack")
	SALT_STACK,

	@XmlEnumValue(value = "command")
	COMMAND,

	@XmlEnumValue(value = "heart_beat")
	HEART_BEAT, 
	
	@XmlEnumValue(value = "log4j_update")
	LOG4J_UPDATE,
	
	@XmlEnumValue(value = "portmap_delete")
	PORTMAP_DELETE,
	
	@XmlEnumValue(value = "download")
	DOWNLOAD
	
}
