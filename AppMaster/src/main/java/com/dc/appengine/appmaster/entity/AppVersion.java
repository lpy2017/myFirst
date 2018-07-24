package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("appVersion")
public class AppVersion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long appId;
	private String versionName;
	private long deployId;
	private long startId;
	private long stopId;
	private long destroyId;
	private String resversionId;
	private String clusterId;
	private int currentVersion;
	
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public long getDeployId() {
		return deployId;
	}
	public void setDeployId(long deployId) {
		this.deployId = deployId;
	}
	public long getStartId() {
		return startId;
	}
	public void setStartId(long startId) {
		this.startId = startId;
	}
	public long getStopId() {
		return stopId;
	}
	public void setStopId(long stopId) {
		this.stopId = stopId;
	}
	public long getDestroyId() {
		return destroyId;
	}
	public void setDestroyId(long destroyId) {
		this.destroyId = destroyId;
	}
	public String getResversionId() {
		return resversionId;
	}
	public void setResversionId(String resversionId) {
		this.resversionId = resversionId;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public int getCurrentVersion() {
		return currentVersion;
	}
	public void setCurrentVersion(int currentVersion) {
		this.currentVersion = currentVersion;
	}
	
	

}