package com.dc.appengine.appmaster.orchestration.envs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EnvParserFactory {
	public static EnvParserFactory factory;
	//匹配$CONTAINER
	public static Pattern pattern1 = Pattern.compile
			("\\$([a-zA-Z0-9_]+)");
	//匹配${CONTAINER_IP}
	public static Pattern pattern2 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\}");
	//匹配${appName.key}
	public static Pattern pattern3 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}");
	//匹配${appName#v123#key}
	public static Pattern pattern4 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\#[V|v]([0-9]+)(?:.0)?\\#([a-zA-Z0-9_]+)\\}");
	//匹配8:00:00-12:00:00
	public static Pattern pattern5 = Pattern.compile
			("([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])");
	//匹配${appName#key}
	public static Pattern pattern6 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\#([a-zA-Z0-9_]+)\\}");
	public static Pattern pattern7 = Pattern.compile
			("\\$\\{get_input:([.a-zA-Z0-9_]+)\\}");
	
	//下面是解析短任务参数使用的正则表达式
	public static Pattern short_pattern1 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+):([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}\\[([-1-9,]+|\\*)\\]");
	public static Pattern short_pattern2 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}\\[([-1-9,]+|\\*)\\]");
	public static Pattern short_pattern3 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\.([a-zA-Z0-9_]+)\\}");
	
	//下面是docker run时解析动态环境变量时使用的正则表达式，最终任务参数和docker run会统一
	public static Pattern DOCKER_PATTERN1 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\#[V|v]([0-9]+)\\#([a-zA-Z0-9_]+)\\}");
	
	public static Pattern DOCKER_PATTERN2 = Pattern.compile
			("\\$\\{([a-zA-Z0-9_]+)\\}");
	
	public static final String VAR_CONTAINER_IP = "CONTAINER_IP";
	public static final String VAR_HOST_IP = "HOST_IP";
	public static final String VAR_ACCESS_PORT = "ACCESSPORT";
	public static final String VAR_MYID = "MYID";
	
	public static final List<Map<String,Object>> SYSTEM_ENV;
	
	static{
		SYSTEM_ENV=new ArrayList<Map<String,Object>>();
		SYSTEM_ENV.add(buildMap("var_key",VAR_CONTAINER_IP,"var_value",""));
		SYSTEM_ENV.add(buildMap("var_key",VAR_HOST_IP,"var_value",""));
		SYSTEM_ENV.add(buildMap("var_key",VAR_ACCESS_PORT,"var_value",""));
	}
	
	private static Map<String,Object> buildMap(Object...strs){
		Map<String,Object> result = new HashMap<String,Object>();
		for(int i=0;i<strs.length;i=i+2){
			String key = (String) strs[i];
			result.put(key, strs[i+1]);
		}
		return result;
	}
	
	
	

	

	public static EnvParserFactory getFactory() {
		if (factory == null) {
			synchronized (EnvParserFactory.class) {
				if (factory == null) {
					factory = new EnvParserFactory();
				}
			}
		}
		return factory;
	}
}
