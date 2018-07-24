package com.dc.appengine.adapter.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.adapter.entity.NodeStatus;
import com.dc.appengine.adapter.service.ClusterService;
import com.dc.appengine.adapter.service.LabelService;
import com.dc.appengine.adapter.service.NodeService;
import com.dc.appengine.adapter.util.WSClient;

@RestController
@EnableAutoConfiguration
@RequestMapping("node")
public class NodeRestService {

	@Autowired
	private NodeService nodeService;
	@Autowired
	private LabelService labelService;
	@Autowired 
	private ClusterService clusterService;

	private Logger log = LoggerFactory.getLogger(this.getClass());

//	@RequestMapping(method = RequestMethod.POST)
//	@ResponseBody
//	public String create(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		String id = UUID.randomUUID().toString();
//		param.put("id", id);
//		param.put("vm_id", (String) body.get("vm_id"));
//		param.put("iaas_id", (String) body.get("iaas_id"));
//		param.put("cluster_id", (String) body.get("cluster_id"));
//		param.put("ip", (String) body.get("ip"));
//		param.put("cpu", (String) body.get("cpu") == null ? null : Integer.valueOf((String) body.get("cpu")));
//		param.put("memory", (String) body.get("memory") == null ? null : Integer.valueOf((String) body.get("memory")));
//		param.put("disk", (String) body.get("disk") == null ? null : Integer.valueOf((String) body.get("disk")));
//		param.put("status", NodeStatus.nonregistered);
//		param.put("name", (String) body.get("name"));
//		param.put("os_version", (String) body.get("os_version"));
//		param.put("docker_version", (String) body.get("docker_version"));
//		param.put("agent_version", (String) body.get("agent_version"));
//		int i = nodeService.insert(param);
//		String labelsAll = (String) body.get("labels");
//		if (labelsAll != null) {
//			String[] labelsStr = labelsAll.split(",");
//			for (String labelStr : labelsStr) {
//				String key = labelStr.split("=")[0];
//				String value = labelStr.split("=")[1];
//				param.clear();
//				param.put("id", UUID.randomUUID().toString());
//				param.put("node_id", id);
//				param.put("key", key);
//				param.put("value", value);
//				labelService.insert(param);
//			}
//		}
////		List<String> bridges = bridgeService.findByCluster((String) body.get("cluster_id"));
////		param.clear();
////		param.put("id", UUID.randomUUID().toString());
////		param.put("node_id", id);
////		if (bridges.size() > 0) {
////			param.put("bridge_ip", bridges.get(0));
////		} else {
////			param.put("bridge_ip", BridgeHelper.getInstance().getBridgeIP());
////		}
////		bridgeService.insert(param);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (i == 1) {
//			log.debug("node insert into db success {}", param);
//			result.put("result", true);
//			result.put("id", id);
//			return JSON.toJSONString(result);
//		} else {
//			log.error("node insert into db fail {}", param);
//			result.put("result", false);
//			result.put("info", "node insert into db fail");
//			return JSON.toJSONString(result);
//		}
//	}
	
