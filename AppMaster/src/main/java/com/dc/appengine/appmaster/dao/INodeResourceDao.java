package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.message.bean.MVNode;

public interface INodeResourceDao {

	MVNode findByName(String name);

	int updateNode(MVNode nodeInDB);

	MVNode findRunningNodeByNodeId(String nodeId);

	List<MVNode> findAllRunningByClusterId(String clusterId);

	void updateCpuShared(Map<String, Object> params);

}
