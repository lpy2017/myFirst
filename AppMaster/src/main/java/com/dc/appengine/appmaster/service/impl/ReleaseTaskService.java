package com.dc.appengine.appmaster.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.IReleaseIntegrationDao;
import com.dc.appengine.appmaster.dao.IReleaseTaskDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseIntegration;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.integration.IntegrationSynchronizationManager;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IReleaseTaskService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.ReleaseTaskThreadPool;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.Utils;

@Service("releaseTaskService")
public class ReleaseTaskService implements IReleaseTaskService {

	private static final Logger log = LoggerFactory.getLogger(ReleaseTaskService.class);

	@Autowired
	@Qualifier("blueprintService")
	IBlueprintService blueprintService;

	@Resource
	IAudit auditService;

	@Autowired
	@Qualifier("releaseTaskDao")
	private IReleaseTaskDao releaseTaskDao;

	@Autowired
	@Qualifier("releaseIntegrationDao")
	private IReleaseIntegrationDao releaseIntegrationDao;

	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplateDao;

	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintDao;

	@Value(value = "${ftp.url}")
	String url;

	@Value(value = "${ftp.port}")
	int port;

	@Value(value = "${ftp.user}")
	String usr;

	@Value(value = "${ftp.pwd}")
	String pwd;

	@Value(value = "${ftp.timeOut}")
	String timeOut;

	@Value(value = "${ftp.home.path}")
	String ftpHome;

	@Value(value = "${flowServerUrl}")
	String flowServerUrl;

	@Value(value = "${itsm.single.timeout}")
	Long itsmTimeout;

	@Override
	public Page listReleaseTaskOrders(Map<String, Object> params, int pageNum, int pageSize) {
		Page page = releaseTaskDao.listReleaseTaskOrders(params, pageNum, pageSize);
		return page;
	}

	@Override
	public ReleaseTaskBean getReleaseTaskOrderById(int taskId) {
		return releaseTaskDao.getReleaseTaskOrderById(taskId);
	}

	@Override
	public List<ReleaseTaskBean> getReleaseTaskOrderByName(String name) {
		return releaseTaskDao.getReleaseTaskOrderByName(name);
	}

