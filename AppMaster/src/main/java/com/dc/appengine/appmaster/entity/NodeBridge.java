package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.List;

public class NodeBridge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long nodeId;
	private String bridgeIp;
	private List<ContainerIp> containerIpList;
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
	/**
	 * @return the nodeId
	 */
	public long getNodeId() {
		return nodeId;
	}
	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}
	/**
	 * @return the bridgeIp
	 */
	public String getBridgeIp() {
		return bridgeIp;
	}
	/**
	 * @param bridgeIp the bridgeIp to set
	 */
	public void setBridgeIp(String bridgeIp) {
		this.bridgeIp = bridgeIp;
	}
	/**
	 * @return the containerIpList
	 */
	public List<ContainerIp> getContainerIpList() {
		return containerIpList;
	}
	/**
	 * @param containerIpList the containerIpList to set
	 */
	public void setContainerIpList(List<ContainerIp> containerIpList) {
		this.containerIpList = containerIpList;
	}
	
}
