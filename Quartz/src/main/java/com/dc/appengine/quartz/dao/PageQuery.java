package com.dc.appengine.quartz.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;

import com.dc.appengine.quartz.entity.Page;
import com.dc.appengine.quartz.utils.JudgeUtil;
import com.dc.appengine.quartz.utils.SortUtil;
import com.github.pagehelper.PageRowBounds;

import tk.mybatis.orderbyhelper.OrderByHelper;

public class PageQuery {
	
	@Resource
	protected SqlSessionTemplate sqlSessionTemplate;
	
	public Page pageQuery(Map<String,Object> condition,int pageNum,int pageSize,String sql){
		int offset = pageSize*(pageNum-1);
		PageRowBounds prb = new PageRowBounds(offset, pageSize);
		//排序
		if(!JudgeUtil.isEmpty(condition)&&!JudgeUtil.isEmpty(condition.get("sortName"))&&SortUtil.judgeSortOrder(condition.get("sortOrder"))){
			OrderByHelper.orderBy(condition.get("sortName")+" "+condition.get("sortOrder"));
		}
		List<Object> list = sqlSessionTemplate.selectList(sql, condition, prb);
		int total = Integer.valueOf(prb.getTotal().toString());
		Page page = new Page(pageSize,total);
		page.setPageNumber(pageNum);
		page.setRows(list);
		return page;
	}
}
