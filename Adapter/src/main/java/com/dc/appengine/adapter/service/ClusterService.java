package com.dc.appengine.adapter.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dc.appengine.adapter.dao.ClusterDAO;

@Service("ClusterService")
public class ClusterService {
	
	@Resource
	private ClusterDAO dao;
	
	public int insert(Map<String, Object> param) {
		return dao.insert(param);
	}
	
	public int delete(Map<String, Object> param) {
//		dao.removeCIDR((String) param.get("id"));
//		dao.removeNetwork((String) param.get("id"));
		dao.removeUserByCluster((String) param.get("id"));
		return dao.delete(param);
	}
	
	public int update(Map<String, Object> param) {
		return dao.update(param);
	}
	
	public List<Map<String, Object>> select(Map<String, Object> param) {
		return dao.select(param);
	}
	
	public int count(Map<String, Object> param) {
		return dao.count(param);
	}
	
	public int countAdmin(Map<String, Object> param) {
		return dao.countAdmin(param);
	}
	
	public List<Map<String, Object>> page(Map<String, Object> param) {
		return dao.page(param);
	}
	
	public List<Map<String, Object>> ui(Map<String, Object> param) {
		return dao.ui(param);
	}
	
	public List<Map<String, Object>> uiAdmin(Map<String, Object> param) {
		return dao.uiAdmin(param);
	}
	
	public List<Map<String, Object>> clusters(Map<String, Object> param) {
		return dao.clusters(param);
	}
	
	public List<Map<String, Object>> clustersAdmin(Map<String, Object> param) {
		return dao.clustersAdmin(param);
	}
	
	public List<Map<String, Object>> otherCluster(Map<String, Object> param) {
		return dao.otherCluster(param);
	}
	
	public int checkName(Map<String, Object> param) {
		return dao.checkName(param);
	}
	
	public Map<String, Object> one(String id) {
		Map<String, Object> clusterInfo = dao.one(id);
//		Map<String, Object> CIDRInfo = dao.getCIDR(id);
//		Map<String, Object> network = dao.getNetwork(id);
//		if (CIDRInfo == null) {
//			clusterInfo.put("network", "default");
//		} else {
//			clusterInfo.put("network", CIDRInfo.get("cidr"));
//		}
//		clusterInfo.put("network_kind", network.get("network"));
		return clusterInfo;
	}
	
	public Map<String, Object> getProfile(String id) {
		return dao.getProfile(id);
	}
	
	public int setCIDR(Map<String, Object> param) {
		return dao.setCIDR(param);
	}
	
	public int setNetwork(Map<String, Object> param) {
		return dao.setNetwork(param);
	}
	
	public List<Map<String, Object>> clusterUsers(Map<String, Object> param) {
		return dao.clusterUsers(param);
	}
	
	public int clusterUsersCount(Map<String, Object> param) {
		return dao.clusterUsersCount(param);
	}
	
	public int checkUser(String user_id) {
		return dao.checkUser(user_id);
	}
	
	public int addNewUser(Map<String, Object> param) {
		return dao.addNewUser(param);
	}
	
	public int setUserRole(Map<String, Object> param) {
		return dao.setUserRole(param);
	}
	
	public int addUser(Map<String, Object> param) {
		return dao.addUser(param);
	}
	
	public int addUserResource(Map<String, Object> param) {
		return dao.addUserResource(param);
	}
	
	public Map<String, Object> getUserResource(long user_id) {
		return dao.getUserResource(user_id);
	}
	
	public int updateUserResource(Map<String, Object> param) {
		return dao.updateUserResource(param);
	}
	
	public int addUserRelationship(Map<String, Object> param) {
		return dao.addUserRelationship(param);
	}
	
	public Map<String, Object> getUserByName(String user_name) {
		return dao.getUserByName(user_name);
	}
	
	public List<Map<String, Object>> getOldUser(Map<String, Object> param) {
		return dao.getOldUser(param);
	}
	
	public int addOldUser(Map<String, Object> param) {
		return dao.addOldUser(param);
	}
	
	public int deleteUser(Map<String, Object> param) {
		return dao.deleteUser(param);
	}
	
	public Map<String, Object> getUserRole(long user_id) {
		return dao.getUserRole(user_id);
	}
	
	public List<Map<String, Object>> getClusterByUser(long user_id) {
		return dao.getClusterByUser(user_id);
	}

}
