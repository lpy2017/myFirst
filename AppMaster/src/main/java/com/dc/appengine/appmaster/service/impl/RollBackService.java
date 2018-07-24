package com.dc.appengine.appmaster.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.IRollBackDao;
import com.dc.appengine.appmaster.dao.impl.RollBackDao;
import com.dc.appengine.appmaster.entity.AppSnapshot;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.PatternUtil;
import com.dc.appengine.appmaster.utils.RollBackExecutorPool;

@Service("rollBackService")
public class RollBackService {

	private static final Logger log = LoggerFactory.getLogger(RollBackService.class);
	private static final String RUNNING = "RUNNING";
	private static final String DEPLOYED = "DEPLOYED";
	private static final String UNDEPLOYED = "UNDEPLOYED";

	private static final HttpComponentsClientHttpRequestFactory httpRequestFactory;
	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(3000);
		httpRequestFactory.setConnectTimeout(3000);
		httpRequestFactory.setReadTimeout(3000);
	}

	private long appId;
	
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	
	@Value(value = "${rollback.threadPoolSize}")
	int size;
	
	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;

	@Autowired
	@Qualifier("applicationService")
	private IApplicationService applicationService;

	@Autowired
	@Qualifier("instanceService")
	private IInstanceService instanceService;

	@Autowired
	@Qualifier("blueprintService")
	private IBlueprintService blueprintService;

	@Autowired
	@Qualifier("rollBackDao")
	IRollBackDao rollBackDao;

	public String appSnapshotRollBack(String snapId, long appId, int type) {
		this.appId = appId;
		// 当前实例
		List<Map<String, Object>> currInstanceList = instanceService.getInstanceList(appId);
		// 过滤调状态为undeployed的实例
		filterFailedInstance(currInstanceList);
		// List<Instance> currInstances = loadInstances(appId,
		// currInstanceList);
		AppSnapshot appSnapshot = rollBackDao.getSnapshotOfApp(snapId, appId);
		String snapshotInfo = appSnapshot.getSnapshotInfo();
		// 组件在快照保存时实例的input/output是json，故整体保存存在json嵌套，而JSON.toJSONString(List<Map<String,
		// Object>>())格式转换时对于嵌套的json当做字符串处理
		snapshotInfo = snapshotInfo.replaceAll("\"\\{", "{").replaceAll("}\"", "}");
		// 快照保存时JSON.toJSONString(List<Map<String,Object>>())把实例的input/output里面的json字符串的引号做了转义
		snapshotInfo = snapshotInfo.replaceAll("\\\\\"","\"");
		// 快照实例
		List<Map<String, Object>> snapshotInstanceList = JSON.parseObject(snapshotInfo,
				new TypeReference<List<Map<String, Object>>>() {
				});
		// 过滤调状态为undeployed的实例
		filterFailedInstance(snapshotInstanceList);
		// List<Instance> snapshotInstances = loadInstances(appId,
		// snapshotInstanceList);

		// 需要转换状态进行回滚的实例对list
		List<RollBackKeyValue> statusConverts = new ArrayList<RollBackKeyValue>();
		// 搜索当前和快照node、version、output、input完全相同的实例;
		for (Map<String, Object> ssInstance : snapshotInstanceList) {
			for (Map<String, Object> crInstance : currInstanceList) {
				if (compareInstance(crInstance, ssInstance)) {
					statusConverts.add(new RollBackKeyValue(crInstance, ssInstance));
				}
			}
		}

		// 过滤可转换状态的实例
		if (!statusConverts.isEmpty()) {
			for (RollBackKeyValue keyValue : statusConverts) {
				Map<String, Object> cr = keyValue.getCurrent();
				Map<String, Object> ss = keyValue.getTarget();
				if (snapshotInstanceList.contains(ss)) {
					snapshotInstanceList.remove(ss);
				}
				if (currInstanceList.contains(cr)) {
					currInstanceList.remove(cr);
				}
			}
		}

		// 需要操作流程进行回滚的实例对list
		List<RollBackKeyValue> flowConverts = new ArrayList<RollBackKeyValue>();
		{
			for (Map<String, Object> ssInstance : snapshotInstanceList) {
				String ssNodeIp = "" + ssInstance.get("nodeIp");
				String ssVersion = "" + ssInstance.get("resVersionId");
				Map<String, String> ssInput = JSON.parseObject("" + ssInstance.get("input"),
						new TypeReference<Map<String, String>>() {
						});
				Map<String, String> ssOutput = JSON.parseObject("" + ssInstance.get("output"),
						new TypeReference<Map<String, String>>() {
						});
				Map<String, Object> matchCrInstance = null;
				// 当前实例list不为空，寻找匹配实例
				if (currInstanceList.size() > 0) {
					for (Map<String, Object> crInstance : currInstanceList) {
						String crNodeIp = "" + crInstance.get("nodeIp");
						String crVersion = "" + crInstance.get("resVersionId");
						Map<String, String> crInput = JSON.parseObject("" + crInstance.get("input"),
								new TypeReference<Map<String, String>>() {
								});
						Map<String, String> crOutput = JSON.parseObject("" + crInstance.get("output"),
								new TypeReference<Map<String, String>>() {
								});
						if (ssNodeIp.equals(crNodeIp)) {
							// 比较快照实例和当前实例的nodeId和input里面关键key，如path、port、dir、root、log，如果有value有相同则匹配上
							Iterator<Entry<String, String>> ssIter = ssInput.entrySet().iterator();
							while (ssIter.hasNext()) {
								Entry<String, String> ssEntry = ssIter.next();
								String ssKey = ssEntry.getKey();
								String ssValue = ssEntry.getValue();
								if (ssKey.contains("path") || ssKey.contains("Path") || ssKey.contains("port")
										|| ssKey.contains("Port") || ssKey.contains("ports") || ssKey.contains("root")
										|| ssKey.contains("dir") || ssKey.contains("Dir") || ssKey.contains("log")) {
									if (crInput.containsValue(ssValue)) {//ssValue.equals(crInput.get(ssKey))
										matchCrInstance = crInstance;
										break;
									}
								}
							}
						}
						if (matchCrInstance != null) {
							break;
						}
					}
					if (matchCrInstance != null) {
						// 匹配上的实例对存入flowConverts
						flowConverts.add(new RollBackKeyValue(matchCrInstance, ssInstance));
						// 当前实例从currInstanceList移除
						currInstanceList.remove(matchCrInstance);
					} else {
						// 没匹配上实例
						Map<String, Object> matchCrrInstance = null;
						// 退而求其次匹配nodeId相同的实例
						for (Map<String, Object> crrInstance : currInstanceList) {
							String crrNodeIp = "" + crrInstance.get("nodeIp");
							if (ssNodeIp.equals(crrNodeIp)) {
								matchCrrInstance = crrInstance;
								break;
							}
						}
						if (matchCrrInstance != null) {
							// 退而求其次匹配上的实例对存入flowConverts
							flowConverts.add(new RollBackKeyValue(matchCrrInstance, ssInstance));
							// 当前实例从currInstanceList移除
							currInstanceList.remove(matchCrrInstance);
						}
						// 退而求其次也没匹配上
						else {
							// 只能从当前实例list中选第一个作为匹配
							matchCrrInstance = currInstanceList.remove(0);
							flowConverts.add(new RollBackKeyValue(matchCrrInstance, ssInstance));
						}
					}
				}
				// 当前实例list已空(已匹配完)，则快照实例自己处理
				{
					flowConverts.add(new RollBackKeyValue(null, ssInstance));
				}
			}
			// 快照实例list已匹配完，当前实例还存在，则当前实例自己处理
			if (currInstanceList.size() > 0) {
				for (Map<String, Object> currInstance : currInstanceList) {
					flowConverts.add(new RollBackKeyValue(currInstance, null));
				}
			}
		}

		// 滚动升级
		if (type == 1) {
			List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
			List<String> ops = new ArrayList<String>();
			// 状态可切换的情况
			for (RollBackKeyValue keyValue : statusConverts) {
				final Map<String, Object> current = keyValue.getCurrent();
				final Map<String, Object> target = keyValue.getTarget();
				String cStatus = "" + current.get("status");
				String tStatus = "" + target.get("status");
				if (tStatus.equalsIgnoreCase(DEPLOYED)) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							rollBackDao.updateInstanceById("" + current.get("id"), "" + target.get("id"));
						}
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 在工作流回调时更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							current.put("updateInstanceIdBySnapshot", target.get("id"));
						}
						instances.add(current);
						ops.add("stop");
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					}
				} else if (tStatus.equalsIgnoreCase(RUNNING)) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 在工作流回调时更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							current.put("updateInstanceIdBySnapshot", target.get("id"));
						}
						instances.add(current);
						ops.add("start");
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							rollBackDao.updateInstanceById("" + current.get("id"), "" + target.get("id"));
						}
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					}
				} else if (tStatus.equalsIgnoreCase(UNDEPLOYED)) {
					// 无视
				} else {
				}
			}

			// 处理操作流程的情况
			for (RollBackKeyValue keyValue : flowConverts) {
				final Map<String, Object> current = keyValue.getCurrent();
				final Map<String, Object> target = keyValue.getTarget();
				String cStatus = "" + current.get("status");
				String tStatus = "" + target.get("status");
				if (current != null) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						// 卸载
						instances.add(current);
						ops.add("destroy");
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						// 卸载
						instances.add(current);
						ops.add("stop");
						// 删除
						instances.add(current);
						ops.add("destroy");
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					} else {
					}
				}
				if (target != null) {
					if (tStatus.equalsIgnoreCase(DEPLOYED)) {
						//部署前先入库,默认状态是UNDEPLOYED
						target.put("appId", appId);
						rollBackDao.saveInstanceBySnapshot(target);
						// 部署
						// FIXME 时间更新为快照时间？
						instances.add(target);
						ops.add("deploy");
					} else if (tStatus.equalsIgnoreCase(RUNNING)) {
						// 部署
						instances.add(target);
						ops.add("deploy");
						// 启动
						// FIXME 时间更新为快照时间？
						instances.add(target);
						ops.add("start");
					} else if (tStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					} else {
					}
				}
			}

			// 穿行执行
			instanceOperation(instances, ops);
		}

		// 快速升级
		if (type == 2) {
			// 处理状态可切换的情况
			for (RollBackKeyValue keyValue : statusConverts) {
				List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
				List<String> ops = new ArrayList<String>();
				final Map<String, Object> current = keyValue.getCurrent();
				final Map<String, Object> target = keyValue.getTarget();
				String cStatus = "" + current.get("status");
				String tStatus = "" + target.get("status");
				if (tStatus.equalsIgnoreCase(DEPLOYED)) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							rollBackDao.updateInstanceById("" + current.get("id"), "" + target.get("id"));
						}
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 在工作流回调时更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							current.put("updateInstanceIdBySnapshot", target.get("id"));
						}
						instances.add(current);
						ops.add("stop");
						CallFlowRollBack cfrb = new CallFlowRollBack(instances, ops);
						// 停止该实例
						RollBackExecutorPool.getInstance().execute(cfrb);
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					}
				} else if (tStatus.equalsIgnoreCase(RUNNING)) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 在工作流回调时更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							current.put("updateInstanceIdBySnapshot", target.get("id"));
						}
						instances.add(current);
						ops.add("start");
						CallFlowRollBack cfrb = new CallFlowRollBack(instances, ops);
						// 启动该实例
						RollBackExecutorPool.getInstance().execute(cfrb);
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						if (!current.get("id").equals(target.get("id"))) {
							// 更新当前实例为快照实例
							// FIXME 时间更新为快照时间？
							rollBackDao.updateInstanceById("" + current.get("id"), "" + target.get("id"));
						}
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					}
				} else if (tStatus.equalsIgnoreCase(UNDEPLOYED)) {
					// 无视
				} else {
				}
			}
			// 处理操作流程的情况
			for (RollBackKeyValue keyValue : flowConverts) {
				final Map<String, Object> current = keyValue.getCurrent();
				final Map<String, Object> target = keyValue.getTarget();
				String cStatus = "" + current.get("status");
				String tStatus = "" + target.get("status");
				List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
				List<String> ops = new ArrayList<String>();
				if (current != null) {
					if (cStatus.equalsIgnoreCase(DEPLOYED)) {
						// 卸载
						instances.add(current);
						ops.add("destroy");
					} else if (cStatus.equalsIgnoreCase(RUNNING)) {
						// 卸载
						instances.add(current);
						ops.add("stop");
						// 删除
						instances.add(current);
						ops.add("destroy");
					} else if (cStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					} else {
					}
				}
				if (target != null) {
					if (tStatus.equalsIgnoreCase(DEPLOYED)) {
						//部署前先入库,默认状态是UNDEPLOYED
						target.put("appId", appId);
						rollBackDao.saveInstanceBySnapshot(target);
						// 部署
						// FIXME 时间更新为快照时间？
						instances.add(target);
						ops.add("deploy");
					} else if (tStatus.equalsIgnoreCase(RUNNING)) {
						// 部署
						instances.add(target);
						ops.add("deploy");
						// 启动
						// FIXME 时间更新为快照时间？
						instances.add(target);
						ops.add("start");
					} else if (tStatus.equalsIgnoreCase(UNDEPLOYED)) {
						// 无视
					} else {
					}
				}
				CallFlowRollBack cfrb = new CallFlowRollBack(instances, ops);
				// 调用线程池执行此串行流程
				RollBackExecutorPool.getInstance().execute(cfrb);
			}
		}

		// 其他
		else {
		}
		return MessageHelper.wrap("result", true, "id", snapId, "message", "回滚成功");
	}

	private void filterFailedInstance(List<Map<String, Object>> list) {
		List<Map<String, Object>> temps = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> instance : list) {
			String status = "" + instance.get("status");
			if ("UNDEPLOYED".equalsIgnoreCase("status")) {
				temps.add(instance);
			}
		}
		if (temps.size() > 0) {
			for (Map<String, Object> temp : temps) {
				list.remove(temp);
			}
		}
	}

	public void instanceOperation(List<Map<String, Object>> instances, List<String> ops) {
		if (instances != null && instances.size() > 0) {
			Map<String, Object> instance = instances.get(0);
			String op = ops.get(0);
			Map<String, List<? extends Object>> message = new HashMap<>();
			message.put("" + instance.get("id"), assembleMessage(instances, ops));
			log.info("assembleMessage==" + JSON.toJSONString(message));

			// 放置到消息管理器中
			putMessage(message);

			RestTemplate rest = new RestTemplate();
			Map<String, Object> versionDetail = resourceService
					.getResourceVersionDetail("" + instance.get("resVersionId"));
			Map<String, String> flows = (Map<String, String>) versionDetail.get("flows");
			long flowId = Long.parseLong(flows.get(op));
			Map<String, String> varMap = new HashMap<String, String>();
			varMap.put("_taskBKey", "" + instance.get("id"));
			String insvarMap = JSON.toJSONString(varMap);
			log.info("taskBKeyMap == " + insvarMap);
			String encoderJson = "";
			try {
				encoderJson = URLEncoder.encode(insvarMap, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error(e.getMessage(), e);
			}
			String startUrl = flowServerUrl + "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId + "&initiator=admin"
					+ "&insvarMap=" + encoderJson;
			log.info("startUrl==" + startUrl);
			String startResult = rest.getForObject(startUrl, String.class);
			log.info("startResult==" + startResult);
		}

	}

	public List<Object> assembleMessage(List<Map<String, Object>> instances, List<String> ops) {
		String currentOp = ops.get(0);
		// 移除第一个操作
		ops.remove(0);
		Map<String, Object> instance = instances.get(0);
		// 移除第一个实例
		instances.remove(0);
		List<Object> messagesList = new ArrayList<>();

		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.error(e.getMessage(), e);
		}

		Application appInfo = applicationService.getApp(appId);
		String appName = appInfo.getAppName();
		String nodeIp = instanceService.findNodeIP("" + instance.get("id"));
		Map<String, Object> resourceVersionDetail = resourceService
				.getResourceVersionDetail("" + instance.get("resVersionId"));
		Map<String, Object> inputMap = JSON.parseObject("" + instance.get("input"));
		Map<String, Object> outputMap = JSON.parseObject("" + instance.get("output"));
		Map<String, Object> map = new HashMap<>();
		Map<String, String> flows = (Map<String, String>) resourceVersionDetail.get("flows");
		long flowId = -1;
		if (flows.containsKey(currentOp)) {
			flowId = Long.valueOf(flows.get(currentOp));
		}

		map.put("instanceId", instance.get("id"));
		map.put("operation", currentOp);
		map.put("nodeIp", nodeIp);
		map.put("deployPath", instance.get("id"));
		map.put("resouceUrl", resourceVersionDetail.get("url"));
		map.put("version", resourceVersionDetail.get("version"));
		map.put("componentName", appName);
		map.put("componentInput", inputMap);
		map.put("componentOutput", outputMap);
		
		// 当前实例状态切换后更新当前实例id为快照id
		if(instance.containsKey("updateInstanceIdBySnapshot")){
			map.put("updateInstanceIdBySnapshot", instance.get("updateInstanceIdBySnapshot"));
		}

		// 有后续ops，记录在map内，工作流回调时触发后续流程；
		if (instances.size() > 0) {
			assembleOpsQueue(instances, ops, map);
		}

		// 获取空子流程ID
		long emptyFlowId = blueprintService.getEmptyFlowId();
		if (flowId != Long.valueOf(emptyFlowId)) {
			Map<String, Object> subFlowInfo = blueprintService.getBlueprintTypeByFlowId(flowId);
			String flowInfo = (String) subFlowInfo.get("FLOW_INFO");
			BluePrint bp = JSON.parseObject(flowInfo, BluePrint.class);
			List<Element> elements = bp.getNodeDataArray();
			for (Element ele : elements) {
				Map<String, String> params = ele.getParams();
				map.put(ele.getPluginName(), params);
			}
		}

		Map<String, Object> configMap = new HashMap<>();
		configMap.putAll(inputMap);
		configMap.putAll(outputMap);
		Map<String, String> templateMap = (Map<String, String>) resourceVersionDetail.get("templates");
		Map<String, String> templateMap1 = templateReplace(templateMap, "" + instance.get("id"), configMap);
		map.put("configTemplate", templateMap1);

		// FIXME 稍后替换为从组件子流程中读取
		Map<String, Object> pluginInputMap = new HashMap<>();
		map.put("pluginInput", pluginInputMap);
		messagesList.add(map);

		return messagesList;
	}

	private void assembleOpsQueue(List<Map<String, Object>> instances, List<String> ops, Map<String, Object> map) {
		map.put("opsQueue", JSON.toJSONString(ops, SerializerFeature.WriteDateUseDateFormat));
		map.put("instanceQueue", JSON.toJSONString(instances, SerializerFeature.WriteDateUseDateFormat));
	}

	public Map<String, String> templateReplace(Map<String, String> templateMap, String instanceId,
			Map<String, Object> tempMapConfig) {
		Map<String, String> templateMap1 = new HashMap<String, String>();
		String template = JSONObject.toJSONString(templateMap);
		Pattern p = Pattern.compile(PatternUtil.P1);
		Matcher mat = p.matcher(template);
		List<String> group = new ArrayList<String>();
		if (template.contains("${instanceId?if_exists}")) {
			template = template.replace("${instanceId?if_exists}", instanceId);
		} else if (mat.find()) {
			group.add(mat.group());
			if (group.size() > 0) {
				for (int i = 0; i < group.size(); i++) {
					Matcher mat1Temp = p.matcher(group.get(i));
					if (mat1Temp.find()) {
						String configKey = mat1Temp.group(1);
						String configValue = (String) tempMapConfig.get(configKey);
						template = template.replace(group.get(i), configValue);
					}
				}
			}
		}
		templateMap1 = (Map<String, String>) JSON.parse(template);
		return templateMap1;

	}

	private void putMessage(Map<String, List<? extends Object>> message) {
		RestTemplate rest = new RestTemplate(httpRequestFactory);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
		mvm.add("message", JSON.toJSONString(message));
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(mvm, requestHeaders);
		String messageResult = rest.postForObject(flowServerUrl + "/cd/message.wf", httpEntity, String.class);
	}

	private HashMap<String, List<Map<String, Object>>> groupByNode(List<Map<String, Object>> instances) {
		HashMap<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> instance : instances) {
			if (!map.containsKey("" + instance.get("nodeId"))) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				list.add(instance);
				map.put("" + instance.get("nodeId"), list);
			} else {
				map.get("" + instance.get("nodeId")).add(instance);
			}
		}
		return map;
	}

	private boolean compareInstance(Map<String, Object> crInstance, Map<String, Object> ssInstance) {
		if (crInstance.get("nodeId").equals(ssInstance.get("nodeId"))
				&& crInstance.get("resVersionId").equals(ssInstance.get("resVersionId"))) {
			if (compareMap("" + crInstance.get("input"), "" + ssInstance.get("input"))) {
				if (compareMap("" + crInstance.get("output"), "" + ssInstance.get("output"))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean compareMap(String crPut, String ssPut) {
		Map<String, String> cr = JSON.parseObject(crPut, new TypeReference<Map<String, String>>() {
		});
		Map<String, String> ss = JSON.parseObject(ssPut, new TypeReference<Map<String, String>>() {
		});
		boolean flag = true;
		if (!ss.isEmpty()) {
			Iterator<Entry<String, String>> ssIter = ss.entrySet().iterator();
			while (ssIter.hasNext()) {
				Entry<String, String> ssEntry = ssIter.next();
				String key = ssEntry.getKey();
				String value = ssEntry.getValue();
				if (!value.equals(cr.get(key))) {
					flag = false;
					break;
				}
			}
		} else {
			if (!cr.isEmpty()) {
				flag = false;
			}
		}
		if (!cr.isEmpty()) {
			Iterator<Entry<String, String>> crIter = cr.entrySet().iterator();
			while (crIter.hasNext()) {
				Entry<String, String> crEntry = crIter.next();
				String key = crEntry.getKey();
				String value = crEntry.getValue();
				if (!value.equals(ss.get(key))) {
					flag = false;
					break;
				}
			}
		} else {
			if (!ss.isEmpty()) {
				flag = false;
			}
		}
		return flag;
	}

	private List<Instance> loadInstances(long appId, List<Map<String, Object>> InstanceList) {
		List<Instance> instances = new ArrayList<Instance>();
		for (Map<String, Object> map : InstanceList) {
			Instance instance = new Instance();
			instance.setId("" + map.get("id"));
			instance.setAppId(String.valueOf(appId));
			instance.setStatus("" + map.get("status"));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			try {
				Date date = format.parse("" + map.get("deployTime"));
				instance.setDeployTime(date);
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
			instance.setNodeId("" + map.get("nodeId"));
			instance.setAppVersionId("" + map.get("resVersionId"));
			instance.setLxcIp("" + map.get("lxcIp"));
			instance.setComponentInput(
					JSON.parseObject("" + map.get("input"), new TypeReference<Map<String, String>>() {
					}));
			instance.setComponentOutput(
					JSON.parseObject("" + map.get("output"), new TypeReference<Map<String, String>>() {
					}));
			instance.setComponentInputTemp(
					JSON.parseObject("" + map.get("inputTemp"), new TypeReference<Map<String, String>>() {
					}));
			instance.setComponentOutputTemp(
					JSON.parseObject("" + map.get("outputTemp"), new TypeReference<Map<String, String>>() {
					}));
			instances.add(instance);
		}
		return instances;
	}

	class CallFlowRollBack implements Runnable {
		private List<Map<String, Object>> instances;
		private List<String> ops;

		public CallFlowRollBack(List<Map<String, Object>> instances, List<String> ops) {
			this.instances = instances;
			this.ops = ops;
		}

		@Override
		public void run() {
			instanceOperation(instances, ops);
		}

	}

	class RollBackKeyValue {
		private Map<String, Object> current;
		private Map<String, Object> target;

		public RollBackKeyValue(Map<String, Object> current, Map<String, Object> target) {
			this.current = current;
			this.target = target;
		}

		public Map<String, Object> getCurrent() {
			return current;
		}

		public void setCurrent(Map<String, Object> current) {
			this.current = current;
		}

		public Map<String, Object> getTarget() {
			return target;
		}

		public void setTarget(Map<String, Object> target) {
			this.target = target;
		}

	}

	public static void main(String[] args) {
		// List<Map<String, Object>> instanceList = new ArrayList<Map<String,
		// Object>>();
		// Map<String, Object> map1 = new HashMap<String, Object>();
		// map1.put("a", "b");
		// map1.put("c", "d");
		// Map<String, Object> map2 = new HashMap<String, Object>();
		// map2.put("1", "2");
		// map2.put("3", "4");
		// instanceList.add(map1);
		// instanceList.add(map2);
		// // String aa = JSON.toJSONString(instanceList,
		// // SerializerFeature.WriteDateUseDateFormat);
		// String aa = JSON.toJSONString(instanceList);
		// System.out.println(aa);

		 String ss = "[{\"out\":{\"output\":\"a\"},\"status\":\"DEPLOYED\"}]";
		// String ss =
		// "[{\"output\":\"a\",\"status\":\"DEPLOYED\"},{\"output\":\"b\",\"status\":\"STARTED\"}]";
//		String ss = "[{\"out\":\"{\"output\":\"a\"}\",\"status\":\"DEPLOYED\"}]";
		// List<HashMap> bb = JSON.parseArray(ss, HashMap.class);
		List<Map<String, String>> bb = JSON.parseObject(ss, new TypeReference<List<Map<String, String>>>() {
		});
		System.out.println(bb);
		System.out.println(bb.get(0).get("out"));
	}

}
