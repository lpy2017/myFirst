package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.INodeResourceDao;
import com.dc.appengine.appmaster.message.bean.MVNode;
@Component("nodeResourceDao")
public class NodeResourceDao implements INodeResourceDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	@Override
	public MVNode findByName(String name) {
		return sqlSessionTemplate.selectOne("nodeResource.findByName", name);
	}

	@Override
	public int updateNode(MVNode nodeInDB) {
		return sqlSessionTemplate.update("nodeResource.update", nodeInDB);
	}

	@Override
	public MVNode findRunningNodeByNodeId(String nodeId) {
		return sqlSessionTemplate
				.selectOne("nodeResource.findRunningNodeByNodeId", nodeId);
	}

	@Override
	public List<MVNode> findAllRunningByClusterId(String clusterId) {
		return sqlSessionTemplate
				.selectList("nodeResource.findAllRunningByClusterId", clusterId);
	}

	@Override
	public void updateCpuShared(Map<String, Object> params) {
		sqlSessionTemplate.update("nodeResource.updateCpuShared", params);
	}
	
}
