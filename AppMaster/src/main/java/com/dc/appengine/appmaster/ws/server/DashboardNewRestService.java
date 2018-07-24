package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.constants.Constants.Monitor;
import com.dc.appengine.appmaster.service.IBlueprintTemplateService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.service.impl.DashboardService;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/dashboardnew")
public class DashboardNewRestService {
	
	private static final Logger log = LoggerFactory.getLogger(DashboardNewRestService.class);
	
	@Resource
	private DashboardService service;
	
	@Resource
	private IUserService userService;
	
	@Resource
	private IBlueprintTemplateService blueprintTemplateService;
	
	@RequestMapping(value = "envNum", method = RequestMethod.GET)
	public Object envNum(HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			return service.getClusterNum(user.getId());
		} catch (Exception e) {
			log.error("", e);
		}
		return 0;
	}
	
	@RequestMapping(value = "blueprintNum", method = RequestMethod.GET)
	public Object blueprintNum(HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String result = userService.getSonsOfUser(user.getId());
			Map<Long, String> users = JSON.parseObject(result, new TypeReference<Map<Long, String>>(){});
			Long[] userIds = users.keySet().toArray(new Long[0]);
			return service.getBlueprintNum(userIds);
		} catch (Exception e) {
			log.error("", e);
		}
		return 0;
	}
	
	@RequestMapping(value = "blueprintInstanceNum", method = RequestMethod.GET)
	public Object blueprintInstanceNum(HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String result = userService.getSonsOfUser(user.getId());
			Map<Long, String> users = JSON.parseObject(result, new TypeReference<Map<Long, String>>(){});
			Long[] userIds = users.keySet().toArray(new Long[0]);
			return service.getBlueprintInstanceNum(userIds);
		} catch (Exception e) {
			log.error("", e);
		}
		return 0;
	}
	
	@RequestMapping(value = "componentNum", method = RequestMethod.GET)
	public Object componentNum() {
		try {
			return service.getComponentNum();
		} catch (Exception e) {
			log.error("", e);
		}
		return 0;
	}
	
	@RequestMapping(value = "componentInCluster", method = RequestMethod.GET)
	public Object getApplicationByCluster(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "clusterId", defaultValue = "") String clusterId,
			@RequestParam(value = "componentName", defaultValue = "") String componentName,
			@RequestParam(value = "nodeName", defaultValue = "") String nodeName,
			@RequestParam(value = "instanceName", defaultValue = "") String instanceName,
			@RequestParam(value = "hostname", defaultValue = "") String hostname,
			@RequestParam(value = "sortName", defaultValue = "NODE_NAME") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> condition = new HashMap<>();
			condition.put("userId", user.getId());
			condition.put("clusterId", clusterId);
			condition.put("componentName", componentName);
			condition.put("nodeName", nodeName);
			condition.put("instanceName", instanceName);
			condition.put("hostname", hostname);
			condition.put("pageSize", pageSize);
			condition.put("pageNum", pageNum);
			condition.put("sortName", SortUtil.getColunmName("application", sortName));
			condition.put("sortOrder", sortOrder);
			return service.getApplicationByCluster(condition, pageNum, pageSize);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "blueprintInstanceInCluster", method = RequestMethod.GET)
	public Object getBlueprintInstanceByCluster(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "clusterId", defaultValue = "") String clusterId,
			@RequestParam(value = "instanceName", defaultValue = "") String instanceName,
			@RequestParam(value = "blueprintName", defaultValue = "") String blueprintName,
			@RequestParam(value = "sortName", defaultValue = "INSTANCE_NAME") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> condition = new HashMap<>();
			condition.put("userId", user.getId());
			condition.put("clusterId", clusterId);
			condition.put("instanceName", instanceName);
			condition.put("blueprintName", blueprintName);
			condition.put("pageSize", pageSize);
			condition.put("pageNum", pageNum);
			condition.put("sortName", SortUtil.getColunmName("bluePrintIns", sortName));
			condition.put("sortOrder", sortOrder);
			return service.getBlueprintInstanceByCluster(condition, pageNum, pageSize);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "getActivity", method = RequestMethod.GET)
	public Object getActivity(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "sortName", defaultValue = "startTime") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "DESC") String sortOrder) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> condition = new HashMap<>();
			condition.put("userId", user.getId());
			condition.put("pageSize", pageSize);
			condition.put("pageNum", pageNum);
			condition.put("sortName", SortUtil.getColunmName("flowInstance", sortName));
			condition.put("sortOrder", sortOrder);
			List<Map<String, Object>> list = service.getFlowInfo(Monitor.RUNNING + "");
			Map<String, Object> map = new HashMap<>();
			for (Map<String, Object> one : list) {
				map.put(one.get("id").toString(), one);
			}
			Set<String> keys = map.keySet();
			condition.put("flowInstanceIds", keys.toArray(new String[0]));
			return service.getFlowInstanceByIds(condition, pageNum, pageSize);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "getFlowInfo", method = RequestMethod.GET)
	public Object getFlowInfo(@RequestParam(value = "status", defaultValue = "正在执行")String status) {
		return service.getFlowInfo(status);
	}

}
