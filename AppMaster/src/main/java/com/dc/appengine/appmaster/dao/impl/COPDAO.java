package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Service("cOPDAO")
public class COPDAO extends PageQuery {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int multiInsertNodes(List<Map<String, Object>> nodes) {
		return sqlSessionTemplate.insert("cop.multiInsertNodes", nodes);
	}
	
	public Page getBlueprintTemplates(int pageNum, int pageSize) {
		return pageQuery(null, pageNum, pageSize, "cop.getBlueprintTemplates");
	}
	
	public List<Map<String, Object>> getFlows(String blueprintTemplateId) {
		return sqlSessionTemplate.selectList("cop.getFlows", blueprintTemplateId);
	}
	
	public Map<String, Object> getBlueprintTemplate(String id) {
		return sqlSessionTemplate.selectOne("cop.getBlueprintTemplate", id);
	}
	
	public List<Map<String, Object>> getComponentVersions(String componentId) {
		return sqlSessionTemplate.selectList("cop.getComponentVersions", componentId);
	}
	
	public int insertLabel(List<Map<String, Object>> labels) {
		return sqlSessionTemplate.insert("cop.insertLabel", labels);
	}
	
	public List<Map<String, Object>> getNodes(List<String> nodes) {
		return sqlSessionTemplate.selectList("cop.getNodes", nodes);
	}
	
	public int updateBlueprintInstance(Map<String, Object> instance) {
		return sqlSessionTemplate.update("cop.updateBlueprintInstance", instance);
	}
	
	public int updateComponentConfig(Map<String, Object> component) {
		return sqlSessionTemplate.update("cop.updateComponentConfig", component);
	}
	
	public Map<String, Object> getBlueprintInstance(String instanceId) {
		return sqlSessionTemplate.selectOne("cop.getBlueprintInstance", instanceId);
	}
	
	public Map<String, Object> getComponent(Map<String, Object> componnet) {
		return sqlSessionTemplate.selectOne("cop.getComponent", componnet);
	}
}
