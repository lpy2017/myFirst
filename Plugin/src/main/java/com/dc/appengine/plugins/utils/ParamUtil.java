package com.dc.appengine.plugins.utils;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.service.impl.AbstractPlugin;

public class ParamUtil {
	public static final String GF_VARIABLE_KEY="gf_variable";
	public static final String ALL_KEY="all";
	
	/*
	 *通过流程变量和all标识判断是否继续执行
	 */
	public static Boolean isContinue(String param){
		return true;
//		String all=null;
//		String gf_variable=null;
//		Map<String, Object> paramMap = JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
//		});
//		Map<String,Object> messageMap = null;
//		if(!JudgeUtil.isEmpty(paramMap.get(Constants.Plugin.MESSAGE))){
//			if(paramMap.get(Constants.Plugin.MESSAGE) instanceof String){
//				messageMap =JSON.parseObject(paramMap.get(Constants.Plugin.MESSAGE).toString(), new TypeReference<Map<String, Object>>() {
//				});
//			}else{
//				messageMap = (Map<String,Object>)paramMap.get(Constants.Plugin.MESSAGE);
//			}
//		}
//		if(messageMap ==null){
//			return false;
//		}else{
//			Map<String, Object> pluginInput =(Map<String, Object>) messageMap.get(paramMap.get(Constants.Plugin.PLUGINNAME).toString());
//			if(!JudgeUtil.isEmpty(pluginInput.get(ALL_KEY))){
//				all=pluginInput.get(ALL_KEY).toString();
//			}
//			if(!JudgeUtil.isEmpty(pluginInput.get(GF_VARIABLE_KEY))){
//				gf_variable=pluginInput.get(GF_VARIABLE_KEY).toString();
//			}
//			if(all !=null && "true".equals(all)){
//				return true;
//			}else{
//				if(gf_variable !=null){
//					if(messageMap.get(gf_variable)!=null){
//						return Boolean.valueOf(messageMap.get(gf_variable).toString());
//					}else{
//						return false;
//					}
//				}else{
//					return false;
//				}
//			}
//			
//		}
	}
	
	/*
	 *通过流程变量判断插件方法执行是否成功
	 */
	public static Boolean isSuccess(String param){
		Map<String, Object> paramMap = JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
		});
		Map<String,Object> messageMap = null;
		if(!JudgeUtil.isEmpty(paramMap.get(Constants.Plugin.MESSAGE))){
			if(paramMap.get(Constants.Plugin.MESSAGE) instanceof String){
				messageMap =JSON.parseObject(paramMap.get(Constants.Plugin.MESSAGE).toString(), new TypeReference<Map<String, Object>>() {
				});
			}else{
				messageMap = (Map<String,Object>)paramMap.get(Constants.Plugin.MESSAGE);
			}
		}
		if(messageMap ==null){
			return false;
		}else{
			messageMap.get(AbstractPlugin.PLUGIN_RESULT_KEY);
			if(messageMap.get(AbstractPlugin.PLUGIN_RESULT_KEY) !=null){
				return Boolean.valueOf(messageMap.get(AbstractPlugin.PLUGIN_RESULT_KEY).toString());
			}else{
				return false;
			}
		}
	}
	
	/*
	 *判断开始节点获取消息是否成功
	 */
	public static Boolean getStartState(String info){
		Map<String, Object> paramMap = JSON.parseObject(info, new TypeReference<Map<String, Object>>(){});
		if(paramMap.get("message") == null){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void main(String[] args){
		String param="{\"workitemId\":\"2017091800040751\",\"instanceId\":\"2017091800040747\",\"insvarMap\":{},\"pluginName\":\"SaltConfig_-11\",\"endnodeId\":\"2017091400014853\",\"message\":{\"SaltConfig_-11\":{\"all\":\"true\",\"gf_variable\":\"\"},\"deployPath\":\"/srv/salt/2239b374-a37f-4ee0-8366-3298acf4143b/target\",\"logicalVersion\":\"target\",\"compURL\":\"ftp://paas:123456@10.1.108.33/srv/salt/17/target/\",\"filePath\":\"/srv/salt/2239b374-a37f-4ee0-8366-3298acf4143b/target/tomcat.zip\",\"nodeIp\":\"10.1.108.100\",\"SaltSSH_-12\":{\"all\":\"true\",\"successRE\":\"Failed:    0\",\"failRE\":\"ERROR:#Exception\",\"gf_variable\":\"\"},\"resourceVersionId\":\"e7e0b0d0-0429-44a6-8616-761b755fdb19\",\"blueprintId\":\"508e427da6b444218a86dfcfc0ac8714\",\"version\":\"v1\",\"result\":{\"result\":true},\"DownloadSaltComp_-9\":{\"all\":\"true\",\"deployPath\":\"\",\"compURL\":\"\",\"gf_variable\":\"\"},\"instanceId\":\"2239b374-a37f-4ee0-8366-3298acf4143b\",\"componentOutput\":{},\"componentInput\":{\"deployPath\":\"/home/cd/\",\"port\":\"19991\"},\"artifactURL\":\"ftp://paas:123456@10.1.108.33/packages/2c24b160-79b8-4bc2-8df6-c60acdd49fac/tomcat.zip\",\"configTemplate\":{\"/settings.ftl\":\"/settings.sls\",\"/files/server.ftl\":\"/files/server.xml\"},\"componentName\":\"tomcat-3\",\"operation\":\"deploy\",\"DownloadSaltArtifact_-8\":{\"all\":\"true\",\"deployPath\":\"\",\"artifactURL\":\"\",\"gf_variable\":\"\"}},\"parinstanceId\":\"2017091800003671\",\"parPdId\":\"2017091500015262\",\"nodeId\":\"2017091400014854\",\"blueprintId\":\"508e427da6b444218a86dfcfc0ac8714\",\"flowInstanceId\":\"-3\"}";
		
		System.out.println("isContinue: "+ParamUtil.isContinue(param));
		System.out.println("isSuccess: "+ParamUtil.isSuccess(param));
	}
}
