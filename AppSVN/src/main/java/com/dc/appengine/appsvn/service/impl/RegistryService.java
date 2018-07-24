package com.dc.appengine.appsvn.service.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.dc.appengine.appsvn.dao.IRegistryDao;
import com.dc.appengine.appsvn.service.IRegistryService;
import com.dc.appengine.appsvn.utils.registry.Constant;
import com.dc.appengine.appsvn.utils.registry.RegistryClient;
@Service("registryService")
public class RegistryService implements IRegistryService {
	/**
	 * 	privateRegistry_url=registry.paas
		privateRegistry_port=443
		privateRegistry_nginx_port=6000
		privateRegistry_user=admin
		privateRegistry_password=123456
	 * 
	 * */
	@Value("${registry.privateRegistry_url}")
	private  String privateRegistryUrl;
	@Value("${registry.privateRegistry_port}")
	private  String privateRegistryPort;
	@Value("${registry.privateRegistry_nginx_port}")
	private  String privateRestPort;
	@Value("${registry.privateRegistry_user}")
	private String privateRegistry_user;
	@Value("${registry.privateRegistry_password}")
	private String privateRegistry_password;
	
	@Value("${registry.publicRegistry_url}")
	private  String publicRegistryUrl;
	@Value("${registry.publicRegistry_port}")
	private  String publicRegistryPort;
	@Value("${registry.publicRegistry_nginx_port}")
	private  String publicRestPort;
	@Value("${registry.publicRegistry_user}")
	private String publicRegistry_user;
	@Value("${registry.publicRegistry_password}")
	private String publicRegistry_password;
	
	@Value("${registry.ftpRegistry_url}")
	private String ftpRegistry_url;
	@Value("${registry.ftpRegistry_port}")
	private String ftpRegistry_port;
	@Value("${registry.ftpRegistry_user}")
	private String ftpRegistry_user;
	@Value("${registry.ftpRegistry_password}")
	private String ftpRegistry_password;

