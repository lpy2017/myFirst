package com.dc.appengine.adapter.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("LabelDAO")
public class LabelDAO {
	
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
	
	public int insert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("label.insert", param);
	}
	
	public int delete(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("label.delete", param);
	}
		
	public List<Map<String, Object>> select(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("label.select", param);
	}
	
	public int multiInsert(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("label.multiInsert", param);
	}
	
	public List<Map<String, Object>> findByUser(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("label.findByUser", param);
	}
	
	public int findByUserCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("label.findByUserCount", param);
	}
	
	public List<Map<String, Object>> findNodeByLabel(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("label.findNodeByLabel", param);
	}
	
	public int findNodeByLabelCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("label.findNodeByLabelCount", param);
	}
	
	public List<Map<String, Object>> findNodeByCluster(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("label.findNodeByCluster", param);
	}
	
	public int findNodeByClusterCount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne("label.findNodeByClusterCount", param);
	}
	
	public int multiAdd(Map<String, Object> param) {
		return this.sqlSessionTemplate.insert("label.multiAdd", param);
	}
	
	public int multiDelete(Map<String, Object> param) {
		return this.sqlSessionTemplate.delete("label.multiDelete", param);
	}
	
	public List<Map<String, Object>> findNodeByClusterAndLabel(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList("label.findNodeByClusterAndLabel", param);
	}

}
