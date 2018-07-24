package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IAuditDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;

@Component("auditDao")
public class AuditDao extends PageQuery implements IAuditDao {

	@Override
	public int saveAllBlueprint(AuditEntity entity) {
		return sqlSessionTemplate.insert("audit.save",entity);
	}

	@Override
	public Page listAudit(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "audit.listAudit");
	}

	@Override
	public List<AuditEntity> exportAudit(List tempIds) {
		return sqlSessionTemplate.selectList("audit.exportAudit", tempIds);
	}

	@Override
	public List<Map<String, Object>> listAll(int total) {
		return sqlSessionTemplate.selectList("audit.listAll", total);
	}

}
