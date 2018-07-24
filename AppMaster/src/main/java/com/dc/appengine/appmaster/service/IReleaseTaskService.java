package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;

import org.springframework.web.multipart.MultipartFile;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseTask;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;

public interface IReleaseTaskService {

	Page listReleaseTaskOrders(Map<String, Object> params, int pageNum, int pageSize);

	ReleaseTaskBean getReleaseTaskOrderById(int id);

	List<ReleaseTaskBean> getReleaseTaskOrderByName(String name);

	int saveReleaseTaskOrder(ReleaseTaskBean task);

	void updateReleaseTaskOrder(ReleaseTaskBean updateTask);

	void deleteReleaseTaskOrder(int taskId);

	void updateReleaseTaskOrderStatus(Map<String, Object> map);

	List<ReleaseTaskBean> listReleaseDependTaskOrders(Map<String, Object> params);

	List<ReleaseTaskBean> listReleaseParentTaskOrders(Map<String, Object> params);

	String executeTaskOrderMannual(int taskId, String releaseId, String releasePhaseId);

	String callCRRealeaseTask(Map<String, String> params);

	boolean uploadReleaseTaskAttachment(MultipartFile attachment, int taskId);

	List<Map<String, Object>> listReleasesBySystem(String systemId);

	List<Map<String, Object>> listPhasesByRelease(Map<String, Object> params);

	int addCRRealeaseTask(ReleaseTaskBean task);

	void executeCRRealeaseTasksByBatch(String taskList);

	boolean uploadCRReleaseTaskAttachment(String attachment, ServletInputStream inputStream, int taskId);

	List<Map<String, Object>> listPhasesBySystemName(String systemName);

	String executeITSMReleaseTasksByBatch(String taskList, String releasePhaseId);

	void executeJiraRealeaseTask(String taskKey);

}
