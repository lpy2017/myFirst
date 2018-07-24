package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class WasQueueCreate extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(WasQueueCreate.class);
	
	private static final String QMNAME = "qmName";
	private static final String QNAME = "qName";
	
	private String qmName;
	private String qName;
	
	public String getQmName() {
		return qmName;
	}

	public void setQmName(String qmName) {
		this.qmName = qmName;
	}

	public String getqName() {
		return qName;
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() throws Exception {
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		if (JudgeUtil.isEmpty(qmName)) {
			log.error("qmName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "qmName is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(qName)){
			log.error("qName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "qName is null");
			return JSON.toJSONString(resultMap);
		}
		
			JSONObject pluginParam = new JSONObject();
			this.paramMap.put("message", this.messageMap);
			this.paramMap.put("pluginName", "WasQueueCreate");
			
			this.messageMap.put("WasQueueCreate", pluginParam);
			
			String cmdStr ="su - mqm <<EOF"+System.lineSeparator()+"crtmqm -q "+qmName+System.lineSeparator()+"strmqm "+qmName+System.lineSeparator()+"touch "+qmName+".txt"+System.lineSeparator()+"echo 'def ql("+qName+")'>>"+qmName+".txt"+System.lineSeparator()+"runmqsc < "+qmName+".txt"; 
			pluginParam.put("CMD",cmdStr);
			
			CMD cmd = new CMD();
			cmd.initPlugin(JSON.toJSONString(this.paramMap),null);
			String result=cmd.doAgent();
		    return result;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(QMNAME))) {
			this.qmName = this.pluginInput.get(QMNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(QNAME))) {
			this.qName = this.pluginInput.get(QNAME).toString();
		}
	}
	
	 public static void main(String [] args){
		 WasQueueCreate was=new WasQueueCreate();
		 try {
			String s=was.doAgent();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}
