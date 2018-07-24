package com.dc.appengine.appmaster.service;

import java.util.Map;

public interface IPluginService {
	public String registPlugin(Map<String,Object> param);
	public String updatePlugin(Map<String,Object> param);
	public String deletePlugin(Map<String,Object> param);
	public Map<String, Object> listPugins(String pluginName,int pageNum,int pageSize);
	public Map<String,Object> getPlugin(String pluginName);
}
