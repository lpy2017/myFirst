package com.dc.appengine.adapter.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.dc.appengine.adapter.service.LabelService;
import com.dc.appengine.adapter.util.WSClient;

@RestController
@EnableAutoConfiguration
@RequestMapping("label")
public class LabelRestService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private LabelService service;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response) {
		int page_num = Integer.valueOf(request.getParameter("pageNum"));
		int page_size = Integer.valueOf(request.getParameter("pageSize"));
		String user_id = request.getParameter("user_id");
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", user_id);
		param.put("offset", (page_num - 1) * page_size);
		param.put("rows", page_size);
		List<Map<String, Object>> labels = service.findByUser(param);
		int total = service.findByUserCount(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", labels);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public String delete(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		Map<String, Object> param = new HashMap<>();
		param.put("key", body.get("label_key"));
		param.put("value", body.get("label_value"));
		param.put("type", body.get("label_type"));
		param.put("cluster_id", body.get("cluster_id"));
		List<Map<String, Object>> nodes = service.findNodeByClusterAndLabel(param);
		if (nodes.size() > 0) {
			param.put("nodes", nodes);
			WebTarget target = WSClient.getMasterWebTarget();
			Form form = new Form();
			form.param("labels", JSON.toJSONString(param));
			String master_result = target.path("node").path("removeLabels").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
			JSONObject jobj = JSON.parseObject(master_result);
			boolean flag = (Boolean) jobj.get("result");
			if (!flag) {
				Map<String, Object> result = new HashMap<>();
				result.put("result", false);
				result.put("info", "master delete label fail");
				return JSON.toJSONString(result);
			}
			service.multiDelete(param);
		}
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "nodes", method = RequestMethod.GET)
	@ResponseBody
	public String nodes(HttpServletRequest request, HttpServletResponse response) {
		int page_num = Integer.valueOf(request.getParameter("pageNum"));
		int page_size = Integer.valueOf(request.getParameter("pageSize"));
		String user_id = request.getParameter("user_id");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		Map<String, Object> param = new HashMap<>();
		param.put("user_id", user_id);
		param.put("offset", (page_num - 1) * page_size);
		param.put("rows", page_size);
		param.put("key", key);
		param.put("value", value);
		param.put("type", type);
		List<Map<String, Object>> nodes = service.findNodeByLabel(param);
		int total = service.findNodeByLabelCount(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", nodes);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "{cluster_id}/nodes", method = RequestMethod.GET)
	@ResponseBody
	public String clusterNodes(HttpServletRequest request, HttpServletResponse response, @PathVariable("cluster_id") String cluster_id) {
		int page_num = Integer.valueOf(request.getParameter("pageNum"));
		int page_size = Integer.valueOf(request.getParameter("pageSize"));
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		Map<String, Object> param = new HashMap<>();
		param.put("cluster_id", cluster_id);
		param.put("offset", (page_num - 1) * page_size);
		param.put("rows", page_size);
		param.put("key", key);
		param.put("value", value);
		param.put("type", type);
		List<Map<String, Object>> nodes = service.findNodeByCluster(param);
		int total = service.findNodeByClusterCount(param);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", total);
		result.put("rows", nodes);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "multiAdd", method = RequestMethod.POST)
	@ResponseBody
	public String multiAdd(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> body) {
		int i = service.multiAdd(body);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i > 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "db error");
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "multiDelete", method = RequestMethod.DELETE)
	@ResponseBody
	public String multiDelete(HttpServletRequest request, HttpServletResponse response) {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		String nodejson = request.getParameter("nodes");
		JSONArray nodes = JSON.parseArray(nodejson);
		Map<String, Object> param = new HashMap<>();
		param.put("key", key);
		param.put("value", value);
		param.put("type", type);
		param.put("nodes", nodes);
		int i = service.multiDelete(param);
		Map<String, Object> result = new HashMap<String, Object>();
		if (i > 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "db error");
		}
		return JSON.toJSONString(result);
	}
	

	
	@RequestMapping(value = "{id}/setlabel", method = RequestMethod.PUT)
	@ResponseBody
	public String setlabel(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String cluster_id, @RequestBody Map<String, Object> body) {
		String tier = (String) body.get("label");
		String label_type = (String) body.get("label_type");
		List<Map<String, Object>> nodes_new = (List<Map<String,Object>>) body.get("nodes");
		if (tier.contains("=")) {
			String key = tier.split("=")[0];
			String value = tier.split("=")[1];
			Map<String, Object> param = new HashMap<>();
			param.put("cluster_id", cluster_id);
			param.put("key", key);
			param.put("value", value);
			param.put("type", label_type);
			List<Map<String, Object>> nodes = service.findNodeByClusterAndLabel(param);
			if (nodes.size() > 0) {
				param.put("nodes", nodes);
				WebTarget target = WSClient.getMasterWebTarget();
				Form form = new Form();
				form.param("labels", JSON.toJSONString(param));
				String master_result = target.path("node").path("removeLabels").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
				JSONObject jobj = JSON.parseObject(master_result);
				boolean flag = (Boolean) jobj.get("result");
				if (!flag) {
					Map<String, Object> result = new HashMap<>();
					result.put("result", false);
					result.put("info", "master delete label fail");
					return JSON.toJSONString(result);
				}
				service.multiDelete(param);
			}
			param.put("nodes", nodes_new);
			WebTarget target = WSClient.getMasterWebTarget();
			Form form = new Form();
			form.param("labels", JSON.toJSONString(param));
			String master_result = target.path("node").path("addLabels").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
			JSONObject jobj = JSON.parseObject(master_result);
			boolean flag = (Boolean) jobj.get("result");
			if (!flag) {
				Map<String, Object> result = new HashMap<>();
				result.put("result", false);
				result.put("info", "master add label fail");
				return JSON.toJSONString(result);
			}
			service.multiAdd(param);
			Map<String, Object> result = new HashMap<>();
			result.put("result", true);
			return JSON.toJSONString(result);
		} else {
			Map<String, Object> result = new HashMap<>();
			result.put("result", false);
			result.put("info", "illegal label");
			return JSON.toJSONString(result);
		}
	}

}
