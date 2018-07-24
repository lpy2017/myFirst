package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.impl.ClusterService;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/cluster")
public class ClusterRestService {

	@Resource
	private ClusterService service;
	
	@Resource
	IAudit auditService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String insert(HttpServletRequest request, @RequestBody Map<String, Object> body) {
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> cluster = new HashMap<>();
		cluster.put("id", UUID.randomUUID().toString());
		cluster.put("name", body.get("name"));
		cluster.put("userId", user.getId());
//		cluster.put("userId", 1);
		int i = service.insert(cluster);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
			result.put("id", cluster.get("id"));
		} else {
			result.put("result", false);
			result.put("info", "数据库插入失败");
		}
		
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+body.get("name"), ResourceCode.Operation.ADD, i == 1?1:0, "新增环境:"+body.get("name")));
			}
		});
		//==============添加审计end=====================
		
		return JSON.toJSONString(result);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public String delete(HttpServletRequest request) {
		String clusterId = request.getParameter("clusterId");
		Map<String, Object> clusterMap = service.one(clusterId);
		int i = service.delete(clusterId);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "数据库删除失败");
		}
		
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				User user = (User) request.getSession().getAttribute("user");
				auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+clusterMap.get("name"), ResourceCode.Operation.DELETE, i == 1?1:0, "删除环境:"+clusterMap.get("name")));
			}
		});
		//==============添加审计end=====================
		
		return JSON.toJSONString(result);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(HttpServletRequest request,@RequestBody Map<String, Object> body) {
		Map<String, Object> formerEnv = service.one(""+body.get("id"));
		String formerEnvName = ""+formerEnv.get("name");
		int i = service.update(body);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("info", "数据库更新失败");
		}
		
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				User user = (User) request.getSession().getAttribute("user");
				auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+body.get("name"), ResourceCode.Operation.UPDATE, i == 1?1:0, formerEnvName + " 更新环境为:"+body.get("name")));
			}
		});
		//==============添加审计end=====================
		
		return JSON.toJSONString(result);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int pageSize = Integer.valueOf(request.getParameter("pageSize"));
		int pageNum = Integer.valueOf(request.getParameter("pageNum"));
		Map<String, Object> condition = new HashMap<>();
		condition.put("userId", user.getId());
//		condition.put("userId", 1);
		condition.put("name", request.getParameter("name"));
		condition.put("sortName", SortUtil.getColunmName("cluster",request.getParameter("sortName")));
		condition.put("sortOrder", request.getParameter("sortOrder"));
		Page page = service.list(condition, pageNum, pageSize);
		return JSON.toJSONString(page);
	}

	@RequestMapping(value = "listAll", method = RequestMethod.GET)
	public String listAll(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		String userId = user.getId().toString();
//		String userId = "1";
		return JSON.toJSONString(service.listAll(userId));
	}
	
	@RequestMapping(value = "test", method = RequestMethod.GET)
	public Object test() {
		String userId = "1";
//		return JSON.toJSONString(service.listAll(userId));
		return service.listAll(userId);
	}
	
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public String check(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		String userId = user.getId().toString();
//		String userId = "1";
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("name", request.getParameter("name"));
		int i = service.check(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "checkOther", method = RequestMethod.GET)
	public String checkOther(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		String userId = user.getId().toString();
//		String userId = "1";
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("name", request.getParameter("name"));
		param.put("id", request.getParameter("id"));
		int i = service.checkOther(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 0) {
			result.put("result", true);
		} else {
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String one(@PathVariable("id") String id) {
		return JSON.toJSONString(service.one(id));
	}

}
