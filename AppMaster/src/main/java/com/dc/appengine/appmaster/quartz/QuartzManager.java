package com.dc.appengine.appmaster.quartz;

import static org.quartz.JobBuilder.newJob;

import java.text.ParseException;
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
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.impl.QuartzTaskService;

@Service
public class QuartzManager {

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static final String JOB_GROUP_NAME = "CD_JOBGROUP_NAME";
	private static final String TRIGGER_GROUP_NAME = "CD_TRIGGERGROUP_NAME";
	private static final Logger log = LoggerFactory.getLogger(QuartzManager.class);
	private static QuartzTaskService taskService;
	private static IBlueprintService blueprintService;

	@Autowired
	public void setQuartzTaskService(QuartzTaskService service) {
		QuartzManager.taskService = service;
	}

	@Autowired
	public void setBlueprintService(IBlueprintService service) {
		QuartzManager.blueprintService = service;
	}

	public static Map<String, Object> testService() {
		return taskService.getCronExpression(1);
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

	public static void addTaskJob(long taskId) {
		Map<String, Object> task = taskService.getCronExpression(taskId);
		if ((Boolean) task.get("AUTO_EXECUTE")) {
			String cronExpression = taskService.getCronExpression(taskId).get("CRONEXPRESSION").toString();
			addJob(TaskJob.class, taskId + "", taskId + "", cronExpression, taskId);
		}
	}

	public static void updateTaskJob(long taskId) {
		removeTaskJob(taskId);
		addTaskJob(taskId);
	}

	public static void removeTaskJob(long taskId) {
		removeJob(taskId + "", taskId + "");
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

	public static void addJob(Class<? extends Job> jobClass, String jobName, String triggerName, String cronExpression,
			long taskId) {
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			JobDetail job = newJob(jobClass).withIdentity(jobName, JOB_GROUP_NAME).build();
			job.getJobDataMap().put("taskId", taskId);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 按新的cronExpression表达式构建一个新的trigger
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
					.withSchedule(scheduleBuilder).build();
			sched.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	// public static void updateJob(String triggerName, String cronExpression) {
	// TriggerKey triggerKey = TriggerKey.triggerKey(triggerName,
	// TRIGGER_GROUP_NAME);
	// Scheduler sched;
	// try {
	// sched = gSchedulerFactory.getScheduler();
	// CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
	// CronScheduleBuilder scheduleBuilder =
	// CronScheduleBuilder.cronSchedule(cronExpression);
	// trigger =
	// trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
	// // 按新的trigger重新设置job执行
	// sched.rescheduleJob(triggerKey, trigger);
	// } catch (SchedulerException e) {
	// log.error("", e);
	// }
	// }

	public static void removeJob(String jobName, String triggerName) {
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
			// sched.pauseTrigger(triggerKey);// 停止触发器
			// sched.unscheduleJob(triggerKey);// 移除触发器
			// sched.deleteJob(jobKey);// 删除任务
		} catch (SchedulerException e) {
			log.error("", e);
		}
	}

	public static void triggerFlow(long taskId) {
		Map<String, Object> task = taskService.getCronExpression(taskId);
		String flowId = task.get("BLUEPRINT_FLOW").toString();
		String blueprintInstanceId = task.get("BLUEPRINT_INSTANCE").toString();
		List<Map<String, Object>> pretask = taskService.getPreTaskNotFinished(taskId);
		List<Map<String, Object>> approver = taskService.getApproverNotPass(taskId);
		if (pretask.isEmpty()) {
			if (approver.isEmpty()) {
				if ("05".equals(task.get("STATUS"))) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("_userName", "Quartz");
					String result = blueprintService.executeBlueprintFlow(flowId, blueprintInstanceId, params);
					JSONObject jsonObj = JSON.parseObject(result);
					if (jsonObj.containsKey("result") && jsonObj.getBooleanValue("result")) {
						String flowInstanceId = jsonObj.getString("id");
						Map<String, Object> taskInfo = new HashMap<>();
						taskInfo.put("taskId", taskId);
						taskInfo.put("status", "02");
						taskInfo.put("flowInstanceId", flowInstanceId);
						taskService.updateTask(taskInfo);
					}
//					Timestamp now = new Timestamp(System.currentTimeMillis());
//					Timestamp startTime = (Timestamp) task.get("START_TIME");
//					if (now.after(startTime)) {
//						Map<String, String> params = new HashMap<String, String>();
//						params.put("_userName", "Quartz");
//						String result = blueprintService.executeBlueprintFlow(flowId, blueprintInstanceId, params);
//						JSONObject jsonObj = JSON.parseObject(result);
//						if (jsonObj.containsKey("result") && jsonObj.getBooleanValue("result")) {
//							String flowInstanceId = jsonObj.getString("id");
//							Map<String, Object> taskInfo = new HashMap<>();
//							taskInfo.put("taskId", taskId);
//							taskInfo.put("status", "02");
//							taskInfo.put("flowInstanceId", flowInstanceId);
//							taskService.updateTask(taskInfo);
//						}
//					}
				}
			} else {
				log.debug("当前任务未审批结束");
				return;
			}
		} else {
			log.debug("依赖任务未结束");
			return;
		}
	}

}