	@RequestMapping(value = "preregister", method = RequestMethod.POST)
	@ResponseBody
	public String preregister(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ip", this.getIP(request));
		log.debug("node_preregister select param {}", param);
		List<Map<String, Object>> nodes = nodeService.select(param);
		log.debug("node_preregister select result {}", nodes);
		if (nodes.size() > 0) {
			if (nodes.size() == 1) {
				Map<String, Object> node = nodes.get(0);
				if (NodeStatus.nonregistered.equals(node.get("status"))) {
					param.put("id", node.get("id"));
					param.put("cpu", (String) body.get("cpu") == null ? null : Integer.valueOf((String) body.get("cpu")));
					param.put("memory", (String) body.get("memory") == null ? null : Integer.valueOf((String) body.get("memory")));
					param.put("disk", (String) body.get("disk") == null ? null : Integer.valueOf((String) body.get("disk")));
					param.put("status", NodeStatus.applied);
					param.put("os_version", (String) body.get("os_version"));
					param.put("docker_version", (String) body.get("docker_version"));
					param.put("agent_version", (String) body.get("agent_version"));
					param.put("labels", body.get("labels"));
					nodeService.update(param);
					this.noticeMasterOpNode(param, "update");
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("result", true);
					result.put("id", node.get("id"));
					result.put("ip", this.getIP(request));
					return JSON.toJSONString(result);
				}
			}
			Map<String, Object> result = new HashMap<String, Object>();
			log.error("node {} already exist", this.getIP(request));
			result.put("result", false);
			result.put("info", "node already exist");
			return JSON.toJSONString(result);
		}
		String id = UUID.randomUUID().toString();
		param.put("id", id);
		param.put("vm_id", (String) body.get("vm_id"));
		param.put("iaas_id", (String) body.get("iaas_id"));
		param.put("cluster_id", (String) body.get("cluster_id"));
		param.put("ip", this.getIP(request));
		param.put("cpu", (String) body.get("cpu") == null ? null : Integer.valueOf((String) body.get("cpu")));
		param.put("memory", (String) body.get("memory") == null ? null : Integer.valueOf((String) body.get("memory")));
		param.put("disk", (String) body.get("disk") == null ? null : Integer.valueOf((String) body.get("disk")));
		param.put("status", NodeStatus.applied);
		param.put("name", (String) body.get("name"));
		param.put("os_version", (String) body.get("os_version"));
		param.put("docker_version", (String) body.get("docker_version"));
		param.put("agent_version", (String) body.get("agent_version"));
		int i = nodeService.insert(param);
//		String labelsAll = (String) body.get("labels");
//		if (labelsAll != null && !"".equals(labelsAll)) {
//			String[] labelsStr = labelsAll.split(",");
//			for (String labelStr : labelsStr) {
//				String key = labelStr.split("=")[0];
//				String value = labelStr.split("=")[1];
//				param.clear();
//				param.put("id", UUID.randomUUID().toString());
//				param.put("node_id", id);
//				param.put("key", key);
//				param.put("value", value);
//				labelService.insert(param);
//			}
//		}
		// TODO
		List<Object> labelslist = null;
		String labelStr = (String) body.get("labels");
		if (labelStr != null && !"".equals(labelStr)) {
			JSONArray labellist = JSON.parseArray(labelStr);
			if (labellist.size() > 0) {
				Map<String, Object> labels = new HashMap<>();
				labels.put("node_id", id);
				labels.put("labels", labellist);
				labelService.multiInsert(labels);
				labelslist = labellist;
			}
		}
//		List<Map<String, Object>> labellist = (List<Map<String,Object>>) body.get("labels");
//		if (labellist.size() > 0) {
//			Map<String, Object> labels = new HashMap<>();
//			labels.put("node_id", id);
//			labels.put("labels", labellist);
//			labelService.multiInsert(labels);
//		}
		
		
//		List<String> bridges = bridgeService.findByCluster((String) body.get("cluster_id"));
//		param.clear();
//		param.put("id", UUID.randomUUID().toString());
//		param.put("node_id", id);
//		if (bridges.size() > 0) {
//			param.put("bridge_ip", bridges.get(0));
//		} else {
//			param.put("bridge_ip", BridgeHelper.getInstance().getBridgeIP());
//		}
//		bridgeService.insert(param);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i == 1) {
			// TODO
			param.clear();
			param.put("ip", this.getIP(request));
			nodes = nodeService.select(param);
			nodes.get(0).put("labels", labelslist);
			boolean flag = this.noticeMasterOpNode(nodes.get(0), "add");
			if (!flag) {
				result.put("result", false);
				result.put("info", "master add node fail");
				return JSON.toJSONString(result);
			}
			log.debug("node insert into db success {}", param);
			result.put("result", true);
			result.put("id", id);
			result.put("ip", this.getIP(request));
			return JSON.toJSONString(result);
		} else {
			log.error("node insert into db fail {}", param);
			result.put("result", false);
			result.put("info", "node insert into db fail");
			return JSON.toJSONString(result);
		}
	}
	
