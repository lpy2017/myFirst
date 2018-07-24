package com.dc.appengine.plugins.manager.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IPluginMService {
	public String registPlugin(Map<String,Object> param);
	public String updatePlugin(Map<String,Object> param);
	public String deletePlugin(Map<String,Object> param);
	public Map<String, Object> listPugins(JSONObject param);
	public Map<String,Object> getPlugin(String pluginName);
}
