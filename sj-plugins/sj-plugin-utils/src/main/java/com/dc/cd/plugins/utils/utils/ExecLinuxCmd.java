package com.dc.cd.plugins.utils.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.cd.plugins.utils.constants.Constants;

public class ExecLinuxCmd {
	public static synchronized String execCmd(Map<String, Object> pluginInput,Map<String, Object> msgMap) throws Exception {
		List<String> list = new ArrayList<String>();
		Map<String, Object> resultCmd = new HashMap<String, Object>();
		String sysEncoding = System.getProperty("sun.jnu.encoding");// 操作系统编码
		boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		if (isWin) {
			list.add("cmd");
			list.add("/c");
		} else {
			list.add("sh");
			list.add("-c");
		}
		String CMD=pluginInput.get("CMD").toString();
		list.add(CMD);
		ProcessBuilder pb = new ProcessBuilder(list);
		pb.redirectErrorStream(true);
		Boolean timeOutFlag = false;
		String FAILRE = pluginInput.get("failRE") != null ? pluginInput.get("failRE").toString()
				: ConfigHelper.getValue("saltFailRE");
		String SUCCESSRE = pluginInput.get("successRE") != null ? pluginInput.get("successRE").toString()
				: ConfigHelper.getValue("saltSuccessRE");
		int timeOut = pluginInput.get("timeOut") != null ? Integer.valueOf(pluginInput.get("timeOut").toString()) : 60;// 默认60秒
		if(!JudgeUtil.isEmpty(SUCCESSRE)){
			resultCmd=CommandUtil.judgePattern(SUCCESSRE);
			if(!(Boolean)resultCmd.get("result")){
				return JSON.toJSONString(resultCmd);
			};
		}
		if(!JudgeUtil.isEmpty(FAILRE)){
			resultCmd=CommandUtil.judgePattern(FAILRE);
			if(!(Boolean)resultCmd.get("result")){
				return JSON.toJSONString(resultCmd);
			};
		}
		Process process = pb.start();
		CountDownLatch cdl = new CountDownLatch(1);
		AnalyseLogThread alt = new AnalyseLogThread(process, SUCCESSRE, FAILRE, cdl, sysEncoding);
		JSONObject msg=JSONObject.parseObject(JSONObject.toJSONString(msgMap));
		alt.setMsg(msg);
		Thread test = new Thread(alt);
		test.setDaemon(true);
		test.start();
		try {
			cdl.await(timeOut * 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {// 中断异常，不用提示用户
			// TODO: handle exception
		}
		StringBuilder sb = new StringBuilder();
		Boolean successN = alt.getSuccess();
		if (cdl.getCount() > 0) {// timeOut
			sb.append("命令执行超时!!!" + "\n");
			timeOutFlag = true;
			successN = false;
		}
		sb.append(alt.getSb());
//		alt.end=true;//结束服务线程
//		if(!JudgeUtil.isEmpty(msg) && !JudgeUtil.isEmpty(msg.getString("FlowInstanceId"))){
//			MongoLog mongoLog =  MongoLog.getInstance();
//			mongoLog.updateNodeRecord(msg, sb.toString());
//		}
		resultCmd.put(Constants.Plugin.RESULT, successN);
		resultCmd.put("saltInputStream", sb.toString()+"\n"+CMD);
		if (timeOutFlag) {// 若超时，主动关闭process
//			if (process.isAlive()) {
//				alt.closeProcess();
//			}
			alt.closeProcess();
		}
		return JSON.toJSONString(resultCmd);
	}
}
