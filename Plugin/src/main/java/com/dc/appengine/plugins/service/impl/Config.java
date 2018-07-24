package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.sdk.SDKUtil;


public class Config extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Config.class);
	
	private static final String ENCODING  = "encoding";
	private String encoding;
	/*
	 * map中需要两个值，
		流程id,组件名称
		(节点属性中的一个值) 
	 */
	@Override
	public String doPreAction(){
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

	@SuppressWarnings("unchecked")
	@Override
	public String doAgent() throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		//替换配置 map key:模板路径  value:替换路径
		SDKUtil sdkUtil = new SDKUtil();
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		Map<String,String> templateMap = (Map<String, String>)messageMap.get(Constants.Plugin.CONFIGTEMPLATE);
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
		result = sdkUtil.configSdk(actualMap, actualTemplateMap,encoding);
		
		for (Map.Entry<String, String> template : actualTemplateMap.entrySet()) {
			String filePath = template.getValue();
			File file = new File(filePath);
			FileInputStream fi = null;
			FileOutputStream fo = null;
			try {
				fi = new FileInputStream(file);
				String exStr=	IOUtils.toString(fi,encoding);
				String afStr = exStr.replaceAll("WANGYB", "\\$");
				fo = new FileOutputStream(file);
				IOUtils.write(afStr, fo,encoding);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}  catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fo != null){
					fo.flush();
					fo.close();
				}
				if(fi != null){
					fi.close();
				}
			}
		}
		
		return JSON.toJSONString(result);}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(ENCODING))){
			this.encoding = this.pluginInput.get(ENCODING).toString();
		}else{
			encoding ="UTF-8";
		}
	}
	
}