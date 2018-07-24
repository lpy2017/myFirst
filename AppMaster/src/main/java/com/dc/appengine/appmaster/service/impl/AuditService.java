package com.dc.appengine.appmaster.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.IAuditDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IAudit;
@Service("auditService")
public class AuditService implements IAudit {

	@Autowired
	@Qualifier("auditDao")
	private IAuditDao auditDao;
	
	@Override
	public int save(AuditEntity entity) {
		return auditDao.saveAllBlueprint(entity);
	}

	@Override
	public Page listAudit(Map<String, Object> condition, int pageNum, int pageSize) {
		return auditDao.listAudit(condition,pageNum,pageSize);
	}

	@Override
	public List<AuditEntity> exportAudit(List tempIds) {
		return auditDao.exportAudit(tempIds);
	}

	@Override
	public List<Map<String, Object>> listAll(int total) {
		return auditDao.listAll(total);
	}

}
