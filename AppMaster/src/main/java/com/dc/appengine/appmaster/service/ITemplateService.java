package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;

public interface ITemplateService {

	public Page listDeployedTemplate(int pageSize, int pageNum, long userId,String key);
	public String getBlueInstanceDetail(int blueInstanceId);
	public String operateBlueInstance(String blueInstanceId,String type);
	public String test();
	public String saveSnapshot(int blueInstanceId,long userId);
	public String listSnapshots(int pageSize,int pageNum,long userId,int blueInstanceId);
	public String deleteSnapshot(String snapId);
	public String recoverySnapshot(String snapId);
	public String saveSnapshotOfApp(int appId,long userId,String snapshotName);
	public String saveSnapshotOfBlueInstance(int blueInstanceId, long userId);
	public List<Map<String,Object>> getSnapshotInsInfo(String snapId, long appId);
	public List<Map<String,Object>> getSnapshotRollResultInfo(String snapId, long appId);
	public String jsonToCorrectJson(String json,long appId);
	public void saveUpdateInfo(Map<String, Object> m);
	public String saveSnapshotOfInstance(String instanceId);
	public List getFlowIdByBlueInstanceId(String blueprintInstanceId);
}
