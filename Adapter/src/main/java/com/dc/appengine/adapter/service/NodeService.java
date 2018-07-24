package com.dc.appengine.adapter.service;

//import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dc.appengine.adapter.dao.NodeDAO;
//import com.dc.appengine.adapter.entity.VMStatus;

@Service("NodeService")
public class NodeService {
	
	@Resource
	private NodeDAO nodeDao;
	
	public int insert(Map<String, Object> param) {
		return nodeDao.insert(param);
	}
	
	public int delete(Map<String, Object> param) {
//		List<Map<String, Object>> nodes = nodeDao.select(param);
//		if (nodes.size() == 1) {
//			Map<String, Object> node = nodes.get(0);
//			if (node.get("vm_id") != null && !"".equals(node.get("vm_id")) && !"null".equals(node.get("vm_id"))) {
//				Map<String, Object> vm = new HashMap<String, Object>();
//				vm.put("status", VMStatus.idle);
//				String[] ids = {(String) node.get("vm_id")};
//				vm.put("ids", ids);
//				iaaSVMDAO.update(vm);
//			}
//		}
		return nodeDao.delete(param);
	}
	
	public int update(Map<String, Object> param) {
		return nodeDao.update(param);
	}
	
	public List<Map<String, Object>> select(Map<String, Object> param) {
		List<Map<String, Object>> nodes = nodeDao.select(param);
		for (Map<String, Object> node : nodes) {
//			node.put("bridges", bridgeDAO.findByNode((String) node.get("id")));
			List<Map<String, Object>> labelList = nodeDao.getLabel((String) node.get("id"));
			if (labelList.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (Map<String, Object> label : labelList) {
					sb.append(",").append(label.get("key")).append("=").append(label.get("value"));
				}
				String labelsAll = sb.toString();
				labelsAll = labelsAll.substring(1);
				node.put("labels", labelsAll);
			} else {
				node.put("labels", "");
			}
		}
		return nodes;
	}
	
	public List<Map<String, Object>> nodelist(Map<String, Object> param) {
		List<Map<String, Object>> nodes = nodeDao.select(param);
		return nodes;
	}
	
	public int count(Map<String, Object> param) {
		return nodeDao.count(param);
	}
	
	public int uicount(Map<String, Object> param) {
		return nodeDao.uicount(param);
	}
	
	public List<Map<String, Object>> page(Map<String, Object> param) {
		return nodeDao.page(param);
	}
	
	public List<Map<String, Object>> ui(Map<String, Object> param) {
		return nodeDao.ui(param);
	}
	
	public List<Map<String, Object>> getLabel(String node_id) {
		return nodeDao.getLabel(node_id);
	}
	
	public int checkName(Map<String, Object> param) {
		return nodeDao.checkName(param);
	}
	
	public Map<String, Object> one(String id) {
		return nodeDao.one(id);
	}
	
	public int updateByStatus(Map<String, Object> param) {
		return nodeDao.updateByStatus(param);
	}
	
	public int multiAdd(Map<String, Object> param) {
		return nodeDao.multiAdd(param);
	}

}
