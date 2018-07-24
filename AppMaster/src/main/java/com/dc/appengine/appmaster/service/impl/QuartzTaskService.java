package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.QuartzTaskDAO;

@Service("quartzTaskService")
public class QuartzTaskService {
	
	@Autowired
	@Qualifier("quartzTaskDAO")
	private QuartzTaskDAO dao;
	
	public Map<String, Object> getCronExpression(long taskId) {
		return dao.getCronExpression(taskId);
	}
	
	public List<Map<String, Object>> getPreTaskNotFinished(long taskId) {
		return dao.getPreTaskNotFinished(taskId);
	}
	
	public List<Map<String, Object>> getApproverNotPass(long taskId) {
		return dao.getApproverNotPass(taskId);
	}
	
	public int updateTask(Map<String, Object> param) {
		return dao.updateTask(param);
	}
	
	public List<Map<String, Object>> getTaskByAppAndStatus(Map<String, Object> param) {
		return dao.getTaskByAppAndStatus(param);
	}

}
