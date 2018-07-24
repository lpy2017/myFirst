package com.dc.appengine.appmaster.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.INodeDao;
import com.dc.appengine.appmaster.entity.Node;
import com.dc.appengine.appmaster.service.INodeService;
import com.dc.appengine.appmaster.utils.ConfigHelper;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Service("nodeService")
public class NodeService implements INodeService {
	
	@Autowired
	@Qualifier("nodeDao")
	private INodeDao dao;
	
	@Value(value="${detectTimeOut:5000}")
	Integer detectTimeOut;
	@Override
	public Node findNodeByName(String name) {
		return dao.findNodeByName(name);
	}

	public String saveNode(String content) {
		Map<String,Object> map = JSON.parseObject(content, new TypeReference<Map<String,Object>>(){});
		Map<String,Object> nodeMap= new HashMap<String,Object>(); 
        nodeMap.put("id", map.get("id").toString());
		nodeMap.put("ip", map.get("ip").toString());
		nodeMap.put("name", map.get("name").toString());
		nodeMap.put("description", "");
		nodeMap.put("cpu", map.get("cpu").toString());
		nodeMap.put("disk", map.get("disk").toString());
		nodeMap.put("memory", map.get("memory").toString());
		nodeMap.put("cluster_id", map.get("cluster_id").toString());
		List<Map<String,Object>> lableList = new ArrayList<>();
		if(!"".equals(map.get("labels")) && map.get("labels") != null){
			List<Map<String,Object>> temp = JSON.parseObject(map.get("labels").toString(), new TypeReference<List<Map<String,Object>>>(){});
			System.out.println(temp);
			if(temp != null && temp.size() > 0){
				for(Map<String,Object> m: temp){
					/*String key = m.get("key").toString();
					String value = m.get("value").toString();
					String[] labelsStr=m.toString().split(",");
					Map<String, Object> labelsMap=new HashMap<String, Object>();
					for(int i =0;i<labelsStr.length;i++){
						
						int index = labelsStr[i].indexOf("=");
						String key = labelsStr[i].substring(0, index);
						String value = labelsStr[i].substring(index+1);
						if(i == 0){
							key = key.substring(1).trim();
						}else if(i == labelsStr.length -1 ){
							value = value.substring(0, value.length() -1 );
						}
						labelsMap.put(key, value);
					}*/
					lableList.add(m);
				}
			}
			
			
		}
		nodeMap.put("labels", lableList);
		Node n = findNodeByName(nodeMap.get("name").toString());
		if( n == null ){
			//保存到ma_node
			int temp = dao.save(nodeMap);
			Node node = dao.findNodeByNodeId(nodeMap.get("id").toString());
			//保存到ma_node_resource
			saveNodeResource( node );
			//存储node的labels
			dao.saveNodeLabels((List<Map<String, Object>>) nodeMap.get("labels"), node.getAdapterNodeId());
			return "true";
		}else{
			//更新ma_node
			
			dao.updateNodeInfo(nodeMap);
			Node newNode = findNodeByName(nodeMap.get("name").toString());
			//更新ma_label
			dao.deleteNodeLabel( n.getAdapterNodeId());
			dao.saveNodeLabels((List<Map<String, Object>>) nodeMap.get("labels"), newNode.getAdapterNodeId());
			//更新ma_node_resource
			dao.deleteNodeRes(n);
			dao.saveNodeResource(newNode);
		}
			return "true";
	}

	public String saveNodeResource(Node n) {
		if( Node.ISOLATA_STATE_WEAK.equals( n.getIsolata() ) ){
			int cpuNum = n.getCpucount();
			String rate = ConfigHelper.getValue( "cpu.rate" );
			if( rate != null ){
				cpuNum *= Double.parseDouble( rate );
				n.setCpucount( cpuNum );
			}
			Boolean[] cpus = new Boolean[cpuNum];
			for( int i = 0 ; i < cpus.length ; i++ ){
				cpus[i] = false;
			}
			n.setCpuStr( StringUtils.join( cpus, "," ) );
		} else {
			boolean[] b = n.getCpus();
			if(b != null && b.length > 0) {
				Boolean[] cpus = new Boolean[b.length];
				int i = 0;
				for(boolean cpu : b) {
					cpus[i] = Boolean.valueOf(cpu);
					i++;
				}
				n.setCpuStr(StringUtils.join(cpus, ","));
			} else {
				Boolean[] cpus = new Boolean[n.getCpucount()];
				for(int i = 0; i < n.getCpucount(); i++) {
					cpus[i] = false;
				}
				n.setCpuStr(StringUtils.join(cpus, ","));
			}
		}
		return dao.saveNodeResource(n);
	}

	@Override
	public String updateNode(String nodeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteNode(String uuid, String ip) {
		// TODO Auto-generated method stub
		Node n = dao.findNodeByNodeId(uuid);
		dao.deleteNode(n.getName());
		dao.deleteNodeLabel(uuid);
		dao.deleteNodeRes(n);
		return "true";
	}

	@Override
	public String deleteLabels(String content) {
		// TODO Auto-generated method stub
		Map<String, Object> labels = JSON.parseObject(content, new TypeReference<Map<String, Object>>(){});
		dao.deleteNodeLabels(labels);
		return MessageHelper.wrap("result", true, "info", "success to delete labels");
	}

	@Override
	public String saveLabels(String content) {
		// TODO Auto-generated method stub
		Map<String, Object> labels = JSON.parseObject(content, new TypeReference<Map<String, Object>>(){});
		dao.saveNodeLabels(labels);
		return MessageHelper.wrap("result", true, "info", "success to add labels");
	}

	@Override
	public Node findNodeById(String id) {
		// TODO Auto-generated method stub
		return dao.findNodeByNodeId(id);
	}

	@Override
	public Node findNodeByIp(String ip) {
		// TODO Auto-generated method stub
		return dao.findNodeByNodeIp(ip);
	}

	@Override
	public Node findNodeByClusterAndIp(Map<String, String> param) {
		return dao.findNodeByClusterAndIp(param);
	}

	@Override
	public String detectNode(JSONObject params) {
		String ip = params.getString("ip");
		InetAddress address;
		Boolean ping =false;
		String info="";
		try {
			address = InetAddress.getByName(ip);
			ping = address.isReachable(detectTimeOut);
			if (ping) {
				info="success to ping ip:"+ip;
				System.out.println("success to ping ip:"+ip);
			} else {
				info="failed to ping ip:"+ip;
				System.out.println("failed to ping ip:"+ip);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			info="failed to ping ip:"+ip+" ["+e.getMessage()+"]";
		}
		JSONObject result = new JSONObject();
		result.put("result", ping);
		result.put("info", info);
		return result.toJSONString();
	}
	
	public static void main(String [] args){

		String ip = "100.126.3.217";
		InetAddress address;
		try {
			address = InetAddress.getByName(ip);
			Boolean ping = address.isReachable(5000);
			if (ping) {
				System.out.println("success to ping ip:"+ip);
			} else {
				System.out.println("failed to ping ip:"+ip);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
