package com.dc.appengine.appmaster.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.IResourceDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Resource;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.entity.VersionFtl;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.Utils;

@Component("resourceDao")
public class ResourceDao extends PageQuery implements IResourceDao{

	@Override
	public void saveResource(com.dc.appengine.appmaster.entity.Resource resource) {
		sqlSessionTemplate.insert("resource.saveResource", resource);
	}

	@Override
	public void saveVersion(Version version) {
		sqlSessionTemplate.insert("resource.saveVersion", version);
	}
	@Override
	public void saveVersionFtl(VersionFtl versionFtl) {
		sqlSessionTemplate.insert("resource.saveVersionFtl", versionFtl);
	}

	@Override
	public boolean updateResourceVersion(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int result = sqlSessionTemplate.update("resource.updateVersionStatus", param);
		if(result>0){
			return true;
		}
		return false;
	}
	@Override
	public boolean updateVersion(Map<String, Object> param) {
		// TODO Auto-generated method stub
		int result = sqlSessionTemplate.update("resource.updateVersion", param);
		if(result>0){
			return true;
		}
		return false;
	}

	@Override
	public String getResourceVersionStatus(String resourceVersionId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("resource.getVsersionStatus", resourceVersionId);
	}

	@Override
	public Page listResource(Map<String,Object> condition,int pageNum,int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "resource.listResource");
	}
	
	@Override
	public List<Version> listResourceVersion(String resourceId) {
		return sqlSessionTemplate.selectList("resource.listResourceVersion", MessageHelper.wrapMessage("resourceId",resourceId));
	}

	@Override
	public void deleteResourceVersion(String resourceId, String id) {
		sqlSessionTemplate.delete("resource.deleteResourceVersion",Utils.newMap("resourceId",resourceId,"id",id));
	}

	@Override
	public void deleteResource(String id) {
		sqlSessionTemplate.delete("resource.deleteResource",id);
	}

	@Override
	public Version getResourceVersion(String id) {
		return sqlSessionTemplate.selectOne("resource.getResourceVersion",id);
	}
	@Override
	public Page listResourceVersionByPage(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "resource.listResourceVersion");
	}

	public int findResourceByName(String resourceName) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("resource.findResourceByName", resourceName);
	}
	@Override
	public String getResourceIdByName(String name) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("resource.findResourceIdByName", name);
	}

	@Override
	public void saveVersionFlow(Map<String, Object> params) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("resource.saveVersionFlow", params);
	}

	@Override
	public void deleteVersionFlow(String versionId) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("resource.deleteVersionFlow", versionId);
	}

	@Override
	public List<Map<String, Object>> getAllFlowByVersionId(String versionId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("resource.getAllFlowByVersionId", versionId);
	}

	@Override
	public void updateVersionFlow(Map<String, Object> params) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("resource.updateVersionFlow",params);
	}
	
	@Override
	public String getAppSubflowInfo(String appSubflowId){
		return sqlSessionTemplate.selectOne("resource.getAppSubflowInfo", appSubflowId);
	}
	
	@Override
	public String getFlowByVersionIdAndFlowType(Map<String, String> params) {
		return sqlSessionTemplate.selectOne("resource.getFlowByVersionIdAndFlowType", params);
	}
	
	@Override
	public boolean saveScriptResource(Map<String,Object> params) {
		// TODO Auto-generated method stub
		int count =  sqlSessionTemplate.insert("resource.saveScriptResource", params);
		if(count ==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteScriptResource(String id) {
		// TODO Auto-generated method stub
		int count =  sqlSessionTemplate.delete("resource.deleteScriptResource", id);
		if(count ==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Page listScriptResource(Map<String, Object> condition, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return pageQuery(condition, pageNum, pageSize, "resource.listScriptResource");
	}

	@Override
	public Map<String, Object> findScriptById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("resource.findScriptById", id);
	}
	
	@Override
	public Map<String, Object> getResourceDetail(String resourceName) {
		return sqlSessionTemplate.selectOne("resource.getResourceDetail", resourceName);
	}

	public List<VersionFtl> getVersionFtl(String resourceVersionId) {
		return sqlSessionTemplate.selectList("resource.getVersionFtl",resourceVersionId);
	}

	public boolean updateVersionFtl(Map<String, Object> map) {
		int result = sqlSessionTemplate.update("resource.updateVersionFtl", map);
		if(result>0){
			return true;
		}
		return false;
	}
	
	@Override
	public int findResourceFlowByTypeAndName(Map<String, Object> map) {
		return sqlSessionTemplate.selectOne("resource.findResourceFlowByTypeAndName", map);
	}
	
	@Override
	public void saveNewResourceFlow(Map<String, Object> params) {
		if(null == params.get("id")){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			params.put("id", format.format(new Date()));
		}
		sqlSessionTemplate.insert("resource.saveNewResourceFlow", params);
	}
	
	@Override
	public List<Map<String, Object>> getAllFlowByResourceId(String resourceId) {
		return sqlSessionTemplate.selectList("resource.getAllFlowByResourceId", resourceId);
	}
	
	@Override
	public String getNewFlowInfoByFlowId(String cdFlowId){
		return sqlSessionTemplate.selectOne("resource.getNewFlowInfoByFlowId", cdFlowId);
	}
	
	@Override
	public Map<String, Object> getNewFlowDetailByFlowId(String cdFlowId) {
		return sqlSessionTemplate.selectOne("resource.getNewFlowDetailByFlowId", cdFlowId);
	}
	
	@Override
	public void updateNewFlowInfoByFlowId(Map<String, Object> map) {
		sqlSessionTemplate.update("resource.updateNewFlowInfoByFlowId", map);
	}
	
	@Override
	public void updateNewFlowDetailById(Map<String, Object> map) {
		sqlSessionTemplate.update("resource.updateNewFlowInfoByFlowId", map);
	}
	
	@Override
	public void deleteNewResourceFlow(String cdFlowId) {
		sqlSessionTemplate.delete("resource.deleteNewResourceFlow", cdFlowId);
	}
	
	@Override
	public void deleteNewVersionFtlByVersionId(String versionId) {
		sqlSessionTemplate.delete("resource.deleteNewVersionFtl",versionId);
	}
	
	@Override
	public void deleteNewResourceFlowByResourceId(String resourceId) {
		sqlSessionTemplate.delete("resource.deleteNewResourceFlowByResourceId",resourceId);
	}

	public void deleteVersionFtl(String id) {
		sqlSessionTemplate.delete("resource.deleteVersionFtl",id);		
	}

	@Override
	public String getVersionName(String resourceVersionId) {
		return sqlSessionTemplate.selectOne("resource.getVersionName", resourceVersionId);
		
	}
	@Override
	public List<Map<String, Object>> exportResources(List<String> tempIds) {
		return sqlSessionTemplate.selectList("resource.exportResource", tempIds);
	}

	public List<Map<String, Object>> getResourecVersion(String resourceId) {
		return sqlSessionTemplate.selectList("resource.exportVersion", resourceId);
	}

	public List<Map<String, Object>> getResourecFtl(String versionId) {
		return sqlSessionTemplate.selectList("resource.exportFtl", versionId);
	}

	public List<Map<String, Object>> getResourecFlow(String resourceId) {
		return sqlSessionTemplate.selectList("resource.exportFlow", resourceId);
	}
	public void importResource(Map<String, Object> map) {
		sqlSessionTemplate.insert("resource.importResource", map);
	}
	public void importVersion(Map<String, Object> map) {
		sqlSessionTemplate.insert("resource.importVersion", map);
	}
	public void importFtl(Map<String, Object> map) {
		sqlSessionTemplate.insert("resource.importFtl", map);
	}
	
	public List<Map<String, Object>> getsubFlowInfo(Map<String, Object> map) {
		return sqlSessionTemplate.selectList("resource.getsubFlowInfo", map);
	}
	public Map<String, Object> findScriptResource(Map<String, Object> map) {
		return sqlSessionTemplate.selectOne("resource.findScriptResource", map);
	}

	public List<Map<String, String>> getAllResourceFlow() {
		return sqlSessionTemplate.selectList("resource.getAllResourceFlow");
	}
	
	public Map<String, String> getResourceByFtlId(String id) {
		return sqlSessionTemplate.selectOne("resource.getResourceByFtlId", id);
	}

	@Override
	public JSONObject saveAudit(JSONObject params) {
		JSONObject result = new JSONObject();
		int insert = sqlSessionTemplate.insert("resource.saveAudit", params);
		if(insert>0){
			result.put("result", true);
			result.put("message", "添加信息成功！");
		}else{
			result.put("result", false);
			result.put("message", "添加信息失败！");
		}
		return result;
	}

	@Override
	public Map getResourceInfo(String versionId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("resource.getResourceInfo", versionId);
	}

	@Override
	public boolean updateResourceInfo(Map params) {
		// TODO Auto-generated method stub
		int result= sqlSessionTemplate.update("resource.updateResourceInfo", params);
		if(result>0){
			return true;
		}
		return false;
	}

	public void bindComponentReleaseFlow(Map<String, Object> params) {
		sqlSessionTemplate.update("resource.bindComponentReleaseFlow", params);
	}
	
	public void bindComponentRollbackFlow(Map<String, Object> params) {
		sqlSessionTemplate.update("resource.bindComponentRollbackFlow", params);
	}

	public Map<String, Object> getResourceDetailByVersionId(String versionId) {
		return sqlSessionTemplate.selectOne("resource.getResourceDetailByVersionId", versionId);
	}
	
	@Override
	public Map<String, Object> getVersionDetailByVersionId(String versionId) {
		return sqlSessionTemplate.selectOne("resource.getVersionDetailByVersionId", versionId);
	}

	public Map<String, Object> getResourceDetailById(String resourceId) {
		return sqlSessionTemplate.selectOne("resource.getResourceDetailById", resourceId);
	}
	
	public Integer getResourceVersionMaxNum(String resourceId) {
		return sqlSessionTemplate.selectOne("resource.getResourceVersionMaxNum", resourceId);
	}
	
	public void updateAudit(JSONObject params) {
		sqlSessionTemplate.update("resource.updateAudit", params);
	}
	
	@Override
	public List<Map<String, Object>> getVersionDetailByVersionIdS(List ids) {
		return sqlSessionTemplate.selectList("resource.getVersionDetailByVersionIds", ids);
	}

	public List<String> listResourceVersionStatuses(String resourceId) {
		return sqlSessionTemplate.selectList("resource.listResourceVersionStatuses", resourceId);
	}

	public void bindComponentStartFlow(Map<String, Object> params) {
		sqlSessionTemplate.update("resource.bindComponentStartFlow", params);
	}

	public void bindComponentStopFlow(Map<String, Object> params) {
		sqlSessionTemplate.update("resource.bindComponentStopFlow", params);
	}

	public String getAuditStatusById(String auditId) {
		return sqlSessionTemplate.selectOne("resource.getAuditStatusById", auditId);
	}

	public String getResourceStatus(String resourceId) {
		return sqlSessionTemplate.selectOne("resource.getResourceStatus", resourceId);
	}

	public void updateResourceStatus(Map<String, Object> param) {
		sqlSessionTemplate.update("resource.updateResourceStatus", param);
	}
	public Map getLatestVersionById(String resourceId) {
		return sqlSessionTemplate.selectOne("resource.getLatestVersionById", resourceId);
	}

	public List<Resource> listAllSystems(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("resource.listAllSystems", param);
	}
}
