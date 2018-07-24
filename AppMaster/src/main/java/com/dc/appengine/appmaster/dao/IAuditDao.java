package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;

public interface IAuditDao {

	public int saveAllBlueprint(AuditEntity entity);
	
	public Page listAudit(Map<String, Object> condition, int pageNum, int pageSize);
	public List<AuditEntity> exportAudit(List tempIds);
	List<Map<String, Object>> listAll(int total);
}
