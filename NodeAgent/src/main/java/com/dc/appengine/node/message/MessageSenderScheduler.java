package com.dc.appengine.node.message;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dcfs.impls.esb.client.ConsumerClient;

public class MessageSenderScheduler {
	
	private long initialDelay = 0;
	private long delay = 1000;
	private long sleeptime = 1000;
	private String filePath = null;
	private ConsumerClient client = null;
	
	public MessageSenderScheduler(String filePath, ConsumerClient client) {
		this.filePath = filePath;
		this.client = client;
	}
	
	public MessageSenderScheduler(String filePath, ConsumerClient client, long initialDelay, long delay, long sleeptime) {
		this.filePath = filePath;
		this.client = client;
		this.initialDelay = initialDelay;
		this.delay = delay;
		this.sleeptime = sleeptime;
	}
	
	public void start() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(new MessageSenderTask(filePath, client, sleeptime), initialDelay, delay, TimeUnit.MILLISECONDS);
	}

}
