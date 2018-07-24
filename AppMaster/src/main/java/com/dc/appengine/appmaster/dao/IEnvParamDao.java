package com.dc.appengine.appmaster.dao;

import java.util.Map;

public interface IEnvParamDao {
	void saveEnvVar(Map<String, Object> params);
}
