package com.dc.appengine.appmaster.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IEnvParamDao;

@Component("envParamDao")
public class EnvParamDao implements IEnvParamDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void saveEnvVar(Map<String, Object> params) {
		sqlSessionTemplate.insert("envParams.saveEnvVar", params);
	}
}
