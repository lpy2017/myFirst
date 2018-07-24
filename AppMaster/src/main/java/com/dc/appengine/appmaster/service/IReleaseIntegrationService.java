package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseIntegration;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;

public interface IReleaseIntegrationService {

	Page listReleaseIntegrations(Map<String, Object> params, int pageSize, int pageNum);

	ReleaseIntegration getReleaseIntegrationById(int integrationId);

	List<ReleaseIntegration> getReleaseIntegrationByName(String integrationName);

	int saveReleaseIntegration(ReleaseIntegration taskIntegration);

	void updateReleaseIntegration(ReleaseIntegration updateTaskIntegration);

	void deleteReleaseIntegration(int integrationId);

	int synReleaseIntegration(int integrationId);

	boolean releaseIntegrationConnectionTest(Map<String, Object> params);

}
