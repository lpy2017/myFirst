package com.dc.appengine.cloudui.master.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Service("LogService")
public class LogService {

	private static final Logger log = LoggerFactory.getLogger(LogService.class);
	public Map<String, Object> nodeState = new HashMap<>();

	public String getProcessMonitorInfo(String id, String flowId) throws Exception {
		// 获取流程信息from master
		String processJson = WSRestClient.getMasterWebTarget().path("blueprint/getFlowInfo")
				.queryParam("flowId", flowId).request().get(String.class);
		Map<String, Object> processJsonMap = JSONObject.parseObject(processJson);
		Map<String, Object> flowInfo = (Map<String, Object>) processJsonMap.get("flowInfo");// 流程信息
		log.info("流程信息 from master =======" + JSON.toJSONString(flowInfo));

		// 获取运行实例id的组件状态信息 from frame
		Map<String, Object> map = new HashMap<>();
		Form form = new Form();
		map.put("instanceid", id);
		form.param("paramMap", JSON.toJSONString(map));
		String processStateJson = WSRestClient.getFrameWebTarget().path("WFService")
				.path("findProcessMonitorInfoByMap.wf").request()
				.post(Entity.entity(form, "application/x-www-form-urlencoded;charset=utf-8"), String.class);
		log.info("流程信息 from frame =======" + processStateJson);
		Map<String, Object> processStateJsonMap = JSON.parseObject(processStateJson);
		Boolean state = (Boolean) processStateJsonMap.get("state");
		if (!state) {
			log.error("请求frame 异常！");
			return JSON.toJSONString(flowInfo);
		}
		List<?> nodeMonitorInfo = JSON.parseArray(processStateJsonMap.get("data").toString(), Map.class);
		List<Map<String, Object>> nodeDataArray = (List<Map<String, Object>>) flowInfo.get("nodeDataArray");// 流程节点
		String bluePrintId = flowInfo.get("bluePrintId").toString();// 流程id
		flowInfo.put("nodeDataArray",
				countNodeState(bluePrintId, nodeDataArray, (List<Map<String, Object>>) nodeMonitorInfo));
		List<Map<String, Object>> linkDataArray = (List<Map<String, Object>>) flowInfo.get("linkDataArray");// 流程连线
		flowInfo.put("linkDataArray", countNodeLinkState(linkDataArray));
		return JSON.toJSONString(flowInfo);
	}

	public List<Map<String, Object>> countNodeState(String bluePrintId, List<Map<String, Object>> nodeDataArray,
			List<Map<String, Object>> monitorInfo) {
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
			int successCount = 0;
			int failCount = 0;
			int doingCount = 0;
			int instotal = isEmpty(node.get("ins"))? 1 : (Integer) node.get("ins");
			int insRuntotal = 0;
			int undoCount = 0;
			String nodeState = "7";
			Boolean match = false;// 匹配标识
			for (Map<String, Object> info : monitorInfo) {
				Object nodeCommend = info.get("nodeComment");
				String nodeId = info.get("activityId").toString();
				String state = ""; // success=2,fail=7,doing=0,undo=-1
				if (info.get("startTime") != null && info.get("endTime") != null && info.get("state") != null
						&& "0".equals(info.get("state").toString())) {// 控流程的状态特殊处理
					state = "2";
				} else {
					state = info.get("state").toString();
				}
				if (nodeCommend != null && identityMark.equals(nodeCommend.toString())) {// 对比唯一标识（blueId_key）
					insRuntotal++;
					match = true;
					if ("2".equals(state)) {
						successCount++;
					} else if ("7".equals(state)) {
						failCount++;
					} else if ("0".equals(state)) {
						doingCount++;
					}
				}
			}
			if (instotal < insRuntotal) {// 如果组件是多实例，则ins以正在运行的实例个数为准
				instotal = insRuntotal;
				nodeModify.put("ins", instotal);
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
			this.nodeState.put(key, nodeState);// 记录节点状态，用于判断连线状态
			nodeModify.put("successCount", successCount);
			nodeModify.put("failCount", failCount);
			nodeModify.put("doingCount", doingCount);
			nodeModify.put("undoCount", undoCount);
			nodeModify.put("state", nodeState);
			result.add(nodeModify);
		}
		return result;
	}

	public List<Map<String, Object>> countNodeLinkState(List<Map<String, Object>> nodeLinkArray) {
		for (Map<String, Object> link : nodeLinkArray) {
			String from = link.get("from").toString();
			String to = link.get("to").toString();
			Object fromState = this.nodeState.get(from);
			Object toState = this.nodeState.get(to);
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
		Form form = new Form();
		map.put("instanceid", id);
		form.param("paramMap", JSON.toJSONString(map));
		String nodesJson = WSRestClient.getFrameWebTarget().path("WFService").path("getCDFlowNodes.wf").request()
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		log.info("流程节点信息 from frame =======" + nodesJson);
		Map<String, Object> nodesJsonMap = JSON.parseObject(nodesJson);
		Boolean state = (Boolean) nodesJsonMap.get("state");
		if (!state) {
			log.error("请求frame异常！");
			return MessageHelper.wrap("result", false, "message", nodesJsonMap.get("errMessage"));
		}
		String nodes = nodesJsonMap.get("data").toString();
		return nodes;
	}

	public String getPluginNodeLogInfo(String flowInstanceId, String flowNodeId) {
		Map<String, Object> param = new HashMap<>();
		param.put("flowInstanceId", flowInstanceId);
		param.put("flowNodeId", flowNodeId);
		// 获取流程信息from master
		String processJson = WSRestClient.getMasterWebTarget().path("blueprint/getNodesLogRecord")
				.queryParam("flowInstanceId", flowInstanceId).queryParam("flowNodeId", flowNodeId).request()
				.get(String.class);
		log.debug("流程节点日志信息 from master =======" + processJson);
		return processJson;
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
}