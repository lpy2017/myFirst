package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.ClusterDAO;
import com.dc.appengine.appmaster.entity.Page;

@Service("clusterService")
public class ClusterService {
	
	@Autowired
	@Qualifier("clusterDAO")
	private ClusterDAO dao;
	
	public int insert(Map<String, Object> param) {
		return dao.insert(param);
	}
	
	public int delete(String clusterId) {
		return dao.delete(clusterId);
	}
	
	public int update(Map<String, Object> param) {
		return dao.update(param);
	}
	
	public Page list(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.list(condition, pageNum, pageSize);
	}
	
	public List<Map<String, Object>> listAll(String userId) {
		return dao.listAll(userId);
	}
	
	public int check(Map<String, Object> param) {
		return dao.check(param);
	}
	
	public int checkOther(Map<String, Object> param) {
		return dao.checkOther(param);
	}
	
	public Map<String, Object> one(String id) {
		return dao.one(id);
	}

}
