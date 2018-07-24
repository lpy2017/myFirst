package com.dc.scriptUtils.appengine.plugins.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dc.scriptUtils.appengine.plugins.constants.Constants;
import com.dc.scriptUtils.appengine.plugins.ssh.SSHClient;

public class SSHUtil {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(SSHUtil.class);
	/*
	 * params:{"ip":"127.0.0.1","port":"21","userName":"admin","password":"123456","FAILRE":"","SUCCESSRE":"","timeOut":"","commandLine":"true"}
	 */
	
	public static Map<String,Object> sshExecCmd(JSONObject params){
		SSHClient ssh;
		Map<String,Object>resultCmd=new HashMap<String,Object>();
		try {
			String CMD=params.getString("CMD");
			String remoteIp=params.getString("ip");
			String userName=params.getString("userName");
			String password=params.getString("password");
			int port=Integer.valueOf(params.getString("port"));
			int timeOut=Integer.valueOf(params.getString("timeOut"));
			String SUCCESSRE=params.getString("SUCCESSRE");
			String FAILRE=params.getString("FAILRE");
			Boolean commandLine=params.getBooleanValue("commandLine");
			ssh = new SSHClient(remoteIp,port,timeOut);//默认超时时间是60s
			ssh.logon(userName,password);
			ssh.openSession();
			ssh.setFAILRE(FAILRE);
			ssh.setSUCCESSRE(SUCCESSRE);
			if(commandLine){
				//生成腳本
				Map<String,Object>result =FileUtil.generateScriptFile(CMD);
				if(!"true".equals(result.get("result").toString())){
					resultCmd= result;
				}else{
					String localFile=(String)result.get("filePath");
					File file = new File(localFile);
					String remoteFile="/tmp/"+file.getName();
					//将本地脚本文件copy到远程机
					ssh.uploadFile(localFile, "/tmp/");
					ssh.execCommandOrWrite(remoteFile);
					resultCmd= ssh.resultMap;
					ssh.execCommandOrWrite("rm -f "+remoteFile);
					ssh.closeCon();
				}
			}else{
				ssh.execCommandOrWrite(CMD.toString());
				ssh.closeCon();
				resultCmd= ssh.resultMap;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			resultCmd.put(Constants.Plugin.RESULT,false);
			resultCmd.put(Constants.Plugin.RESULT_MESSAGE,"CMD命令执行异常"+e.getMessage());
		}
		return resultCmd;
	}
}
