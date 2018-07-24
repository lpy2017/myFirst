package com.dc.appengine.adapter.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("ClusterDAO")
public class ClusterDAO {
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.insert", param);
	}
	
	public int delete(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("cluster.delete", param);
	}
	
	public int update(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("cluster.update", param);
	}
	
	public List<Map<String, Object>> select(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.select", param);
	}
	
	public int count(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("cluster.count", param);
	}
	
	public int countAdmin(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("cluster.count_admin", param);
	}
	
	public List<Map<String, Object>> page(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.page", param);
	}
	
	public List<Map<String, Object>> ui(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.ui", param);
	}
	
	public List<Map<String, Object>> uiAdmin(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.ui_admin", param);
	}
	
	public List<Map<String, Object>> clusters(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.clusters", param);
	}
	
	public List<Map<String, Object>> clustersAdmin(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.clusters_admin", param);
	}
	
	public List<Map<String, Object>> otherCluster(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.otherCluster", param);
	}
	
	public int checkName(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("cluster.checkName", param);
	}
	
	public Map<String, Object> one(String id) {
		return this.sqlSessionTemplate.selectOne("cluster.one", id);
	}
	
	public Map<String, Object> getProfile(String id) {
		return this.sqlSessionTemplate.selectOne("cluster.getProfileName", id);
	}
	
	public int setCIDR(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.setCIDR", param);
	}
	
	public int removeCIDR(String cluster_id) {
		return this.sqlSessionTemplate.delete("cluster.removeCIDR", cluster_id);
	}
	
	public Map<String, Object> getCIDR(String cluster_id) {
		return this.sqlSessionTemplate.selectOne("cluster.getCIDR", cluster_id);
	}
	
	public int setNetwork(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.setNetwork", param);
	}
	
	public int removeNetwork(String cluster_id) {
		return this.sqlSessionTemplate.delete("cluster.removeNetwork", cluster_id);
	}
	
	public Map<String, Object> getNetwork(String cluster_id) {
		return this.sqlSessionTemplate.selectOne("cluster.getNetwork", cluster_id);
	}
	
	public List<Map<String, Object>> clusterUsers(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.clusterUsers", param);
	}
	
	public int clusterUsersCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("cluster.clusterUsersCount", param);
	}
	
	public int addUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.addUser", param);
	}
	
	public int addUserResource(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.addUserResource", param);
	}
	
	public int updateUserResource(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("cluster.updateUserResource", param);
	}
	
	public Map<String, Object> getUserResource(long user_id) {
		return this.sqlSessionTemplate.selectOne("cluster.getUserResource", user_id);
	}
	
	public int addNewUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.addNewUser", param);
	}
	
	public int addUserRelationship(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.addUserRelationship", param);
	}
	
	public int checkUser(String user_id) {
		return (Integer) this.sqlSessionTemplate.selectOne("cluster.checkUser", user_id);
	}
	
	public List<Map<String, Object>> getOldUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("cluster.getOldUser", param);
	}
	
	public int addOldUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.addOldUser", param);
	}
	
	public int deleteUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("cluster.deleteUser", param);
	}
	
	public List<Map<String, Object>> getClusterByUser(long user_id) {
		return this.sqlSessionTemplate.selectList("cluster.getClusterByUser", user_id);
	}
	
	public Map<String, Object> getUserRole(long user_id) {
		return this.sqlSessionTemplate.selectOne("cluster.getUserRole", user_id);
	}
	
	public int setUserRole(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.setUserRole", param);
	}
	
	public Map<String, Object> getUserByName(String user_name) {
		return this.sqlSessionTemplate.selectOne("cluster.getUserByName", user_name);
	}
	
	public int removeUserByCluster(String cluster_id) {
		return this.sqlSessionTemplate.delete("cluster.removeUserByCluster", cluster_id);
	}

}
