package com.dc.appengine.appmaster.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.MongoLog;
import com.dc.appengine.appmaster.utils.MongoUtil;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.appengine.appmaster.utils.SortUtil;

@Component("MonitorService")
public class MonitorService {
	@Resource
	IBlueprintService blueprintService;
	@Resource
	IBlueprintDao blueprintDao;
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;

	private static final Logger log = LoggerFactory.getLogger(MonitorService.class);
	
	public String getProcessMonitorInfo(String id, String flowId) throws Exception {
		// 获取流程信息from master
		String processJson = blueprintService.getFlowInfo(flowId);
		Map<String, Object> processJsonMap = JSONObject.parseObject(processJson);
		Map<String, Object> flowInfo = (Map<String, Object>) processJsonMap.get("flowInfo");// 流程信息

		// 获取运行实例id的组件状态信息 from frame
		Map<String, Object> map = new HashMap<>();
		map.put("instanceid", id);
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();;
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("paramMap", JSON.toJSONString(map));
		String processStateJson = restUtil.postForObject(flowServerUrl+"/WFService/findProcessMonitorInfoByMap.wf", requestEntity, String.class);
		Map<String, Object> processStateJsonMap = JSON.parseObject(processStateJson);
		Boolean state = (Boolean) processStateJsonMap.get("state");
		if (!state) {
			return MessageHelper.wrap("result", false, "message", "请求frame，获取流程监控信息失败！");
		}
		List<?> nodeMonitorInfo = JSON.parseArray(processStateJsonMap.get("data").toString(), Map.class);
		List<Map<String, Object>> nodeDataArray = (List<Map<String, Object>>) flowInfo.get("nodeDataArray");// 流程节点
		String bluePrintId = flowInfo.get("bluePrintId").toString();// 流程id
		Map<Integer, Object> nodeKeyMap = new HashMap<>();
		Map<String, Object> poolsIns = new HashMap<>();
		getPoolsIns(id, nodeDataArray,poolsIns);//获取资源池的ins
		getNodeKeyMap(nodeDataArray, nodeKeyMap);
		flowInfo.put("nodeDataArray",
				countNodeState(bluePrintId,nodeDataArray, (List<Map<String, Object>>) nodeMonitorInfo,nodeKeyMap,poolsIns));
		return MessageHelper.wrap("result", true, "message", JSON.toJSONString(flowInfo));
	}

	public List<Map<String, Object>> countNodeState(String bluePrintId, List<Map<String, Object>> nodeDataArray,
			List<Map<String, Object>> monitorInfo,Map<Integer, Object> nodeKeyMap,Map<String, Object> poolsIns) {
		List<Map<String, Object>> result = new ArrayList<>();
		// 设置装态
		for (Map<String, Object> node : nodeDataArray) {
			Map<String, Object> nodeModify = new HashMap<>();
			nodeModify.putAll(node);
			Object cdFlowid = node.get("cdFlowId");
			String identityMark = "";
			String key = node.get("key").toString();
			if (cdFlowid != null) {
				identityMark = bluePrintId + "_" + cdFlowid + "_" + key;
			} else {
				identityMark = bluePrintId + "_" + key;
			}
			Object flowcontroltype = node.get("flowcontroltype");
			int instotal = isEmpty(node.get("ins"))? 1 : (Integer) node.get("ins");
			Object groupKey = node.get("group");
			if(flowcontroltype !=null && "10".equals(flowcontroltype.toString())){//资源池,获取真实的ins
				String oldKey=node.get("oldkey").toString();
				instotal = Integer.valueOf(poolsIns.get(oldKey).toString());
			}else{
				if(groupKey !=null){
					Map<String,Object> pool =searchPoolByKey(Integer.valueOf(groupKey.toString()), nodeKeyMap);
					if(pool !=null){
						String oldKey=pool.get("oldkey").toString();
						instotal = Integer.valueOf(poolsIns.get(oldKey).toString());
					}
				}
			}
			int successCount = 0;
			int failCount = 0;
			int doingCount = 0;
			int undoCount = 0;
			int insRT=0;
			String nodeState = "7";
			for (Map<String, Object> info : monitorInfo) {
				Object nodeCommend = info.get("nodeComment");
				String state = ""; // success=2,fail=7,doing=0,undo=-1
				state = info.get("state").toString();
				if (nodeCommend != null && identityMark.equals(nodeCommend.toString())) {// 对比唯一标识（blueId_key）
					insRT++;
					if ("2".equals(state)) {
						successCount++;
					} else if ("7".equals(state)) {
						failCount++;
					} else if ("0".equals(state)) {
						doingCount++;
					}
				}
			}
			if(groupKey==null && insRT>instotal){//非组节点的instotal(不包pool)，由运行时决定，解决循环节点的总数计算问题
				instotal=insRT;
			}
			undoCount = instotal - successCount - failCount - doingCount;
			if(failCount >0){
				nodeState = "7";// 执行失败
			}else if (instotal == successCount) {
				nodeState = "2";// 执行成功
			} else if (instotal==undoCount) {
				nodeState = "-1";// 未执行
			} else if (doingCount > 0 || (instotal !=undoCount && undoCount>0) ) {
				nodeState = "0";// 正在执行
			}
			nodeModify.put("successCount", successCount);
			nodeModify.put("failCount", failCount);
			nodeModify.put("doingCount", doingCount);
			nodeModify.put("undoCount", undoCount);
			nodeModify.put("state", nodeState);
			nodeModify.put("ins", instotal);
			result.add(nodeModify);
		}
		return result;
	}

