package com.dc.appengine.appmaster.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.IEnvParamDao;
import com.dc.appengine.appmaster.entity.EnvVarInfo;
import com.dc.appengine.appmaster.service.IEnvParamService;

@Service("envParamService")
public class EnvParamService implements IEnvParamService {
	private static Logger log = LoggerFactory.getLogger(EnvParamService.class);
	
	@Autowired
	@Qualifier("envParamDao")
	private IEnvParamDao envParamDao;

	@Override
	public void saveEnvVar(EnvVarInfo env, String id, String type, String fromId, String fromType, String versionId,
			String fromVersionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("objId", id);
		params.put("objType", type);
		params.put("varKey", env.getVarName());
		params.put("varValue", env.getVarValue());
		params.put("description", env.getDescription());
		params.put("valueFromId", fromId);
		params.put("valueFromType", fromType);
		params.put("versionId", versionId);
		params.put("fromVersionId", fromVersionId);
		envParamDao.saveEnvVar(params);
	}
	
}
