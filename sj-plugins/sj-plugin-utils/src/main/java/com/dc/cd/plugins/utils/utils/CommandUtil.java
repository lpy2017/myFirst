package com.dc.cd.plugins.utils.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.filters.StringInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.cd.plugins.utils.constants.Constants;
import com.dc.cd.plugins.utils.ssh.SSHClient;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class CommandUtil {
	public static String sysEncoding=System.getProperty("sun.jnu.encoding");//操作系统编码
	public static Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(CommandUtil.class);
	/*
	 * params,msg(msg可为空)
	 * params:{"CMD":"echo aa","FAILRE":"","SUCCESSRE":"","timeOut":"","commandLine":"true","longTask":false}
	 */
	public static Map<String, Object> excuteCommmand(JSONObject params,JSONObject msg){
		String CMD=params.getString("CMD");
		int timeOut=Integer.valueOf(params.getString("timeOut"));
		String SUCCESSRE=params.getString("SUCCESSRE");
		String FAILRE=params.getString("FAILRE");
		Boolean commandLine=params.getBooleanValue("commandLine");
		Boolean longTask = params.getBooleanValue("longTask");
		Map<String, Object> resultCmd = new JSONObject();
		String scriptParams=params.getString("scriptParams");
		List<String> list = new ArrayList<String>();
		if(isWin){
			list.add("cmd");
			list.add("/c");
		}else{
			list.add("sh");
			list.add("-c");
		}
		if(!JudgeUtil.isEmpty(SUCCESSRE)){
			resultCmd=judgePattern(SUCCESSRE);
			if(!(Boolean)resultCmd.get("result")){
				return resultCmd;
			};
		}
		if(!JudgeUtil.isEmpty(FAILRE)){
			resultCmd=judgePattern(FAILRE);
			if(!(Boolean)resultCmd.get("result")){
				return resultCmd;
			};
		}
		String localFile=null;
		if(commandLine){
			Map<String,Object>result =FileUtil.generateScriptFile(CMD);
			if(!"true".equals(result.get("result").toString())){
				resultCmd= result;
			}else{
				localFile=(String)result.get("filePath");
				log.debug("generate command file===="+localFile);
				list.add(localFile);
			}
		}else{
			list.add(CMD);
			if(!JudgeUtil.isEmpty(scriptParams)){//执行带参数的脚本
				String[] scriptParamArray = scriptParams.split(" ");
				for(String one : scriptParamArray){
					list.add(one);
				}
			   }
		}
		log.debug(JSON.toJSONString(list));
		ProcessBuilder pb = new ProcessBuilder(list);
		pb.redirectErrorStream(true);
		Boolean timeOutFlag=false;
		Process process;
		AnalyseLogThread alt =null;
		try {
			process = pb.start();
			CountDownLatch cdl = new CountDownLatch(1);
			alt = new AnalyseLogThread(process, SUCCESSRE, FAILRE, cdl,sysEncoding);
			Thread test = new Thread(alt);
			alt.setMsg(msg);
			test.setDaemon(true);
			test.start();
			cdl.await(timeOut*1000, TimeUnit.MILLISECONDS);
			StringBuilder sb = new StringBuilder();
			Boolean successN = alt.getSuccess();
			if(cdl.getCount()>0){//timeOut
				sb.append("命令执行超时!!!"+"\n");
				timeOutFlag=true;
				successN = false;
			}
			sb.append(alt.getSb());
//			alt.end=true;//结束服务线程
//			if(!JudgeUtil.isEmpty(msg) && !JudgeUtil.isEmpty(msg.getString("FlowInstanceId"))){
//				MongoLog mongoLog =  MongoLog.getInstance();
//				mongoLog.updateNodeRecord(msg, sb.toString());
//			}
			resultCmd.put(Constants.Plugin.RESULT, successN);
			resultCmd.put(Constants.Plugin.RESULT_MESSAGE, sb.toString());
			if(timeOutFlag ||longTask){//若超时或是长任务，都需要主动关闭process
//				if(process.isAlive()){
//					alt.closeProcess();
//				}
				alt.closeProcess();
			}
			if(localFile !=null){
				if(isWin){//windows环境下，临时脚本执行完后，如果立即删除，可能会删不掉，需要先杀进程树
					killProcessTree(process);
				}
				File tmpfile = new File(localFile);
				Boolean deleteResult = tmpfile.delete();
//				log.debug("process.isAlive()="+process.isAlive()+"delete command file===="+tmpfile.getAbsolutePath()+" deleteResult="+deleteResult);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally {
			if(alt !=null){
				if(!alt.end){
					alt.end=true;
				}
			}
		}
		return resultCmd;
	}

	
	/*
	 * params:{"ip":"127.0.0.1","port":"21","userName":"admin","password":"123456","CMD":"echo aa","FAILRE":"","SUCCESSRE":"","timeOut":"","commandLine":"true","longTask":false}
	 */
	public static Map<String,Object> sshExecCmd(JSONObject params,JSONObject msg){
		SSHClient ssh;
		Map<String,Object>resultCmd=new HashMap<String, Object>();
		try {
			String CMD=params.getString("CMD");
			String remoteIp=params.getString("ip");
			String userName=params.getString("userName");
			String password=params.getString("password");
			int port=Integer.valueOf(params.getString("port"));
			int timeOut=Integer.valueOf(params.getString("timeOut"));
			String SUCCESSRE=params.getString("SUCCESSRE");
			String FAILRE=params.getString("FAILRE");
			if(!JudgeUtil.isEmpty(SUCCESSRE)){
				resultCmd=judgePattern(SUCCESSRE);
				if(!(Boolean)resultCmd.get("result")){
					return resultCmd;
				};
			}
			if(!JudgeUtil.isEmpty(FAILRE)){
				resultCmd=judgePattern(FAILRE);
				if(!(Boolean)resultCmd.get("result")){
					return resultCmd;
				};
			}
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
			resultCmd.put(Constants.Plugin.MESSAGE,"CMD命令执行异常"+e.getMessage());
		}
		return resultCmd;
	}
	//windows环境下kill进程树
	public static void killProcessTree(Process process) {
		try {
			Field f = process.getClass().getDeclaredField("handle");
			f.setAccessible(true);
			long handl = f.getLong(process);
			Kernel32 kernel = Kernel32.INSTANCE;
			WinNT.HANDLE handle = new WinNT.HANDLE();
			handle.setPointer(Pointer.createConstant(handl));
			int ret = kernel.GetProcessId(handle);
			Long PID = Long.valueOf(ret);
			String cmd = getKillProcessTreeCmd(PID);
			Runtime rt = Runtime.getRuntime();
			Process killPrcess = rt.exec(cmd);
			killPrcess.waitFor();
			killPrcess.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getKillProcessTreeCmd(Long Pid) {
		String result = "";
		if (Pid != null)
			result = "cmd.exe /c taskkill /PID " + Pid + " /F /T ";
		return result;
	}
	/*
	 * String 匹配多个正则表达式,有一个匹配上，则返回true
	 */
	public static Boolean mathLine(String line,String[] REs){
		Boolean result=false;
		for(String re : REs){
			Pattern p = Pattern.compile(re);
			  Matcher mat = p.matcher(line);
			  while(mat.find()){
				  result= true;
				  break;
			  }
		}
		return result;
	}
	public static void modifySqlFile(String filePath,String pkbName){
		File file =new File(filePath);
		FileInputStream fi =null;
		StringInputStream sis=null;
		BufferedReader br =null;
		BufferedWriter wr =null;
		try {
			fi = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fi));
			String line =null;
			StringBuffer sb = new StringBuffer();
			Boolean pkbFlag=false;
			while((line = br.readLine())!=null){
				if((!pkbFlag&&line.contains("package"))){
					pkbFlag=true;
				}
				if(pkbFlag){
					if(line.contains("package")&&line.contains(pkbName)){
						line =line.replace("package", "create or replace package");
					}
					sb.append(line).append("\n");
					if(line.contains("end;")){
						sb.append("/").append("\n");
					}
				}
			}
			file.delete();
			if(sb.length()>0){
				sb.append("show errors;"+"\n"+"quit;");
				fi.close();
				String str=sb.toString();
				wr=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				wr.write(str);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				if(fi !=null){
					fi.close();
				}
				if(br !=null){
					fi.close();
				}
				if(sis !=null){
					fi.close();
				}
				if(wr !=null){
					wr.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static Map<String,Object> judgePattern(String... patternStr){
		Map<String,Object> result = new HashMap<String, Object>();
		if(patternStr!=null &&patternStr.length>0){
			for(int i=0;i<patternStr.length;i++){
				Pattern pattern = null;
				try {
					pattern = Pattern.compile(patternStr[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(pattern==null){
					result.put("result", false);
					result.put("message", "正则表达式 "+patternStr[i]+" 不合法,请检查该表达式！");
					return result;
				}
			}
		}
		result.put("result", true);
		return result;
	}
	
	public static void main(String []args) throws IOException{
//		String line="Failed:    1";
//		String successRE="Failed:    0";
//		String [] sREs=successRE.split("#");
//		String failRE="(?i)error:#Failed:\\s{4}[1-9]\\d*";
//		String [] fREs=failRE.split("#");
//		InputStreamReader inputReader = new InputStreamReader(new FileInputStream("f:/test/test1/result.txt"));
//		BufferedReader br = new BufferedReader(inputReader);
//		String str = null;
//		StringBuffer sb= new StringBuffer();
////		sb.append("stat to record..." + System.lineSeparator());
////		recordLog();//按时间间隔记录日志
//		while ((str = br.readLine()) != null) {
//			sb.append(str + System.lineSeparator());
//			if (successRE != null && CommandUtil.mathLine(str, sREs)) {
//				System.out.println(true);
//				break;
//			} else if (failRE != null && CommandUtil.mathLine(str, fREs)) {
//				System.out.println(false);
//				break;
//			}else{
//				continue;
//			}
//		}
//		String line="2018-01-04T11:51:38.359806Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).";
//		String failRe="\\[ERROR\\]";
//		String [] REs=failRe.split("#");
//		System.out.println(CommandUtil.mathLine(line, REs));
		System.out.println(JSON.toJSONString(test()));
	
		
		
		
	}
	
	public static Map<String,Object> test(){
		Map<String,Object> resultCmd =new HashMap<String, Object>();
		String SUCCESSRE ="*aaaa";
		String FAILRE ="*aa*";
		if(!JudgeUtil.isEmpty(SUCCESSRE)){
			resultCmd=judgePattern(SUCCESSRE);
			if(!(Boolean)resultCmd.get("result")){
				return resultCmd;
			};
		}
		if(!JudgeUtil.isEmpty(FAILRE)){
			resultCmd=judgePattern(FAILRE);
			if(!(Boolean)resultCmd.get("result")){
				return resultCmd;
			};
		}
		return resultCmd;
	}
}
