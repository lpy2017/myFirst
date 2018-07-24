package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("NodeLabelDAO")
public class NodeLabelDAO {
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
	
	public List<Map<String, Object>> findLabelsByCluster(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node_label.findLabelsByCluster", param);
	}
	
	public List<Map<String, Object>> findNodesByLabel(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node_label.findNodesByLabel", param);
	}
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("node_label.insert", param);
	}
	
	public int delete(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("node_label.delete", param);
	}
	
	public int check(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("node_label.check", param);
	}
	
	public List<Map<String, Object>> nodes (Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node_label.nodes", param);
	}

}
