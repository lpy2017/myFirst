package com.dc.appengine.appmaster.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.service.impl.ConfigsService;
import com.dc.appengine.appmaster.service.impl.UserService;
import com.dc.appengine.appmaster.utils.ServiceBeanContext;

@Controller
@RequestMapping("/ws/configs")
public class ConfigsRestService {

	@Resource
	IUserService userService;
	@Resource
	ConfigsService configService;
	public List<Long> getUserIds(String userId) {
		String resultStr = userService.getSonsOfUser(Long.valueOf(userId));
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {
		});
		List<Long> list = new ArrayList<Long>();
		for (long unit : map.keySet()) {
			list.add(unit);
		}
		return list;
	}

	public List<String> getUserNames(String userId) {
		String resultStr = userService.getSonsOfUser(Long.valueOf(userId));
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {
		});
		List<String> list = new ArrayList<String>();
		for (long unit : map.keySet()) {
			list.add(map.get(unit));
		}
		return list;
	}

	public String listToString(List list) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object unit : list) {
			if (index != 0) {
				sb.append(",");
			}
			index++;
			sb.append(unit.toString());
		}
		return sb.toString();
	}

	/**
	 * 分类型显示用户下的所有配置
	 * 
	 * @param pageSize
	 * @param pageNum
	 * @param keyword
	 * @return
	 * {"pageNum":1,"pageSize":5,"rows":[
	 * 		{"USERID":1,"configId":1,"description":"1d","id":1,
	 * 		"lastTime":"2017-02-10 10:28:45","lastVersion":"v1",
	 * 		"name":"testconfig1",
	 * 		"type":"component","versionNum":1}
	 * 		,{}
	 * 		],"total":46,"type":"component"}
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String list(@RequestParam("userId") String userId, @RequestParam("type") String type,
			@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum,
			@RequestParam("keyword") String keyword) {
		if (keyword == null || "".equals(keyword)) {
			keyword = null;
		}
		List<Long> userIdss = getUserIds(userId);
		String userIds = listToString(userIdss);
		if (type == null || "".equals(type)) {
			type = "component";
		}
		return configService.getConfigByPage(pageSize, pageNum, userIds, type, keyword);
	}

	/**
	 * 配置描述信息 id,name USERID configId versionNum lastTime lastVersion
	 * {
	 * "USERID":1,"configId":1,
	 * "lastVersion":"v1","name":"testconfig1",
	 * "type":"component","versionNum":1,"description":"1d",
	 * "id":1,"lastTime":"2017-02-10 10:28:45"
	 * }
	 */
	@RequestMapping(value = "getConfigInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getConfigInfo(@RequestParam("configId") int configId) {
		return JSON.toJSONString(configService.getConfigInfo(configId), SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * 显示配置详细信息 list<key-value>
	 */
	@RequestMapping(value = "getConfigList", method = RequestMethod.GET)
	@ResponseBody
	public String getConfigList(@RequestParam("versionId") int versionId, @RequestParam("keyword") String keyword) {
		// int configId=configService.getConfigId(versionId);
		return configService.getConfigList(versionId, keyword);
	}
	/**
	 * 添加一行 键值对 key value type
	 * @param versionId
	 * @param key
	 * @param value
	 * @param type
	 * @param description
	 * @return
	 */
	@RequestMapping(value = "addLine", method = RequestMethod.POST)
	@ResponseBody
	public String addLine(@RequestParam("versionId") int versionId, @RequestParam("key") String key,
			@RequestParam("value") String value, @RequestParam("type") String type,
			@RequestParam("description") String description) {
		return configService.addLine(versionId, key, value, type, description);
	}
	/**
	 * 更新或添加一行  如果有id字段则去更新这一行  如果没有id字段则去添加一行
	 * @param versionId
	 * @param id
	 * @param key
	 * @param value
	 * @param type
	 * @param description
	 * @return
	 * {"result":true}
	 * {"result":false,"msg":"db error"}
	 */
	@RequestMapping(value = "updateLine", method = RequestMethod.POST)
	@ResponseBody
	public String updateLine(@RequestParam("versionId") int versionId, @RequestParam("id") long id,
			@RequestParam("key") String key, @RequestParam("value") String value, @RequestParam("type") String type,
			@RequestParam("description") String description) {

		if (id == 0l) {
			return configService.addLine(versionId, key, value, type, description);
		}
		return configService.updateLine(versionId, id, key, value, type, description);
	}
	/**
	 * 为某一份配置中的某个key设置value
	 * @param versionId
	 * @param key
	 * @param value
	 * @return
	 * {"result":true}
	 * {"result":false,"msg":"db error"}
	 */
	@RequestMapping(value = "setValue", method = RequestMethod.POST)
	@ResponseBody
	public String setValue(@RequestParam("versionId") int versionId, @RequestParam("key") String key,
			@RequestParam("value") String value) {
		return configService.setValue(versionId, key, value);
	}
	/**
	 * 删除一行
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "deleteLine", method = RequestMethod.GET)
	@ResponseBody
	public String deleteLine(@RequestParam("id") long id) {
		return configService.delLine(id);
	}
	/**
	 * 批量导入配置
	 * @param versionId
	 * @param type
	 * @param content  key1=value1\r\n#descriptionofkey2\r\nkey2=value2
	 * @return
	 * 正确返回 插入的行数 更新的行数  出错的行数
	 * {"result":true,"insertnum":1,"updatenum":2,"errornum":0}
	 * {"result":false,"insertnum":1,"updatenum":2,"errornum":1, "error"[{"line":"errorline","reason":"errorReason"}]}
	 */
	@RequestMapping(value = "injectConfigs", method = RequestMethod.POST)
	@ResponseBody
	public String inject(@RequestParam("versionId") int versionId, @RequestParam("type") String type,
			@RequestParam("content") String content
	) {
		// int configId=configService.getConfigIdByVersionId(versionId);
		// boolean hasPermision=configService.hasWritePermision(configId,
		// getUserIds(userId));
		// if(!hasPermision){
		// return "{\"result\":false,\"msg\":\"have no permission to do
		// this\"}";
		// }
		return configService.batchImport(versionId, content, type);
	}
	/**
	 * 外部调用申请配置接口
	 * @param type
	 * @param userName
	 * @param password
	 * @param appName
	 * @param appVersion
	 * @return
	 * {"appName"}
	 */
	@RequestMapping(value = "getConfig", method = RequestMethod.POST)
	@ResponseBody
	public String getConfigs(@RequestParam("type") String type, @RequestParam("userName") String userName,
			@RequestParam("password") String password, @RequestParam("appName") String appName,
			@RequestParam("appVersion") String appVersion) {
		return configService.getConfig(appName, appVersion);
	}
	/**
	 * 配置主动下发接口  暂无使用
	 * @param versionId
	 * @return
	 */
//	@GET
//	@Path("sendConfigs")
//	public String sendConfigs(@QueryParam("versionId") int versionId) {
//		return configService.sendConfig(versionId);
//	}
	/**
	 * 国寿版本无使用
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "getApps", method = RequestMethod.GET)
	@ResponseBody
	public String getApps(@RequestParam("userId") String userId) {
		String userIds = listToString(getUserIds(userId));
		return JSON.toJSONString(configService.getApps(userIds));
	}
	/**
	 * 获取版本列表
	 * @param configId
	 * @return
	 * {["id":1,"version":"v1","last_update_time":"2016-02-01 19:19:23","name":"storm_config"]}
	 */
	@RequestMapping(value = "getVersions", method = RequestMethod.GET)
	@ResponseBody
	public String getVersions(@RequestParam("configId") int configId) {
		return JSON.toJSONString(configService.getVersions(configId), SerializerFeature.WriteDateUseDateFormat);
	}
	/**
	 * 创建一份配置 
	 * @param configName
	 * @param userId
	 * @param type
	 * @param description
	 * @param jsonConfigs  [{"key":"aaa","value":"123","type":"","description":""},{}] }
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public String create(@RequestParam("configName") String configName, @RequestParam("userId") String userId,
			@RequestParam("type") String type, // component
			@RequestParam("description") String description, @RequestParam("configs") String jsonConfigs) {
		if (configName == null || "".equals(configName)) {
			return "{\"result\":false,\"msg\":\"appName cannot be null\"}";
		}
		if (type == null || "".equals(type)) {
			type = "component";
		}
		// 检查名字是否被注册
		boolean registed = configService.checkConfig(configName, type);
		if (registed) {
			return "{\"result\":false,\"msg\":\"this config:" + configName + " is already!\"}";
		}

		// 添加新配置
		int configId = configService.addNewConfig(configName, type, userId, description);
		if (configId == 0) {
			return "{\"result\":false,\"msg\":\"new config add failed!\"}";
		}
		String newVersion = configService.generateNewVrsionName(configId);
		int versionId = configService.addNewVersion(configId, newVersion);
		if (versionId == 0) {
			// 删除
			configService.delConfig(configId);
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
				configService.addLine(versionId, key, unit.get("value"), unit.get("type"), unit.get("description"));
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);
		resultMap.put("version", newVersion);
		resultMap.put("name", configName);
		resultMap.put("id", versionId);
		resultMap.put("configId", configId);
		return JSON.toJSONString(resultMap);
	}
	/**
	 * 创建一个新的版本
	 * 两种方式 
	 * 1.利用configFrom 这个字段是其他版本的名称 然后拷贝一份作为新的配置
	 * 2.jsonConfigs 前端手动填一份新的配置 json格式发送到后端
	 * @param configId
	 * @param createFrom
	 * @param jsonConfigs
	 * @return
	 */
	@RequestMapping(value = "addVersion", method = RequestMethod.POST)
	@ResponseBody
	public String addVersion(@RequestParam("configId") int configId, @RequestParam("createFrom") String createFrom,
			@RequestParam("configs") String jsonConfigs
	// @FormParam("userId")String userId
	) {
		// boolean hasPermision=configService.hasWritePermision(configId,
		// getUserIds(userId));
		// if(!hasPermision){
		// return "{\"result\":false,\"msg\":\"have no permission to do
		// this\"}";
		// }
		// 先获取下配置信息

		Map<String, Object> configInfo = configService.getConfigInfo(configId);
		if (configInfo == null || configInfo.get("name") == null) {

			return "{\"result\":false,\"msg\":\"cannot get the info of config[" + configId + "]!\"}";
		}
		String configName = configInfo.get("name").toString();
		String newVersion = configService.generateNewVrsionName(configId);
		int newversionId = configService.addNewVersion(configId, newVersion);
		if (newversionId == 0) {
			return "{\"result\":false,\"msg\":\"new config version add failed!\"}";
		}
		if (StringUtils.isNotEmpty(createFrom)) {
			int fromId = 0;
			if (createFrom.startsWith("v")) {
				// int fromId=Integer.valueOf(createFrom.replace("v", ""));
				// 查询versionId
				fromId = configService.getVersionIdByName(configId, createFrom);
			} else {
				fromId = Integer.valueOf(createFrom);
			}
			boolean success = configService.clone(fromId, newversionId);
			if (!success) {
				return "{\"result\":false,\"msg\":\"clone configs error\"}";
			} else {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("result", true);
				result.put("name", configName);
				result.put("version", newVersion);
				result.put("id", newversionId);
			}
		}
		// 默认克隆后仍可添加新配置 两项可以兼容
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
				configService.addLine(newversionId, key, unit.get("value"), unit.get("type"), unit.get("description"));
			}
		}
		return "{\"result\":true}";

	}

	// 一整份配置 所有版本删除配置
	
	@RequestMapping(value = "delForApp", method = RequestMethod.GET)
	@ResponseBody
	public String delConfig(@RequestParam("configId") int configId, @RequestParam("userId") String userId) {
//		boolean hasPermision = configService.hasWritePermision(configId, getUserIds(userId));
		boolean hasPermision =true;
		if (!hasPermision) {
			return "{\"result\":false,\"msg\":\"have no permission to do this\"}";
		}
		boolean result = configService.delConfig(configId);
		if (!result) {
			return "{\"result\":false,\"msg\":\"there are some version  used by app,del app first\"}";
		}
		return "{\"result\":true}";
	}

	// 删除版本
	@RequestMapping(value = "delForVersion", method = RequestMethod.GET)
	@ResponseBody
	public String delVersion(@RequestParam("versionId") int versionId, @RequestParam("userId") String userId) {
		int configId = configService.getConfigIdByVersionId(versionId);
//		boolean hasPermision = configService.hasWritePermision(configId, getUserIds(userId));
		boolean hasPermision =true;
		if (!hasPermision) {
			return "{\"result\":false,\"msg\":\"have no permission to do this\"}";
		}
		boolean result = configService.delVersion(versionId,0);

		if (!result) {
			return "{\"result\":false,\"msg\":\"this version is used by app,del app first\"}";
		}
		return "{\"result\":true}";
	}
	
	/**
	 * 添加配置
	 * @param configName
	 * @param jsonConfigs [{"key":"a1","value":"111","type":"","description":""},{"key":"a2","value":"222","type":"","description":""}]
	 */
	@RequestMapping(value = "addConfigs", method = RequestMethod.POST)
	@ResponseBody
	public String addConfigs(@RequestParam("configName") String configName,@RequestParam("userId") String userId, @RequestParam("jsonConfigs") String jsonConfigs) {
		if (configName == null || "".equals(configName)) {
			return "{\"result\":false,\"msg\":\"appName cannot be null\"}";
		}
		
		String type = "component";
		// 检查名字是否被注册
		boolean registed = configService.checkConfig(configName, type);
		if (registed) {
			return "{\"result\":false,\"msg\":\"this config:" + configName + " is already!\"}";
		}
		
		int configId = configService.addNewConfig(configName, type, userId, "");
		if (configId == 0) {
			return "{\"result\":false,\"msg\":\"new config add failed!\"}";
		}
		String newVersion = configService.generateNewVrsionName(configId);
		int versionId = configService.addNewVersion(configId, newVersion);
		if (versionId == 0) {
			// 删除
			configService.delConfig(configId);
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
				configService.addLine(versionId, key, unit.get("value"), unit.get("type"), unit.get("description"));
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", true);
		resultMap.put("version", newVersion);
		resultMap.put("name", configName);
		resultMap.put("id", versionId);
		resultMap.put("configId", configId);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 查询配置
	 * @param versionId
	 * 
	 */
	@RequestMapping(value = "findConfigs", method = RequestMethod.GET)
	@ResponseBody
	public String findConfigs(@RequestParam("versionId") String versionId,@RequestParam("blueprintinstance") String blueprintinstance,@RequestParam("instanceId")String instanceId) {
	 /*   String appVersion = null;
	    if (configName == null || "".equals(configName)) {
			return "{\"result\":false,\"msg\":\"appName cannot be null\"}";
		}
	    int configId = configService.getConfigIdByName(configName);
	    //默认为v1版本
	    int versionId = configService.getVersionIdByName(configId, "v1");*/
	    return configService.getConfigsList(versionId,blueprintinstance,instanceId);
	}
	
	@RequestMapping(value = "updateConfigs", method = RequestMethod.POST)
	@ResponseBody
	public String updateConfigs(@RequestParam("configVersionId") String configVersionId, @RequestParam("configMap") String configMap) {
		return configService.updateConfigs(configVersionId,configMap);
	}
	
	@RequestMapping(value = "configlist", method = RequestMethod.GET)
	@ResponseBody
	public String configList(@RequestParam("userId") String userId,@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum,
			@RequestParam("keyword") String keyword) {
		
	   return configService.configList(userId,pageSize,pageNum,keyword);
	}
	
	@RequestMapping(value = "configinfos", method = RequestMethod.GET)
	@ResponseBody
	public String configinfos(@RequestParam("userId") String userId,@RequestParam("appId") int appId) {
		
	   return configService.configinfos(userId,appId);
	}
	
	@RequestMapping(value = "findFlowId", method = RequestMethod.GET)
	@ResponseBody
	public String findFlowId(@RequestParam("blueInstanceId") String blueInstanceId,@RequestParam("op") String op) {
		
	   return configService.findFlowId(blueInstanceId,op);
	}
	
	@RequestMapping(value = "findNewFlowId", method = RequestMethod.GET)
	@ResponseBody
	public String findNewFlowId(@RequestParam("blueInstanceId") String blueInstanceId,@RequestParam("appName") String appName,@RequestParam("op") String op) {
		
		return configService.findNewFlowId(blueInstanceId,appName,op);
	}
}
