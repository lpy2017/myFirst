package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

public class ContainerIp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id ;
	private String bridgeIp;//网桥ip
	private String containerIp;//容器ip
	private String  appId;//容器ip关联的应用唯一标示符
	private String instanceId;//容器关联的实例唯一标识符
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	private long used;
	
//	public ContainerIp() {
//	}
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	public String getBridgeIp() {
		return bridgeIp;
	}
	public void setBridgeIp(String bridgeIp) {
		this.bridgeIp = bridgeIp;
	}
	/**
	 * @return the containerIp
	 */
	public String getContainerIp() {
		return containerIp;
	}
	/**
	 * @param containerIp the containerIp to set
	 */
	public void setContainerIp(String containerIp) {
		this.containerIp = containerIp;
	}

	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	/**
	 * @return the used
	 */
	public long getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(long used) {
		this.used = used;
	}
}
