package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("releaseLifecycleDAO")
public class ReleaseLifecycleDAO {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int saveLifecycle(Map<String, Object> param) {
		return sqlSessionTemplate.insert("release_lifecycle.saveLifecycle", param);
	}
	
	public int countRelease(String[] lifecycleIds) {
		return sqlSessionTemplate.selectOne("release_lifecycle.countRelease", lifecycleIds);
	}
	
	public int deleteLifecycle(String[] ids) {
		return sqlSessionTemplate.delete("release_lifecycle.deleteLifecycle", ids);
	}
	
	public int updateLifecycle(Map<String, Object> param) {
		return sqlSessionTemplate.update("release_lifecycle.updateLifecycle", param);
	}
	
	public List<Map<String, Object>> lifecycles(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("release_lifecycle.lifecycles", param);
	}
	
	public int saveStage(Map<String, Object> param) {
		return sqlSessionTemplate.insert("release_lifecycle.saveStage", param);
	}
	
	public int deleteStage(String[] ids) {
		return sqlSessionTemplate.delete("release_lifecycle.deleteStage", ids);
	}
	
	public int deleteStageByLifecycleIds(String[] lifecycleIds) {
		return sqlSessionTemplate.delete("release_lifecycle.deleteStageByLifecycleIds", lifecycleIds);
	}
	
	public int updateStage(Map<String, Object> param) {
		return sqlSessionTemplate.update("release_lifecycle.updateStage", param);
	}
	
	public List<Map<String, Object>> stages(String lifecycleId) {
		return sqlSessionTemplate.selectList("release_lifecycle.stages", lifecycleId);
	}
	
	public int sortStage(List<Map<String, Object>> param) {
		return sqlSessionTemplate.update("release_lifecycle.sortStage", param);
	}
	
	public Map<String, Object> getStage(String id) {
		return sqlSessionTemplate.selectOne("release_lifecycle.getStage", id);
	}

}