//	@RequestMapping(value = "register", method = RequestMethod.PUT)
//	@ResponseBody
//	public String register(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("ip", this.getIP(request));
//		log.debug("node_register select param {}", param);
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		log.debug("node_register select result {}", nodes);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
//			node.put("cpu", (String) body.get("cpu"));
//			node.put("memory", (String) body.get("memory"));
//			node.put("disk", (String) body.get("disk"));
//			node.put("os_version", (String) body.get("os_version"));
//			node.put("docker_version", (String) body.get("docker_version"));
//			node.put("agent_version", (String) body.get("agent_version"));
//			node.put("status", NodeStatus.applied);
//			log.debug("node_register update param {}", node);
//			int i = nodeService.update(node);
//			log.debug("node_register update result {}", i);
////			result.put("result", true);
////			param.clear();
////			param.put("cluster_id", node.get("cluster_id"));
////			List<Map<String, Object>> nodes_now = nodeService.select(param);
////			if (nodes_now.size() >= 2) {
////				ExecutorServicePool.getInstance().execute(new TunnelCreateJob(node));
////			}
////			Map<String, Object> cluster = clusterService.one((String) node.get("cluster_id"));
////			INetwork network = NetworkFactory.getInstance("com.dc.appengine.adapter.network.Network" + cluster.get("network_kind"));
////			network.addNode(node);
//			ExecutorServicePool.getInstance().execute(new NoticeMasterUpdateNodeJob((String) node.get("ip"), "add"));
//			boolean flag = this.noticeMasterOpNode(node, "add");
//			if (flag) {
//				ExecutorServicePool.getInstance().execute(new NoticeNodeUpdateStatus((String) node.get("ip"), "master"));
//			}
//			result.put("result", flag);
////			result.put("info", "notice master fail");
//		} else {
//			result.put("result", false);
//			result.put("info", "db error");
//		}
//		return JSON.toJSONString(result);
//	}
	
	private boolean noticeMasterOpNode(Map<String, Object> node, String op) {
		if ("add".equals(op)) {
			WebTarget target = WSClient.getMasterWebTarget();
			Form form = new Form();
			if (node.get("cpu") instanceof Integer) {
				node.put("cpu", String.valueOf((Integer) node.get("cpu")));
			}
			if (node.get("memory") instanceof Integer) {
				node.put("memory", String.valueOf((Integer) node.get("memory")));
			}
			if (node.get("disk") instanceof Integer) {
				node.put("disk", String.valueOf((Integer) node.get("disk")));
			}
			form.param("node_info", JSON.toJSONString(node));
			log.debug("noticeMasterOpNode {} node: {}", op, node);
			String result = target.path("node").path("addnode").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
			log.debug("noticeMasterOpNode {} node result: {}", op, result);
			return (Boolean) JSON.parseObject(result).get("result");
		}
		if ("update".equals(op)) {
			WebTarget target = WSClient.getMasterWebTarget();
//			Form form = new Form();
			if (node.get("cpu") instanceof Integer) {
				node.put("cpu", String.valueOf((Integer) node.get("cpu")));
			}
			if (node.get("memory") instanceof Integer) {
				node.put("memory", String.valueOf((Integer) node.get("memory")));
			}
			if (node.get("disk") instanceof Integer) {
				node.put("disk", String.valueOf((Integer) node.get("disk")));
			}
//			form.param("node_info", JSON.toJSONString(node));
			String node_info = JSON.toJSONString(node);
			log.debug("noticeMasterOpNode {} node: {}", op, node);
			String result = target.path("node").path("updatenode").request().put(Entity.entity(node_info, MediaType.APPLICATION_JSON), String.class);
			log.debug("noticeMasterOpNode {} node result: {}", op, result);
			return "true".equals(result);
		}
		if ("remove".equals(op)) {
			WebTarget target = WSClient.getMasterWebTarget();
			log.debug("noticeMasterOpNode {} node: {}", op, node);
			String result = target.path("node").path("removenode").queryParam("id", (String) node.get("id")).queryParam("ip", (String) node.get("ip")).request().delete(String.class);
			log.debug("noticeMasterOpNode {} node result: {}", op, result);
			return "true".equals(result);
		}
		return false;
	}
	
	private String getIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}
	
