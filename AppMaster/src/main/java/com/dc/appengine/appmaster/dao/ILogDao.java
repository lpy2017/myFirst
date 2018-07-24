package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

public interface ILogDao {
	public Map<String, Object> findFlowNodeLog(String tokenId);
	public int updateFlowNodeLog(String id,String log,String state);
	public int saveFlowNodeLog(Map<String, Object> param);
	public Map<String,Object>  findFlowNodeLog(Map<String, Object> param);
	public List<Map<String,Object>> findFlowNodeState(String flowInstanceId);
}
