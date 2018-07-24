package com.dc.appengine.appmaster.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atlassian.jira.rest.client.domain.Issue;
import com.dc.appengine.appmaster.dao.IReleaseIntegrationDao;
import com.dc.appengine.appmaster.dao.IReleaseTaskDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseIntegration;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;
import com.dc.appengine.appmaster.integration.IntegrationSynchronizationManager;
import com.dc.appengine.appmaster.service.IReleaseIntegrationService;

@Service("releaseIntegrationService")
public class ReleaseIntegrationService implements IReleaseIntegrationService {

	private static final Logger log = LoggerFactory.getLogger(ReleaseIntegrationService.class);

	@Autowired
	@Qualifier("releaseIntegrationDao")
	private IReleaseIntegrationDao releaseIntegrationDao;
	
	@Autowired
	@Qualifier("releaseTaskDao")
	private IReleaseTaskDao releaseTaskDao;

	@Override
	public Page listReleaseIntegrations(Map<String, Object> params, int pageSize, int pageNum) {
		return releaseIntegrationDao.listReleaseIntegrations(params, pageSize, pageNum);
	}

	@Override
	public ReleaseIntegration getReleaseIntegrationById(int integrationId) {
		return releaseIntegrationDao.getReleaseIntegrationById(integrationId);
	}

	@Override
	public List<ReleaseIntegration> getReleaseIntegrationByName(String integrationName) {
		return releaseIntegrationDao.getReleaseIntegrationByName(integrationName);
	}

	@Override
	public int saveReleaseIntegration(ReleaseIntegration taskIntegration) {
		return releaseIntegrationDao.saveReleaseIntegration(taskIntegration);
	}

	@Override
	public void updateReleaseIntegration(ReleaseIntegration updateTaskIntegration) {
		releaseIntegrationDao.updateReleaseIntegration(updateTaskIntegration);

	}

	@Override
	public void deleteReleaseIntegration(int integrationId) {
		releaseIntegrationDao.deleteReleaseIntegration(integrationId);
	}

	@Override
	@Transactional
	public int synReleaseIntegration(int integrationId) {
		// 同步jira
		ReleaseIntegration integration = releaseIntegrationDao.getReleaseIntegrationById(integrationId);
		String type = integration.getType();
		String projectId = integration.getSystemId();
		String projectName = integration.getSystemName();
		String url = integration.getUrl();
		String usr = integration.getUser();
		String pwd = integration.getPassword();
		IntegrationSynchronizationManager manager = IntegrationSynchronizationManager.getInstance();
		boolean test = manager.getIntegrationImpl(type).testConnection(url, usr, pwd);
		if (!test) {
			throw new RuntimeException("集成类型[" + manager.getTypeName(type) + "]url[" + url + "]用户[" + usr + "]连接失败！ ");
		}
		try {
			List<ReleaseTaskBean> list = manager.getIntegrationImpl(type).synchronize(url, usr, pwd, projectName);
			for (ReleaseTaskBean item : list) {
				item.setSystemId(projectId);
				item.setIntegrationId(integrationId);
			}
			//先删掉所有的
			releaseTaskDao.deleteReleaseTasksByIntegrationId(integrationId);
			//插入新的
			releaseTaskDao.synReleaseIntegration(list);
			//更新同步时间
			releaseIntegrationDao.synReleaseIntegration(integrationId);
			return list.size();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("集成类型[" + manager.getTypeName(type) + "]url[" + url + "]用户[" + usr + "]系统["
					+ projectName + "]同步失败！ " + e.getMessage());
		}
	}

	@Override
	public boolean releaseIntegrationConnectionTest(Map<String, Object> params) {
		String type = "" + params.get("type");
		String url = "" + params.get("url");
		String usr = "" + params.get("user");
		String pwd = "" + params.get("password");
		boolean test = IntegrationSynchronizationManager.getInstance().getIntegrationImpl(type).testConnection(url, usr,
				pwd);
		return test;
	}

}
