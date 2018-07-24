package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;

public interface IPackageDao {

	boolean savePackage(Map<String,Object> params);
	Page listPackagesByPage(Map<String, Object> condition, int pageNum, int pageSize);
	Map<String,Object> findPackageById(String id);
	boolean deletePackage(String id);
	Map<String, Object> findPackageByPath(String resourcePath);
	List<Map<String, Object>> exportPackage(List<String> ids);
	Map<String, Object> findPackageByName(String name);
}
