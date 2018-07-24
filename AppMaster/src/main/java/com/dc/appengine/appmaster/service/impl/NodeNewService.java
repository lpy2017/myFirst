package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.dao.impl.NodeNewDAO;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.messageserver.jmx.QueueViewMBean;

@Service("nodeNewService")
public class NodeNewService {
	private static final Logger log = LoggerFactory
			.getLogger(NodeNewService.class);
	@Autowired
	@Qualifier("nodeNewDAO")
	private NodeNewDAO dao;
	
	@Value(value="${momIp:127.0.0.1}")
	String momIp;
	
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	
	public int insert(Map<String, Object> param) {
		return dao.insert(param);
	}
	
	public int delete(String nodeId) {
		return dao.delete(nodeId);
	}
	
	public int deleteCheck(String nodeId) {
		return dao.deleteCheck(nodeId);
	}
	
	public int update(Map<String, Object> param) {
		return dao.update(param);
	}
	
	public Page list(Map<String, Object> condition, int pageNum, int pageSize) {
		return dao.list(condition, pageNum, pageSize);
	}
	
	public List<Map<String, Object>> listAll(String clusterId) {
		return dao.listAll(clusterId);
	}
	
	public Map<String, Object> one(String nodeId) {
		return dao.one(nodeId);
	}
	
	public int multiDelete(String[] nodes) {
		return dao.multiDelete(nodes);
	}
	
	public int checkIP(String ip) {
		return dao.checkIP(ip);
	}
	
	public int checkOtherIP(Map<String, Object> node) {
		return dao.checkOtherIP(node);
	}

	public List lookupAliveNode() {
		List selectorList = new ArrayList();
		try{
			MBeanServerConnection connection = JMXConnectorFactory.connect(new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + momIp + ":7799/jmxrmi")).getMBeanServerConnection();
			ObjectName oName = new ObjectName("com.dc.messageserver:name=queue_NodeService.out");
			QueueViewMBean out = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, oName, QueueViewMBean.class, true);
			Map<String,Integer> selectors = out.reloadReceiverCountWithSelector();
			for(String selectorKey : selectors.keySet()){
				String tempSelectorKey = selectorKey.split("MXSD_DISTINCT_TO=")[1];
				selectorList.add(tempSelectorKey.subSequence(1, tempSelectorKey.length()-1));
			}
		}catch(Exception e){
			log.debug("连接momServer异常 momIp="+momIp);
			e.printStackTrace();
		}
		return selectorList;
	}
	
	public List<Object> lookupAliveAutoAgent() {
		try {
			RestTemplate rest = RequestClient.getInstance().getRestParamTemplate();
			String result = rest.getForObject(flowServerUrl + "/cd/nodes.wf", String.class);
			return JSON.parseArray(result);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	public static void main(String [] args){
		NodeNewService nodeservice = new NodeNewService();
		System.out.println("selectors="+JSON.toJSONString(nodeservice.lookupAliveNode()));
		System.out.println("nohao");
	}

}