	public List<Map<String, Object>> countNodeLinkState(List<Map<String, Object>> nodeLinkArray,Map<String, Object> nodeStateMap) {
		for (Map<String, Object> link : nodeLinkArray) {
			String from = link.get("from").toString();
			String to = link.get("to").toString();
			Object fromState = nodeStateMap.get(from);
			Object toState = nodeStateMap.get(to);
			String linkState = "-1";// 未执行
			if (!isEmpty(fromState) && !"-1".equals(fromState) && !isEmpty(toState) && !"-1".equals(toState)) {
				linkState = "2";
			}
			link.put("state", linkState);
		}
		return nodeLinkArray;
	}

	public String getFlowNodes(String id, String flowId) {
		Map<String, Object> map = new HashMap<>();
		map.put("instanceid", id);
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("paramMap", JSON.toJSONString(map));
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
		String nodesJson = restUtil.postForObject(flowServerUrl+"/WFService/getCDFlowNodes.wf", requestEntity, String.class);
		Map<String, Object> nodesJsonMap = JSON.parseObject(nodesJson);
		Boolean state = (Boolean) nodesJsonMap.get("state");
		if (!state) {
			log.error("请求frame异常！");
			return MessageHelper.wrap("result", false, "message", nodesJsonMap.get("errMessage"));
		}
		String nodes = nodesJsonMap.get("data").toString();
		return MessageHelper.wrap("result", true, "message", nodes);
	}

