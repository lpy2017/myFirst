package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.impl.ConfigDao;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.utils.PatternUtil;
 

@Service("configservice")
public class ConfigsService {
	private Logger log = LoggerFactory.getLogger(ConfigsService.class);
	@Autowired
	@Qualifier("configDao")
	private ConfigDao configDao;
	
	@Autowired
	@Qualifier("applicationService")
	private IApplicationService appService;

//	@Autowired
//	@Qualifier("instanceService")
//	private InstanceService instanceServicce;

	/**
	 * 分页获取所有配置资源
	 * @param pageSize
	 * @param pageNum
	 * @param userIds
	 * @param type （default component） component template topo app
	 * @param keyword 搜索关键词
	 * @return
	 */
	public String getConfigByPage(int pageSize, int pageNum,String userIds,String type, String keyword) {
		int start = 0;
		if (pageNum != 0) {
			start = pageSize * (pageNum - 1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userIds.split(","));
		param.put("type", type);
		param.put("pageSize", pageSize);
		param.put("pageNum", pageNum);
		param.put("start", start);
		param.put("keyword", keyword);
		int num = configDao.getConfigNum(param);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (num != 0) {
			rows = configDao.getConfigByPage(param);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", rows);
		result.put("total", num);
		result.put("pageSize", pageSize);
		result.put("pageNum", pageNum);
		result.put("keyword", keyword);
		result.put("type", type);
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}
	//
	// 检查config名字是否被注册
	public boolean checkConfig(String configName,String type) {
		return configDao.checkName(configName,type);
	}
	/**
	 * 添加一份新配置
	 * @param configName
	 * @param type
	 * @param userId
	 * @param description
	 * @return
	 */
	public int addNewConfig(String configName, String type,String userId, String description) {
		return configDao.addNewConfig(configName, type,userId, description);
	}
	/**
	 * 根据当前配置 生成新的版本名称  v1 v2 ->v3
	 * @param configId
	 * @return
	 */
	public String generateNewVrsionName(int configId) {
		List<String> list = getVersionNames(configId);
		int newIndex = 1;
		if (list.size() != 0) {
			Collections.sort(list);
			String lastV = list.get(list.size() - 1);
			newIndex = Integer.valueOf(lastV.replace("v", "")) + 1;
		}
		return "v" + newIndex;
	}
	/**
	 * 注册一个新版本
	 * @param configId
	 * @param versionName
	 * @return
	 */
	public int addNewVersion(int configId, String versionName) {
		// check
		boolean c = configDao.checkVersionName(configId, versionName);
		if (c) {
			return 0;
		}
		// add
		return configDao.addConfigVersion(configId, versionName);
	}
	/**
	 * 获取配置的所有版本
	 * @param configId
	 * @return
	 */
	public List<Map<String, Object>> getVersionList(int configId) {
		List<Map<String, Object>> list = configDao.getVersionList(configId);
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}
	/**
	 * 根据配置资源的id获取所有版本信息（名称 v1 v2 v3）
	 * @param configId
	 * @return
	 */
	public List<String> getVersionNames(int configId) {
		List<String> list = new ArrayList<String>();
		List<Map<String, Object>> listM = getVersionList(configId);

		for (Map<String, Object> unit : listM) {
			String version = unit.get("version").toString();
			if (version == null) {
				continue;
			}
			list.add(version);
		}
		return list;
	}
	/**
	 * 删除一个配置资源 包括所有版本和详情
	 * @param configId
	 * @return
	 */
	public boolean delConfig(int configId) {
		// 遍历版本 逐个删除
		List<Map<String, Object>> list = getVersionList(configId);
		for (Map<String, Object> unit : list) {
			Object obj = unit.get("id");
			if (obj != null) {
				int versionId = Integer.valueOf(obj.toString());
				if (isVersionUsed(versionId)) {
					return false;
				}
			}
		}
		// 真正去删除版本
		for (Map<String, Object> unit : list) {
			Object obj = unit.get("id");
			if (obj != null) {
				int versionId = Integer.valueOf(obj.toString());
				delVersionById(versionId);
			}
		}
		// 删除config
		configDao.delConfig(configId);
		return true;

	}
	/**
	 * 删除一个版本的配置 私有
	 * @param versionId
	 * @return
	 */
	private boolean delVersionById(int versionId) {
		// 删除配置列表
		configDao.clearConfig(versionId);
		// 删除配置
		configDao.deleteVersion(versionId);
		return true;
	}
	
	
	 
	/**
	 * 删除一个版本配置 
	 * @param versionId
	 * expectNum 预期次数
	 * 0 直接删除配置
	 * 1 通过组件删配置
	 * @return
	 */
	public boolean delVersion(int versionId,int expectNum) {
		int useedNum=appService.getConfigUsedCount(versionId);
		if(expectNum != useedNum){
			return false;
		}
		// 检查是否为最后一个版本
		Map<String, Object> map = getVersionInfo(versionId);
		if(map == null){
			return true;
		}
		Object obj = map.get("configId");
		boolean lastVersion = false;
		int configId = 0;
		if (obj != null && !"".equals(obj.toString())) {
			configId = Integer.valueOf(obj.toString());
			List<?> vlist = getVersionList(configId);
			if (vlist != null && vlist.size() == 1) {
				lastVersion = true;
			}
		}
		delVersionById(versionId);
		if (lastVersion) {
			log.debug("this is the last version of config:" + map.get("name") + " ,del the config!");
			configDao.delConfig(configId);
		}

		return true;
	}
	/**
	 * 判断当前版本配置是否在被使用
	 * @param versionId
	 * @return
	 */
	public boolean isVersionUsed(int versionId) {
		int i = appService.getConfigUsedCount(versionId);
		return i>=1;
	}
	/**
	 * 根据配置版本id获取配置信息
	 * @param versionId
	 * @return
	 * @returnfield (id configId version last_update_time name)
	 */
	public Map<String, Object> getVersionInfo(int versionId) {
		return configDao.getVersionInfo(versionId);
	}
	/**
	 * 根据configId  vx 获取版本名称
	 * @param configId
	 * @param versionName
	 * @return
	 */
	public int getVersionIdByName(int configId, String versionName) {
		return configDao.getVersionIdByName(configId, versionName);
	}
	/**
	 * 根据配置名获取配置id
	 * @param name
	 * @return
	 */
	public int getConfigIdByName(String name) {
		return configDao.getconfigIdByName(name);
	}
	/**
	 * 根据配置名 配置版本  获取版本id
	 * @param configName
	 * @param versionName
	 * @return
	 */
	public int getVersionIdByName(String configName, String versionName) {
		int configId = configDao.getconfigIdByName(configName);
		return getVersionIdByName(configId, versionName);
	}
	/*public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ConfigsService service = (ConfigsService) ctx.getBean("configservice");
		int re=service.deepCopy(23, 1, null,"template");
		System.out.println(re);
	}*/
	/**
	 * 
	 * 申请配置接口 当遇到待解析的配置 处理 兼容方式
	 * {appName#appVersion#key1}  	获取指定的app的某个版本的信息（配置，环境变量）
	 * {appName#key1}				获取指定的app的当前版本的信息（配置，环境变量）
	 * {key1}						获取当前应用的信息（配置，环境变量）引用自身
	 * @param appName
	 * @param appVersion
	 * @return
	 */
	public String getConfig(String appName,String appVersion) {
		int configId=0;
		List<Map<String, Object>> generateList=new ArrayList<Map<String, Object>>();
//		if(isTopo(appName)){
//			//获取topo配置
//			 configId=(int)templateService.getTopoConfigId(appName,appVersion);
//			 Map<String, Object> param = new HashMap<String, Object>();
//			 param.put("versionId", configId);
//			 generateList  = configDao.getConfigList(param);
//			 param.put("total", generateList.size());
//			 param.put("rows", generateList);
//			 return JSON.toJSONString(param);
//		} 
//		long appId = appService.getIdByAppName(appName);
//		int appVersionId = (int) appService.getAppVersionId(appId,appVersion);
		
		configId=getConfigIdByAppVersionId(1);
		 
		// 获取应用信息
		
		if(configId==0){
			return "{\"result\":false,\"msg\":\"no config\"}";
		}
		Pattern p3 = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\#[V|v]([0-9]+)\\#([a-zA-Z0-9_]+)\\}");
		Pattern p2 = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}");
		//Pattern p1 = Pattern.compile("\\$\\{\\#([a-zA-Z0-9_]+)\\}");//防止用户自定义 ${}
		Pattern p1 = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");
		Map<String, Object> param = new HashMap<String, Object>();

		//param.put("configId", configId);
		param.put("versionId", configId);
		List<Map<String, Object>> list = configDao.getConfigList(param);
		
		 
		if (list != null && list.size() > 0) {
			for (Map<String, Object> unit : list) {
				Object typeObj = unit.get("type");
				String type="";
				if (typeObj != null) {
					type=typeObj.toString();
				}
				unit.put("type",type);
				// 需要解析的话不是显示 不需要id
				unit.remove("id");
				String key=unit.get("key").toString();
				String value = unit.get("value").toString();
				Matcher m0 = p1.matcher(key);
				if(m0.find()){
//					generateList.addAll(generateKeyPair(key,value,type ,appVersionId));
					unit.remove(key);
					continue;
				}
				generateList.add(unit);
				while (true) {
					Matcher m3 = p3.matcher(value);
					Matcher m2 = p2.matcher(value);
					Matcher m1 = p1.matcher(value);
					if (m3.find()) {
						String depAppName = m3.group(1);
						String depVersion = m3.group(2);
						String depKey = m3.group(3);
						long depAppId;
						int depVersionId = 0;
//						long depAppId = appService.getIdByAppName(depAppName);
//						int depVersionId = (int) appService.getAppVersionId(depAppId,depVersion);
						String temp = getOtherAppInfo(depVersionId, depKey);
						value = value.replaceFirst(p3.pattern(), temp);
					} else if (m2.find()) {
						String depAppName = m2.group(1);
						String depKey = m2.group(2);
						int depVersionId = getAppVersionNow(depAppName);
						String temp = getOtherAppInfo(depVersionId, depKey);
						value = value.replaceFirst(p2.pattern(), temp);
					} else if (m1.find()) {
						String depKey = m1.group(1);
						String temp = null;
//						= getOtherAppInfo(appVersionId, depKey);
						value = value.replaceFirst(p1.pattern(), temp);
					} else {
						break;
					}
				}
				unit.put("value", value);
			}

		}
		list.addAll(generateList);
		param.remove("versionId");
		param.put("total", generateList.size());
		param.put("rows", generateList);
		param.put("configId", null);
		Map<String, Object> info2 = configDao.getVersionInfo(configId); // 获取其他配置信息
																		// 待定
		param.putAll(info2);
		return JSON.toJSONString(param, SerializerFeature.WriteDateUseDateFormat);
	}

	private String getInstanceInfo(Instance instance,String key){
		if(instance==null){
			return null;
		}
		if(key.equals("MYID")){
			return instance.getId()+"";
		}
		if(key.equals("HOST_IP")){
			return instance.getIp();
		}
		if(key.equals("CONTAINER_IP")){
			return instance.getLxcIp();
		}
		if(key.equals("ACCESS_PORT")){
			return instance.getPort()+"";
		}
		return "";
	}
	/**
	 * 解析key
	 * @param key
	 * @param value
	 * @param type
	 * @param appVersionId
	 * @return
	 */

	//for parse MYID list  eg:(server.${MYID}=${HOST_IP}:8080:9090)
	public List<Map<String,Object>> generateKeyPair(String key,String value,String type,int appVersionId){
		List<Map<String,Object>>  result= new ArrayList<Map<String,Object>>();
		List<Instance> instances = null;
//		=instanceServicce.findOperationInstanceByVersionId(appVersionId);
		 
		if(instances==null || instances.size()==0){
			return result;
		}
		Pattern p1 = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");
		for(Instance instance:instances){
			Map<String,Object> map= new HashMap<String,Object>();
			map.put("type", type);
			Matcher mkey=p1.matcher(key);
			if(mkey.find()){
				String refkey=mkey.group(1);
				String refkeyResult=getInstanceInfo(instance,refkey);
				if(refkeyResult==null){
					continue;
				}
				String keyresult=key.replaceFirst(p1.pattern(), refkeyResult);	
				map.put("key",keyresult);
			}else{
				continue;
			}
			
			Matcher mvalue=p1.matcher(value);
			if(mvalue.find()){
				String refkey=mvalue.group(1);
				String refkeyResult=getInstanceInfo(instance,refkey);
				if(refkeyResult==null){
					refkeyResult="";
				}
				String valueResult=value.replaceFirst(p1.pattern(), refkeyResult);
				map.put("value",valueResult);
			}else{
				map.put("value",value);
			}
			
			result.add(map);
		}
		return result;
	}
	/**
	 * 根据当前的应用版本id获取配置
	 * @param appVersionId
	 * @return
	 */
	public int getConfigIdByAppVersionId(int appVersionId) {
//		return appService.getConfigIdByAppVersionId(appVersionId);
		return 0;
	}

	public int getAppVersionNow(String appName) {
//		return appService.getAppVersionNow(appName);
		return 0;
	}

	/**
	 * 深度拷贝
	 *	@param fromId
	 *	@param userId
	 * 	@param name  copy result name
	 * 	@param toType
	 *            component-> template ->app
	 * @return
	 */
	public int deepCopy(int fromId,int userId,String name, String toType) {
		if (toType == null || "".equals(toType)) {
			toType = "component";
		}
		//创建一份配置 app_configs-add(name) v1 
		String toname="";
		if(name==null || "".equals(name)){
			Map<String,Object> configInfo=configDao.getConfigInfoByVersionId(fromId);
			String fromname="";
			if(configInfo.get("name")==null){
				fromname=UUID.randomUUID().toString();
			}else{
				fromname=configInfo.get("name").toString();
			}
			toname= fromname;
		}else{
			toname=name;
		}
		
		int configId=addNewConfig(toname,toType,userId+"","");
		//添加版本
		String versionName=generateNewVrsionName(configId);
		int versionId=addNewVersion(configId,versionName);
		boolean success=clone(fromId, versionId);
		if(success){
			return versionId;
		}
		return 0;
	}

	public String getOtherAppInfo(int appVersionId, String key) {
		// 先获取环境变量
		Map<String, String> info = null; 
//				appService.getOtherAppInfo(appVersionId);
		if (info == null) {
			return "UNDEFINDED";
		}
		// 如果环境变量不存在此key获取配置
		if (info.containsKey(key)) {
			String temp = info.get(key);
			if (temp == null || "".equals(temp)) {
				return "";
			}
			return temp;
		} else {
			// 取配置
			int configId = getConfigIdByAppVersionId(appVersionId);

			try {
				String temp = configDao.getValue(key, configId);
				if (temp == null) {
					return "UNDEFINDED";
				}
				return temp;
			} catch (Exception e) {
				return "UNDEFINDED";
			}
		}
	}

	public List<Map<String, Object>> getConfigList(int versionId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		List<Map<String, Object>> list = configDao.getConfigList(param);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> unit : list) {
				Object type = unit.get("type");
				if (type == null) {
					unit.put("type", "");
				}
			}
		}
		return list;
	}

	public List<Map<String, Object>> getConfigList(String keyword, int versionId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		param.put("keyword", keyword);
		List<Map<String, Object>> list = configDao.getConfigList(param);
		return list;
	}
	/**
	 * 前台获取配置列表接口
	 * @param versionId
	 * @param keyword
	 * @return
	 */
	public String getConfigList(int versionId, String keyword) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		param.put("keyword", keyword);
		List<Map<String, Object>> list = configDao.getConfigList(param);
		// 需要遍历list查看是否有引用的配置

		if (list != null && list.size() > 0) {
			for (Map<String, Object> unit : list) {
				Object type = unit.get("type");
				if (type == null) {
					unit.put("type", "");
				}

			}

		}
		param.put("total", list.size());
		param.put("rows", list);
		param.put("configId", null);
		Map<String, Object> info2 = configDao.getVersionInfo(versionId);
		param.putAll(info2);
		return JSON.toJSONString(param, SerializerFeature.WriteDateUseDateFormat);
	}
	/**
	 * 前台获取配置列表接口
	 * @param versionId
	 * @return
	 */
	public String getConfigsList(String versionId,String blueprintId,String instanceId ) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		List<Map<String, Object>> list = configDao.getConfigList(param);
		// 需要遍历list查看是否有引用的配置
		
		Pattern p4 = Pattern.compile(PatternUtil.P4);
		Pattern p1 = Pattern.compile(PatternUtil.P1);
		
		
		if (list != null && list.size() > 0) {
			for (Map<String, Object> unit : list) {
				String key=unit.get("key").toString();
				String value = unit.get("value").toString();
				List<String> group=new ArrayList<String>();
				List<String> group1=new ArrayList<String>();
					Matcher m4 = p4.matcher(value);
					Matcher m1 = p1.matcher(value);
					while(m4.find()){
				   	  group.add(m4.group());
					}
					while(m1.find()){
					  group1.add(m1.group());
					}
					if(group.size()>0) {
				        for(int i=0;i<group.size();i++){
				         Matcher tempmat =p4.matcher(group.get(i));
				         if (tempmat.find()) {
				        	 String bluekey = tempmat.group(1);
						     String componentName = tempmat.group(2);
						     String componentVision = tempmat.group(3);
						     String key1 = tempmat.group(4);
						     String temp = findValue(blueprintId,componentName, componentVision,bluekey,key1);
						      if(!(temp == null || "".equals(temp))){
						    	 value = value.replace(group.get(i),temp);
							  }
						      unit.put("value", value);
				         }
				       }
					}
					if(group1.size()>0) {
				        for(int i=0;i<group1.size();i++){
				         Matcher tempmat =p1.matcher(group1.get(i));
				         if (tempmat.find()) {
				        	 String key1 = tempmat.group(1);
								if(key1.equals("instanceid")){
									value = value.replaceFirst(p1.pattern(),instanceId);
									unit.put("value", value);
								}else{
								  String temp = findValue2(versionId,key1);
								  if(!(temp == null || "".equals(temp))){
									  value = value.replace(group1.get(i),temp);
								  }
								}
							unit.put("value", value);
				         }
					}
				}	
			}
		}
		param.put("total", list.size());
		param.put("rows", list);
		return JSON.toJSONString(param, SerializerFeature.WriteDateUseDateFormat);
	}
	
	/**
	 * 添加一行
	 * @param versionId
	 * @param key
	 * @param value
	 * @param type
	 * @param description
	 * @return
	 */
	public String addLine(int versionId, String key, String value, Object type, Object description) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		param.put("key", key);
		param.put("value", value);
		param.put("type", type);
		param.put("description", description);
		Map<String, Object> line = configDao.checkKey(param);
		if (line != null) {
			return "{\"result\":false,\"msg\":\"key already exists\"}";
		}
		configDao.addLine(param);
		return "{\"result\":true}";
	}
	/**
	 * 添加或更新一行
	 * @param versionId
	 * @param id
	 * @param key
	 * @param value
	 * @param type
	 * @param description
	 * @return
	 */
	public String updateLine(int versionId, long id, String key, String value, String type, String description) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("versionId", versionId);
		param.put("key", key);
		param.put("value", value);
		param.put("type", type);
		param.put("description", description);
		Map<String, Object> line = configDao.checkKey(param);
		if (line != null) {
			return "{\"result\":false,\"msg\":\"key already exists\"}";
		}
		configDao.updateLine(param);
		return "{\"result\":true}";
	}
	//根据key设置值
	public String setValue(int versionId, String key, String value) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("versionId", versionId);
		param.put("key", key);
		param.put("value", value);
		Map<String, Object> line = configDao.checkKey(param);
		if (line == null) {
			return "{\"result\":false,\"msg\":\"cannot get key["+key+"]\"}";
		}
		configDao.setValue( versionId,  key,  value);
		return "{\"result\":true}";
	}
	//删除一行
	public String delLine(long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		configDao.delLine(param);
		return "{\"result\":true}";
	}
	/**
	 * 检测是properties 还是yaml格式   仅数云使用
	 * @param content
	 * @return
	 */
	public String typeScanner(String content) {
		String type = "properties";
		String PRO_TEST = "=";
		int pro_count = 0;
		String YAML_TEST = ":";
		int yaml_count = 0;
		for (String line : content.split("\n")) {
			if (line.startsWith("#")) {
				continue;
			}
			if (line.contains(YAML_TEST)) {
				yaml_count++;
			}
			if (line.contains(PRO_TEST)) {
				pro_count++;
			}
		}
		if (pro_count < yaml_count) {
			type = "yaml";
		}
		return type;
	}
	/**
	 * 批量导入配置
	 * @param versionId
	 * @param content
	 * @param type
	 * @return
	 */
	public String batchImport(int versionId, String content, String type) {
		String mark = "";
		List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> doList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> insertList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> updateList = new ArrayList<Map<String, Object>>();
		for (String line : content.split("\n")) {
			// 检查行的合法性
			if (line.startsWith("#")) {
				mark = mark + line.substring(1);
			} else if (line.contains("=") && line.indexOf("#") != 0) {
				Map<String, Object> addUnit = new HashMap<String, Object>();
				String key = line.substring(0, line.indexOf("="));
				String value = null;
				String nearMark = "";
				String extra = line.substring(line.indexOf("=") + 1);

				if (extra != null) {
					value = extra.trim();
				}
				nearMark = nearMark + mark;
				mark = "";// 清空
				addUnit.put("line", line);
				key = key.trim();
				if (key == null || "".equals(key)) {
					Map<String, Object> temp = new HashMap<String, Object>();
					temp.put("line", line);
					temp.put("reason", "null key is not allow");
					errorList.add(temp);
					continue;
				}
				addUnit.put("key", key.trim());
				addUnit.put("type", type);
				addUnit.put("value", value.trim());
				addUnit.put("description", nearMark.trim());
				doList.add(addUnit);
			} else if (line.trim() == null || "".equals(line.trim())) {
				continue;
			} else {
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("line", line);
				temp.put("reason", "format_error");
				errorList.add(temp);
			}

		}
		// 添加队列
		for (Map<String, Object> unit : doList) {
			unit.put("versionId", versionId);
			long id = checkKey(unit);
			if (id != 0) {
				unit.put("id", id);
				// 更新值
				configDao.updateLine(unit);
				updateList.add(unit);
				continue;
			}
			configDao.addLine(unit);
			insertList.add(unit);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insertnum", insertList.size());
		result.put("updatenum", updateList.size());
		result.put("errornum", errorList.size());

		// result.put("insert", insertList);
		// result.put("update", updateList);
		result.put("error", errorList);
		result.put("result", errorList.size() == 0);
		return JSON.toJSONString(result);
	}
	/**
	 * 根据新的配置更新出 新的版本
	 * @param fromVersionId
	 * @param configs
	 * @return
	 */
	public int updateNewVersion(int configId,List<Map<String,Object>> configs){
		String newVersion = generateNewVrsionName(configId);
		int newVersionId =  addNewVersion(configId, newVersion);
		if(newVersionId==0){
			return 0;
		}
		for(Map<String,Object> unit:configs){
			if(unit.get("key")==null || "".equals(unit.get("key"))){
				continue;
			}
			String key=unit.get("key").toString();
			String value=unit.get("value")==null?"":unit.get("value").toString();
 
			addLine(newVersionId, key, value, unit.get("type"), unit.get("description"));
		}
		return newVersionId;
	}
	/**
	 * 检查key是否已经被占用
	 * @param param
	 * @return
	 */
	private long checkKey(Map<String, Object> param) {
		Map<String, Object> result = configDao.checkKey(param);
		if (result == null) {
			return 0l;
		} else {
			return Long.valueOf(result.get("id").toString());
		}
	}
	//记录申请配置客户端ip
	public boolean addClient(int configId, String clientUrl) {
		List<String> list = configDao.getClients(configId);
		if (list == null || list.size() == 0) {
			configDao.addClient(configId, clientUrl);
			return true;
		}

		for (String cli : list) {
			if (clientUrl.equals(cli)) {
				return false;
			}
		}
		configDao.addClient(configId, clientUrl);
		return true;

	}
	
	private boolean sendConfig(Client client, int configId, String clientUrl, String content) throws Exception {
		if (!clientUrl.startsWith("http://")) {
			clientUrl = "http://" + clientUrl;
		}
		Form form = new Form();
		form.param("content", content);
		WebTarget web = client.target(clientUrl);
		web.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		return true;

	}

	public List<Map<String, Object>> getApps(String userId) {
		return configDao.getApps(userId);
	}
	/**
	 * 获取版本列表
	 * @param configId
	 * @return
	 */
	public List<Map<String, Object>> getVersions(int configId) {
		return configDao.getVersions(configId);
	}
	
	/**
	 * 将某一版本的配置拷贝给另一版本
	 * @param fromId
	 * @param toId
	 * @return
	 */
	public boolean clone(int fromId, int toId) {
		return configDao.clone(fromId, toId);
	}
	/**
	 * 获取配置信息
	 * @param configId
	 * @return
	 * 字段 id name USERID type description configId versionNum lastTime lastVersion
	 */
	public Map<String, Object> getConfigInfo(int configId) {
		return configDao.getConfigInfo(configId);
	}
	/**
	 * 根据版本id获取配置id
	 * @param versionId
	 * @return
	 */
	public int getConfigIdByVersionId(int versionId) {
		return configDao.getConfigIdByVersion(versionId);
	}
	/**
	 * 根据key的id获取配置版本id
	 * @param keyId
	 * @return
	 */
	public int getConfigIdByKeyId(long keyId) {
		return configDao.getConfigIdByKey(keyId);
	}
	private boolean isTopo(String name){
		String regex="^[a-f0-9]{8}(\\-[a-f0-9]{4}){4}[a-f0-9]{8}";
		Pattern p= Pattern.compile(regex);
		Matcher m= p.matcher(name);
		return m.find();
	}
	
	public String addconfig(String configName, String userId, String blueprintId, String bluekey, String componentVisionString, String jsonConfigs) {
		int configId = configDao.addComponnetConfigs(configName,userId,blueprintId,bluekey,componentVisionString);
		if (configId == 0) {
			return "{\"result\":false,\"msg\":\"new config add failed!\"}";
		}
		String newVersion = this.generateNewVrsionName(configId);
		int versionId = this.addNewVersion(configId, newVersion);
		if (versionId == 0) {
			// 删除
			this.delConfig(configId);
			return "{\"result\":false,\"msg\":\"new config version add failed!\"}";
		}
		if (jsonConfigs != null && !"".equals(jsonConfigs)) {
			// 添加几条配置
			List<Map<String, String>> configs = JSON.parseObject(jsonConfigs,
					new TypeReference<List<Map<String, String>>>() {
					});
			for (Map<String, String> unit : configs) {
				String key = unit.get("key").trim();
				if (key == null || "".equals(key)) {
					continue;
				}
				this.addLine(versionId, key, unit.get("value"), unit.get("type"), unit.get("description"));
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);
		resultMap.put("id", versionId);
		return JSON.toJSONString(resultMap);
	}
	
	public String findValue(String blueprintId,String componentName,String componnetVision,String bluekey,String key){
		String value = configDao.findValue( blueprintId, componentName, componnetVision,bluekey, key);
		return value;
	}
	
	public String findValue2(String versionId,String key){
		String value = configDao.findValue2( versionId, key);
		return value;
	}
	
	
	public String updateConfigs(String versionId,String configMap){
		Map<String,Object> map = JSON.parseObject(configMap, new TypeReference<Map<String, Object>>() {
		});
		String value = configDao.updateConfigs( versionId, map);
		return value;
	}
	public String configList(String userId, int pageSize, int pageNum, String keyword) {
		int start = 0;
		if (pageNum != 0) {
			start = pageSize * (pageNum - 1);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("pageSize", pageSize);
		param.put("pageNum", pageNum);
		param.put("start", start);
		param.put("keyword", keyword);
		Map<String, Object> param1 = new HashMap<String, Object>();
		if(!(keyword == null || "".equals(keyword))){
			param1.put("keyword", keyword);
		}
		if(!(userId == null || "".equals(userId))){
			param1.put("userId", userId);
		}
		int num = configDao.getNum(param1);
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		if (num != 0) {
			rows = configDao.getConfigs(param);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", rows);
		result.put("total", num);
		result.put("pageSize", pageSize);
		result.put("pageNum", pageNum);
		result.put("keyword", keyword);
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}
	public String configinfos(String userId, int appId) {
		// TODO Auto-generated method stub
		Map<String, Object> rows = new HashMap<>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("appId", appId);
		rows = configDao.configinfos(param);
		
		Map<String, Object> config =  JSONObject.parseObject(rows.get("componentinput").toString(), Map.class);
		
		List<Map<String, Object>> configss = new ArrayList<>();
		for (Map.Entry<String, Object> map : config.entrySet()) {
			Map<String, Object> configs = new HashMap<>();
			configs.put("key", map.getKey());
			configs.put("value", map.getValue());
			configss.add(configs);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("configs", configss);
		result.put("appId", rows.get("appId").toString());
		result.put("appName", rows.get("appName").toString());
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}
	
	public String findFlowId(String blueInstanceId, String op) {
		// TODO Auto-generated method stub
		Map<String, Object> rows = new HashMap<>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("blueInstanceId", blueInstanceId);
		param.put("op", op);
		rows = configDao.findFlowId(param);
		if(rows!=null){
			return JSON.toJSONString(rows.get("FLOW_ID"), SerializerFeature.WriteDateUseDateFormat);
		}
		return null;
	}
	
	public String findNewFlowId(String blueInstanceId, String appName, String op) {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<String, String>();
		param.put("blueInstanceId", blueInstanceId);
		param.put("appName", appName);
		param.put("op", op);
		String rows = configDao.findNewFlowId(param);
		
		return rows;
	}
	
}
