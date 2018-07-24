package com.dc.appengine.adapter.rest;

import java.util.ArrayList;
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
import com.dc.appengine.adapter.entity.NodeStatus;
import com.dc.appengine.adapter.entity.Role;
import com.dc.appengine.adapter.service.ClusterService;
import com.dc.appengine.adapter.service.LabelService;
import com.dc.appengine.adapter.service.NodeService;
import com.dc.appengine.adapter.util.MD5Util;
import com.dc.appengine.adapter.util.WSClient;

@RestController
@EnableAutoConfiguration
@RequestMapping("cluster")
public class ClusterRestService {

	@Autowired
	private ClusterService clusterService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private LabelService labelService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
//	@RequestMapping(value = "network_kind", method = RequestMethod.GET)
//	@ResponseBody
//	public String networkKind(HttpServletRequest request, HttpServletResponse response) {
//		return JSON.toJSONString(ENV.NETWORK_KIND.split(","));
//	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public String create(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		Map<String, Object> param = new HashMap<String, Object>();
		String id = UUID.randomUUID().toString();
//		String network_type = (String) body.get("network_type");
//		if (network_type.equals("user_defined")) {
//			param.put("cluster_id", id);
//			param.put("cidr", body.get("network"));
//			clusterService.setCIDR(param);
//			param.clear();
//		}
//		String network_kind = (String) body.get("network_kind");
//		param.put("cluster_id", id);
//		param.put("network", network_kind);
//		clusterService.setNetwork(param);
//		param.clear();
		param.put("id", id);
		param.put("name", (String) body.get("name"));
		param.put("user_id", (String) body.get("user_id"));
		int i = clusterService.insert(param);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i == 1) {
			log.debug("cluster insert into db success {}", param);
			result.put("result", true);
			result.put("id", id);
			return JSON.toJSONString(result);
		} else {
			log.error("cluster insert into db fail {}", param);
			result.put("result", false);
			result.put("info", "cluster insert into db fail");
			return JSON.toJSONString(result);
		}
	}
	
