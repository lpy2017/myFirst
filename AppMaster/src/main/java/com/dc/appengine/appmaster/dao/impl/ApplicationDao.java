package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Component("applicationDao")
public class ApplicationDao implements IApplicationDao{
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public Application findByAppId(String appId) {
		return sqlSessionTemplate.selectOne("application.getAppInfo",appId);
	}

	@Override
	public Integer countAppsByUserIdSearch(Application application) {
		// TODO Auto-generated method stub
		return (Integer)sqlSessionTemplate.selectOne("application.countAppsByUserIdSearch", application);
	}
	@Override
	public List<Application> listOperationApps(Page page) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("application.listOperationApps", page);
	}
	public Integer countInstanceNum( long appId ){
		return (Integer)sqlSessionTemplate.selectOne("application.countInstanceNum", appId);
	}

	@Override
	public String getAppUpdateTime(long appId) {
		// TODO Auto-generated method stub
		
		return sqlSessionTemplate.selectOne("application.getAppUpdateTime", appId);
	}
	@Override
	public Application getAppInfo(String appId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("application.getAppInfo", appId);
	}

	@Override
	public String getClusterNameByClusterId(Map m) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("application.getClusterName",m);
	}

	@Override
	public long save(Application app) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("application.save", app);
	}

	@Override
	public int delApp(long appId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("application.delApp", appId);
	}

	@Override
	public List<Application> findAppByBlueprintInstIdAndKey(int blueprintInsId, long key) {
		Object params = MessageHelper.wrapMessage("blueprintInsId",blueprintInsId,"key",key);
		return sqlSessionTemplate.selectList("application.findAppByBlueprintInstIdAndKey", params);
	}


	@Override
	public long findAppIdformAppName(String appName, int bluePrintInstanceId, long key) {
		Object params = MessageHelper.wrapMessage("appName",appName,"bluePrintInstanceId",bluePrintInstanceId,"key",key);
		long a = 0;
		try {
			a =  sqlSessionTemplate.selectOne("application.findAppIdformAppName",params);

		} catch (java.lang.NullPointerException e) {
			return a;

		}
		return a ;
	}

	@Override
	public List<Map<String, Object>> getAppByBlueInstanceId(int blueInstanceId,
			long userId) {
		// TODO Auto-generated method stub
		Object params = MessageHelper.wrapMessage("blueInstanceId",blueInstanceId,
				"userId",userId);
		return sqlSessionTemplate.selectList("application.getAppByBlueInstanceId",params);
	}

	@Override
	public List<Application> listAppsOfCluster(Application app) {
		// TODO Auto-generated method stub

		return sqlSessionTemplate.selectList("application.listAppsOfCluster", app);
	}

	@Override
	public void save2(Application app) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("application.save2", app);
	}
	
	@Override
	public List<Application> listApps(Application app) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("application.listApps", app);
	}

	@Override
	public List<Application> getBlueprintComponents(String blueprintId) {
		
		List<Application> ll =  sqlSessionTemplate.selectList("application.getBlueprintComponents", blueprintId);
		return ll;
	}

	@Override
	public Map<String, String> getAppliactionVersionConfig(String componentId) {
		return null;
	}

	@Override
	public Application getNodeDisplayByAppName(Map paraMap) {
		return sqlSessionTemplate.selectOne("application.getNodeDisplayByAppName",paraMap);
	}

	@Override
	public void updateAppVersion(Map<String, Object> map) {
		sqlSessionTemplate.update("application.updateAppVersion",map);
	}

}