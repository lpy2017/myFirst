package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("appSnapshot")
public class AppSnapshot implements Serializable {
	private static final long serialVersionUID = 1L;

	private String snapshotId;
	private String snapshotName;
	private long appId;
	private String appName;
	private String snapshotInfo;
	private long blueInstanceId;
	private long appKey;
	private String updateTime;
	private long userId;

	private Application app;
	private List<Instance> instances;
	
	public String getSnapshotId() {
		return snapshotId;
	}
	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}
	public String getSnapshotName() {
		return snapshotName;
	}
	public void setSnapshotName(String snapshotName) {
		this.snapshotName = snapshotName;
	}
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getSnapshotInfo() {
		return snapshotInfo;
	}
	public void setSnapshotInfo(String snapshotInfo) {
		this.snapshotInfo = snapshotInfo;
	}
	public long getBlueInstanceId() {
		return blueInstanceId;
	}
	public void setBlueInstanceId(long blueInstanceId) {
		this.blueInstanceId = blueInstanceId;
	}
	public long getAppKey() {
		return appKey;
	}
	public void setAppKey(long appKey) {
		this.appKey = appKey;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public Application getApp() {
		return app;
	}
	public void setApp(Application app) {
		this.app = app;
	}
	public List<Instance> getInstances() {
		return instances;
	}
	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

}
