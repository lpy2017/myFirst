package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.LogRecord;

public class GetCompTargetVersion extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(GetCompTargetVersion.class);
	public String preAction(String param) throws Exception {
		try {
			Boolean result=false;
			Map<String,Object> map= JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
			});
			Map<String,Object> message = (Map<String, Object>)map.get("message");
			if((Map<String, Object>) message.get(Constants.Plugin.TargetVersion_Key) !=null){
				message.putAll((Map<String, Object>) message.get(Constants.Plugin.TargetVersion_Key));
				map.put(Constants.Plugin.MESSAGE, message);
				result=true;
			}
			super.initPlugin(JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect),Constants.Plugin.PHRASE_PREACTION);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do preAction inputParam==="+System.lineSeparator()+param);
			if(gf_variable != null){
				this.messageMap.put(gf_variable,result);
			}
			this.messageMap.put(PLUGIN_RESULT_KEY,result);
			this.paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is end  outputParam==="+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_preAction:"+result);
			return JSON.toJSONString(this.paramMap);
		
		} catch (Exception e) {
			if(gf_variable != null){
				messageMap.put(gf_variable,false);
			}
			messageMap.put(PLUGIN_RESULT_KEY,false);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
			log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+e.getMessage());
			e.printStackTrace();
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
			+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_preAction:"+false);
			return JSON.toJSONString(this.paramMap);
		}
	}
	
	
	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		final String json = JSON.toJSONString(paramMap);
		Runnable runnable= new Runnable() {
			@Override
			public void run() {
				resultMap.put(Constants.Plugin.RESULT, true);
				resultMap.put(Constants.Plugin.MESSAGE, "获取目标版本"+Constants.Plugin.TargetVersion_Key+"信息，成功！");
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
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String doAgent() throws Exception {
		Map<String, Object> result = new HashMap<>();
		return JSON.toJSONString(result);
	}

	@Override
	public void setFields() {

	}
}
