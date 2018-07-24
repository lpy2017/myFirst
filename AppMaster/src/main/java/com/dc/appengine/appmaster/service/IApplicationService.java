package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.OperationResource;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Application;


public interface IApplicationService {

	Page listOperationApps(int pageSize, int pageNum,  Long userId,int blueInstanceId,String sortName,String sortOrder);

	Application getAppInfo(String appId);

	int getConfigUsedCount(int versionId);

	List<Map<String, Object>> findAppByNodeName(String nodeName);
	
	List<Application> findAppByBlueprintInstIdAndKey(int blueprintInsId,long key);

	String listAppsOfCluster(String clusterId, long userId);

	void saveApplication(Application app);

	Application getApp(long appId);

	List<Application> findByAutoMaintain();

	String listApps(long blue_instance_id, long userId);
	
	Application getNodeDisplayByAppName(Map paraMap);
}