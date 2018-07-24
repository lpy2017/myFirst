package com.dc.appengine.appmaster.utils;

import java.util.HashMap;
import java.util.Map;

public class CreateComponentFile {
	
	public static Map<String,Object> commonMap(String instanceId){
		Map<String,Object> commonMap  = new  HashMap<String,Object>();
		commonMap.put("instanceId",instanceId);
		//tomcat
		
		commonMap.put("catalina_base","${catalina.base}");
		commonMap.put("CATALINA_HOME","${CATALINA_HOME}");
		commonMap.put("CATALINA_BASE","${CATALINA_BASE}");
		commonMap.put("TOMCAT_JAVA_HOME","${TOMCAT_JAVA_HOME}");
		commonMap.put("ERRFILE","${ERRFILE}");
		commonMap.put("OUTFILE","${OUTFILE}");
		commonMap.put("CATALINA_OPTS","${CATALINA_OPTS}");
		commonMap.put("JAVA_OPTS","${JAVA_OPTS}");
		commonMap.put("JAVA_HOME","$JAVA_HOME");
		commonMap.put("PATH","$PATH");
		commonMap.put("releasever","$releasever");
		commonMap.put("uri","$uri");
		commonMap.put("MAINPID","$MAINPID");

		
		//zookeeper
		commonMap.put("SERVER_SCRIPT","${SERVER_SCRIPT}");
		commonMap.put("ZOOPIDFILE","${ZOOPIDFILE}");
		commonMap.put("DESC","${DESC}");

	
		//mysql-cluster
		commonMap.put("logfile", "$logfile");
		commonMap.put("logpos", "$logpos");
		commonMap.put("ip", "$ip");
		
		//poc_scene3_newApp
		commonMap.put("TERM","${TERM:-$TERM_DEFAULT}");
		
		return commonMap;
	}
	



}
