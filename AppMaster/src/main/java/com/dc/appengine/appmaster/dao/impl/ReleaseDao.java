package com.dc.appengine.appmaster.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IReleaseDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseApproval;
import com.dc.appengine.appmaster.entity.ReleaseBus;
import com.dc.appengine.appmaster.entity.ReleaseTask;

@Component("releaseDao")
public class ReleaseDao extends PageQuery implements IReleaseDao {
	@Override
	public ReleaseTask getReleaseTaskById(int taskId) {
		return sqlSessionTemplate.selectOne("release.getReleaseTaskById", taskId);
	}
	
	@Override
	public List<ReleaseTask> getReleaseTaskByName(String taskName) {
		return sqlSessionTemplate.selectList("release.getReleaseTaskByName", taskName);
	}
	
	@Override
	public ReleaseApproval getReleaseApprovalById(int approvalId) {
		return sqlSessionTemplate.selectOne("release.getReleaseApprovalById", approvalId);
	}
	
	@Override
	public Page listReleaseApprovalsByUser(Map<String, Object> params, int pageNum, int pageSize) {
		return pageQuery(params, pageNum, pageSize, "release.listReleaseApprovalsByUser");
	}

	@Override
	public Page listReleaseTask(Map<String, Object> params, int pageNum, int pageSize) {
		return pageQuery(params, pageNum, pageSize, "release.listReleaseTask");
	}

	@Override
	public int saveReleaseTask(ReleaseTask task) {
		return sqlSessionTemplate.insert("release.saveReleaseTask", task);
	}

	@Override
	public int updateReleaseTask(ReleaseTask task) {
		return sqlSessionTemplate.update("release.updateReleaseTask",task);
	}
	
	@Override
	public int deleteReleaseTask(int taskId) {
		return sqlSessionTemplate.delete("release.deleteReleaseTask", taskId);
	}

	
	@Override
	public List<ReleaseApproval> listReleaseApprovalByTaskId(int taskId) {
		return sqlSessionTemplate.selectList("release.listReleaseApprovalByTaskId", taskId);
	}
	
	@Override
	public int saveReleaseApproval(ReleaseApproval approval) {
		return sqlSessionTemplate.insert("release.saveReleaseApproval",approval);
	}
	
	@Override
	public int updateReleaseApprovalStatus(Map<String, Object> params) {
		return sqlSessionTemplate.update("release.updateReleaseApprovalStatus",params);
	}
	
	@Override
	public int deleteReleaseApprovalsByTaskId(int taskId) {
		return sqlSessionTemplate.delete("release.deleteReleaseApprovalsByTaskId", taskId);
	}

	@Override
	public int deleteReleaseApprovalById(int approvalId) {
		return sqlSessionTemplate.delete("release.deleteReleaseApprovalById", approvalId);
	}
	
	@Override
	public int updateReleaseApprovalSingle(Map<String, Object> params) {
		return sqlSessionTemplate.update("release.updateReleaseApprovalSingle",params);
	}
	
	@Override
	public int updateReleaseTaskExecute(Map<String, Object> params) {
		return sqlSessionTemplate.update("release.updateReleaseTaskExecute",params);
	}
	
	@Override
	public int updateReleaseTaskDone(Map<String, Object> params) {
		return sqlSessionTemplate.update("release.updateReleaseTaskDone",params);
	}

	@Override
	public int updateReleaseTaskStatus(Map<String, Object> params) {
		return sqlSessionTemplate.update("release.updateReleaseTaskStatus",params);
	}

	@Override
	public Page listReleaseBus(Map<String, Object> params, int pageNum, int pageSize) {
		return pageQuery(params, pageNum, pageSize, "release.listReleaseBus");
	}
	
	@Override
	public ReleaseBus getReleaseBusById(int busId) {
		return sqlSessionTemplate.selectOne("release.getReleaseBusById", busId);
	}
	
	@Override
	public List<ReleaseTask> listAllReleaseTaskByBusId(int busId) {
		return sqlSessionTemplate.selectList("release.listReleaseTaskByBusId", busId);
	}

	@Override
	public int saveReleaseBus(ReleaseBus bus) {
		return sqlSessionTemplate.insert("release.saveReleaseBus", bus);
	}

	@Override
	public Page listReleaseTaskByBusId(Map<String, Object> params, int pageNum, int pageSize) {
		return pageQuery(params, pageNum, pageSize, "release.listDependReleaseTaskByBusId");
	}

	@Override
	public int updateReleaseBus(ReleaseBus bus) {
		return sqlSessionTemplate.update("release.updateReleaseBus", bus);
	}

	@Override
	public int deleteReleaseBusById(int busId) {
		return sqlSessionTemplate.delete("release.deleteReleaseBusById", busId);
	}
	
	@Override
	public int countReleaseTaskByFlowInstanceId(String flowInstanceId) {
		return sqlSessionTemplate.selectOne("release.countReleaseTaskByFlowInstanceId", flowInstanceId);
	}

	@Override
	public List listReleaseTaskByBusId(int busId) {
		return sqlSessionTemplate.selectList("release.listReleaseTasksByBusId", busId);
	}

	@Override
	public int judgeHaveChild(int id) {
		return sqlSessionTemplate.selectOne("release.judgeHaveChild", id);
	}

	@Override
	public Map findReleaseTaskById(int id) {
		return sqlSessionTemplate.selectOne("release.findReleaseTaskById", id);
	}
	
	@Override
	public int countReleaseBusByName(String busName) {
		return sqlSessionTemplate.selectOne("release.countReleaseBusByName", busName);
	}
}
