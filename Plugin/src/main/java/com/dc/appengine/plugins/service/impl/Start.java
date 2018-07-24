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


public class Start extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Start.class);
	private static final String STARTPATH_KEY  = "startPath";
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	
	private String FAILRE;
	private String SUCCESSRE;
	private String startPath;
	/*
	 * map中需要两个值，
		流程id,组件名称
		(节点属性中的一个值) 
	 */
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

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(STARTPATH_KEY ))){
			this.startPath=this.pluginInput.get(STARTPATH_KEY ).toString();
		}
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}

	public void setStartPath(String startPath) {
		this.startPath = startPath;
	}
	
}
