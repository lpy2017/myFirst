package com.dc.appengine.appsvn.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dc.appengine.appsvn.dao.IRegistryDao;

@Component("registryDao")
public class RegistryDao  implements IRegistryDao {

	private final Logger log = LoggerFactory.getLogger(RegistryDao.class);
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> checkResource(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("registry.checkResource",param);
		if(obj==null){
			return null;
		}
		return (Map<String, Object>)obj;
	}

	@Override
	public void addResource(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.insert("registry.addResource" , param);
		param.put("ID",obj);
	}

	@Override
	public void updateResource(Map<String,Object> param){
		sqlSessionTemplate.update("registry.updateResourceInfo",param);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> checkVersion(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("registry.checkVersion" , param);
		if(obj==null){
			return null;
		}
		
		return (Map<String,Object>)obj;
	}
	@Override
	public Map<String, Object> checkFtpVersion(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("registry.checkFtpVersion" , param);
		if(obj==null){
			return null;
		}
		
		return (Map<String,Object>)obj;
	}
	@Override
	public void addVersion(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.insert("registry.addVersion" , param);
		param.put("ID",obj);
	}
	@Override
	public void addFtpVersion(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.insert("registry.addFtpVersion" , param);
		param.put("ID",obj);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getImagesForUser(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getImagesForUser";
		if(registryId==2){
			dao="registry.getImagesForUserPublic";
		}else if(registryId==3){
			dao="registry.getResourcesForUser";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if ( obj == null) {
			return null;
		}
		return (List<String>)obj;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String,Object>> getTagsForUser(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getTagsForUser";
		if(registryId==2){
			dao="registry.getTagsForUserPublic";
		}else if(registryId==3){
			dao="registry.getResourceTagsForUser";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if ( obj == null) {
			return null;
		}
		return (List<Map<String,Object>>)obj;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getImageListByPage(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getImageListByPage";
		if(registryId==2){
			dao="registry.getImageListByPagePublic";
		}else if(registryId==3){
			dao="registry.getResourceListByPage";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if ( obj == null) {
			return null;
		}
		return (List<Map<String, Object>>)obj;
	}

	@Override
	public int getImageListNum(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getImageListNum";
		if(registryId==2){
			dao="registry.getImageListPublicNum";
		}else if(registryId==3){
			dao="registry.getResourceListNum";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getImageInfo(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getImageInfo";
		if(registryId==2){
			dao="registry.getPublicImageInfo";
		}else if(registryId==3){
			dao="registry.getResourceInfo";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if(obj==null){
			return null;
		}
		return (Map<String, Object>)obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTagsForUserByPage(Map<String, Object> param) {
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getTagsForUserByPage";
		if(registryId==2){
			dao="registry.getTagsPublicByPage";
		}else if(registryId==3){
			dao="registry.getResouceTagsByPage";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if ( obj == null) {
			return Collections.EMPTY_LIST;
		}
		return (List<Map<String,Object>>)obj;
	}

	@Override
	public int getTagsForUserCount(Map<String, Object> param) {
		
		int registryId=Integer.parseInt(param.get("registryId").toString());
		String dao="registry.getTagsForUserCount";
		if(registryId==2){
			dao="registry.getTagsPublicCount";
		}else if(registryId==3){
			dao="registry.getResourceTagsCount";
		}
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	@Override
	public Map<String,Object> checkVersionNum(int versionId){
		Object obj=sqlSessionTemplate.selectOne("registry.getVersionById",versionId);
		if(obj==null){
			return null;
		}
		return (Map<String,Object>)obj;
	}

	@Override
	public void deleteResource(int resourceId) {
		sqlSessionTemplate.delete("registry.delResource",resourceId);
		
	}

	@Override
	public void deleteVersion(int versionId) {
		sqlSessionTemplate.delete("registry.delVersion",versionId);
		
	}

	@Override
	public boolean checkResourcePermission(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("registry.checkResourcePermission",param);
		if(obj==null ){
			return false;
		}
		int count=(Integer)obj;
		if(count ==1){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkVersionPermission(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("registry.checkVersionPermission",param);
		if(obj==null ){
			return false;
		}
		int count=(Integer)obj;
		if(count ==1){
			return true;
		}
		return false;
	}

	@Override
	public void updateVersion(Map<String, Object> param) {
		sqlSessionTemplate.update("registry.updateVersionInfo",param);
	}

	@Override
	public String getResourceDescription(String resourceName) {
		Object obj=sqlSessionTemplate.selectOne("registry.getImageDescription",resourceName);
		if(obj==null){
			return null;
		}
		return  obj.toString();
	}

	@Override
	public Map<String, Object> getVersionInfo(Map<String, Object> param) {
		String dao="registry.getVersionInfo";
		Object obj=sqlSessionTemplate.selectOne(dao,param);
		if(obj==null){
			return null;
		}
		return (Map<String, Object>)obj;
	
	}

}
