package com.dc.appengine.appmaster.service;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Resource;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.entity.VersionFtl;

public interface IResourceService {
	boolean registResource(Resource resource);
	Page listResource(Map<String,Object> condition,int pageNum,int pageSize);
	Page listResourceVersionByPage(Map<String,Object> condition,int pageNum,int pageSize);
	List<Map<String,Object>> listAllResource(String resourceName,String labelId);
	List<Version> listResourceVersion(String resourceId);
	void deleteResourceVersion(String resourceId, String id);
	void deleteResource(String id);
	Map<String,Properties> getInputAndOutput(String resourceVersionId);
	boolean uploadFile(InputStream input,String uuid,String fileName);
	Map<String,Object> getResourceVersionDetail(String resourceVersionId);
	long saveFlow(BluePrint bp,String subFlowType);
	
	boolean upateResourceVersionStatus(String resourceVersionId,String status,String description,String userId);
	String getResourceVersionStatus(String resourceVersionId);
	void saveResourceVersion(Version version);
	public String getResourceIdByName(String name);
	public long getEmptyFlow();
	public Map<String,Long> saveComponentFlows(Map<String,String> map);
	void saveVersionFlow(HashMap<String,Object> map);
	String getResourceVersionFlows(String resourceVersionId);
	boolean updateResourceVersionFlows(String resourceVersionId, String flows);
	void saveVersionFlows(String resourceVersionId, Map<String, Long> saveMap);
	String getAppSubflowVars(String appSubflowId);
	Long saveNewComponentFlow(Map<String, String> map);
	Map<String, List<Map<String, String>>> getNewVersionOperations(String versionId);
	String getFlowInfoByFlowId(String flowId);
	boolean updateFlowInfoByFlowId(String flowId, String flowInfo);
	boolean updateResourceVersion(String versionId,String resourcePath,String md5);
	String saveScriptResource(Map<String,Object> param);
	String deleteScriptResource(String id);
	Page listScriptResource(Map<String, Object> condition, int pageNum, int pageSize);
	Version getResourceVersion(String versionId);
	Map<String, Object> getResourceDetail(String resourceName);
	void saveVersionFtl(VersionFtl versionFtl);
	String getWorkpieceTree(String ftpUrl);
	String getWorkpieceFile(String resourceVersionId, String filePath);
	boolean updateVersion(String resourceVersionId, String input, String output, String resource_path);
	List<VersionFtl> getVersionFtl(String resourceVersionId);
	boolean updateVersionFtl(String id,String ftlText);
	Long addNewResourceFlow(Map<String, Object> map);
	String getNewResourceFlows(String resourceId,String sortName,String sortOrder);
	String getNewFlowInfoByFlowId(String flowId);
	boolean updateNewFlowInfoByFlowId(String flowId, String flowInfo);
	void deleteNewResourceFlow(String flowId);
	void deleteNewResourceVersion(String versionId);
	void deleteNewResource(String resourceId);
	String getNewFlowVarsByFlowId(String flowId);
	Page listNewResourceVersionByPage(Map<String, Object> condition, int pageNum, int pageSize);
	Map<String, Object> getNewResourceVersionDetail(String resourceVersionId);
	void deleteVersionFtl(String id);
	String exportResources(String ids);
	String importResources(String resource, String flow, String version,String ftl,String packages,String zip,String userId);
	List<Map<String, Object>> getsubFlowInfo(Map<String, Object> map);
	boolean judgeWorkpieceUrl(String url);
	Boolean checkResourceName(String name);
	Map<String, List<Map<String, String>>> getCDAllFlows();
	public List<Map<String, Object>> exportResources(List<String> tempIds);
	public Map<String, Object> getNewFlowDetailByFlowId(String cdFlowId);
	public Map<String, String> getResourceByFtlId(String id);
	public int findResourceByName(String resourceName);
	public void importResource(Map<String, Object> map);
	public void importVersion(Map<String, Object> map);
	public void importFtl(Map<String, Object> map);
	public Map<String, Object> findScriptById(String id);
	void zipResourceAndPackage(List<String> idList, File parentFolder,
			String exportPackageId);
	JSONObject saveAudit(JSONObject params);
	void bindComponentFlow(Map<String, Object> param);
	String executeComponentVersionFlow(Map<String, Object> params, Object object);
	void releaseComponentVersionsByBatch(Map<String, Object> params);
	Map<String, Object> getResourceBindDetail(String resourceId);
	void updateAudit(JSONObject params);
	String exportPatches(String ids);
	List<String> listResourceVersionStatuses(String resourceId);
	String getAuditStatusById(String auditId);
	String getResourceStatus(String resourceId);
	String executeNewComponentVersionFlow(Map<String, Object> params);
	String releaseNewComponentVersionsByBatch(Map<String, Object> params);
	String getLatestVersionId(String resourceId);
	void saveResourceVersionByRestApi(String resourceId, String versionName, String versionDesc, String fileName, int type);
	List<Resource> listAllSystems(Map<String, Object> param);
}
