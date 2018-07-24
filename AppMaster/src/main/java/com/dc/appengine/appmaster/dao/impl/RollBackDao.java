package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IRollBackDao;
import com.dc.appengine.appmaster.entity.AppSnapshot;

@Component("rollBackDao")
public class RollBackDao implements IRollBackDao{
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	public AppSnapshot getSnapshotOfApp(String snapId, long appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("snapId", snapId);
		map.put("appId", appId);
		return sqlSessionTemplate.selectOne("rollback.getSnapshotOfApp", map);
	}

	public void updateInstanceById(String sId, String tId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", sId);
		map.put("tId", tId);
		sqlSessionTemplate.update("rollback.updateInstanceById", map);
	}
	
	public Map<String, Object> getInstanceById(String instanceId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("instanceId", instanceId);
		return sqlSessionTemplate.selectOne("rollback.getInstanceById", map);
	}

	public void saveInstanceBySnapshot(Map<String, Object> map) {
		sqlSessionTemplate.insert("rollback.saveInstanceBySnapshot", map);
	}
	
	public List<AppSnapshot> getSnapshotByAppId(long appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		return sqlSessionTemplate.selectList("rollback.getSnapshotByAppId", map);
	}
	
	public void updateSnapshotInfoById(String snapshotInfo, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("snapshotInfo", snapshotInfo);
		map.put("id", id);
		sqlSessionTemplate.update("rollback.updateSnapshotInfoById", map);
	}
}
