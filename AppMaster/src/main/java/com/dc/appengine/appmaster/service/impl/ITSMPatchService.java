package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.ITSMPatchDao;
import com.dc.appengine.appmaster.entity.Page;

@Service
public class ITSMPatchService {
	
	@Autowired
	@Qualifier("iTSMPatchDao")
	private ITSMPatchDao dao;
	
	public List<Map<String, Object>> getPatchList(Map<String, Object> param) {
		return dao.getPatchList(param);
	}
	
	public Page getPatchStatistics(Map<String, Object> condition, int pageSize, int pageNum) {
		return dao.getPatchStatistics(condition, pageNum, pageSize);
	}
	
	public List<Map<String, Object>> export(String[] ids) {
		return dao.export(ids);
	}
	
	public List<Map<String, Object>> exportAll(Map<String, Object> condition) {
		return dao.exportAll(condition);
	}

}
