package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("quartzTaskDAO")
public class QuartzTaskDAO {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	public Map<String, Object> getCronExpression(long taskId) {
		return sqlSessionTemplate.selectOne("quartz_task.getCronExpression", taskId);
	}
	
	public List<Map<String, Object>> getPreTaskNotFinished(long taskId) {
		return sqlSessionTemplate.selectList("quartz_task.getPreTaskNotFinished", taskId);
	}
	
	public List<Map<String, Object>> getApproverNotPass(long taskId) {
		return sqlSessionTemplate.selectList("quartz_task.getApproverNotPass", taskId);
	}
	
	public int updateTask(Map<String, Object> param) {
		return sqlSessionTemplate.update("quartz_task.updateTask", param);
	}
	
	public List<Map<String, Object>> getTaskByAppAndStatus(Map<String, Object> param) {
		return sqlSessionTemplate.selectList("quartz_task.getTaskByAppAndStatus", param);
	}

}
