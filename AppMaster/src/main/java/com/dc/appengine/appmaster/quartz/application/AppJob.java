package com.dc.appengine.appmaster.quartz.application;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AppJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		QuartzManager4App.releaseTasks(context.getMergedJobDataMap());
	}

}
