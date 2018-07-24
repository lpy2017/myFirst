package com.dc.appengine.quartz.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.quartz.entity.Page;

@Component("quartzDao")
public class QuartzDao extends PageQuery{
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	public void saveQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("quartz.insertQuartz", param);
	}

	public void updateQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.update("quartz.updateQuartzInfo", param);
	}

	public List<String> getJobNameByQId(String quartzId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("quartz.listJonsByQuatzId", quartzId);
	}

	public void deleteQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		String quartzId = param.get("quartzId").toString();
		sqlSessionTemplate.delete("quartz.deleteQuartzById", quartzId);
	}

	public int countQuartzNum(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("quartz.countQuartzByUser", param);
	}

	public Page getQuartzListByPage(Map<String, Object> condition, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return pageQuery(condition, pageNum, pageSize, "quartz.listQuartzs");
	}

	public void insertQuartzJob(String quartzId, String jobName) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("quartzId", quartzId);
		param.put("jobName", jobName);
		param.put("jobStatus", "RUNNING");
		sqlSessionTemplate.insert("quartz.insertQuartzJob",param);
	}

	public String getCronById(String quartzId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("quartz.selectCronById", quartzId);
	}

	public Map<String,Object> getQuartz(String quartzId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("quartz.getQuartz",quartzId);
	}

	public void updateJobStatus(String jobName, String jobStatus) {
		// TODO Auto-generated method stub
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("jobStatus", jobStatus);
		param.put("jobName", jobName);
		sqlSessionTemplate.update("quartz.updateJobStatus",param);
	}
	public Page getJobsOfQuartz(Map<String, Object> condition, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return pageQuery(condition, pageNum, pageSize, "quartz.getJobsOfQuartz");
	}

	public void deleteRuartzJobs(String quartzId) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("quartz.deleteQuartzJobs", quartzId);
	}

	public void deleteQuartzJobByJobName(String jobName) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("quartz.deleteQuartzJobByJob", jobName);
	}

	public int countJobFromQT(String job) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("quartz.countJobFromQT", job);
	}
	
	
}
