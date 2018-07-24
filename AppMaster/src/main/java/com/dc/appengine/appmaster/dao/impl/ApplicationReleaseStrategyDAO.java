package com.dc.appengine.appmaster.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Service("applicationReleaseStrategyDAO")
public class ApplicationReleaseStrategyDAO extends PageQuery {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public int save(Map<String, Object> param) {
		return sqlSessionTemplate.insert("application_release_strategy.save", param);
	}
	
	public int update(Map<String, Object> param) {
		return sqlSessionTemplate.update("application_release_strategy.update", param);
	}
	
	public int delete(String[] param) {
		return sqlSessionTemplate.delete("application_release_strategy.delete", param);
	}
	
	public Page page(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "application_release_strategy.page");
	}

}
