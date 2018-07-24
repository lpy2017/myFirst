package com.dc.appengine.appmaster.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.RCDao;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Service("rcService")
public class RCService {
	@Autowired
	@Qualifier("rcDao")
	RCDao rcdao;

	public void updateAppScalableStatus(long appId, int status) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("status", status);
		rcdao.updateAppScalableStatus(map);
	}

	public List<Map<String,Object>> getInstanceOfAppAndVersion(long appId, String versionId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("versionId", versionId);
		return rcdao.getInstanceOfAppAndVersion(map);
	}

	public Object getAppVersionListByAppId(long appId) {
		// TODO Auto-generated method stub
		return rcdao.getAppVersionListByAppId(appId);
	}
	
	public List<Map<String,Object>> getMasterApplications(){
		return rcdao.getMasterApplications();
	}

	public Object getMasterInstanceByApplicationId(long appId) {
		// TODO Auto-generated method stub
		return rcdao.getMasterInstanceByApplicationId(appId);
	}

	public Map<String,Object> getInsNumByAppIdAndResId(long appId, String versionId) {
		// TODO Auto-generated method stub
		return rcdao.getInsNumByAppIdAndResId(appId,versionId);
	}

	public String maintainAppInstances(long appId, String resourceVersionId,
			int targetCount) {
		// TODO Auto-generated method stub
		rcdao.maintainAppInstances(appId,resourceVersionId,targetCount);
		return MessageHelper.wrap("result",true,"message","保存目标实例数成功！");
	}
}
