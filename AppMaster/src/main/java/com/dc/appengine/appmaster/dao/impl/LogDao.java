package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.ILogDao;
import com.dc.appengine.appmaster.dao.PageQuery;

@Component("logDao")
public class LogDao extends PageQuery implements ILogDao {

	@Override
	public Map<String, Object> findFlowNodeLog(String tokenId) {
		return sqlSessionTemplate.selectOne("log.findFlowNodeLog", tokenId);
	}

	@Override
	public int updateFlowNodeLog(String id, String log,String state) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		param.put("log", log);
		param.put("state", state);
		return sqlSessionTemplate.update("log.updateFlowNodeLog", param);
	}

	@Override
	public int saveFlowNodeLog(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("log.saveFlowNodeLog",param);
	}

	@Override
	public Map<String, Object> findFlowNodeLog(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("log.findFlowNodeLog", param);
	}

	@Override
	public List<Map<String, Object>> findFlowNodeState(String flowInstanceId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("log.findFlowNodeState", flowInstanceId);
	}
}
