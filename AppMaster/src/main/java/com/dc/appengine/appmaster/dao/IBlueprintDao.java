package com.dc.appengine.appmaster.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;

public interface IBlueprintDao {
	int saveAllBlueprint(Map<String, String> param);
	String getAllBlueprint(String blueprint_name);
	int updateAllBlueprint(Map<String, String> param);
	int saveBluePrintType(BluePrintType bluePrintType);
	int getBlueprint_id(String blueprint_name);
	void delBluePrintType(String blueprint_id);
	Map<String,Object> getBlueprintInstanceById(int id);
	Map<String,Object> getBlueprintType(String string,String flowType);
	String getDeployBlueprint(String blueprint_id);	
	long getBlueprint_userid(int blueprint_id);
	HashMap<String, Object> getBlueprint(String blueprint_name);
	int saveBlueprintInstance(Map<String, String> param);
	int getbluePrintInstanceId(String blueprint_id, String blueprint_instance_name);
	long getElementIns(long appId, String nodeId);
	Page listBlueprints(Map<String,Object> condition,int pageNum,int pageSize);
	Page listBlueprintInstances(Map<String,Object> condition,int pageNum,int pageSize);
	void updateBlueprintInstance(Map<String,Object> info);
	int getBlueprintInstanceNum(String blueprint_id);
	long getEmptyFlowId();
	int saveBlueprintTemplate(Map<String, String> param);
	HashMap<String, Object> getBlueprintInstance(String instance_id);
	void delBlueprintTemplate(String blueprint_id);
	public List<Map<String, Object>> listAllBlueprintInstances(Map<String, Object> condition);
	List<String> getBlueprints();
	Map<String,Object> getFlowInfoById(String id);
	int updateBlueprintComponentConfig(Map<String, Object> param);
	int updateBpInsKeyConfig(Map<String, Object> param);
	String getResourcePoolConfigsById(String blueprintId);
	String getResourcePoolConfigsByInstanceId(String blueprintInstanceId);
	String updateResourcePoolConfig(Map<String, String> param);
	List<Map<String, String>> getClusters(int userId);
	Map<String, Object> getBluerintAppByAppName(Map<String, String> param);
	List<Map<String, String>> getBluerintAppInstances(Map<String, String> param);
	HashMap<String, Object> getBlueprintInstanceByInstanceId(String blueprintId);
	public List<Map<String,Object>> getFlowInstanceIdsByMap(Map<String, Object> param);
	int saveFlowInstanceId(Map<String, Object> param);
	int deleteFlowInstanceIdById(String id);
	List<Map<String, Object>> getBrotherBlueprintInstance(String blueprintId,String bluePrintInsId);
	String getBlueprintInstanceKV(String bluePrintInsId);
	Map<String, Object> getLatestFlowInstanceId(Map<String,Object> param);
	Map<String, Object> getBlueInstanceInfo(Map<String, Object> param);
	int saveSnapshotOfBlueprintInstance(Map<String, Object> param);
	int checkSnapshotNameOfBlueprintInstance(Map<String, Object> param);
	List<Map<String, Object>> listSnapshotOfBlueprintInstance(Map<String, Object> param);
	Map<String, Object> getSnapshotDetailByBlueprintInstance(Map<String, Object> param);
	int deleteSnapshotByBlueprintInstanceId(String blueInstanceId);
	int deleteSnapshotByBlueprintInstance(HashMap<String, Object> param);
	Map<String, Object> getSnapshotDetailById(String id);
	Map<String, Object> getRcDetailsByBlueprintId(String blueprintId);
	int saveRcOfBlueprintId(HashMap<String, Object> param);
	int updateRcDetailByBlueprintId(HashMap<String, Object> param);
	int deleteFlowInstanceIds(String id);
	Page listFlowInstanceIds(Map<String,Object> condition,int pageNum,int pageSize);
	Map<String, Object> getBlueprintTemplateByInsId(String blueprintId);
	Map<String, Object> getFlowRecordByInstanceId(String instanceId);
	int updateBlueprintReducePoolConfig(Map<String, String> para);
	String getBlueprintReducePoolSize(String blueprintInstanceId);
	List<Map<String, Object>> listBlueprintByNameAndTemplateAndApp(Map<String, Object> condition);
	List<Map<String, Object>> listFlowInstanceIds(Map<String, Object> condition);
	
	public List<Map<String,Object>> getFlowInstanceIdsByMapNew(Map<String, Object> param);
	public List<Map<String,Object>> getFlowInstanceIds(Map<String, Object> param);
	public Map<String, Object> getBlueprintById(String blueprintId);
	public List findBlueInstanceIds(Map<String, Object> param);

}
