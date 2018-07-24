package com.dc.appengine.cloudui.ws.server;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dcits.Common.entity.User;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Path("dashboard")
public class DashboardRestService {
	public WebTarget target;
	public Client client = ClientBuilder.newClient();
	
	@GET
	@Path("listAllBlueprintInstances")
	public String listAllBlueprintInstances(@Context HttpServletRequest request) {
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg",
					"can not list images if not logged in");
		}
		User user = (User) u;
		String userId = user.getId() + "";
		return WSRestClient.getMasterWebTarget().path("blueprint/listAllBlueprintInstances").queryParam("userId", userId).request()
				.get(String.class);
	}
	
	@GET
	@Path("listApps")
	public String listApps(@QueryParam("userId") String userId,@QueryParam("id") String id) {
		Form form = new Form();
		form.param("blue_instance_id", id);
		form.param("userId", userId);
		return WSRestClient.getMasterWebTarget().path("deployedApp/listApps").request()
		.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	@GET
	@Path("getCluster")
	public String getCluster(@Context HttpServletRequest request) {
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg",
					"can not list images if not logged in");
		}
		User user = (User) u;
		String userId = user.getId() + "";
		return WSRestClient.getAdapterWebTarget().path("cluster/master").queryParam("user_id", userId).request()
				.get(String.class);
	}
	
	@GET
	@Path("getComponent")
	public String getComponent(@QueryParam("userId") String userId,@QueryParam("clusterId") String clusterId,@QueryParam("clusterName") String clusterName) {
		Form form = new Form();
		form.param("clusterId", clusterId);
		form.param("userId", userId);
		return WSRestClient.getMasterWebTarget().path("deployedApp/listAppsOfCluster").request()
		.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	@GET
	@Path("getDeploySuccessRates")
	public String getDeploySuccessRates(@Context HttpServletRequest request,@QueryParam("startdate") String startdate,
			@QueryParam("enddate") String enddate,@QueryParam("blueInstanceId") String blueInstanceId,@QueryParam("appName") String appName) {
		/*Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg",
					"can not list images if not logged in");
		}
		User user = (User) u;
		String userId = user.getId() + "";*/
//		String userId = "1";
		/*
		 * 根据ma_application找flow_id
		 * 从ma_application中获取id关联到表ma_instance中的appId,从结果集中找到字段resource_version_id在管理表sv_version_flow表。
		 */
		NumberFormat df = NumberFormat.getPercentInstance();
		df.setMinimumFractionDigits(2);
		
		String subWorkflowid = WSRestClient.getMasterWebTarget().path("/configs/findNewFlowId")
										.queryParam("blueInstanceId", blueInstanceId)
										.queryParam("appName", appName)
										.queryParam("op", "deploy").request().get(String.class);
		List<Map<String,String>> subJsonObject = JSON.parseObject(subWorkflowid, List.class);//组件子流程flowId
		List<String> subFlowIdList = new ArrayList();
		for(Map<String,String> item : subJsonObject){
			subFlowIdList.add(String.valueOf(item.get("FLOWID")));
		}
		
		String workflowid = WSRestClient.getMasterWebTarget().path("/deployedApp/getFlowIdByBlueInstanceId")
								.queryParam("blueInstanceId", blueInstanceId).request().get(String.class);//父流程flowId
		
		List<Map<String,String>> jsonObject = JSON.parseObject(workflowid, List.class);
		Map<String, Object> json = new HashMap<>();
		List resultList = new ArrayList();
		for(Map<String,String> item : jsonObject){
			
			
			/*
			 * 根据子流程的flowId查找子流程实例id,然后根据子流程实例id查找成功数
			 */
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flowid", item.get("FLOWID"));
			map.put("op", "deploy");
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("componentName", appName+"_deploy");
			Form form = new Form();
			form.param("paramMap", JSON.toJSONString(map));
			String tempresult =  WSRestClient.getFrameWebTarget().path("WFService").path("findCDSuccessByMap.wf")
					.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
			Map<String, Object> rows = JSONObject.parseObject(tempresult,Map.class);
			List<Map<String,Object>> result = JSONObject.parseObject(rows.get("data").toString(),CopyOnWriteArrayList.class);
			
			
			//========================查找组件部署失败的次数===========================================================
			//查找父流程中组件的部署子流程
			List<String> targetSubFlowIdList = new ArrayList();
			String parentFlowInfo = item.get("FLOW_INFO");
			Map<String,Object> ssFlowInfoMap = JSON.parseObject(parentFlowInfo,new TypeReference<Map<String,Object>>(){});
			List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
			for(Map<String,Object> data:ssFlowInfoDatas){
				if(data.get("subflowid")!= null && subFlowIdList.contains(data.get("subflowid").toString()))
					targetSubFlowIdList.add(data.get("subflowid").toString());
			}
			
			int failNum = 0;
			//查询失败次数从ma_flow_log表中查询state=7
			for(String subFlowId : targetSubFlowIdList){
				//根据子流程id在工作流的表里查找子流程的实例id
				Map<String, Object> subMap = new HashMap<String, Object>();
				subMap.put("flowid", subFlowId);
				subMap.put("op", "deploy");
				subMap.put("startdate", startdate);
				subMap.put("enddate", enddate);
				subMap.put("componentName", appName+"_deploy");
				Form subForm = new Form();
				subForm.param("paramMap", JSON.toJSONString(subMap));
				String subResult =  WSRestClient.getFrameWebTarget().path("WFService").path("findSubProcedureId.wf")
						.request().post(Entity.entity(subForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
				Map<String, Object> subRows = JSONObject.parseObject(subResult,Map.class);
				List<String> subFlowResult = JSONObject.parseObject(subRows.get("data").toString(),List.class);
				for(String subFlowInstanceId : subFlowResult){//根据子流程实例id到ma_flow_log表中查找失败个数
					Form nodes = new Form();
					List<Map<String,Object>> comInfo = new ArrayList(); 
					Map<String,Object> nodesMap = new HashMap();
					nodesMap.put("TYPE", "0");//子流程
					nodesMap.put("ACTIVITYID", "subFlowNodeId");
					nodesMap.put("FLOWID", subFlowInstanceId);
					nodesMap.put("ID", subFlowInstanceId);
					comInfo.add(nodesMap);
					nodes.param("nodes", JSON.toJSONString(comInfo));
					nodes.param("flowId",subFlowInstanceId);
					String comInfoNew = WSRestClient.getMasterWebTarget().path("blueprint/getFlowNodeState")
							.request().post(Entity.entity(nodes,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
					List<Map<String,Object>> comInfoNewList = JSON.parseObject(comInfoNew, new TypeReference<List<Map<String,Object>>>(){});
					for(Map<String,Object> comInf : comInfoNewList){
						if("subFlowNodeId".equals(comInf.get("ACTIVITYID")) && null != comInf.get("STATE") &&  "7".equals(String.valueOf(comInf.get("STATE")))){
							failNum++;
						}
					}
				}
			}
			
			//===================================================================================
			
			for(Map<String,Object> object : result){
//				object.put("versionName", item.get("version_name"));
				object.put("appName", appName);
				object.put("startdate", startdate);
				object.put("enddate", enddate);
				object.put("flowName", item.get("FLOW_NAME"));
				if(failNum!=0){
					object.put("fNum", failNum);
				}
				
				double sNum = Double.parseDouble(object.get("sNum").toString());
				double fNum = Double.parseDouble(object.get("fNum").toString());
				if(sNum!=0 || fNum!=0){
					String succNum = null;
					String failedNum = null;
					if(sNum != 0){
						succNum = df.format(sNum/(sNum+fNum));
					}else{
						succNum = "0.00";
					}
					if(fNum != 0){
						failedNum = df.format(fNum/(sNum+fNum));
					}else{
						failedNum = "0.00";
					}
					object.put("sNum", succNum);
					object.put("fNum", failedNum);
					object.put("sum", sNum+fNum);
				}else{
					result.remove(object);
				}
			}
			resultList.addAll(result);
		}
		json.put("total", resultList.size());
		json.put("rows", resultList);
		return JSON.toJSONString(json, SerializerFeature.WriteDateUseDateFormat);
		
	}
	
	DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@GET
	@Path("getDeployTime")
	public String getDeployTime(@Context HttpServletRequest request,@QueryParam("startdate") String startdate,
			@QueryParam("enddate") String enddate,@QueryParam("blueInstanceId") String blueInstanceId,@QueryParam("appName") String appName) {
		/*
		 * 根据ma_application找flow_id
		 * 从ma_application中获取id关联到表ma_instance中的appId,从结果集中找到字段resource_version_id在管理表sv_version_flow表。
		 */
		String subWorkflowid = WSRestClient.getMasterWebTarget().path("/configs/findNewFlowId")
				.queryParam("blueInstanceId", blueInstanceId)
				.queryParam("appName", appName)
				.queryParam("op", "deploy").request().get(String.class);
		List<Map<String,String>> subJsonObject = JSON.parseObject(subWorkflowid, List.class);//组件子流程flowId
		List<String> subFlowIdList = new ArrayList();
		for(Map<String,String> item : subJsonObject){
			subFlowIdList.add(String.valueOf(item.get("FLOWID")));
		}

		String workflowid = WSRestClient.getMasterWebTarget().path("/deployedApp/getFlowIdByBlueInstanceId")
				.queryParam("blueInstanceId", blueInstanceId).request().get(String.class);
		
		List<Map<String,String>> jsonObject = JSON.parseObject(workflowid, List.class);
		Map<String, Object> json = new HashMap<>();
		List resultList = new ArrayList();
		for(Map<String,String> item : jsonObject){
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flowid", item.get("FLOWID"));
			map.put("op", "deploy");
			map.put("componentName", appName);
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("componentName", appName+"_deploy");
			Form form = new Form();
			form.param("paramMap", JSON.toJSONString(map));
			String tempresult =  WSRestClient.getFrameWebTarget().path("WFService").path("findCDPeriodsByMap.wf")
					.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
			Map<String, Object> rows = JSONObject.parseObject(tempresult,Map.class);
			List<Map<String,Object>> result = JSONObject.parseObject(rows.get("data").toString(),List.class);
			
			
			//========================查找组件部署失败的次数===========================================================
			//查找父流程中组件的部署子流程
			List<String> targetSubFlowIdList = new ArrayList();
			String parentFlowInfo = item.get("FLOW_INFO");
			Map<String,Object> ssFlowInfoMap = JSON.parseObject(parentFlowInfo,new TypeReference<Map<String,Object>>(){});
			List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
			for(Map<String,Object> data:ssFlowInfoDatas){
				if(data.get("subflowid")!= null && subFlowIdList.contains(data.get("subflowid").toString()))
					targetSubFlowIdList.add(data.get("subflowid").toString());
			}
			
			//查询失败次数从ma_flow_log表中查询state=7
			for(String subFlowId : targetSubFlowIdList){
				//根据子流程id在工作流的表里查找子流程的实例id
				Map<String, Object> subMap = new HashMap<String, Object>();
				subMap.put("flowid", subFlowId);
				subMap.put("op", "deploy");
				subMap.put("startdate", startdate);
				subMap.put("enddate", enddate);
				subMap.put("componentName", appName+"_deploy");
				Form subForm = new Form();
				subForm.param("paramMap", JSON.toJSONString(subMap));
				String subResult =  WSRestClient.getFrameWebTarget().path("WFService").path("findSubProcedureId.wf")
						.request().post(Entity.entity(subForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
				Map<String, Object> subRows = JSONObject.parseObject(subResult,Map.class);
				List<String> subFlowResult = JSONObject.parseObject(subRows.get("data").toString(),List.class);
				for(String subFlowInstanceId : subFlowResult){//根据子流程实例id到ma_flow_log表中查找失败个数
					Form nodes = new Form();
					List<Map<String,Object>> comInfo = new ArrayList(); 
					Map<String,Object> nodesMap = new HashMap();
					nodesMap.put("TYPE", "0");//子流程
					nodesMap.put("ACTIVITYID", "subFlowNodeId");
					nodesMap.put("FLOWID", subFlowInstanceId);
					nodesMap.put("ID", subFlowInstanceId);
					comInfo.add(nodesMap);
					nodes.param("nodes", JSON.toJSONString(comInfo));
					nodes.param("flowId", subFlowInstanceId);
					String comInfoNew = WSRestClient.getMasterWebTarget().path("blueprint/getFlowNodeState")
							.request().post(Entity.entity(nodes,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
					List<Map<String,Object>> comInfoNewList = JSON.parseObject(comInfoNew, new TypeReference<List<Map<String,Object>>>(){});
					for(Map<String,Object> comInf : comInfoNewList){
						if("subFlowNodeId".equals(comInf.get("ACTIVITYID")) && null != comInf.get("STATE") &&  "7".equals(String.valueOf(comInf.get("STATE")))){
							Map<String,Object> object = new HashMap();
							object.put("state", "7");
							object.put("appName", appName);
//							object.put("flowName", item.get("FLOW_NAME"));
							object.put("endTime", df.format(comInf.get("update_time")));
							String startDate = getStartDate(subFlowInstanceId);
							if(startDate!=null){
								object.put("starttime", startDate);
								long period = 0;
								period = (Long.parseLong(comInf.get("update_time")+"")-Timestamp.valueOf(startDate).getTime())/1000;
								object.put("period", period);
							}
							result.add(object);
						}
					}
				}
			}
			//===================================================================================
			for(Map<String,Object> object : result){
//				object.put("versionName", item.get("version_name"));
				object.put("appName", appName);
				object.put("startdate", startdate);
				object.put("enddate", enddate);
				object.put("flowName", item.get("FLOW_NAME"));
			}
			resultList.addAll(result);
		}
		json.put("total", resultList.size());
		json.put("rows", resultList);
		return JSON.toJSONString(json, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@GET
	@Path("getOPRecords")
	public String getOPRecords(@Context HttpServletRequest request,@QueryParam("startdate") String startdate,
			@QueryParam("enddate") String enddate,@QueryParam("blueInstanceId") String blueInstanceId,@QueryParam("appName") String appName,@QueryParam("op") String op) {
	
		/*
		 * 根据ma_application找flow_id
		 * 从ma_application中获取id关联到表ma_instance中的appId,从结果集中找到字段resource_version_id在管理表sv_version_flow表。
		 */
		String subWorkflowid = WSRestClient.getMasterWebTarget().path("/configs/findNewFlowId")
				.queryParam("blueInstanceId", blueInstanceId)
				.queryParam("appName", appName)
				.queryParam("op", op).request().get(String.class);
		List<Map<String,String>> subJsonObject = JSON.parseObject(subWorkflowid, List.class);//组件子流程flowId
		List<String> subFlowIdList = new ArrayList();
		for(Map<String,String> item : subJsonObject){
			subFlowIdList.add(String.valueOf(item.get("FLOWID")));
		}

		String workflowid = WSRestClient.getMasterWebTarget().path("/deployedApp/getFlowIdByBlueInstanceId")
				.queryParam("blueInstanceId", blueInstanceId).request().get(String.class);//父流程flowId
		
		List<Map<String,String>> jsonObject = JSON.parseObject(workflowid, List.class);
		Map<String, Object> json = new HashMap<>();
		List resultList = new ArrayList();
		for(Map<String,String> item : jsonObject){
			Map<String , Object> map = new HashMap<>();
			map.put("flowid", item.get("FLOWID"));
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("componentName", appName+"_"+ op);
			Form form = new Form();
			form.param("paramMap", JSON.toJSONString(map));
			String tempresult =  WSRestClient.getFrameWebTarget().path("WFService").path("findCDOprecordsByMap.wf")
					.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
			Map<String, Object> rows = JSONObject.parseObject(tempresult,Map.class);
			List<Map<String,Object>> result = JSONObject.parseObject(rows.get("data").toString(),List.class);
			
			//========================查找组件部署失败的次数===========================================================
			//查找父流程中组件的部署子流程
			List<String> targetSubFlowIdList = new ArrayList();
			String parentFlowInfo = item.get("FLOW_INFO");
			Map<String,Object> ssFlowInfoMap = JSON.parseObject(parentFlowInfo,new TypeReference<Map<String,Object>>(){});
			List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
			for(Map<String,Object> data:ssFlowInfoDatas){
				if(data.get("subflowid")!= null && subFlowIdList.contains(data.get("subflowid").toString()))
					targetSubFlowIdList.add(data.get("subflowid").toString());
			}
			
			//查询失败次数从ma_flow_log表中查询state=7
			for(String subFlowId : targetSubFlowIdList){
				//根据子流程id在工作流的表里查找子流程的实例id
				Map<String, Object> subMap = new HashMap<String, Object>();
				subMap.put("flowid", subFlowId);
				subMap.put("op", "deploy");
				subMap.put("startdate", startdate);
				subMap.put("enddate", enddate);
				subMap.put("componentName", appName+"_deploy");
				Form subForm = new Form();
				subForm.param("paramMap", JSON.toJSONString(subMap));
				String subResult =  WSRestClient.getFrameWebTarget().path("WFService").path("findSubProcedureId.wf")
						.request().post(Entity.entity(subForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
				Map<String, Object> subRows = JSONObject.parseObject(subResult,Map.class);
				List<String> subFlowResult = JSONObject.parseObject(subRows.get("data").toString(),List.class);
				for(String subFlowInstanceId : subFlowResult){//根据子流程实例id到ma_flow_log表中查找失败个数
					Form nodes = new Form();
					List<Map<String,Object>> comInfo = new ArrayList(); 
					Map<String,Object> nodesMap = new HashMap();
					nodesMap.put("TYPE", "0");//子流程
					nodesMap.put("ACTIVITYID", "subFlowNodeId");
					nodesMap.put("FLOWID", subFlowInstanceId);
					nodesMap.put("ID", subFlowInstanceId);
					comInfo.add(nodesMap);
					nodes.param("nodes", JSON.toJSONString(comInfo));
					nodes.param("flowId", subFlowInstanceId);
					String comInfoNew = WSRestClient.getMasterWebTarget().path("blueprint/getFlowNodeState")
							.request().post(Entity.entity(nodes,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
					List<Map<String,Object>> comInfoNewList = JSON.parseObject(comInfoNew, new TypeReference<List<Map<String,Object>>>(){});
					for(Map<String,Object> comInf : comInfoNewList){
						if("subFlowNodeId".equals(comInf.get("ACTIVITYID")) && null != comInf.get("STATE") &&  "7".equals(String.valueOf(comInf.get("STATE")))){
							Map<String,Object> failMap = new HashMap();
							failMap.put("componentName", appName);
							failMap.put("endTime", df.format(comInf.get("update_time")));
							failMap.put("state", 7);
//							map.put("flowid", item.get("FLOWID"));
							String startDate = getStartDate(subFlowInstanceId);
							if(startDate!=null)
								failMap.put("starttime", startDate);
							result.add(failMap);
						}
					}
				}
			}
			//===================================================================================
			for(Map<String,Object> object : result){
//				object.put("versionName", item.get("version_name"));
				object.put("op", op);
				object.put("flowName", item.get("FLOW_NAME"));
				object.put("componentName", appName);
			}
			resultList.addAll(result);
			
		}
		json.put("total", resultList.size());
		json.put("rows", resultList);
		return JSON.toJSONString(json, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@GET
	@Path("getComponentForm")
	public String getComponentForm(@Context HttpServletRequest request,@QueryParam("startdate") String startdate,
			@QueryParam("enddate") String enddate,@QueryParam("blueInstanceId") String blueInstanceId,@QueryParam("appName") String appName) {
			
		//部署信息
		String deployMsg = getExecMsg(getFlowId(blueInstanceId),"deploy",appName,startdate,enddate,blueInstanceId);
		//启动信息
		String startMsg = getExecMsg(getFlowId(blueInstanceId),"start",appName,startdate,enddate,blueInstanceId);
		//停止信息
		String stopMsg = getExecMsg(getFlowId(blueInstanceId),"stop",appName,startdate,enddate,blueInstanceId);
		//卸载信息
		String destroyMsg = getExecMsg(getFlowId(blueInstanceId),"destroy",appName,startdate,enddate,blueInstanceId);
		//升级信息
		String upgradeMsg = getExecMsg(getFlowId(blueInstanceId),"upgrade",appName,startdate,enddate,blueInstanceId);
		//回滚信息
		String rollbackMsg = getExecMsg(getFlowId(blueInstanceId),"rollback",appName,startdate,enddate,blueInstanceId);
		//快照信息
		String snapshotMsg = getExecMsg(getFlowId(blueInstanceId),"snapshot",appName,startdate,enddate,blueInstanceId);
		
		
		Map<String, Object> json = new HashMap<>();
		json.put("total", 7);
		json.put("deployMsg", change(deployMsg));
		json.put("startMsg", change(startMsg));
		json.put("stopMsg", change(stopMsg));
		json.put("destroyMsg", change(destroyMsg));
		json.put("upgradeMsg", change(upgradeMsg));
		json.put("rollbackMsg", change(rollbackMsg));
		json.put("snapshotMsg", change(snapshotMsg));
		return JSON.toJSONString(json, SerializerFeature.WriteDateUseDateFormat);

		
	}
	
	public List<Object> change(String msg){
//		Map<String, Object> Map = JSONObject.parseObject(msg,Map.class);
		List<Object> list = JSONObject.parseObject(msg,List.class);
		return list;
	}
	//重新组装map，根据flowId查找组件，在根据组件查找环境
	/*public List<Map<String, Object>> getResultMap(List<Map<String, Object>> flowlist,String user_id){
		//查询cluster
		String response = WSRestClient.getAdapterWebTarget().path("cluster/master").queryParam("user_id", user_id).request()
				.get(String.class);
		List<Map<String, Object>> list = JSONObject.parseObject(response,List.class);
		for(Map<String, Object> map :list){
			Form form = new Form();
			form.param("clusterId", (String) map.get("id"));
			form.param("userid", (String) map.get("user_id"));
			//查询component
			String component = WSRestClient.getMasterWebTarget().path("deployedApp/listAppsOfCluster").request()
			.put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
			List<Map<String, Object>> list1 = JSONObject.parseObject(component,List.class);
			for(Map<String, Object> map1 :list1){
				//重新组装装Map
				for(Map<String, Object> flowmap :flowlist){
					if(flowmap.get("flowId").toString().equals(map1.get("depolyId").toString())){
						flowmap.put("component", map1.get("appName").toString());
						flowmap.put("componentId", map1.get("id").toString());
						flowmap.put("cluster", map.get("name").toString());
						flowmap.put("clusterId", map.get("id").toString());
						flowmap.put("op","部署");
					}else if (flowmap.get("flowId").toString().equals(map1.get("startId").toString())){
						flowmap.put("component", map1.get("appName").toString());
						flowmap.put("componentId", map1.get("id").toString());
						flowmap.put("cluster", map.get("name").toString());
						flowmap.put("clusterId", map.get("id").toString());
						flowmap.put("op","启动");
					}else if (flowmap.get("flowId").toString().equals(map1.get("stopId").toString())){
						flowmap.put("component", map1.get("appName").toString());
						flowmap.put("componentId", map1.get("id").toString());
						flowmap.put("cluster", map.get("name").toString());
						flowmap.put("clusterId", map.get("id").toString());
						flowmap.put("op","停止");
					}else if (flowmap.get("flowId").toString().equals(map1.get("destroyId").toString())){
						flowmap.put("component", map1.get("appName").toString());
						flowmap.put("componentId", map1.get("id").toString());
						flowmap.put("cluster", map.get("name").toString());
						flowmap.put("clusterId", map.get("id").toString());
						flowmap.put("op","卸载");
					}
					
				}
				
			}
		}
		return flowlist;
	}*/
	
	public String getFlowId(String blueInstanceId){
//		String flowid = WSRestClient.getMasterWebTarget().path("/configs/findNewFlowId")
//				.queryParam("blueInstanceId", blueInstanceId)
//				.queryParam("appName", appName)
//				.queryParam("op", op).request().get(String.class);
		String flowid = WSRestClient.getMasterWebTarget().path("/deployedApp/getFlowIdByBlueInstanceId")
				.queryParam("blueInstanceId", blueInstanceId).request().get(String.class);
		return flowid;
	}
	
	public String getExecMsg(String flowid ,String op,String appName,String startdate,String enddate,String blueInstanceId){
		
		String subWorkflowid = WSRestClient.getMasterWebTarget().path("/configs/findNewFlowId")
				.queryParam("blueInstanceId", blueInstanceId)
				.queryParam("appName", appName)
				.queryParam("op", op).request().get(String.class);
		List<Map<String,String>> subJsonObject = JSON.parseObject(subWorkflowid, List.class);//组件子流程flowId
		List<String> subFlowIdList = new ArrayList();
		for(Map<String,String> item : subJsonObject){
			subFlowIdList.add(String.valueOf(item.get("FLOWID")));
		}
	
		List<Map<String,Object>> result = new ArrayList();
		List<Map<String,String>> jsonObject = JSON.parseObject(flowid, List.class);
		for(Map<String,String> item : jsonObject){
			Map<String , Object> map = new HashMap<>();
			map.put("flowid", item.get("FLOWID"));
			map.put("startdate", startdate);
			map.put("enddate", enddate);
			map.put("componentName", appName+"_"+ op);
			Form form = new Form();
			form.param("paramMap", JSON.toJSONString(map));
			String tempresult =  WSRestClient.getFrameWebTarget().path("WFService").path("findCDOprecordsByMap.wf")
					.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
			Map<String, Object> rows = JSONObject.parseObject(tempresult,Map.class);
			List<Map<String,Object>> resultList = JSONObject.parseObject(rows.get("data").toString(),List.class);
			for(Map<String,Object> m : resultList){
				m.put("flowName", item.get("FLOW_NAME"));
//				m.put("versionName", item.get("version_name"));
				m.put("componentName", appName);
			}
			result.addAll(resultList);
			
			//========================查找组件部署失败的次数===========================================================
			//查找父流程中组件的部署子流程
			List<String> targetSubFlowIdList = new ArrayList();
			String parentFlowInfo = item.get("FLOW_INFO");
			Map<String,Object> ssFlowInfoMap = JSON.parseObject(parentFlowInfo,new TypeReference<Map<String,Object>>(){});
			List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
			for(Map<String,Object> data:ssFlowInfoDatas){
				if(data.get("subflowid")!= null && subFlowIdList.contains(data.get("subflowid").toString()))
					targetSubFlowIdList.add(data.get("subflowid").toString());
			}
			
			//查询失败次数从ma_flow_log表中查询state=7
			for(String subFlowId : targetSubFlowIdList){
				//根据子流程id在工作流的表里查找子流程的实例id
				Map<String, Object> subMap = new HashMap<String, Object>();
				subMap.put("flowid", subFlowId);
				subMap.put("op", "deploy");
				subMap.put("startdate", startdate);
				subMap.put("enddate", enddate);
				subMap.put("componentName", appName+"_deploy");
				Form subForm = new Form();
				subForm.param("paramMap", JSON.toJSONString(subMap));
				String subResult =  WSRestClient.getFrameWebTarget().path("WFService").path("findSubProcedureId.wf")
						.request().post(Entity.entity(subForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
				Map<String, Object> subRows = JSONObject.parseObject(subResult,Map.class);
				List<String> subFlowResult = JSONObject.parseObject(subRows.get("data").toString(),List.class);
				for(String subFlowInstanceId : subFlowResult){//根据子流程实例id到ma_flow_log表中查找失败个数
					Form nodes = new Form();
					List<Map<String,Object>> comInfo = new ArrayList(); 
					Map<String,Object> nodesMap = new HashMap();
					nodesMap.put("TYPE", "0");//子流程
					nodesMap.put("ACTIVITYID", "subFlowNodeId");
					nodesMap.put("FLOWID", subFlowInstanceId);
					nodesMap.put("ID", subFlowInstanceId);
					comInfo.add(nodesMap);
					nodes.param("nodes", JSON.toJSONString(comInfo));
					nodes.param("flowId", subFlowInstanceId);
					String comInfoNew = WSRestClient.getMasterWebTarget().path("blueprint/getFlowNodeState")
							.request().post(Entity.entity(nodes,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
					List<Map<String,Object>> comInfoNewList = JSON.parseObject(comInfoNew, new TypeReference<List<Map<String,Object>>>(){});
					for(Map<String,Object> comInf : comInfoNewList){
						if("subFlowNodeId".equals(comInf.get("ACTIVITYID")) && null != comInf.get("STATE") &&  "7".equals(String.valueOf(comInf.get("STATE")))){
							Map<String,Object> failMap = new HashMap();
							failMap.put("componentName", appName);
							failMap.put("endTime", df.format(comInf.get("update_time")));
							failMap.put("state", 7);
//							failMap.put("flowid", item.get("FLOWID"));
							failMap.put("flowName", item.get("FLOW_NAME"));
							String startDate = getStartDate(subFlowInstanceId);
							if(startDate!=null)
								failMap.put("starttime", startDate);
							result.add(failMap);
						}
					}
				}
			}
			//===================================================================================
		}
		return JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
	}
	
	@POST
	@Path("test")
	public void test(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("starttime", "2017-04-12 12:12:12");
		map.put("endtime", "2017-04-25 12:12:12");
		map.put("workflowid", "f02d47b9b71641ee99dce3f4c8537da0_-4");
		map.put("pagesize", "10");
		map.put("pagenumber", "1");
		map.put("op", "download");
		Form form = new Form();
		form.param("paramMap", JSON.toJSONString(map));
		System.out.println(JSON.toJSONString(map));
		String result =  WSRestClient.getFrameWebTarget()
				.path("WFService").path("findCDResultByMap.wf")
				.request()
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						String.class);
	}
	
	private String getStartDate(String subFlowInstanceId){
		Map<String , Object> map = new HashMap<>();
		map.put("flowinstanceid", subFlowInstanceId);
		Form subForm = new Form();
		subForm.param("paramMap", JSON.toJSONString(map));
		String result =  WSRestClient.getFrameWebTarget().path("WFService").path("findErrorSubProcedure.wf")
				.request().post(Entity.entity(subForm,MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
		Map<String, Object> rows = JSONObject.parseObject(result,Map.class);
		List<String> resultList = JSONObject.parseObject(rows.get("data").toString(),List.class);
		for(String update_time : resultList){
			return update_time;
		}
		return null;
	}
}
