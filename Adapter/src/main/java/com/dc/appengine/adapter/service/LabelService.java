package com.dc.appengine.adapter.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dc.appengine.adapter.dao.LabelDAO;

@Service("LabelService")
public class LabelService {
	
	@Resource
	private LabelDAO dao;
	
	public int insert(Map<String, Object> param) {
		return dao.insert(param);
	}
	
	public int delete(Map<String, Object> param) {
		return dao.delete(param);
	}
	
	public List<Map<String, Object>> select(Map<String, Object> param) {
		return dao.select(param);
	}
	
	public int multiInsert(Map<String, Object> param) {
		return dao.multiInsert(param);
	}
	
	public List<Map<String, Object>> findByUser(Map<String, Object> param) {
		return dao.findByUser(param);
	}
	
	public int findByUserCount(Map<String, Object> param) {
		return dao.findByUserCount(param);
	}
	
	public List<Map<String, Object>> findNodeByLabel(Map<String, Object> param) {
		return dao.findNodeByLabel(param);
	}
	
	public int findNodeByLabelCount(Map<String, Object> param) {
		return dao.findNodeByLabelCount(param);
	}
	
	public List<Map<String, Object>> findNodeByCluster(Map<String, Object> param) {
		return dao.findNodeByCluster(param);
	}
	
	public int findNodeByClusterCount(Map<String, Object> param) {
		return dao.findNodeByClusterCount(param);
	}
	
	public int multiAdd(Map<String, Object> param) {
		return dao.multiAdd(param);
	}
	
	public int multiDelete(Map<String, Object> param) {
		return dao.multiDelete(param);
	}
	
	public List<Map<String, Object>> findNodeByClusterAndLabel(Map<String, Object> param) {
		return dao.findNodeByClusterAndLabel(param);
	}

}
