package com.dc.appengine.plugins.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParseUtil {
	public static String p1="\\$\\{([a-zA-Z0-9_#\\.]+)\\}";//${deployPath},${tomcat_-1#deployPath},${blueprint#deployPath}
	public static String p2="\\$\\{([a-zA-Z0-9_\\.]+)\\}";//${deployPath}
	public static String p3="\\$\\{([a-zA-Z0-9_\\.]+)#([a-zA-Z0-9_\\.]+)\\}";//${tomcat_-1#deployPath}
	public static String p4="\\$\\{blueprint#([a-zA-Z0-9_\\.]+)\\}";//${blueprint#deployPath}
	
	
	public static String parseFromFlow(Map<String,Object> paramMap,String target){
		String result=target;
		Pattern p = Pattern.compile(p3);
		  Matcher mat = p.matcher(target);
		  List<String> group=new ArrayList<String>();
		  while(mat.find()){
			  group.add(mat.group());
		  }
		if(group.size()==0){
			return result;
		}else{
			for(String one:group){
				int indexF = one.indexOf("${");
				int indexL = one.lastIndexOf("}");
				String oneNew = one.substring(indexF+2, indexL);
				String[] deps = oneNew.split("#");
				if(deps !=null && deps.length>1){
					String componentName=deps[0];
					String cnKey=deps[1];
					//"\\$\\{([a-zA-Z0-9_]+)#([a-zA-Z0-9_]+)\\}",优先查找message，message不存在，则查找componentInput
					if(paramMap.containsKey(cnKey)){
						Object cnMessage = paramMap.get(componentName);
						Map<String,Object> cnMap =null;
						if(cnMessage instanceof Map){
							cnMap = (Map<String,Object>)cnMessage;
						}
						else if(cnMessage instanceof List){//多实例聚合，目前没有这种场景
							List<Map<String,Object>> cnList = (List<Map<String, Object>>) cnMessage;
							cnMap = cnList.get(0);
						}
						if(cnMap !=null){
							Map<String, Object> componentInput = (Map<String, Object>) cnMap.get("componentInput");
							if(cnMap.containsKey(cnKey)){//${mysql_-1#ip},例如DB依赖mysql的ip，ip存放在message的根下
								String tmpValue=cnMap.get(cnKey).toString();
								if(!JudgeUtil.isEmpty(tmpValue.toString())){
									result=result.replace(one, cnMap.get(cnKey).toString());
								}
							}else{
								Object cnValue=componentInput.get(cnKey);
								if(cnValue!=null && !"".equals(cnValue.toString())){
									String value = cnValue.toString();
									result=result.replace(one, value);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public static String parseFromBluePrintConfig(Map<String,Object> blueprintMap,String target){
		String result=target;
		Pattern p = Pattern.compile(p4);
		  Matcher mat = p.matcher(target);
		  List<String> group=new ArrayList<String>();
		  while(mat.find()){
			  group.add(mat.group());
		  }
		if(group.size()==0){
			return result;
		}else{
			for(String one:group){
				int indexF = one.indexOf("${");
				int indexL = one.lastIndexOf("}");
				String oneNew = one.substring(indexF+2, indexL);
				String[] deps = oneNew.split("#");
				if(deps !=null && deps.length>1){
					String blueprint=deps[0];
					String bpKey=deps[1];
					if(blueprintMap.containsKey(bpKey)){
						Object bpValue=blueprintMap.get(bpKey);
						if(bpValue!=null && !"".equals(bpValue.toString())){
							result=result.replace(one, bpValue.toString().toString());
						}
					}
				}
			}
		}
		return result;
		
	}
	
	public static String parseFromMap(Map<String,Object> from,String target){
		String result=target;
		Pattern p = Pattern.compile(p2);
		  Matcher mat = p.matcher(target);
		  List<String> group=new ArrayList<String>();
		  while(mat.find()){
			  group.add(mat.group());
		  }
		if(group.size()==0){
			return result;
		}else{
			for(String one:group){
				int indexF = one.indexOf("${");
				int indexL = one.lastIndexOf("}");
				String oneNew = one.substring(indexF+2, indexL);
				for(Map.Entry<String, Object> entry:from.entrySet()){
					String key=entry.getKey();
					Object value=entry.getValue();
					if(oneNew.equals(key)&& value!=null && !"".equals(value.toString())){
						result=result.replace(one, value.toString());	
					}
				}
			}
		}
		return result;
	}
	public static void main (String[] args){
//		Map<String, Object> message = new HashMap<String, Object>();
//		  Map<String, Object> pluginInput = new HashMap<String, Object>();
//		  Map<String, String> componentInput = new HashMap<String, String>();
//		  message.put("operation", "deploy");
//		  message.put("instanceId", "2345sssss444444");
//		  message.put("nodeIp", "10.1.108.126");
//		  message.put("resouceUrl", "ftp://admin@123456:/upload/tomcat.zip");
//		  message.put("deployPath", "");
//		  message.put("componentName", "tomcat");
//		  message.put("configUrl", "http://127.0.0.1:5091/masterl");
//		  pluginInput.put("path", "${deployPath}");
//		  componentInput.put("deployPath", "${componentName}/${instanceId}");
//		  componentInput.put("startPath", "${deployPath}/tomcat/bin/startup.sh");
//		  componentInput.put("stopPath", "${deployPath}/tomcat/bin/shutdown.sh");
//		  componentInput.put("destroyPath", "${deployPath}/tomcat");
//		  message.put("componentInput", componentInput);
//		  message.put("pluginInput", pluginInput);
//		 String deployPath=componentInput.get("deployPath");
//		 System.out.println(deployPath);
//		 Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");
//		  Matcher mat = p.matcher(deployPath);
//		  List<String> group=new ArrayList<String>();
//		  while(mat.find()){
//			  group.add(mat.group());
//		  }
//		 String result = JsonParseUtil.parseFromMessage(message, deployPath);
//		 JsonParseUtil.parseFromFlow(message, null);
		Pattern p1 = Pattern.compile(JsonParseUtil.p1);//存在变量
		Matcher mat1 = p1.matcher("${deploy.pa.th}");
		if(mat1.find()){
			System.out.println("匹配成功");
		}else{
			System.out.println("匹配失败");
		}
	}
}
