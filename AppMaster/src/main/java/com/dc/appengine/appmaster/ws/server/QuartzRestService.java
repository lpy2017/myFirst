package com.dc.appengine.appmaster.ws.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.quartz.QuartzManager;

@RestController
@RequestMapping("/ws/quartz")
public class QuartzRestService {
	
	private static final Logger log = LoggerFactory.getLogger(QuartzRestService.class);
	
	@RequestMapping(value = "checkCronExpression", method = RequestMethod.GET)
	public String testService(@RequestParam("cronExpression") String cronExpression) {
		log.debug("cronExpression = {}", cronExpression);
		return QuartzManager.checkCronExpression(cronExpression) + "";
	}
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String add(@RequestParam("taskId") long taskId) {
		QuartzManager.addTaskJob(taskId);
		return "add task";
	}
	
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(@RequestParam("taskId") long taskId) {
		QuartzManager.updateTaskJob(taskId);
		return "update task";
	}
	
	@RequestMapping(value = "remove", method = RequestMethod.GET)
	public String remove(@RequestParam("taskId") long taskId) {
		QuartzManager.removeTaskJob(taskId);
		return "remove task";
	}

}
