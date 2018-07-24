package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

@Component("componentUpdateDAO")
public class ComponentUpdateDAO {
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	
	public List<String> findInstancesByAppId(long appId) {
		return this.sqlSessionTemplate.selectList("component_update.findInstancesByAppId", appId);
	}
	
	public String findJsonByFlowId(String flowId) {
		return this.sqlSessionTemplate.selectOne("component_update.findJsonByFlowId", flowId);
	}
	
	public int updateInputAndOutput(Map<String, Object> param) {
		return this.sqlSessionTemplate.update("component_update.updateInputAndOutput", param);
	}
	
	public Long findKeyByAppId(long appId) {
		return this.sqlSessionTemplate.selectOne("component_update.findKeyByAppId", appId);
	}

}
