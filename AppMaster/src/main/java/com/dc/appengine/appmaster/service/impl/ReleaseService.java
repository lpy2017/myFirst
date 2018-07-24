package com.dc.appengine.appmaster.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IReleaseDao;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseApproval;
import com.dc.appengine.appmaster.entity.ReleaseBus;
import com.dc.appengine.appmaster.entity.ReleaseTask;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IReleaseService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.RequestClient;

@Service("releaseService")
public class ReleaseService implements IReleaseService {

	@Autowired
	@Qualifier("blueprintService")
	IBlueprintService blueprintService;

	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;

	@Autowired
	@Qualifier("releaseDao")
	private IReleaseDao releaseDao;

	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;

	@Value(value = "${flowServerUrl}")
	String flowServerUrl;

	@Override
	public ReleaseTask getReleaseTaskById(int taskId) {
		return releaseDao.getReleaseTaskById(taskId);
	}

	@Override
	public List<ReleaseTask> getReleaseTaskByName(String name) {
		return releaseDao.getReleaseTaskByName(name);
	}

	@Override
	public Page listReleaseTask(Map<String, Object> params, int pageNum, int pageSize) {
		Page page = releaseDao.listReleaseTask(params, pageNum, pageSize);
		return page;
	}

	@Transactional
	@Override
	public int saveReleaseTask(ReleaseTask task) {
		try {
			return releaseDao.saveReleaseTask(task);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void updateReleaseTask(ReleaseTask task) {
		try {
			releaseDao.updateReleaseTask(task);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteReleaseTask(int taskId) {
		try {
			releaseDao.deleteReleaseTask(taskId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<ReleaseApproval> listReleaseApprovalByTaskId(int taskId) {
		return releaseDao.listReleaseApprovalByTaskId(taskId);
	}

	@Transactional
	@Override
	public void saveReleaseApprovals(int taskId, String json) {
		JSONArray approvals = JSON.parseArray(json);
		if (approvals.size() == 0) {
			throw new RuntimeException("发布任务未检测到审批信息，请重新配置！");
		}
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for (int i = 0; i < approvals.size(); i++) {
				JSONObject object = approvals.getJSONObject(i);
				ReleaseApproval approval = new ReleaseApproval(taskId, object.getString("approver"), i, (i == 0 ? "01" : "00"));
				releaseDao.saveReleaseApproval(approval);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void updateReleaseApproval(int taskId, String json) {
		JSONArray approvals = JSON.parseArray(json);
		if (approvals.size() == 0) {
			throw new RuntimeException("发布任务未检测到审批信息，请重新配置！");
		}
		releaseDao.deleteReleaseApprovalsByTaskId(taskId);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			for (int i = 0; i < approvals.size(); i++) {
				JSONObject object = approvals.getJSONObject(i);
				ReleaseApproval approval = new ReleaseApproval(taskId, object.getString("approver"), i, (i == 0 ? "01" : "00"));
				releaseDao.saveReleaseApproval(approval);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteReleaseApprovalsByTaskId(int taskId) {
		try {
			releaseDao.deleteReleaseApprovalsByTaskId(taskId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void deleteReleaseApprovalById(int approvalId) {
		try {
			releaseDao.deleteReleaseApprovalById(approvalId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void updateReleaseApprovalSingle(Map<String, Object> params) {
		try {
			releaseDao.updateReleaseApprovalSingle(params);
			String status = (String)params.get("status");
			int id = (int) params.get("id");
			int index = -1;
			//更新下一条为待审批
			ReleaseApproval approval = releaseDao.getReleaseApprovalById(id);
			int taskId = approval.getTaskId();
			if("03".equals(status)){
				//如果当前审批记录状态为"03拒绝"则不更新下一条审批记录，审批流程停止，审批任务更新为06审批拒绝
				Map<String, Object> map = new HashMap<>();
				map.put("id", taskId);
				map.put("status", "06");
				releaseDao.updateReleaseTaskDone(map);
				return;
			}
			List<ReleaseApproval> approvals = releaseDao.listReleaseApprovalByTaskId(taskId);
			for (int i = 0; i < approvals.size(); i++) {
				ReleaseApproval item = approvals.get(i);
				int approvalId = item.getId();
				if (approvalId == id) {
					index = i;
					break;
				}
			}
			// 更新下一个审批为待审批
			if (index < approvals.size() - 1) {
				ReleaseApproval nextApproval = approvals.get(index + 1);
				Map<String, Object> map = new HashMap<>();
				map.put("id", nextApproval.getId());
				map.put("status", "01");
				releaseDao.updateReleaseApprovalStatus(map);
			}
			else{
				//当前为最后一条审批，更新任务为05审批通过
				Map<String, Object> map = new HashMap<>();
				map.put("id", taskId);
				map.put("status", "05");
				releaseDao.updateReleaseTaskDone(map);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String refreshFlowStatus(String instanceId) {
		String status = null;
		Map<String, Object> singleInstanceMap = new HashMap<>();
		singleInstanceMap.put("instanceId", instanceId);
		List<Map> instanceList = new ArrayList<>();
		instanceList.add(singleInstanceMap);
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("instancesList", JSON.toJSONString(instanceList, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect));
		String instanceResult = restUtil.postForObject(flowServerUrl + "/WFService/getInstancesStatus.wf",
				requestEntity, String.class);
		Map<String, Object> instanceResultMap = JSON.parseObject(instanceResult);
		if ((Boolean) instanceResultMap.get("state")) {
			List<Map> instanceResultData = JSON.parseArray(instanceResultMap.get("data").toString(), Map.class);
			String instanceFlowState = (String) instanceResultData.get(0).get("flowState");
			Object isOverTime = (String) instanceResultData.get(0).get("isOverTime");
			String instanceState = (String) instanceResultData.get(0).get("state");
			// 流程已结束
			if (Constants.Monitor.FINISHED.equals(instanceFlowState)) {
				status = "03";
			}
			// 流程未结束
			else {
				// 挂起超时
				if (Constants.Monitor.OverTime.equals(isOverTime)) {
					status = "04";
				}
				// 状态已结束
				if ("2".equals(instanceState)) {
					status = "03";
				}
				// 状态正在执行0/失败7/...
				else {
					status = "02";
				}
			}
		} else {
			status = "02";
		}
		return status;
	}

	@Transactional
	@Override
	public void updateReleaseTaskDone(Map<String, Object> params) {
		try {
			releaseDao.updateReleaseTaskDone(params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void updateBlueprintAppsVersion(Map<String, Object> params) {
		String blueprintInstanceId = "" + params.get("blueprintInstanceId");
		String versionId = "" + params.get("versionId");
		Map<String, Object> resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
		// 更新蓝图组件实例配置
		List<Application> apps = blueprintService.getBlueprintComponents(blueprintInstanceId);
		for (Application app : apps) {
			String appComponentName = app.getComponentName();
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			if (appComponentName.equals(resourceName)) {
				Map<String, Object> map = new HashMap<>();
				long appId = app.getId();
				map.put("appId", appId);
				map.put("executeFlag", 1);
				map.put("versionId", versionId);
				applicationDao.updateAppVersion(map);
			}
		}
	}

	@Override
	public Page listReleaseApprovalsByUser(Map<String, Object> params, int pageNum, int pageSize) {
		return releaseDao.listReleaseApprovalsByUser(params, pageNum, pageSize);
	}
	
	@Transactional
	@Override
	public void updateReleaseTaskStatus(Map<String, Object> params) {
		try {
			releaseDao.updateReleaseTaskStatus(params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Page listReleaseBus(Map<String, Object> params, int pageNum, int pageSize) {
		Page page = releaseDao.listReleaseBus(params, pageNum, pageSize);
		List<ReleaseBus> rows = page.getRows();
		if(rows != null && rows.size() > 0){
			for(ReleaseBus bus : rows){
				int busId = bus.getId();
				List<ReleaseTask> tasks = releaseDao.listAllReleaseTaskByBusId(busId);
				bus.setTasks(tasks);
			}
		}
		return page;
	}
	
	@Override
	public ReleaseBus getReleaseBusById(int busId) {
		ReleaseBus bus = releaseDao.getReleaseBusById(busId);
		List<ReleaseTask> tasks = releaseDao.listAllReleaseTaskByBusId(busId);
		bus.setTasks(tasks);
		return bus;
	}
	
	@Override
	public List<ReleaseTask> listAllReleaseTaskByBusId(int busId) {
		return releaseDao.listAllReleaseTaskByBusId(busId);
	}

	@Override
	public int saveReleaseBus(ReleaseBus bus) {
		return releaseDao.saveReleaseBus(bus);
	}

	@Override
	public Page listReleaseTaskByBusId(Map<String, Object> params, int pageNum, int pageSize) {
		return releaseDao.listReleaseTaskByBusId(params, pageNum, pageSize);
	}

	@Override
	public int updateReleaseBus(ReleaseBus bus) {
		return releaseDao.updateReleaseBus(bus);
	}

	@Transactional
	@Override
	public int deleteReleaseBusById(int busId) {
		try {
			//deleteReleaseTaskByBusId(busId);
			return releaseDao.deleteReleaseBusById(busId);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Transactional
	@Override
	public void deleteReleaseTaskByBusId(int busId) {
		try{
			List<ReleaseTask> tasks = releaseDao.listAllReleaseTaskByBusId(busId);
			if(tasks != null && tasks.size() > 0){
				for(ReleaseTask task : tasks){
					deleteReleaseTask(task.getId());
					deleteReleaseApprovalsByTaskId(task.getId());
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String executeTaskMannual(int taskId) {
		try {
			String flowInstance = null;
			// 执行流程
			ReleaseTask task = releaseDao.getReleaseTaskById(taskId);
			String blueprintInstance = task.getBlueprintInstance();
			String blueprintFlow = task.getBlueprintFlow();
			Map<String, String> map = new HashMap<>();
			map.put("_userName", task.getInitiator());
			String result = blueprintService.executeBlueprintFlow(blueprintFlow, blueprintInstance, map);
			JSONObject flowResult = JSON.parseObject(result);
			if (flowResult.getBooleanValue("result")) {
				flowInstance = flowResult.getString("id");
				Map<String, Object> param = new HashMap<>();
				param.put("id", taskId);
				param.put("blueprintFlowInstance", flowInstance);
				param.put("status", "02");
				releaseDao.updateReleaseTaskExecute(param);
			} else {
				throw new RuntimeException("流程执行失败，请查看日志，解决错误后重新执行！");
			}
			return flowInstance;
		} catch (Exception e) {
			throw new RuntimeException("工作流执行异常，请查看工作流日志，解决错误后重新执行！");
		}
	}

	@Override
	public String getGanttChart(int busId) {
		Map<String,Object> result = new HashMap<>();
		List<Map<String,Object>> tasks = new ArrayList<>();
		List list = releaseDao.listReleaseTaskByBusId(busId);
		ReleaseBus  bus =releaseDao.getReleaseBusById(busId);
		BigDecimal b2 =new BigDecimal(60*60*24L*1000);
		Map<String,Object> busTask = new HashMap<>();
		String busStatus ="STATUS_SUSPENDED";//默认状态等待执行，STATUS_SUSPENDED-等待执行，STATUS_ACTIVE正在执行
		                                     //STATUS_DONE-成功,STATUS_FAILED-失败，STATUS_UNDEFINED-未定义
		Map <Integer,Integer> lineMap= new HashMap<>();
		int line=0;
		lineMap.put(bus.getId(), ++line);
		busTask.put("id", bus.getId());
		busTask.put("name", bus.getName());
		busTask.put("progress", 0);
		busTask.put("description", "");
		busTask.put("code", "");
		busTask.put("depends", "");
		busTask.put("status", busStatus);
		busTask.put("level", 0);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long durationTimeRoot =0;
		durationTimeRoot =bus.getStopTime().getTime()-bus.getStartTime().getTime();
		BigDecimal b1 =new BigDecimal(durationTimeRoot);
		double day=b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		busTask.put("canWrite", true);
		busTask.put("start", bus.getStartTime().getTime());
		busTask.put("duration", day+"");
		busTask.put("end", bus.getStopTime().getTime());
		busTask.put("startIsMilestone", false);
		busTask.put("endIsMilestone",false );
		busTask.put("collapsed",false );
		busTask.put("assigs", new ArrayList<>());
		busTask.put("hasChild", true);
		tasks.add(busTask);
		if(list !=null){
			int success=0;
			int fail=0;
			int running=0;
			for(int i=0;i<list.size();i++){
				Map<String,Object> task = new HashMap<>();
				Map one = (Map)list.get(i);
				int id =(int) one.get("ID");
				lineMap.put(id, ++line);
				int dependId =one.get("DEPEND_ID")==null?0:(int) one.get("DEPEND_ID");
				task.put("id", id);
				task.put("name", one.get("NAME"));
				task.put("progress", 0);
				task.put("description", one.get("DESCRIPTION"));
				task.put("code", "");
				if(dependId >0){
					task.put("depends", ""+dependId);
				}else{
					task.put("depends", "");
					task.put("startIsMilestone", true);
				}
				task.put("level", 1);
				String status="";
				if("02".equals(one.get("STATUS"))){//02-正在执行
					status="STATUS_ACTIVE";
					running++;
				}else if("03".equals(one.get("STATUS"))){//03-执行成功
					status="STATUS_DONE";
					success++;
				}else if("04".equals(one.get("STATUS"))){//04-执行失败
					status="STATUS_FAILED";
					fail++;
				}else{
					status="STATUS_SUSPENDED";
				}
				task.put("status", status);
				String startTime = one.get("START_TIME").toString();
				String endTime = one.get("STOP_TIME").toString();
				long durationTime =0;
				try {
					durationTime =format.parse(endTime).getTime()-format.parse(startTime).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				b1 =new BigDecimal(durationTime);
				day=b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				task.put("canWrite", true);
				task.put("start", one.get("START_TIME"));
				task.put("duration", day+"");
				task.put("end", one.get("STOP_TIME"));
				task.put("startIsMilestone", false);
				task.put("endIsMilestone",false );
				task.put("collapsed",false );
				task.put("assigs", new ArrayList<>());
				if(releaseDao.judgeHaveChild(id)>0){
					task.put("hasChild", true);
				}else{
					task.put("hasChild", false);
				}
				tasks.add(task);
			}
			if(fail>0){
				busStatus="STATUS_FAILED";
			}
			if(success==list.size()){
				busStatus="STATUS_DONE";
			}
			if(running>0){
				busStatus="STATUS_ACTIVE";
			}
			//获取依赖的行号
			for(int i=0;i<tasks.size();i++){
				Map task = (Map)tasks.get(i);
				//设置bus状态
				if((Integer)task.get("level")==0){
					task.put("status", busStatus);
				}
				String depends =(String) task.get("depends"); 
				if(!JudgeUtil.isEmpty(depends)){
					if(lineMap.get(Integer.valueOf(""+depends))!=null){
						depends=""+lineMap.get(Integer.valueOf(""+depends));
						task.put("depends",depends);
					}
				}
			}
		}
		result.put("tasks", tasks);
		result.put("selectedRow", 0);
		result.put("canWrite", false);
		result.put("canWriteOnParent", false);
		String json=JSON.toJSONString(result);
		return json;
	}
	
	public void getLevel(int id,Integer level){
		if(level>1000){
			return;
		}
		Map task = releaseDao.findReleaseTaskById(id);
		int dependId =task.get("DEPEND_ID")==null?0:(int) task.get("DEPEND_ID");
		if(dependId >0){
			level=level+1;
			getLevel(dependId, level);
		}
	}
	
	@Override
	public int countReleaseBusByName(String busName) {
		return releaseDao.countReleaseBusByName(busName);
	}
}
