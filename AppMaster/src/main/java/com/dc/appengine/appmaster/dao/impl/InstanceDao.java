package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;

@Component("instanceDao")
public class InstanceDao implements IInstanceDao {

	@Resource
	SqlSessionTemplate SqlSessionTemplate;

	/**
	 * 按照instance唯一标识进行查询
	 * 
	 * @param instanceId
	 * @return instance
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findByInstanceId(String instanceId) {
		return (Map<String, Object>) SqlSessionTemplate.selectOne("instance.findByInstanceId",
				instanceId);
	}

	@Override
	public int countInstancesByAppId(long appId) {
		// TODO Auto-generated method stub
		return (Integer)SqlSessionTemplate.selectOne("instance.countInstancesByAppId", appId);
	}

	@Override
	public List<Map<String,String>> listInstances(Page page) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectList("instance.listInstances", page);
	}

	@Override
	public int delInstance(String instanceId) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.delete("instance.delInstance", instanceId);
	}

	@Override
	public void saveInstance(Instance instance) {
		SqlSessionTemplate.insert("instance.saveInstance", instance);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findAppByInstanceId(String instanceId) {
		// TODO Auto-generated method stub
		return (Map<String, Object>) SqlSessionTemplate.selectOne("instance.findApp", instanceId);
	}

	@Override
	public boolean isExitInstance(String instanceId) {
		// TODO Auto-generated method stub
		int temp = SqlSessionTemplate.selectOne("instance.isExitInstance", instanceId);
		if(temp > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> listInstancesByAppId(long appId) {
		return SqlSessionTemplate.selectList("instance.listInstancesByAppId", appId);
	}

	
	public void updateInstance(Map<String,Object> map){
		SqlSessionTemplate.update("instance.updateInstance", map);
	}

	@Override
	public List<Map<String, Object>> getInstanceList(long id) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectList("instance.findInstanceList", id);
	}
	@Override
	public List<Map<String, Object>> getInstances(long id,String resourceVersionId,String nodeId) {
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("id", id);
		param.put("resourceVersionId", resourceVersionId);
		param.put("nodeId", nodeId);
		return SqlSessionTemplate.selectList("instance.getInstances", param);
	}

	@Override
	public List<Map<String,Object>> getAppList(int blueInstanceId) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectList("instance.findAppList", blueInstanceId);
	}

	@Override
	public String findversionId(String instanceId) {
		// TODO Auto-generated method stub
		 return (String)SqlSessionTemplate.selectOne("instance.findversionId", instanceId);
	}

	@Override
	public String findNodeIP(String instanceId) {
		// TODO Auto-generated method stub
		 return ( String)SqlSessionTemplate.selectOne("instance.findnodeIP", instanceId);
	}

	@Override
	public String findresourceVersionId(String instanceId) {
		// TODO Auto-generated method stub
		 return (String)SqlSessionTemplate.selectOne("instance.findresourceVersionId", instanceId);
	}

	@Override
	public Map<String, Object> findMessage(String instanceId) {
		// TODO Auto-generated method stub
		 return (Map<String, Object>)SqlSessionTemplate.selectOne("instance.findMessage", instanceId);
	}

	@Override
	public String findComponentInput(String blueId, String bluekey, String instanceId1) {
		// TODO Auto-generated method stub
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("blueId", blueId);
		param.put("bluekey", bluekey);
		param.put("instanceId", instanceId1);
		return (String)SqlSessionTemplate.selectOne("instance.findComponentInput", param);
	}

	@Override
	public int countVersionByAppId(long appId) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectOne("instance.countVersionByAppId", appId);
	}

	@Override
	public List<Map<String, String>> listVersions(Page page) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectList("instance.listVersions", page);
	}

	@Override
	public int countInstancesByVersion(String versionId,long appId) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<>();
		m.put("versionId", versionId);
		m.put("appId", appId+"");

		return SqlSessionTemplate.selectOne("instance.countInstancesByVersion", m);
	}

	@Override
	public String getConfigInput(String configId,long appId) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<>();
		m.put("configId", configId);
		m.put("appId", appId+"");
		return SqlSessionTemplate.selectOne("instance.getConfigInfoByVersionId", m);
	}

	@Override
	public int updateComponentInputTemp(String newInputTemp,String resId,long appId) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<>();
		m.put("resId", resId);
		m.put("appId", appId+"");
		m.put("newInputTemp", newInputTemp);
		return SqlSessionTemplate.update("instance.updateComponentInputTemp", m);
	}

	@Override
	public List<Map<String, Object>> getInstanceListByResVerionId(long appId,
			String oldResVersionId) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<>();
		m.put("resId", oldResVersionId);
		m.put("appId", appId+"");
		return SqlSessionTemplate.selectList("instance.getInstanceListByResVerionId", m);
	}

	@Override
	public void updateInstanceResVersionId(String resVersionId, String insId) {
		// TODO Auto-generated method stub
		Map<String,String> m = new HashMap<>();
		m.put("resVersionId", resVersionId);
		m.put("insId", insId);
		SqlSessionTemplate.update("instance.updateInstanceResVersionId", m);
	}

	@Override
	public void updateInstanceAll(Instance instance) {
		SqlSessionTemplate.update("instance.updateInstanceAll", instance);
	}

	@Override
	public String getVersionName(String resVersionId) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectOne("instance.getVersionName",resVersionId);
	}

	@Override
	public String findresVerId(String componentName, String version) {
		Map<String,String> m = new HashMap<>();
		m.put("componentName", componentName);
		m.put("version", version);
		return SqlSessionTemplate.selectOne("instance.findresVerId",m);
	}

	@Override
	public List<Map<String, Object>> findInstances(String instanceId, String componentName, String resourceVersionId) {
		Map<String,String> m = new HashMap<>();
		m.put("instanceId", instanceId);
		m.put("componentName", componentName);
		m.put("resourceVersionId", resourceVersionId);
		return SqlSessionTemplate.selectList("instance.findInstances", m);
	}

	@Override
	public List<Map<String, Object>> findInstancesBybp(String blueInstanceName, String componentName,
			String resourceVersionId) {
		Map<String,String> m = new HashMap<>();
		m.put("blueInstanceName", blueInstanceName);
		m.put("componentName", componentName);
		m.put("resourceVersionId", resourceVersionId);
		return SqlSessionTemplate.selectList("instance.findInstancesBybp", m);
	}

	@Override
	public Map<String, Object> getInstanceBuInsId(String snapInsId) {
		// TODO Auto-generated method stub
		return SqlSessionTemplate.selectOne("instance.getInstanceBuInsId",snapInsId);
	}
	
	@Override
	public Map<String, Object> getInstanceDetailById(String instanceId) {
		return SqlSessionTemplate.selectOne("instance.getInstanceDetailById",instanceId);
	}

	@Override
	public void updateInstanceStatus(Map<String, Object> param) {
		SqlSessionTemplate.update("instance.updateInstanceStatus", param);
	}

	@Override
	public void updateBlueprintComponentInstanceParams(Map<String, Object> param) {
		SqlSessionTemplate.update("instance.updateBlueprintComponentInstanceParams", param);
	}
}
