package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.service.impl.ReleaseLifecycleService;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/releaselifecycle")
public class ReleaseLifecycleRestService {
	
	@Resource
	private ReleaseLifecycleService service;
	
	@RequestMapping(method = RequestMethod.POST, value = "lifecycle")
	public Object saveLifecycle(@RequestBody Map<String, Object> param, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		param.put("userId", user.getId());
		param.put("id", UUID.randomUUID().toString());
		int i = service.saveLifecycle(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "新增发布生命周期失败：数据库插入失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "lifecycle")
	public Object deleteLifecycle(@RequestParam("ids") String ids) {
		String[] idArray = ids.split(",");
		Map<String, Object> result = new HashMap<>();
		if (service.countRelease(idArray) > 0) {
			result.put("result", false);
			result.put("message", "删除发布生命周期失败：请先删除关联了该发布生命周期的发布");
			return result;
		}
		service.deleteStageByLifecycleIds(idArray);
		int i = service.deleteLifecycle(idArray);
//		Map<String, Object> result = new HashMap<>();
		if (i == idArray.length) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "删除发布生命周期失败：数据库删除失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "lifecycle")
	public Object updateLifecycle(@RequestBody Map<String, Object> param) {
		int i = service.updateLifecycle(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "更新发布生命周期失败：数据库更新失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "lifecycle")
	public Object lifecycles(@RequestParam(value = "name", defaultValue = "") String name, HttpServletRequest request) {
		Map<String, Object> param = new HashMap<>();
		param.put("name", name);
		User user = (User) request.getSession().getAttribute("user");
		param.put("userId", user.getId());
		return service.lifecycles(param);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "stage")
	public Object saveStage(@RequestBody Map<String, Object> param) {
		param.put("id", UUID.randomUUID().toString());
		int i = service.saveStage(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "保存发布阶段失败：数据库插入失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "stage")
	public Object deleteStage(@RequestParam("ids") String ids) {
		String[] idArray = ids.split(",");
		int i = service.deleteStage(idArray);
		Map<String, Object> result = new HashMap<>();
		if (i == idArray.length) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "删除发布阶段失败：数据库删除失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "stage")
	public Object updateStage(@RequestBody Map<String, Object> param) {
		int i = service.updateStage(param);
		Map<String, Object> result = new HashMap<>();
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "更新发布阶段失败：数据库更新失败");
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "stage")
	public Object stages(@RequestParam("lifecycleId") String lifecycleId) {
		return service.stages(lifecycleId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "sortstage")
	public Object sortStage(@RequestBody List<String> stageIds) {
		int i = service.sortStage(stageIds);
		Map<String, Object> result = new HashMap<>();
		if (i == stageIds.size()) {
			result.put("result", true);
		} else {
			result.put("result", false);
			result.put("message", "更新发布阶段顺序失败：数据库更新失败");
		}
		return result;
		
	}

}
