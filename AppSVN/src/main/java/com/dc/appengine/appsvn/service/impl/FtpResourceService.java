package com.dc.appengine.appsvn.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appsvn.dao.impl.FtpDao;
import com.dc.appengine.appsvn.service.IRegistryService;
import com.dc.appengine.appsvn.utils.ConfigHelper;
import com.dc.appengine.appsvn.utils.FileUtil;
import com.dc.appengine.appsvn.utils.FileWorker;
import com.dc.appengine.appsvn.utils.MessageUtil;
import com.dc.appengine.appsvn.utils.structurecheck.CheckTopo;
import com.dc.appengine.appsvn.ws.client.MasterClient;

@Service("ftpService")
public class FtpResourceService {
	private static final Logger log = LoggerFactory.getLogger(FtpResourceService.class);

	@Resource
	@Qualifier("ftpsourceDao")
	private FtpDao ftpDao;

	@Autowired
	@Qualifier("registryService")
	private IRegistryService registryService;

	/**
	 * 向master 获取子用户id列表
	 * 
	 * @param userId
	 * @return
	 */
	public List<Long> getUserIds(String userId) {
		List<Long> list = new ArrayList<Long>();
		MasterClient client = new MasterClient();
		String resultStr = client.getSonIds(userId);
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {
		});

