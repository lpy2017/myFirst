package com.dc.appengine.appsvn.service;

import java.util.List;
import java.util.Map;

public interface IRegistryService {
	/**
	 * 显示所有仓库的相信信息
	 * @return
	 */
	public List<Map<String, Object>> getAllRegistrys();
	/**
	 * 根据类型获取仓库的详细信息  
	 * @param id   1表示私有仓库  2表示公有仓库
	 * @return
	 */
	public Map<String,Object> getRegistryById(int id);
	
	public String getImageDescription(String imageName);
	
	/**
	 * 根据镜像名称获取资源是否被注册
	 * @param userId
	 * @param resourceName
	 * @return
	 */
	public Map<String ,Object> checkResource(String userId ,String resourceName);
	/**
	 * 保存resource表  
	 * @param userId
	 * @param resourceName
	 * @param description
	 * @return
	 */
	public int saveResource(String userId,String resourceName,boolean isDockerResource,String description);
	/**
	 * 更新资源描述
	 * @param resourceId
	 * @param description
	 */
	public void updateResource(int resourceId,String description);
	/**
	 * 查看版本是否被注册
	 * @param userId
	 * @param imageName
	 * @param imageTag
	 * @return
	 */
	public Map<String,Object> checkVersion( String imageName,String imageTag);
	public Map<String,Object> checkFtpVersion(String versionName, String packagePath,int resourceId);
	/**
	 * 保存版本信息
	 * @param resourceId
	 * @param imageName
	 * @param imageTag
	 * @param registryId
	 * @param otherInfo
	 * @return
	 */
	public int saveVersion(int resourceId ,String  imageName ,String imageTag ,int registryId,Map<String,Object> otherInfo);
	public int saveFtpVersion(int resourceId ,String  packagePath,int registryId,Map<String,Object> otherInfo);
	/**
	 * 根据解析后的仓库信息 （域名 端口 ） 获取仓库的其他信息
	 * @param domain
	 * @param registryport
	 * @return
	 */
	public Map<String,Object> getRegistryByDomain(String domain,String registryport);
	
	/**
	 * 分页获取镜像列表
	 * @param param {userId,keyword,pageSize,pageNum}
	 * @return
	 */
	public Map<String,Object> getImageForUser(Map<String,Object> param);
	
	/**
	 * 获取用户所有注册镜像信息
	 * @param userId
	 * @param registryId
	 * @return
	 */
	public List<String> getImagesForUser(String userId,int registryId);
	
	/**
	 * 获取镜像所有版本信息
	 * @param userId
	 * @param registryId
	 * @param imageName
	 * @return
	 */
	public List<Map<String,Object>> getTagsForUser(int registryId,String imageName);
	
	/**
	 * 获取镜像基本信息
	 * @param userId
	 * @param registryId
	 * @param imageName
	 * @return
	 */
	public Map<String,Object> getResourceInfo( int registryId,String imageName);
	
	/**
	 * 查看该用户是否对该资源或版本有权限 没有resourceId  或versionId可用0占位
	 * @param userId
	 * @param resourceId
	 * @param versionId
	 * @return
	 */
	public boolean hasPermission(String userIds,int resourceId,int versionId);
	
	/**
	 * 分页获取镜像所有版本信息
	 * @param userId
	 * @param registryId
	 * @param imageName
	 * @param pageSize
	 * @param pageNum
	 * @param keyword
	 * @return
	 */
	public Map<String,Object> getResourceTagListByPage(int registryId,String imageName,int pageSize,int pageNum,String keyword);
	
	/**
	 * 删除一个已注册版本
	 * @param versionId
	 */
	public void deleteVersion(int versionId);
	
	/**
	 * 更新版本基本信息 
	 * @param param {versionId,startScript,startPort,deploy_timeout,start_timeout,stop_timeout,destroy_timeout}
	 * @return
	 */
	public boolean updateVersionInfo(Map<String,Object> param);
	
	public Map<String, Object> getResourceVersionInfo( int versionId);
}
