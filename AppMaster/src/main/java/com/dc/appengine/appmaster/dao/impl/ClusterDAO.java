package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Service("clusterDAO")
public class ClusterDAO extends PageQuery {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("cluster.insert", param);
	}
	
	public int delete(String clusterId) {
		return this.sqlSessionTemplate.delete("cluster.delete", clusterId);
	}
	
	public int update(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("cluster.update", param);
	}
	
	public Page list(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "cluster.list");
	}
	
	public List<Map<String, Object>> listAll(String userId) {
		return this.sqlSessionTemplate.selectList("cluster.listAll", userId);
	}
	
	public int check(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("cluster.check", param);
	}
	
	public int checkOther(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("cluster.checkOther", param);
	}
	
	public Map<String, Object> one(String id) {
		return this.sqlSessionTemplate.selectOne("cluster.one", id);
		
	}
	
	public Map<String, Object> findClusterByName(String name) {
		return this.sqlSessionTemplate.selectOne("cluster.findClusterByName", name);
	}

}
