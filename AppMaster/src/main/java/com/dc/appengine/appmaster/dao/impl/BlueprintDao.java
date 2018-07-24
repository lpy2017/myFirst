package com.dc.appengine.appmaster.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Component("blueprintDao")
public class BlueprintDao extends PageQuery implements IBlueprintDao{

	@Override
	public int saveAllBlueprint(Map<String, String> param) {
		return sqlSessionTemplate.insert("blueprint.saveAllBlueprint",param);
	}

	@Override
	public String getAllBlueprint(String blueprint_instance_name) {
		return sqlSessionTemplate.selectOne("blueprint.getAllBlueprint",blueprint_instance_name);
	}

	@Override
	public int updateAllBlueprint(Map<String, String> param) {
		return sqlSessionTemplate.update("blueprint.updateAllBlueprint",param);
	}


	@Override
	public int saveBluePrintType(BluePrintType bluePrintType) {
		if(null == bluePrintType.getId()){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			bluePrintType.setId(format.format(new Date()));
		}
		return sqlSessionTemplate.insert("blueprint.saveBluePrintType",bluePrintType);
	}

	@Override
	public int getBlueprint_id(String blueprint_name) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprint_id",blueprint_name);
	}

	@Override
	public void delBluePrintType(String blueprint_id) {
		sqlSessionTemplate.delete("blueprint.delBluePrintType",blueprint_id);
	}

	@Override
	public Map<String, Object> getBlueprintInstanceById(int id) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintInstanceById", id);
	}

	@Override
	public Map<String, Object> getBlueprintType(String blueprintId, String flowType) {
		Object o = MessageHelper.wrapMessage("blueprintId",blueprintId,"flowType",flowType);
		return sqlSessionTemplate.selectOne("blueprint.findBlueprintType", o);
	}

	@Override
	public String getDeployBlueprint(String blueprint_id) {
		return sqlSessionTemplate.selectOne("blueprint.getDeployBlueprint",blueprint_id);
	}

	@Override
	public long getBlueprint_userid(int blueprint_id) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprint_userid",blueprint_id);
	}

	@Override
	public HashMap<String, Object> getBlueprint(String blueprint_name) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprint",blueprint_name);
	}
	@Override
	public HashMap<String, Object> getBlueprintInstance(String instance_id) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintInstance",instance_id);
	}

	@Override
	public int saveBlueprintInstance(Map<String, String> param) {
		return sqlSessionTemplate.insert("blueprint.saveBlueprintInstance",param);
	}

	@Override
	public int getbluePrintInstanceId(String blueprint_id, String blueprint_instance_name) {
		Object o = MessageHelper.wrapMessage("blueprint_id",blueprint_id,"blueprint_instance_name",blueprint_instance_name);
		return sqlSessionTemplate.selectOne("blueprint.getbluePrintInstanceId", o);
	}

	@Override
	public long getElementIns(long appId, String nodeId) {
		Object o = MessageHelper.wrapMessage("appId",appId,"nodeId",nodeId);
		
		long a = 0 ;
		try {
			a =  sqlSessionTemplate.selectOne("blueprint.getElementIns", o);

		} catch (java.lang.NullPointerException e) {
			return a;

		}
		return a ;
	}

	@Override
	public Page listBlueprints(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "blueprint.listBlueprints");
	}

	@Override
	public List<Map<String, Object>> listAllBlueprintInstances(Map<String, Object> condition) {
		return sqlSessionTemplate.selectList("blueprint.listAllBlueprintInstances", condition);
	}
	
	@Override
	public Page listBlueprintInstances(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "blueprint.listBlueprintInstances");
	}

	@Override
	public void updateBlueprintInstance(Map<String, Object> info) {
		sqlSessionTemplate.update("blueprint.updateBlueprintInstance", info);
	}

	@Override
	public int getBlueprintInstanceNum(String blueprint_id) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintInstanceNum",blueprint_id);
	}

	@Override
	public long getEmptyFlowId() {
		return sqlSessionTemplate.selectOne("blueprint.getEmptyFlowId");
	}

	@Override
	public int saveBlueprintTemplate(Map<String, String> param) {
		return sqlSessionTemplate.insert("blueprint.saveBlueprintTemplate",param);
	}

	@Override
	public void delBlueprintTemplate(String blueprint_id) {
		sqlSessionTemplate.delete("blueprint.delBlueprintTemplate",blueprint_id);
	}

	@Override
	public List<String> getBlueprints() {
		return sqlSessionTemplate.selectList("blueprint.getBlueprints");
	}

	@Override
	public Map<String, Object> getFlowInfoById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueprint.getFlowInfoById", id);
	}

	@Override
	public int updateBlueprintComponentConfig(Map<String, Object> param) {
		return sqlSessionTemplate.update("blueprint.updateBlueprintComponentConfig",param);
	}

	@Override
	public int updateBpInsKeyConfig(Map<String, Object> param) {
		return sqlSessionTemplate.update("blueprint.updateBpInsKeyConfig",param);
	}
	
	@Override
	public String getResourcePoolConfigsById(String blueprintId) {
		return sqlSessionTemplate.selectOne("blueprint.getResourcePoolConfigsById",blueprintId);
	}
	
	@Override
	public String getResourcePoolConfigsByInstanceId(String blueprintInstanceId) {
		return sqlSessionTemplate.selectOne("blueprint.getResourcePoolConfigsByInstanceId",blueprintInstanceId);
	}
	
	@Override
	public String updateResourcePoolConfig(Map<String, String> param) {
		return sqlSessionTemplate.selectOne("blueprint.updateResourcePoolConfig",param);
	}
	
	@Override
	public List<Map<String, String>> getClusters(int userId) {
		return sqlSessionTemplate.selectList("blueprint.getClusters",userId);
	}
	
	@Override
	public Map<String, Object> getBluerintAppByAppName(Map<String, String> param) {
		return sqlSessionTemplate.selectOne("blueprint.getBluerintAppByAppName",param);
	}
	
	@Override
	public List<Map<String, String>> getBluerintAppInstances(Map<String, String> param) {
		return sqlSessionTemplate.selectList("blueprint.getBluerintAppInstances",param);
	}
	
	@Override
	public HashMap<String, Object> getBlueprintInstanceByInstanceId(String blueprintId) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintInstanceByInstanceId",blueprintId);
	}

	@Override
	public List<Map<String, Object>> getFlowInstanceIdsByMap(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueprint.getFlowInstanceIdsByMap",param);
	}
	
	@Override
	public int saveFlowInstanceId(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("blueprint.saveFlowInstanceId",param);
	}
	
	@Override
	public int deleteFlowInstanceIdById(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("blueprint.deleteFlowInstanceIdById",id);
	}

	@Override
	public List<Map<String, Object>> getBrotherBlueprintInstance(String blueprintId,String bluePrintInsId) {
		Map<String,String> paraMap = new HashMap();
		paraMap.put("blueprintId", blueprintId);
		paraMap.put("bluePrintInsId", bluePrintInsId);
		return sqlSessionTemplate.selectList("blueprint.getBrotherBlueprintInstance", paraMap);
	}

	@Override
	public String getBlueprintInstanceKV(String bluePrintInsId) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintInstanceKV", bluePrintInsId);
	}
	
	@Override
	public Map<String,Object> getLatestFlowInstanceId(Map<String,Object> param) {
		return (Map<String,Object>)sqlSessionTemplate.selectOne("blueprint.getLatestFlowInstanceId",param);
	}
	
	@Override
	public Map<String,Object> getBlueInstanceInfo(Map<String,Object> param) {
		return (Map<String,Object>)sqlSessionTemplate.selectOne("blueprint.getBlueInstanceInfo",param);
	}
	
	@Override
	public int saveSnapshotOfBlueprintInstance(Map<String, Object> param) {
		return sqlSessionTemplate.insert("blueprint.saveSnapshotOfBlueprintInstance",param);
	}
	
	@Override
	public int checkSnapshotNameOfBlueprintInstance(Map<String, Object> param) {
		return sqlSessionTemplate.selectOne("blueprint.checkSnapshotNameOfBlueprintInstance",param);
	}
	
	@Override
	public List<Map<String, Object>> listSnapshotOfBlueprintInstance(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("blueprint.listSnapshotOfBlueprintInstance", param);
	}
	
	@Override
	public Map<String,Object> getSnapshotDetailByBlueprintInstance(Map<String,Object> param) {
		return sqlSessionTemplate.selectOne("blueprint.getSnapshotDetailByBlueprintInstance",param);
	}

	@Override
	public int deleteSnapshotByBlueprintInstanceId(String blueInstanceId) {
		return sqlSessionTemplate.delete("blueprint.deleteSnapshotByBlueprintInstanceId",blueInstanceId);
	}

	@Override
	public int deleteSnapshotByBlueprintInstance(HashMap<String, Object> param) {
		return sqlSessionTemplate.delete("blueprint.deleteSnapshotByBlueprintInstance",param);
	}
	
	@Override
	public Map<String,Object> getSnapshotDetailById(String id) {
		return sqlSessionTemplate.selectOne("blueprint.getSnapshotDetailById",id);
	}

	@Override
	public Map<String, Object> getRcDetailsByBlueprintId(String blueprintId) {
		return sqlSessionTemplate.selectOne("blueprint.getRcDetailsByBlueprintId",blueprintId);
	}
	
	@Override
	public int saveRcOfBlueprintId(HashMap<String, Object> param) {
		return sqlSessionTemplate.insert("blueprint.saveRcOfBlueprintId",param);
	}
	
	@Override
	public int updateRcDetailByBlueprintId(HashMap<String, Object> param) {
		return sqlSessionTemplate.update("blueprint.updateRcDetailByBlueprintId",param);
	}

	@Override
	public int deleteFlowInstanceIds(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("blueprint.deleteFlowInstanceIds", id);
	}

	@Override
	public Page listFlowInstanceIds(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "blueprint.listFlowInstanceIds");
	}
	@Override
	public Map<String, Object> getBlueprintTemplateByInsId(String blueprintId) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintTemplateByInsId",blueprintId);
	}

	@Override
	public Map<String, Object> getFlowRecordByInstanceId(String instanceId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueprint.getFlowRecordByInstanceId",instanceId);
	}

	@Override
	public int updateBlueprintReducePoolConfig(Map<String, String> para) {
		return sqlSessionTemplate.update("blueprint.updateBlueprintReducePoolConfig",para);
	}

	@Override
	public String getBlueprintReducePoolSize(String blueprintInstanceId) {
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintReducePoolSize", blueprintInstanceId);
	}

	@Override
	public List<Map<String, Object>> listBlueprintByNameAndTemplateAndApp(Map<String, Object> params) {
		return sqlSessionTemplate.selectList("blueprint.listBlueprintByNameAndTemplateAndApp", params);
	}

	@Override
	public List<Map<String, Object>> listFlowInstanceIds(Map<String, Object> condition) {
		return sqlSessionTemplate.selectList("blueprint.listFlowInstanceIds", condition);
	}

	@Override
	public List<Map<String, Object>> getFlowInstanceIdsByMapNew(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueprint.getFlowInstanceIdsByMapNew", param);

	}
	@Override
	public List<Map<String, Object>> getFlowInstanceIds(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueprint.getFlowInstanceIds", param);

	}

	@Override
	public Map<String, Object> getBlueprintById(String blueprintId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintById",blueprintId);

	}

	@Override
	public List findBlueInstanceIds(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueprint.findBlueInstanceIds", param);
	}
	
}
