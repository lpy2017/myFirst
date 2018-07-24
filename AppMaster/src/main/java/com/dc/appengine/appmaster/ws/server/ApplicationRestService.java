package com.dc.appengine.appmaster.ws.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;

@Controller
@RequestMapping("/ws/application")
public class ApplicationRestService {
	private static final Logger log = LoggerFactory
			.getLogger(ApplicationRestService.class);
	
	@Resource
	IApplicationService applicationService;
	
	@Resource
	IInstanceService instanceService;
	
	@Autowired
	@Qualifier("instanceDao")
	IInstanceDao instanceDao;
	
	@RequestMapping(value = "test",method = RequestMethod.GET)
	@ResponseBody
	public String test(){
		return "hello"+new Date();
	}
	
	
	/*@RequestMapping(value = "/listOperationApps",method = RequestMethod.GET)
	@ResponseBody
	public String listOperationApps(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum,
			@RequestParam("appName") String appName, @RequestParam("userId") Long userId) {
		if (log.isDebugEnabled()) {
			log.debug("receive querying application request: appName=" + appName + ", pageNum=" + pageNum
					+ ", pageSize=" + pageSize + ", userId=" + userId);
		}

		try {
			Page page = applicationService.listOperationApps(pageSize, pageNum,  userId);
			return JSON.toJSONString(page);
		} catch (Exception e) {
			log.error("failed to query operation application", e);
			return MessageHelper.wrap("total", 0, "rows", null);
		}
	}*/
	
/*	*//**
	 * 根据伸缩值，更新部署策略中实例数，由定时任务伸缩实例数
	 * @param appId
	 * @param scaleCount
	 * @return
	 *//*

	@RequestMapping(value = "maintainAppInstances",method = RequestMethod.POST)
	@ResponseBody
	public String maintainAppInstances(@RequestParam("appId") String appId, @RequestParam("scaleCount") String scaleCount) {
		if (log.isDebugEnabled()) {
			log.debug("receive maintain application instances number events request: appId=" + appId + ", scaleCount="
					+ scaleCount);
		}
		boolean check = existRollingUpgradeTask(appId);
		if(check){
			return MessageHelper.wrap("result", false, "message", "应用正在升级，请稍后维护......");
		}
		String message = "";
		try {
			Map<Object, Object> scaleCountMap = JSON.parseObject(scaleCount, new TypeReference<Map<Object, Object>>(){});
			Map<Object, Object> map = commandService.maintainAppInstances(appId, scaleCountMap);
			message = JSON.toJSONString(map);
		} catch (Exception e) {
			log.error("failed to query operation application", e);
			message = "{\"result\":\"false\",\"message\":\"" + "扩展应用失败！" + "\"}";
		}
		return message;
	}
	

	@RequestMapping(value = "updateRCScalable",method = RequestMethod.GET)
	@ResponseBody
	public String updateRCScalable(@RequestParam("appId")String appId,@RequestParam("enable") boolean enable ){
		int status=0;
		String result = MessageHelper.wrap("result",true,"message","维护开关已打开！");
		List<Instance>instances = instanceService.findInstanceByApp(appId);
		if(enable){
			status=1;
			if(instances==null ||instances.size()==0){
				return MessageHelper.wrap("result",false,"message","实例数为0，维护开关不可打开！");
			}
			applicationService.updateAppScalableStatus(appId, status);
		}else{
//			Map<String, Object> resultMap=existRCTask(appId);
//			if(!(Boolean)resultMap.get("result")){
//				return JSON.toJSONString(resultMap);
//			}
			applicationService.updateAppScalableStatus(appId, status);
			commandService.clearInstanceTask(appId, null);
			result=MessageHelper.wrap("result",true,"message","维护开关已关闭！");
		}
		 return result;
	}*/
	
	@RequestMapping(value = "getBlueprintComponentInstanceParams", method = RequestMethod.GET)
	@ResponseBody
	public String getBlueprintComponentInstanceParams(@RequestParam("instanceId") String instanceId){
		Map<String, Object> result = new HashMap<>();
		try{
			String params = instanceService.getBlueprintComponentInstanceParams(instanceId);
			params = SensitiveDataUtil.decryptConfig(params);
			result.put("result", true);
			result.put("message", "获取组件实例配置成功！");
			result.put("data", params);
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "updateBlueprintComponentInstanceParams", method = RequestMethod.POST)
	@ResponseBody
	public String updateBlueprintComponentInstanceParams(@RequestParam("instanceId") String instanceId,
			@RequestParam("params") String params){
		Map<String, Object> result = new HashMap<>();
		try{
			Map<String, Object> map = new HashMap<>();
			map.put("instanceId", instanceId);
			params = SensitiveDataUtil.getEncryptConfig(params);
			map.put("params", params);
			instanceService.updateBlueprintComponentInstanceParams(map);
			result.put("result", true);
			result.put("message", "更新组件实例配置成功");
		}catch(Exception e){
			log.error(e.getMessage(),e);
			result.put("result", false);
			result.put("message", "更新组件实例配置失败，reason：" + e.getMessage());
		}
		return JSON.toJSONString(result);
	}
	
}
