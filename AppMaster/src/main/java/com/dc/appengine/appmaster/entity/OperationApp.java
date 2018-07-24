package com.dc.appengine.appmaster.entity;

import java.util.List;
import java.util.Map;

/**
 * 运维云app实体
 * 
 * @author xuxyc
 *
 */
public class OperationApp {
	private String appId;
	private String appName;
	private String description;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private int accessPort;
	private OperationResource resourceInfo;
	private Map<String, Object> strategyInfo;
	private Map<String, Object> env;
	// deps has the form: {appId: {env1:val1, env2:val2}, ...}
	private List<String> deps;
	private long userId;
	private String versionUUId;
	private boolean fromTemplate;
	private String netModel;
	private long dockerApp;
	private long confVersionId;
	

	public long isDockerApp() {
		return dockerApp;
	}


	public long getDockerApp() {
		return dockerApp;
	}

	public void setDockerApp(long dockerApp) {
		this.dockerApp = dockerApp;
	}

	public String getNetModel() {
		return netModel;
	}

	public void setNetModel(String netModel) {
		this.netModel = netModel;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public OperationResource getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(OperationResource resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public Map<String, Object> getStrategyInfo() {
		return strategyInfo;
	}

	public void setStrategyInfo(Map<String, Object> strategyInfo) {
		this.strategyInfo = strategyInfo;
	}

	public Map<String, Object> getEnv() {
		return env;
	}

	public void setEnv(Map<String, Object> env) {
		this.env = env;
	}

	public List<String> getDeps() {
		return deps;
	}

	public void setDeps(List<String> deps) {
		this.deps = deps;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getAccessPort() {
		return accessPort;
	}

	public void setAccessPort(int accessPort) {
		this.accessPort = accessPort;
	}

	public String getVersionUUId() {
		return versionUUId;
	}

	public void setVersionUUId(String versionUUId) {
		this.versionUUId = versionUUId;
	}

	public boolean isFromTemplate() {
		return fromTemplate;
	}

	public void setFromTemplate(boolean fromTemplate) {
		this.fromTemplate = fromTemplate;
	}

	public long getConfVersionId() {
		return confVersionId;
	}

	public void setConfVersionId(long confVersionId) {
		this.confVersionId = confVersionId;
	}	
}
