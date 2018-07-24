package com.dc.appengine.appmaster.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskJob implements Job {
	
	private static final Logger log = LoggerFactory.getLogger(TaskJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		long taskId = (Long) context.getMergedJobDataMap().get("taskId");
		log.debug("taskId = {}", taskId);
		QuartzManager.triggerFlow(taskId);
	}

}
