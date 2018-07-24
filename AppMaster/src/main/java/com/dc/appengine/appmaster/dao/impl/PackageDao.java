package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IPackageDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Component("packageDao")
public class PackageDao extends PageQuery implements IPackageDao{

	@Override
	public boolean savePackage(Map<String, Object> params) {
		int count =  sqlSessionTemplate.insert("package.savePackage", params);
		if(count ==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Page listPackagesByPage(Map<String, Object> condition, int pageNum,
			int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "package.listPackagesByPage");
	}

	@Override
	public Map<String, Object> findPackageById(String id) {
		return sqlSessionTemplate.selectOne("package.findPackageById", id);
	}

	@Override
	public boolean deletePackage(String id) {
		int count =  sqlSessionTemplate.delete("package.deletePackage", id);
		if(count ==1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Map<String, Object> findPackageByPath(String resourcePath) {
		return sqlSessionTemplate.selectOne("package.findPackageByPath", resourcePath);
	}

	@Override
	public List<Map<String, Object>> exportPackage(List<String> ids) {
		return sqlSessionTemplate.selectList("package.exportPackage",ids);
	}
	@Override
	public Map<String, Object> findPackageByName(String name) {
		return sqlSessionTemplate.selectOne("package.findPackageByName",name);
	}

}
