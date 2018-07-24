package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseApproval;
import com.dc.appengine.appmaster.entity.ReleaseBus;
import com.dc.appengine.appmaster.entity.ReleaseTask;

public interface IReleaseDao {
	ReleaseTask getReleaseTaskById(int id);
	
	List<ReleaseTask> getReleaseTaskByName(String name);
	
	ReleaseApproval getReleaseApprovalById(int approvalId);
	
	Page listReleaseApprovalsByUser(Map<String, Object> params, int pageNum, int pageSize);
	
	Page listReleaseTask(Map<String, Object> params, int pageNum, int pageSize);

	int saveReleaseTask(ReleaseTask task);

	int updateReleaseTask(ReleaseTask task);

	int deleteReleaseTask(int taskId);

	List<ReleaseApproval> listReleaseApprovalByTaskId(int taskId);
	
	int saveReleaseApproval(ReleaseApproval params);
	
	int updateReleaseApprovalSingle(Map<String, Object> params);
	
	int updateReleaseApprovalStatus(Map<String, Object> params);
	
	int deleteReleaseApprovalsByTaskId(int taskId);
	
	int deleteReleaseApprovalById(int approvalId);
	
	int updateReleaseTaskExecute(Map<String, Object> params);
	
	int updateReleaseTaskDone(Map<String, Object> params);

	int updateReleaseTaskStatus(Map<String, Object> params);

	Page listReleaseBus(Map<String, Object> params, int pageNum, int pageSize);
	
	ReleaseBus getReleaseBusById(int busId);
	
	List<ReleaseTask> listAllReleaseTaskByBusId(int busId);

	int saveReleaseBus(ReleaseBus bus);

	Page listReleaseTaskByBusId(Map<String, Object> params, int pageNum, int pageSize);

	int updateReleaseBus(ReleaseBus bus);

	int deleteReleaseBusById(int busId);
	List listReleaseTaskByBusId(int busId);
	int judgeHaveChild(int id);
	Map findReleaseTaskById(int id);
	int countReleaseTaskByFlowInstanceId(String flowInstanceId);

	int countReleaseBusByName(String busName);
}
