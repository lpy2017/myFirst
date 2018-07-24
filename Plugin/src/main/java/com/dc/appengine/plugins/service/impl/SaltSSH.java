package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.ExecLinuxCmd;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;


public class SaltSSH extends AbstractPlugin{
	private static Logger log = LoggerFactory.getLogger(Config.class);
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	
	private String FAILRE;
	private String SUCCESSRE;
	private String op;
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
		String op = messageMap.get(Constants.Plugin.OPERATION).toString();
		String nodeIp = messageMap.get(Constants.Plugin.NODEIP).toString();
		String instanceId = messageMap.get(Constants.Plugin.INSTANCEID).toString();
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		String[] strs = deployPath.split(File.separator);
		String cmdpath = strs[3].concat(".").concat(strs[4]);
		String cmd;
		cmd = "sudo salt-ssh -i  " + nodeIp +" state.sls "; 
		//salt -S '192.168.88.130' pkg.install mysql-community-server
		if(this.op == null || "".equals(this.op)){
		}else{
			cmd += cmdpath+"."+this.op;
		}
		log.info("salt-ssh command is: "+ cmd);
		pluginInput.put("CMD", cmd);
		String  result = ExecLinuxCmd.execCmd(pluginInput,this.paramMap);
		Map<String,Object> map = JSON.parseObject(result);
		map.put(Constants.Plugin.MESSAGE, map.get("saltInputStream").toString());
		map.remove("saltInputStream");
		return JSON.toJSONString(map);
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
	}

	public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}

	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}
	
}
