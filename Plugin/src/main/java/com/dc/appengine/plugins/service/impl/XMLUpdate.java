package com.dc.appengine.plugins.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.XMLUpdateUtil;


public class XMLUpdate extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Config.class);
	//xml方法的key
	private static final String XML_KEY = "XMLUpdate";
	//XML插件执行成功或失败标识的key
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";

	private String FAILRE;
	private String SUCCESSRE;

	private String optsJson;
	
	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws IOException, InterruptedException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, DocumentException, Exception, SecurityException{
		Map<String,Object> resultXml= new HashMap<>();
		if(JudgeUtil.isEmpty(this.optsJson)) {
			resultXml.put(Constants.Plugin.RESULT,false);
			resultXml.put(Constants.Plugin.MESSAGE,"没提供操作数据");
		}else {
			List<Map> list = JSON.parseArray(this.optsJson, Map.class);
			
			Class<?> xmlUtilClass = Class.forName("com.dc.appengine.plugins.utils.XMLUpdateUtil");
			Method method = null;
			Map<String, Method> methodPool = new HashMap<>();
			methodPool.put("addComment", xmlUtilClass.getDeclaredMethod("addComment",Document.class, String.class,String.class));
			methodPool.put("editComment", xmlUtilClass.getDeclaredMethod("editComment",Document.class, String.class,String.class));
			methodPool.put("removeComment", xmlUtilClass.getDeclaredMethod("removeComment",Document.class, String.class));
			methodPool.put("addNode", xmlUtilClass.getDeclaredMethod("addNode",Document.class, String.class,String.class));
			methodPool.put("editNodeName", xmlUtilClass.getDeclaredMethod("editNodeName",Document.class, String.class,String.class));
			methodPool.put("removeNode", xmlUtilClass.getDeclaredMethod("removeNode",Document.class, String.class));
			methodPool.put("addAttribute", xmlUtilClass.getDeclaredMethod("addAttribute",Document.class, String.class,String.class,String.class));
			methodPool.put("editAttributeName", xmlUtilClass.getDeclaredMethod("editAttributeName",Document.class, String.class,String.class,String.class));
			methodPool.put("editAttributeValue", xmlUtilClass.getDeclaredMethod("editAttributeValue",Document.class, String.class,String.class));
			methodPool.put("removeAttribute", xmlUtilClass.getDeclaredMethod("removeAttribute",Document.class, String.class));
			methodPool.put("addText", xmlUtilClass.getDeclaredMethod("addText",Document.class, String.class,String.class));
			methodPool.put("editText", xmlUtilClass.getDeclaredMethod("editText",Document.class, String.class,String.class));
			methodPool.put("removeText", xmlUtilClass.getDeclaredMethod("removeText",Document.class, String.class));
			
			Map<String,Object> fileMap;	
			String filePath = "";
			String encoding = "";
			boolean isCheckDTD = false;
			List<String> strList;
			String params = "";
			String[] paramArr ;
			Document doc;
			for (int i = 0; i < list.size(); i++) {
				fileMap = list.get(i);
				filePath = (String) fileMap.get("file");
				fileMap.remove("file");
				encoding = (String) fileMap.get("encoding");
				fileMap.remove("encoding");
				isCheckDTD = Boolean.parseBoolean((String) fileMap.get("checkDTD"));
				fileMap.remove("checkDTD");
				
				doc = XMLUpdateUtil.getDocument(filePath,isCheckDTD);
				Set<String> keySet = fileMap.keySet();
				for (String key : keySet) {
					method = methodPool.get(key);
					strList = (List<String>) fileMap.get(key);
					for (int j = 0; j < strList.size(); j++) {
						params = strList.get(j);
						paramArr = params.split("->");
						Object[] paramHandle = new Object[paramArr.length+1];
						paramHandle[0] = doc;
						for (int k = 0; k < paramArr.length; k++) {
							paramHandle[k+1] = paramArr[k];
							
						}
						
						method.invoke(null,paramHandle);
					}
				}
				XMLUpdateUtil.writeBack(doc, filePath, encoding);
			}
			
			resultXml.put(Constants.Plugin.RESULT,true);
			resultXml.put(Constants.Plugin.MESSAGE,"操作成功！");
		}
		
		return JSON.toJSONString(resultXml);
	}


	public String getFAILRE() {
		return FAILRE;
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public String getSUCCESSRE() {
		return SUCCESSRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}

	public String getOptsJspn() {
		return optsJson;
	}

	public void setOptsJspn(String optsJspn) {
		this.optsJson = optsJspn;
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get("optsJson" ))){
			this.optsJson=this.pluginInput.get("optsJson" ).toString();
		}
	
		
	}
	
}