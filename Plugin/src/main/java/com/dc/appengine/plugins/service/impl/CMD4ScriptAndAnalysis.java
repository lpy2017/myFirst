package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.service.impl.tail.LogTail;
import com.dc.appengine.plugins.service.impl.tail.TailNotifyImpl;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class CMD4ScriptAndAnalysis extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Config.class);
	//CMD命令方法的key
	private static final String CMD_KEY = "CMD";
	//CMD插件执行成功或失败标识的key
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String TIMEOUT="timeout";
	private static final String LOGPATH = "logPath";
	private static final String SCRIPTPATH = "scriptPath";
	
	private String scriptPath;
	private String logPath;
	private String FAILRE;
	private String SUCCESSRE;
	private long timeout;
	
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
		List<String> list = new ArrayList<>();
		list.add("cmd");
		list.add("/c");
		list.add(this.scriptPath);
		log.debug(JSON.toJSONString(list));
		ProcessBuilder pb = new ProcessBuilder(list);
		pb.redirectErrorStream(true);
		Pattern success = null;
		Pattern fail = null;
		if(!JudgeUtil.isEmpty(SUCCESSRE)){
			success = Pattern.compile(SUCCESSRE);
		}
		if(!JudgeUtil.isEmpty(FAILRE)){
			fail = Pattern.compile(FAILRE);
		}
		final Process process = pb.start();
		File logFile = new File(this.logPath);
		LogTail tailer = new LogTail(10, logFile, false);  
		tailer.setSuccess(success);
		tailer.setFail(fail);
		tailer.setResult(result);
        Object obj=new Object();
        tailer.setObj(obj);
        tailer.setP(process);
//        tailer.setEnd("end");
        TailNotifyImpl msger=new TailNotifyImpl();
        tailer.add(msger);  
        tailer.start();
        new Thread(tailer).start();  
        synchronized (obj) {
        	obj.wait(timeout);
		}
        tailer.stop();
        if(process.isAlive()){
        	process.destroy();
        }
		if(!result.containsKey(Constants.Plugin.RESULT)){
			result.put(Constants.Plugin.RESULT, false);	
			result.put(Constants.Plugin.MESSAGE, "cmd timeout");
		}else{
			result.put(Constants.Plugin.MESSAGE, msger.getResult());
		}
	
		return JSON.toJSONString(result);
	}
	

	public void setCDM(String cMD) {
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		this.scriptPath = scriptPath;
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
			this.pluginInput.get(CMD_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(TIMEOUT))){
			this.timeout = Long.valueOf(this.pluginInput.get(TIMEOUT).toString());
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(LOGPATH))){
			this.logPath = this.pluginInput.get(LOGPATH).toString();
		}		
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SCRIPTPATH))){
			this.scriptPath=this.pluginInput.get(SCRIPTPATH ).toString();
		}
	}
	
}