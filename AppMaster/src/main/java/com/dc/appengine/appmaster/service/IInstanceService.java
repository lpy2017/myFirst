package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;

public interface IInstanceService {
	/**
	 * 按照instance唯一标识进行查询
	 * @param instanceId
	 * @return
	 */
	public Map<String, Object> findByInstanceId(String instanceId);
	Page listInstances(int pageSize, int pageNum, long appId,String sortName,String sortOrder);
	public String deleteInstance(String instanceId);
	void saveInstance(Instance instance);
	List<Map<String,Object>> listInstancesByAppId(long appId);
	void updateInstance(String instanceId,String status);
	public String updateStatus(String message);
	public String getAppStatus(long id);
	public List<Map<String, Object>> getAppList(int blueInstanceId);
	public String getBlueInstanceStatus(int blueInstanceId);
	public String findversionId(String instanceId);
	public String findresourceVersionId(String instanceId);
	public String findNodeIP(String instanceId);
	public Map<String, Object> findMessage(String instanceId);
	public String findComponentInput(String blueId, String bluekey, String instanceId1);
	public String getAppVersionList(int pageSize, int pageNum, long appId);
	public String getConfigInfoByVersionId(String configId,long appId);
	public String updateComponentInputTemp(String resId, long appId,
			String key, String value);
	public String removeLine(String resId, long appId, String key, String value);
	public String updateResVersionInstance(long appId, String resVersionId, String oldResVersionId);
	public List<Map<String, Object>> getInstanceList(long appId);
	public void updateInstanceAll(Instance instance);
	public List<Map<String, Object>> getInstances(long appId, String resourceVersionId, String nodeId);
	public String findresVerId(String componentName, String version);
	public List<Map<String, Object>> findInstances(String instanceId, String componentName, String resourceVersionId);
	public List<Map<String, Object>> findInstancesBybp(String blueInstanceName, String componentName,
			String resourceVersionId);
	public void delInstanceAndFile(String insId);
	public String getBlueprintComponentInstanceParams(String instanceId) throws Exception;
	public void updateBlueprintComponentInstanceParams(Map<String, Object> param);
	
}
