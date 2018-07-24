package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Resource;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.entity.VersionFtl;

public interface IResourceDao {
	void saveResource(Resource resource);

	void saveVersion(Version version);

	Page listResource(Map<String, Object> condition, int pageNum, int pageSize);

	List<Version> listResourceVersion(String resourceId);

	void deleteResourceVersion(String resourceId, String id);
	
	void deleteResource(String id);

	Version getResourceVersion(String id);

	boolean updateResourceVersion(Map<String, Object> param);

	String getResourceVersionStatus(String resourceVersionId);

	Page listResourceVersionByPage(Map<String, Object> condition, int pageNum, int pageSize);
	
	void saveVersionFlow(Map<String,Object> params);
	
	void deleteVersionFlow(String versionId);
	
	List<Map<String,Object>> getAllFlowByVersionId(String versionId);
	
	void updateVersionFlow(Map<String,Object> params);
	
	String getAppSubflowInfo(String appSubflowId);
	
	String getFlowByVersionIdAndFlowType(Map<String, String> params);
	boolean saveScriptResource(Map<String,Object> params);
	boolean deleteScriptResource(String id);
	Page listScriptResource(Map<String, Object> condition, int pageNum, int pageSize);
	Map<String,Object> findScriptById(String id);
	Map<String, Object> getResourceDetail(String resourceName);

	void saveVersionFtl(VersionFtl versionFtl);

	boolean updateVersion(Map<String, Object> param);
	
	int findResourceFlowByTypeAndName(Map<String, Object> map);
	void saveNewResourceFlow(Map<String, Object> params);
	List<Map<String, Object>> getAllFlowByResourceId(String resourceId);
	String getNewFlowInfoByFlowId(String flowId);
	Map<String, Object> getNewFlowDetailByFlowId(String flowId);
	void updateNewFlowInfoByFlowId(Map<String, Object> map);
	void updateNewFlowDetailById(Map<String, Object> map);
	void deleteNewResourceFlow(String flowId);
	void deleteNewVersionFtlByVersionId(String versionId);
	void deleteNewResourceFlowByResourceId(String resourceId);
	String getVersionName(String currentVersion);

	String getResourceIdByName(String name);

	List<Map<String, Object>> exportResources(List<String> tempIds);
	JSONObject saveAudit(JSONObject params);
	Map getResourceInfo(String versionId);
	boolean updateResourceInfo(Map params);

	Map<String, Object> getVersionDetailByVersionId(String versionId);

	List<Map<String, Object>> getVersionDetailByVersionIdS(List ids);
}