	@Transactional
	@Override
	public int saveReleaseTaskOrder(ReleaseTaskBean task) {
		try {
			return releaseTaskDao.saveReleaseTaskOrder(task);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void updateReleaseTaskOrder(ReleaseTaskBean task) {
		try {
			releaseTaskDao.updateReleaseTaskOrder(task);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void deleteReleaseTaskOrder(int taskId) {
		try {
			releaseTaskDao.deleteReleaseTaskOrder(taskId);
			Utils.deleteFolder(timeOut, url, port, usr, pwd, "releaseTask", String.valueOf(taskId));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public void updateReleaseTaskOrderStatus(Map<String, Object> params) {
		try {
			releaseTaskDao.updateReleaseTaskOrderStatus(params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<ReleaseTaskBean> listReleaseDependTaskOrders(Map<String, Object> params) {
		return releaseTaskDao.listReleaseDependTaskOrders(params);
	}

	@Override
	public List<ReleaseTaskBean> listReleaseParentTaskOrders(Map<String, Object> params) {
		return releaseTaskDao.listReleaseParentTaskOrders(params);
	}

	@Transactional
	@Override
	public String executeTaskOrderMannual(int taskId, String releaseId, String releasePhaseId) {
		try {
			// 更新绑定信息
			Map<String, Object> params = new HashMap<>();
			params.put("id", taskId);
			params.put("releaseId", releaseId);
			params.put("releasePhaseId", releasePhaseId);
			releaseTaskDao.updateTaskOrderRelease(params);

			Map<String, Object> releasePhase = releaseTaskDao.getReleasePhaseById(releasePhaseId);
			Object blueprintInstance = releasePhase.get("blueprint_ins_id");
			Object blueprintFlow = releasePhase.get("flow_id");
			if (blueprintInstance == null || blueprintFlow == null) {
				throw new RuntimeException("当前任务绑定的发布未关联及配置应用环境，请关联后再执行！");
			}
			// 执行流程
			String flowInstance = null;
			List<String> jobs = new ArrayList<>();
			jobs.add("MIS-" + taskId);
			Map<String, List<String>> extendedAttributes = new HashMap<>();
			extendedAttributes.put("jobs", jobs);
			ReleaseTaskBean task = releaseTaskDao.getReleaseTaskOrderById(taskId);
			Map<String, String> map = new HashMap<>();
			map.put("_userName", task.getPrincipal());
			map.put("_extendedAttributes", JSON.toJSONString(extendedAttributes));
			String result = blueprintService.executeBlueprintFlow("" + blueprintFlow, "" + blueprintInstance, map);
			JSONObject flowResult = JSON.parseObject(result);
			if (flowResult.getBooleanValue("result")) {
				flowInstance = flowResult.getString("id");
				Map<String, Object> param = new HashMap<>();
				param.put("id", taskId);
				param.put("blueprintFlowInstance", flowInstance);
				param.put("status", "03");// 处理中
				releaseTaskDao.updateReleaseTaskOrderExecute(param);
			} else {
				throw new RuntimeException("工作流流程执行失败，请检查工作流日志！");
			}
			return flowInstance;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public String callCRRealeaseTask(Map<String, String> params) {
		Map<String, List<String>> extendedAttributes = new HashMap<>();
		List<String> jobs = new ArrayList<>();
		String taskList = params.get("taskList");
		Map<String, List<String>> task = JSON.parseObject(taskList, new TypeReference<Map<String, List<String>>>() {
		});
		Set<String> keys = task.keySet();
		for (String key : keys) {
			List<String> value = task.get(key);
			jobs.addAll(value);
		}
		extendedAttributes.put("jobs", jobs);
		String blueprintInstanceId = "301346e3782743f4814e6c50fff9b0e4";
		String cdFlowId = "20180626105302182";
		Map<String, Object> bpInfo = blueprintDao.getBlueprintInstance(blueprintInstanceId);
		Map<String, Object> flowInfo = blueprintTemplateDao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		Map<String, String> param = new HashMap<>();
		param.put("_userName", "chinaLifeIt");
		param.put("_extendedAttributes", JSON.toJSONString(extendedAttributes));
		String result;
		try {
			result = blueprintService.executeBlueprintFlow(cdFlowId, blueprintInstanceId, param);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = MessageHelper.wrap("result", false, "message", "执行蓝图流程失败：" + e.getMessage());
		}
		JSONObject resultJson = JSON.parseObject(result);
		int operateResult = resultJson.getBoolean("result") ? 1 : 0;
		ThreadPool.service.execute(new Runnable() {
			@Override
			public void run() {
				auditService.save(new AuditEntity("chinaLifeIt", ResourceCode.BLUEPRINTFLOW,
						"" + flowInfo.get("FLOW_NAME"), ResourceCode.Operation.EXECUTE, operateResult,
						"蓝图实例[" + bpInfo.get("INSTANCE_NAME") + "]执行蓝图流程[" + flowInfo.get("FLOW_NAME") + "]"));
			}
		});
		return result;
	}

	@Transactional
	@Override
	public boolean uploadReleaseTaskAttachment(MultipartFile attachment, int taskId) {
		try {
			String fileName = attachment.getOriginalFilename();
			InputStream file = attachment.getInputStream();
			Utils.deleteFile(url, port, usr, pwd, "releaseTask" + File.separator + taskId, fileName);
			boolean upload = Utils.uploadFileAndUnzip(url, port, usr, pwd, "releaseTask" + File.separator + taskId,
					fileName, file, ftpHome + "releaseTask" + File.separator);
			if (!upload) {
				throw new RuntimeException("上传附件[" + fileName + "]失败！");
			}
			return upload;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> listReleasesBySystem(String systemId) {
		List<Map<String, Object>> list = releaseTaskDao.listReleasesBySystem(systemId);
		return list;
	}

	@Override
	public List<Map<String, Object>> listPhasesByRelease(Map<String, Object> params) {
		List<Map<String, Object>> list = releaseTaskDao.listPhasesByRelease(params);
		return list;
	}

	@Transactional
	@Override
	public int addCRRealeaseTask(ReleaseTaskBean task) {
		try {
			return releaseTaskDao.addCRRealeaseTask(task);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	@Override
	public boolean uploadCRReleaseTaskAttachment(String fileName, ServletInputStream inputStream, int taskId) {
		try {
			Utils.deleteFile(url, port, usr, pwd, "releaseTask" + File.separator + taskId, fileName);
			boolean upload = Utils.uploadFileAndUnzip(url, port, usr, pwd, "releaseTask" + File.separator + taskId,
					fileName, inputStream, ftpHome + "releaseTask" + File.separator);
			if (!upload) {
				throw new RuntimeException("上传附件[" + fileName + "]失败！");
			}
			return upload;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void executeCRRealeaseTasksByBatch(String taskList) {
		try {
			List<String> tasks = JSON.parseObject(taskList, new TypeReference<List<String>>() {
			});
			Map<String, Map<String, List<ReleaseTaskBean>>> allMap = new HashMap<>();
			for (String taskId : tasks) {
				ReleaseTaskBean task = releaseTaskDao.getReleaseTaskOrderById(Integer.parseInt(taskId));
				String phaseId = task.getReleasePhaseId();
				if (phaseId == null) {
					throw new RuntimeException("发布任务[" + task.getName() + "]未绑定环境（发布阶段），无法执行发布！");
				} else {
					validateTask(task);
					String systemId = task.getSystemId();
					if (!allMap.containsKey(systemId)) {
						List<ReleaseTaskBean> releaseList = new ArrayList<>();
						releaseList.add(task);
						Map<String, List<ReleaseTaskBean>> releaseMap = new HashMap<>();
						releaseMap.put(phaseId, releaseList);
						allMap.put(systemId, releaseMap);
					} else {
						Map<String, List<ReleaseTaskBean>> releaseMap = allMap.get(systemId);
						if (!releaseMap.containsKey(phaseId)) {
							List<ReleaseTaskBean> releaseList = new ArrayList<>();
							releaseList.add(task);
							releaseMap.put(phaseId, releaseList);
						} else {
							releaseMap.get(phaseId).add(task);
						}
					}
				}
			}
			executeRealeaseTasksByBatch(allMap);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private void executeRealeaseTasksByBatch(Map<String, Map<String, List<ReleaseTaskBean>>> allMap) {
		for (String systemId : allMap.keySet()) {
			Map<String, List<ReleaseTaskBean>> releaseMap = allMap.get(systemId);
			for (String phaseId : releaseMap.keySet()) {
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						Map<String, Object> releasePhase = releaseTaskDao.getReleasePhaseById(phaseId);
						Object blueprintInstance = "" + releasePhase.get("blueprint_ins_id");
						Object blueprintFlow = "" + releasePhase.get("flow_id");
						// 执行流程
						String flowInstance = null;
						List<Integer> taskIds = new ArrayList<>();
						List<String> jobs = new ArrayList<>();
						List<ReleaseTaskBean> tasks = releaseMap.get(phaseId);
						for (ReleaseTaskBean task : tasks) {
							taskIds.add(task.getId());
							jobs.add("MIS-" + task.getId());
						}
						Map<String, List<String>> extendedAttributes = new HashMap<>();
						extendedAttributes.put("jobs", jobs);
						Map<String, String> map = new HashMap<>();
						map.put("_userName", "timeBatchReleaseTaskUser");
						map.put("_extendedAttributes", JSON.toJSONString(extendedAttributes));
						String result = blueprintService.executeBlueprintFlow("" + blueprintFlow,
								"" + blueprintInstance, map);
						JSONObject flowResult = JSON.parseObject(result);
						if (flowResult.getBooleanValue("result")) {
							flowInstance = flowResult.getString("id");
							Map<String, Object> param = new HashMap<>();
							param.put("ids", taskIds);
							param.put("blueprintFlowInstance", flowInstance);
							param.put("status", "03");// 处理中
							releaseTaskDao.updateReleaseTaskOrderBatchExecute(param);
						} else {
							throw new RuntimeException("流程执行失败:" + flowResult.getString("message"));
						}

					}
				};
				ReleaseTaskThreadPool.getInstance().getService().execute(runnable);
			}
		}
	}

	private void checkReleaseTask(String parent, List<String> children) {
		ReleaseTaskBean parentTask = releaseTaskDao.getReleaseTaskOrderById(Integer.parseInt(parent));
		for (String child : children) {
			ReleaseTaskBean childTask = releaseTaskDao.getReleaseTaskOrderById(Integer.parseInt(child));
			checkSibship(parentTask, childTask);
		}

	}

	private void checkSibship(ReleaseTaskBean parentTask, ReleaseTaskBean childTask) {
		if (parentTask.getReleasePhaseId().equals(childTask.getReleasePhaseId())) {
			throw new RuntimeException(
					"父任务[" + parentTask.getName() + "]和子任务[" + childTask.getName() + "]的环境(发布阶段)不相同");
		}
	}

	private void validateTask(ReleaseTaskBean task) throws RuntimeException {
		Map<String, Object> phase = releaseTaskDao.getReleasePhaseById(task.getReleasePhaseId());
		Object blueprintInstance = "" + phase.get("blueprint_ins_id");
		Object blueprintFlow = "" + phase.get("flow_id");
		if (blueprintInstance == null || blueprintFlow == null) {
			throw new RuntimeException("当前任务绑定的环境（发布阶段）未配置蓝图实例和蓝图流程，无法发布！");
		}
		// 校验时间
		Date expectTime = task.getExpectTime();
		if (expectTime != null) {
			Date now = new Date();
			if (now.after(expectTime)) {
				throw new RuntimeException("当前时间已经晚于发布任务[" + task.getName() + "]的计划完成时间！");
			}
		}
		// 校验状态
		String status = task.getStatus();
		// 03处理中，05已关闭
		if ("03".equals(status) || "05".equals(status)) {
			String statusError = status.equals("03") ? "处理中" : "已关闭";
			throw new RuntimeException("当前任务状态为[" + statusError + "]不允许执行发布操作！");
		}
		// 校验依赖任务
		// 校验责任人
	}

	@Override
	public List<Map<String, Object>> listPhasesBySystemName(String systemName) {
		List<Map<String, Object>> list = releaseTaskDao.listPhasesBySystemName(systemName);
		return list;
	}

	@Override
	public String executeITSMReleaseTasksByBatch(String taskList, String releasePhaseId) {
		List<String> tasks = JSON.parseObject(taskList, new TypeReference<List<String>>() {
		});
		Map<String, Object> releasePhase = releaseTaskDao.getReleasePhaseById(releasePhaseId);
		Object blueprintInstance = "" + releasePhase.get("blueprint_ins_id");
		Object blueprintFlow = "" + releasePhase.get("flow_id");
		// 执行流程
		String flowInstance = null;
		List<String> jobs = new ArrayList<>();
		for (String task : tasks) {
			jobs.add("MIS-" + task);
		}
		Map<String, List<String>> extendedAttributes = new HashMap<>();
		extendedAttributes.put("jobs", jobs);
		Map<String, String> map = new HashMap<>();
		map.put("_userName", "itsm");
		map.put("_extendedAttributes", JSON.toJSONString(extendedAttributes));
		String result = blueprintService.executeBlueprintFlow("" + blueprintFlow, "" + blueprintInstance, map);
		JSONObject flowResult = JSON.parseObject(result);
		if (flowResult.getBooleanValue("result")) {
			flowInstance = flowResult.getString("id");
		} else {
			throw new RuntimeException("流程执行失败:" + flowResult.getString("message"));
		}
		monitorFlowInstanceAndCallBackItsm(taskList, flowInstance);
		return flowInstance;
	}

	private void monitorFlowInstanceAndCallBackItsm(String taskList, String flowInstance) {
		// 检查流程执行情况
		ReleaseTaskThreadPool.getInstance().getService().execute(new Runnable() {
			@Override
			public void run() {
				boolean status = monitorFlow(flowInstance);
				callBackItsm(taskList, status, flowInstance);
			}

			private void callBackItsm(String taskList, boolean status, String flowInstance) {
				// FIXME 回调更新itsm工单状态， 参数taskList,status,flowInstance

			}
		});
	}

	@Override
	public void executeJiraRealeaseTask(String taskKey) {
		List<ReleaseTaskBean> tasks = releaseTaskDao.getReleaseTaskOrderByName(taskKey);
		if (tasks == null) {
			throw new RuntimeException("jira发布任务[" + taskKey + "]在发布管理平台不存在！请先同步更新后再执行！");
		} else if (tasks != null && tasks.size() > 1) {
			throw new RuntimeException("jira发布任务[" + taskKey + "]在发布管理平台存在多个！请先同步更新后再执行！");
		} else {
		}
		ReleaseTaskBean task = tasks.get(0);
		String releasePhaseId = task.getReleasePhaseId();
		if (releasePhaseId == null) {
			throw new RuntimeException("jira发布任务[" + taskKey + "]未绑定环境（发布阶段）！请检查jira配置同步更新后再执行！");
		}
		Map<String, Object> releasePhase = releaseTaskDao.getReleasePhaseById(releasePhaseId);
		Object blueprintInstance = "" + releasePhase.get("blueprint_ins_id");
		Object blueprintFlow = "" + releasePhase.get("flow_id");
		// 执行流程
		String flowInstance = null;
		List<String> jobs = new ArrayList<>();
		jobs.add(taskKey);
		Map<String, List<String>> extendedAttributes = new HashMap<>();
		extendedAttributes.put("jobs", jobs);
		Map<String, String> map = new HashMap<>();
		map.put("_userName", "jira");
		map.put("_extendedAttributes", JSON.toJSONString(extendedAttributes));
		String result = blueprintService.executeBlueprintFlow("" + blueprintFlow, "" + blueprintInstance, map);
		JSONObject flowResult = JSON.parseObject(result);
		if (flowResult.getBooleanValue("result")) {
			flowInstance = flowResult.getString("id");
		} else {
			throw new RuntimeException("流程执行失败:" + flowResult.getString("message"));
		}
		monitorFlowInstanceAndCallBackJira(task, flowInstance);
	}

	private void monitorFlowInstanceAndCallBackJira(ReleaseTaskBean task, String flowInstance) {
		ReleaseTaskThreadPool.getInstance().getService().execute(new Runnable() {
			@Override
			public void run() {
				// 检查流程执行情况
				boolean status = monitorFlow(flowInstance);
				int integrationId = task.getIntegrationId();
				ReleaseIntegration integration = releaseIntegrationDao.getReleaseIntegrationById(integrationId);
				try {
					IntegrationSynchronizationManager.getInstance().getIntegrationImpl(integration.getType())
							.callbackStatus(integration.getUrl(), integration.getUser(), integration.getPassword(),
									status, task.getName());
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		});
	}
	
	private boolean monitorFlow(String flowInstance) {
		boolean status = false;
		Map<String, Object> instanceMap = new HashMap<>();
		instanceMap.put("instanceId", flowInstance);
		List<Map> instanceList = new ArrayList<>();
		instanceList.add(instanceMap);
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("instancesList", JSON.toJSONString(instanceList, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect));
		long times = -1L;
		if (itsmTimeout <= 0) {
			// 不设置超时时间，360次30分钟
			times = 360L;
		} else {
			times = itsmTimeout / 5000L;
			if (itsmTimeout % 5000L != 0) {
				times++;
			}
		}
		while (times > 0) {
			try {
				// 5秒查一次
				Thread.sleep(5000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String instanceResult = restUtil.postForObject(flowServerUrl + "/WFService/getInstancesStatus.wf",
					requestEntity, String.class);
			Map<String, Object> instanceResultMap = JSON.parseObject(instanceResult);
			if ((Boolean) instanceResultMap.get("state")) {
				List<Map> instanceResultData = JSON.parseArray(instanceResultMap.get("data").toString(), Map.class);
				String instanceFlowState = (String) instanceResultData.get(0).get("flowState");
				Object isOverTime = (String) instanceResultData.get(0).get("isOverTime");
				String instanceState = String.valueOf(instanceResultData.get(0).get("state"));
				// 流程已结束
				if (Constants.Monitor.FINISHED.equals(instanceFlowState)) {
					break;
				}
				// 流程未结束
				else {
					// 挂起超时
					if (Constants.Monitor.OverTime.equals(isOverTime)) {
						times = 0;
						break;
					}
					// 状态已结束
					if ("2".equals(instanceState)) {
						break;
					}
					// 状态正在执行0/失败7/...
					else {
						times--;
					}
				}
			} else {
				times--;
			}
		}
		if (times == 0) {
			// 超时时间到了还没执行结束/流程超时
			status = false;
		} else {
			status = true;
		}
		return status;
	}

}
