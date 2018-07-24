package com.dc.appengine.appmaster.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.ApplicationReleaseStrategyDAO;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.quartz.application.AppJob;
import com.dc.appengine.appmaster.quartz.application.QuartzManager4App;

@Service("applicationReleaseStrategyService")
public class ApplicationReleaseStrategyService {
	
	@Autowired
	@Qualifier("applicationReleaseStrategyDAO")
	private ApplicationReleaseStrategyDAO dao;
	
	public Map<String, Object> save(Map<String, Object> param) {
		String id = UUID.randomUUID().toString();
		param.put("id", id);
		boolean flag = QuartzManager4App.addJob(AppJob.class, id, id, (String) param.get("cron"), param);
		Map<String, Object> result = new HashMap<>();
		result.put("result", flag);
		if (flag) {
			dao.save(param);
		} else {
			result.put("message", "增加定时任务失败导致添加策略失败");
		}
		return result;
	}
	
	public Map<String, Object> update(Map<String, Object> param) {
		String id = (String) param.get("id");
		boolean flag = QuartzManager4App.removeJob(id, id);
		Map<String, Object> result = new HashMap<>();
		result.put("result", flag);
		if (flag) {
			flag = QuartzManager4App.addJob(AppJob.class, id, id, (String) param.get("cron"), param);
			if (flag) {
				dao.update(param);
			} else {
				result.put("result", flag);
				result.put("message", "增加定时任务失败导致更新策略失败");
			}
		} else {
			result.put("message", "删除定时任务失败导致更新策略失败");
		}
		return result;
	}
	
	private int deleteById(String id) {
		String[] ids = new String[1];
		ids[0] = id;
		return dao.delete(ids);
	}
	
	public Map<String, Object> delete(String[] param) {
		boolean flag = true;
		for (String id : param) {
			if (QuartzManager4App.removeJob(id, id)) {
				deleteById(id);
			} else {
				flag = false;
				break;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("result", flag);
		if (!flag) {
			result.put("message", "删除定时任务失败导致更新策略失败");
		}
		return result;
	}
	
	public Page page(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.page(condition, pageNum, pageSize);
	}

}
