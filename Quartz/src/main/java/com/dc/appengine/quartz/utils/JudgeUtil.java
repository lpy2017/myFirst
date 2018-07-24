package com.dc.appengine.quartz.utils;

import java.util.List;
import java.util.Map;

public class JudgeUtil {
	/*
	 *该方法可以扩展，目前只判断string 和map类型的null
	 */
	public static Boolean isEmpty(Object param){
		if(param instanceof String){
			if(param ==null || "".equals(param)){
				return true;
			}else{
				return false;
			}
		}else if(param instanceof Map){
			if(param ==null || ((Map)param).isEmpty()){
				return true;
			}else{
				return false;
			}
		}else if(param instanceof List){
			if(param ==null || ((List)param).isEmpty()){
				return true;
			}else{
				return false;
			}
		}else{
			return true;
		}
	}
}
