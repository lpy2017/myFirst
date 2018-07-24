package com.dc.appengine.plugins.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.ssh.SSHClient;
import com.dc.appengine.plugins.utils.ExecLinuxCmd;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;


public class Salt extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(Config.class);
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String USERNAME = "nodeUserName";
	private static final String PASSWORD = "nodePassword";
	
	private String FAILRE;
	private String SUCCESSRE;
	private String op;
	private String userName;
	private String password;
	private String nodeIp;
	
	@Override
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
				} catch (Exception e) {
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
	public String doPostAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception{
		String instanceId = messageMap.get(Constants.Plugin.INSTANCEID).toString();
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		String[] strs = deployPath.split(File.separator);
		String cmdpath = strs[3].concat(".").concat(strs[4]);
		String pillar_refresh_cmd;
		String cmd;
		String minionId = getSaltMinionId();
		if(minionId != null){
			pillar_refresh_cmd = "sudo salt " + minionId +" saltutil.refresh_pillar;";
			cmd = pillar_refresh_cmd +"sudo salt " + minionId +" state.sls "; 
		}
		else{
			pillar_refresh_cmd = "sudo salt -S " + nodeIp +" saltutil.refresh_pillar;";
			cmd = pillar_refresh_cmd +"sudo salt -S " + nodeIp +" state.sls "; 
		}
		if(this.op == null || "".equals(this.op)){
		}else{
			cmd += cmdpath+"."+this.op;
		}
		log.info("salt command is: "+ cmd);
		pluginInput.put("CMD", cmd);
		String  result = ExecLinuxCmd.execCmd(pluginInput,this.paramMap);
		Map<String,Object> map = JSON.parseObject(result);
		map.put(Constants.Plugin.MESSAGE, map.get("saltInputStream").toString());
		map.remove("saltInputStream");
		return JSON.toJSONString(map);
	}

	private String getSaltMinionId() {
		SSHClient ssh = null;
		String cmd = "more /etc/salt/minion_id";
		String result = null;
		try {
			ssh = new SSHClient(nodeIp, 22, 5000);
			ssh.logon(userName, password);
			ssh.openSession();
			result = ssh.execCommandOrWrite(cmd);
			ssh.closeCon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result != null){
			if(result.endsWith("No such file or directory")){
				result = null;
			}
		}
		return result;
	}

	private String getNodeInnerNetIp() {
		String regex = "inet\\D*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s{2}\\D*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s{2}\\D*(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
		String cmd = "ifconfig|grep 'inet'|grep -v '127.0.0.1'|grep -v '0.0.0.0'";
		SSHClient ssh = null;
		String result = null;
		String ip = null;
		try {
			ssh = new SSHClient(nodeIp, 22, 10000);
			ssh.logon(userName, password);
			ssh.openSession();
			result = ssh.execCommandOrWrite(cmd);
			ssh.closeCon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result != null){
			BufferedReader br = null;
			List<String> ips = new ArrayList<>();
			try {
				br = new BufferedReader(new StringReader(result));
				String line = null;
				while((line = br.readLine()) != null){
					line = line.trim();
					Matcher matcher = Pattern.compile(regex).matcher(line);
					if(matcher.matches()){
						ips.add(matcher.group(1));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					if(br != null){
						br.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(!ips.contains(nodeIp)){
				if(ips.size() > 0){
					ip = ips.get(0);
				}
			}
		}
		return ip;
	}
	
	private String getNodeHostname() {
		SSHClient ssh = null;
		String cmd = "hostname -f";
		String result = null;
		try {
			ssh = new SSHClient(nodeIp, 22, 60000);
			ssh.logon(userName, password);
			ssh.openSession();
			result = ssh.execCommandOrWrite(cmd);
			ssh.closeCon();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(Constants.Plugin.OPERATION))) {
			this.op=this.pluginInput.get(Constants.Plugin.OPERATION).toString();
		}else{
			this.op=this.messageMap.get(Constants.Plugin.OPERATION).toString();
		}
		userName = this.messageMap.get(USERNAME).toString();
		password = decryptRoot(PASSWORD);
		nodeIp = this.messageMap.get(Constants.Plugin.NODEIP).toString();
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}
	
}