//	@RequestMapping(method = RequestMethod.GET)
//	@ResponseBody
//	public String select(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		Map<String, Object> result = new HashMap<String, Object>();
//		List<Map<String, Object>> clusters = clusterService.select(param);
//		log.debug("clusters {}", clusters);
//		result.put("clusters", clusters);
//		return JSON.toJSONString(result);
//	}
	
	@RequestMapping(value = "master", method = RequestMethod.GET)
	@ResponseBody
	public String master(HttpServletRequest request, HttpServletResponse response) {
		long user_id = Long.valueOf(request.getParameter("user_id"));
		Map<String, Object> role = clusterService.getUserRole(user_id);
		if (role == null || Role.operator.equals(role.get("id"))) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_id", user_id);
			List<Map<String, Object>> clusters = clusterService.select(param);
			log.debug("clusters {}", clusters);
			return JSON.toJSONString(clusters);
		} else {
			if (Role.normal.equals(role.get("id"))) {
				List<Map<String, Object>> clusters = clusterService.getClusterByUser(user_id);
				log.debug("clusters {}", clusters);
				return JSON.toJSONString(clusters);
			}
			if (Role.admin.equals(role.get("id"))) {
				Map<String, Object> param = new HashMap<String, Object>();
				List<Map<String, Object>> clusters = clusterService.select(param);
				log.debug("clusters {}", clusters);
				return JSON.toJSONString(clusters);
			}
		}
		return null;
	}
	
	@RequestMapping(value = "ui", method = RequestMethod.GET)
	@ResponseBody
	public String ui(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");
		Map<String, Object> role = clusterService.getUserRole(Long.valueOf(user_id));
		if (Role.admin.equals(role.get("id")) || Role.observer.equals(role.get("id"))) {
			int page_num = Integer.valueOf(request.getParameter("pageNum"));
			int page_size = Integer.valueOf(request.getParameter("pageSize"));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_id", user_id);
			param.put("offset", (page_num - 1) * page_size);
			param.put("rows", page_size);
			List<Map<String, Object>> clusters = clusterService.uiAdmin(param);
			int total = clusterService.countAdmin(param);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", clusters);
			log.debug("clusters {}", result);
			return JSON.toJSONString(result);
		} else {
			int page_num = Integer.valueOf(request.getParameter("pageNum"));
			int page_size = Integer.valueOf(request.getParameter("pageSize"));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("user_id", user_id);
			param.put("offset", (page_num - 1) * page_size);
			param.put("rows", page_size);
			List<Map<String, Object>> clusters = clusterService.ui(param);
			int total = clusterService.count(param);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("rows", clusters);
			log.debug("clusters {}", result);
			return JSON.toJSONString(result);
		}
	}
	
	@RequestMapping(value = "clusters", method = RequestMethod.GET)
	@ResponseBody
	public String clusters(HttpServletRequest request, HttpServletResponse response) {
		String user_id = request.getParameter("user_id");
		Map<String, Object> role = clusterService.getUserRole(Long.valueOf(user_id));
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", user_id);
		if (Role.admin.equals(role.get("id")) || Role.observer.equals(role.get("id"))) {
			List<Map<String, Object>> clusters = clusterService.clustersAdmin(param);
			return JSON.toJSONString(clusters);
		} else {
			List<Map<String, Object>> clusters = clusterService.clusters(param);
			return JSON.toJSONString(clusters);
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public String one(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		return JSON.toJSONString(clusterService.one(id));
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.PUT)
	@ResponseBody
	public String update(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @RequestBody Map<String, Object> body) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);
		param.put("name", (String) body.get("name"));
		log.debug("cluster_update param {}", param);
		int i = clusterService.update(param);
		log.debug("cluster_update result {}", i);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i == 1) {
			result.put("result", true);
			return JSON.toJSONString(result);
		} else {
			result.put("result", false);
			return JSON.toJSONString(result);
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", id);
		List<Map<String, Object>> nodes = nodeService.select(param);
		if (nodes.size() > 0) {
			result.put("result", false);
			result.put("info", "the cluster is not empty");
			return JSON.toJSONString(result);
		}
		param.clear();
		param.put("id", id);
		log.debug("cluster_delete param {}", param);
		int i = clusterService.delete(param);
		log.debug("cluster_delte result {}", i);
		if (i == 1) {
			result.put("result", true);
			return JSON.toJSONString(result);
		} else {
			result.put("result", false);
			return JSON.toJSONString(result);
		}
	}
	
	@RequestMapping(value = "checkName", method = RequestMethod.GET)
	@ResponseBody
	public String checkName(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", request.getParameter("id"));
		param.put("name", request.getParameter("name"));
		param.put("user_id", Long.valueOf(request.getParameter("user_id")));
		int count = clusterService.checkName(param);
		Map<String, Object> result = new HashMap<String, Object>();
		if (count == 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
//	@RequestMapping(value = "otherCluster", method = RequestMethod.GET)
//	@ResponseBody
//	public String otherCluster(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("user_id", request.getParameter("user_id"));
//		param.put("id", request.getParameter("cluster_id"));
//		return JSON.toJSONString(clusterService.otherCluster(param));
//	}
	
	@RequestMapping(value = "{id}/users", method = RequestMethod.GET)
	@ResponseBody
	public String users(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		int page_num = Integer.valueOf(request.getParameter("pageNum"));
		int page_size = Integer.valueOf(request.getParameter("pageSize"));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", id);
		param.put("offset", (page_num - 1) * page_size);
		param.put("rows", page_size);
		List<Map<String, Object>> users = clusterService.clusterUsers(param);
		int total = clusterService.clusterUsersCount(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", users);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "{id}/checkuser", method = RequestMethod.POST)
	@ResponseBody
	public String checkUser(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		String user_name = (String) body.get("USERID");
		int i = clusterService.checkUser(user_name);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i == 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
//	private boolean addSVNUser(String username, String userpwd) {
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//		Map<String, Object> map = new HashMap<>();
//		map.put("userName", username);
//		map.put("password", userpwd);
//		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String,Object>>(map, headers);
//		String result = restTemplate.postForObject(ENV.SVN_URL + "/datacenter" + "/updateRegistryUser", entity, String.class);
//		return result.contains("ok");
//	}
	
	@RequestMapping(value = "{id}/adduser", method = RequestMethod.POST)
	@ResponseBody
	public String addUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @RequestBody Map<String, Object> body) {
		try {
			String user_name = (String) body.get("USERID");
			String user_pwd = (String) body.get("USERPASSWORD");
//			this.addSVNUser(user_name, user_pwd);
			user_pwd = MD5Util.md5(user_pwd);
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("USERID", user_name);
			param.put("USERPASSWORD", user_pwd);
			clusterService.addNewUser(param);
			param = clusterService.getUserByName(user_name);
			long user_id = (Long) param.get("ID");
//			param.clear();
//			param.put("user_id", user_id);
//			param.put("cpu", Integer.valueOf((String) body.get("cpu")));
//			param.put("memory", Integer.valueOf((String) body.get("memory")));
//			param.put("disk", Integer.valueOf((String) body.get("disk")));
//			clusterService.addUserResource(param);
			param.clear();
			param.put("user_id", user_id);
			param.put("role_id", Role.normal);
			clusterService.setUserRole(param);
			param = clusterService.one(id);
			long father_id = (Long) param.get("user_id");
			param.clear();
			param.put("user_id", user_id);
			param.put("father_id", father_id);
			clusterService.addUserRelationship(param);
			param.clear();
			param.put("cluster_id", id);
			param.put("user_id", user_id);
			clusterService.addUser(param);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", true);
			result.put("user_id", user_id);
			return JSON.toJSONString(result);
		} catch (Exception e) {
			log.error("", e);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("result", true);
			result.put("info", "db error");
			return JSON.toJSONString(result);
		}
	}
	
//	@RequestMapping(value = "{id}/updateuser", method = RequestMethod.PUT)
//	@ResponseBody
//	public String updateUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @RequestBody Map<String, Object> body) {
//		Map<String, Object> userResource = clusterService.getUserResource(Long.valueOf((String) body.get("ID")));
//		int cpu = Integer.valueOf((String) body.get("cpu"));
//		int memory = Integer.valueOf((String) body.get("memory"));
//		int disk = Integer.valueOf((String) body.get("disk"));
//		int cpu_used = (Integer) userResource.get("cpu_used");
//		int memory_used = (Integer) userResource.get("memory_used");
//		int disk_used = (Integer) userResource.get("disk_used");
//		if (cpu < cpu_used || memory < memory_used || disk < disk_used) {
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("result", false);
//			result.put("info", "illegal");
//			return JSON.toJSONString(result);
//		}
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("user_id", Long.valueOf((String) body.get("ID")));
//		param.put("cpu", Integer.valueOf((String) body.get("cpu")));
//		param.put("memory", Integer.valueOf((String) body.get("memory")));
//		param.put("disk", Integer.valueOf((String) body.get("disk")));
//		clusterService.updateUserResource(param);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("result", true);
//		return JSON.toJSONString(result);
//	}
	
	@RequestMapping(value = "{id}/userlist", method = RequestMethod.GET)
	@ResponseBody
	public String userList(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		Map<String, Object> param = new HashMap<String, Object>();
		Map<String, Object> cluster = clusterService.one(id);
		param.put("user_id", cluster.get("user_id"));
		param.put("cluster_id", id);
		List<Map<String, Object>> users = clusterService.getOldUser(param);
		return JSON.toJSONString(users);
	}
	
	@RequestMapping(value = "{id}/selectuser", method = RequestMethod.POST)
	@ResponseBody
	public String selectUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id, @RequestBody Map<String, Object> body) {
		String[] user_ids = ((String) body.get("user_ids")).split(",");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", id);
		param.put("user_ids", user_ids);
		clusterService.addOldUser(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "{id}/deleteuser", method = RequestMethod.DELETE)
	@ResponseBody
	public String deleteUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) {
		String[] user_ids = request.getParameter("user_ids").split(",");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("cluster_id", id);
		param.put("user_ids", user_ids);
		clusterService.deleteUser(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "addClusterAndNodes", method = RequestMethod.POST)
	@ResponseBody
	public String addClusterAndNodes(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		Map<String, Object> cluster = (Map<String, Object>) body.get("cluster");
		Map<String, Object> label = (Map<String, Object>) body.get("label");
		List<Map<String, Object>> labels = new ArrayList<>();
		labels.add(label);
		List<Map<String, Object>> nodes = (List<Map<String, Object>>) body.get("cluster");
		String clusterId = UUID.randomUUID().toString();
		for (Map<String, Object> node : nodes) {
			node.put("id", UUID.randomUUID().toString());
			node.put("status", NodeStatus.nonregistered);
			Map<String, Object> nodeMap = new HashMap<>();
			nodeMap.put("id", node.get("id"));
			nodeMap.put("ip", node.get("ip"));
			nodeMap.put("name", node.get("name"));
			nodeMap.put("cluster_id", clusterId);
			nodeMap.put("labels", JSON.toJSONString(labels));
			nodeMap.put("cpu", 0);
			nodeMap.put("memory", 0);
			nodeMap.put("disk", 0);
			WebTarget target = WSClient.getMasterWebTarget();
			Form form = new Form();
			form.param("node_info", JSON.toJSONString(nodeMap));
			target.path("node").path("addnode").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		}
		cluster.put("id", clusterId);
		cluster.put("name", cluster.get("clusterName"));
		clusterService.insert(cluster);
		Map<String, Object> nodeParam = new HashMap<>();
		nodeParam.put("clusterId", clusterId);
		nodeService.multiAdd(nodeParam);
		label.put("nodes", nodes);
		labelService.multiAdd(label);
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("cluster_id", clusterId);
		return JSON.toJSONString(result);
	}

}
