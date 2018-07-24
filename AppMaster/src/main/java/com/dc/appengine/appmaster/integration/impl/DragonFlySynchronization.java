package com.dc.appengine.appmaster.integration.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.entity.ReleaseTaskBean;
import com.dc.appengine.appmaster.integration.interfaces.IIntegrationSynchronization;

public class DragonFlySynchronization implements IIntegrationSynchronization {
	private static final Logger log = LoggerFactory.getLogger(DragonFlySynchronization.class);

	private transient static DragonFlySynchronization instance;

	private DragonFlySynchronization() {
	}

	public static DragonFlySynchronization getInstance() {
		if (instance == null) {
			synchronized (DragonFlySynchronization.class) {
				if (instance == null) {
					instance = new DragonFlySynchronization();
				}
			}
		}
		return instance;
	}

	@Override
	public List<ReleaseTaskBean> synchronize(String url, String user, String password, String projectName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean testConnection(String url, String user, String password) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void callbackStatus(String url, String user, String password, boolean status, String issueKey)
			throws Exception {
		// TODO Auto-generated method stub
		
	}


}
