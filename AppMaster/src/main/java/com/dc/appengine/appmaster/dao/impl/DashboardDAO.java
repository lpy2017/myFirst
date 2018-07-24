package com.dc.appengine.appmaster.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Service("dashboardDAO")
public class DashboardDAO extends PageQuery {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int getClusterNum(long userId) {
		return sqlSessionTemplate.selectOne("dashboard.getClusterNum", userId);
	}
	
	public int getBlueprintNum(Long[] userId) {
		return sqlSessionTemplate.selectOne("dashboard.getBlueprintNum", userId);
	}
	
	public int getBlueprintInstancNum(Long[] userId) {
		return sqlSessionTemplate.selectOne("dashboard.getBlueprintInstanceNum", userId);
	}
	
	public int getComponentNum() {
		return sqlSessionTemplate.selectOne("dashboard.getComponentNum");
	}
	
	public Page getApplicationByCluster(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "dashboard.getApplicationByCluster");
	}
	
	public Page getBlueprintInstanceByCluster(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "dashboard.getBlueprintInstanceByCluster");
	}
	
	public Page getFlowInstanceByIds(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "dashboard.getFlowInstanceByIds");
	}

}
