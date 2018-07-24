package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.service.impl.NodeLabelService;

@RestController
@RequestMapping("/ws/label")
public class LabelRestService {
	
	@Resource
	private NodeLabelService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public String insert(@RequestBody Map<String, Object> body) {
		int i = service.insert(body);
		Map<String, Object> result = new HashMap<>();
		if (i > 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "数据库插入失败");
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		param.put("clusterId", request.getParameter("clusterId"));
		param.put("key", request.getParameter("key"));
		param.put("value", request.getParameter("value"));
		param.put("type", request.getParameter("type"));
		int i = service.delete(param);
		Map<String, Object> result = new HashMap<>();
		if (i > 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "数据库删除失败");
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public String update(@RequestBody Map<String, Object> body) {
		service.update(body);
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public String check(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		param.put("clusterId", request.getParameter("clusterId"));
		param.put("key", request.getParameter("key"));
		param.put("value", request.getParameter("value"));
		param.put("type", request.getParameter("type"));
		int i = service.check(param);
		Map<String, Object> result = new HashMap<>();
		if (i > 0) {
			result.put("result", false);
		} else {
			result.put("result", true);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "nodes", method = RequestMethod.GET)
	public String nodes(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		param.put("clusterId", request.getParameter("clusterId"));
		param.put("key", request.getParameter("key"));
		param.put("value", request.getParameter("value"));
		param.put("type", request.getParameter("type"));
		return JSON.toJSONString(service.nodes(param));
	}

}
