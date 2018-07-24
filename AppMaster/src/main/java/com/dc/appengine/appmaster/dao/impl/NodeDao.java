package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.INodeDao;
import com.dc.appengine.appmaster.entity.Node;

@Component("nodeDao")
public class NodeDao implements INodeDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	@Override
	public Node findNodeByName(String name) {
		return	sqlSessionTemplate.selectOne("node.findByName",name);
	}
	/**
	 * 保存node
	 * @param node
	 */
	public int save(Map<String,Object> node){
		return sqlSessionTemplate.insert("node.saveNode",node);
	}
	
	public void saveNodeLabels(List<Map<String, Object>> nodeLabels, String nodeId) {
		// TODO Auto-generated method stub
		if(nodeLabels != null && !nodeLabels.isEmpty()){
			for(Map<String, Object> label: nodeLabels){
				Map<String, Object> param = new HashMap<String, Object>();
				System.out.println(label);
				param.put("nodeId", nodeId);
				param.put("key", label.get("key"));
				param.put("value", label.get("value"));
				param.put("type", label.get("type"));
				sqlSessionTemplate.insert("node.saveNodeLabels", param);
			}
		}
	}
	
	@Override
	public String saveNodeResource(Node n) {
		sqlSessionTemplate.insert("node.saveNodeResource", n);
		return n.getAdapterNodeId();
	}
	
	public int updateNodeInfo(Map<String,Object> node) {
		return sqlSessionTemplate.update("node.vmUpdate", node);
	}
	@Override
	public void deleteNodeLabel(String adapterNodeId) {
		
		sqlSessionTemplate.delete("node.deleteNodeLabels", adapterNodeId);
		
	}

	@Override
	public void deleteNodeRes(Node n) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("node.deleteNodeResource",n.getName());
	}
	@Override
	public Node findNodeByNodeId(String uuid) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("node.findById",uuid);
	}
	public void deleteNode(String name){
		sqlSessionTemplate.delete("node.deleteByName",name);
	}
	@Override
	public void saveNodeLabels(Map<String, Object> labels) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("node.addLabels", labels);
	}
	@Override
	public void deleteNodeLabels(Map<String, Object> labels) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("node.deleteLabels", labels);
	}
	
	@Override
	public Node findNodeByNodeIp(String ip) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("node.findByIp",ip);
	}
	@Override
	public Node findNodeByClusterAndIp(Map<String, String> param) {
		return sqlSessionTemplate.selectOne("node.findByClusterAndIp", param);
	}
	
}
