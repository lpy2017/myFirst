package com.dc.appengine.plugins.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.CommandUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;


public class CMD4Script extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Config.class);
	//CMD命令方法的key
	private static final String CMD_KEY = "CMD";
	//CMD插件执行成功或失败标识的key
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String LONGTASK = "longTask";
	private static final String USERNAME = "userName";
	private static final String PASSWORD = "password";
	private static final String PORT = "port";
	private static final String IP = "ip";
	private static final String SCRIPTPATH = "scriptPath";
	private static final String SCRIPTPARAMS = "scriptParams";
	private static final String TIMEOUT_KEY  = "timeOut";
	
	private String FAILRE;
	private String SUCCESSRE;
	private boolean longTask;
	private String userName;
	private String password;
	private String port;
	private String ip;
	private String scriptPath;
	private String scriptParams;
	private int timeOut;
	
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
	public String doAgent() throws IOException, InterruptedException{
		Map<String,Object> resultCmd= new HashMap<>();
		if(JudgeUtil.isEmpty(this.scriptPath)){
			resultCmd.put(Constants.Plugin.RESULT,false);
			resultCmd.put(Constants.Plugin.MESSAGE,"CMD命令为空");
			return JSON.toJSONString(resultCmd);	
		}
		final Map<String,Object> result = new HashMap<>();
		JSONObject params = new JSONObject();
		params.put("ip", this.ip);
		params.put("userName", this.userName);
		params.put("password", this.password);
		params.put("port", this.port);
		params.put("timeOut", this.timeOut+"");
		params.put("SUCCESSRE", this.SUCCESSRE);
		params.put("FAILRE", this.FAILRE);
		params.put("longTask", this.longTask);
		params.put("CMD", this.scriptPath);
		if(!JudgeUtil.isEmpty(this.userName) && !JudgeUtil.isEmpty(this.password)){//判断是否使用指定用户执行命令
			if(!JudgeUtil.isEmpty(this.scriptParams)){
				this.scriptPath=this.scriptPath+" "+this.scriptParams;//带上参数
			   }
			params.put("CMD", this.scriptPath);
			resultCmd=CommandUtil.sshExecCmd(params,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		}else{
			params.put("scriptParams", this.scriptParams);
			resultCmd=CommandUtil.excuteCommmand(params,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		}
		return JSON.toJSONString(resultCmd);
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}

	public void setLongTask(boolean longTask) {
		this.longTask = longTask;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SCRIPTPATH))){
			this.scriptPath=this.pluginInput.get(SCRIPTPATH ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(LONGTASK))){
			this.longTask = Boolean.valueOf(this.pluginInput.get(LONGTASK).toString());
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(USERNAME))){
			this.userName = this.pluginInput.get(USERNAME).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(PASSWORD))){
			this.password = this.pluginInput.get(PASSWORD).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(PORT))){
			this.port = this.pluginInput.get(PORT).toString();
		}else{
			this.port =Constants.Plugin.SSHPORT.toString();//默认值22
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(IP))){
			this.ip = this.pluginInput.get(IP).toString();
		}else{
			this.ip =this.messageMap.get(Constants.Plugin.NODEIP).toString();//默认本机ip
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SCRIPTPARAMS))){
			this.scriptParams = this.pluginInput.get(SCRIPTPARAMS).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(TIMEOUT_KEY))){
			this.timeOut = Integer.valueOf(this.pluginInput.get(TIMEOUT_KEY).toString());
		}else{
			timeOut =60;//默认超时时间是60s
		}
	}
	public static void main(String [] args) throws IOException{
		JSONObject param = new JSONObject();
		JSONObject message = new JSONObject();
		JSONObject pluginParam = new JSONObject();
		message.put("instanceId", "33sdgfdsgsdgsd555");
		message.put("hostIp", "10.1.108.11");
		message.put("CMD4Script", pluginParam);
		param.put("pluginName", "CMD4Script");
		param.put("message", message);
		pluginParam.put("successRE", "");
		pluginParam.put("failRE", "");
		pluginParam.put("gf_variable", "cmd_gf_variable");
		pluginParam.put("longTask", "false");
		pluginParam.put("scriptPath", "F:/test/test1/test.bat");
		pluginParam.put("scriptParams", "1 2 3");
		int count=1;
		while(count!=0){
			CMD4Script cmd = new CMD4Script();
			try {
				cmd.initPlugin(param.toJSONString(),null);
				String result="";
				result=cmd.doAgent();
				System.out.println(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("count=="+count--);
		}
	}
}