//	@RequestMapping(value = "bridge_ip", method = RequestMethod.GET)
//	@ResponseBody
//	public String bridgeIP(HttpServletRequest request, HttpServletResponse response) {
//		String nodeIP = this.getIP(request);
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("ip", nodeIP);
//		log.debug("node_bridge_ip select param {}", param);
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		log.debug("node_bridge_ip select result {}", nodes);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
//			@SuppressWarnings("unchecked")
//			List<String> bridges = (List<String>) node.get("bridges");
//			Map<String, Object> cluster = clusterService.one((String) node.get("cluster_id"));
////			String network = ConfigHelper.getValue("network.impl");
////			network = network.replaceAll("com\\.dc\\.appengine\\.adapter\\.network\\.Network", "");
//			result.put("result", true);
//			result.put("bridge_ip", bridges.get(0));
//			result.put("ip", nodeIP);
//			result.put("network", cluster.get("network_kind"));
//		} else {
//			result.put("result", false);
//			result.put("info", "db error");
//		}
//		return JSON.toJSONString(result);
//	}
	
//	@RequestMapping(method = RequestMethod.GET)
//	@ResponseBody
//	public String select(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		log.debug("nodes {}", nodes);
//		result.put("nodes", nodes);
//		return JSON.toJSONString(result);
//	}
	
//	@RequestMapping(value = "cluster", method = RequestMethod.GET)
//	@ResponseBody
//	public String cluster(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("cluster_id", request.getParameter("cluster_id"));
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		log.debug("nodes {}", nodes);
//		result.put("nodes", nodes);
//		return JSON.toJSONString(result);
//	}
	
