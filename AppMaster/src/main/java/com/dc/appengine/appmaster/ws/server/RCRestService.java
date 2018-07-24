package com.dc.appengine.appmaster.ws.server;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.service.impl.RCService;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Controller
@RequestMapping("/ws/RC")
public class RCRestService {
	
	@Resource
	IUserService userService;
	
	@Resource
	IApplicationService applicationService;
	
	@Resource
	IInstanceService instanceService;
	
	@Resource
	RCService rcService;
	
	@Resource
	IBlueprintService blueprintService;
	
	/*
	 * 开启、关闭维护开关
	 */
	@RequestMapping(value = "updateRCScalable",method = RequestMethod.GET)
	@ResponseBody
	public String updateRCScalable(@RequestParam("appId") long appId,
			@RequestParam("enable") boolean enable) {
		int status=0;
		String result = MessageHelper.wrap("result",true,"message","维护开关已打开！");
		List<Map<String,Object>>instances = instanceService.listInstancesByAppId(appId);
		if(enable){
			status=1;
			if(instances==null ||instances.size()==0){
				return MessageHelper.wrap("result",false,"message","实例数为0，维护开关不可打开！");
			}
			rcService.updateAppScalableStatus(appId, status);
		}else{
			rcService.updateAppScalableStatus(appId, status);
			//commandService.clearInstanceTask(appId, null);
			result=MessageHelper.wrap("result",true,"message","维护开关已关闭！");
		}

		return result;
	}
	
	/*
	 * 更新组件实例个数
	 */
	@RequestMapping(value = "maintainAppInstances",method = RequestMethod.POST)
	@ResponseBody
    public String maintainAppInstances (@RequestParam("appId") long appId,
    		@RequestParam("resouceVersionId") String resourceVersionId,
    		@RequestParam("targetCount") int targetCount){
		return rcService.maintainAppInstances(appId,resourceVersionId,targetCount);
	}

	/*
	 * 获取当前组件当前版本下的所有实例(ma_instance(app_id,version_id))
	 * [{"COMPONENT_OUTPUT_TEMP":"{\"webappsPath\":\"/usr/share/tomcat/webapps/\"}","RESOURCE_VERSION_ID":"4d47450f-4203-4f2b-a5bf-5173d19dec4d","LXC_IP":"0.0.0.0","DEPLOY_TIME":1496382786000,"ID":"c6f0e269-f7b3-455b-b6cc-e1f26287b7f4","NODE_ID":"3fb36d64-ce1d-4bf2-9583-53797de9b973","COMPONENT_INPUT_TEMP":"{\"COMPONENT_OUTPUT":"","COMPONENT_INPUT":"{\"startPort\":\"18089\"}","APP_ID":3267}]
	 */
	@RequestMapping(value = "getInstanceOfAppAndVersion",method = RequestMethod.GET)
	@ResponseBody
	public String getInstanceOfAppAndVersion(@RequestParam("appId") long appId,
			@RequestParam("versionId") String versionId){
		return JSON.toJSONString(rcService.getInstanceOfAppAndVersion(appId,versionId));
	}
	
	/*
	 * 查询组件实例的版本信息
	 * 返回值为list（同getInstanceOfAppAndVersion的返回格式），多个则说明多个版本
	 */
	@RequestMapping(value = "getAppVersionListByAppId",method = RequestMethod.GET)
	@ResponseBody
	public String getAppVersionListByAppId(@RequestParam("appId") long appId){
		return JSON.toJSONString(rcService.getAppVersionListByAppId(appId));
	}
	
	/*
	 * 查询所有部署过的组件
	 */
	@RequestMapping(value = "getMasterApplications",method = RequestMethod.GET)
	@ResponseBody
	public String getMasterApplications(){
		return JSON.toJSONString(rcService.getMasterApplications());
	}
	
	/*
	 * 通过组件id查询其所有实例
	 */
	@RequestMapping(value = "getMasterInstanceByApplicationId",method = RequestMethod.GET)
	@ResponseBody
	public String getMasterInstanceByApplicationId(long appId){
		return JSON.toJSONString(rcService.getMasterInstanceByApplicationId(appId));
	}
	
	/*
	 * 获取组件+当前版本的目标实例数
	 */
	@RequestMapping(value = "getInsNumByAppIdAndResId",method = RequestMethod.GET)
	@ResponseBody
	public String getInsNumByAppIdAndResId(@RequestParam("appId") long appId,
			@RequestParam("versionId") String versionId){
		return JSON.toJSONString(rcService.getInsNumByAppIdAndResId(appId,versionId));
	}
	
	/*rc调用子流程（启动、停止、卸载）
	 * 
	 */
	@RequestMapping(value = "operateFlow",method = RequestMethod.GET)
	@ResponseBody
	public String operateFlow(@RequestParam("appId") long appId,
			@RequestParam("instanceId ") String instanceId ,
			@RequestParam("op") String op){
		return blueprintService.operateFlow(appId,instanceId,op);
	}
	
	/*rc调用子流程（部署）
	 * 
	 */
	@RequestMapping(value = "operateFlowDeploy",method = RequestMethod.GET)
	@ResponseBody
	public String operateFlowDeploy(@RequestParam("appId") long appId,
			@RequestParam("versionId") String versionId,
			@RequestParam("instanceId") String instanceId,
			@RequestParam("nodeId") String nodeId){
		return blueprintService.operateFlowDeploy(appId,versionId,instanceId,nodeId);
	}
}
