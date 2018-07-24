package com.dc.appengine.quartz.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SortUtil {

	private static Map<String, String> colunmNameMap4Quartz=new HashMap<>();
	private static Map<String, String> colunmNameMap4Job=new HashMap<>();
	static{
		/*quartz table 列名映射*/
		colunmNameMap4Quartz.put("quartzName", "quartzName");
		colunmNameMap4Quartz.put("quartzCron", "quartzCron");
		colunmNameMap4Quartz.put("updateTime", "updateTime");
		
		/*job table 列名映射*/
		colunmNameMap4Job.put("quartz_name", "quartz_name");
		colunmNameMap4Job.put("jobName", "jobName");
		colunmNameMap4Job.put("jobStatus", "jobStatus");
		
	}
	public static String getColunmName(String objectName,String sortName){
		if (!JudgeUtil.isEmpty(sortName)) {
			switch (objectName) {
			case "quartz":
				return colunmNameMap4Quartz.get(sortName);
			case "job":
				return colunmNameMap4Job.get(sortName);
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
	
	// 按照节点编号比较  
	 public int compare(Object o1, Object o2) {
		 String j1=((Map)o1).get(sortName)==null?"":((Map)o1).get(sortName).toString();
		 String j2=((Map)o2).get(sortName)==null?"":((Map)o2).get(sortName).toString();
		 int result = 0;
		 if(sortOrder.equals("DESC")){
			 result = j2.compareTo(j1);
		 }else{
			 result = j1.compareTo(j2);
		 }
		 return result;
	 }
}