	public static Boolean isEmpty(Object param) {
		if (param instanceof String) {
			if (param == null || "".equals(param)) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public String meddleFlow(String tokenId, String state) {
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();;
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("workitemID", tokenId);
		requestEntity.add("userID", "admin");
		requestEntity.add("insvarMap", JSON.toJSONString(new HashMap<>()));//为空
		requestEntity.add("nextUserIDs", JSON.toJSONString(new HashMap<>()));//为空
		requestEntity.add("taskvarMap", JSON.toJSONString(new HashMap<>()));//为空
		requestEntity.add("isdelToken", "true");//固定值，true
		String result = restUtil.postForObject(flowServerUrl+"/WFService/reexecuteWorkitem.wf", requestEntity, String.class);
		Map<String, Object> jsonMap = JSON.parseObject(result);
		Boolean resultState = (Boolean) jsonMap.get("state");
		if (!resultState) {
			log.error("请求frame异常,流程干预失败！");
			return MessageHelper.wrap("result", false, "message", "请求frame异常,流程干预失败！");
		}else{
			return MessageHelper.wrap("result", true, "message", "流程干预正在执行！");
		}
	}
	public void getPoolsIns(String instanceId,List<Map<String, Object>> nodeDataArray,Map<String, Object> poolsIns){
		for(Map<String,Object> node:nodeDataArray){
			Object flowcontroltype = node.get("flowcontroltype");
			if(flowcontroltype !=null && "10".equals(flowcontroltype.toString())){//资源池,获取真实的ins
				int poolIns =1;
				String oldKey=node.get("oldkey").toString();
				Map<String, Object> blueInstance = blueprintDao.getFlowRecordByInstanceId(instanceId);
				if(blueInstance!=null){
					if(blueInstance.get("POOLCONFIG") !=null){
						Map<String, Object> pools = JSON.parseObject(blueInstance.get("POOLCONFIG").toString());
						if(pools !=null && pools.size()>0){
							Map<String, Object> pool = (Map<String, Object>)pools.get(oldKey);
							poolIns = Integer.valueOf(pool.get("ins").toString());
						}
					}
				}
				poolsIns.put(oldKey, poolIns);
			}
		}
	}

	public String endInstance(String instanceId) {
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();;
		MultiValueMap<String, String> reqEntity = new LinkedMultiValueMap<>();
		reqEntity.add("instanceId", instanceId);
		String res = restUtil.postForObject(flowServerUrl+"/WFService/isRunning.wf", reqEntity, String.class);
		Map<String, Object> resMap = JSON.parseObject(res);
		Boolean resState = (Boolean) resMap.get("data");
		if(!resState){
			return MessageHelper.wrap("result", false, "message", "流程【"+instanceId+"】已结束！");
		}else{
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("instanceID", instanceId);
			String result = restUtil.postForObject(flowServerUrl+"/WFService/endInstance.wf", requestEntity, String.class);
			Map<String, Object> jsonMap = JSON.parseObject(result);
			Boolean resultState = (Boolean) jsonMap.get("state");
			if (!resultState) {
				log.error("请求frame异常,结束流程失败！");
				Map<String, Object> errmsg = (JSONObject)jsonMap.get("errmsg");
				return MessageHelper.wrap("result", false, "message", (String)errmsg.get("message"));
			}else{
				return MessageHelper.wrap("result", true, "message", "正在结束流程！");
			}
		}
	
	}

	public String getFlowTree(String flowInstanceId, String flowId) {
		// TODO Auto-generated method stub
		MongoLog mongoLog = MongoUtil.getInstance();
		return JSON.toJSONString(mongoLog.generateFlowTree(flowInstanceId, flowId), SerializerFeature.WriteDateUseDateFormat,SerializerFeature.DisableCircularReferenceDetect);
	}

	public String getFlowMonitorInfo(String flowInstanceId, String flowId) {

		// 获取流程信息from master
		String processJson = blueprintService.getFlowInfo(flowId);
		Map<String, Object> processJsonMap = JSONObject.parseObject(processJson);
		Map<String, Object> flowInfo = (Map<String, Object>) processJsonMap.get("flowInfo");// 流程信息

		//获取节点信息
		MongoLog mongoLog = MongoUtil.getInstance();
		List<?> nodeMonitorInfo = mongoLog.getFlowNodes(flowInstanceId,flowId);
		List<Map<String, Object>> nodeDataArray = (List<Map<String, Object>>) flowInfo.get("nodeDataArray");// 流程节点
		flowInfo.put("nodeDataArray",
				countNodeStateNew(nodeDataArray, (List<Map<String, Object>>) nodeMonitorInfo));
		return MessageHelper.wrap("result", true, "message", JSON.toJSONString(flowInfo));
	}
	public List<Map<String, Object>> countNodeStateNew(List<Map<String, Object>> nodeDataArray,List<Map<String, Object>> monitorInfo) {
		List<Map<String, Object>> result = new ArrayList<>();
		// 设置装态
		for (Map<String, Object> node : nodeDataArray) {
			Map<String, Object> nodeModify = new HashMap<>();
			nodeModify.putAll(node);
			Object cdFlowid = node.get("cdFlowId");
			String identityMark = node.get("textKey").toString();
			int instotal = isEmpty(node.get("ins"))? 1 : (Integer) node.get("ins");
			int successCount = 0;
			int failCount = 0;
			int doingCount = 0;
			int undoCount = 0;
			String nodeState = "7";
			for (Map<String, Object> info : monitorInfo) {
				Object nodeCommend = info.get("nodeComment");
				String state = ""; // success=2,fail=7,doing=0,undo=-1
				if (info.get("startTime") != null && info.get("endTime") != null && info.get("state") != null
						&& "0".equals(info.get("state").toString())) {// 控流程的状态特殊处理
					state = "2";
				} else {
					state = info.get("state").toString();
				}
				if (nodeCommend != null && identityMark.equals(nodeCommend.toString())) {// 对比唯一标识（blueId_key）
					if ("2".equals(state)) {
						successCount++;
					} else if ("7".equals(state)) {
						failCount++;
					} else if ("0".equals(state)) {
						doingCount++;
					}
				}
			}
			undoCount = instotal - successCount - failCount - doingCount;
			if (successCount == 0 && doingCount == 0 && failCount == 0) {
				nodeState = "-1";// 未执行
			} else if (successCount == instotal) {
				nodeState = "2";// 执行成功
			} else if (doingCount > 0) {
				nodeState = "0";// 正在执行
			} else {
				nodeState = "7";// 执行失败
			}
			nodeModify.put("successCount", successCount);
			nodeModify.put("failCount", failCount);
			nodeModify.put("doingCount", doingCount);
			nodeModify.put("undoCount", undoCount);
			nodeModify.put("state", nodeState);
			nodeModify.put("ins", instotal);
			result.add(nodeModify);
		}
		return result;
	}
	
	public void getNodeKeyMap(List<Map<String, Object>> nodeDataArray,Map<Integer, Object> nodeKeyMap){
		for(Map<String,Object> node:nodeDataArray){
			Integer key = (Integer) node.get("key");
			nodeKeyMap.put(key, node);
		}
	}
	
	private Map<String, Object> searchPoolByKey(int key, Map<Integer, Object> keyMap) {
		Map<String, Object> keyObject = (Map<String, Object>)keyMap.get(key);
		if(10==(Integer)keyObject.get("flowcontroltype")){
			return keyObject;
		}else if(10==(Integer)keyObject.get("flowcontroltype")){
			int groupKey = Integer.valueOf(keyObject.get("group").toString());
			return searchPoolByKey(groupKey, keyMap);
		}else{
			return null;
		}
	}
	
	public static Object changeStatusValue(String status){
		switch(status){
			case "正在执行":
				return 1;
			case "成功":
				return 2;
			case "失败":
				return 7;
			case "1":
				return "正在执行";
			case "2":
				return "成功";
			case "7":
				return "失败";
			default:
				return null;
			}
	}
	
	public Map<String,Object> getFlowNodesNew(List<String> instanceIds) {
		Map<String, Object> map = new HashMap<>();
		if(JudgeUtil.isEmpty(instanceIds)){
			instanceIds= new ArrayList<>();
		}
		map.put("instanceIds", instanceIds);
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("params", JSON.toJSONString(map));
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
		String nodesJson = restUtil.postForObject(flowServerUrl+"/WFService/getSubFlowNodes.wf", requestEntity, String.class);
		Map<String, Object> nodesJsonMap = JSON.parseObject(nodesJson);
		Boolean state = (Boolean) nodesJsonMap.get("state");
		if (!state) {
			return new HashMap<>();
		}
		String nodes = nodesJsonMap.get("data").toString();
		return JSON.parseObject(nodes, new TypeReference<Map<String,Object>>(){});
	}
	/*
	 * intervalType 区间类型 year,quarter,month 对应粒度类型 月，周，日
	 * start  区间开始时间（时间戳）
	 * end    区间结束时间（时间戳）
	 * series 展示哪几条线，例如claimCar_SIT，和蓝图实例名一一对应 
	 * releaseTypes 发布类型 构建，部署，构建+部署和蓝图过程一一对应
	 * indicatorType 指标类型 frequency success averageTime actualTime
	 */
	public String getDeployIndicators(List series,List releaseTypes,String intervalType,String start,String end,
			String indicatorType){
		List<Map> list = new ArrayList<>();
		if(JudgeUtil.isEmpty(releaseTypes)){
			releaseTypes=null;
		}
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");
		try {
			List<String> xCoordinate = new ArrayList<>();
			Calendar ca = Calendar.getInstance();
			if(!"actualTime".equals(indicatorType)){
				if("quarter".equals(intervalType)){
					ca.setFirstDayOfWeek(Calendar.SUNDAY);
					ca.setTime(format1.parse(start));
					int startDay = ca.get(Calendar.DAY_OF_WEEK);
					if(startDay!=1){
						ca.add(Calendar.DATE, 1-startDay);
						start=format1.format(ca.getTime());
					}
					ca.setTime(format1.parse(end));
					ca.add(Calendar.DATE, -1);
					int endDay = ca.get(Calendar.DAY_OF_WEEK);
					if(endDay!=7){
						ca.add(Calendar.DATE, 7-startDay+1);
						end=format1.format(ca.getTime());
					}
					for(String st=start;st.compareTo(end)<0;){
						ca.setTime(format1.parse(st));
						String sDay=format2.format(ca.getTime().getTime());
						int day = ca.get(Calendar.DAY_OF_WEEK);
						ca.add(Calendar.DATE, 7-day);
						String eDay=format2.format(ca.getTime().getTime());
						xCoordinate.add(sDay+"-"+eDay);
						ca.add(Calendar.DATE, 1);
						st=format1.format(ca.getTime().getTime());
					}
				}else if("year".equals(intervalType)){//年图
					ca.setTime(format1.parse(start));
					int year = ca.get(Calendar.YEAR);
					for(int i=1;i<=12;i++){
						if(i<10){
							xCoordinate.add(year+".0"+i);
						}else{
							xCoordinate.add(year+"."+i);
						}
					}
				}else if("month".equals(intervalType)){//月图
					SimpleDateFormat  format= format2;
					ca.setTime(format1.parse(start));
				    ca.add(Calendar.MONTH, 1);  
				     ca.set(Calendar.DAY_OF_MONTH, 0);  
				     String lastday = format.format(ca.getTime()); 
				     String [] time = lastday.split("\\.");
				     Integer max = Integer.valueOf(time[2]);
				     for(int i=1;i<=max;i++){
							if(i<10){
								xCoordinate.add(time[0]+"."+time[1]+".0"+i);
							}else{
								xCoordinate.add(time[0]+"."+time[1]+"."+i);
							}
						}
				}
			}else{
				for(String st=start;st.compareTo(end)<0;){
					ca.setTime(format1.parse(st));
					String sDay=format3.format(ca.getTime().getTime());
					xCoordinate.add(sDay);
					ca.add(Calendar.DATE, 1);
					st=format1.format(ca.getTime().getTime());
				}
			}
			if("frequency".equals(indicatorType)){
				return getFrequency(series, releaseTypes, intervalType, start, end,xCoordinate);
			}else if("success".equals(indicatorType)){//获取发布成功数
				return getSuccess(series, releaseTypes, intervalType, start, end,xCoordinate);
			}else if("actualTime".equals(indicatorType)){//获取实际时长
				return getActualTime(series, releaseTypes, intervalType, start, end,indicatorType,xCoordinate);
			}else if("averageTime".equals(indicatorType)){//获取发布平均时长
				return getAverageTime(series, releaseTypes, intervalType, start, end,indicatorType,xCoordinate);
			}else{
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return JSON.toJSONString(list);
	}
	
	public String getFrequency(List series,List releaseTypes,String intervalType,String start,String end,List<String> xCoordinate){
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.SUNDAY);
		Map<String,Object> blueprintInsMap = new HashMap<>();//instanceName-Object
		Map<String,Object> params = new HashMap();
		params.put("names", series);
		params.put("flowNames", releaseTypes);
		params.put("startTime", start);
		params.put("endTime", end);
		List<Map<String,Object>> instances =blueprintService.getFlowInstanceIds(params);
		if(xCoordinate ==null){
			xCoordinate= new ArrayList<>();
		}
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy.MM");
		for(Map<String,Object> instance:instances){
			String id = (String)instance.get("id");
			String instanceName = (String)instance.get("instanceName");
			series.remove(instanceName);
			Map<String,Integer> indicatorMap = null;
			Map<String,Object> instanceInfo=null;
			if(JudgeUtil.isEmpty(blueprintInsMap.get(id))){
				instanceInfo = new HashMap<>();//蓝图实例信息
				indicatorMap = new HashMap<>();//指标信息
				instanceInfo.put("name", instanceName);
//				instanceInfo.put("id", id);
				instanceInfo.put("indicatorMap", indicatorMap);
				blueprintInsMap.put(id, instanceInfo);
			}else{
				instanceInfo=(Map)blueprintInsMap.get(id);
				indicatorMap=(Map<String,Integer>)instanceInfo.get("indicatorMap");
			}
			//获取发布频率
			String xKey ="";
			if("year".equals(intervalType)){//年图
				xKey = format3.format(instance.get("startTime"));
			}else if("quarter".equals(intervalType)){//季图
				ca.setTime((Date)instance.get("startTime"));
				int day = ca.get(Calendar.DAY_OF_WEEK);
				String startDay=format2.format(instance.get("startTime"));
				if(day!=1){
					ca.add(Calendar.DATE, 1-day);
					startDay=format2.format(ca.getTime().getTime());
				}
				ca.add(Calendar.DATE, 7-day);
				String endDay=format2.format(ca.getTime().getTime());
				xKey = startDay+"-"+endDay;
			}else if("month".equals(intervalType)){//月图
				xKey = format2.format(instance.get("startTime"));
			}
			if(!JudgeUtil.isEmpty(xKey)){
				if(!xCoordinate.contains(xKey)){
					xCoordinate.add(xKey);
				}
				Integer count =indicatorMap.get(xKey)==null?1:Integer.valueOf(indicatorMap.get(xKey))+1;
				indicatorMap.put(xKey, count);
			}
		}
		//组织响应报文
		SortUtil.sort(xCoordinate);
		List<Map> seris = new ArrayList<>();
		for(Map.Entry<String, Object> entry:blueprintInsMap.entrySet()){
			String key = entry.getKey();
			Map instanceInfo = (Map)entry.getValue();
			Map<String,Integer> indicatorMap=(Map<String,Integer>)instanceInfo.get("indicatorMap");
			List<Integer> data = new ArrayList<>();
			for(int i=0;i<xCoordinate.size();i++){
				Integer count =indicatorMap.get(xCoordinate.get(i))==null?0:(Integer)indicatorMap.get(xCoordinate.get(i));
				data.add(count);
			}
			instanceInfo.put("data", data);
			instanceInfo.remove("indicatorMap");
			seris.add(instanceInfo);
		}
		//对未找到的蓝图实例进行坐标补齐
		for(int i=0;i<series.size();i++){
			Map<String,Object> instanceInfo=new HashMap<>();
			List<Integer> data = new ArrayList<>();
			for(int j=0;j<xCoordinate.size();j++){
				data.add(0);
			}
			instanceInfo.put("data", data);
			instanceInfo.put("name", series.get(i));
			seris.add(instanceInfo);
		}
		Map<String,Object> result = new HashMap<>();
		result.put("x", xCoordinate);
		result.put("seris", seris);
		return JSON.toJSONString(result);
	}
	
	
	public String getSuccess(List series,List releaseTypes,String intervalType,String start,String end,List<String> xCoordinate) throws ParseException{
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.SUNDAY);
		Map<String,Object> blueprintInsMap = new HashMap<>();//instanceName-Object
		Map<String,Object> params = new HashMap();
		params.put("names", series);
		params.put("flowNames", releaseTypes);
		params.put("startTime", start);
		params.put("endTime", end);
		List<Map<String,Object>> instances =blueprintService.getFlowInstanceIds(params);
		Map<String,String> instanceMap= new HashMap<>();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy.MM");
		SimpleDateFormat format4 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		for(Map<String,Object> instance:instances){
			String id = (String)instance.get("id");
			String instanceId = (String)instance.get("instanceId");
			String instanceName = (String)instance.get("instanceName");
			series.remove(instanceName);
			instanceMap.put(instanceId, instanceName);
			instanceMap.put(instanceName, id);
		}
		instances=listFlowInstanceState(instances);
		if(xCoordinate ==null){
			xCoordinate= new ArrayList<>();
		}
		for(Map<String,Object> instance:instances){
			String instanceId = (String)instance.get("instanceId");
			String state = instance.get("state")+"";
			if(!"2".equals(state)){
				continue;
			}
			String instanceName = instanceMap.get(instanceId);
			String id = instanceMap.get(instanceName);
			Map<String,Integer> indicatorMap = null;
			Map<String,Object> instanceInfo=null;
			if(JudgeUtil.isEmpty(blueprintInsMap.get(id))){
				instanceInfo = new HashMap<>();//蓝图实例信息
				indicatorMap = new HashMap<>();//指标信息
				instanceInfo.put("name", instanceName);
				instanceInfo.put("indicatorMap", indicatorMap);
				blueprintInsMap.put(id, instanceInfo);
			}else{
				instanceInfo=(Map)blueprintInsMap.get(id);
				indicatorMap=(Map<String,Integer>)instanceInfo.get("indicatorMap");
			}
			//获取发布频率
			Date startTimeDay = format4.parse((String)instance.get("startTime"));
			long startTime =startTimeDay.getTime();
			String xKey ="";
			if("year".equals(intervalType)){//年图
				xKey = format3.format(startTime);
			}else if("quarter".equals(intervalType)){//季图
				ca.setTime(startTimeDay);
				int day = ca.get(Calendar.DAY_OF_WEEK);
				String startDay=format2.format(startTimeDay);
				if(day!=1){
					ca.add(Calendar.DATE, 1-day);
					startDay=format2.format(ca.getTime().getTime());
				}
				ca.add(Calendar.DATE, 7-day);
				String endDay=format2.format(ca.getTime().getTime());
				xKey = startDay+"-"+endDay;
			}else if("month".equals(intervalType)){//月图
				xKey = format2.format(startTime);
			}
			if(!JudgeUtil.isEmpty(xKey)){
				if(!xCoordinate.contains(xKey)){
					xCoordinate.add(xKey);
				}
				Integer count =indicatorMap.get(xKey)==null?1:Integer.valueOf(indicatorMap.get(xKey))+1;
				indicatorMap.put(xKey, count);
			}
		}
		//组织响应报文
		SortUtil.sort(xCoordinate);
		List<Map> seris = new ArrayList<>();
		for(Map.Entry<String, Object> entry:blueprintInsMap.entrySet()){
			String key = entry.getKey();
			Map instanceInfo = (Map)entry.getValue();
			Map<String,Integer> indicatorMap=(Map<String,Integer>)instanceInfo.get("indicatorMap");
			List<Integer> data = new ArrayList<>();
			for(int i=0;i<xCoordinate.size();i++){
				Integer count =indicatorMap.get(xCoordinate.get(i))==null?0:(Integer)indicatorMap.get(xCoordinate.get(i));
				data.add(count);
			}
			instanceInfo.put("data", data);
			instanceInfo.remove("indicatorMap");
			seris.add(instanceInfo);
		}
		//对未找到的蓝图实例进行坐标补齐
		for(int i=0;i<series.size();i++){
			Map<String,Object> instanceInfo=new HashMap<>();
			List<Integer> data = new ArrayList<>();
			for(int j=0;j<xCoordinate.size();j++){
				data.add(0);
			}
			instanceInfo.put("data", data);
			instanceInfo.put("name", series.get(i));
			seris.add(instanceInfo);
		}
		Map<String,Object> result = new HashMap<>();
		result.put("x", xCoordinate);
		result.put("seris", seris);
		return JSON.toJSONString(result);
	}

	public String getActualTime(List series,List releaseTypes,String intervalType,String start,String end,
			String indicatorType,List<String> xCoordinate) throws ParseException{
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.SUNDAY);
		Map<String,Object> blueprintInsMap = new HashMap<>();//instanceName-Object
		Map<String,Object> params = new HashMap();
		params.put("names", series);
		params.put("flowNames", releaseTypes);
		params.put("startTime", start);
		params.put("endTime", end);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss SSS");
		SimpleDateFormat format4 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		List<Map<String,Object>> instances =blueprintService.getFlowInstanceIds(params);
		Map<String,String> instanceMap= new HashMap<>();
		for(Map<String,Object> instance:instances){
			String id = (String)instance.get("id");
			String instanceId = (String)instance.get("instanceId");
			String instanceName = (String)instance.get("instanceName");
			series.remove(instanceName);
			instanceMap.put(instanceId, instanceName);
			instanceMap.put(instanceName, id);
		}
		instances=listFlowInstanceState(instances);
		List<String> xCoordinateBak = xCoordinate;//若所有的蓝图实例都没有数据，使用此坐标
		xCoordinate= new ArrayList<>();
		for(Map<String,Object> instance:instances){
			String instanceId = (String)instance.get("instanceId");
			String state = instance.get("state")+"";
			Float opTime=instance.get("opTime")==null?0:Float.valueOf(instance.get("opTime").toString());
			String instanceName = instanceMap.get(instanceId);
			String id = instanceMap.get(instanceName);
			Map<String,String> indicatorMap = null;
			Map<String,Object> instanceInfo=null;
			if(JudgeUtil.isEmpty(blueprintInsMap.get(id))){
				instanceInfo = new HashMap<>();//蓝图实例信息
				indicatorMap = new HashMap<>();//指标信息
				instanceInfo.put("name", instanceName);
				instanceInfo.put("indicatorMap", indicatorMap);
				blueprintInsMap.put(id, instanceInfo);
			}else{
				instanceInfo=(Map)blueprintInsMap.get(id);
				indicatorMap=(Map<String,String>)instanceInfo.get("indicatorMap");
			}
			//获取发布频率
			Date startTimeDay = format4.parse((String)instance.get("startTime"));
			String xKey =format2.format(startTimeDay);
			if(!JudgeUtil.isEmpty(xKey)){
				if(!xCoordinate.contains(xKey)){
					xCoordinate.add(xKey);
				}
				Float time =opTime;
				Integer count =1;
				if(indicatorMap.get(xKey)!=null){
					count=Integer.valueOf(indicatorMap.get(xKey).split("#")[0])+1;
					time=Float.valueOf(indicatorMap.get(xKey).split("#")[1])+opTime;
				}
				indicatorMap.put(xKey, count+"#"+time);
			}
		}
		//组织响应报文
		SortUtil.sort(xCoordinate);
		List<Map> seris = new ArrayList<>();
		for(Map.Entry<String, Object> entry:blueprintInsMap.entrySet()){
			String key = entry.getKey();
			Map instanceInfo = (Map)entry.getValue();
			Map<String,String> indicatorMap=(Map<String,String>)instanceInfo.get("indicatorMap");
			List<Float> data = new ArrayList<>();
			for(int i=0;i<xCoordinate.size();i++){
				String value = indicatorMap.get(xCoordinate.get(i));
				Float time =0f;
				Integer count =0;
				if(value!=null){
					count=Integer.valueOf(value.split("#")[0])+1;
					time=Float.valueOf(value.split("#")[1]);
				}
				data.add(time);
				
			}
			instanceInfo.put("data", data);
			instanceInfo.remove("indicatorMap");
			seris.add(instanceInfo);
		}
		if(xCoordinate.size()==0){
			xCoordinate=xCoordinateBak;
		}
		//对未找到的蓝图实例进行坐标补齐
		for(int i=0;i<series.size();i++){
			Map<String,Object> instanceInfo=new HashMap<>();
			List<Integer> data = new ArrayList<>();
			for(int j=0;j<xCoordinate.size();j++){
				data.add(0);
			}
			instanceInfo.put("data", data);
			instanceInfo.put("name", series.get(i));
			seris.add(instanceInfo);
		}
		Map<String,Object> result = new HashMap<>();
		result.put("x", xCoordinate);
		result.put("seris", seris);
		return JSON.toJSONString(result);
	}
	
	public String getAverageTime(List series,List releaseTypes,String intervalType,String start,String end,
			String indicatorType,List<String> xCoordinate) throws ParseException{
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.SUNDAY);
		Map<String,Object> blueprintInsMap = new HashMap<>();//instanceName-Object
		Map<String,Object> params = new HashMap();
		params.put("names", series);
		params.put("flowNames", releaseTypes);
		params.put("startTime", start);
		params.put("endTime", end);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy.MM");
		SimpleDateFormat format4 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		List<Map<String,Object>> instances =blueprintService.getFlowInstanceIds(params);
		Map<String,String> instanceMap= new HashMap<>();
		for(Map<String,Object> instance:instances){
			String id = (String)instance.get("id");
			String instanceId = (String)instance.get("instanceId");
			String instanceName = (String)instance.get("instanceName");
			series.remove(instanceName);
			instanceMap.put(instanceId, instanceName);
			instanceMap.put(instanceName, id);
		}
		instances=listFlowInstanceState(instances);
		if(xCoordinate ==null){
			xCoordinate= new ArrayList<>();
		}
		for(Map<String,Object> instance:instances){
			String instanceId = (String)instance.get("instanceId");
			String state = instance.get("state")+"";
			Float opTime=instance.get("opTime")==null?0:Float.valueOf(instance.get("opTime").toString());
			String instanceName = instanceMap.get(instanceId);
			String id = instanceMap.get(instanceName);
			Map<String,String> indicatorMap = null;
			Map<String,Object> instanceInfo=null;
			if(JudgeUtil.isEmpty(blueprintInsMap.get(id))){
				instanceInfo = new HashMap<>();//蓝图实例信息
				indicatorMap = new HashMap<>();//指标信息
				instanceInfo.put("name", instanceName);
				instanceInfo.put("indicatorMap", indicatorMap);
				blueprintInsMap.put(id, instanceInfo);
			}else{
				instanceInfo=(Map)blueprintInsMap.get(id);
				indicatorMap=(Map<String,String>)instanceInfo.get("indicatorMap");
			}
			//获取发布频率
			Date startTimeDay = format4.parse((String)instance.get("startTime"));
			long startTime =startTimeDay.getTime();
			String xKey ="";
			if("year".equals(intervalType)){//年图
				xKey = format3.format(startTime);
			}else if("quarter".equals(intervalType)){//季图
				ca.setTime(startTimeDay);
				int day = ca.get(Calendar.DAY_OF_WEEK);
				String startDay=format2.format(startTimeDay);
				if(day!=1){
					ca.add(Calendar.DATE, 1-day);
					startDay=format2.format(ca.getTime().getTime());
				}
				ca.add(Calendar.DATE, 7-day);
				String endDay=format2.format(ca.getTime().getTime());
				xKey = startDay+"-"+endDay;
			}else if("month".equals(intervalType)){//月图
				xKey = format2.format(startTime);
			}
			if(!JudgeUtil.isEmpty(xKey)){
				if(!xCoordinate.contains(xKey)){
					xCoordinate.add(xKey);
				}
				Float time =opTime;
				Integer count =1;
				if(indicatorMap.get(xKey)!=null){
					count=Integer.valueOf(indicatorMap.get(xKey).split("#")[0])+1;
					time=Float.valueOf(indicatorMap.get(xKey).split("#")[1])+opTime;
				}
				indicatorMap.put(xKey, count+"#"+time);
			}
		}
		//组织响应报文
		SortUtil.sort(xCoordinate);
		List<Map> seris = new ArrayList<>();
		for(Map.Entry<String, Object> entry:blueprintInsMap.entrySet()){
			String key = entry.getKey();
			Map instanceInfo = (Map)entry.getValue();
			Map<String,String> indicatorMap=(Map<String,String>)instanceInfo.get("indicatorMap");
			List<Float> data = new ArrayList<>();
			for(int i=0;i<xCoordinate.size();i++){
				String value = indicatorMap.get(xCoordinate.get(i));
				Float time =0f;
				Integer count =0;
				if(value!=null){
					count=Integer.valueOf(value.split("#")[0])+1;
					time=Float.valueOf(value.split("#")[1]);
				}
				if(count==0){
					data.add(0f);
				}else{
					BigDecimal b1 =new BigDecimal(time);
					BigDecimal b2 =new BigDecimal(count);
					Float ave=b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).floatValue();
					data.add(ave);
				}
			}
			instanceInfo.put("data", data);
			instanceInfo.remove("indicatorMap");
			seris.add(instanceInfo);
		}
		//对未找到的蓝图实例进行坐标补齐
		for(int i=0;i<series.size();i++){
			Map<String,Object> instanceInfo=new HashMap<>();
			List<Integer> data = new ArrayList<>();
			for(int j=0;j<xCoordinate.size();j++){
				data.add(0);
			}
			instanceInfo.put("data", data);
			instanceInfo.put("name", series.get(i));
			seris.add(instanceInfo);
		}
		Map<String,Object> result = new HashMap<>();
		result.put("x", xCoordinate);
		result.put("seris", seris);
		return JSON.toJSONString(result);
	}
	public List listFlowInstanceState(List list){
		List resultState = new ArrayList<>();
		try {
			//判断流程是否已结束以及实例状态
			int pageSizeTmp=500;
			int size =list.size();
			//判断流程是否已结束以及实例状态
			if(list.size()>0){
				//当数据量很大时，rest请求容易超时，因此，此处分页调用工作流接口，获取信息
				int pageNumTmp =size%pageSizeTmp==0?size/pageSizeTmp:size/pageSizeTmp+1;
				for(int i=0;i<pageNumTmp;i++){
					List listTmp = new ArrayList<>();
					int fromIndex=i*pageSizeTmp;
					int toIndex=((i+1)*pageSizeTmp)<size?(i+1)*pageSizeTmp:size;
					listTmp.addAll(list.subList(fromIndex,toIndex));
					RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
					MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
					//防止中文乱码
					MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
					HttpHeaders headers =new HttpHeaders();
					headers.setContentType(type);
					requestEntity.add("instancesList", JSON.toJSONString(listTmp,SerializerFeature.WriteDateUseDateFormat,
							SerializerFeature.DisableCircularReferenceDetect));
					HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<MultiValueMap<String, String>>(requestEntity,headers);
					String result = restUtil.postForObject(flowServerUrl+"/WFService/getInstancesStatus.wf", requestHttpEntity, String.class);
					Map<String, Object> jsonMap = JSON.parseObject(result);
					if((Boolean)jsonMap.get("state")){
						List resultStateTmp = JSON.parseArray(jsonMap.get("data").toString(), Map.class);
						resultState.addAll(resultStateTmp);
					}
				}
			
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return resultState;
	}
}