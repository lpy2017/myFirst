package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.INodeResourceDao;
import com.dc.appengine.appmaster.message.bean.MVNode;
import com.dc.appengine.appmaster.service.INodeResourceService;

@Service("nodeResourceService")
public class NodeResourceService implements INodeResourceService {
	private static final Logger log = LoggerFactory.getLogger(NodeResourceService.class);
	@Autowired
	@Qualifier("nodeResourceDao")
	private INodeResourceDao nodeResourceDao;

	@Override
	public int updateNode(MVNode node) {
		MVNode nodeInDB = nodeResourceDao.findByName(node.getName());
		if ( nodeInDB == null || nodeInDB.getMemory() < node.getMemory()) {
			return -1;
		}
		nodeInDB.setMemory(nodeInDB.getMemory() - node.getMemory());
		nodeInDB.setDisk( nodeInDB.getDisk() - node.getDisk() );
		nodeInDB.setInstanceNum(nodeInDB.getInstanceNum() + node.getInstanceNum());
		nodeInDB.setAlone(node.isAlone());
		nodeInDB.setIsolateState( node.getIsolateState() );
		//nodeInDB.setCpuStr(StringUtils.join(cpus, ","));
		int i = nodeResourceDao.updateNode(nodeInDB);
		if(i > 0){			
			log.info("node " + nodeInDB.getName() + "扣除资源，还剩规划内存：" + nodeInDB.getMemory());
		}else{
			log.info("更新nodeResource失败...");
		}
		return i;
	}

	@Override
	public List<MVNode> findAllRunning(String clusterId, String nodeIds) {
		List<MVNode> list = new ArrayList<MVNode>();
		if (!"".equals(nodeIds) && nodeIds != null) {
			String [] ids = nodeIds.split(",");
			String nodeId = "";
			MVNode tmp = new MVNode();
			for (int i = 0; i < ids.length; i++) {
				nodeId = ids[i];
				tmp = nodeResourceDao.findRunningNodeByNodeId(nodeId);
				if (tmp !=null ) {
					list.add(tmp);
				}
			}
		}else {
			 list = nodeResourceDao.findAllRunningByClusterId(clusterId);
		}
		//将各个node上的cpu字符串转换为boolean逻辑类型 String[] cpus-> boolean[] cpus
		if(list != null && list.size() > 0) {
			for(MVNode node : list) {
				String cpuStr = node.getCpuStr();
				if(cpuStr != null && !cpuStr.isEmpty()) {
					String[] b = cpuStr.split(",");
					boolean[] cpus = new boolean[b.length];
					for(int i = 0; i < b.length; i++) {
						cpus[i] = Boolean.valueOf(b[i]);
					}
					node.setCpus(cpus);
				}
			}
		}
		return list;
	}

	@Override
	public void updateCpuShared(long id, Boolean b, int usedCount) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("shared", b);
		params.put("usedCount", usedCount);
		log.error("cpu id:"+id+",usedCount:"+usedCount);
		nodeResourceDao.updateCpuShared(params);
	}
	
}
