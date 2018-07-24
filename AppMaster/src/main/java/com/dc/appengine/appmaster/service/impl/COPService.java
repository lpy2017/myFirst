package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.COPDAO;
import com.dc.appengine.appmaster.entity.Page;

@Service("cOPService")
public class COPService {
	
	@Autowired
	@Qualifier("cOPDAO")
	private COPDAO dao;
	
	public int multiInsertNodes(List<Map<String, Object>> nodes) {
		return dao.multiInsertNodes(nodes);
	}
	
	public Page getBlueprintTemplates(int pageNum, int pageSize) {
		return dao.getBlueprintTemplates(pageNum, pageSize);
	}
	
	public List<Map<String, Object>> getFlows(String blueprintTemplateId) {
		return dao.getFlows(blueprintTemplateId);
	}
	
	public Map<String, Object> getBlueprintTemplate(String id) {
		return dao.getBlueprintTemplate(id);
	}
	
	public List<Map<String, Object>> getComponentVersions(String componentId) {
		return dao.getComponentVersions(componentId);
	}
	
	public int insertLabels(List<Map<String, Object>> labels) {
		return dao.insertLabel(labels);
	}
	
	public List<Map<String, Object>> getNodes(List<String> nodes) {
		return dao.getNodes(nodes);
	}
	
	public int updateBlueprintInstance(Map<String, Object> instance) {
		return dao.updateBlueprintInstance(instance);
	}
	
	public int updateComponentConfig(Map<String, Object> component) {
		return dao.updateComponentConfig(component);
	}
	
	public Map<String, Object> getBlueprintInstance(String instanceId) {
		return dao.getBlueprintInstance(instanceId);
	}
	
	public Map<String, Object> getComponent(Map<String, Object> component) {
		return dao.getComponent(component);
	}

}
