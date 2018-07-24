package com.dc.appengine.appsvn.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class MessageUtil {
	public static String mkBooleanMessage(boolean success,String message){
		 Map<String,Object> result= new HashMap<String,Object>(); 
		 result.put("result",success);
		 if(message!=null && !"".equals(message)){
			 result.put("msg",message);
		 }
		return JSON.toJSONString(result);
	}
	public static String mkMessage(String ... params){
		Map<String,String> result= new HashMap<String,String>(); 
		int length=params.length;
		if(length%2==1){
			return null;
		}
		for(int i=0;i<params.length;i+=2){
			String key=params[i];
			String value=params[i+1];
			result.put(key, value);
		}
		return JSON.toJSONString(result);
	}
	public static void main(String[] args) {
		System.out.println(mkBooleanMessage(true,"aaa"));
	}
}
