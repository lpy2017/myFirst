package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.ITemplateDao;
import com.dc.appengine.appmaster.entity.Page;

@Component("templateDao")
public class TemplateDao implements ITemplateDao{
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<Map<String, Object>> getDeployedTemplate(Page page) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueinstance.listBlueInstance", page);
	}

	@Override
	public int countDeployedTemNum(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.countBlueInstanceNumOfUser", m);
	}

	@Override
	public String getClusterNameById(int blueInstanceId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.getClusterName", blueInstanceId);
	}

	@Override
	public String getUpdateTime(int blueInstanceId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.getUpdateTime", blueInstanceId);
	}
	public int getAppNumByBlueInstanceId(int blueInstanceId){
		return sqlSessionTemplate.selectOne("blueinstance.getAppNumByBlueInstanceId",blueInstanceId);
	}
	
	public Map<String, Object> getBlueInstanceDetail(int blueInstanceId){
		return sqlSessionTemplate.selectOne("blueinstance.getBlueInstanceDetail", blueInstanceId);
	}

	@Override
	public void delBlueInstance(int blueInstanceId) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("blueinstance.delBlueInstance", blueInstanceId);
	}
	
	@Override
	public void delBlueInstanceType(int blueprint_instance_id) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("blueinstance.delBlueInstanceType", blueprint_instance_id);
		
	}

	@Override
	public void saveSnapShot(Map<String, Object> param) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("blueinstance.saveSnapShot",param);
	}

	@Override
	public int countSnapshotNum(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.countSnapshotNum", m);
	}

	@Override
	public List<Map<String, Object>> listSnapshots(Page page) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("blueinstance.listSnapshots", page);
	}
	
	public void deleteSnap(String snapId){
		sqlSessionTemplate.delete("blueinstance.deleteSnapshot",snapId);
	}

	@Override
	public Map<String, Object> getSnapshot(String snapId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.getSnapshot", snapId);
	}

	@Override
	public String getNodeIpByNodeId(String nodeId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.getNodeIpByNodeId",nodeId);
	}

	@Override
	public String getBluePrintId(String snapId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueinstance.getBluePrintId",snapId);
	}

	@Override
	public void saveSnapShotOfBlueInstance(Map<String, Object> param) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("blueinstance.saveSnapShotOfBlueInstance",param);
	}

	@Override
	public int findSnapshotOfAppByName(String snapshotName,int appId) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("snapshotName", snapshotName);
		map.put("appId", appId);
		return sqlSessionTemplate.selectOne("blueinstance.findSnapshotOfAppByName",map);
	}

	@Override
	public Map<String, Object> getComponentSnapshot(String snapshotId) {
		return sqlSessionTemplate.selectOne("blueinstance.getComponentSnapshot",snapshotId);
	}

	@Override
	public List<Map<String, Object>> getSnapShotInfoByBlueInstanceId(int blueprintInstanceId) {
		return sqlSessionTemplate.selectList("blueinstance.getSnapShotInfoByBlueInstanceId",blueprintInstanceId);
	}
	
	@Override
	public void deleteSnapShotByBlueInstanceId(int blueprintInstanceId) {
		sqlSessionTemplate.delete("blueinstance.deleteSnapShotByBlueInstanceId",blueprintInstanceId);
	}
	
	@Override
	public void deleteAppSnapShotByBlueInstanceId(int blueprintInstanceId) {
		sqlSessionTemplate.delete("blueinstance.deleteAppSnapShotByBlueInstanceId",blueprintInstanceId);
	}

}
