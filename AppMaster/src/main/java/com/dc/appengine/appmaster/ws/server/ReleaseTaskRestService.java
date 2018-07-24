package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.IReleaseTaskDao;
import com.dc.appengine.appmaster.dao.IResourceDao;
import com.dc.appengine.appmaster.dao.impl.ClusterDAO;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;
import com.dc.appengine.appmaster.quartz.QuartzManager;
import com.dc.appengine.appmaster.service.IReleaseTaskService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.Utils;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/releaseTask")
public class ReleaseTaskRestService {
	private static final Logger log = LoggerFactory.getLogger(ReleaseTaskRestService.class);

	@Autowired
	@Qualifier("releaseTaskService")
	private IReleaseTaskService releaseTaskService;

	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplateDao;

	@Autowired
	@Qualifier("releaseTaskDao")
	private IReleaseTaskDao releaseTaskDao;

	@Autowired
	@Qualifier("resourceDao")
	private IResourceDao resourceDao;

	@Autowired
	@Qualifier("clusterDAO")
	private ClusterDAO clusterDAO;

	@Value(value = "${ftp.url}")
	String url;

	@Value(value = "${ftp.port}")
	int port;

	@Value(value = "${ftp.user}")
	String usr;

	@Value(value = "${ftp.pwd}")
	String pwd;

	@Value(value = "ftp.home.path")
	String ftpHomePath;

