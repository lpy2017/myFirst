package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.AppVersion;
import com.dc.appengine.appmaster.entity.AppVersionStrategy;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Page;

public interface IApplicationDao {
	
	Application findByAppId(String appId);
	
	Integer countAppsByUserIdSearch(Application app);
	List<Application> listOperationApps(Page page);
	Integer countInstanceNum(long l);
	String getAppUpdateTime(long l);
	Application getAppInfo(String appId);

	String getClusterNameByClusterId(Map m);

	long save(Application app);

	int delApp(long appId);
	
	List<Application> findAppByBlueprintInstIdAndKey(int blueprintInsId,long key);

	long findAppIdformAppName(String appName, int bluePrintInstanceId, long key);

	List<Map<String, Object>> getAppByBlueInstanceId(int blueInstanceId,
			long userId);

	List<Application> listAppsOfCluster(Application app);

	void save2(Application app);
	
	List<Application> listApps(Application app);

	List<Application> getBlueprintComponents(String blueprintId);

	Map<String, String> getAppliactionVersionConfig(String componentId);
	
	Application getNodeDisplayByAppName(Map paraMap);

	void updateAppVersion(Map<String, Object> map);

}