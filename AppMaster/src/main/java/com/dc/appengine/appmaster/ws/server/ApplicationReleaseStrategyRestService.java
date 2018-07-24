package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.service.impl.ApplicationReleaseStrategyService;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/ApplicationReleaseStrategy")
public class ApplicationReleaseStrategyRestService {
	
	@Resource
	private ApplicationReleaseStrategyService service;
	
	@RequestMapping(method = RequestMethod.POST)
	public Object save(HttpServletRequest request, @RequestBody Map<String, Object> body) {
		User user = (User) request.getSession().getAttribute("user");
		body.put("user_id", user.getId());
		return service.save(body);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public Object update(HttpServletRequest request, @RequestBody Map<String, Object> body) {
		User user = (User) request.getSession().getAttribute("user");
		body.put("user_id", user.getId());
		return service.update(body);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public Object delete(@RequestParam("ids") String idStr) {
		List<String> ids = JSON.parseObject(idStr, new TypeReference<List<String>>(){});
		return service.delete(ids.toArray(new String[0]));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public Object page(HttpServletRequest request,
			@RequestParam(value = "pageSize", defaultValue = "10") String pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") String pageNum,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "appName", defaultValue = "") String appName,
			@RequestParam(value = "taskStatus", defaultValue = "") String taskStatus,
			@RequestParam(value = "sortName", defaultValue = "name") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder) {
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> condition = new HashMap<>();
		condition.put("user_id", user.getId());
		condition.put("name", name);
		condition.put("app_name", appName);
		condition.put("task_status", taskStatus);
		condition.put("sortName", SortUtil.getColunmName("ApplicationReleaseStrategy", sortName));
		condition.put("sortOrder", sortOrder);
		return service.page(condition, Integer.valueOf(pageNum), Integer.valueOf(pageSize));
	}

}
