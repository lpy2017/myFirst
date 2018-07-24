package com.dc.appengine.appmaster.service;

import java.util.List;

import com.dc.appengine.appmaster.message.bean.MVNode;

public interface INodeResourceService {
	int updateNode(MVNode node);
	List<MVNode> findAllRunning(String clusterId,String nodeId);
	void updateCpuShared(long id, Boolean b, int usedCount);
}
