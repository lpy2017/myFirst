package com.dc.appengine.appmaster.quartz.application;

import static org.quartz.JobBuilder.newJob;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.quartz.StdSchedulerFactory;
import com.dc.appengine.appmaster.service.IReleaseTaskService;
import com.dc.appengine.appmaster.service.impl.QuartzTaskService;

@Service
public class QuartzManager4App {
	
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static final String JOB_GROUP_NAME = "CD_JOBGROUP_NAME";
	private static final String TRIGGER_GROUP_NAME = "CD_TRIGGERGROUP_NAME";
	private static final Logger log = LoggerFactory.getLogger(QuartzManager4App.class);
	private static QuartzTaskService taskService;
	private static IReleaseTaskService releaseTaskService;
	
	@Autowired
	public void setQuartzTaskService(QuartzTaskService service) {
		QuartzManager4App.taskService = service;
	}
	
	@Autowired
	public void setQuartzTaskService(IReleaseTaskService service) {
		QuartzManager4App.releaseTaskService = service;
	}
	
	public static void start() {
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}
	
	public static void shutdown() {
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			sched.shutdown();
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}
	
	public static boolean addJob(Class<? extends Job> jobClass, String jobName, String triggerName, String cronExpression,
			Map<String, Object> jobData) {
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			JobDetail job = newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
			job.getJobDataMap().putAll(jobData);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式构建一个新的trigger
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
					.withSchedule(scheduleBuilder).build();
			sched.scheduleJob(job, trigger);
			return true;
		} catch (SchedulerException e) {
			log.error("", e);
			return false;
		}
	}
	
	public static boolean removeJob(String jobName, String triggerName) {
		TriggerKey triggerKey = TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME);
		JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			if (sched.checkExists(triggerKey)) {
				sched.unscheduleJob(triggerKey);// 移除触发器
			}
			if (sched.checkExists(jobKey)) {
				sched.deleteJob(jobKey);// 删除任务
			}
			return true;
			// sched.pauseTrigger(triggerKey);// 停止触发器
			// sched.unscheduleJob(triggerKey);// 移除触发器
			// sched.deleteJob(jobKey);// 删除任务
		} catch (SchedulerException e) {
			log.error("", e);
			return false;
		}
	}
	
	public static boolean checkCronExpression(String cronExpression) {
		boolean flag = true;
		try {
			new CronExpression(cronExpression);
		} catch (ParseException e) {
			log.error("", e);
			flag = false;
		}
		return flag;
	}
	
	public static void releaseTasks(Map<String, Object> param) {
		List<Map<String, Object>> tasks = taskService.getTaskByAppAndStatus(param);
//		Map<String, Object> map = new HashMap<>();
//		for (Map<String, Object> task : tasks) {
//			if ((Integer) task.get("PARENT_ID") == -1) {
//				List<String> list = new ArrayList<>();
//				map.put(task.get("ID").toString(), list);
//			} else {
//				if (map.containsKey(task.get("PARENT_ID").toString())) {
//					List<String> list = (List<String>) map.get(task.get("PARENT_ID").toString());
//					list.add(task.get("ID").toString());
//				} else {
//					List<String> list = new ArrayList<>();
//					list.add(task.get("ID").toString());
//					map.put(task.get("PARENT_ID").toString(), list);
//				}
//			}
//		}
		List<String> list = new ArrayList<>();
		for (Map<String, Object> task : tasks) {
			list.add(task.get("ID").toString());
		}
		log.info("tasks:{}", JSON.toJSONString(list));
		releaseTaskService.executeCRRealeaseTasksByBatch(JSON.toJSONString(list));
	}

}
