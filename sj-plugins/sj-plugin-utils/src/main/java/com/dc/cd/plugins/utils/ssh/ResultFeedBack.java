package com.dc.cd.plugins.utils.ssh;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dc.cd.plugins.utils.constants.Constants;
import com.dc.cd.plugins.utils.utils.JudgeUtil;

public class ResultFeedBack extends Thread {
	
	private final static Logger LOG = Logger.getLogger(ResultFeedBack.class);
	
	private String cmd;
	private InputStreamReader inputReader;
	private SSHClient sshClient;
	private int  timeout =   10 * 60 ; // 十分钟
	//private int index=-1;
	public ResultFeedBack(InputStreamReader br,String cmd, SSHClient sshClient){
		this.inputReader=br;
		this.cmd=cmd;
		this.sshClient=sshClient;
		if (sshClient.timeout != null){
			if (sshClient.timeout > 0 ){
				this.timeout =sshClient.timeout;  //秒	
				LOG.info("sshClient.timeout" + this.timeout);
			}else{
				LOG.info("sshClient.timeout 小于 零");
			}
		}
	
		
	}
	public void run(){
		synchronized(sshClient){
			Boolean patternFlag = false;//匹配到正则表达式的标识
			long startTime = System.currentTimeMillis();
			
			String str="";	
			int c =0;
			String remain = "" ;
			try {
				Pattern success = null;
				Pattern fail = null;
				if(!JudgeUtil.isEmpty(sshClient.SUCCESSRE)){
					success = Pattern.compile(sshClient.SUCCESSRE);
				}
				if(!JudgeUtil.isEmpty(sshClient.FAILRE)){
					fail = Pattern.compile(sshClient.FAILRE);
				}
				sshClient.resultMap.clear();//清空resultMap,然后记录新的命令执行结果
				sshClient.resultMap.put(Constants.Plugin.RESULT, true);//默認結果成功
				char[] array = new char[2048];
				while((c= inputReader.read(array)) != -1){
					remain = remain + new String(array, 0, c);
					int idx =0;
					while(idx>=0){
						idx  = remain.indexOf("\n") ;
						if ( idx ==-1 && remain.indexOf("\r") >=0 ){
							idx = remain.indexOf("\r");
						}
						if ( idx >= 0){
							String tmp = remain;
							if (remain.length() > idx+1 ){
								remain = remain.substring(idx+1);
							}else{
								remain = "";
							}
							if (idx >=1 && tmp.charAt(idx-1) == '\r'){
								idx --;
							}
							
							str = tmp.substring(0, idx);
							LOG.debug(str);
							if (sshClient.tip.length() == 0){
								int cidx = str.indexOf(cmd);
								if (cidx > 0){
									sshClient.tip = str.substring(0, cidx);
									LOG.info("tip:" + sshClient.tip);
								}
							}else{

								int at1 = str.indexOf(sshClient.tip);
								// if (at1 == 0)
								//	continue;
								int at2 = str.indexOf(cmd);
								// if (at2 == 0)
								//	continue;
								if ( at1 != 0 && at2 !=0 ){
									sshClient.result += (str + "\n");
									if(success!=null && success.matcher(str).find()){
										sshClient.resultMap.put(Constants.Plugin.RESULT, true);
										patternFlag=true;
									}else if(fail!=null && fail.matcher(str).find()){
										sshClient.resultMap.put(Constants.Plugin.RESULT, false);
										patternFlag=true;
									}
									sshClient.resultMap.put(Constants.Plugin.RESULT_MESSAGE, sshClient.result);
								}
								if(patternFlag){
									return;
								}
								if (isEnd(sshClient.tip,remain,str)){
									return;
								}
							}
						}else{
							if (isEnd(sshClient.tip,remain,str) && !inputReader.ready()){
								LOG.debug("remain 数据完成 ");
								return;
							}
						}
					}
					
				
					int count = 0;
					while (! inputReader.ready()) {
		
//						if ( count > 100 && count % 100 ==0 ){
//							LOG.warn(str);
//						}
						if (count > 10 && count % 5 ==0  ){ // 超过 2秒
							if (isEnd(sshClient.tip, remain, str)){
								LOG.error("超过 2秒 ");
								count = -2;
								break;
							}
						}
						
						if ( count > timeout * 5 ) { //超过 timout秒
							LOG.error(" 超过"+timeout+"秒");
							count = -1;
							break;
						}
						
						Thread.sleep(200);
						count++;
					}
					
					long diff = System.currentTimeMillis() - startTime ;
					LOG.info("diff==" + diff);
					if (diff > timeout * 1000 ){
						LOG.error(" 超过"+timeout+"秒");
						count = -1;
					}
					//超时					
					if (count < 0 ){
						sshClient.resultMap.put(Constants.Plugin.RESULT, false);
						sshClient.result += ("命令执行超时！" + "\n");
						sshClient.resultMap.put(Constants.Plugin.RESULT_MESSAGE, sshClient.result);
						LOG.error("命令：" + cmd);
						LOG.info(sshClient.tip);
						LOG.info(str);
						LOG.info(remain);
						LOG.info(sshClient.result);
						break;
					}
				}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			sshClient.notify(); 
			sshClient.isFirst=false;
		}
		
		}
		
		
	}
	
	public boolean isEnd(String tip , String remain, String str){
		
		if (StringUtils.isNotEmpty(str) && str.equals(sshClient.tip) && ( str.equals(remain) || sshClient.endWithTip(remain))){
			return true;
		}
		
		if (StringUtils.isNotEmpty(remain)  && remain.equals(tip) )
			return true;
				
		return false;
				
		
	}
	
}
