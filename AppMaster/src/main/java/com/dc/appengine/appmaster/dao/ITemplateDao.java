package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;

public interface ITemplateDao {

	List<Map<String,Object>> getDeployedTemplate(Page page);

	int countDeployedTemNum(Map<String, Object> m);

	String getClusterNameById(int blueInstanceId);

	String getUpdateTime(int blueInstanceId);

	int getAppNumByBlueInstanceId(int blueInstanceId);

	Map<String, Object> getBlueInstanceDetail(int blueInstanceId);

	void delBlueInstance(int blueInstanceId);

	void saveSnapShot(Map<String, Object> param);

	int countSnapshotNum(Map<String, Object> m);

	List<Map<String,Object>> listSnapshots(Page page);

	void deleteSnap(String snapId);

	Map<String, Object> getSnapshot(String snapId);

	String getNodeIpByNodeId(String object);

	String getBluePrintId(String snapId);

	void saveSnapShotOfBlueInstance(Map<String, Object> param);

	int  findSnapshotOfAppByName(String snapshotName,int appId);
	
	Map<String,Object> getComponentSnapshot(String snapshotId);

	void delBlueInstanceType(int blueprintInstanceId);
	
	List<Map<String, Object>> getSnapShotInfoByBlueInstanceId(int blueprintInstanceId);
	
	void deleteSnapShotByBlueInstanceId(int blueprintInstanceId);
	
	void deleteAppSnapShotByBlueInstanceId(int blueprintInstanceId);
}