		for (long unit : map.keySet()) {
			list.add(unit);
		}
		return list;
	}

	/**
	 * 向master查询用户id
	 * 
	 * @param userName
	 * @return
	 */
	public int getUserIdByName(String userName) {
		MasterClient client = new MasterClient();
		String userId = client.getUserId(userName);
		if (userId == null) {
			return 1;
		} else {
			return Integer.valueOf(userId);
		}

	}

	/**
	 * 检查资源名是否已经被注册
	 * 
	 * @param resourceName
	 * @param appId
	 * @param userId
	 * @return
	 */
	public int checkResource(String resourceName, int appId, int userId) {
		Map<String, Object> ret = null;
		int resourceId = 0;
		if (appId == 0 || userId == 0) {
			ret = ftpDao.checkPublicResource(resourceName);
		} else {
			ret = ftpDao.checkPrivateResource(resourceName, userId + "", appId + "");
		}
		if (ret != null && ret.containsKey("ID")) {
			resourceId = Integer.valueOf(ret.get("ID").toString());
		}
		return resourceId;
	}

	/**
	 * 检查zip包名是否被注册
	 * 
	 * @param packagePath
	 * @return
	 */
	public int checkPackage(String packagePath) {
		return ftpDao.checkPackage(packagePath);
	}

	/**
	 * 获取公有资源列表
	 * 
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public String getPublicResourceList(String keyword, int pageNum, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int num = ftpDao.getPublicResourceNum(keyword);
		result.put("total", num);
		if (num == 0) {
			result.put("rows", Collections.emptyList());
		} else {
			if (pageNum == 0) {
				pageNum = 1;
			}
			int startNum = pageSize * (pageNum - 1);
			List<Map<String, Object>> rows = ftpDao.getPublicResourceList(keyword, startNum, pageSize);
			result.put("rows", rows);
		}
		result.put("keyword", keyword);
		result.put("pageNum", pageNum);
		result.put("pageSize", pageSize);
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * 获取公有资源数量
	 * 
	 * @param keyword
	 * @return
	 */
	public int getPublicResourceNum(String keyword) {
		return ftpDao.getPublicResourceNum(keyword);
	}

	/**
	 * 获取用户私有资源（topo）
	 * 
	 * @param userId
	 * @param appId
	 * @param keyword
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public String getUserResourceList(String userId, String appId, String keyword, int pageNum, int pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		int num = ftpDao.getUserResourceNum(userId, appId, keyword);
		result.put("total", num);
		if (num == 0) {
			result.put("rows", Collections.emptyList());
		} else {
			if (pageNum == 0) {
				pageNum = 1;
			}
			int startNum = pageSize * (pageNum - 1);
			List<Map<String, Object>> rows = ftpDao.getUserResourceList(userId, appId, keyword, startNum, pageSize);
			result.put("rows", rows);
		}
		result.put("keyword", keyword);
		result.put("pageNum", pageNum);
		result.put("pageSize", pageSize);
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * 获取用户资源数量
	 * 
	 * @param userId
	 * @param appId
	 * @param keyword
	 * @return
	 */
	public int getUserResourceNum(String userId, String appId, String keyword) {
		return ftpDao.getUserResourceNum(userId, appId, keyword);
	}

	/**
	 * @param resourceName
	 * @param userId
	 * @param appId
	 * @param packagePath
	 *            包路径
	 * @param versionName
	 *            版本名称
	 * @param description
	 *            版本描述
	 * @param obtheParam(deployTimeout,startTimeout,stopTimeout,destroyTimeout,
	 *            startPort,command)
	 * @return 0 失败 -1 包已注册 -2 版本已注册 **** 在第一条数据添加之前完成上传
	 */
	public int saveResourceVersion(String resourceName, int userId, int appId, String resourceDescription,
			String packagePath, String versionName, String description, Map<String, Object> obtheParam) {
		int resourceId = checkResource(resourceName, appId, userId);
		int checkPackage = ftpDao.checkPackage(packagePath);
		if (checkPackage != 0) {
			return -1;
		}
		if (resourceId == 0) {
			resourceId = ftpDao.addResource(resourceName, userId, appId, resourceDescription);
		}
		if (resourceId == 0) { // resource 添加失败
			return 0;
		}
		// check version
		int checkVersion = ftpDao.checkVersion(versionName, resourceId);
		if (checkVersion == 0) {
			return ftpDao.addVersion(resourceId, packagePath, versionName, description, obtheParam);
		} else {
			return -2;
		}
	}

	/**
	 * 保存资源 1.检查资源包结构 2.检查数据库是否有注册冲突 3.上传资源包到ftp
	 * 如果ftp与svn在同一主机或共享内存则是直接从工作空间直接拷贝到ftp指定目录 所需配置{ftphome} 4.注册资源信息到数据库
	 * 
	 * @param resourceName
	 * @param userName
	 * @param appId
	 * @param resourceDescription
	 * @param workId
	 * @param versionName
	 * @param description
	 * @param obtheParam
	 * @param isPublic
	 * @return
	 */
	public String saveResourceVersion(String resourceName, String userName, int appId, String resourceDescription,
			String workId, String versionName, String description, Map<String, Object> obtheParam, boolean isPublic) {
		int userId = 0;
		int resourceId = 0;
		String localPath = ConfigHelper.getValue("workPath");
		String fileName = null;
		boolean success = false;
		String msg;
		save: {
			// 本地文件检查
			fileName = FileWorker.getInstance().getWorkSource(workId);
			if (fileName == null) {
				msg = "file upload error,work dir is not found";
				break save;
			}
			String ftpFolder = "/" + userName;
			if (isPublic) {
				appId = 0;
				userId = 0;
				ftpFolder = "/public";
			} else {
				if ("admin".equals(userName)) {
					ftpFolder = "";
				} else {
					ftpFolder = "/" + userName;
				}
				userId = getUserIdByName(userName);
			}

			String sourcePath = ftpFolder + "/" + fileName;
			String packagePath = sourcePath;
			obtheParam.put("sourcePath", sourcePath);
			// 拓扑包结构检测
			Map<String, Object> payload = new HashMap<String, Object>();
			if (!isPublic) {
				String loP = localPath + "/" + workId + "/" + fileName;
				boolean checkStructureOk = CheckTopo.check(loP, payload);
				if (!checkStructureOk) {
					msg = payload.get("errorMsg").toString();
					break save;
				}
			}

			// 拓扑包制作
			// String zipname = null;
			// if (fileName.endsWith(".jar") && !isPublic) {
			// // 制作拓扑包
			// String sName = fileName.substring(0, fileName.lastIndexOf("."));
			// zipname = "topo_" + sName + "." + versionName + ".zip";
			// packagePath = ftpFolder + "/" + zipname;
			// FileWorker.getInstance().mkZip(workId, "topo", zipname);
			// }
			// 检查包是否被注册过

			int preg = checkPackage(packagePath);
			if (preg != 0) {
				log.error("package has bean registed");
				msg = "package has bean registed";
				break save;
			}

			boolean uploadOK = false;
			resourceId = checkResource(resourceName, appId, userId);

			if (resourceId == 0) {
				boolean uploadResult = uploadResource(workId, ftpFolder);
				if (!uploadResult) {
					msg = "package upload to ftp error ";
					break save;
				}
				uploadOK = true;
				resourceId = ftpDao.addResource(resourceName, userId, appId, resourceDescription);
			}
			if (resourceId == 0) { // resource 添加失败
				msg = "add resource error";
				break save;
			}
			// check version
			int checkVersion = ftpDao.checkVersion(versionName, resourceId);
			int vid = 0;
			if (checkVersion == 0) {
				if (!uploadOK) {
					boolean uploadResult = uploadResource(workId, ftpFolder);
					if (!uploadResult) {
						msg = "package upload to ftp error";
						break save;
					}
				}
				vid = ftpDao.addVersion(resourceId, packagePath, versionName, description, obtheParam);
				msg = vid + "";
			} else {
				msg = "this version name has bean registed";
				break save;
			}
			if (!isPublic && payload.get("configs") != null && vid != 0) {
				// 导入配置
				String cId = ftpDao.addResourceConfig(payload.get("configs").toString());
				ftpDao.connectConfigToTopo(cId, vid);
			}
			success = true;
		}
		if (!success) {
			// roll back
			// 删除临时包
			File localDir = new File(localPath + "/" + workId);
			if (localDir.exists() && localDir.isDirectory()) {
				for (File file : localDir.listFiles()) {
					if (fileName != null && fileName.equals(file.getName())) {
						continue;
					}
					try {
						FileUtil.forceDelete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return MessageUtil.mkBooleanMessage(success, msg);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", success);
		result.put("resourceName", resourceName);
		result.put("tag", versionName);
		result.put("id", msg);
		result.put("resourceId", resourceId);

		return JSON.toJSONString(result);
	}

	/**
	 * 根据workId 上传workId里面的文件
	 * 
	 * @param workId
	 * @param ftpFolder
	 * @return
	 */
	public boolean uploadResource(String workId, String ftpFolder) {
		// 放到ftp上去
		Map<String, Object> registry = registryService.getRegistryById(3);

		return FileWorker.getInstance().uploadToftp(workId, registry.get("url").toString(), ftpFolder, true);

	}

	/**
	 * 获取资源所有版本的列表
	 * 
	 * @param resourceId
	 * @param keyword
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public String getVersionList(int resourceId, String keyword, int pageSize, int pageNum) {
		Map<String, Object> result = new HashMap<String, Object>();
		int num = ftpDao.getVersionNum(resourceId, keyword);
		result.put("total", num);
		if (num == 0) {
			result.put("rows", Collections.emptyList());
		} else {
			int startNum = (pageNum - 1) * pageSize;
			result.put("rows", ftpDao.getVersionList(resourceId, keyword, pageSize, startNum));
		}
		result.put("pageSize", pageSize);
		result.put("pageNum", pageNum);
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * 删除整个资源 以及下面的版本
	 * 
	 * @param resourceId
	 */
	public void deleteResource(int resourceId) {
		ftpDao.deleteResource(resourceId);
	}

	/*
	 * 删除版本
	 */
	public void deleteVersion(int versionId) {
		Map<String, Object> versionInfo = ftpDao.getVersionInfo(versionId);
		if (versionInfo != null) {
			if (versionInfo.containsKey("num")) {
				int vnum = Integer.valueOf(versionInfo.get("num").toString());
				if (vnum == 1) {
					log.debug("this is the last version,delete the resource");
					int resourceId = Integer.valueOf(versionInfo.get("resourceId").toString());
					deleteResource(resourceId);
				}
			}
			ftpDao.deleteVersion(versionId);
		}
	}

	/**
	 * 获取版本详细信息
	 * 
	 * @param versionId
	 * @return (id,resourceId,resourceName,sourcePath,num,last_time,packagePath,
	 *         startScript,startPort,deploy_timeot,stop_timeout,start_timeout,
	 *         destroy_timeout,REGISTRY_ID,url)
	 */
	public Map<String, Object> getResourceVersionInfo(int versionId) {

		Map<String, Object> param = ftpDao.getVersionInfo(versionId);
		if (param == null) {
			return null;
		} else {
			Long regestryId = (Long) param.get("REGISTRY_ID");
			if (regestryId == 3) {
				Map<String, Object> map = registryService.getRegistryById(3);
				String url = map.get("url") + "" + param.get("packagePath");
				param.put("url", url);
			}
		}
		return param;

	}

	public void updateVersionInfo(int versionId, String startScript, String startPort, int deployTimeout, int startTimeout,
			int stopTimeout, int destroyTimeout,String description) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		param.put("startScript", startScript);
		param.put("startPort", startPort);
		param.put("deployTimeout", deployTimeout);
		param.put("startTimeout", startTimeout);
		param.put("stopTimeout", stopTimeout);
		param.put("destroyTimeout", destroyTimeout);
		param.put("description", description);
		ftpDao.updateVersion(param);
	}

	/**
	 * 获取资源信息
	 * 
	 * @param resourceId
	 * @return
	 */
	public Map<String, Object> getResourceInfo(int resourceId) {
		return ftpDao.getResourceInfo(resourceId);
	}
}
