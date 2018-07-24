package com.dc.appengine.appmaster.ws.server;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.service.impl.MonitorService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dcits.Common.entity.User;

@Controller
@RequestMapping("/ws/dashboard")
public class DashboardRestService {
	
	
	@Resource
	IBlueprintService blueprintService;
	
	@Resource
	IUserService userService;
	
	@Resource
	IApplicationService applicationService;
	
	@Autowired
	@Qualifier("resourceService")
	IResourceService resourceService;
	
	@Resource
	private MonitorService monitorService;
	
	@RequestMapping(value = "listAllBlueprintInstances", method = RequestMethod.GET)
	@ResponseBody
	public String listAllBlueprintInstances(@Context HttpServletRequest request) {
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg","can not list blueprintInstances if not logged in");
		}
		User user = (User) u;
		String resultStr = userService.getSonsOfUser(user.getId());
		Map<Long, String> map = JSON.parseObject(resultStr,
				new TypeReference<Map<Long, String>>() {
		});
		StringBuilder userIds = new StringBuilder();
		int index = 0;
		for (long unit : map.keySet()) {
			if (index != 0) {
				userIds.append(",");
			}
			index++;
			userIds.append("" + unit);
		}
		Map<String,Object> condition = new HashMap<>();
		condition.put("userIds", userIds.toString().split(","));
		List<Map<String,Object>> result = blueprintService.listAllBlueprintInstances(condition);
		return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "getFlows", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowsByBlueInstanceId(@RequestParam("blueInstanceId") String blueInstanceId) {
		List<Map<String, Object>> bpFlows = blueprintService.getFlowsByBlueInstanceId(blueInstanceId);
		return JSON.toJSONString(bpFlows);
	}
	
