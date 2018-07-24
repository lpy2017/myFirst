package com.dc.appengine.quartz.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.quartz.entity.Page;
import com.dc.appengine.quartz.service.QuartzService;
import com.dc.appengine.quartz.utils.Audit2Master;
import com.dc.appengine.quartz.utils.MasterEnv;
import com.dc.appengine.quartz.utils.MessageHelper;
import com.dc.appengine.quartz.utils.SortUtil;
import com.dc.appengine.quartz.utils.UUIDGenerator;
import com.dcits.Common.entity.User;

@Controller
@RequestMapping("/ws/quartz")
public class QuartzRestService {
	private static final Logger log = LoggerFactory
			.getLogger(QuartzRestService.class);
	
	@Resource
	QuartzService quartzService;
	
	//添加定时任务——业务表
	@RequestMapping(value = "/addQuartzInfo",method = RequestMethod.POST)
	@ResponseBody
	public String addQuartzInfo(@RequestParam("quartzDate") String quartzDate,
			@RequestParam("quartzName") String quartzName,
			@RequestParam(value = "userId", required = false) String userId,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		try{
			String quartzId = UUIDGenerator.getUUID();
			/*SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			Date date =  formatter.parse(quartzDate);
			String quartzCron = quartzService.getCron(date);*/
			Map<String, String> param= new HashMap<String, String>();
			param.put("quartzId", quartzId);
			param.put("quartzName", quartzName);
			param.put("quartzCron", quartzDate);
			param.put("status", "未启动");
			param.put("userId", 1+"");
			boolean cronExpressionFlag = CronExpression.isValidExpression(quartzDate);
			if(!cronExpressionFlag){
				return MessageHelper.wrap("result",false,"message","cron表达式不合法，请重新输入");
			}
			quartzService.saveQuartz(param);
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",quartzName,"add",1,"新增定时任务 :"+quartzName)).start();
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","保存定时任务成功");
		}catch(Exception e){
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",quartzName,"add",0,"新增定时任务 :"+quartzName)).start();
			//==============添加审计end=====================
			log.error("保存定时任务失败", e);
			if (e instanceof DuplicateKeyException) {
				return MessageHelper.wrap("result",false,"message","定时任务名称重复，请更改名称");
			}
			return MessageHelper.wrap("result",false,"message","保存定时任务失败");		
		}
	}
	
	//更新定时任务——业务表+job表
	@RequestMapping(value = "/updateQuartzInfo",method = RequestMethod.POST)
	@ResponseBody
	public String updateQuartzInfo(@RequestParam("quartzDate") String quartzDate,
			@RequestParam("quartzName") String quartzName,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam("quartzId") String quartzId,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		try{
			/*SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			Date date =  formatter.parse(quartzDate);
			String quartzCron = quartzService.getCron(date);*/
			boolean cronExpressionFlag = CronExpression.isValidExpression(quartzDate);
			if(!cronExpressionFlag){
				return MessageHelper.wrap("result",false,"message","cron表达式不合法，请重新输入");
			}
			Map<String, String> param= new HashMap<String, String>();
			param.put("quartzId", quartzId);
			param.put("quartzName", quartzName);
			param.put("quartzCron", quartzDate);
			param.put("userId", 1+"");
			quartzService.updateQuartzInfo(param);
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",quartzName,"update",1,"更新定时任务 :"+quartzName)).start();
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","更新定时任务成功");
		}catch(Exception e){
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",quartzName,"update",0,"更新定时任务 :"+quartzName)).start();
			//==============添加审计end=====================
			log.error("更新定时任务失败", e);
			if (e instanceof DuplicateKeyException) {
				return MessageHelper.wrap("result",false,"message","定时任务名称重复，请更改名称");
			}
			return MessageHelper.wrap("result",false,"message","更新定时任务失败");		
		}
	}
	
	//暂停某个定时任务的所有job
	@RequestMapping(value = "/stopQuartzInJob",method = RequestMethod.POST)
	@ResponseBody
	public String stopQuartzInJob(@RequestParam("quartzId") String quartzId){
		try{
			Map<String, String> param= new HashMap<String, String>();
			param.put("quartzId", quartzId);
			param.put("status", "STOP");
			quartzService.stopQuartzInfo(param);
			return MessageHelper.wrap("result",true,"message","暂停定时任务成功");
		}catch(Exception e){
			e.printStackTrace();
			log.error("暂停定时任务失败", e);
			return MessageHelper.wrap("result",false,"message","暂停定时任务失败");		
		}
	}
	
	//恢复某个定时任务的所有job
	@RequestMapping(value = "/resumeQuartzInJob",method = RequestMethod.POST)
	@ResponseBody
	public String resumeQuartzInJob(@RequestParam("quartzId") String quartzId){
		try{
			Map<String, String> param= new HashMap<String, String>();
			param.put("quartzId", quartzId);
			param.put("status", "START");
			quartzService.resumeQuartzInJob(param);
			return MessageHelper.wrap("result",true,"message","恢复定时任务成功");
		}catch(Exception e){
			e.printStackTrace();
			log.error("恢复定时任务失败", e);
			return MessageHelper.wrap("result",false,"message","恢复定时任务失败");		
		}
	}
	
