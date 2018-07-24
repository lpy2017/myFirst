package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.ibatis.type.Alias;

@Alias("instance")
public class Instance implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public final static String STATUS_UNDEPLOYED = "UNDEPLOYED";
	public final static String STATUS_RUNNING = "RUNNING";
	public final static String STATUS_DEPLOYED = "DEPLOYED";
	public final static String MESSAGE_SUCCESS = "success";
	//数据库中的属性
	private String id;
	private String resVersionId;
	private String appId;
	private String nodeId;
	private Date deployTime;
	private String lxcIp;
	private Map<String,String> componentInput;
	private Map<String,String> componentOutput;
	private Map<String,String> componentInputTemp;
	private Map<String,String> componentOutputTemp;
	
	public Instance() {
		super();
	}

	public Instance(String id, String resVersionId, String appId, String nodeId, Date deployTime,
			String lxcIp) {
		super();
		this.id = id;
		this.resVersionId = resVersionId;
		this.appId = appId;
		this.nodeId = nodeId;
		this.deployTime = deployTime;
		this.lxcIp = lxcIp;
	}

	//其他属性
	private String status = STATUS_UNDEPLOYED;
	private String ip;
	private int port;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppVersionId() {
		return resVersionId;
	}

	public void setAppVersionId(String resVersionId) {
		this.resVersionId = resVersionId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Date getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(Date deployTime) {
		this.deployTime = deployTime;
	}

	public String getLxcIp() {
		return lxcIp;
	}

	public void setLxcIp(String lxcIp) {
		this.lxcIp = lxcIp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Map<String, String> getComponentInput() {
		return componentInput;
	}

	public void setComponentInput(Map<String, String> componentInput) {
		this.componentInput = componentInput;
	}
	

	public Map<String, String> getComponentOutput() {
		return componentOutput;
	}

	public void setComponentOutput(Map<String, String> componentOutput) {
		this.componentOutput = componentOutput;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id="+this.id+"-,appid="+this.appId+"-,IP="+this.ip;
	}

	public Map<String, String> getComponentInputTemp() {
		return componentInputTemp;
	}

	public void setComponentInputTemp(Map<String, String> componentInputTemp) {
		this.componentInputTemp = componentInputTemp;
	}

	public Map<String, String> getComponentOutputTemp() {
		return componentOutputTemp;
	}

	public void setComponentOutputTemp(Map<String, String> componentOutputTemp) {
		this.componentOutputTemp = componentOutputTemp;
	}
	
}
