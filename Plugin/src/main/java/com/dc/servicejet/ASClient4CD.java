package com.dc.servicejet;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.dc.bd.auto.client.ASClient;
import com.dc.bd.auto.client.JobExecDetail;
import com.dc.bd.auto.client.JobExecResult;

public class ASClient4CD {
	
	private static ASClient4CD instance;
	private ASClient client;
	private String configPath;
	public static final String CLIENT_SYS_ID = "001";
	public static final String CLIENT_SYS_NAME = "SmartCD";
	public static final String CLIENT_SYS_VERSION = "2.0";
	
	private ASClient4CD() {
		ASClientListenerImpl clientListener = new ASClientListenerImpl();
		client = new ASClient(CLIENT_SYS_ID, CLIENT_SYS_NAME, CLIENT_SYS_VERSION, clientListener);
		String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		configPath = new File(jarPath).getParent() + "/plugins_conf/conf/ice.conf";
		client.connect(configPath);
	}
	
	public static ASClient4CD getInstance() {
		synchronized (ASClient4CD.class) {
			if (instance == null) {
				instance = new ASClient4CD();
			}
		}
		return instance;
	}
	
	public JobExecResult execJob(JobExecDetail jobExecDetail, Map<String, String> context) {
//		client.connect(configPath);
		return client.execJob(jobExecDetail, context);
	}
	
	public List<String> getActiveAgents() {
		return client.queryConnectedAgentIps();
	}
	
}
