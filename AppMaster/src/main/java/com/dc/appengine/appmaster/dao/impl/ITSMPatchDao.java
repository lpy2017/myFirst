package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.SortUtil;

import tk.mybatis.orderbyhelper.OrderByHelper;

@Service("iTSMPatchDao")
public class ITSMPatchDao extends PageQuery {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public List<Map<String, Object>> getPatchList(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("itsmPatch.getPatchList", param);
	}
	
	public Page getPatchStatistics(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "itsmPatch.getPatchStatistics");
	}
	
	public List<Map<String, Object>> export(String[] ids) {
		return sqlSessionTemplate.selectList("itsmPatch.export", ids);
	}
	
	public List<Map<String, Object>> exportAll(Map<String, Object> condition) {
		//保持排序与查询结果一致
		if(!JudgeUtil.isEmpty(condition)&&!JudgeUtil.isEmpty(condition.get("sortName"))&&SortUtil.judgeSortOrder(condition.get("sortOrder"))){
			OrderByHelper.orderBy(condition.get("sortName")+" "+condition.get("sortOrder"));
		}
		return sqlSessionTemplate.selectList("itsmPatch.getPatchStatistics", condition);
	}
	
}
