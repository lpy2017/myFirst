package com.dc.appengine.appmaster.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class test {

	@SuppressWarnings("unchecked")
	public static void main(String[] args){

		String componentName = "tomcat";
		String oldResVersionId = "9360147f-f398-47eb-8024-93a51db785c5";
		String resVersionId = "9360147f-f398-47eb-8024-93a51db785c5";
		String config = "";
		String resName = "";
		String info = "{'nodeDataArray': [{'text': '静态资源池','eleType': 'resource','ins': '','des': '静态资源池','source': 'img/static.png','color': '#ff916c','key': -1,'loc': '-249.59375 -381'}"+
				",{'des': 'resourceDesc','id': '22bd6b79-1262-4a67-b452-30d272e00ac2','text': 'tomcat','isGroup': true,'category': 'OfNodes','source': 'img/tomcat.png'"+
				",'eleType': 'component','versionlist': [{'ins': '2','resourceVersionId': '9360147f-f398-47eb-8024-93a51db785c5'"+
				",'versionName': 'v8','config': {'input': {'ajpPort': '18009','deployPath': '/home/tomcat8/','shutdownPort': '18005'"+
				",'startPort': '18081','version': '8.5.15'},'output': {'webappsPath': '/home/tomcat8/apache-tomcat-8.5.15/webapps/'}}}]"+
				",'key': -3,'loc': '-249.59375 -341','group': -1,'ins': 2}],'linkDataArray': []}";
		
		Map<String,Object> map = JSON.parseObject(info, new TypeReference<Map<String,Object>>(){});
		//组件列表
		List<Map<String,Object>> nodeData =  (List<Map<String, Object>>) map.get("nodeDataArray");
		for(Map<String,Object> m : nodeData){
			if(m.get("text").toString().equals(componentName)){
				//版本列表
				List<Map<String,Object>> versionList = (List<Map<String, Object>>) m.get("versionlist");
				for(Map<String,Object> v : versionList){
					//对单个版本升级时判断
					if(v.get("resourceVersionId").toString().equals(oldResVersionId)){
						if((Integer.valueOf(v.get("ins").toString())) >= 2 && 
								(!oldResVersionId.equals(resVersionId) || !v.get("config").toString().equals(config))){
							//当前版本有多余一个的实例
							Map<String,Object> newVersion = new HashMap<String, Object>();
							newVersion.put("ins",1);
							newVersion.put("resourceVersionId",resVersionId);
							newVersion.put("config",config);
							newVersion.put("versionName",resName);
							versionList.add(newVersion);
							//更新原来版本的实例个数减1
							v.put("ins", Integer.valueOf(v.get("ins").toString()) - 1);
							break;
						}else{
							//当前版本只有一个实例
							v.put("config", config);
							v.put("resourceVersionId", resVersionId);
							v.put("versionName", resName);
							break;
						}
					}
					
					//组件的全部实例都升级
					/*v.put("config", config);
					v.put("resourceVersionId", resVersionId);
					v.put("versionName", resName);*/
				}
			}
		}
		map.put("nodeDataArray", nodeData);
		String newinfo = JSON.toJSONString(map);
		System.out.println(newinfo);
	}
}
