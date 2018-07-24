package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Node;

public interface INodeDao {
	Node findNodeByName(String name);
	int save(Map<String,Object> node);
	public void saveNodeLabels(List<Map<String, Object>> nodeLabels,String nodeId);
	public String saveNodeResource(Node n);
	public int updateNodeInfo(Map<String,Object> node);
	void deleteNodeLabel(String adapterNodeId);
	void deleteNodeRes(Node n);
	Node findNodeByNodeId(String uuid);
	public void deleteNode(String name);
	public void saveNodeLabels(Map<String, Object> labels);
	public void deleteNodeLabels(Map<String, Object> labels);
	public Node findNodeByNodeIp(String ip);
	public Node findNodeByClusterAndIp(Map<String, String> param);
}
