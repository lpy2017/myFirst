package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.impl.DashboardDAO;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IBlueprintService;

@Service("dashboardService")
public class DashboardService {
	
	@Autowired
	@Qualifier("dashboardDAO")
	private DashboardDAO dao;
	
	@Autowired
	@Qualifier("blueprintService")
	private IBlueprintService blueprintService;
	
	public int getClusterNum(long userId) {
		return dao.getClusterNum(userId);
	}
	
	public int getBlueprintNum(Long[] userId) {
		return dao.getBlueprintNum(userId);
	}
	
	public int getBlueprintInstanceNum(Long[] userId) {
		return dao.getBlueprintInstancNum(userId);
	}
	
	public int getComponentNum() {
		return dao.getComponentNum();
	}
	
	public Page getApplicationByCluster(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.getApplicationByCluster(condition, pageNum, pageSize);
	}
	
	public Page getBlueprintInstanceByCluster(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.getBlueprintInstanceByCluster(condition, pageNum, pageSize);
	}
	
	public Page getFlowInstanceByIds(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.getFlowInstanceByIds(condition, pageNum, pageSize);
	}
	
	public List<Map<String, Object>> getFlowInfo(String status) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("status", status);
		return blueprintService.getFlowInstanceIds(jsonObj);
	}

}
