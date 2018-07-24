package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class AuditEntity {
	
	public long id;
	public String userId;
	public String resourceType;
	public String resourceName;
	public String operateType;
	public int operateResult;
	public Date operateTime;
	public String detail;
	
	public AuditEntity(){
	}
	
	public AuditEntity(String userId, String resourceType,String resourceName, 
			String operateType, int operateResult,String detail) {
		super();
		this.userId = userId;
		this.resourceType = resourceType;
		this.resourceName = resourceName;
		this.operateType = operateType;
		this.operateResult = operateResult;
		this.detail = detail;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public int getOperateResult() {
		return operateResult;
	}
	public void setOperateResult(int operateResult) {
		this.operateResult = operateResult;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

	
}
