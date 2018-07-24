package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IReleaseIntegrationDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseIntegration;

@Component("releaseIntegrationDao")
public class ReleaseIntegrationDao extends PageQuery implements IReleaseIntegrationDao {

	@Override
	public Page listReleaseIntegrations(Map<String, Object> params, int pageSize, int pageNum) {
		return pageQuery(params, pageNum, pageSize, "releaseIntegration.listReleaseIntegrations");
	}

	@Override
	public ReleaseIntegration getReleaseIntegrationById(int integrationId) {
		return sqlSessionTemplate.selectOne("releaseIntegration.getReleaseIntegrationById", integrationId);
	}

	@Override
	public List<ReleaseIntegration> getReleaseIntegrationByName(String integrationName) {
		return sqlSessionTemplate.selectList("releaseIntegration.getReleaseIntegrationByName", integrationName);
	}

	@Override
	public int saveReleaseIntegration(ReleaseIntegration taskIntegration) {
		return sqlSessionTemplate.insert("releaseIntegration.saveReleaseIntegration", taskIntegration);
	}

	@Override
	public void updateReleaseIntegration(ReleaseIntegration updateTaskIntegration) {
		sqlSessionTemplate.update("releaseIntegration.updateReleaseIntegration", updateTaskIntegration);

	}

	@Override
	public void deleteReleaseIntegration(int integrationId) {
		sqlSessionTemplate.delete("releaseIntegration.deleteReleaseIntegration", integrationId);

	}

	@Override
	public void synReleaseIntegration(int integrationId) {
		sqlSessionTemplate.update("releaseIntegration.synReleaseIntegration", integrationId);

	}

}
