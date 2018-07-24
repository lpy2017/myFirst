package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.sdk.SDKUtil;

public class SaltConfig extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Config.class);

	private static final String ENCODING  = "encoding";
	private String encoding;

	@Override
	public String doPreAction(){
		// TODO Auto-generated method stub
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		// TODO Auto-generated method stub
		final String json=JSON.toJSONString(paramMap);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result;
				try {
					result = doActive(json);
					resultMap = JSON.parseObject(result,new TypeReference<Map<String,Object>>(){});
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(LogRecord.getStackTrace(e));
					resultMap.put(Constants.Plugin.RESULT, false);
					resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
				}
				if(gf_variable != null){//设置方法的执行结果
					messageMap.put(gf_variable,(Boolean)resultMap.get(Constants.Plugin.RESULT));
					AbstractPlugin.setGFVariable(gf_variable, resultMap.get(Constants.Plugin.RESULT).toString(),paramMap);
				}
				messageMap.put(PLUGIN_RESULT_KEY,(Boolean)resultMap.get(Constants.Plugin.RESULT));
				//记录日志
				messageMap.put(Constants.Plugin.RESULT, JSON.toJSONString(resultMap));
				paramMap.put(Constants.Plugin.MESSAGE, messageMap);
				updatePluginLog(paramMap.get(Constants.Plugin.PLUGINNAME)+"do doActive is end outputParam==="+JSON.toJSONString(paramMap)
				+System.lineSeparator()+"plugin_result_active:"+resultMap.get(Constants.Plugin.RESULT)+System.lineSeparator()+"result_message:"+System.lineSeparator()+resultMap.get(Constants.Plugin.RESULT_MESSAGE));
				//触发工作流
				invokeWorkflowServer(paramMap);
			}
		};
		Thread t = new Thread(runnable);
		t.start();
		//		DoAgentThreadPool.getInstance().execute(runnable);
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent(){
		//配置的key中若有 ".",替换成"_"  模版文件中key值中也要对应修改为_
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		Map<String,String> templateMap = (Map<String, String>)messageMap.get(Constants.Plugin.CONFIGTEMPLATE);
		Map<String,String> actualTemplateMap = new HashMap<String, String>();
		for (Entry <String,String> entry : templateMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			key = key.substring(1, key.length());
			if (value.startsWith(".")) {
				value = value.substring(1, value.length());
				actualTemplateMap.put(deployPath + key,deployPath + value);
			}else{
				actualTemplateMap.put(deployPath + key, value);
			}
		}

		Map<String,String> Map = (Map<String,String>)messageMap.get(Constants.Plugin.COMPONENTINPUT);
		
		Map<String,Object> 	actualMap = new HashMap<String, Object>();
		
		for (Entry<String, String> entry : Map.entrySet()) {
			if (entry.getKey().contains(".")) {
				String key  = entry.getKey().replace(".", "_");
				actualMap.put(key, entry.getValue());
			}else {
				actualMap.put(entry.getKey(), entry.getValue());
			}
		}
		Map<String,Object> result= new HashMap<>();
		//替换配置 map key:模板路径  value:替换路径
		SDKUtil sdkUtil = new SDKUtil();
		result = sdkUtil.configSdk(actualMap, actualTemplateMap,encoding);
		return JSON.toJSONString(result);
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(ENCODING))){
			this.encoding = this.pluginInput.get(ENCODING).toString();
		}else{
			encoding ="UTF-8";
		}
	}
}