	@Resource
	private IRegistryDao registryDao;
	@Override
	public List<Map<String, Object>> getAllRegistrys() {
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		list.add(getRegistryById(1));
		list.add(getRegistryById(2));
		list.add(getRegistryById(3));
		return list;
	}
	@Override
	public Map<String, Object> getRegistryById(int id) {
		Map<String,Object> registry = new HashMap<String,Object>();
		registry.put("id", id);
		StringBuilder sb= new StringBuilder();
		
		if(id==1){
			//private
			sb.append("repository://");
			registry.put("registry_name","private_registry");
			registry.put("domain",privateRegistryUrl==null?"registry.paas":privateRegistryUrl);
			registry.put("registry_port",privateRegistryPort==null?"443":privateRegistryPort);
			registry.put("nginx_port",privateRestPort==null?"6000":privateRestPort);
			registry.put("userName",privateRegistry_user);
			registry.put("password",privateRegistry_password);
			sb.append(privateRegistry_user);
			sb.append(":");
			sb.append(privateRegistry_password);
			sb.append("@");
			sb.append(privateRegistryUrl);
		}else if (id==2){
			//public
			sb.append("repository://");
			registry.put("registry_name","public_registry");
			registry.put("domain",publicRegistryUrl==null?"registry.paas":publicRegistryUrl);
			registry.put("registry_port",publicRegistryPort==null?"443":publicRegistryPort);
			registry.put("nginx_port",publicRestPort==null?"6000":publicRestPort);
			registry.put("userName",publicRegistry_user);
			registry.put("password",publicRegistry_password);
			sb.append(publicRegistry_user);
			sb.append(":");
			sb.append(publicRegistry_password);
			sb.append("@");
			sb.append(publicRegistryUrl);
			
		}else if(id==3){
			sb.append("ftp://");
			registry.put("registry_name","ftp_registry");
			registry.put("domain",ftpRegistry_url==null?"registry.paas":ftpRegistry_url);
			registry.put("registry_port",ftpRegistry_port==null?"21":ftpRegistry_port);
			registry.put("nginx_port",null);
			registry.put("userName",ftpRegistry_user);
			registry.put("password",ftpRegistry_password);
			sb.append(ftpRegistry_user);
			sb.append(":");
			sb.append(ftpRegistry_password);
			sb.append("@");
			sb.append(ftpRegistry_url);
		}else{
			return null;
		}
		registry.put("url", sb.toString());
		return registry;
	}
	@Override
	public Map<String, Object> getRegistryByDomain(String domain, String registryport) {
		if(domain==null || registryport==null || "".equals(domain) ||"".equals(registryport)){
			return null;
		}
		int registryId=1;
		if(domain.equals(privateRegistryUrl) && registryport.equals(privateRegistryPort)){
			registryId=1;
		}else if(domain.equals(publicRegistryUrl) && registryport.equals(publicRegistryPort)){
			registryId=2;
		}else{
			registryId=3;
		}
		return getRegistryById(registryId);
	}
	@Override
	public Map<String, Object> checkResource(String userId, String resourceName) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("resourceName", resourceName);
		return registryDao.checkResource(param);
	}
	@Override
	public int saveResource(String userId, String resourceName,boolean isDocker,String description) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userId);
		param.put("resourceName", resourceName);
		param.put("isDockerResource",isDocker?1:0);
		param.put("description", description);
		registryDao.addResource(param);
		return Integer.valueOf(param.get("ID").toString());
	}
	@Override
	public void updateResource(int resourceId,String description){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("resourceId", resourceId);
		param.put("description", description);
		registryDao.updateResource(param);
	}
	@Override
	public Map<String, Object> checkVersion( String imageName, String imageTag) {
		if(imageTag==null || "".equals(imageTag)){
			imageTag="latest";
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("imageName", imageName);
		param.put("imageTag",imageTag);
		return registryDao.checkVersion(param);
	}
	@Override
	public Map<String, Object> checkFtpVersion(String versionName, String packagePath ,int resourceId) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("packagePath",packagePath );
		param.put("versionName", versionName);
		param.put("resourceId", resourceId);
		return registryDao.checkFtpVersion(param);
	}
	@Override
	public int saveVersion(int resourceId, String imageName, String imageTag, int registryId,Map<String,Object> otherInfo) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.putAll(otherInfo);
		param.put("resourceId", resourceId);
		param.put("imageName",imageName );
		param.put("imageTag",imageTag );
		param.put("registryId",registryId );
		registryDao.addVersion(param);
		return Integer.valueOf(param.get("ID").toString());
		
	}
	public int saveFtpVersion(int resourceId,String packagePath,int registryId,Map<String,Object> otherInfo){
		Map<String,Object> param=new HashMap<String,Object>();
		param.putAll(otherInfo);
		param.put("resourceId", resourceId);
		param.put("packagePath", packagePath);
		param.put("registryId", registryId);
		registryDao.addFtpVersion(param);
		return Integer.valueOf(param.get("ID").toString());
	}
	@Override
	public List<String> getImagesForUser(String userIds,int registryId) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId", userIds.split(","));
		param.put("registryId", registryId);
		return registryDao.getImagesForUser(param);
	}
	@Override
	public List<Map<String,Object>> getTagsForUser(int registryId,String resourceName) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("registryId", registryId);
		param.put("resourceName", resourceName);
		return registryDao.getTagsForUser(param);
	}
	@Override
	public Map<String, Object> getImageForUser(Map<String, Object> param) {
		List<Map<String,Object>> rows=registryDao.getImageListByPage(param);
		for(Map<String,Object> unit:rows){
			String timeStamp=unit.get("last_time").toString().replace(".0", "");
//			SimpleDateFormat format= new SimpleDateFormat("yyyy-mm-dd HH:ii:ss");
//			String newtime=format.format(new Date(timeStamp));
			unit.put("last_time", timeStamp);
		}
		int total=registryDao.getImageListNum(param);
		Map<String,Object> result= new HashMap<String,Object>();
		result.put("total", total);
		result.put("rows", rows);
		return result;
	}
	
	@Override
	public Map<String, Object> getResourceInfo( int registryId, String resourceName) {
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("registryId", registryId);
		param.put("resourceName", resourceName);
		param=registryDao.getImageInfo(param);
		if(param==null){
			return null;
		}
		if(param.containsKey("last_time")){
			String newTime=param.get("last_time").toString().replace(".0", "");
			param.put("last_time", newTime);
		}
		return param;

	}
	@Override
	public Map<String, Object> getResourceTagListByPage( int registryId, String resourceName,int pageSize,int pageNum,String keyword) {
		if(pageNum==0){
			pageNum=1;
		}
		int startNum=pageSize*(pageNum-1);
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("registryId",registryId);
		if(registryId==3){
			param.put("resourceName",resourceName);
		}else{
			param.put("imageName",resourceName);
		}
		param.put("keyword", keyword);
		int total=registryDao.getTagsForUserCount(param);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> rows= Collections.EMPTY_LIST;
		if(total!=0){
			param.put("pageSize",pageSize);
			param.put("startNum",startNum);
			param.put("pageNum",pageNum);
			rows=registryDao.getTagsForUserByPage(param);
			for(Map<String,Object> unit :rows){
				if(unit.containsKey("createDate")){
					String newTime=unit.get("createDate").toString().replace(".0", "");
					unit.put("createDate", newTime);
				}
			}
		}
		param.remove("startNum");
		param.remove("userId");
		param.put("total", total);
		param.put("rows", rows);
		return param;
	}
	@Override
	public void deleteVersion(int versionId) {
		Map<String, Object> result=registryDao.checkVersionNum(versionId);
		if(result==null){
			return ;
		}
		int registryId = Integer.valueOf(result.get("registryId").toString());
		String repository = (String) result.get("imageName");
		String tag = (String) result.get("tag");
		if(tag!=null && !"".equals(tag.trim())){
			//证明是docker资源 删除镜像
			Map<String, Object> registry = getRegistryById(registryId);
			String domain = registry.get("domain").toString();
			String port = String.valueOf(registry.get("registry_port"));
			String aPort = String.valueOf(registry.get("nginx_port"));

			RegistryClient registryClient = new RegistryClient(domain, port, aPort);
			registryClient.deleteImageByTag(Constant.catalogUser,
					Constant.catalogPassword, repository, tag);

		}else{
			//删除ftp上的资源
		}
		
		int remainVersion=Integer.parseInt(result.get("num").toString());
		//删除version
		registryDao.deleteVersion(versionId);
		if(remainVersion==0 || remainVersion==1){
			//删除resource
			int resourceId=Integer.parseInt(result.get("resourceId").toString());
			registryDao.deleteResource(resourceId);
		}
	}
	@Override
	public boolean hasPermission(String userIds, int resourceId, int versionId) {
		for(String id:userIds.split(",")){
			if("1".equals(id)){
				return true;
			}
			Map<String,Object> param= new HashMap<String,Object>();
			param.put("userId" ,id);
			boolean temp=false;
			if(resourceId!=0){
				//check resource
				param.put("resourceId",resourceId);
				temp= registryDao.checkResourcePermission(param);
			}
			if(versionId !=0){
				//check version
				param.put("versionId",versionId);
				temp= registryDao.checkVersionPermission(param);
			}
			if(temp){
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean updateVersionInfo(Map<String, Object> param) {
		registryDao.updateVersion(param);
		return true;
	}
	@Override
	public String getImageDescription(String imageName) {
		if(imageName.contains(":")){
			imageName=imageName.split(":")[0];
		}
		String resourceName=imageName.replace("/", "_");
		return registryDao.getResourceDescription(resourceName);
	}
	
	@Override
	public Map<String, Object> getResourceVersionInfo( int versionId) {
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId", versionId);
		param=registryDao.getVersionInfo(param);
		if(param==null){
			return null;
		}else{
			Long regestryId=(Long)param.get("REGISTRY_ID");
			Map<String, Object> map = getRegistryById(Integer.valueOf(""+regestryId));
			String url=map.get("url").toString();
			String registryPort=map.get("registry_port").toString();
			if(regestryId==3){
				url+=":"+registryPort+param.get("PACKAGE_PATH");
			}else{
				url+=":"+registryPort+"/"+param.get("IMAGE_NAME")+":"+param.get("IMAGE_TAG");
			}
			param.put("url", url);
		}
		return param;

	}
	public static void main(String[] args) {
		IRegistryService service = (IRegistryService) new ClassPathXmlApplicationContext("applicationContext.xml").getBean("registryService");
		System.out.println(service.getResourceVersionInfo(1));
	}
}
