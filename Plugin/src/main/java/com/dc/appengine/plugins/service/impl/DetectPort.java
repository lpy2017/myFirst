package com.dc.appengine.plugins.service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;


public class DetectPort extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Config.class);
	//CMD插件执行成功或失败标识的key
	private static final String PORT = "port";
	private static final String IP = "ip";
	private static final String TIMEOUT_KEY  = "timeOut";
	private static final String UNUSED  = "unUsed";
	
	private String port;
	private String ip;
	private int timeOut;
	private Boolean unUsed;
	
	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
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
	public String doAgent() throws IOException, InterruptedException {

		Map<String, Object> resultCmd = new HashMap<>();
		Boolean success = true;
		StringBuffer sb = new StringBuffer();
		if (JudgeUtil.isEmpty(ip)) {
			success = false;
			sb.append("探测 ip 为空!");
		}
		if (JudgeUtil.isEmpty(port)) {
			success = false;
			sb.append("探测 port 为空");
		}
		Pattern p=Pattern.compile("^[1-9]([0-9]*)(#[1-9]([0-9]*))*$");//端口的格式8080#8081
		Matcher match = p.matcher(port);
		String[] ports={};
		if(match.find()){
			ports =port.split("#");
		}else{
			success = false;
			sb.append("端口 "+port+" 格式有问题");
		}
		if (!success) {
			resultCmd.put(Constants.Plugin.RESULT, success);
			resultCmd.put(Constants.Plugin.MESSAGE, sb.toString());
			return JSON.toJSONString(resultCmd);
		} else {
			if(!unUsed){
				resultCmd = detectUsedPort(ip, port, timeOut);
			}else{
				resultCmd = detectUnUsedPort(ip, ports);
			}
			return JSON.toJSONString(resultCmd);
		}
	}

	
	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(PORT))){
			this.port = this.pluginInput.get(PORT).toString();
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
		if(!JudgeUtil.isEmpty(this.pluginInput.get(UNUSED))){
			this.unUsed = Boolean.valueOf(this.pluginInput.get(UNUSED).toString());
		}else{
			this.unUsed=false;
		}
	}
	
	public Map<String,Object> detectUsedPort(String ip,String port,int timeOut){
		Boolean success=true;;
		Boolean timeOutFlag=false;
		Boolean continueFlag=true;
		Map<String,Object> resultCmd = new HashMap<>();
		long startTime = System.currentTimeMillis();
		while(continueFlag){
			long diff = System.currentTimeMillis()-startTime;
			if(diff > timeOut*1000){
				timeOutFlag=true;
				continueFlag=false;
				break;
			}
			Socket socket = new Socket();
			try {
				socket.connect(new InetSocketAddress(ip,Integer.valueOf(port)));
				success =true;
			} catch (IOException e) {
				success =false;
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(!timeOutFlag && !success){
				continue;
			}else{
				break;
			}
		}
		resultCmd.put(Constants.Plugin.RESULT, success);
		if(!timeOutFlag){
			resultCmd.put(Constants.Plugin.MESSAGE, "探测 ip【"+ip+"】 端口【"+port+"】成功！");
		}else{
			resultCmd.put(Constants.Plugin.MESSAGE, "探测 ip【"+ip+"】 端口【"+port+"】失败，探测超时！");
		}
		return resultCmd;
	}
	
	public Map<String,Object> detectUnUsedPort(String ip,String[] ports){
		Boolean result=true;
		Map<String,Object> resultCmd = new HashMap<>();
		List<String> usedPorts = new ArrayList<>();
		for(int i=0;i<ports.length;i++){
			Socket socket = new Socket();
			int detectPort=Integer.valueOf(ports[i]);
			try {
				socket.connect(new InetSocketAddress(ip,detectPort));
				usedPorts.add(ports[i]);
			} catch (IOException e) {
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(usedPorts.size()>0){
			result=false;
			resultCmd.put(Constants.Plugin.MESSAGE, "端口 "+JSON.toJSONString(usedPorts)+" 已被占用");
		}else{
			result=true;
			resultCmd.put(Constants.Plugin.MESSAGE, "端口"+JSON.toJSONString(ports)+" 未被使用");
		}
		resultCmd.put(Constants.Plugin.RESULT, result);
		return resultCmd;
	}
	public static void main(String [] args){
		Boolean success=true;
		int timeOut=10;
		String ip="10.126.3.222";
		String port="8092";
		Pattern p=Pattern.compile("^[1-9]([0-9]*)(#[1-9]([0-9]*))*$");//端口的格式8080#8081
		Matcher match = p.matcher(port);
		String[] ports={};
		if(match.find()){
			ports =port.split("#");
		}else{
			success = false;
		}
		DetectPort test = new DetectPort();
		System.out.println(JSON.toJSONString(test.detectUsedPort(ip, port, timeOut)));
		if(!success){
			System.out.println(port+"异常");
		}else{
			System.out.println(JSON.toJSONString(test.detectUnUsedPort(ip, ports)));
		}
	}
}
