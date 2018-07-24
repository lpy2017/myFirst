package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;

public interface IInstanceDao {
	
	/**
	 * 按照instance唯一标识进行查询
	 * @param instanceId 
	 * @return instance
	 */
	public Map<String,Object> findByInstanceId(String instanceId);

	public int countInstancesByAppId(long appId);

	public List<Map<String,String>> listInstances(Page page);

	public int delInstance(String instanceId);
	
	void saveInstance(Instance instance);

	public Map<String, Object> findAppByInstanceId(String instanceId);

	public boolean isExitInstance(String instanceId);
	
	List<Map<String,Object>> listInstancesByAppId(long appId);
	
	public void updateInstance(Map<String,Object> map);

	public List<Map<String, Object>> getInstanceList(long id);

	public List<Map<String,Object>> getAppList(int blueInstanceId);
	
	public String findversionId(String instanceId);
	
	public String findresourceVersionId(String instanceId);
	
	public String findNodeIP(String instanceId);
	
	public Map<String, Object> findMessage(String instanceId);

	public String findComponentInput(String blueId, String bluekey, String instanceId1);

	public int countVersionByAppId(long appId);

	public List<Map<String, String>> listVersions(Page page);

	public int countInstancesByVersion(String versionId,long appId);

	public String getConfigInput(String configId,long appId);

	public int updateComponentInputTemp(String newInputTemp,String resId,long appId);

	public List<Map<String, Object>> getInstanceListByResVerionId(long appId,
			String oldResVersionId);

	public void updateInstanceResVersionId(String resVersionId, String id);

	public void updateInstanceAll(Instance instance);

	public List<Map<String, Object>> getInstances(long appId, String resourceVersionId, String nodeId);

	public String getVersionName(String resVersionId);

	public String findresVerId(String componentName, String version);
	
	public List<Map<String, Object>> findInstances(String instanceId, String componentName, String resourceVersionId);

	public List<Map<String, Object>> findInstancesBybp(String blueInstanceName, String componentName,
			String resourceVersionId);

	public Map<String, Object> getInstanceBuInsId(String snapInsId);

	public Map<String, Object> getInstanceDetailById(String instanceId);

	public void updateInstanceStatus(Map<String, Object> param);

	public void updateBlueprintComponentInstanceParams(Map<String, Object> param);
	
}
