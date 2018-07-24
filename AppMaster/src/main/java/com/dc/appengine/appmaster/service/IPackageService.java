package com.dc.appengine.appmaster.service;

import java.io.InputStream;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;

public interface IPackageService {

	boolean uploadFile(InputStream input,String uuid,String fileName);
	String downloadFile(String id,String resourceParent);
	String savePackage(Map<String,Object> param);
	Page listPackagesByPage(Map<String,Object> condition,int pageNum,int pageSize);
	String deletePackage(String id);
	String getPackageTree(String resourcePath);
	String exportPackage(String ids);
	String importPackage(String packageList,String zip,String userId);
	Map<String, Object> findPackageByName(String fileName);
	Map<String,Object> findPackageById(String id);
	boolean deletePackageOnly(String id);
	Map<String, Object> getWorkpiece(String resourcePath);
	String checkPackage(Map<String,Object> record,String fileType);
}
