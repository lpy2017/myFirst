package com.dc.cd.plugins.utils.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.alibaba.fastjson.JSONObject;

public class AnalyseLogThread implements Runnable {
	boolean continueflag = true;
	Process process;
	StringBuilder log;
	Map<String, Object> result = new HashMap<String, Object>();
	String successRE;
	String failRE;
	boolean longTask;
	Boolean success = true;
	int timeOut;
	Boolean timeOutFlag = false;
	long diff;
	Object obj;
	String sysEncoding = "UTF-8";
	StringBuilder sb = new StringBuilder();
	CountDownLatch cdl;
	JSONObject msg;//子流程中，调用插件是传入的参数
	Boolean recordLine = false;
	int writeInterval=1000;//默认每1000ms记录一次库
	Boolean end = false;

	public AnalyseLogThread(Process process, String success, String fail,CountDownLatch cdl,
			String sysEncoding) {
		this.process = process;
		this.successRE = success;
		this.failRE = fail;
		this.cdl = cdl;
		this.sysEncoding = sysEncoding;
	}


	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public StringBuilder getSb() {
		return sb;
	}

	public void setSb(StringBuilder sb) {
		this.sb = sb;
	}
	public void closeProcess(){
		process.destroy();
	}
	@Override
	public void run() {
		InputStream in = null;
		InputStreamReader inputReader = null;
		BufferedReader br=null;
		String[] sREs =null;
		String[] fREs =null;
		if(!JudgeUtil.isEmpty(msg) && !JudgeUtil.isEmpty(msg.getString("FlowInstanceId"))){
			recordLine=true;
		}
//		writeInterval=ConfigHelper.getValue("writeInterval")==null?1000:Integer.valueOf(ConfigHelper.getValue("writeInterval"));
		try {
			if(!JudgeUtil.isEmpty(this.successRE)){
				sREs = this.successRE.split("#");
			}
			if(!JudgeUtil.isEmpty(this.failRE)){
				fREs =this.failRE.split("#");
			}
			in = process.getInputStream();
			inputReader = new InputStreamReader(in,this.sysEncoding);
			br = new BufferedReader(inputReader);
			String str = null;
//			sb.append("stat to record..." + System.lineSeparator());
//			recordLog();//按时间间隔记录日志
			while ((str = br.readLine()) != null) {
				sb.append(str + "\n");
				if (successRE != null && CommandUtil.mathLine(str, sREs)) {
					success = true;
					break;
				} else if (failRE != null && CommandUtil.mathLine(str, fREs)) {//读到错误消息后，继续读，直到超时
					success = false;
//					break;
				}else{
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(br !=null){
					br.close();
				}
				if(inputReader !=null){
					inputReader.close();
				}
				if(in !=null){
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cdl.countDown();
		}
	}


	public JSONObject getMsg() {
		return msg;
	}


	public void setMsg(JSONObject msg) {
		this.msg = msg;
	}
	//间隔writeInterval ms写日志
//	public void recordLog(){
//		Runnable run = new Runnable() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				String logOld="";
//				if(recordLine){
//					while(!end){
//						if(sb.toString().length()>logOld.length()){
//							MongoLog mongoLog = null;
//							mongoLog = MongoLog.getInstance();
//							mongoLog.updateNodeRecord(msg, sb.toString());
//							logOld=sb.toString();
//						}
//						try {
//							Thread.sleep(writeInterval);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		};
//		Thread thread = new Thread(run);
//		thread.setDaemon(true);
//		thread.start();
//	}
}
