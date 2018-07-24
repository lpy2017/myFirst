package com.dc.appengine.appmaster.integration;

import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.appmaster.integration.impl.DragonFlySynchronization;
import com.dc.appengine.appmaster.integration.impl.ITJobSynchronization;
import com.dc.appengine.appmaster.integration.impl.JiraSynchronization;
import com.dc.appengine.appmaster.integration.impl.RedmineSynchronization;
import com.dc.appengine.appmaster.integration.interfaces.IIntegrationSynchronization;

public class IntegrationSynchronizationManager {

	private static final String CR_TYPE = "01";
	private static final String REDMINE_TYPE = "02";
	private static final String JIRA_TYPE = "03";
	private static final String ITJOB_TYPE = "04";
	private static final String DRAGONFLY_TYPE = "05";
	private static final String OTHER_TYPE = "06";

	private static transient IntegrationSynchronizationManager instance;
	private static Map<String, IIntegrationSynchronization> integrationMap;
	private static Map<String, String> typeMap;

	private IntegrationSynchronizationManager() {
		typeMap = new HashMap<>();
		typeMap.put(CR_TYPE, "发布管理平台");
		typeMap.put(REDMINE_TYPE, "redmine");
		typeMap.put(JIRA_TYPE, "jira");
		typeMap.put(ITJOB_TYPE, "国寿IT作业管理平台");
		typeMap.put(DRAGONFLY_TYPE, "droganFly");
		typeMap.put(OTHER_TYPE, "其他");
		integrationMap = new HashMap<>();
		IIntegrationSynchronization redmine = RedmineSynchronization.getInstance();
		integrationMap.put(REDMINE_TYPE, redmine);
		IIntegrationSynchronization jira = JiraSynchronization.getInstance();
		integrationMap.put(JIRA_TYPE, jira);
		IIntegrationSynchronization itjob = ITJobSynchronization.getInstance();
		integrationMap.put(ITJOB_TYPE, itjob);
		IIntegrationSynchronization dragonfly = DragonFlySynchronization.getInstance();
		integrationMap.put(DRAGONFLY_TYPE, dragonfly);
	}

	public static IntegrationSynchronizationManager getInstance() {
		if (instance == null) {
			synchronized (IntegrationSynchronizationManager.class) {
				if (instance == null) {
					instance = new IntegrationSynchronizationManager();
				}
			}
		}
		return instance;
	}

	public IIntegrationSynchronization getIntegrationImpl(String type) {
		return integrationMap.get(type);
	}

	public String getTypeName(String type){
		return typeMap.get(type);
	}

}