	@RequestMapping(value = "listApps", method = RequestMethod.GET)
	@ResponseBody
	public String listApps(@Context HttpServletRequest request,@RequestParam("id") long id) {
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg","can not list components if not logged in");
		}
		User user = (User) u;
		return applicationService.listApps(id,user.getId());
	}
	
	@RequestMapping(value = "getRecords", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String getRecords(@Context HttpServletRequest request,
							 @RequestParam(name="nodeDisplay",required=false) String nodeDisplay,
			                 @RequestParam(name="startDate",required=false) String startDate,
							 @RequestParam(name="endDate",required=false) String endDate,
							 @RequestParam(name="blueInstanceId",required=false) String blueInstanceId,
							 @RequestParam(name="blueInstanceName",required=false) String blueInstanceName,
							 @RequestParam(name="flowName",required=false) String flowName,
							 @RequestParam(name="flowId",required=false) String flowId,
							 @RequestParam(name="appName",required=false) String appName,
							 @RequestParam(name="componentName",required=false) String componentName,
							 @RequestParam(name="op",required=false) String op,
							 @RequestParam(name="sortOrder",required=false) String sortOrder,
							 @RequestParam(name="sortName",required=false) String sortName,
							 @RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
							 @RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat dfPage = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.ENGLISH);
		//获取组件子流程id
		Map<String,Object> map = new HashMap();
		map.put("resourceName", componentName);
		map.put("flowType", op);
		List<Map<String, Object>> subFlows = resourceService.getsubFlowInfo(map);
		Map<String,String> subFlowMap = new HashMap(); 
		for(Map<String, Object> subflowMp : subFlows){
			subFlowMap.put(subflowMp.get("ID")+"",subflowMp.get("FLOW_NAME")+""); 
		}
		
		//通过蓝图实例id,获取流程的运行实例
		Map<String,Object> params = new HashMap();
		params.put("id", blueInstanceId);
		params.put("flowName", flowName);
		params.put("startTime", startDate.trim());
		params.put("endTime", endDate.trim());
		List<Map<String,Object>> rtFlowList = blueprintService.getFlowInstanceIdsByMap(params);
		List<Map<String,Object>> returnList = new ArrayList();
		try {
			List<String> instanceIds = new ArrayList<>();
			Map<String,Object> blueInstanceMap = new HashMap<>();
			if(rtFlowList.size()>0){
				//遍历流程实例列表
				for(Map<String,Object> rtFlowMap : rtFlowList){
					String instanceId = "" + rtFlowMap.get("instanceId");
					instanceIds.add(instanceId);
					String trueBlueInsName = StringUtils.isEmpty(blueInstanceName)?""+rtFlowMap.get("instanceName"):blueInstanceName;
					String trueFlowName = StringUtils.isEmpty(flowName)?"" + rtFlowMap.get("flowName"):flowName;
					Map<String,String> blueInstanceInfo = new HashMap<>();
					blueInstanceInfo.put("trueBlueInsName", trueBlueInsName);
					blueInstanceInfo.put("trueFlowName", trueFlowName);
					blueInstanceMap.put(instanceId, blueInstanceInfo);
				}
				Map<String,Object> subFlowNodeMap = monitorService.getFlowNodesNew(instanceIds);
				if(subFlowNodeMap.size() !=0){
					for(Map.Entry<String, Object> entry:blueInstanceMap.entrySet()){
						String instanceId = entry.getKey();
						Map<String,String> blueInstanceInfo = (Map<String, String>) entry.getValue();
						String trueBlueInsName =blueInstanceInfo.get("trueBlueInsName");
						String trueFlowName =blueInstanceInfo.get("trueFlowName");
						//组件+key和组件子流程联合确定蓝图流程里的子流程
						List<Map<String, Object>> list = (List<Map<String, Object>>) subFlowNodeMap.get(instanceId);
						if(list==null||list.isEmpty()){
							continue;
						}
						List<Map<String,Object>> singleInstanceList = computeSubProcedure(null,subFlowMap,trueBlueInsName,trueFlowName,op,appName,nodeDisplay,list);
						if(singleInstanceList==null||singleInstanceList.size()==0){
							continue;
						}
						returnList.addAll(singleInstanceList);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		SortUtil.sort(returnList, SortUtil.getColunmName("dashboard", sortName), sortOrder);
		Map<String,Object> returnMap = prepareResult(returnList,pageSize,pageNum);
		return JSON.toJSONString(returnMap, SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
	}

	private Map<String,Object> prepareResult(List<Map<String,Object>> returnList,int pageSize,int pageNum) {
		Map<String,Object> returnMap = new HashMap();
		returnMap.put("data", returnList);
		int sNum = 0;
		int fNum = 0;
		//计算成功失败率
		for(Map<String,Object> finalMap : returnList){
			if(Boolean.valueOf(""+finalMap.get("result"))){
				sNum++;
			}else{
				fNum++;
			}
		}
		String succNum = null;
		String failedNum = null;
		if(sNum!=0 || fNum!=0){
			NumberFormat numFormat = NumberFormat.getPercentInstance();
			numFormat.setMinimumFractionDigits(2);
			if(sNum != 0){
				succNum = numFormat.format(Double.parseDouble(sNum+"")/Double.parseDouble(sNum+fNum+""));
			}else{
				succNum = "0.00";
			}
			if(fNum != 0){
				failedNum = numFormat.format(Double.parseDouble(fNum+"")/Double.parseDouble(sNum+fNum+""));
			}else{
				failedNum = "0.00";
			}
		}
		returnMap.put("succRate", succNum);
		returnMap.put("failRate", failedNum);
		returnMap.put("total", sNum + fNum);
		//分页
		int total = sNum + fNum;
		int fromIndex=(pageNum-1)*pageSize;
		int toIndex=(pageNum*pageSize)<total?pageNum*pageSize:total;
		returnList=returnList.subList(fromIndex, toIndex);
		returnMap.put("data", returnList);
		return returnMap;
	}

	private List<Map<String,Object>> computeSubProcedure(String monitorJson,Map<String,String> subflowMap,String blueInstanceName,String flowName,String op,String appName,String nodeDisplay,
			List<Map<String,Object>> subFlowNodes){
		List<Map<String,Object>> returnList = new CopyOnWriteArrayList<Map<String,Object>>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> monitorList =null;
		if(subFlowNodes !=null){
			monitorList=subFlowNodes;
		}else{
			JSONObject monitorObject = JSONObject.parseObject(monitorJson);
			JSONObject monitorMessage = JSONObject.parseObject(""+monitorObject.get("message"));
			String monitorStr = monitorMessage.getString("children");
			monitorList = JSONObject.parseObject(monitorStr, List.class);
		}
		for(Map<String,Object> monitorMap : monitorList){//遍历监控tree
//			String flowId = "" + monitorMap.get("flowId");
			String type = "" + monitorMap.get("type");
			List<Map<String,Object>> childrenList = (List) monitorMap.get("children");
			String childrenEndTime = null;
			if (childrenList.size() > 0) {
				 childrenEndTime = (String)childrenList.get(childrenList.size()-1).get("endTime");
			}
			Object parentEndTime = monitorMap.get("endTime");
			String mapEndTime = parentEndTime==null?childrenEndTime:parentEndTime+"";
			if (StringUtils.isEmpty(mapEndTime)) {
				continue;
			}
			if("0".equals(type)){//匹配flowId的子流程
				String nodeComment = "" + monitorMap.get("nodeComment");
				String cdflowId =nodeComment.substring(0,nodeComment.lastIndexOf("_"));
				cdflowId =cdflowId.substring(cdflowId.lastIndexOf("_")+1);
				if(subflowMap.keySet().contains(cdflowId)){
					String name = "" + monitorMap.get("name");
					String monitorNodeDisplay = name.substring(0,name.lastIndexOf("_"));//获取监控树种的nodeDisplay
					if(!StringUtils.isEmpty(nodeDisplay) && nodeDisplay.equals(monitorNodeDisplay) || StringUtils.isEmpty(nodeDisplay)){
						Map<String,Object> subMap = new HashMap();
						subMap.put("subFlowName",subflowMap.get(cdflowId));
						String trueOp = "";
						if(StringUtils.isEmpty(op)){
							trueOp = name.substring(name.lastIndexOf("_")+1,name.length());
						}else{
							trueOp = op;
						}
						String trueAppName = StringUtils.isEmpty(nodeDisplay)?monitorNodeDisplay:nodeDisplay;
						subMap.put("result","2".equals(""+monitorMap.get("state"))?true:false);
						subMap.put("nodeDisplay",trueAppName);
						subMap.put("blueInstanceName", blueInstanceName);
						subMap.put("flowName", flowName);
						subMap.put("op", trueOp);
						subMap.put("startTime",monitorMap.get("startTime"));
						subMap.put("endTime",mapEndTime);
						try {
							Date startTime = df.parse("" + subMap.get("startTime"));
							Date endTime = df.parse("" + subMap.get("endTime"));
							
							long interval = (endTime.getTime() - startTime.getTime())/1000;
							subMap.put("period", interval);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						returnList.add(subMap);
					}
				}
			}
		}
		return returnList;
	}
	public static void main(String[] args) {
		String a = "f56a49ba32fc48ab8e3c6aba6f01b373_20171219190713387_-2";
		System.out.println(a.substring(a.lastIndexOf("_")+1,a.length()));
		
	}
}
