package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class AppDestroy4Was extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(AppDestroy4Was.class);
	
	private static final String WASUSR = "wasUsr";
	private static final String WASPWD = "wasPwd";
	private static final String APPNAME = "appName";
	private static final String WASPATH="wasPath";
	private static final String PROFILENAME="profileName";
	private static final String JACLPATH="jaclPath";
	private static final String DESTROYJACLNAME="destroyJaclName";
	
	private String wasUsr;
	private String wasPwd;
	private String appName;
	private String wasPath;
	private String profileName;
	private String jaclPath;
	private String destroyJaclName;
	
	public String getWasUsr() {
		return wasUsr;
	}

	public void setWasUsr(String wasUsr) {
		this.wasUsr = wasUsr;
	}

	public String getWasPwd() {
		return wasPwd;
	}

	public void setWasPwd(String wasPwd) {
		this.wasPwd = wasPwd;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getWasPath() {
		return wasPath;
	}

	public void setWasPath(String wasPath) {
		this.wasPath = wasPath;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getJaclPath() {
		return jaclPath;
	}

	public void setJaclPath(String jaclPath) {
		this.jaclPath = jaclPath;
	}
    
	public String getDestroyJaclName() {
		return destroyJaclName;
	}

	public void setDestroyJaclName(String destroyJaclName) {
		this.destroyJaclName = destroyJaclName;
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
		if (JudgeUtil.isEmpty(wasUsr)) {
			log.error("wasUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wasUsr is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(wasPwd)){
			log.error("wasPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wasPwd is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(appName)){
			log.error("appName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appName is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(wasPath)){
			log.error("wasPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wasPath is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(profileName)){
			log.error("profileName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "profileName is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(jaclPath)){
			log.error("jaclPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "jaclPath is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(destroyJaclName)){
			log.error("destroyJaclName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "destroyJaclName is null");
			return JSON.toJSONString(resultMap);
		}
			JSONObject pluginParam = new JSONObject();
			this.paramMap.put("message", this.messageMap);
			this.paramMap.put("pluginName", "AppDestroy4Was");
			
			this.messageMap.put("AppDestroy4Was", pluginParam);
			pluginParam.put("CMD", wasPath+"/profiles/"+profileName+"/bin/wsadmin.sh -conntype SOAP -lang jacl -f "+jaclPath+"/"+destroyJaclName+"  -username "+wasUsr+"  -password "+wasPwd);
			
			CMD cmd = new CMD();
			cmd.initPlugin(JSON.toJSONString(this.paramMap),null);
			String result=cmd.doAgent();
		    return result;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WASUSR))) {
			this.wasUsr = this.pluginInput.get(WASUSR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WASPWD))) {
			this.wasPwd = this.pluginInput.get(WASPWD).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(APPNAME))) {
			this.appName = this.pluginInput.get(APPNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WASPATH))) {
			this.wasPath = this.pluginInput.get(WASPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(PROFILENAME))) {
			this.profileName = this.pluginInput.get(PROFILENAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(JACLPATH))) {
			this.jaclPath = this.pluginInput.get(JACLPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DESTROYJACLNAME))) {
			this.destroyJaclName = this.pluginInput.get(DESTROYJACLNAME).toString();
		}
	}
	
	 public static void main(String [] args){
		 AppDestroy4Was was=new AppDestroy4Was();
		 try {
			String s=was.doAgent();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}
