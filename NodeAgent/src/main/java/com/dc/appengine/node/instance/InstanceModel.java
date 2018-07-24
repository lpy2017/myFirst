/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class InstanceModel {
	@XmlElement
	protected String id;
	protected String name;
	protected String instanceId;
	protected String appId;
	protected String containerName;
	protected String containerIp;
	protected boolean dualNic;

	public InstanceModel() {
	}

	public InstanceModel(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getContainerIp() {
		return containerIp;
	}

	public void setContainerIp(String containerIp) {
		this.containerIp = containerIp;
	}

	public boolean isDualNic() {
		return dualNic;
	}

	public void setDualNic(boolean dualNic) {
		this.dualNic = dualNic;
	}

}