//	@RequestMapping(value = "master", method = RequestMethod.GET)
//	@ResponseBody
//	public String master(HttpServletRequest request, HttpServletResponse response) {
//		List<Map<String, Object>> nodes = new ArrayList<Map<String,Object>>();
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("cluster_id", request.getParameter("cluster_id"));
//		param.put("status", NodeStatus.healthy);
//		List<Map<String, Object>> nodesHealthy = nodeService.select(param);
//		param.put("status", NodeStatus.applied);
//		List<Map<String, Object>> nodesApplied = nodeService.select(param);
//		nodes.addAll(nodesHealthy);
//		nodes.addAll(nodesApplied);
//		log.debug("nodes {}", nodes);
//		return JSON.toJSONString(nodes);
//	}
	
	@RequestMapping(value = "ui", method = RequestMethod.GET)
	@ResponseBody
	public String ui(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", request.getParameter("cluster_id"));
		String labelsStr = request.getParameter("labels");
		if (labelsStr != null && !labelsStr.equals("")) {
			param.put("labels", labelsStr.split(","));
		}
		int page_num = Integer.valueOf(request.getParameter("pageNum"));
		int page_size = Integer.valueOf(request.getParameter("pageSize"));
		param.put("offset", (page_num - 1) * page_size);
		param.put("rows", page_size);
		List<Map<String, Object>> nodes = nodeService.ui(param);
		int total = nodeService.uicount(param);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Map<String, Object> node : nodes) {
			Timestamp ts = (Timestamp) node.get("timestamp");
			node.put("timestamp", sdf.format(new Date(ts.getTime())));
			param.clear();
			param.put("node_id", node.get("id"));
			List<Map<String, Object>> labels = labelService.select(param);
			if (labels.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (Map<String, Object> label : labels) {
					sb.append(",").append(label.get("key")).append("=").append(label.get("value"));
				}
				String labelsAll = sb.toString();
				labelsAll = labelsAll.substring(1);
				node.put("labels", labelsAll);
			} else {
				node.put("labels", "");
			}
//			int status = Integer.valueOf((String) node.get("status"));
//			if (status <= 3) {
//				node.put("editable", true);
//			} else {
//				node.put("editable", false);
//			}
			node.put("editable", true);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", nodes);
		log.debug("nodes {}", result);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "nodelist", method = RequestMethod.GET)
	@ResponseBody
	public String nodelist(HttpServletRequest request, HttpServletResponse response) {
		String cluster_id = request.getParameter("cluster_id");
		Map<String, Object> param = new HashMap<>();
		param.put("cluster_id", cluster_id);
		List<Map<String, Object>> nodes = nodeService.select(param);
		log.debug("nodes {}", nodes);
		return JSON.toJSONString(nodes);
	}
	
	@RequestMapping(value = "nodelistselected/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String nodelistselected(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body, @PathVariable("id") String cluster_id) {
		Map<String, Object> param = new HashMap<>();
		param.put("cluster_id", cluster_id);
		List<Map<String, Object>> nodes = nodeService.select(param);
		param.put("key", body.get("label_key"));
		param.put("value", body.get("label_value"));
		param.put("type", body.get("label_type"));
		List<Map<String, Object>> nodes_selected = labelService.findNodeByClusterAndLabel(param);
		Map<String, Object> dict = new HashMap<>();
		for (Map<String, Object> node : nodes_selected) {
			dict.put((String) node.get("id"), true);
		}
		for (Map<String,Object> node : nodes) {
			if (dict.containsKey(node.get("id"))) {
				node.put("selected", true);
			} else {
				node.put("selected", false);
			}
		}
		log.debug("nodes {}", nodes);
		return JSON.toJSONString(nodes);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public String one(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		Map<String, Object> node = nodeService.one(id);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("node_id", id);
		List<Map<String, Object>> labels = labelService.select(param);
		if (labels.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (Map<String, Object> label : labels) {
				sb.append(",").append(label.get("key")).append("=").append(label.get("value"));
			}
			String labelsAll = sb.toString();
			labelsAll = labelsAll.substring(1);
			node.put("labels", labelsAll);
		} else {
			node.put("labels", "");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp ts = (Timestamp) node.get("timestamp");
		node.put("timestamp", sdf.format(new Date(ts.getTime())));
//		int status = Integer.valueOf((String) node.get("status"));
//		if (status <= 3) {
//			node.put("editable", true);
//		} else {
//			node.put("editable", false);
//		}
		node.put("editable", true);
		return JSON.toJSONString(node);
	}
	
//	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
//	@ResponseBody
//	public String update(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body, @PathVariable("id") String id) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		if ((String) body.get("cpu") != null || (String) body.get("memory") != null || (String) body.get("disk") != null) {
//			param.put("id", id);
//			List<Map<String, Object>> nodes = nodeService.select(param);
//			if (nodes.size() == 1) {
//				Map<String, Object> node = nodes.get(0);
//				if ((String) body.get("cpu") != null) {
//					node.put("cpu", (String) body.get("cpu"));
//				}
//				if ((String) body.get("memory") != null) {
//					node.put("memory", (String) body.get("memory"));
//				}
//				if ((String) body.get("disk") != null) {
//					node.put("disk", (String) body.get("disk"));
//				}
//				boolean flag = this.noticeMasterOpNode(node, "update");
//				if (!flag) {
//					Map<String, Object> result = new HashMap<String, Object>();
//					result.put("result", false);
//					result.put("info", "node in use");
//					return JSON.toJSONString(result);
//				}
//			}
//		}
//		param.clear();
//		param.put("id", id);
//		param.put("vm_id", (String) body.get("vm_id"));
//		param.put("iaas_id", (String) body.get("iaas_id"));
//		param.put("cluster_id", (String) body.get("cluster_id"));
//		param.put("ip", (String) body.get("ip"));
//		param.put("cpu", (String) body.get("cpu") == null ? null : Integer.valueOf((String) body.get("cpu")));
//		param.put("memory", (String) body.get("memory") == null ? null : Integer.valueOf((String) body.get("memory")));
//		param.put("disk", (String) body.get("disk") == null ? null : Integer.valueOf((String) body.get("disk")));
//		param.put("status", (String) body.get("status"));
//		param.put("name", (String) body.get("name"));
//		log.debug("node_update param {}", param);
//		int i = nodeService.update(param);
//		log.debug("node_update result {}", i);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (i == 1) {
//			result.put("result", true);
//			return JSON.toJSONString(result);
//		} else {
//			result.put("result", false);
//			return JSON.toJSONString(result);
//		}
//	}
	
	@RequestMapping(value = "{id}/updateLabel", method = RequestMethod.PUT)
	@ResponseBody
	public String updateLabel(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body, @PathVariable("id") String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		List<Map<String, Object>> nodes = nodeService.select(param);
		if (nodes.size() == 1) {
			Map<String, Object> node = nodes.get(0);
			// TODO
//			node.put("labels", (String) body.get("labels"));
			node.put("labels", body.get("labels"));
			boolean flag = this.noticeMasterOpNode(node, "update");
			if (!flag) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("result", false);
				result.put("info", "node in use");
				return JSON.toJSONString(result);
			}
			param.clear();
			param.put("node_id", id);
			labelService.delete(param);
//			String labelsStr = (String) body.get("labels");
//			if (!"".equals(labelsStr)) {
//				String[] labels = labelsStr.split(",");
//				List<Map<String, Object>> labelL = new ArrayList<Map<String,Object>>();
//				for (String label : labels) {
//					String key = label.split("=")[0];
//					String value = label.split("=")[1];
//					String label_id = UUID.randomUUID().toString();
//					Map<String, Object> labelM = new HashMap<String, Object>();
//					labelM.put("id", label_id);
//					labelM.put("key", key);
//					labelM.put("value", value);
//					labelL.add(labelM);
//				}
//				param.put("labels", labelL);
//				labelService.multiInsert(param);
//			}
			// TODO
			List<Map<String, Object>> labellist = (List<Map<String,Object>>) body.get("labels");
			if (labellist.size() > 0) {
				Map<String, Object> labels = new HashMap<>();
				labels.put("node_id", id);
				labels.put("labels", labellist);
				labelService.multiInsert(labels);
			}
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", true);
			return JSON.toJSONString(result);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", false);
		result.put("info", "db error");
		return JSON.toJSONString(result);
//		param.clear();
//		param.put("node_id", id);
//		labelService.delete(param);
//		String labelsStr = (String) body.get("labels");
//		String[] labels = labelsStr.split(",");
//		List<Map<String, Object>> labelL = new ArrayList<Map<String,Object>>();
//		for (String label : labels) {
//			String key = label.split("=")[0];
//			String value = label.split("=")[1];
//			String label_id = UUID.randomUUID().toString();
//			Map<String, Object> labelM = new HashMap<String, Object>();
//			labelM.put("id", label_id);
//			labelM.put("key", key);
//			labelM.put("value", value);
//			labelL.add(labelM);
//		}
//		param.put("labels", labelL);
//		labelService.multiInsert(param);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("result", true);
//		return JSON.toJSONString(result);
	}
	
	private String deleteOne(String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> nodeOne = nodeService.one(id);
		boolean flag = this.noticeMasterOpNode(nodeOne, "remove");
		if (!flag) {
			result.put("result", false);
			result.put("info", "the node " + nodeOne.get("name") + " is in use");
			return JSON.toJSONString(result);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("node_id", id);
		labelService.delete(param);
		param.clear();
		param.put("id", id);
		log.debug("node_delete select param {}", param);
		List<Map<String, Object>> nodes = nodeService.select(param);
		log.debug("node_delete select result {}", nodes);
		int i = nodeService.delete(param);
		if (i == 1) {
			result.put("result", true); 
		} else {
			result.put("result", false);
			result.put("info", "db error");
		}
		
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
////			param.clear();
////			param.put("cluster_id", node.get("cluster_id"));
////			log.debug("node_delete select param {}", param);
////			List<Map<String, Object>> nodes_now = nodeService.select(param);
////			log.debug("node_delete select result {}", nodes_now);
////			if (nodes_now.size() > 1) {
////				ExecutorServicePool.getInstance().execute(new TunnelDeleteJob(node, true));
////				result.put("result", true);
////			} else {
////				param.clear();
////				param.put("id", id);
////				log.debug("node_delete delete param {}", param);
////				int i = nodeService.delete(param);
////				log.debug("node_delte delete result {}", i);
////				param.clear();
////				param.put("node_id", id);
////				bridgeService.delete(param);
////				if (i == 1) {
////					result.put("result", true);
////				} else {
////					result.put("result", false);
////					result.put("info", "db error");
////				}
////			}
//			result.put("result", true);
////			CountDownLatch cdl = new CountDownLatch(1);
////			node.put("CountDownLatch", cdl);
////			Map<String, Object> cluster = clusterService.one((String) node.get("cluster_id"));
////			INetwork network = NetworkFactory.getInstance("com.dc.appengine.adapter.network.Network" + cluster.get("network_kind"));
////			network.removeNode(node);
////			try {
////				cdl.await();
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//			ExecutorServicePool.getInstance().execute(new NoticeMasterUpdateNodeJob((String) node.get("ip"), "remove"));
//		} else {
//			result.put("result", false);
//			result.put("info", "db error");
//		}
		result.put("result", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "multiDelete", method = RequestMethod.DELETE)
	@ResponseBody
	public String multiDelete(HttpServletRequest request, HttpServletResponse response) {
		String idsStr = request.getParameter("ids");
		String[] ids = idsStr.split(",");
		String info = "";
		for (String id : ids) {
			String rStr = this.deleteOne(id);
			JSONObject map = JSON.parseObject(rStr);
			if (!(Boolean) map.get("result")) {
				info = info + "," + map.get("info");
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (info.length() > 0) {
			info = info.substring(1);
			result.put("result", false);
			result.put("info", info);
		} else {
			result.put("result", true);
		}
		return JSON.toJSONString(result);
	}
	
//	@DELETE
//	@Path("{id}")
//	public String delete(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body, @PathVariable("id") String id) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("node_id", id);
//		labelService.delete(param);
//		param.clear();
//		param.put("id", id);
//		log.debug("node_delete select param {}", param);
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		log.debug("node_delete select result {}", nodes);
//		Map<String, Object> result = new HashMap<String, Object>();
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
//			param.clear();
//			param.put("cluster_id", node.get("cluster_id"));
//			log.debug("node_delete select param {}", param);
//			List<Map<String, Object>> nodes_now = nodeService.select(param);
//			log.debug("node_delete select result {}", nodes_now);
//			if (nodes_now.size() > 1) {
//				ExecutorServicePool.getInstance().execute(new TunnelDeleteJob(node, true));
//				result.put("result", true);
//			} else {
//				param.clear();
//				param.put("id", id);
//				log.debug("node_delete delete param {}", param);
//				int i = nodeService.delete(param);
//				log.debug("node_delte delete result {}", i);
//				param.clear();
//				param.put("node_id", id);
//				bridgeService.delete(param);
//				if (i == 1) {
//					result.put("result", true);
//				} else {
//					result.put("result", false);
//					result.put("info", "db error");
//				}
//			}
//			ExecutorServicePool.getInstance().execute(new NoticeMasterUpdateNodeJob((String) node.get("ip"), "remove"));
//		} else {
//			result.put("result", false);
//			result.put("info", "db error");
//		}
//		return JSON.toJSONString(result);
//	}
	
//	@RequestMapping(value = "apply", method = RequestMethod.GET)
//	@ResponseBody
//	public String apply(HttpServletRequest request, HttpServletResponse response) {
//		String nodeIDsStr = request.getParameter("node_ids");
//		String[] nodeIDs = nodeIDsStr.split(",");
//		List<Map<String, Object>> returnNodes = new ArrayList<Map<String,Object>>();
//		for (String nodeID : nodeIDs) {
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("id", nodeID);
//			param.put("status_new", NodeStatus.applied);
//			param.put("status_old", NodeStatus.healthy);
//			int i = nodeService.updateByStatus(param);
//			if (i == 0) {
//				List<Map<String, Object>> nodes = nodeService.select(param);
//				if (nodes.size() == 1) {
//					Map<String, Object> node = nodes.get(0);
//					node.put("master_applied", true);
////					ExecutorServicePool.getInstance().execute(new NoticeNodeUpdateStatus((String) node.get("ip"), "master"));
//					returnNodes.add(node);
//				}
//			} else {
//				List<Map<String, Object>> nodes = nodeService.select(param);
//				if (nodes.size() == 1) {
//					Map<String, Object> node = nodes.get(0);
//					node.put("master_applied", false);
//					ExecutorServicePool.getInstance().execute(new NoticeNodeUpdateStatus((String) node.get("ip"), "master"));
//					returnNodes.add(node);
//				}
//			}
//		}
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("result", true);
//		result.put("nodes", returnNodes);
//		return JSON.toJSONString(result);
//	}
	
//	@RequestMapping(value = "restore", method = RequestMethod.PUT)
//	@ResponseBody
//	public String restore(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
//		String nodeIDsStr = (String) body.get("node_ids");
//		String[] nodeIDs = nodeIDsStr.split(",");
//		for (String nodeID : nodeIDs) {
//			Map<String, Object> param = new HashMap<String, Object>();
//			param.put("id", nodeID);
//			param.put("status", NodeStatus.healthy);
//			nodeService.update(param);
//			List<Map<String, Object>> nodes = nodeService.select(param);
//			if (nodes.size() == 1) {
//				Map<String, Object> node = nodes.get(0);
//				ExecutorServicePool.getInstance().execute(new NoticeNodeUpdateStatus((String) node.get("ip"), "adapter"));
//			}
//		}
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("result", true);
//		return JSON.toJSONString(result);
//	}
	
	@RequestMapping(value = "checkName", method = RequestMethod.GET)
	@ResponseBody
	public String checkName(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", request.getParameter("cluster_id"));
		param.put("name", request.getParameter("name"));
		param.put("id", request.getParameter("id"));
		int count = nodeService.checkName(param);
		Map<String, Object> result = new HashMap<String, Object>();
		if (count == 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "{id}/getLabel", method = RequestMethod.GET)
	@ResponseBody
	public String getLabel(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		Map<String, Object> param = new HashMap<>();
		param.put("node_id", id);
		return JSON.toJSONString(labelService.select(param));
	}
	
//	@RequestMapping(value = "status", method = RequestMethod.GET)
//	@ResponseBody
//	public String status(HttpServletRequest request, HttpServletResponse response) {
//		String ip = request.getParameter("ip");
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("ip", ip);
//		List<Map<String, Object>> nodes = nodeService.select(param);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("result", false);
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
//			String status = (String) node.get("status");
//			if (status.equals(NodeStatus.healthy)) {
//				result.put("result", true);
//			}
//		}
//		return JSON.toJSONString(result);
//	}

}
