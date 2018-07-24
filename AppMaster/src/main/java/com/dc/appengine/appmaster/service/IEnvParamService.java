package com.dc.appengine.appmaster.service;

import com.dc.appengine.appmaster.entity.EnvVarInfo;

public interface IEnvParamService {
	void saveEnvVar(EnvVarInfo env, String id, String type, 
			String fromId, String fromType, String versionId, String fromVersionId);
}
