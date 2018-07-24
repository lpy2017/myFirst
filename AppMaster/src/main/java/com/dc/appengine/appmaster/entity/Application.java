package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("application")
public class Application implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;// 组件id 
	private String appName;
	private String appType;
	private String description;
	private long userId;
	private int startId;
	private int stopId;
	private int deployId;
	private int destroyId;
	private String clusterId;
	private long blueInstanceId;


	private String[] userIds;
	private String clusterName;
	private int instancesNumber;
	private String status;
	private String updateTime;
	private String versionid; 
	private long key;
	private boolean rcFlag;

	private String resourceId;
	private String componentName;
	private String currentVersion;
	private String currentInput;
	private String currentOutput;
	private String targetVersion;
	private String targetInput;
	private String targetOutput;
	private String currentVersionName;
	private String targetVersionName;
	
	private int smartFlag;
	
	private String nodeDisplay;
	
	private int executeFlag;


	public String getCurrentVersionName() {
		return currentVersionName;
	}

	public void setCurrentVersionName(String currentVersionName) {
		this.currentVersionName = currentVersionName;
	}

	public String getTargetVersionName() {
		return targetVersionName;
	}

	public void setTargetVersionName(String targetVersionName) {
		this.targetVersionName = targetVersionName;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getCurrentInput() {
		return currentInput;
	}

	public void setCurrentInput(String currentInput) {
		this.currentInput = currentInput;
	}

	public String getCurrentOutput() {
		return currentOutput;
	}

	public void setCurrentOutput(String currentOutput) {
		this.currentOutput = currentOutput;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public String getTargetInput() {
		return targetInput;
	}

	public void setTargetInput(String targetInput) {
		this.targetInput = targetInput;
	}

	public String getTargetOutput() {
		return targetOutput;
	}

	public void setTargetOutput(String targetOutput) {
		this.targetOutput = targetOutput;
	}

	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public void setVersionid(String versionid) {
		this.versionid = versionid;
	}

	public String getVersionid(){
		return versionid;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}


	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public int getInstancesNumber() {
		return instancesNumber;
	}

	public void setInstancesNumber(int instancesNumber) {
		this.instancesNumber = instancesNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String appDescription) {
		this.description = appDescription;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "appName:" + this.appName + " appId:" + this.id;
	}

	public int getStartId() {
		return startId;
	}

	public void setStartId(int startId) {
		this.startId = startId;
	}

	public int getStopId() {
		return stopId;
	}

	public void setStopId(int stopId) {
		this.stopId = stopId;
	}


	public int getDeployId() {
		return deployId;
	}

	public void setDeployId(int deployId) {
		this.deployId = deployId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public int getDestroyId() {
		return destroyId;
	}

	public void setDestroyId(int destroyId) {
		this.destroyId = destroyId;
	}


	public long getBlueInstanceId() {
		return blueInstanceId;
	}

	public void setBlueInstanceId(long blueInstanceId) {
		this.blueInstanceId = blueInstanceId;
	}

	public boolean isRcFlag() {
		return rcFlag;
	}

	public void setRcFlag(boolean rcFlag) {
		this.rcFlag = rcFlag;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public int getSmartFlag() {
		return smartFlag;
	}

	public void setSmartFlag(int smartFlag) {
		this.smartFlag = smartFlag;
	}

	public String getNodeDisplay() {
		return nodeDisplay;
	}

	public void setNodeDisplay(String nodeDisplay) {
		this.nodeDisplay = nodeDisplay;
	}

	public int getExecuteFlag() {
		return executeFlag;
	}

	public void setExecuteFlag(int executeFlag) {
		this.executeFlag = executeFlag;
	}

}
