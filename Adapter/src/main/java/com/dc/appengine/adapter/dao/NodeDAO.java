package com.dc.appengine.adapter.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("NodeDAO")
public class NodeDAO {
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("node.insert", param);
	}
	
	public int delete(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("node.delete", param);
	}
	
	public int update(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("node.update", param);
	}
	
	public List<Map<String, Object>> select(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node.select", param);
	}
	
	public int count(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("node.count", param);
	}
	
	public int uicount(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("node.uicount", param);
	}
	
	public List<Map<String, Object>> page(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node.page", param);
	}
	
	public List<Map<String, Object>> ui(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("node.ui", param);
	}
	
	public List<Map<String, Object>> getLabel(String node_id) {
		return this.sqlSessionTemplate.selectList("node.getLabel", node_id);
	}
	
	public int checkName(Map<String, Object> param) {
		return (Integer) this.sqlSessionTemplate.selectOne("node.checkName", param);
	}
	
	public Map<String, Object> one(String id) {
		return this.sqlSessionTemplate.selectOne("node.one", id);
	}
	
	public int updateByStatus(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("node.updateByStatus", param);
	}
	
	public int multiAdd(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("node.multiAdd", param);
	}

}
