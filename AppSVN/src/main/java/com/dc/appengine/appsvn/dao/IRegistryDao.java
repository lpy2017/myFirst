package com.dc.appengine.appsvn.dao;

import java.util.List;
import java.util.Map;

public interface IRegistryDao {
	public Map<String ,Object> checkResource(Map<String,Object> param);
	public void addResource(Map<String,Object> param);
	public void updateResource(Map<String,Object> param);
	public Map<String,Object> checkVersion(Map<String,Object> param);
	public Map<String,Object> checkFtpVersion(Map<String,Object> param);
	public void addVersion(Map<String,Object> param);
	public void addFtpVersion(Map<String,Object> param);
	public List<String> getImagesForUser(Map<String,Object> param);
	public List<Map<String,Object>> getTagsForUser(Map<String,Object> param);
	public List<Map<String,Object>> getImageListByPage(Map<String,Object> param );
	public int getImageListNum(Map<String,Object> param);
	public Map<String,Object> getImageInfo(Map<String,Object> param);
	public List<Map<String,Object>> getTagsForUserByPage(Map<String,Object> param);
	public int getTagsForUserCount(Map<String,Object> param);
	public  Map<String,Object>checkVersionNum(int versionId);
	public void deleteResource(int resourceId);
	public void deleteVersion(int versionId);
	public boolean checkResourcePermission(Map<String,Object> param);
	public boolean checkVersionPermission(Map<String,Object> param);
	public void updateVersion(Map<String,Object> param);
	public String getResourceDescription(String resourceName);
	public Map<String,Object> getVersionInfo(Map<String,Object> param);
}
