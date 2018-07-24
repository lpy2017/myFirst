package com.dc.appengine.appmaster.service;

public interface IReleaseManageService {
	public String saveRelease(String name,String description,String lifecycleId,long userId);
	public String listReleases(String releaseName,String lifecycleName,String description,int pageSize,int pageNum,String sortName,String sortOrder);
	public String deleteRelease(String id);
	public String updateRelease(String id,String name,String description,String lifecycleId);
	public String checkReleaseName(String releaseName,String id);
	public String listLifecycles(long userId);
	public String addReleaseApps(String releaseId, String resourceIds);
	public String getReleaseDetail(String releaseId);
	public String updateReleaseEnvFlow(String releaseName,String blueprintInsId, String flowId);
	public String deleteReleaseApp(String releaseId, String resourceId);
	public String listReleaseApps(String releaseId);
	public String getReleaseStageEnvs(String releaseName,String resourceName);
	public String saveReleaseStageEnv(String releaseName,String resourceName,String blueprintInsName, String phaseId, String blueprintId, String cdFlowId,
			String resPoolConfig,String userId);
	public String relieveReleaseStageEnv(String releaseName, String resourceName, String phaseId);
	public String deleteReleaseStage(String phaseId);
	public String addReleaseStage(String stageId,String lifecycleId);
	
}
