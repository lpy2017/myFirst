package com.dc.appengine.plugins.message.callback;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.message.bean.InnerMessage;
import com.dc.appengine.plugins.service.impl.AbstractPlugin;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class Callback implements Runnable{
	private static Logger log = LoggerFactory.getLogger(LogRecord.class);
	private String op;
	private Map<String, Object> paramMap;
	private Map<String,Object> messageMap;
	private Map<String, Object> pluginInput;
	private String gf_variable=null;
	private Map<String, Object> result;
	
	public Callback(byte[] bytes){
		initFileds(new String(bytes));
	}

	@Override
	public void run() {
		Boolean result_state = (Boolean)result.get("result");
		if(gf_variable !=null){
			messageMap.put(gf_variable,result_state);
		}
		messageMap.put(AbstractPlugin.PLUGIN_RESULT_KEY,result.get(Constants.Plugin.RESULT));
		paramMap.put(Constants.Plugin.MESSAGE, messageMap);
		log.debug(LogRecord.getCurrentTime()+paramMap.get(Constants.Plugin.PLUGINNAME)+paramMap.get(Constants.Plugin.WORKITEMID).toString()+" "+"do doActive is end outputParam==="+System.lineSeparator()+JSON.toJSONString(paramMap)+System.lineSeparator()+"plugin_result_active:"+result.get(Constants.Plugin.RESULT)+" "+"result_message:"+System.lineSeparator()+result.get(Constants.Plugin.RESULT_MESSAGE));
//		System.out.println(LogRecord.getCurrentTime()+paramMap.get(Constants.Plugin.PLUGINNAME)+paramMap.get(Constants.Plugin.WORKITEMID).toString()+" "+"do doActive is end outputParam==="+System.lineSeparator()+JSON.toJSONString(paramMap)+System.lineSeparator()+"plugin_result_active:"+result.get(Constants.Plugin.RESULT)+" "+"result_message:"+System.lineSeparator()+result.get(Constants.Plugin.RESULT_MESSAGE));
		LogRecord.send2Master(paramMap.get(Constants.Plugin.WORKITEMID).toString(),
				LogRecord.getCurrentTime()+paramMap.get(Constants.Plugin.PLUGINNAME)+"do doActive is end outputParam==="+System.lineSeparator()+
				JSON.toJSONString(paramMap)+System.lineSeparator()+"plugin_result_active:"+result.get(Constants.Plugin.RESULT)+" "+"result_message:"+System.lineSeparator()+result.get(Constants.Plugin.RESULT_MESSAGE));
		//设置流程变量gf_variable，表示当前节点执行是否成功
		if(!JudgeUtil.isEmpty(gf_variable)){
			JSONObject insvarMap = (JSONObject) paramMap.get("insvarMap");
			insvarMap.put(gf_variable, result.get("result").toString());
			//set插件状态流程变量
			AbstractPlugin.setGFVariable(gf_variable, result.get("result").toString(),paramMap);
		}
		//invoke工作流的方法，触发下步操作
		AbstractPlugin.invokeWorkflowServer(paramMap);
	}
	
	@SuppressWarnings("unchecked")
	public void initFileds(String body){
		InnerMessage<Map<String,Object>> message = null;
		message = JSON.parseObject(body,new TypeReference<InnerMessage<Map<String,Object>>>(){} );
		op=message.getOp();//插件名称
		paramMap=message.getContent();
	    messageMap = (Map<String,Object>)paramMap.get("message");
		pluginInput =(Map<String, Object>) messageMap.get(op);
		if(!JudgeUtil.isEmpty(pluginInput.get(AbstractPlugin.GF_VARIABLE_KEY))){
			this.gf_variable=pluginInput.get(AbstractPlugin.GF_VARIABLE_KEY).toString();
		}
		result = JSON.parseObject(messageMap.get("result").toString(),new TypeReference<Map<String,Object>>(){} );
	}
	
	
}