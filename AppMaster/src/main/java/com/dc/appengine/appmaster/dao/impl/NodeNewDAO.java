package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Service("nodeNewDAO")
public class NodeNewDAO extends PageQuery {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("node_new.insert", param);
	}
	
	public int delete(String nodeId) {
		return this.sqlSessionTemplate.delete("node_new.delete", nodeId);
	}
	
	public int deleteCheck(String nodeId) {
		return this.sqlSessionTemplate.selectOne("node_new.deleteCheck", nodeId);
	}
	
	public int update(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("node_new.update", param);
	}
	
	public Page list(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "node_new.list");
	}
	
	public List<Map<String, Object>> listAll(String clusterId) {
		return this.sqlSessionTemplate.selectList("node_new.listAll", clusterId);
	}
	
	public Map<String, Object> one(String nodeId) {
		return this.sqlSessionTemplate.selectOne("node_new.one", nodeId);
	}
	
	public int multiDelete(String[] nodes) {
		return this.sqlSessionTemplate.delete("node_new.multiDelete", nodes);
	}
	
	public int checkIP(String ip) {
		return this.sqlSessionTemplate.selectOne("node_new.checkIP", ip);
	}
	
	public int checkOtherIP(Map<String, Object> node) {
		return this.sqlSessionTemplate.selectOne("node_new.checkOtherIP", node);
	}

}
