package com.dc.appengine.appmaster.integration.interfaces;

import java.util.List;

import com.dc.appengine.appmaster.entity.ReleaseTaskBean;

public interface IIntegrationSynchronization {

	public List<ReleaseTaskBean> synchronize(String url, String user, String password, String projectName) throws Exception;

	public boolean testConnection(String url, String user, String password);

	public void callbackStatus(String url, String user, String password, boolean status, String issueKey) throws Exception;

}
