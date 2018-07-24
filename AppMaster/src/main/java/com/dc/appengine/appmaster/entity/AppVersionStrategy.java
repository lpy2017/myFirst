package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.Date;
/*
 * 应用版本策略映射关系对象
 */

import org.apache.ibatis.type.Alias;

@Alias("appVersionStrategy")
public class AppVersionStrategy implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Long versionId;
	private Long strategyId;
	private Long appId;
	private Boolean isCurrentVersion;
	private Date createdTime;
	private Long extendStrategyId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersionId() {
		return versionId;
	}
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	public Long getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(Long strategyId) {
		this.strategyId = strategyId;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Boolean getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(Boolean isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Long getExtendStrategyId() {
		return extendStrategyId;
	}
	public void setExtendStrategyId(Long extendStrategyId) {
		this.extendStrategyId = extendStrategyId;
	}
}
