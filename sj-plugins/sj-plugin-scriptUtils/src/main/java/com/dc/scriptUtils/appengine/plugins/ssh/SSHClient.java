package com.dc.scriptUtils.appengine.plugins.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dc.scriptUtils.appengine.plugins.utils.SystemUtil;
import com.trilead.ssh2.Connection;
import com.trilead.ssh2.SCPClient;
import com.trilead.ssh2.Session;
import com.trilead.ssh2.StreamGobbler;

/**
 * 
 * @author huangwei
 *
 */
public class SSHClient {
	

	private final static Logger LOG = Logger.getLogger(SSHClient.class);
	
	
	private final static Pattern CMDTIP = Pattern.compile("[\\]>][$%#]");

	public boolean isFirst=true;
	private InputStream in;
	private PrintStream out;
	private String prompt = "&";
	
	public String result="";
	
	private Connection conn;
	
	public Integer timeout;  //秒 
	
	Session sess=null;
	
	public String tip="";
	
	
	public String agentRootDir = ""; //
	
	public String agentScriptDir =""; 
	public String FAILRE;
	public String SUCCESSRE;
	public Boolean longTask;
	public Map<String,Object> resultMap=new HashMap<String,Object>();
	public String osType="3";
	
	
     public void setFAILRE(String fAILRE) {
		FAILRE = fAILRE;
	}
	public void setSUCCESSRE(String sUCCESSRE) {
		SUCCESSRE = sUCCESSRE;
	}
	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	public void setLongTask(Boolean longTask) {
		this.longTask = longTask;
	}
	/**
      * 连接远程主机
      * @param hostname
      * @param timeout 执行命令和脚本的超时时间，任务的超时
      * @throws Exception
      */
	public SSHClient(String hostname,int port, Integer timeout) throws Exception {
		
		this.conn = new Connection(hostname,port);
		this.timeout = timeout;
		LOG.info("命令执行的超时时间(秒)：" +timeout);
		
		try {
			this.conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Exception("不能连接远程主机["+hostname+"]",e);
		}
		
	}
     /**
      * 通过用户名、密码登陆到远程主机
      * @param username
      * @param password
      * @return
      */
	public boolean logon(String username,String password){
		boolean isAuthenticated=false;
		try {
			isAuthenticated = conn.authenticateWithPassword(username, password);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return isAuthenticated;
	}
	/**
	 * 下载文件到本地
	 * @param romoteFileName
	 * @param localDir
	 * @throws Exception
	 */
	public  void downloadFile(String remoteFileName, String localDir) throws Exception   
	{  
		
     	SCPClient scpClient = new SCPClient(conn);  
    
		try {
			scpClient.get(remoteFileName, localDir);
			
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
			throw new Exception("将远程主机 "+ conn.getHostname() +"当前目录下的文件["+remoteFileName+"]下载到本地出错");
		}  
	    
	}
	/**
	 * 将本地文件上传到远程主机
	 * @param localFile
	 * @param remoteTargetDirectory
	 * @throws Exception
	 */
	public  void uploadFile(String localFile, String remoteTargetDirectory) throws Exception   
	{  
		SCPClient scpClient = new SCPClient(conn);  
		try {
			scpClient.put(localFile, remoteTargetDirectory,"0777");
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
			throw new Exception("将本地文件["+localFile+"]上传到远程主机出错");
		}  
	    
	}
	/**
	 * 切换到root用户下
	 * @param password
	 */
	public void su(String password) {
		try {
			write("su -");
			readUntil("Password: ");
			write(password);
			prompt = "#";
			readUntil(prompt + " ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 执行cd 命令
	 * @param dir
	 * @throws AutoAgentException  执行出错时
	 */
	public void execCdCmd(String dir) throws Exception{
		
		try {
			write("cd "+dir );
			
			StringBuilder res = new StringBuilder();
			int ch = in.read();
			res.append((char)ch);
			
			int count =0;
			boolean slept = false;
			while (true) {
				if (count==1){
					break;
				}
				switch ((char)ch) {
					case '%':
					case '#':
					case '$':
					case '>':
						count ++;
						break;
				}
			
				if (in.available()>0){
					ch = in.read();
					res.append((char)ch);
				}else{
					if (slept){
						break;
					}
					LOG.warn("sleep 1000ms ");
					Thread.sleep(1000);
					slept = true;
				}
			}
			
			LOG.info(res.toString());
			
			if (res.toString().contains("-bash:")){ //cd 出错
				throw new  Exception("无法进入文件夹"+dir);
			}
			
			this.tip = "";
			
		} catch (IOException e) {
			LOG.error("IOException ",e);
			throw new  Exception("无法进入文件夹"+dir,e);

		} catch (InterruptedException e) {
			LOG.error("InterruptedException cd ",e);
			throw new  Exception("无法进入文件夹"+dir,e);
		}
		
		this.write("echo \"start\"");
		this.execCommandOrWrite("echo \"go\""); 
		
	}
	
	
	/**
	 * 输入命令
	 * @param value
	 */
	private void write(String value) {
		try {
			
			out.println(value);
			out.flush();
			//System.out.println(value);
		} catch (Exception e) {
			System.out.println("输入命令异常"+value+SystemUtil.getLineSeparator()+getStackTrace(e));
			e.printStackTrace();
		}
	}
	/**
	 * 按匹配模式获取所需要的数据
	 * @param pattern
	 * @return
	 */
	public String readUntil(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			// boolean found = false;
			char ch = (char) in.read();
			while (true) {
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						byte[] temp = sb.toString().getBytes("iso8859-1");
						System.out.print(new String(temp, "UTF-8"));
						return new String(temp, "UTF-8");
					}
				}
				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 执行命令，获取执行命令完成后的输出
	 * @param cmd
	 * @return
	 */
	public String execCommandOrWrite(String cmd){
		this.write(cmd);
		//BufferedReader br = new BufferedReader();
	
		ResultFeedBack resultFeedBack=new ResultFeedBack(new InputStreamReader(in),cmd,this);
		synchronized(this){
		try {
			resultFeedBack.start();
			this.wait();
			
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(),e);
		}	
		}
		String str=this.result;
		if(str.length()>0)
			str=str.substring(0, str.length()-1);
		this.result="";
		return str;
	}
	
	
	
	public String getCommandRetureCode(){
		return execCommandOrWrite("echo $?");
	}
	
	/**
	 * 开机会话
	 * @throws Exception 
	 * @throws IOException
	 */
	public void openSession() throws Exception {
	
		try {
			sess = conn.openSession();
			int x_width = 800;
			int y_width = 30;
			sess.requestPTY("bash",x_width,y_width,0,0,null);
			sess.startShell();
		} catch (IOException e) {
			
			throw new Exception("开启远程主机会话出错!");
		}
		in = new StreamGobbler(sess.getStdout());
		out = new PrintStream(sess.getStdin());
		readSysMsg();
	
		
	}
	
	
	/**
	 * 判断 str的末尾包含 tip
	 * @param result
	 * @return
	 */
	public boolean endWithTip(String str){
		 if (str == null)
			 return false;
		 
		 Matcher m = CMDTIP.matcher(str);
		 int end = -1;
		 
		 while(m.find()){
			 end = m.end();
		 }
		 if (end >=0 ){
			 return end + 3 > str.length(); 
		 }
		 
		 return false;
	}
	
	/**
	 * 读系统信息
	 */
	public void readSysMsg(){
		
		char c =0;
		char[] array = new char[2048];
		int i=0;
		StringBuilder msg = new StringBuilder();
		try {
			//读　Last login:
			while (msg.length() <= 0) {
				c = (char)in.read();
				array[i]=c;
				i++;
				if (c =='\n' || c=='\r'){
					break;
				}
				msg.append(c);
			}
			Thread.sleep(500);
			//读其它信息
			while(in.available() >0 ){
				c = (char)in.read();
				array[i]=c;
				i++;
				msg.append(c);
			}
			
		} catch (IOException e) {
			LOG.info("system msg = ",e);
		} catch (InterruptedException e) {
			LOG.info("system msg = ",e);
		}
		String sysInfo = new String(array);
		if(sysInfo.contains("AIX")){
			this.osType="2";
		}
//		System.out.println(sysInfo);
		LOG.info(msg.toString());		
		this.write("echo \"start\"");
		this.execCommandOrWrite("echo \"go\""); 
		
	}
	
	
	public void closeCon(){
		
		if(null!=sess){
			sess.close();
		}
		if(null != conn){
			conn.close();
		}
		
		
	}
	
	
	public boolean isSuSucess(InputStreamReader inputReader,String cmd,String username,String password){
		String remain = "" ;
		String str = "";
		int c = 0 ,count =-1;
		char[] array = new char[2048];
		try{
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
						LOG.info(str);
						
						int cidx = str.indexOf(cmd);
						if (cidx > 0){
							tip = str.substring(0, cidx);
						}
					}
				}
				
				while (in.available() ==0) {
					LOG.info(remain);
					if (endWithTip(remain)){
						LOG.info("tip:"+ tip);
						if (StringUtils.isEmpty(tip)){
							return remain.contains(username);
						}
						
						if (remain.indexOf(tip) ==-1){
							tip = remain;
							return true;
						}else{
							return false;
						}
					}else{
						if (count == -1  ){
							out.println(password);
							out.flush();
						}
					}
					Thread.sleep(1000);
					count++;
					
					if (count > 10){
						count = -10;
						break;
					}
				}
				if (count <= -10){
					LOG.info(tip);
					LOG.info(str);
					LOG.info(remain);
					break;
				}
				count =0 ;
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
		}finally{
			LOG.info("remain" + remain);
		}
		
		return false;	
	}
	
	
	public boolean switchToUser(String username,String password) throws Exception {
		
		String cmd="su - ";
		cmd +=username;
		out.println(cmd);
		out.flush();
		Thread.sleep(500);  //睡眠方式登录password提示符出现不是最优的, 可能的提示符有password和 “密码”. 如果中文时乱码问题解决不了。fyw,2016-09-10
		
		return isSuSucess(new InputStreamReader(in), cmd,username,password);
			
	}
	
//	public String execCommand(String cmd){
//		try {
//			Session sess = this.conn.openSession();
//			sess.execCommand(cmd);
//			InputStream in = new StreamGobbler(sess.getStdout());
//			StringBuffer sb=new StringBuffer();
//			while(true){
//				int v=in.read();
//                if(v!=-1)
//                	sb.append((char)v);
//                else				
//                	break;
//			}
//			return sb.toString();			
//				  
//			
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "";
//	}
	
	
	
	public String getAgentRootDir() {
		return agentRootDir;
	}
	
	
	public String getAgentScriptDir() {
		return agentScriptDir;
	}
	
	
	public void setAgentRootDir(String agentRootDir) {
		
		
		if (! agentRootDir.endsWith("/")){
			agentRootDir += "/";
		}
		
		this.agentRootDir = agentRootDir;
		
	};
	
	
	public void setAgentScriptDir(String agentScriptDir) {
		this.agentScriptDir = agentScriptDir;
		if (! agentScriptDir.endsWith("/")){
			agentScriptDir += "/";
		}
		this.agentScriptDir = agentScriptDir;
	}
	public  static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	/**
	 * 判断 str的末尾包含 tip
	 * @param result
	 * @return
	 */
	public boolean judgeTip(String str){
		 if (str == null)
			 return false;
		 
		 Matcher m = CMDTIP.matcher(str);
		 int end = -1;
		 
		 while(m.find()){
			 end = m.end();
		 }
		 if (end >=0 ){
			 return end == str.length()-1; 
		 }
		 
		 return false;
	}
	public static void main(String[] args) throws IOException {
		SSHClient ssh;
		try {
			String FAILRE="*couldn't connect to host";
			String SUCCESSRE="100%";
			ssh = new SSHClient("10.126.3.222",22,6000);
			System.out.println(ssh.logon("root","123456"));
//			ssh = new SSHClient("10.0.202.205",22,6000);
//			System.out.println(ssh.logon("root","root"));
			ssh.openSession();
//			ssh.setFAILRE(FAILRE);
//			ssh.setSUCCESSRE(SUCCESSRE);
//			String result1 = ssh.execCommandOrWrite("/home/hansn/test.sh");
//			String result1 = ssh.execCommandOrWrite("whoami");
//			Boolean result2 = ssh.switchToUser("hsn", "asdqwezxc");
			String result3 =  ssh.execCommandOrWrite("cd /home;mkdir test1;echo \"test\"");
			//tail -fn 100 /datafs/CloudUI/cloudui.log
//			String result1 = ssh.execCommandOrWrite("mkdir -p /hansn/aaa");
//			String result1 = ssh.execCommandOrWrite("echo aaa");
			System.out.println("resultString=="+result3+" resultMap=="+JSON.toJSONString(ssh.resultMap));
		} catch (Exception e) {
		       System.out.println(e.getMessage());
		}
		
		
		
	}

	
}
