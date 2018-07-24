package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component("rcDao")
public class RCDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	public Object updateAppScalableStatus(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("application.updateAppScalableStatus",map);
	}

	public List<Map<String, Object>> getInstanceOfAppAndVersion(
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("instance.getInstanceOfAppAndVersion",map);
	}

	public List<Map<String,Object>> getAppVersionListByAppId(long appId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("instance.getAppVersionListByAppId",appId);
	}

	public List<Map<String, Object>> getMasterApplications() {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("application.getMasterApplications");
	}

	public List<Map<String, Object>> getMasterInstanceByApplicationId(long appId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("application.getMasterInstanceByApplicationId",appId);
	}

	public Map<String, Object> getInsNumByAppIdAndResId(long appId,
			String versionId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("versionId", versionId);
		return sqlSessionTemplate.selectOne("application.getInsNumByAppIdAndResId",map);
	}

	public void maintainAppInstances(long appId, String resourceVersionId,
			int targetCount) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("versionId", resourceVersionId);
		map.put("targetCount", targetCount);
		sqlSessionTemplate.insert("application.insertRc", map);
	}
	
}
