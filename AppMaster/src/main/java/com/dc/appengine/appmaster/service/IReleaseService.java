package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseApproval;
import com.dc.appengine.appmaster.entity.ReleaseBus;
import com.dc.appengine.appmaster.entity.ReleaseTask;

public interface IReleaseService {
	ReleaseTask getReleaseTaskById(int id);

	List<ReleaseTask> getReleaseTaskByName(String name);

	Page listReleaseTask(Map<String, Object> params, int pageNum, int pageSize);

	int saveReleaseTask(ReleaseTask task);

	void updateReleaseTask(ReleaseTask task);

	void deleteReleaseTask(int taskId);

	List<ReleaseApproval> listReleaseApprovalByTaskId(int taskId);

	void saveReleaseApprovals(int taskId, String approvals);

	void updateReleaseApproval(int taskId, String json);

	void deleteReleaseApprovalsByTaskId(int taskId);

	void deleteReleaseApprovalById(int approvalId);

	void updateReleaseApprovalSingle(Map<String, Object> params);

	String refreshFlowStatus(String instanceId);

	void updateReleaseTaskDone(Map<String, Object> params);

	void updateBlueprintAppsVersion(Map<String, Object> params);

	Page listReleaseApprovalsByUser(Map<String, Object> params, int pageNum, int pageSize);
	
	void updateReleaseTaskStatus(Map<String, Object> params);

	Page listReleaseBus(Map<String, Object> params, int pageNum, int pageSize);

	ReleaseBus getReleaseBusById(int busId);

	List<ReleaseTask> listAllReleaseTaskByBusId(int busId);

	int saveReleaseBus(ReleaseBus bus);

	Page listReleaseTaskByBusId(Map<String, Object> params, int pageNum, int pageSize);

	int updateReleaseBus(ReleaseBus bus);

	int deleteReleaseBusById(int busId);

	void deleteReleaseTaskByBusId(int busId);

	String executeTaskMannual(int taskId);
	String getGanttChart(int busId);

	int countReleaseBusByName(String busName);
}
