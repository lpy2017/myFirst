package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;

public interface IReleaseManageDao {
	public int saveRelease(Map<String,Object> params);
	public int deleteReleaseById(String id);
	public int updateRelease(Map<String,Object> params);
	public Map findReleaseById(String id);
	public Page listReleasesByPage(int pageSize, int pageNum,Map<String,Object> params);
	public int deleteReleaseApps(Map<String,Object> params);
	public Map checkRelease(Map<String,Object> params);
	public List listLifecycles(Map<String,Object> params);
	public int addReleaseApps(Map<String, Object> params);
	public List findAppIds(Map<String,Object> params);
	public List findPhaseBylifecycleId(String lifecycleId);
	public int addReleaseStageEnvs(Map<String, Object> params);
	public int deleteReleaseStageEnvByStageId(String stageId);
	public List findReleaseStageEnvsById(String releaseId);
	public List findReleaseAppsById(String releaseId);
	public List findReleaseStageEnvs(Map<String, Object> params);
	public int updateReleaseEnv(Map<String, Object> params);
	public int deleteReleaseStageEnvs(Map<String,Object> params);
	public Map findReleaseByName(String releaseName);
	public Map findResourceInfo(String resourceName);
	public List findNodeIps(Map<String, Object> params);
	public int updateReleaseEnvFlow(Map<String, Object> params);
	public List findReleaseIdsBylifecycleId(String lifecycleId);
}
