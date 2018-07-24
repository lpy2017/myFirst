package com.dc.appengine.appmaster.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dc.appengine.appmaster.dao.impl.NodeLabelDAO;

@Service("nodeLabelService")
public class NodeLabelService {
	
	@Resource
	private NodeLabelDAO dao;
	
	public List<Map<String, Object>> findLabelsByCluster(Map<String, Object> param) {
		// param demo
//		{
//		    "cluster_id": "uuid",
//		    "type": "0"
//		}
		
		// result demo
//		[
//		    {
//		        "key": "db",
//		        "value": "mysql",
//		        "type": "0"
//		    }
//		]
		return dao.findLabelsByCluster(param);
	}
	
	public List<Map<String, Object>> findNodesByLabel(Map<String, Object> param) {
		// param demo
//		{
//		    "cluster_id": "uuid",
//		    "type": "0",
//		    "key": "db",
//		    "value": "mysql"
//		}
		
		// result demo
		//ma_node表结构
		return dao.findNodesByLabel(param);
	}
	
	public int insert(Map<String, Object> param) {
		return dao.insert(param);
	}
	
	/**
	 * 
	 * @param param
	 * {
			"key": "k",
			"value": "v",
			"type": "1",
			"clusterId": "uuid"
		}
	 * @return
	 */
	public int delete(Map<String, Object> param) {
		return dao.delete(param);
	}
	
	public int check(Map<String, Object> param) {
		return dao.check(param);
	}
	
	/**
	 * 
	 * @param param
	 * {
			"label": {
				"key": "k",
				"value": "v",
				"type": "1"
			},
			"nodes": ["66f65302-02fc-4e76-8025-a308522ca68f"],
			"clusterId": "d6ff6710-eeea-448f-a762-9536d6a6d4ac"
		}
	 * @return
	 */
	@Transactional
	public Map<String, Integer> update(Map<String, Object> param) {
		Map<String, Object> label = (Map<String, Object>) param.get("label");
		Map<String, Object> deleteParam= new HashMap<>();
		deleteParam.put("clusterId", param.get("clusterId"));
		deleteParam.put("key", label.get("key"));
		deleteParam.put("value", label.get("value"));
		deleteParam.put("type", label.get("type"));
		int insert = 0;
		int delete = 0;
		delete = dao.delete(deleteParam);
		insert = dao.insert(param);
		Map<String, Integer> result = new HashMap<>();
		result.put("insert", insert);
		result.put("delete", delete);
		return result;
	}
	
	public List<Map<String, Object>> nodes(Map<String, Object> param) {
		return dao.nodes(param);
	}

}
