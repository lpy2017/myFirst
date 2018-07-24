package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class Version {
	private String id;
	private String resourceId;
	private String resourceName;
	private String resourcePath;
	private String versionName;
	private String versionDesc;
	private String status;
	private Date updateTime;
	private int deployTimeout;
	private int startTimeout;
	private int stopTimeout;
	private int destroyTimeout;
	private int registryId;
	private String md5;
	
	public String ftpLocation;
	public String url;
	public String input;
	public String output;
	public int type;
	public boolean isftpLegal;
	public int versionNum;

	public boolean isIsftpLegal() {
		return isftpLegal;
	}
	public void setIsftpLegal(boolean isftpLegal) {
		this.isftpLegal = isftpLegal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getFtpLocation() {
		return ftpLocation;
	}
	public void setFtpLocation(String ftpLocation) {
		this.ftpLocation = ftpLocation;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourcePath() {
		return resourcePath;
	}
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public String getVersionDesc() {
		return versionDesc;
	}
	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getDeployTimeout() {
		return deployTimeout;
	}
	public void setDeployTimeout(int deployTimeout) {
		this.deployTimeout = deployTimeout;
	}
	public int getStartTimeout() {
		return startTimeout;
	}
	public void setStartTimeout(int startTimeout) {
		this.startTimeout = startTimeout;
	}
	public int getStopTimeout() {
		return stopTimeout;
	}
	public void setStopTimeout(int stopTimeout) {
		this.stopTimeout = stopTimeout;
	}
	public int getDestroyTimeout() {
		return destroyTimeout;
	}
	public void setDestroyTimeout(int destroyTimeout) {
		this.destroyTimeout = destroyTimeout;
	}
	public int getRegistryId() {
		return registryId;
	}
	public void setRegistryId(int registryId) {
		this.registryId = registryId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public Version(){
		
	}
	
	
	public int getVersionNum() {
		return versionNum;
	}
	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}
	public Version(String id, String resourceId, String resourcePath, String versionName, String versionDesc,
			Date updateTime, int deployTimeout, int startTimeout, int stopTimeout, int destroyTimeout,
			int registryId,String md5,int type,String input,String output) {
		super();
		this.id = id;
		this.resourceId = resourceId;
		this.resourcePath = resourcePath;
		this.versionName = versionName;
		this.versionDesc = versionDesc;
		this.updateTime = updateTime;
		this.deployTimeout = deployTimeout;
		this.startTimeout = startTimeout;
		this.stopTimeout = stopTimeout;
		this.destroyTimeout = destroyTimeout;
		this.registryId = registryId;
		this.md5 = md5;
		this.type = type;
		this.input = input;
		this.output = output;
	}
	public Version(String id, String resourceId, String resourcePath, String versionName, String versionDesc,
			Date updateTime, int deployTimeout, int startTimeout, int stopTimeout, int destroyTimeout,
			int registryId,String md5) {
		super();
		this.id = id;
		this.resourceId = resourceId;
		this.resourcePath = resourcePath;
		this.versionName = versionName;
		this.versionDesc = versionDesc;
		this.updateTime = updateTime;
		this.deployTimeout = deployTimeout;
		this.startTimeout = startTimeout;
		this.stopTimeout = stopTimeout;
		this.destroyTimeout = destroyTimeout;
		this.registryId = registryId;
		this.md5 = md5;
	}
	
	
}