	@RequestMapping(value = "listReleaseTaskOrders", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseTaskOrders(@RequestParam("taskId") int taskId, @RequestParam("taskName") String taskName,
			@RequestParam("taskType") String taskType, @RequestParam("taskLevel") String taskLevel,
			@RequestParam("taskStatus") String taskStatus, @RequestParam("taskLabel") String taskLabel,
			@RequestParam("taskParentId") int taskParentId, @RequestParam("taskDependId") int taskDependId,
			@RequestParam("taskSystem") String taskSystem, @RequestParam("taskCreater") String taskCreater,
			@RequestParam("taskPrincipal") String taskPrincipal,
			@RequestParam("taskCreateStartTime") String taskCreateStartTime,
			@RequestParam("taskCreateEndTime") String taskCreateEndTime, @RequestParam("source") String source,
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "sortName", required = false) String sortName) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Map<String, Object> params = new HashMap<>();
			params.put("id", taskId);
			params.put("name", taskName);
			params.put("type", taskType);
			params.put("level", taskLevel);
			params.put("status", taskStatus);
			params.put("label", taskLabel);
			params.put("parentId", taskParentId);
			params.put("dependId", taskDependId);
			params.put("systemId", taskSystem);
			params.put("creater", taskCreater);
			params.put("principal", taskPrincipal);
			params.put("source", source);
			Date start = null;
			Date stop = null;
			if (taskCreateStartTime != null && !"".equals(taskCreateStartTime.trim())) {
				start = format.parse(taskCreateStartTime);
				params.put("startTime", start);
			}
			if (taskCreateEndTime != null && !"".equals(taskCreateEndTime.trim())) {
				stop = format.parse(taskCreateEndTime);
				params.put("stopTime", stop);
			}
			if (start != null && stop != null) {
				if (start.after(stop)) {
					throw new Exception("搜索的开始时间[" + taskCreateStartTime + "]不能晚于截止时间[" + taskCreateEndTime + "]");
				}
			}
			params.put("sortName", SortUtil.getColunmName("task", sortName));
			params.put("sortOrder", sortOrder);
			Page page = releaseTaskService.listReleaseTaskOrders(params, pageNum, pageSize);
			return MessageHelper.wrap("result", true, "message", "查询发布任务成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "getReleaseTaskOrderById", method = RequestMethod.POST)
	@ResponseBody
	public String getReleaseTaskOrderById(@RequestParam("taskId") int taskId) {
		try {
			ReleaseTaskBean task = releaseTaskService.getReleaseTaskOrderById(taskId);
			ReleaseTaskBean parentTask = null;
			if (task != null) {
				int parentId = task.getParentId();
				if (parentId != -1) {
					parentTask = releaseTaskService.getReleaseTaskOrderById(parentId);
					if (parentTask != null) {
						task.setParentName(parentTask.getName());
					}
				}
				// FIXME 依赖任务名称未处理，所属清单名称未处理
				String releaseId = task.getReleaseId();
				//如果子任务未绑定发布，且其有父任务，则使用其父任务的发布
				if(releaseId == null && parentTask != null) {
					releaseId = parentTask.getReleaseId();
				}
				if (releaseId != null) {
					Map<String, Object> releaseDetail = releaseTaskDao.getReleaseById(releaseId);
					if (releaseDetail != null) {
						task.setReleaseName("" + releaseDetail.get("name"));
					}
				}
				String releasePhase = task.getReleasePhaseId();
				//如果子任务未绑定发布阶段，且其有父任务，则使用其父任务的发布阶段
				if(releasePhase == null && parentTask != null) {
					releasePhase = parentTask.getReleasePhaseId();
				}
				if (releasePhase != null) {
					Map<String, Object> phaseDetail = releaseTaskDao.getReleasePhaseById(releasePhase);
					if (phaseDetail != null) {
						task.setReleasePhaseName("" + phaseDetail.get("name"));
						if (phaseDetail.get("blueprint_ins_id") != null) {
							task.setBlueprintInstance("" + phaseDetail.get("blueprint_ins_id"));
						}
						if (phaseDetail.get("blueprint_tmplate_id") != null) {
							task.setBlueprintTemplate("" + phaseDetail.get("blueprint_tmplate_id"));
						}
						if (phaseDetail.get("flow_id") != null) {
							Map<String, Object> flowInfo = blueprintTemplateDao
									.getBlueprintTemplateFlowByCdFlowId("" + phaseDetail.get("flow_id"));
							if (flowInfo != null) {
								task.setBlueprintFlow("" + flowInfo.get("FLOW_ID"));
								task.setBlueprintFlowName("" + flowInfo.get("FLOW_NAME"));
							}
						}
						if (phaseDetail.get("cluster_id") != null) {
							task.setEnvironmentId("" + phaseDetail.get("cluster_id"));
							Map<String, Object> cluster = clusterDAO.one("" + phaseDetail.get("cluster_id"));
							if (cluster != null) {
								task.setEnvironmentName("" + cluster.get("name"));
							}
						}
					}
				}
			}
			return MessageHelper.wrap("result", true, "message", "获取任务详情成功", "data", task);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取任务详情失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "saveReleaseTaskOrder", method = RequestMethod.POST)
	@ResponseBody
	public String saveReleaseTaskOrder(@RequestParam("name") String name, @RequestParam("type") String type,
			@RequestParam("level") String level, @RequestParam("label") String label,
			@RequestParam("dependId") String dependId, @RequestParam("parentId") int parentId,
			@RequestParam("systemId") String systemId, @RequestParam("systemName") String systemName,
			@RequestParam("systemRequireId") String systemRequireId,
			@RequestParam("systemRequireName") String systemRequireName,
			@RequestParam("businessRequireId") String businessRequireId,
			@RequestParam("businessRequireName") String businessRequireName,
			@RequestParam("codeBranchName") String codeBranchName,
			@RequestParam("codeBaselineName") String codeBaselineName, @RequestParam("principal") String principal,
			@RequestParam(name = "expectTime", required = false, defaultValue = "") String expectTime,
			@RequestParam(name = "autoExecute", required = false, defaultValue = "false") boolean autoExecute,
			@RequestParam(name = "cronExpression", required = false, defaultValue = "") String cronExpression,
			@RequestParam("description") String description, @RequestParam("remark") String remark,
			@RequestParam(name = "file", required = false) MultipartFile attachment,
			@Context HttpServletRequest request) {
		try {
			if (name == null || "".equals(name.trim())) {
				throw new Exception("发布任务名称不能为空，请重新命名！");
			}
			List<ReleaseTaskBean> names = releaseTaskService.getReleaseTaskOrderByName(name);
			if (names != null && names.size() > 0) {
				throw new Exception("发布任务名称[" + name + "]已存在，请重新命名！");
			}
			if (cronExpression != null && !"".equals(cronExpression.trim())) {
				if (!QuartzManager.checkCronExpression(cronExpression)) {
					throw new Exception("定时任务表达式[" + cronExpression + "]不符合规范，请检查！");
				}
			}
			Date expect = null;
			if (expectTime != null && !"".equals(expectTime.trim())) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				expect = format.parse(expectTime);
				Date now = new Date();
				if (now.after(expect)) {
					throw new Exception("期望时间不能早于当前时间，请重新设置！");
				}
			}
			User requestUser = (User) request.getSession().getAttribute("user");
			ReleaseTaskBean task = new ReleaseTaskBean(name, type, level, label, dependId, parentId, systemId,
					systemName, systemRequireId, systemRequireName, businessRequireId, businessRequireName,
					codeBranchName, codeBaselineName, requestUser.getName(), principal, expect, autoExecute,
					cronExpression, description, remark, "01",
					(attachment == null ? "" : attachment.getOriginalFilename()));
			int rows = releaseTaskService.saveReleaseTaskOrder(task);
			if (attachment != null) {
				releaseTaskService.uploadReleaseTaskAttachment(attachment, task.getId());
			}
			// QuartzManager.addTaskJob(task.getId());
			return MessageHelper.wrap("result", true, "message", "保存发布任务成功", "id", task.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "保存发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "updateReleaseTaskOrder", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseTaskOrder(@RequestParam("taskId") int taskId, @RequestParam("name") String name,
			@RequestParam("type") String type, @RequestParam("level") String level, @RequestParam("label") String label,
			@RequestParam("dependId") String dependId, @RequestParam("parentId") int parentId,
			@RequestParam("systemId") String systemId, @RequestParam("systemName") String systemName,
			@RequestParam("systemRequireId") String systemRequireId,
			@RequestParam("systemRequireName") String systemRequireName,
			@RequestParam("businessRequireId") String businessRequireId,
			@RequestParam("businessRequireName") String businessRequireName,
			@RequestParam("codeBranchName") String codeBranchName,
			@RequestParam("codeBaselineName") String codeBaselineName, @RequestParam("principal") String principal,
			@RequestParam(name = "expectTime", required = false, defaultValue = "") String expectTime,
			@RequestParam("autoExecute") boolean autoExecute, @RequestParam("cronExpression") String cronExpression,
			@RequestParam("description") String description, @RequestParam("remark") String remark,
			@RequestParam(name = "file", required = false) MultipartFile attachment,
			@Context HttpServletRequest request) {
		try {
			if (name == null || "".equals(name.trim())) {
				throw new Exception("发布任务名称不允许为空，请检查！");
			}
			ReleaseTaskBean oldTask = releaseTaskService.getReleaseTaskOrderById(taskId);
			if (oldTask == null) {
				throw new Exception("原发布任务不存在，无法更新，请检查！");
			} else {
				if (!name.equals(oldTask.getName())) {
					List<ReleaseTaskBean> names = releaseTaskService.getReleaseTaskOrderByName(name);
					if (names != null && names.size() > 0) {
						throw new Exception("新的发布任务名称[" + name + "]已存在，请重新命名！");
					}
				}
			}
			if (cronExpression != null && !"".equals(cronExpression.trim())) {
				if (!QuartzManager.checkCronExpression(cronExpression)) {
					throw new Exception("定时任务表达式[" + cronExpression + "]不符合规范，请检查！");
				}
			}
			String status = oldTask.getStatus();
			if ("03".equals(status) || "05".equals(status)) {
				String statusError = "03".equals(status) ? "当前发布任务正在处理中" : "当前发布任务已关闭";
				throw new Exception(statusError + "，不允许修改。！");
			}

			Date expect = null;
			if (expectTime != null && !"".equals(expectTime.trim())) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				expect = format.parse(expectTime);
				Date now = new Date();
				if (now.after(expect)) {
					throw new Exception("计划完成时间不能早于当前时间，请重新设置！");
				}
			}
			User user = (User) request.getSession().getAttribute("user");
			if (!"admin".equals(user.getName()) && !user.getName().equals(oldTask.getCreater())) {
				throw new Exception("目前登录用户[" + user.getName() + "]不是任务创建人，无权修改！");
			}
			ReleaseTaskBean updateTask = new ReleaseTaskBean(taskId, name, type, level, label, dependId, parentId,
					systemId, systemName, systemRequireId, systemRequireName, businessRequireId, businessRequireName,
					codeBranchName, codeBaselineName, user.getName(), principal, expect, autoExecute, cronExpression,
					description, remark, "01", attachment == null ? "" : attachment.getOriginalFilename());
			releaseTaskService.updateReleaseTaskOrder(updateTask);
			if (attachment != null) {
				releaseTaskService.uploadReleaseTaskAttachment(attachment, taskId);
			}
			// QuartzManager.updateTaskJob(taskId);
			return MessageHelper.wrap("result", true, "message", "修改发布任务成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "修改发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "deleteReleaseTaskOrder", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReleaseTaskOrder(@RequestParam("taskId") int taskId, @Context HttpServletRequest request) {
		try {
			ReleaseTaskBean task = releaseTaskService.getReleaseTaskOrderById(taskId);
			if (task == null) {
				throw new Exception("当前发布任务已不存在，无法删除！");
			}
			int parentId = task.getParentId();
			if (parentId == -1) {
				List<ReleaseTaskBean> children = releaseTaskDao.getReleaseTasksByParentId(taskId);
				if (children != null && children.size() > 0) {
					throw new Exception("当前发布任务为父任务，请先删除其全部子任务后再删除！");
				}
			}
			List<ReleaseTaskBean> dependList = releaseTaskDao.getReleaseTasksByDependId(taskId);
			if (dependList != null && dependList.size() > 0) {
				throw new Exception("当前发布任务存在被依赖的任务，请先删除其全部被依赖任务后再删除！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if (!"admin".equals(user.getName()) && !user.getName().equals(task.getCreater())) {
				throw new Exception("目前登录用户[" + user.getName() + "]不是发布任务创建人，无权删除！");
			}
			releaseTaskService.deleteReleaseTaskOrder(taskId);
			// QuartzManager.removeTaskJob(taskId);
			return MessageHelper.wrap("result", true, "message", "删除发布任务成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "删除发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "updateReleaseTaskOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseTaskOrderStatus(@RequestParam("flowInstanceId") String flowInstanceId,
			@RequestParam("status") String status) {
		try {
			int count = releaseTaskDao.countReleaseTaskOrderByFlowInstanceId(flowInstanceId);
			if (count == 0) {
				return MessageHelper.wrap("result", true, "message", "更新发布任务状态成功");
			}
			Map<String, Object> map = new HashMap<>();
			map.put("flowInstanceId", flowInstanceId);
			map.put("status", status);
			releaseTaskService.updateReleaseTaskOrderStatus(map);
			return MessageHelper.wrap("result", true, "message", "更新发布任务状态成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "更新发布任务状态失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listReleaseDependTaskOrders", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseDependTaskOrders(
			@RequestParam(name = "systemId", required = false, defaultValue = "") String systemId,
			@Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("systemId", systemId);
			List<ReleaseTaskBean> list = releaseTaskService.listReleaseDependTaskOrders(params);
			return MessageHelper.wrap("result", true, "message", "获取依赖任务列表成功", "data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取依赖任务列表失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listReleaseParentTaskOrders", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseParentTaskOrders(
			@RequestParam(name = "systemId", required = false, defaultValue = "") String systemId,
			@Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("systemId", systemId);
			List<ReleaseTaskBean> list = releaseTaskService.listReleaseParentTaskOrders(params);
			return MessageHelper.wrap("result", true, "message", "获取父任务列表成功", "data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取父任务列表失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "executeTaskOrderMannual", method = RequestMethod.POST)
	@ResponseBody
	public String executeTaskOrderMannual(@RequestParam("taskId") int taskId,
			@RequestParam("releaseId") String releaseId, @RequestParam("releasePhaseId") String releasePhaseId,
			@Context HttpServletRequest request) {
		try {
			ReleaseTaskBean task = releaseTaskService.getReleaseTaskOrderById(taskId);
			// 时间校验
			Date expectTime = task.getExpectTime();
			if (expectTime != null) {
				Date now = new Date();
				if (now.after(expectTime)) {
					return MessageHelper.wrap("result", false, "message", "发布任务失败:当前时间已经超过任务计划完成时间！");
				}
			}
			// 负责人校验
			User user = (User) request.getSession().getAttribute("user");
			if (!user.getName().equals("admin") && !user.getName().equals(task.getPrincipal())) {
				return MessageHelper.wrap("result", false, "message", "发布任务失败:当前用户既不是负责人也不是管理员，没有权限执行发布操作！");
			}
			int parentId = task.getParentId();
			if (parentId != -1) {
				// FIXME 如果为父任务，是否发布其所有子任务?

			}
			//如果有依赖任务，依赖任务必须全部处理成功
			String dependId = task.getDependId();
			if (!"[]".equals(dependId)) {
				List<String> dependsIds = JSON.parseObject(dependId, new TypeReference<List<String>>() {});
				for(String item : dependsIds) {
					int depend = Integer.parseInt(item);
					ReleaseTaskBean dependTask = releaseTaskService.getReleaseTaskOrderById(depend);
					String status = dependTask.getStatus();
					//04已解决，05已关闭，07处理成功
					if(!("04".equals(status) || "05".equals(status) || "07".equals(status))) {
						return MessageHelper.wrap("result", false, "message", "任务[" + task.getName() + "]所依赖的父任务[" 
								+ dependTask.getName() + "]未处理成功，无法执行当前发布任务！");
					}
				}
			}
			// 状态校验
			String status = task.getStatus();
			//03处理中，05已关闭
			if ("03".equals(status) || "05".equals(status)) {
				String statusError = status.equals("03") ? "处理中" : "已关闭";
				return MessageHelper.wrap("result", false, "message", "发布任务失败:当前任务状态为[" + statusError + "]不允许执行发布操作！");
			}
			String flowInstanceId = releaseTaskService.executeTaskOrderMannual(taskId, releaseId, releasePhaseId);
			return MessageHelper.wrap("result", true, "message", "开始执行发布任务", "flowInstanceId", flowInstanceId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "执行发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "callCRRealeaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String callCRRealeaseTask(@RequestParam("taskList") String taskList,
			@RequestParam("systemName") String systemName, @RequestParam("environmentId") String environmentId,
			@RequestParam("codeBranchName") String codeBranchName,
			@RequestParam("codeBaseLineName") String codeBaseLineName,
			@RequestParam("flowInstanceId") String flowInstanceId, @RequestParam("flowNodeId") String flowNodeId,
			@Context HttpServletRequest request) {
		try {
			Map<String, String> params = new HashMap<>();
			params.put("taskList", taskList);
			params.put("systemName", systemName);
			params.put("environmentId", environmentId);
			params.put("codeBranchName", codeBranchName);
			params.put("codeBaseLineName", codeBaseLineName);
			params.put("flowInstanceId", flowInstanceId);
			params.put("flowNodeId", flowNodeId);
			releaseTaskService.callCRRealeaseTask(params);
			return MessageHelper.wrap("result", true, "message", "开始执行发布任务！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "执行发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listReleasesBySystem", method = RequestMethod.GET)
	@ResponseBody
	public String listReleasesBySystem(@RequestParam("systemId") String systemId, @Context HttpServletRequest request) {
		try {
			List<Map<String, Object>> list = releaseTaskService.listReleasesBySystem(systemId);
			return MessageHelper.wrap("result", true, "message", "获取发布列表成功", "data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取发布列表失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "listPhasesByRelease", method = RequestMethod.GET)
	@ResponseBody
	public String listPhasesByRelease(@RequestParam("systemId") String systemId,
			@RequestParam("releaseId") String releaseId, @Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("systemId", systemId);
			params.put("releaseId", releaseId);
			List<Map<String, Object>> list = releaseTaskService.listPhasesByRelease(params);
			return MessageHelper.wrap("result", true, "message", "获取发布阶段列表成功", "data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取发布阶段列表失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "downloadReleaseTaskAttachment", method = RequestMethod.GET)
	@ResponseBody
	public String downloadReleaseTaskAttachment(@RequestParam("taskId") int taskId,
			@Context HttpServletResponse response) {
		try {
			ReleaseTaskBean task = releaseTaskService.getReleaseTaskOrderById(taskId);
			String attachment = task.getAttachment();
			if (attachment == null || "".equals(attachment)) {
				return MessageHelper.wrap("result", false, "message", "当前任务未上传附件");
			} else {
				boolean download = Utils.downloadFile(url, port, usr, pwd, "releaseTask" + File.separator + taskId,
						attachment, "releaseTask" + File.separator + taskId);
				if (download) {
					File file = new File("releaseTask" + File.separator + taskId + File.separator + attachment);
					response.setHeader("content-type", "application/octet-stream");
					response.setContentType("application/octet-stream");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + URLEncoder.encode(attachment, "UTF8"));
					ServletOutputStream os = response.getOutputStream();
					BufferedOutputStream bos = new BufferedOutputStream(os);
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					int length = -1;
					byte[] bytes = new byte[1024 * 8];
					while ((length = bis.read(bytes)) != -1) {
						bos.write(bytes, 0, length);
					}
					bis.close();
					bos.flush();
					bos.close();
				} else {
					return MessageHelper.wrap("result", false, "message", "下传附件失败！");
				}
			}
			return MessageHelper.wrap("result", true, "message", "下载附件成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "下载附件失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "addCRRealeaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String addCRRealeaseTask(@RequestParam("name") String name, @RequestParam("type") String type,
			@RequestParam("level") String level,
			@RequestParam(name = "label", required = false, defaultValue = "") String label,
			@RequestParam("status") String status,
			@RequestParam(name = "dependId", required = false, defaultValue = "[]") String dependId,
			@RequestParam(name = "parentId", required = false, defaultValue = "-1") int parentId,
			@RequestParam("systemName") String systemName, @RequestParam("releasePhaseName") String releasePhaseName,
			@RequestParam("creater") String creater, @RequestParam("principal") String principal,
			@RequestParam(name = "expectTime", required = false, defaultValue = "") String expectTime,
			@RequestParam(name = "description", required = false, defaultValue = "") String description,
			@RequestParam(name = "remark", required = false, defaultValue = "") String remark,
			@RequestParam(name = "attachment", required = false, defaultValue = "") String attachment,
			@RequestParam(name = "systemRequireId", required = false, defaultValue = "") String systemRequireId,
			@RequestParam(name = "systemRequireName", required = false, defaultValue = "") String systemRequireName,
			@RequestParam(name = "businessRequireId", required = false, defaultValue = "") String businessRequireId,
			@RequestParam(name = "businessRequireName", required = false, defaultValue = "") String businessRequireName,
			@RequestParam(name = "codeBranchName", required = false, defaultValue = "") String codeBranchName,
			@RequestParam(name = "codeBaselineName", required = false, defaultValue = "") String codeBaselineName,
			@Context HttpServletRequest request) {
		try {
			if (name == null || "".equals(name.trim())) {
				throw new Exception("发布任务名称不能为空！");
			}
			if (type == null || "".equals(type.trim())) {
				throw new Exception("发布任务类型不能为空！");
			}
			if (level == null || "".equals(level.trim())) {
				throw new Exception("发布任务等级不能为空！");
			}
			if (creater == null || "".equals(creater.trim())) {
				throw new Exception("发布任务创建人不能为空！");
			}
			if (principal == null || "".equals(principal.trim())) {
				throw new Exception("发布任务负责人不能为空！");
			}
			if (systemName == null || "".equals(systemName.trim())) {
				throw new Exception("发布任务系统名称不能为空！");
			}
			if (releasePhaseName == null || "".equals(releasePhaseName.trim())) {
				throw new Exception("发布任务环境名称不能为空！");
			}
			List<ReleaseTaskBean> names = releaseTaskService.getReleaseTaskOrderByName(name);
			if (names != null && names.size() > 0) {
				throw new Exception("发布任务名称[" + name + "]已存在，请重新命名！");
			}
			String systemId = resourceDao.getResourceIdByName(systemName);
			if (systemId == null) {
				throw new Exception("系统名称[" + systemName + "]不存在！");
			}
			/*Map<String, Object> cluster = clusterDAO.findClusterByName(environmentName);
			if (cluster == null) {
				throw new Exception("环境名称[" + environmentName + "]不存在！");
			}
			String environmentId = "" + cluster.get("id");
			*/
			Map<String, Object> params = new HashMap<>();
			params.put("systemName", systemName);
			params.put("releasePhaseName", releasePhaseName);
			List<Map<String, Object>> phases = releaseTaskDao.getReleasePhaseByName(params);
			if(phases == null) {
				throw new Exception("环境名称[" + releasePhaseName + "]不存在！");
			}
			else if(phases.size() > 1) {
				throw new Exception("环境名称[" + releasePhaseName + "]存在多个，无法保存！");
			}
			else {
				
			}
			String releasePhaseId = "" + phases.get(0).get("id");
			Date expect = null;
			if (expectTime != null && !"".equals(expectTime.trim())) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				expect = format.parse(expectTime);
				Date now = new Date();
				if (now.after(expect)) {
					throw new Exception("期望时间不能早于当前时间，请重新设置！");
				}
			}
			ReleaseTaskBean task = new ReleaseTaskBean(name, type, level, label, status, dependId, parentId, systemId,
					systemName, releasePhaseId, creater, principal, expect, description, remark, "04",
					attachment, systemRequireId, systemRequireName, businessRequireId, businessRequireName,
					codeBranchName, codeBaselineName);
			int rows = releaseTaskService.addCRRealeaseTask(task);
			if (attachment != null && !"".equals(attachment)) {
				if (request.getInputStream() != null) {
					releaseTaskService.uploadCRReleaseTaskAttachment(attachment, request.getInputStream(),
							task.getId());
				}
			}
			// QuartzManager.addTaskJob(task.getId());
			return MessageHelper.wrap("result", true, "message", "同步发布任务成功", "id", task.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "同步发布任务失败:" + e.getMessage());
		}
	}

	@RequestMapping(value = "executeCRRealeaseTasksByBatch", method = RequestMethod.POST)
	@ResponseBody
	public String executeCRRealeaseTasksByBatch(@RequestParam("taskList") String taskList,
			@Context HttpServletRequest request) {
		try {
			releaseTaskService.executeCRRealeaseTasksByBatch(taskList);
			return MessageHelper.wrap("result", true, "message", "开始批量发布任务！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "批量发布任务执行失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "listReleasePhasesBySystemName", method = RequestMethod.GET)
	@ResponseBody
	public String listReleasePhasesBySystemName(@RequestParam("systemName") String systemName,
			@Context HttpServletRequest request) {
		try {
			List<Map<String, Object>> list = releaseTaskService.listPhasesBySystemName(systemName);
			return MessageHelper.wrap("result", true, "message", "获取发布阶段列表成功", "data", list);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取发布阶段列表失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "executeITSMReleaseTasksByBatch", method = RequestMethod.POST)
	@ResponseBody
	public String executeITSMReleaseTasksByBatch(@RequestParam("taskList") String taskList,
			@RequestParam("releasePhaseId") String releasePhaseId, 
			@RequestParam("systemName") String systemName, 
			@Context HttpServletRequest request) {
		try {
			String flowInstance = releaseTaskService.executeITSMReleaseTasksByBatch(taskList, releasePhaseId);
			return MessageHelper.wrap("result", true, "message", "ITSM开始批量发布任务！", "flowInstance", flowInstance);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "ITSM批量发布任务执行失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "callJiraRealeaseTask", method = RequestMethod.POST)
	@ResponseBody
	public String callJiraRealeaseTask(@RequestParam("taskKey") String taskKey,
			@Context HttpServletRequest request) {
		try {
			releaseTaskService.executeJiraRealeaseTask(taskKey);
			return MessageHelper.wrap("result", true, "message", "开始执行jira发布任务！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "执行jira发布任务失败:" + e.getMessage());
		}
	}

}
