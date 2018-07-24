package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IReleaseManageDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Component("releaseManageDao")
public class ReleaseManageDao extends PageQuery implements IReleaseManageDao {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;	
	@Override
	public int saveRelease(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("releaseManage.saveRelease", params);
	}

	@Override
	public int deleteReleaseById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("releaseManage.deleteReleaseById", id);
	}

	@Override
	public int updateRelease(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("releaseManage.updateRelease", params);
	}

	@Override
	public Map findReleaseById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("releaseManage.findReleaseById", id);
	}

	@Override
	public Page listReleasesByPage(int pageSize, int pageNum,Map<String, Object> params) {
		// TODO Auto-generated method stub
		return pageQuery(params, pageNum, pageSize,"releaseManage.listReleases");
	}

	@Override
	public int deleteReleaseApps(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("releaseManage.deleteReleaseApps", params);
	}

	@Override
	public Map checkRelease(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("releaseManage.checkRelease", params);
	}

	@Override
	public List listLifecycles(Map<String,Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.listAllLifecycles",params);
	}

	@Override
	public int addReleaseApps(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("releaseManage.addReleaseApps", params);
	}

	@Override
	public List findAppIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findAppIds",params);
	}

	@Override
	public List findPhaseBylifecycleId(String lifecycleId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findPhaseBylifecycleId",lifecycleId);
	}

	@Override
	public int addReleaseStageEnvs(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("releaseManage.addReleaseStageEnvs", params);
	}

	@Override
	public int deleteReleaseStageEnvByStageId(String stageId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("releaseManage.deleteReleaseStageEnvByStageId", stageId);
	}

	@Override
	public List findReleaseStageEnvsById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findReleaseStageEnvsById", id);
	}

	@Override
	public List findReleaseAppsById(String releaseId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findReleaseAppsById", releaseId);
	}

	@Override
	public List findReleaseStageEnvs(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findReleaseStageEnvs", params);
	}

	@Override
	public int updateReleaseEnv(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("releaseManage.updateReleaseEnv", params);
	}

	@Override
	public int deleteReleaseStageEnvs(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("releaseManage.deleteReleaseStageEnvs", params);
	}
	@Override
	public Map findReleaseByName(String releaseName) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("releaseManage.findReleaseByName", releaseName);
	}

	@Override
	public Map findResourceInfo(String resourceName) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("releaseManage.findResourceInfo", resourceName);
	}

	@Override
	public List findNodeIps(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findNodeIps", params);

	}

	@Override
	public int updateReleaseEnvFlow(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("releaseManage.updateReleaseEnvFlow", params);
	}

	@Override
	public List findReleaseIdsBylifecycleId(String lifecycleId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("releaseManage.findReleaseIdsBylifecycleId", lifecycleId);
	}
	
	
}