	//删除某个定时任务的所有job
	@RequestMapping(value = "/deleteQuartzInJob",method = RequestMethod.POST)
	@ResponseBody
	public String deleteQuartzInJob(@RequestParam("quartzId") String quartzId,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		String jsonQuartz = quartzService.getQuartz(quartzId);
		Map<String,Object> mapQuertz = JSON.parseObject(jsonQuartz, Map.class);
		try{
			Map<String, String> param= new HashMap<String, String>();
			param.put("quartzId", quartzId);
			quartzService.deleteQuartzInfo(param);
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",""+mapQuertz.get("quartzName"),"delete",1,"删除定时任务 :"+""+mapQuertz.get("quartzName"))).start();
			//==============添加审计end=====================
			return MessageHelper.wrap("result",true,"message","删除定时任务成功");
		}catch(Exception e){
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",""+mapQuertz.get("quartzName"),"delete",0,"删除定时任务 :"+""+mapQuertz.get("quartzName"))).start();
			//==============添加审计end=====================
			log.error("删除定时任务失败", e);
			return MessageHelper.wrap("result",false,"message","删除定时任务失败");		
		}
	}
	
	//定时任务列表
	@RequestMapping(value = "getQuartzListByPage",method = RequestMethod.GET)
	@ResponseBody
	public String getQuartzListByPage(@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum,
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		try{
			//权限管理userId
			//String resultStr= userService.getSonsOfUser(userId);
			//Map<Long,String> map=JSON.parseObject(resultStr, new TypeReference<Map<Long,String>>(){});
			//StringBuilder userIds= new StringBuilder();
			//int index=0;
			//for (long unit:map.keySet()){
				//if(index!=0){
				//	userIds.append(",");
				//}
				//index++;
				//userIds.append(unit+"");
			//}
			Map<String,Object> condition = new HashMap<>();
			condition.put("userIds", "1".split(","));
			condition.put("quartzName", key);
			condition.put("sortName", SortUtil.getColunmName("quartz", sortName));
			condition.put("sortOrder", sortOrder);
			Page page = quartzService.getQuartzListByPage(condition, pageNum, pageSize);
			return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
		}catch(Exception e){
			e.printStackTrace();
			log.error("查询中出现异常", e);
			return MessageHelper.wrap("result",false,"message","查询中出现异常");		
		}
	}
	
	//给定时任务添加job
	@RequestMapping(value = "/createQuartzJob",method = RequestMethod.POST)
	@ResponseBody
	public String createQuartzJob(@RequestParam("flowId") String flowId,
			@RequestParam("flowName") String flowName,
			@RequestParam("instanceId") String instanceId,
			@RequestParam("instanceName") String instanceName,
			@RequestParam("quartzId") String quartzId,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		String jsonQuartz = quartzService.getQuartz(quartzId);
		Map<String,Object> mapQuertz = JSON.parseObject(jsonQuartz, Map.class);
		try{
			String returnMsg = quartzService.createQuartzJob(quartzId,flowId,instanceId,flowName,instanceName);
			//==============添加审计start===================
			JSONObject jo = JSONObject.parseObject(returnMsg);
			boolean result = jo.getBoolean("result");
			new Thread(new Audit2Master(user.getName(),"定时任务",""+mapQuertz.get("quartzName"),"update",result?1:0,"添加job")).start();
			//==============添加审计end=====================
			return returnMsg;
		}catch(Exception e){
			//==============添加审计start===================
			new Thread(new Audit2Master(user.getName(),"定时任务",""+mapQuertz.get("quartzName"),"update",0,"添加job")).start();
			//==============添加审计end=====================
			e.printStackTrace();
			log.error("创建job失败", e);
			return MessageHelper.wrap("result",false,"message","创建job失败");		
		}
	}
	
	//某个定时任务的详细信息
	@RequestMapping(value = "getQuartz",method = RequestMethod.GET)
	@ResponseBody
	public String getQuartz(@RequestParam("quartzId") String quartzId){
		return quartzService.getQuartz(quartzId);
	}
	
	//某个定时任务的job列表
	@RequestMapping(value = "getJobsOfQuartz",method = RequestMethod.GET)
	@ResponseBody
	public String getJobsOfQuartz(@RequestParam("quartzId") String quartzId,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("pageSize") int pageSize,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		return quartzService.getJobsOfQuartz(pageNum,pageSize,quartzId,sortName,sortOrder);
	}
	
	//对单个job的操作
	@RequestMapping(value = "/OperateQuartzJobOne",method = RequestMethod.POST)
	@ResponseBody
	public String OperateQuartzJobOne(@RequestParam("jobName") String jobName,
			@RequestParam("op") String op){
		try{
			quartzService.OperateQuartzJobOne(jobName,op);
			return MessageHelper.wrap("result",true,"message",op+"job成功");
		}catch(Exception e){
			e.printStackTrace();
			log.error(op+"job失败", e);
			return MessageHelper.wrap("result",false,"message",op+"job失败");		
		}
	}
	//请求master接口，获取蓝图实例列表
	@RequestMapping(value = "listAllBlueprintInstances",method = RequestMethod.GET)
	@ResponseBody
	public String listAllBlueprintInstances(){
		RestTemplate restUtil = new RestTemplate();
		String query = "userId="+1;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/listAllBlueprintInstances?"+query, String.class);
		return result;
	}
	
	//请求master接口，获取蓝图实例的流程操作列表
	@RequestMapping(value = "listBpInstanceFlows",method = RequestMethod.GET)
	@ResponseBody
	public String listBpInstanceFlows(@RequestParam("blueprintInstanceId") String blueprintInstanceId){
		RestTemplate restUtil = new RestTemplate();
		String query = "blueprintInstanceId="+blueprintInstanceId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/getFlowsByInstance?"+query, String.class);
		return result;
	}
}
