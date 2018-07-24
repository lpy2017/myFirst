package com.dc.appengine.appmaster.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.Node;

public interface INodeService {
	Node findNodeByName(String name);
	public String saveNode(String content);
	String updateNode(String nodeInfo);
	String deleteNode(String uuid, String ip);
	//批量删除和新增标签
	String deleteLabels(String content);
	String saveLabels(String content);
	Node findNodeById(String id);
	Node findNodeByIp(String ip);
	Node findNodeByClusterAndIp(Map<String, String> param);
	String detectNode(JSONObject content);
}
