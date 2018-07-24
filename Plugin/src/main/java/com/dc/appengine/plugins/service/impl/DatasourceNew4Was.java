package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class DatasourceNew4Was extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(DatasourceNew4Was.class);
	
	private static final String WASUSR = "wasUsr";
	private static final String WASPWD = "wasPwd";
	private static final String APPNAME = "appName";
	private static final String APPPATH = "appPath";
	private static final String  FQDN= "fqdn";
	private static final String WASPATH="wasPath";
	private static final String PROFILENAME="profileName";
	private static final String JYTHONPATH="jythonPath";
	private static final String JYTHONNAME="jythonName";
	private static final String DBUSR = "dbUsr";
	private static final String DBPWD = "dbPwd";
	private static final String DBIP="dbIp";
	private static final String DBPORT="dbPort";
	
	private String wasUsr;
	private String wasPwd;
	private String appName;
	private String appPath;
	private String fqdn="localhost";
	private String wasPath;
	private String profileName;
	private String jythonPath;
	private String jythonName;
	private String dbUsr;
	private String dbPwd;
	private String dbIp;
	private String dbPort;
	
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
	
	public String getAppPath() {
		return appPath;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
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

	public String getJythonPath() {
		return jythonPath;
	}

	public void setJythonPath(String jythonPath) {
		this.jythonPath = jythonPath;
	}

	public String getJythonName() {
		return jythonName;
	}

	public void setJythonName(String jythonName) {
		this.jythonName = jythonName;
	}
	
	public String getDbUsr() {
		return dbUsr;
	}

	public void setDbUsr(String dbUsr) {
		this.dbUsr = dbUsr;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getDbIp() {
		return dbIp;
	}

	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
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
		if(JudgeUtil.isEmpty(appPath)){
			log.error("appPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appPath is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(fqdn)){
			log.error("fqdn is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "fqdn is null");
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
		if(JudgeUtil.isEmpty(jythonPath)){
			log.error("jythonPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "jythonPath is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(jythonName)){
			log.error("jythonName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "jythonName is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(dbUsr)){
			log.error("dbUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbUsr is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(dbPwd)){
			log.error("dbPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPwd is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(dbIp)){
			log.error("dbIp is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbIp is null");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(dbPort)){
			log.error("dbPort is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPort is null");
			return JSON.toJSONString(resultMap);
		}
		
			JSONObject pluginParam = new JSONObject();
			this.paramMap.put("message", this.messageMap);
			this.paramMap.put("pluginName", "deployApp4Was");
			
			this.messageMap.put("deployApp4Was", pluginParam);
			
			pluginParam.put("CMD", wasPath+"/profiles/"+profileName+"/bin/wsadmin.sh -conntype SOAP -lang jython -f "+jythonPath+"/"+jythonName+"  -username "+wasUsr+"  -password "+wasPwd);
			
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
		if (!JudgeUtil.isEmpty(this.pluginInput.get(APPPATH))) {
			this.appPath = this.pluginInput.get(APPPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(FQDN))) {
			this.fqdn = this.pluginInput.get(FQDN).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WASPATH))) {
			this.wasPath = this.pluginInput.get(WASPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(PROFILENAME))) {
			this.profileName = this.pluginInput.get(PROFILENAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(JYTHONPATH))) {
			this.jythonPath = this.pluginInput.get(JYTHONPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(JYTHONNAME))) {
			this.jythonName = this.pluginInput.get(JYTHONNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBUSR))) {
			this.dbUsr = this.pluginInput.get(DBUSR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBPWD))) {
			this.dbPwd = this.pluginInput.get(DBPWD).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBIP))) {
			this.dbIp = this.pluginInput.get(DBIP).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBPORT))) {
			this.dbPort = this.pluginInput.get(DBPORT).toString();
		}
	}
	
	 public static void main(String [] args){
		 DatasourceNew4Was was=new DatasourceNew4Was();
		 try {
			String s=was.doAgent();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
}
