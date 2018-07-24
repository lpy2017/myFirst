package com.dc.appengine.appmaster.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component("flowsaveDao")
public class flowSaveDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	public void saveUpdateInfo(Map<String,Object> m){
		sqlSessionTemplate.insert("flow.saveUpdateInfo", m);
	}
}
