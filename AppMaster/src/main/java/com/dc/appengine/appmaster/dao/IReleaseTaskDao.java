package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseTask;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;

public interface IReleaseTaskDao {

	Page listReleaseTaskOrders(Map<String, Object> params, int pageNum, int pageSize);

	ReleaseTaskBean getReleaseTaskOrderById(int taskId);

	List<ReleaseTaskBean> getReleaseTaskOrderByName(String name);

	int saveReleaseTaskOrder(ReleaseTaskBean task);

	int updateReleaseTaskOrder(ReleaseTaskBean task);

	int deleteReleaseTaskOrder(int taskId);

	int countReleaseTaskOrderByFlowInstanceId(String flowInstanceId);

	int updateReleaseTaskOrderStatus(Map<String, Object> params);

	List<ReleaseTaskBean> listReleaseDependTaskOrders(Map<String, Object> params);

	List<ReleaseTaskBean> listReleaseParentTaskOrders(Map<String, Object> params);

	int updateReleaseTaskOrderExecute(Map<String, Object> param);

	List<Map<String, Object>> listReleasesBySystem(String systemId);

	List<Map<String, Object>> listPhasesByRelease(Map<String, Object> params);

	int updateTaskOrderRelease(Map<String, Object> params);

	Map<String, Object> getReleasePhaseById(String releasePhaseId);

	Map<String, Object> getReleaseById(String releaseId);

	List<ReleaseTaskBean> getReleaseTasksByParentId(int parentId);

	List<ReleaseTaskBean> getReleaseTasksByDependId(int dependId);

	int addCRRealeaseTask(ReleaseTaskBean task);

	List<Map<String, Object>> listPhasesBySystemName(String systemName);

	int updateReleaseTaskOrderBatchExecute(Map<String, Object> param);

	List<Map<String, Object>> getReleasePhaseByName(Map<String, Object> params);

	void deleteReleaseTasksByIntegrationId(int integrationId);

	void synReleaseIntegration(List<ReleaseTaskBean> list);

}
