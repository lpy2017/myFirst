package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.impl.ReleaseLifecycleDAO;
import com.dc.appengine.appmaster.service.IReleaseManageService;

@Service("releaseLifecycleService")
public class ReleaseLifecycleService {
	
	@Autowired
	@Qualifier("releaseLifecycleDAO")
	private ReleaseLifecycleDAO dao;
	@Resource
	private IReleaseManageService releaseManageService;
	
	public int saveLifecycle(Map<String, Object> param) {
		return dao.saveLifecycle(param);
	}
	
	public int countRelease(String[] lifecycleIds) {
		return dao.countRelease(lifecycleIds);
	}
	
	public int deleteLifecycle(String[] ids) {
		return dao.deleteLifecycle(ids);
	}
	
	public int updateLifecycle(Map<String, Object> param) {
		return dao.updateLifecycle(param);
	}
	
	public List<Map<String, Object>> lifecycles(Map<String, Object> param) {
		return dao.lifecycles(param);
	}
	
	public int saveStage(Map<String, Object> param) {
		int count=dao.saveStage(param);
		if(count>0){
			String stageId=(String) param.get("id");
			String lifecycleId=(String) param.get("release_lifecycle_id");
			releaseManageService.addReleaseStage(stageId, lifecycleId);
		}
		return count;
	}
	
	public int deleteStage(String[] ids) {
		for (String id : ids) {
			releaseManageService.deleteReleaseStage(id);
		}
		if (ids.length > 0) {
			Map<String, Object> stage = dao.getStage(ids[0]);
			List<Map<String, Object>> stages = dao.stages(stage.get("release_lifecycle_id").toString());
			int i = dao.deleteStage(ids);
			if (stages.size() - ids.length > 0) {
				stages = sort(stages);
				List<String> idList = Arrays.asList(ids);
				List<String> idNew = new ArrayList<>();
				for (Map<String, Object> one : stages) {
					if (idList.contains(one.get("id"))) {
						continue;
					}
					idNew.add(one.get("id").toString());
				}
				sortStage(idNew);
			}
			return i;
		} else {
			return 0;
		}
	}
	
	public int deleteStageByLifecycleIds(String[] lifecycleIds) {
		return dao.deleteStageByLifecycleIds(lifecycleIds);
	}
	
	public int updateStage(Map<String, Object> param) {
		return dao.updateStage(param);
	}
	
	private List<Map<String, Object>> sort(List<Map<String, Object>> list) {
		if (list.isEmpty()) {
			return list;
		}
		Map<String, Map<String, Object>> dict = new HashMap<>();
		for (Map<String, Object> map : list) {
			if (map.containsKey("pre_stage_id")) {
				dict.put(map.get("pre_stage_id").toString(), map);
			} else {
				dict.put("first", map);
			}
		}
		List<Map<String, Object>> sorted = new ArrayList<>();
		sorted.add(dict.get("first"));
		while (sorted.size() < list.size()) {
			sorted.add(dict.get(sorted.get(sorted.size() - 1).get("id")));
		}
		return sorted;
	}
	
	public List<Map<String, Object>> stages(String lifecycleId) {
		return sort(dao.stages(lifecycleId));
	}
	
	public int sortStage(List<String> stageIds) {
		List<Map<String, Object>> param = new ArrayList<>();
		for (int i = 0; i < stageIds.size(); i++) {
			Map<String, Object> stage = new HashMap<>();
			stage.put("id", stageIds.get(i));
			if (i > 0) {
				stage.put("pre_stage_id", stageIds.get(i - 1));
			}
			param.add(stage);
		}
		return dao.sortStage(param);
	}

}
