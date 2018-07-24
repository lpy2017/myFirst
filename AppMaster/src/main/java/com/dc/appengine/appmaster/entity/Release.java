package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class Release {
	
	private String id;
	private String releaseName;
	private String description;
	private Date updateTime;
	private String lifecycleId;
	private String lifecycleName;
	private Integer userId;
	private Integer appCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getLifecycleId() {
		return lifecycleId;
	}
	public void setLifecycleId(String lifecycleId) {
		this.lifecycleId = lifecycleId;
	}
	public String getLifecycleName() {
		return lifecycleName;
	}
	public void setLifecycleName(String lifecycleName) {
		this.lifecycleName = lifecycleName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getAppCount() {
		return appCount;
	}
	public void setAppCount(Integer appCount) {
		this.appCount = appCount;
	}
	
	
}
