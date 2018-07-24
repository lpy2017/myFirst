package com.dc.appengine.appmaster.utils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SortUtil {
	private static Map<String, String> colunmNameMap4Cluster=new HashMap<>();
	private static Map<String, String> colunmNameMap4Node=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrint=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrintFlow=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrintIns=new HashMap<>();
	private static Map<String, String> colunmNameMap4Application=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrintInsCom=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrintInsComIns=new HashMap<>();
	
	private static Map<String, String> colunmNameMap4Instancelog=new HashMap<>();
	private static Map<String, String> colunmNameMap4BluePrintInsSnapShot=new HashMap<>();
	private static Map<String, String> colunmNameMap4FlowInstance=new HashMap<>();
	private static Map<String, String> colunmNameMap4Component=new HashMap<>();
	private static Map<String, String> colunmNameMap4ComponentFlow=new HashMap<>();
	
	private static Map<String, String> colunmNameMap4ComponentVersion=new HashMap<>();
	private static Map<String, String> colunmNameMap4User=new HashMap<>();
	private static Map<String, String> colunmNameMap4Role=new HashMap<>();
	private static Map<String, String> colunmNameMap4Policy=new HashMap<>();
	private static Map<String, String> colunmNameMap4Artifact=new HashMap<>();
	private static Map<String, String> colunmNameMap4Script=new HashMap<>();
	private static Map<String, String> colunmNameMap4Dashboard=new HashMap<>();
	private static Map<String, String> colunmNameMap4Audit=new HashMap<>();
	private static Map<String, String> colunmNameMap4Approve=new HashMap<>();
	private static Map<String, String> colunmNameMap4Task=new HashMap<>();
	private static Map<String, String> colunmNameMap4Bus=new HashMap<>();
	private static Map<String, String> colunmNameMap4Release=new HashMap<>();
	private static Map<String, String> colunmNameMap4ApplicationReleaseStrategy=new HashMap<>();
	static{
		/*cluster map*/
		colunmNameMap4Cluster.put("name", "name");
		/*node map*/
		colunmNameMap4Node.put("name", "name");
		colunmNameMap4Node.put("description", "description");
		colunmNameMap4Node.put("ip", "ip");
		colunmNameMap4Node.put("isConnected", "isConnected");
		colunmNameMap4Node.put("os", "os");
		/*bluePrint map*/
		colunmNameMap4BluePrint.put("NAME", "NAME");
		colunmNameMap4BluePrint.put("DESC", "DESC");
		colunmNameMap4BluePrint.put("CREATE_TIME", "CREATE_TIME");
		colunmNameMap4BluePrint.put("UPDATE_TIME", "UPDATE_TIME");
		/*bluePrintFlow map*/
		colunmNameMap4BluePrintFlow.put("flowName", "FLOW_NAME");
		/* 蓝图实例表名映射*/
		colunmNameMap4BluePrintIns.put("INSTANCE_NAME", "INSTANCE_NAME");
		colunmNameMap4BluePrintIns.put("TEMPLATE_NAME", "TEMPLATE_NAME");
		colunmNameMap4BluePrintIns.put("INSTANCE_DESC", "INSTANCE_DESC");
		colunmNameMap4BluePrintIns.put("NAME", "NAME");

		/* 蓝图实例组件表名映射*/
		colunmNameMap4Application.put("APP_NAME", "APP_NAME");
		colunmNameMap4Application.put("COMPONENT_NAME", "COMPONENT_NAME");
		colunmNameMap4Application.put("NODE_NAME", "NODE_NAME");
		colunmNameMap4Application.put("INSTANCE_NAME", "INSTANCE_NAME");
		colunmNameMap4Application.put("IP", "IP");
		colunmNameMap4Application.put("HOSTNAME", "HOSTNAME");
		
		/* blueprint component列表名映射*/
		colunmNameMap4BluePrintInsCom.put("nodeDisplay", "nodeDisplay");
		colunmNameMap4BluePrintInsCom.put("clusterName", "clusterName");
		colunmNameMap4BluePrintInsCom.put("instancesNumber", "instancesNumber");
		
		/* blueprint component实例列表，列名映射*/
		colunmNameMap4BluePrintInsComIns.put("id", "id");
		colunmNameMap4BluePrintInsComIns.put("status", "status");
		colunmNameMap4BluePrintInsComIns.put("nodeIp", "nodeIp");
		colunmNameMap4BluePrintInsComIns.put("deployTime", "deployTime");
		/* instance log 列名映射*/
		colunmNameMap4Instancelog.put("instanceId", "instanceId");
		colunmNameMap4Instancelog.put("flowState", "flowState");
		colunmNameMap4Instancelog.put("startTime", "startTime");
		colunmNameMap4Instancelog.put("endTime", "endTime");
		colunmNameMap4Instancelog.put("opTime", "opTime");
		/* blueprint instance snapShot 列名映射*/
		colunmNameMap4BluePrintInsSnapShot.put("snapshotName", "snapshotName");
		
		/* ma_flowinstance 列名映射*/
		colunmNameMap4FlowInstance.put("startTime", "startTime");
		colunmNameMap4FlowInstance.put("blueprintInstanceName", "blueprintInstanceName");
		colunmNameMap4FlowInstance.put("flowName", "flowName");
		colunmNameMap4FlowInstance.put("clusterName", "clusterName");
		colunmNameMap4FlowInstance.put("userName", "userName");
		
		/* 组件资源 列名映射*/
		colunmNameMap4Component.put("resourceName", "resourceName");
		/* 组件過程 列名映射*/
		colunmNameMap4ComponentFlow.put("flowName", "flowName");
		/* 组件版本列表 列名映射*/
		colunmNameMap4ComponentVersion.put("status", "STATUS");
		colunmNameMap4ComponentVersion.put("updateTime", "UPDATE_TIME");
		colunmNameMap4ComponentVersion.put("versionDesc", "VERSION_DESC");
		colunmNameMap4ComponentVersion.put("versionName", "VERSION_NAME");
		colunmNameMap4ComponentVersion.put("versionNum", "VERSIONNUM");
		/*user table*/
		colunmNameMap4User.put("userName", "userName");
		colunmNameMap4User.put("roleName", "roleName");
		/*role table*/
		colunmNameMap4Role.put("name", "name");
		/**/
		colunmNameMap4Policy.put("pText", "pText");
		colunmNameMap4Policy.put("text", "text");
		colunmNameMap4Policy.put("type", "type");
		colunmNameMap4Policy.put("sref", "sref");
		
		/*Artifact table*/
		colunmNameMap4Artifact.put("resourceName", "resourceName");
		colunmNameMap4Artifact.put("resourceDesc", "resourceDesc");
		colunmNameMap4Artifact.put("resourcePath", "resourcePath");
		colunmNameMap4Artifact.put("resourceSize", "resourceSize");
		colunmNameMap4Artifact.put("updateTime", "updateTime");
		
		/*Script table*/
		colunmNameMap4Script.put("resourceName", "resourceName");
		colunmNameMap4Script.put("resourceDesc", "resourceDesc");
		colunmNameMap4Script.put("absolutePath", "resourcePath");
		colunmNameMap4Script.put("updateTime", "updateTime");
		/*Dashbord */
		colunmNameMap4Dashboard.put("blueInstanceName", "blueInstanceName");
		colunmNameMap4Dashboard.put("flowName", "flowName");
		colunmNameMap4Dashboard.put("nodeDisplay", "nodeDisplay");
		colunmNameMap4Dashboard.put("subFlowName", "subFlowName");
		colunmNameMap4Dashboard.put("op", "op");
		colunmNameMap4Dashboard.put("startTime", "startTime");
		colunmNameMap4Dashboard.put("endTime", "endTime");
		colunmNameMap4Dashboard.put("period", "period");
		colunmNameMap4Dashboard.put("result", "result");
		
		/*audit */
		colunmNameMap4Audit.put("userId", "userId");
		colunmNameMap4Audit.put("resourceType", "resourceType");
		colunmNameMap4Audit.put("resourceName", "resourceName");
		colunmNameMap4Audit.put("operateType", "operateType");
		colunmNameMap4Audit.put("operateResult", "operateResult");
		colunmNameMap4Audit.put("operateTime", "operateTime");
		colunmNameMap4Audit.put("detail", "detail");
		
		/*approve*/
		colunmNameMap4Approve.put("taskName", "NAME");
		colunmNameMap4Approve.put("applyTime", "APPLY_TIME");
		colunmNameMap4Approve.put("approveTime", "APPROVE_TIME");
		colunmNameMap4Approve.put("status", "STATUS");
		colunmNameMap4Approve.put("startTime", "START_TIME");
		
		/*task*/
		colunmNameMap4Task.put("initiator", "INITIATOR");
		colunmNameMap4Task.put("taskName", "NAME");
		colunmNameMap4Task.put("busName", "BUS_NAME");
		colunmNameMap4Task.put("startTime", "START_TIME");
		colunmNameMap4Task.put("endTime", "END_TIME");
		colunmNameMap4Task.put("status", "STATUS");
		
		/*bus*/
		colunmNameMap4Bus.put("name", "NAME");
		colunmNameMap4Bus.put("stopTime", "STOP_TIME");
		colunmNameMap4Bus.put("startTime", "START_TIME");
		/*release*/
		colunmNameMap4Release.put("releaseName", "releaseName");
		colunmNameMap4Release.put("description", "description");
		colunmNameMap4Release.put("lifecycleName", "lifecycleName");
		colunmNameMap4Release.put("appCount", "appCount");
		colunmNameMap4Release.put("updateTime", "updateTime");
		
		// ApplicationReleaseStrategy
		colunmNameMap4ApplicationReleaseStrategy.put("name", "name");
		colunmNameMap4ApplicationReleaseStrategy.put("app_name", "app_name");
		colunmNameMap4ApplicationReleaseStrategy.put("task_status", "task_status");
	}
	public static String getColunmName(String objectName,String sortName){
		if (!JudgeUtil.isEmpty(sortName)) {
			switch (objectName) {
			case "cluster":
				return colunmNameMap4Cluster.get(sortName);
			case "node":
				return colunmNameMap4Node.get(sortName);
			case "bluePrint":
				return colunmNameMap4BluePrint.get(sortName);
			case "bluePrintFlow":
				return colunmNameMap4BluePrintFlow.get(sortName);
			case "bluePrintIns":
				return colunmNameMap4BluePrintIns.get(sortName);
			case "application":
				return colunmNameMap4Application.get(sortName);
			case "bluePrintInsCom":
				return colunmNameMap4BluePrintInsCom.get(sortName);
			case "bluePrintInsComIns":
				return colunmNameMap4BluePrintInsComIns.get(sortName);
			case "instancelog":
				return colunmNameMap4Instancelog.get(sortName);
			case "bluePrintInsSnapShot":
				return colunmNameMap4BluePrintInsSnapShot.get(sortName);
			case "flowInstance":
				return colunmNameMap4FlowInstance.get(sortName);
			case "component":
				return colunmNameMap4Component.get(sortName);
			case "componentFlow":
				return colunmNameMap4ComponentFlow.get(sortName);
			case "componentVersion":
				return colunmNameMap4ComponentVersion.get(sortName);
			case "user":
				return colunmNameMap4User.get(sortName);
			case "role":
				return colunmNameMap4Role.get(sortName);
			case "policy":
				return colunmNameMap4Policy.get(sortName);
			case "artifact":
				return colunmNameMap4Artifact.get(sortName);
			case "script":
				return colunmNameMap4Script.get(sortName);
			case "dashboard":
				return colunmNameMap4Dashboard.get(sortName);
			case "audit":
				return colunmNameMap4Audit.get(sortName);
			case "approve":
				return colunmNameMap4Approve.get(sortName);
			case "task":
				return colunmNameMap4Task.get(sortName);
			case "bus":
				return colunmNameMap4Bus.get(sortName);
			case "release":
				return colunmNameMap4Release.get(sortName);
			case "ApplicationReleaseStrategy":
				return colunmNameMap4ApplicationReleaseStrategy.get(sortName);
			default:
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	public static void main(String[]args){
		String colunmName =SortUtil.getColunmName("cluster", "name");
		System.out.println(colunmName);
	}
	
	public static void sort(List list,String sortName,String sortOrder){
		if(!JudgeUtil.isEmpty(list)&&!JudgeUtil.isEmpty(sortName)&&!JudgeUtil.isEmpty(sortOrder)&&judgeSortOrder(sortOrder)){
			Collections.sort(list, new NodeIDComparator(sortName,sortOrder));
		}
	}
	public static void sort(List list){
		Collections.sort(list, new StringComparator());
	}
	public static Boolean judgeSortOrder(Object sortOrder){
		if(!JudgeUtil.isEmpty(sortOrder)&&("DESC".equals(sortOrder)||"ASC".equals(sortOrder))){
			return true;
		}else{
			return false;
		}
	}
	
}

class NodeIDComparator implements Comparator {
	String sortOrder="DESC";
	String sortName="";
	public NodeIDComparator(String sortName,String sortOrder){
		this.sortName=sortName;
		this.sortOrder = sortOrder;
	}
	
	// 按照节点编号比较  ,O1和O2参数的类型需要一致，若不一致，需要定制
	 public int compare(Object o1, Object o2) {
		 int result = 0;
		 if("opTime".equals(sortName)){//流程引擎返回的消息，经过JSON工具处理后，opTime字段对应的值的类型会不一致，例如0->integer,1.23->BigDecimal
			 Float j1=((Map)o1).get(sortName)==null?0:Float.valueOf(((Map)o1).get(sortName).toString());
			 Float j2=((Map)o2).get(sortName)==null?0:Float.valueOf(((Map)o2).get(sortName).toString());
			 if(sortOrder.equals("DESC")){
				 result = j2.compareTo(j1);
			 }else{
				 result = j1.compareTo(j2);
			 }
		 }else{
			 if(((Map)o1).get(sortName) instanceof Long){
				 Long j1=((Map)o1).get(sortName)==null?0:Long.valueOf(((Map)o1).get(sortName).toString());
				 Long j2=((Map)o2).get(sortName)==null?0:Long.valueOf(((Map)o2).get(sortName).toString());
				 if(sortOrder.equals("DESC")){
					 result = j2.compareTo(j1);
				 }else{
					 result = j1.compareTo(j2);
				 }
			 }else if(((Map)o1).get(sortName) instanceof BigDecimal){
				 Float j1=((Map)o1).get(sortName)==null?0:Float.valueOf(((Map)o1).get(sortName).toString());
				 Float j2=((Map)o2).get(sortName)==null?0:Float.valueOf(((Map)o2).get(sortName).toString());
				 if(sortOrder.equals("DESC")){
					 result = j2.compareTo(j1);
				 }else{
					 result = j1.compareTo(j2);
				 }
			 }else if(((Map)o1).get(sortName) instanceof Float){
				 Float j1=((Map)o1).get(sortName)==null?0:Float.valueOf(((Map)o1).get(sortName).toString());
				 Float j2=((Map)o2).get(sortName)==null?0:Float.valueOf(((Map)o2).get(sortName).toString());
				 if(sortOrder.equals("DESC")){
					 result = j2.compareTo(j1);
				 }else{
					 result = j1.compareTo(j2);
				 }
			 } else if(((Map)o1).get(sortName) instanceof Integer){
				 Integer j1=((Map)o1).get(sortName)==null?0:Integer.valueOf(((Map)o1).get(sortName).toString());
				 Integer j2=((Map)o2).get(sortName)==null?0:Integer.valueOf(((Map)o2).get(sortName).toString());
				 if(sortOrder.equals("DESC")){
					 result = j2.compareTo(j1);
				 }else{
					 result = j1.compareTo(j2);
				 }
			 }else{
				 String j1=((Map)o1).get(sortName)==null?"":((Map)o1).get(sortName).toString();
				 String j2=((Map)o2).get(sortName)==null?"":((Map)o2).get(sortName).toString();
				 if(sortOrder.equals("DESC")){
					 result = j2.compareTo(j1);
				 }else{
					 result = j1.compareTo(j2);
				 }
			 }
		 }
		 return result;
	 }
}

class StringComparator implements Comparator {
	// 按照节点编号比较  
	 public int compare(Object o1, Object o2) {
		 String j1=(String)o1;
		 String j2=(String)o2;
		 int result = j1.compareTo(j2);
		 return result;
	 }
}
