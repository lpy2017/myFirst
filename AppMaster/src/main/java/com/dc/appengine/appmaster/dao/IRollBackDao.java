package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.AppSnapshot;

public interface IRollBackDao {

	AppSnapshot getSnapshotOfApp(String snapId, long appId);
	
	void updateInstanceById(String sId, String tId);
	
	Map<String, Object> getInstanceById(String instanceId);
	
	void saveInstanceBySnapshot(Map<String, Object> map);
	
	List<AppSnapshot> getSnapshotByAppId(long appId);
	
	void updateSnapshotInfoById(String snapshotInfo, String id);
	
}
