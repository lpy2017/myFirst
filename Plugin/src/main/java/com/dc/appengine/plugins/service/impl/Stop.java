package com.dc.appengine.plugins.service.impl;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.command.CommandResult;
import com.dc.appengine.plugins.command.analyser.impl.EchoAnalyseer;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;


public class Stop extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Stop.class);
	private static final String STOPPATH_KEY  = "stopPath";
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	
	private String FAILRE;
	private String SUCCESSRE;
	private String stopPath;
	@Override
	public String doPreAction(){
		messageMap.put(Constants.Plugin.COMMAND_PARAMS,new ArrayList<>());
		paramMap.put("message",  messageMap);
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		String result = CommandResult.getResult(new EchoAnalyseer(),
				CommandWaitExecutor.class, false, messageMap);
		log.debug("result: " + result);
		return result;
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}

	public void setStopPath(String stopPath) {
		this.stopPath = stopPath;
	}

	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(STOPPATH_KEY ))){
			this.stopPath=this.pluginInput.get(STOPPATH_KEY ).toString();
		}
	}

	
}
