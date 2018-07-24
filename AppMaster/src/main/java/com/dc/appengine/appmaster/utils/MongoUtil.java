package com.dc.appengine.appmaster.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MongoUtil {
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
	private static MongoLog mongoLog=null;
	public static MongoLog getInstance(){
		synchronized (MongoUtil.class) {
			if(mongoLog==null){
				mongoLog = new MongoLog();
			}
			return mongoLog;
		}
	}
	
	public static void main(String [] args){
		String flowInstanceId ="0b6dfc89-8bf2-4dce-888c-14c428918791";
		MongoLog mongoLog = MongoUtil.getInstance();
		MongoUtil mongoUtil = new MongoUtil();
//		mongoUtil.test1();
//		mongoUtil.test2();
//		mongoLog.queryFlowInstances("4d80588a-d0fd-4e38-b4ed-7c5e2ba1f468");
//		mongoUtil.test4();
		mongoUtil.test5();

	}

	public void test1(){
		String flowEngineId="2017101700008924";
		JSONObject logMsg=new JSONObject();
		JSONArray fid = new JSONArray();
		fid.add("4d80588a-d0fd-4e38-b4ed-7c5e2ba1f468");
		logMsg.put("bluePrintInstanceId", "eaa0db1d05d0405a97f0a4bae36cc8ee");
		logMsg.put("FlowInstanceIds", fid.toString());
		logMsg.put("FlowInstanceId", fid.get(fid.size()-1));
		logMsg.put("FlowId", flowEngineId);
		logMsg.put("FlowcontrolType", -88888888);
		logMsg.put("FlowType", "");
		logMsg.put("SubflowName", "");
		logMsg.put("TextKey", "");
		logMsg.put("NodeKey", -88888888);
		logMsg.put("BeginTime", sdf.format(new Date()));
		logMsg.put("EndTime", sdf.format(new Date()));
		logMsg.put("Status",true);
		MongoLog mongoLog = MongoUtil.getInstance();
		mongoLog.logInsertInstanceStatusMsg(logMsg);
	}
	
	public void test2(){
		String flowEngineId="20171017141754751";
		JSONObject logMsg=new JSONObject();
		JSONArray fid = new JSONArray();
		fid.add("4d80588a-d0fd-4e38-b4ed-7c5e2ba1f468");
		fid.add("9d3d2378-07ae-40e9-b2d8-586169a3f70a");
		fid.add("2775e561-3e57-43f3-bbe8-0502aecfd237");
		logMsg.put("FlowInstanceIds", fid.toString());
		logMsg.put("FlowInstanceId", fid.get(fid.size()-1));
		logMsg.put("FlowId", flowEngineId);
		logMsg.put("FlowcontrolType", -88888888);
		logMsg.put("FlowType", "");
		logMsg.put("SubflowName", "");
		logMsg.put("TextKey", "");
		logMsg.put("NodeKey", -88888888);
		logMsg.put("BeginTime", sdf.format(new Date()));
		logMsg.put("EndTime", sdf.format(new Date()));
		logMsg.put("Status",true);
		MongoLog mongoLog = MongoUtil.getInstance();
		mongoLog.logInsertInstanceStatusMsg(logMsg);
	}
	
	public void test3(){
		String bluePrintInstanceId="eaa0db1d05d0405a97f0a4bae36cc8ee";
		String flowId="2017101700008924";
		int pageNum=1;
		int pageSize=10;
		System.out.println(mongoLog.queryFlowInstancesByPage(bluePrintInstanceId, flowId, pageNum, pageSize));
	}
	
	public void test4(){
		MongoLog mongoLog = MongoUtil.getInstance();
		String flowInstanceId="4d80588a-d0fd-4e38-b4ed-7c5e2ba1f468";
		String rootFlowId="eaa0db1d05d0405a97f0a4bae36cc8ee";
		mongoLog.generateFlowTree(flowInstanceId, rootFlowId);
	}
	
	public void test5(){
		MongoLog mongoLog = MongoUtil.getInstance();
		String nodeId="59f31e203965c45e8cbdab9c";
		mongoLog.queryFlowInstance(nodeId);
	}
}

