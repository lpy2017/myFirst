package com.dc.appengine.plugins.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.CommandUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;


public class CMD4Local extends AbstractPlugin {
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
	private static final String TIMEOUT_KEY  = "timeOut";
	private static final String COMMANDLINE_KEY  = "commandLine";
	
	private String CMD;
	private String FAILRE;
	private String SUCCESSRE;
	private boolean longTask=false;
	private String userName;
	private String password;
	private String port;
	private String ip;
	private int timeOut;
	private boolean commandLine=false;
	
	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		// TODO Auto-generated method stub
		final String json=JSON.toJSONString(paramMap);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result;
				try {
					result = doActive(json);
					resultMap = JSON.parseObject(result,new TypeReference<Map<String,Object>>(){});
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(LogRecord.getStackTrace(e));
					resultMap.put(Constants.Plugin.RESULT, false);
					resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
				}
				if(gf_variable != null){//设置方法的执行结果
					messageMap.put(gf_variable,(Boolean)resultMap.get(Constants.Plugin.RESULT));
					AbstractPlugin.setGFVariable(gf_variable, resultMap.get(Constants.Plugin.RESULT).toString(),paramMap);
				}
				messageMap.put(PLUGIN_RESULT_KEY,(Boolean)resultMap.get(Constants.Plugin.RESULT));
				//记录日志
				messageMap.put(Constants.Plugin.RESULT, JSON.toJSONString(resultMap));
				paramMap.put(Constants.Plugin.MESSAGE, messageMap);
				updatePluginLog(paramMap.get(Constants.Plugin.PLUGINNAME)+"do doActive is end outputParam==="+JSON.toJSONString(paramMap)
				+System.lineSeparator()+"plugin_result_active:"+resultMap.get(Constants.Plugin.RESULT)+System.lineSeparator()+"result_message:"+System.lineSeparator()+resultMap.get(Constants.Plugin.RESULT_MESSAGE));
				//触发工作流
				invokeWorkflowServer(paramMap);
			}
		};
		Thread t = new Thread(runnable);
		t.start();
//		DoAgentThreadPool.getInstance().execute(runnable);
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws IOException, InterruptedException{
		Map<String,Object> resultCmd= new HashMap<>();
		try {
			if(JudgeUtil.isEmpty(CMD)){
				resultCmd.put(Constants.Plugin.RESULT,false);
				resultCmd.put(Constants.Plugin.MESSAGE,"CMD命令为空");
				return JSON.toJSONString(resultCmd);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			resultCmd.put(Constants.Plugin.RESULT,false);
			resultCmd.put(Constants.Plugin.MESSAGE,"解密CMD命令异常"+e.getMessage());
			return JSON.toJSONString(resultCmd);
		}
		JSONObject params = new JSONObject();
		params.put("CMD", this.CMD);
		params.put("ip", this.ip);
		params.put("userName", this.userName);
		params.put("password", this.password);
		params.put("port", this.port);
		params.put("timeOut", this.timeOut+"");
		params.put("commandLine", this.commandLine);
		params.put("SUCCESSRE", this.SUCCESSRE);
		params.put("FAILRE", this.FAILRE);
		params.put("longTask", this.longTask);
		if(!JudgeUtil.isEmpty(this.userName) && !JudgeUtil.isEmpty(this.password)){//判断是否使用指定用户执行命令
			resultCmd=CommandUtil.sshExecCmd(params,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		}else{
			resultCmd=CommandUtil.excuteCommmand(params,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		}
		return JSON.toJSONString(resultCmd);
	}

	public void setCDM(String cMD) {
		CMD = cMD;
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
		if(!JudgeUtil.isEmpty(this.pluginInput.get(CMD_KEY ))){
			this.CMD=this.pluginInput.get(CMD_KEY ).toString();
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
		if(!JudgeUtil.isEmpty(this.pluginInput.get(TIMEOUT_KEY))){
			this.timeOut = Integer.valueOf(this.pluginInput.get(TIMEOUT_KEY).toString());
		}else{
			timeOut =60;//默认超时时间是60s
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(COMMANDLINE_KEY)) && "true".equals(this.pluginInput.get(COMMANDLINE_KEY))){
			this.commandLine = true;
		}else{
			this.commandLine = false;
		}
	}
}