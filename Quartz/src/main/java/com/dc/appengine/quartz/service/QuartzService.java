package com.dc.appengine.quartz.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.quartz.dao.QuartzDao;
import com.dc.appengine.quartz.entity.Page;
import com.dc.appengine.quartz.manager.QuartzJobFactory;
import com.dc.appengine.quartz.manager.QuartzManager;
import com.dc.appengine.quartz.manager.ScheduleJob;
import com.dc.appengine.quartz.utils.MessageHelper;
import com.dc.appengine.quartz.utils.SortUtil;


@Service("quartzService")
public class QuartzService {
	@Autowired
	@Qualifier("quartzDao")
	private QuartzDao quartzDao;

	public void saveQuartz(Map<String, String> param) {
		// TODO Auto-generated method stub
		quartzDao.saveQuartzInfo(param);
	}
	
	public String getCron(java.util.Date  date){  
	    String dateFormat="ss mm HH dd MM ? yyyy";  
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        String formatTimeStr = null;  
        if (date != null) {  
            formatTimeStr = sdf.format(date);  
        }  
        return formatTimeStr;
	}
	
	public static void main(String[] args){
		//44 50 09 25 07 ? 2017
		//System.out.println(getCron(new Date()));
		
	}

	public void updateQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		//找到该quartzId关联的所有job
		List<String> jobList = quartzDao.getJobNameByQId(param.get("quartzId").toString());
		String cron = param.get("quartzCron").toString();
		if(jobList != null && jobList.size() > 0){
			for(String job : jobList){
				//当已有job执行完被清理情况，从quartaz表里看是否存在，不存在删除业务表里的job
				int count = quartzDao.countJobFromQT(job);
				if(count != 1){
					quartzDao.deleteRuartzJobs(param.get("quartzId").toString());
				}
				//更新job任务
				String result = QuartzManager.modifyJobTime(job, cron);
				if(!result.contains("true")){
					
					return;
				}
				updateJobStatus(job, "RUNNING");
			}
		}
		quartzDao.updateQuartzInfo(param);
	}

	@Transactional
	public void stopQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		quartzDao.updateQuartzInfo(param);
		//找到该quartzId关联的所有job
		List<String> jobList = quartzDao.getJobNameByQId(param.get("quartzId").toString());
		if(jobList != null && jobList.size() > 0){
			for(String job : jobList){
				//更新job任务
				QuartzManager.pauseJob(job);
				updateJobStatus(job, "PAUSED");
			}
		}
	}

	@Transactional
	public void deleteQuartzInfo(Map<String, String> param) {
		// TODO Auto-generated method stub
		//找到该quartzId关联的所有job
		List<String> jobList = quartzDao.getJobNameByQId(param.get("quartzId").toString());
		if(jobList != null && jobList.size() > 0){
			for(String job : jobList){
				//删除job任务
				QuartzManager.removeJob(job);
			}
		}
		quartzDao.deleteQuartzInfo(param);
		quartzDao.deleteRuartzJobs(param.get("quartzId").toString());
	}

	public Page getQuartzListByPage(Map<String, Object> condition, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		return quartzDao.getQuartzListByPage(condition, pageNum, pageSize);
	}

	public int countQuartzNum(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return quartzDao.countQuartzNum(param);
	}
	
	@Transactional
	public String createQuartzJob(String quartzId, String flowId,
			String instanceId ,String flowName,String instanceName) {
		// TODO Auto-generated method stub
		String jobName = instanceName + "_" + flowName + "_" + System.currentTimeMillis();
		
		if(flowId != null && !flowId.equals("")){
			//生成job任务,判断什么流程，调用不同的执行类
			String cron = quartzDao.getCronById(quartzId);
			ScheduleJob job = new ScheduleJob();
			job.setCreTime(new Date()+"");
			job.setJobCron(cron);
			job.setJobDesc("定时执行"+flowName+"流程");
			job.setJobId(UUID.randomUUID().toString());
			job.setJobName(jobName);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("flowName", flowName);
			map.put("flowId", flowId);
			map.put("instanceId", instanceId);
			job.setExcuteInfo(map);
			String result = QuartzManager.addJob(jobName, QuartzJobFactory.class, cron, job);
			//存关联表中
			if(!result.contains("true")){
				return result;
			}
			quartzDao.insertQuartzJob(quartzId,jobName);
			return MessageHelper.wrap("result",true,"message","创建job成功");
		}else{
			return MessageHelper.wrap("result",false,"message","创建job失败");
		}
	}

	public String getQuartz(String quartzId) {
		// TODO Auto-generated method stub
		return JSON.toJSONString(quartzDao.getQuartz(quartzId));
	}

	public void resumeQuartzInJob(Map<String, String> param) {
		// TODO Auto-generated method stub
		//quartz_info表里的status字段暂不用
		//quartzDao.updateQuartzInfo(param);
		//找到该quartzId关联的所有job
		List<String> jobList = quartzDao.getJobNameByQId(param.get("quartzId").toString());
		if(jobList != null && jobList.size() > 0){
			for(String job : jobList){
				//更新job任务
				QuartzManager.startJob(job);
				updateJobStatus(job, "RUNNING");
			}
		}
	}
	//更新job的状态
	public void updateJobStatus(String jobName,String jobStatus){
		quartzDao.updateJobStatus(jobName,jobStatus);
	}
	//获取quartzinfo的jobs列表
	public String getJobsOfQuartz(int pageNum,int pageSize,String quartzId,String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		List<String> jobList = quartzDao.getJobNameByQId(quartzId);
		int jobNum = 0;
		if(jobList != null ){
			jobNum = jobList.size();
		}
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("quartzId", quartzId);
		condition.put("sortName", SortUtil.getColunmName("job", sortName));
		condition.put("sortOrder", sortOrder);
		Page page = quartzDao.getJobsOfQuartz(condition, pageNum, pageSize);
		return JSON.toJSONString(page);
	}

	//操作单个job
	public void OperateQuartzJobOne(String jobName, String op) {
		// TODO Auto-generated method stub
		if("resume".equals(op) || "RESUME".equals(op)){
			//更新job任务
			QuartzManager.startJob(jobName);
			updateJobStatus(jobName, "RUNNING");
		}else if("stop".equals(op) || "STOP".equals(op)){
			//更新job任务
			QuartzManager.pauseJob(jobName);
			updateJobStatus(jobName, "PAUSED");
		}else if("delete".equals(op) || "DELETE".equals(op)){
			QuartzManager.removeJob(jobName);
			quartzDao.deleteQuartzJobByJobName(jobName);
		}
	}
}
