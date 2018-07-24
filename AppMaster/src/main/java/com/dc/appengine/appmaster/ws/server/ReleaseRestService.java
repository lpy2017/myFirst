package com.dc.appengine.appmaster.ws.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.dao.IReleaseDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseApproval;
import com.dc.appengine.appmaster.entity.ReleaseBus;
import com.dc.appengine.appmaster.entity.ReleaseTask;
import com.dc.appengine.appmaster.quartz.QuartzManager;
import com.dc.appengine.appmaster.service.IReleaseService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/release")
public class ReleaseRestService {
	private static final Logger log = LoggerFactory.getLogger(ReleaseRestService.class);

	@Autowired
	@Qualifier("releaseService")
	private IReleaseService releaseService;
	
	@Autowired
	@Qualifier("releaseDao")
	private IReleaseDao releaseDao;

	@RequestMapping(value = "listReleaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseTask(@RequestParam("busId") int busId, @RequestParam("initiator") String initiator,
			@RequestParam("taskName") String taskName, @RequestParam("system") String system,
			@RequestParam("module") String module, @RequestParam("startTime") String startTime,
			@RequestParam("stopTime") String stopTime, @RequestParam("status") String status,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> params = new HashMap<>();
			params.put("busId", busId);
			params.put("name", taskName);
			params.put("initiator", initiator);
			params.put("system", system);
			params.put("module", module);
			Date start = null;
			Date stop = null;
			if(startTime != null && !"".equals(startTime.trim())){
				start = format.parse(startTime);
				params.put("startTime", start);
			}
			if(stopTime != null && !"".equals(stopTime.trim())){
				stop = format.parse(stopTime);
				params.put("stopTime", stop);
			}
			if(start != null && stop != null){
				if(start.after(stop)){
					throw new Exception("搜索的开始时间[" + startTime + "]不能晚于截止时间[" + stopTime + "]");
				}
			}
			params.put("status", status);
			params.put("sortName", SortUtil.getColunmName("task", sortName));
			params.put("sortOrder", sortOrder);
			Page page = releaseService.listReleaseTask(params, pageNum, pageSize);
			List<ReleaseTask> list = page.getRows();
			for (ReleaseTask task : list) {
				int id = task.getId();
				/*任务状态由插件回调更新，不再由查询时再去更新
				String flowInstance = task.getBlueprintFlowInstance();
				String taskStatus = task.getStatus();
				if (flowInstance != null && "02".equals(taskStatus)) {
					// 如果任务执行中，则刷新一次状态
					taskStatus = releaseService.refreshFlowStatus(flowInstance);
					if (!"02".equals(taskStatus)) {
						// 如果执行完毕，更新发布任务状态
						task.setStatus(taskStatus);
						Map<String, Object> map = new HashMap<>();
						map.put("id", id);
						map.put("status", taskStatus);
						releaseService.updateReleaseTaskDone(map);
					}
				}
				*/
				List<ReleaseApproval> approvals = releaseService.listReleaseApprovalByTaskId(id);
				task.setApprovals(approvals);
			}
			return MessageHelper.wrap("result", true, "message", "查询发布任务成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "getReleaseTaskById", method = RequestMethod.POST)
	@ResponseBody
	public String getReleaseTaskById(@RequestParam("id") int id) {
		try {
			ReleaseTask task = releaseService.getReleaseTaskById(id);
			if (task != null) {
				/*
				String flowInstance = task.getBlueprintFlowInstance();
				String taskStatus = task.getStatus();
				if (flowInstance != null && "02".equals(taskStatus)) {
					// 如果任务执行中，则刷新一次状态
					taskStatus = releaseService.refreshFlowStatus(flowInstance);
					if (!"02".equals(taskStatus)) {
						// 如果执行完毕，更新发布任务状态
						task.setStatus(taskStatus);
						Map<String, Object> map = new HashMap<>();
						map.put("id", id);
						map.put("status", taskStatus);
						releaseService.updateReleaseTaskDone(map);
					}
				}
				*/
				int dependId = task.getDependId();
				if(dependId != 0){
					ReleaseTask dependTask = releaseService.getReleaseTaskById(dependId);
					task.setDependName(dependTask.getName());
				}
				List<ReleaseApproval> approvals = releaseService.listReleaseApprovalByTaskId(id);
				task.setApprovals(approvals);
			}
			return MessageHelper.wrap("result", true, "message", "获取任务详情成功", "data", task);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取任务详情失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "saveReleaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String saveReleaseTask(@RequestParam("busId") int busId, @RequestParam("taskName") String taskName,
			@RequestParam("system") String system,
			@RequestParam("module") String module, @RequestParam("description") String description,
			@RequestParam("startTime") String startTime, @RequestParam("stopTime") String stopTime,
			@RequestParam("blueprintTemplate") String blueprintTemplate,
			@RequestParam("blueprintInstance") String blueprintInstance,
			@RequestParam("blueprintFlow") String blueprintFlow, 
			@RequestParam("dependId") int dependId,
			@RequestParam("autoExecute") boolean autoExecute,
			@RequestParam("cronExepression") String cronExepression,
			@RequestParam("approvals") String approvals,
			@Context HttpServletRequest request) {
		try {
			if(taskName == null || "".equals(taskName.trim())){
				throw new Exception("发布任务名称不能为空，请重新命名！");
			}
			List<ReleaseTask> names = releaseService.getReleaseTaskByName(taskName);
			if (names != null && names.size() > 0) {
				throw new Exception("发布任务名称[" + taskName + "]已存在，请重新命名！");
			}
			if(cronExepression != null && !"".equals(cronExepression.trim())){
				if(!QuartzManager.checkCronExpression(cronExepression)){
					throw new Exception("定时任务表达式[" + cronExepression + "]不符合规范，请检查！");
				}
			}
			if(startTime == null || "".equals(startTime.trim())){
				throw new Exception("开始时间不能为空，请重新设置！");
			}
			if(stopTime == null || "".equals(stopTime.trim())){
				throw new Exception("截止时间不能为空，请重新设置！");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = format.parse(startTime);
			Date stop = format.parse(stopTime);
			if(start.after(stop)){
				throw new Exception("开始时间不能晚于截止时间，请重新设置！");
			}
			ReleaseBus bus = releaseService.getReleaseBusById(busId);
			Date busStartTime = bus.getStartTime();
			Date busStopTime = bus.getStopTime();
			if(start.before(busStartTime)){
				throw new Exception("发布任务的开始时间["+ startTime +"]不能早于所属班车的开始时间[" + format.format(busStartTime) + "]，请重新设置！");
			}
			if(stop.after(busStopTime)){
				throw new Exception("发布任务的截止时间["+ stopTime +"]不能晚于所属班车的截止时间[" + format.format(busStopTime) + "]，请重新设置！");
			}
			if(dependId != -1){
				ReleaseTask dependTask = releaseService.getReleaseTaskById(dependId);
				if(dependTask.getStopTime().after(start)){
					throw new Exception("发布任务的依赖任务[" + dependTask.getName() + "]的截止时间["+ format.format(dependTask.getStopTime()) 
						+"]不能晚于发布任务的开始时间[" + startTime + "]，请重新设置！");
				}
			}
			User user = (User) request.getSession().getAttribute("user");
			ReleaseTask task = new ReleaseTask(busId, taskName, user.getName(), system, module, start, stop,
					description, "01", blueprintTemplate, blueprintInstance, blueprintFlow, dependId, autoExecute, cronExepression);
			int rows = releaseService.saveReleaseTask(task);
			releaseService.saveReleaseApprovals(task.getId(), approvals);
			QuartzManager.addTaskJob(task.getId());
			return MessageHelper.wrap("result", true, "message", "保存发布任务成功", "id", task.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "保存发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "updateReleaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseTask(@RequestParam("taskId") int taskId, @RequestParam("busId") int busId,
			@RequestParam("taskName") String taskName,
			@RequestParam("system") String system, @RequestParam("module") String module,
			@RequestParam("description") String description, @RequestParam("startTime") String startTime,
			@RequestParam("stopTime") String stopTime,
			@RequestParam("blueprintTemplate") String blueprintTemplate,
			@RequestParam("blueprintInstance") String blueprintInstance,
			@RequestParam("blueprintFlow") String blueprintFlow, 
			@RequestParam("dependId") int dependId, 
			@RequestParam("autoExecute") boolean autoExecute, 
			@RequestParam("cronExepression") String cronExepression, 
			@RequestParam("approvals") String approvals,
			@Context HttpServletRequest request) {
		try {
			ReleaseTask oldTask = releaseService.getReleaseTaskById(taskId);
			if (oldTask == null) {
				throw new Exception("原发布任务不存在，请检查！");
			}
			else{
				if(!taskName.equals(oldTask.getName())){
					List<ReleaseTask> names = releaseService.getReleaseTaskByName(taskName);
					if (names != null && names.size() > 0) {
						throw new Exception("新的发布任务名称[" + taskName + "]已存在，请重新命名！");
					}
				}
			}
			if(cronExepression != null && !"".equals(cronExepression.trim())){
				if(!QuartzManager.checkCronExpression(cronExepression)){
					throw new Exception("定时任务表达式[" + cronExepression + "]不符合规范，请检查！");
				}
			}
			String status = oldTask.getStatus();
			/*
			if ("02".equals(status) || "03".equals(status) || "04".equals(status)) {
				throw new Exception("原发布任务正在执行中或已执行完成，不允许更新。！");
			}
			*/
			if(taskName == null || "".equals(taskName.trim())){
				throw new Exception("发布任务名称不允许为空，请检查！");
			}
			if(startTime == null || "".equals(startTime.trim())){
				throw new Exception("开始时间不允许为空，请检查！");
			}
			if(stopTime == null || "".equals(stopTime.trim())){
				throw new Exception("截止时间不允许为空，请检查！");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = format.parse(startTime);
			Date stop = format.parse(stopTime);
			if(start.after(stop)){
				throw new Exception("开始时间不能晚于截止时间，请重新设置！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if(!"admin".equals(user.getName()) && !user.getName().equals(oldTask.getInitiator())){
				throw new Exception("目前登录用户[" + user.getName() + "]不是发布任务发起人，无权修改！");
			}
			ReleaseBus bus = releaseService.getReleaseBusById(busId);
			Date busStartTime = bus.getStartTime();
			Date busStopTime = bus.getStopTime();
			if(start.before(busStartTime)){
				throw new Exception("发布任务新的开始时间["+ startTime +"]不能早于所属班车的开始时间[" + format.format(busStartTime) + "]，请重新设置！");
			}
			if(stop.after(busStopTime)){
				throw new Exception("发布任务新的截止时间["+ stopTime +"]不能晚于所属班车的截止时间[" + format.format(busStopTime) + "]，请重新设置！");
			}
			if(dependId != -1){
				ReleaseTask dependTask = releaseService.getReleaseTaskById(dependId);
				if(dependTask.getStopTime().after(start)){
					throw new Exception("当前发布任务的依赖任务[" + dependTask.getName() + "]的截止时间["+ format.format(dependTask.getStopTime()) 
						+"]不能晚于当前发布任务的开始时间[" + startTime + "]，请重新设置！");
				}
			}
			ReleaseTask updateTask = new ReleaseTask(taskId, busId, taskName, user.getName(), system, module,
					start, stop, description, "01", blueprintTemplate, 
					blueprintInstance, blueprintFlow, dependId, autoExecute, cronExepression);
			releaseService.updateReleaseTask(updateTask);
			releaseService.updateReleaseApproval(taskId, approvals);
			QuartzManager.updateTaskJob(taskId);
			return MessageHelper.wrap("result", true, "message", "修改发布任务成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "修改发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "deleteReleaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReleaseTask(@RequestParam("taskId") int taskId, @Context HttpServletRequest request) {
		try {
			ReleaseTask task = releaseService.getReleaseTaskById(taskId);
			if (task == null) {
				throw new Exception("当前发布任务已不存在，无法删除！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if(!"admin".equals(user.getName()) && !user.getName().equals(task.getInitiator())){
				throw new Exception("目前登录用户[" + user.getName() + "]不是发布任务发起人，无权删除！");
			}
			releaseService.deleteReleaseTask(taskId);
			releaseService.deleteReleaseApprovalsByTaskId(taskId);
			QuartzManager.removeTaskJob(taskId);
			return MessageHelper.wrap("result", true, "message", "删除发布任务成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "删除发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "updateBlueprintAppsVersion", method = RequestMethod.GET)
	@ResponseBody
	public String updateBlueprintAppsVersion(@RequestParam("blueprintInstanceId") String blueprintInstanceId,
			@RequestParam("versionId") String versionId) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("blueprintInstanceId", blueprintInstanceId);
			params.put("versionId", versionId);
			releaseService.updateBlueprintAppsVersion(params);
			return MessageHelper.wrap("result", true, "message", "蓝图实例组件配置修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "蓝图实例组件配置修改失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listReadyReleaseApprovalsByUser", method = RequestMethod.POST)
	@ResponseBody
	public String listReadyReleaseApprovalsByUser(
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName,
			@Context HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> params = new HashMap<>();
			//admin用户全部可以查询
			if(!"admin".equals(user.getName())){
				params.put("approver", user.getName());
			}
			List<String> statuses = new ArrayList<>();
			statuses.add("01");
			params.put("statuses", statuses);
			params.put("sortName", SortUtil.getColunmName("approve", sortName));
			params.put("sortOrder", sortOrder);
			Page page = releaseService.listReleaseApprovalsByUser(params, pageNum, pageSize);
			return MessageHelper.wrap("result", true, "message", "查询待审批成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询待审批失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listDoneReleaseApprovalsByUser", method = RequestMethod.POST)
	@ResponseBody
	public String listDoneReleaseApprovalsByUser(
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName,
			@Context HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> params = new HashMap<>();
			//admin用户全部可以查询
			//if(!"admin".equals(user.getName())){
			params.put("performer", user.getName());
			//}
			List<String> statuses = new ArrayList<>();
			statuses.add("02");
			statuses.add("03");
			params.put("statuses", statuses);
			params.put("sortName", SortUtil.getColunmName("approve", sortName));
			params.put("sortOrder", sortOrder);
			Page page = releaseService.listReleaseApprovalsByUser(params, pageNum, pageSize);
			return MessageHelper.wrap("result", true, "message", "查询已审批成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询已审批失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "updateReleaseApprovalStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseApprovalStatus(@RequestParam("id") int id, @RequestParam("status") String status, @Context HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			params.put("user", user.getName());
			params.put("status", status);
			releaseService.updateReleaseApprovalSingle(params);
			return MessageHelper.wrap("result", true, "message", "更新审批状态成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "更新审批状态失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateReleaseTaskStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseTaskStatus(@RequestParam("flowInstanceId") String flowInstanceId, @RequestParam("status") String status) {
		try {
			int count = releaseDao.countReleaseTaskByFlowInstanceId(flowInstanceId);
			if(count == 0){
				// return MessageHelper.wrap("result", true, "message", "流程实例[" + flowInstanceId + "]对应的发布任务不存在，无法更新状态！");
				return MessageHelper.wrap("result", true, "message", "更新发布任务状态成功");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flowInstanceId", flowInstanceId);
			map.put("status", status);
			releaseService.updateReleaseTaskStatus(map);
			return MessageHelper.wrap("result", true, "message", "更新发布任务状态成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "更新发布任务状态失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "listReleaseBus", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseBus(@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName,
			@Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("sortOrder", sortOrder);
			params.put("sortName", SortUtil.getColunmName("bus", sortName));
			Page page = releaseService.listReleaseBus(params, pageNum, pageSize);
			return MessageHelper.wrap("result", true, "message", "查询班车成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询班车失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "listReleaseTaskByBusId", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseTaskByBusId(@RequestParam("busId") int busId,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("busId", busId);
			Page page = releaseService.listReleaseTaskByBusId(params, pageNum, pageSize);
			return MessageHelper.wrap("result", true, "message", "获取依赖任务列表成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取依赖任务列表失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "saveReleaseBus", method = RequestMethod.POST)
	@ResponseBody
	public String saveReleaseBus(@RequestParam("name") String name,
			@RequestParam("startTime") String startTime,
			@RequestParam("stopTime") String stopTime,
			@Context HttpServletRequest request) {
		try {
			if(name == null || "".equals(name.trim())){
				return MessageHelper.wrap("result", true, "message", "班车名称不允许为空！");
			}
			if(startTime == null || "".equals(startTime.trim())){
				return MessageHelper.wrap("result", true, "message", "开始时间不允许为空！");
			}
			if(stopTime == null || "".equals(stopTime.trim())){
				return MessageHelper.wrap("result", true, "message", "截止时间不允许为空！");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = format.parse(startTime);
			Date stop = format.parse(stopTime);
			if(start.after(stop)){
				throw new Exception("开始时间不能晚于截止时间，请重新设置！");
			}
			int count = releaseService.countReleaseBusByName(name);
			if (count > 0) {
				throw new Exception("发布班车名称[" + name + "]已存在，请重新命名！");
			}
			User user = (User) request.getSession().getAttribute("user");
			ReleaseBus bus = new ReleaseBus(name, start, stop, user.getName());
			releaseService.saveReleaseBus(bus);
			return MessageHelper.wrap("result", true, "message", "保存班车成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "保存班车失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "getReleaseBusById", method = RequestMethod.POST)
	@ResponseBody
	public String getReleaseBusById(@RequestParam("busId") int busId, @Context HttpServletRequest request) {
		try {
			ReleaseBus bus = releaseService.getReleaseBusById(busId);
			return MessageHelper.wrap("result", true, "message", "获取班车详情成功", "data", bus);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取班车详情失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateReleaseBus", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseBus(@RequestParam("id") int id,
			@RequestParam("name") String name,
			@RequestParam("startTime") String startTime,
			@RequestParam("stopTime") String stopTime,
			@Context HttpServletRequest request) {
		try {
			if(name == null || "".equals(name.trim())){
				return MessageHelper.wrap("result", true, "message", "班车名称不允许为空！");
			}
			if(startTime == null || "".equals(startTime.trim())){
				return MessageHelper.wrap("result", true, "message", "开始时间不允许为空！");
			}
			if(stopTime == null || "".equals(stopTime.trim())){
				return MessageHelper.wrap("result", true, "message", "截止时间不允许为空！");
			}
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = format.parse(startTime);
			Date stop = format.parse(stopTime);
			if(start.after(stop)){
				throw new Exception("开始时间不能晚于截止时间，请重新设置！");
			}
			ReleaseBus oldBus = releaseService.getReleaseBusById(id);
			if(oldBus == null){
				throw new Exception("当前班车已不存在，无法修改！");
			}
			else{
				if(!oldBus.getName().equals(name)){
					int count = releaseService.countReleaseBusByName(name);
					if (count > 0) {
						throw new Exception("新班车名称[" + name + "]已存在，请重新命名！");
					}
				}
			}
			User user = (User) request.getSession().getAttribute("user");
			if(!"admin".equals(user.getName()) && !oldBus.getAuthor().equals(user.getName())){
				throw new Exception("当前登录用户[" + user.getName() + "]不是该班车创建者，无权限修改！");
			}
			/*
			if(start.after(oldBus.getStartTime())){
				throw new Exception("班车新的开始时间[" + startTime + "]不允许晚于原班车开始时间[" + format.format(oldBus.getStartTime()) + "]");
			}
			if(stop.before(oldBus.getStopTime())){
				throw new Exception("班车新的截止时间[" + stopTime + "]不允许早于原班车截止时间[" + format.format(oldBus.getStopTime()) + "]");
			}
			*/
			List<ReleaseTask> tasks = releaseService.listAllReleaseTaskByBusId(id);
			if(tasks != null){
				for(ReleaseTask task : tasks){
					if(start.after(task.getStartTime())){
						throw new Exception("班车新的开始时间[" + startTime + "]不能晚于原班车已有发布任务[" + task.getName() + "]的开始时间[" + format.format(task.getStartTime()) + "]，请重新配置");
					}
					if(stop.before(task.getStopTime())){
						throw new Exception("班车新的截止时间[" + stopTime + "]不能早于原班车已有发布任务[" + task.getName() + "]的截止时间[" + format.format(task.getStopTime()) + "]，请重新配置");
					}
				}
			}
			ReleaseBus bus = new ReleaseBus(id, name, start, stop, user.getName());
			releaseService.updateReleaseBus(bus);
			return MessageHelper.wrap("result", true, "message", "修改班车成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "修改班车失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "deleteReleaseBusById", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReleaseBusById(@RequestParam("busId") int busId, @Context HttpServletRequest request) {
		try {
			ReleaseBus oldBus = releaseService.getReleaseBusById(busId);
			if(oldBus == null){
				throw new Exception("当前班车已不存在，无法删除！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if(!"admin".equals(user.getName()) && !oldBus.getAuthor().equals(user.getName())){
				throw new Exception("当前登录用户[" + user.getName() + "]不是该班车创建者，无权限删除！");
			}
			releaseService.deleteReleaseBusById(busId);
			return MessageHelper.wrap("result", true, "message", "删除班车成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "删除班车失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "executeTaskMannual", method = RequestMethod.POST)
	@ResponseBody
	public String executeTaskMannual(@RequestParam("taskId") int taskId, @Context HttpServletRequest request) {
		try {
			ReleaseTask task = releaseService.getReleaseTaskById(taskId);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			Date start = task.getStartTime();
			/*
			if(now.before(start)){
				return MessageHelper.wrap("result", false, "message", "当前时间[" + format.format(now) + "]早于任务开始时间[" + format.format(start) + "]，不允许执行！");
			}
			*/
			String flowInstanceId = releaseService.executeTaskMannual(taskId);
			return MessageHelper.wrap("result", true, "message", "手动执行任务成功", "flowInstanceId", flowInstanceId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "手动执行任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "getGanttChart", method = RequestMethod.GET)
	@ResponseBody
	public String getGanttChart(@RequestParam("busId") int busId, @Context HttpServletRequest request) {
		try {
			String json = releaseService.getGanttChart(busId);
			return json;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取数据异常:" + e.getMessage());
		}
	}
}
