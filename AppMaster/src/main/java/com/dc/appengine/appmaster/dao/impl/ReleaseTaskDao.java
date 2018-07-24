package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IReleaseTaskDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseTask;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;

@Component("releaseTaskDao")
public class ReleaseTaskDao extends PageQuery implements IReleaseTaskDao {

	@Override
	public Page listReleaseTaskOrders(Map<String, Object> params, int pageNum, int pageSize) {
		return pageQuery(params, pageNum, pageSize, "releaseTask.listReleaseTaskOrders");
	}

	@Override
	public ReleaseTaskBean getReleaseTaskOrderById(int taskId) {
		return sqlSessionTemplate.selectOne("releaseTask.getReleaseTaskOrderById", taskId);
	}

	@Override
	public List<ReleaseTaskBean> getReleaseTaskOrderByName(String taskName) {
		return sqlSessionTemplate.selectList("releaseTask.getReleaseTaskOrderByName", taskName);
	}

	@Override
	public int saveReleaseTaskOrder(ReleaseTaskBean task) {
		return sqlSessionTemplate.insert("releaseTask.saveReleaseTaskOrder", task);
	}

	@Override
	public int updateReleaseTaskOrder(ReleaseTaskBean task) {
		return sqlSessionTemplate.update("releaseTask.updateReleaseTaskOrder",task);
	}

	@Override
	public int deleteReleaseTaskOrder(int taskId) {
		return sqlSessionTemplate.delete("releaseTask.deleteReleaseTaskOrder", taskId);
	}

	@Override
	public int countReleaseTaskOrderByFlowInstanceId(String flowInstanceId) {
		return sqlSessionTemplate.selectOne("releaseTask.countReleaseTaskOrderByFlowInstanceId", flowInstanceId);
	}

	@Override
	public int updateReleaseTaskOrderStatus(Map<String, Object> params) {
		return sqlSessionTemplate.update("releaseTask.updateReleaseTaskOrderStatus",params);
	}

	@Override
	public List<ReleaseTaskBean> listReleaseDependTaskOrders(Map<String, Object> params) {
		return sqlSessionTemplate.selectList("releaseTask.listReleaseDependTaskOrders", params);
	}

	@Override
	public List<ReleaseTaskBean> listReleaseParentTaskOrders(Map<String, Object> params) {
		return sqlSessionTemplate.selectList("releaseTask.listReleaseParentTaskOrders", params);
	}

	@Override
	public int updateReleaseTaskOrderExecute(Map<String, Object> param) {
		return sqlSessionTemplate.update("releaseTask.updateReleaseTaskOrderExecute",param);
	}

	@Override
	public List<Map<String, Object>> listReleasesBySystem(String systemId) {
		return sqlSessionTemplate.selectList("releaseTask.listReleasesBySystem", systemId);
	}

	@Override
	public List<Map<String, Object>> listPhasesByRelease(Map<String, Object> params) {
		return sqlSessionTemplate.selectList("releaseTask.listPhasesByRelease", params);
	}

	@Override
	public int updateTaskOrderRelease(Map<String, Object> params) {
		return sqlSessionTemplate.update("releaseTask.updateTaskOrderRelease", params);
	}

	@Override
	public Map<String, Object> getReleasePhaseById(String releasePhaseId) {
		return sqlSessionTemplate.selectOne("releaseTask.getReleasePhaseById", releasePhaseId);
	}

	@Override
	public Map<String, Object> getReleaseById(String releaseId) {
		return sqlSessionTemplate.selectOne("releaseTask.getReleaseById", releaseId);
	}

	@Override
	public List<ReleaseTaskBean> getReleaseTasksByParentId(int parentId) {
		return sqlSessionTemplate.selectList("releaseTask.getReleaseTasksByParentId", parentId);
	}

	@Override
	public List<ReleaseTaskBean> getReleaseTasksByDependId(int dependId) {
		return sqlSessionTemplate.selectList("releaseTask.getReleaseTasksByDependId", dependId);
	}

	@Override
	public int addCRRealeaseTask(ReleaseTaskBean task) {
		return sqlSessionTemplate.insert("releaseTask.addCRRealeaseTask", task);
	}

	@Override
	public List<Map<String, Object>> listPhasesBySystemName(String systemName) {
		return sqlSessionTemplate.selectList("releaseTask.listPhasesByRelease", systemName);
	}

	@Override
	public int updateReleaseTaskOrderBatchExecute(Map<String, Object> param) {
		return sqlSessionTemplate.update("releaseTask.updateReleaseTaskOrderBatchExecute",param);
	}

	@Override
	public List<Map<String, Object>> getReleasePhaseByName(Map<String, Object> params) {
		return sqlSessionTemplate.selectOne("releaseTask.getReleasePhaseByName", params);
	}

	@Override
	public void deleteReleaseTasksByIntegrationId(int integrationId) {
		sqlSessionTemplate.delete("releaseTask.deleteReleaseTasksByIntegrationId", integrationId);
	}

	@Override
	public void synReleaseIntegration(List<ReleaseTaskBean> list) {
		sqlSessionTemplate.insert("releaseTask.synReleaseIntegration", list);
	}

}
