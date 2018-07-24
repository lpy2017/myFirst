package com.dc.appengine.appmaster.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.FlowNode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoLog {
	MongoClient mongoClient=null;
	MongoDatabase database=null;
	public MongoLog(){
		MongoClientOptions.Builder build = new MongoClientOptions.Builder();        
        build.connectionsPerHost(50);   //与目标数据库能够建立的最大connection数量为50
//        build.//自动重连数据库启动
        build.socketKeepAlive(true);
        build.threadsAllowedToBlockForConnectionMultiplier(50); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
        /*
         * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
         * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
         * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
         */
        build.maxWaitTime(1000*60*2);
        build.connectTimeout(1000*60*1);    //与数据库建立连接的timeout设置为1分钟
        
        MongoClientOptions myOptions = build.build();     
//		mongoClient = new MongoClient( new MongoClientURI("mongodb://localhost:27017"));
		List<ServerAddress> seeds = new ArrayList<ServerAddress>();
		ServerAddress serverAddress = new ServerAddress("localhost", 27017);
		seeds.add(serverAddress);
		mongoClient = new MongoClient(seeds, myOptions);
		database = mongoClient.getDatabase("flowlogdb");
	}
	public void logInsertInstanceStatusMsg(JSONObject msg){
		Document doc=Document.parse(msg.toJSONString());
		MongoCollection<Document> collection = database.getCollection("flowinstancestatuslogs");
		collection.insertOne(doc);
	}
	public void logInsertInstancelogsMsg(JSONObject msg){
		Document doc=Document.parse(msg.toJSONString());
		MongoCollection<Document> collection = database.getCollection("flowinstancelogs");
		collection.insertOne(doc);
	}
	/*
	 * 查询流程节点
	 */
	public MongoCursor queryFlowNodes(String flowInstanceId){
		MongoCollection<Document> collection = database.getCollection("flowinstancelogs");
		Document obj = new Document();
		Pattern pattern = Pattern.compile("^\\[\"" + Pattern.quote(flowInstanceId) + ".*$");
		obj.put("FlowInstanceIds", pattern);
		long count = collection.count(obj);
		System.out.println("flowinstancelogs Count"+count);
		FindIterable<Document> findIterable = collection.find(obj);
		MongoCursor<Document> mongocursor = findIterable.iterator();
		return mongocursor;
	}
	/*
	 * 查询流程实例
	 */
	public MongoCursor queryFlowInstances(String flowInstanceId){
		MongoCollection<Document> collection = database.getCollection("flowinstancestatuslogs");
		Document obj = new Document();
		Pattern pattern = Pattern.compile("^\\[\"" + Pattern.quote(flowInstanceId) + ".*$");
		obj.put("FlowInstanceIds", pattern);
		Document sort = new Document();
		sort.put("BeginTime", 1);//升序
		long count = collection.count(obj);
		System.out.println("flowinstancestatuslogs Count "+count);
		FindIterable<Document> findIterable = collection.find(obj).sort(sort);
		MongoCursor<Document> mongocursor = findIterable.iterator();
		return mongocursor;
	}
	
	/*
	 * 获取流程树
	 */
	public FlowNode generateFlowTree(String flowInstanceId,String rootFlowId){
		Map<String,Object> nodes = new HashMap<>();
		Map<String,Object> instances = new HashMap<>();
		MongoCursor<Document> nodeInstances = queryFlowNodes(flowInstanceId);
		MongoCursor<Document> flowInstances = queryFlowInstances(flowInstanceId);
		while (nodeInstances.hasNext()) {
			Document document = (Document) nodeInstances.next();
			FlowNode flowNode = new FlowNode();
			String LastFlowInstanceId = document.getString("LastFlowInstanceId");
			String nodeKey= document.get("NodeKey")+"";
			String text=document.getString("text");
			Boolean status = document.getBoolean("Status");
			String flowId = document.getString("FlowId");
			flowNode.setId(document.getObjectId("_id")+"");
			flowNode.setNodeComment(nodeKey);
			flowNode.setParentFlowId(flowId);
//			flowNode.setFlowId(flowId);
			flowNode.setInstanceId(LastFlowInstanceId);
			flowNode.setStartTime(document.getString("BeginTime"));
			flowNode.setEndTime(document.getString("EndTime"));
			flowNode.setName(text);
			if(status){
				flowNode.setState(2);//成功
			}else if(!status){
				flowNode.setState(7);//失败
			};
			//同一流程实例可能有多个子节点
			List<FlowNode> list =null;
			if(nodes.get(LastFlowInstanceId) == null){
				list = new ArrayList<>();
			}else{
				list =(List<FlowNode>)nodes.get(LastFlowInstanceId);
			}
			list.add(flowNode);
			nodes.put(LastFlowInstanceId, list);
		}
		FlowNode rootNode = new FlowNode();
		rootNode.setFlowId(rootFlowId);
		rootNode.setInstanceId(flowInstanceId);
		Map<String,Object> flowNodes = new HashMap<>();
		while (flowInstances.hasNext()) {
			Document document = (Document) flowInstances.next();
			FlowNode flowNode = new FlowNode();
			List<String> list = JSONObject.parseArray(document.get("FlowInstanceIds").toString(), String.class);
			if(list.size()>=2){
				list.remove(1);
			}
			String instanceId=document.getString("FlowInstanceId");
			Boolean status = document.getBoolean("Status");
			String flowId = document.getString("FlowId");
			flowNode.setId(document.getObjectId("_id")+"");
			flowNode.setFlowId(flowId);
			flowNode.setInstanceId(instanceId);
			flowNode.setStartTime(document.getString("BeginTime"));
			flowNode.setEndTime(document.getString("EndTime"));
			flowNode.setName(instanceId);
			String parentInstanceId="";
			if(status){
				flowNode.setState(2);//成功
			}else if(!status){
				flowNode.setState(7);//失败
			}
			if(list.size()==1 && instanceId.equals(list.get(0))){//根节点
				flowNode.setRootNode(true);
				rootNode=flowNode;
			}else if(list.size() > 1){
				flowNode.setRootNode(false);
				parentInstanceId=list.get(list.size()-2);
			}
			flowNode.setParentInstanceId(parentInstanceId);
			if(list.size() > 1){
				//同一流程实例可能有多个子节点
				List<FlowNode> flowList =null;
				if(flowNodes.get(instanceId) == null){
					flowList = new ArrayList<>();
				}else{
					flowList =(List<FlowNode>)flowNodes.get(instanceId);
				}
				flowList.add(flowNode);
				flowNodes.put(parentInstanceId, flowList);
			}
			instances.put(instanceId, flowNode);
			if(!flowNode.getRootNode()){
				((FlowNode) instances.get(flowNode.instanceId)).setChildren((List)nodes.get(flowNode.instanceId));;
				((FlowNode) instances.get(flowNode.parentInstanceId)).setChildren((List)flowNodes.get(flowNode.parentInstanceId));
			}
		}
		System.out.println(JSON.toJSONString(rootNode));
		return rootNode;
	}
	
	/**
	 * 分页查询蓝图实例执行记录
	 * @param bluePrintInstanceId
	 * @param flowId
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Map<String,Object> queryFlowInstancesByPage(String bluePrintInstanceId,String flowId,int pageNum,int pageSize){
		Map<String,Object> result = new HashMap<String,Object>();
		List<Map<String,Object>> rows = new ArrayList<>();
		MongoCollection<Document> collection = database.getCollection("flowinstancestatuslogs");
		Document obj = new Document();
		obj.put("bluePrintInstanceId", bluePrintInstanceId);
		obj.put("FlowId", flowId);
		Document sort = new Document();
		sort.put("BeginTime", 1);//升序
		int skip = pageSize*(pageNum-1);
		long total=collection.count(obj);//条件查询出来的总数
		FindIterable<Document> findIterable = collection.find(obj).sort(sort).skip(skip).limit(pageSize);//分页查询结果
		MongoCursor<Document> mongocursor = findIterable.iterator();
		while (mongocursor.hasNext()) {
			Document document = (Document) mongocursor.next();
			String state = "";
			String flowState="";
			if(JudgeUtil.isEmpty(document.get("EndTime"))){
				state ="-1";
				flowState="未结束";
			}else{
				state = (Boolean)document.get("Status")?"2":"7";
				flowState="已结束";
			}
			Map<String,Object> row = new HashMap<>();
			row.put("id", document.get("bluePrintInstanceId"));
			row.put("instanceId", document.getString("FlowInstanceId"));
			row.put("startTime", document.getString("BeginTime"));
			row.put("flowState", flowState);
			row.put("state", state);
			rows.add(row);
		}
		result.put("pageNumber", pageNum);
		result.put("pageSize", pageSize);
		result.put("total", total);
		result.put("rows", rows);
		return result;
	}
	/*
	 * 获取节点日志
	 */
	public Map<String,Object> queryFlowInstance(String flowInstanceId){
		Map<String,Object> map = new HashMap<>();
		MongoCollection<Document> collection = database.getCollection("flowinstancelogs");
		Document obj = new Document();
		obj.put("_id", new ObjectId(flowInstanceId));
		FindIterable<Document> findIterable = collection.find(obj);
		MongoCursor<Document> mongocursor = findIterable.iterator();
		while (mongocursor.hasNext()) {
			Document document = (Document) mongocursor.next();
			JSONObject message = (JSONObject)document.get("Message");
			JSONObject result = JSON.parseObject(message.get("result").toString());;
			map.put("log", result.get("message"));
		}
		return map;
	}
	/*
	 * 获取流程的子节点
	 */
	public List<Map<String,Object>> getFlowNodes(String flowInstanceId,String rootFlowId){
		Map<String,Object> nodes = new HashMap<>();
		Map<String,Object> instances = new HashMap<>();
		MongoCursor<Document> nodeInstances = queryFlowNodes(flowInstanceId);
		MongoCursor<Document> flowInstances = queryFlowInstances(flowInstanceId);
		while (nodeInstances.hasNext()) {
			Document document = (Document) nodeInstances.next();
			FlowNode flowNode = new FlowNode();
			String LastFlowInstanceId = document.getString("LastFlowInstanceId");
			String nodeKey= document.get("NodeKey")+"";
			String text=document.getString("text");
			Boolean status = document.getBoolean("Status");
			String flowId = document.getString("FlowId");
			flowNode.setId(document.getObjectId("_id")+"");
			flowNode.setNodeComment(nodeKey);
			flowNode.setParentFlowId(flowId);
//			flowNode.setFlowId(flowId);
			flowNode.setInstanceId(LastFlowInstanceId);
			flowNode.setStartTime(document.getString("BeginTime"));
			flowNode.setEndTime(document.getString("EndTime"));
			flowNode.setName(text);
			if(status){
				flowNode.setState(2);//成功
			}else if(!status){
				flowNode.setState(7);//失败
			};
			//同一流程实例可能有多个子节点
			List<FlowNode> list =null;
			if(nodes.get(LastFlowInstanceId) == null){
				list = new ArrayList<>();
			}else{
				list =(List<FlowNode>)nodes.get(LastFlowInstanceId);
			}
			list.add(flowNode);
			nodes.put(LastFlowInstanceId, list);
		}
		FlowNode rootNode = new FlowNode();
		rootNode.setFlowId(rootFlowId);
		rootNode.setInstanceId(flowInstanceId);
		Map<String,Object> flowNodes = new HashMap<>();
		while (flowInstances.hasNext()) {
			Document document = (Document) flowInstances.next();
			FlowNode flowNode = new FlowNode();
			List<String> list = JSONObject.parseArray(document.get("FlowInstanceIds").toString(), String.class);
			if(list.size()>=2){
				list.remove(1);
			}
			String instanceId=document.getString("FlowInstanceId");
			Boolean status = document.getBoolean("Status");
			String flowId = document.getString("FlowId");
			flowNode.setId(document.getObjectId("_id")+"");
			flowNode.setFlowId(flowId);
			flowNode.setInstanceId(instanceId);
			flowNode.setStartTime(document.getString("BeginTime"));
			flowNode.setEndTime(document.getString("EndTime"));
			flowNode.setName(instanceId);
			String parentInstanceId="";
			if(status){
				flowNode.setState(2);//成功
			}else if(!status){
				flowNode.setState(7);//失败
			}
			if(list.size()==1 && instanceId.equals(list.get(0))){//根节点
				flowNode.setRootNode(true);
				rootNode=flowNode;
			}else if(list.size() > 1){
				flowNode.setRootNode(false);
				parentInstanceId=list.get(list.size()-2);
			}
			flowNode.setParentInstanceId(parentInstanceId);
			if(list.size() > 1){
				//同一流程实例可能有多个子节点
				List<FlowNode> flowList =null;
				if(flowNodes.get(instanceId) == null){
					flowList = new ArrayList<>();
				}else{
					flowList =(List<FlowNode>)flowNodes.get(instanceId);
				}
				flowList.add(flowNode);
				flowNodes.put(parentInstanceId, flowList);
			}
			instances.put(instanceId, flowNode);
			if(!flowNode.getRootNode()){
				((FlowNode) instances.get(flowNode.instanceId)).setChildren((List)nodes.get(flowNode.instanceId));;
				((FlowNode) instances.get(flowNode.parentInstanceId)).setChildren((List)flowNodes.get(flowNode.parentInstanceId));
			}
		}
		System.out.println(JSON.toJSONString(rootNode));
		return rootNode.getChildren();
	}
	
//	public List<Map<String,Object>> getFlowNodes(String flowInstanceId){
//		List<Map<String,Object>> list = new ArrayList<>();
//		MongoCursor<Document> nodeInstances = queryFlowNodes(flowInstanceId);
//		while (nodeInstances.hasNext()) {
//			Document document = (Document) nodeInstances.next();
//			Map<String,Object> node = new HashMap<>();
//			String nodeId = document.getString("NodeId");
//			String nodeKey= document.getString("NodeKey");
//			Boolean status = document.getBoolean("Status");
//			String nodeFlag = null;
//			if(!nodeId.endsWith(nodeKey)){
//				nodeFlag=nodeId+"_"+nodeKey;
//			}else{
//				nodeFlag=nodeId;
//			}
//			node.put(nodeFlag, status);
//			list.add(node);
//		}
//		return list;
//	}
}

