package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class WasQueueDelete extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(WasQueueDelete.class);
	
	private static final String QNAME = "qName";
	
	private String qName;
	
	
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
		if(JudgeUtil.isEmpty(qName)){
			log.error("qName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "qName is null");
			return JSON.toJSONString(resultMap);
		}
		
			JSONObject pluginParam = new JSONObject();

			this.paramMap.put("message", this.messageMap);
			this.paramMap.put("pluginName", "WasQueueDelete");
			
			this.messageMap.put("WasQueueDelete", pluginParam);
			String cmdStr="su - mqm <<EOF"+System.lineSeparator()+"touch "+qName+"Del.txt"+System.lineSeparator()+"cat << EOE > "+qName+"Del.txt"+System.lineSeparator()+"delete qlocal("+qName+")"+System.lineSeparator()+
					"EOE"+System.lineSeparator()+"runmqsc < "+qName+"Del.txt"+System.lineSeparator()+"EOF";
			pluginParam.put("CMD",cmdStr);
			
			CMD cmd = new CMD();
			cmd.initPlugin(JSON.toJSONString(this.paramMap),null);
			String result=cmd.doAgent();
		    return result;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(QNAME))) {
			this.qName = this.pluginInput.get(QNAME).toString();
		}
	}
	
	 public static void main(String [] args){
		 WasQueueDelete was=new WasQueueDelete();
		 try {
			String s=was.doAgent();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}
