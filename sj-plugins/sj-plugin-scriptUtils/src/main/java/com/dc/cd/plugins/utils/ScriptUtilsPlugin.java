package com.dc.cd.plugins.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;
import com.dc.scriptUtils.appengine.plugins.constants.Constants;
import com.dc.scriptUtils.appengine.plugins.service.impl.tail.LogTail;
import com.dc.scriptUtils.appengine.plugins.service.impl.tail.TailNotifyImpl;
import com.dc.scriptUtils.appengine.plugins.ssh.SSHClient;
import com.dc.scriptUtils.appengine.plugins.utils.CommandUtil;
import com.dc.scriptUtils.appengine.plugins.utils.ExecLinuxCmd;
import com.dc.scriptUtils.appengine.plugins.utils.FileUtil;
import com.dc.scriptUtils.appengine.plugins.utils.JudgeUtil;

public class ScriptUtilsPlugin extends BaseAgentPlugin {

	private static final Logger log = LoggerFactory.getLogger(ScriptUtilsPlugin.class);
	public static String sysEncoding=System.getProperty("sun.jnu.encoding");//操作系统编码
	
	public JobExecResultDto execute(JobDetailDto detailDTO) {
		log.info("demoplugin receive job");
		log.info("action:{}", detailDTO.getAction());
		log.info("deviceIP:{}", detailDTO.getDeviceIp());
		log.info("JobDetailParam:{}", detailDTO.getJobDetailParam());
		log.info("Encode:{}", detailDTO.getEncode());
		log.info("jobid:{}", detailDTO.getJobId());
		log.info("JobInstId:{}", detailDTO.getJobInstId());
		log.info("jobname:{}", detailDTO.getJobName());
		log.info("NodeGrpId:{}", detailDTO.getNodeGrpId());
		log.info("nodeid:{}", detailDTO.getNodeId());
		log.info("PluginCode:{}", detailDTO.getPluginCode());
		log.info("ProtocolList:{}", detailDTO.getProtocolList());
		log.info("Timeout:{}", detailDTO.getTimeout());
		log.info("TypeCode:{}", detailDTO.getTypeCode());
		log.info("uuid:{}", detailDTO.getUuid());
		/*
		 * 执行作业的代码省略
		 */
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		try {
			Map<String, Object> pluginInput = JSON.parseObject(detailDTO.getJobDetailParam());
			if (Constants.Plugin.TYPE_CODE_CMD.equals(detailDTO.getTypeCode())||Constants.Plugin.TYPE_CODE_INSTALLNODE.equals(detailDTO.getTypeCode())
					||Constants.Plugin.TYPE_CODE_CMD4LOCAL.equals(detailDTO.getTypeCode())) {
				return CMD(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_SALT.equals(detailDTO.getTypeCode())) {
				return salt(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_SALTSSH.equals(detailDTO.getTypeCode())) {
				return saltSSH(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_SQLEXCUTE.equals(detailDTO.getTypeCode())) {
				return sqlExecute(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_CMD4Analysis.equals(detailDTO.getTypeCode())) {
				return CMD4Analysis(detailDTO, pluginInput);
			}else{
				// 未找到匹配的typecode
				execResultDto.setSuccess(false);
				execResultDto.setMsg("未找到对应的typecode");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			execResultDto.setSuccess(false);
			execResultDto.setMsg(getStackTrace(e));
		}
		return execResultDto;
		
		
	}

	public JobExecResultDto CMD(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		Map<String,Object> resultCmd= new HashMap<String,Object>();
		String CMD=(String)pluginInput.get(Constants.Plugin.CMD_KEY);
		int timeOut=60;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.TIMEOUT_KEY))) {
			timeOut=Integer.valueOf((String)pluginInput.get(Constants.Plugin.TIMEOUT_KEY));
		}
		String SUCCESSRE=JudgeUtil.isEmpty((String)pluginInput.get(Constants.Plugin.SUCCESSRE_KEY))?null:(String)pluginInput.get(Constants.Plugin.SUCCESSRE_KEY);
		String FAILRE=JudgeUtil.isEmpty((String)pluginInput.get(Constants.Plugin.FAILRE_KEY))?null:(String)pluginInput.get(Constants.Plugin.FAILRE_KEY);
		Boolean commandLine=false;
		if("true".equals((String)pluginInput.get(Constants.Plugin.COMMANDLINE_KEY))) {
			commandLine=true;
		}
		Boolean longTask = false;
		if("true".equals((String)pluginInput.get(Constants.Plugin.LONGTASK))) {
			longTask=true;
		}
		String userName=null;
		String password=null;
		String ip=null;
		if(Constants.Plugin.TYPE_CODE_INSTALLNODE.equals(detailDTO.getTypeCode())){
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.NODEUSERNAME))){
				userName = messageMap.get(Constants.Plugin.NODEUSERNAME).toString();
			}
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.NODEPASSWORD))){
				password = messageMap.get(Constants.Plugin.NODEPASSWORD).toString();
			}
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.NODEIP))){
				ip = messageMap.get(Constants.Plugin.NODEIP).toString();
			}
		}else{
			if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.USERNAME))){
				userName = pluginInput.get(Constants.Plugin.USERNAME).toString();
			}
			if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.PASSWORD))){
				password = pluginInput.get(Constants.Plugin.PASSWORD).toString();
			}
			if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.IP))){
				ip = pluginInput.get(Constants.Plugin.IP).toString();
			}
		}
		String port= Constants.Plugin.SSHPORT.toString();
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.PORT))){
			port = pluginInput.get(Constants.Plugin.PORT).toString();
		}
		if(JudgeUtil.isEmpty(CMD)){
			execResultDto.setSuccess(false);
			execResultDto.setMsg("CMD命令为空");
			return execResultDto;
		}
		JSONObject params = new JSONObject();
		params.put("CMD", CMD);
		params.put("ip", ip);
		params.put("userName", userName);
		params.put("password", password);
		params.put("port", port);
		params.put("timeOut", timeOut+"");
		params.put("commandLine", commandLine);
		params.put("SUCCESSRE", SUCCESSRE);
		params.put("FAILRE", FAILRE);
		params.put("longTask", longTask);
		if(!JudgeUtil.isEmpty(userName) && !JudgeUtil.isEmpty(password)){//判断是否使用指定用户执行命令
			resultCmd=CommandUtil.sshExecCmd(params,null);
		}else{
			resultCmd=CommandUtil.excuteCommmand(params,null);
		}
		execResultDto.setSuccess((Boolean)resultCmd.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultCmd.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	public JobExecResultDto salt(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String op="";
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.OPERATION))) {
			op=pluginInput.get(Constants.Plugin.OPERATION).toString();
		}else{
			op=messageMap.get(Constants.Plugin.OPERATION).toString();
		}
		String userName = messageMap.get(Constants.Plugin.NODEUSERNAME).toString();
		String password = messageMap.get(Constants.Plugin.NODEPASSWORD).toString();
		String nodeIp = messageMap.get(Constants.Plugin.NODEIP).toString();
		String instanceId = messageMap.get(Constants.Plugin.INSTANCEID).toString();
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		String[] strs = deployPath.split(File.separator);
		String cmdpath = strs[3].concat(".").concat(strs[4]);
		String pillar_refresh_cmd;
		String cmd;
		String minionId = getSaltMinionId(nodeIp,userName,password);
		if(minionId != null){
			pillar_refresh_cmd = "sudo salt " + minionId +" saltutil.refresh_pillar;";
			cmd = pillar_refresh_cmd +"sudo salt " + minionId +" state.sls "; 
		}
		else{
			pillar_refresh_cmd = "sudo salt -S " + nodeIp +" saltutil.refresh_pillar;";
			cmd = pillar_refresh_cmd +"sudo salt -S " + nodeIp +" state.sls "; 
		}
		if(op == null || "".equals(op)){
		}else{
			cmd += cmdpath+"."+op;
		}
		log.info("salt command is: "+ cmd);
		pluginInput.put("CMD", cmd);
		String result="";
		Map<String,Object> resultCmd =new HashMap<String, Object>();
		try {
			result = ExecLinuxCmd.execCmd(pluginInput,new HashMap<String, Object>());
			resultCmd = JSON.parseObject(result);
			resultCmd.put(Constants.Plugin.MESSAGE, resultCmd.get("saltInputStream").toString());
			resultCmd.remove("saltInputStream");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCmd.put(Constants.Plugin.MESSAGE,getStackTrace(e));
			resultCmd.put(Constants.Plugin.RESULT, false);
		}
		
		execResultDto.setSuccess((Boolean)resultCmd.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultCmd.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	public JobExecResultDto saltSSH(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String op="";
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.OPERATION))) {
			op=pluginInput.get(Constants.Plugin.OPERATION).toString();
		}else{
			op=messageMap.get(Constants.Plugin.OPERATION).toString();
		}
		String nodeIp = messageMap.get(Constants.Plugin.NODEIP).toString();
		String instanceId = messageMap.get(Constants.Plugin.INSTANCEID).toString();
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		String[] strs = deployPath.split(File.separator);
		String cmdpath = strs[3].concat(".").concat(strs[4]);
		String cmd;
		cmd = "sudo salt-ssh -i  " + nodeIp +" state.sls "; 
		//salt -S '192.168.88.130' pkg.install mysql-community-server
		if(op == null || "".equals(op)){
		}else{
			cmd += cmdpath+"."+op;
		}
		log.info("salt-ssh command is: "+ cmd);
		pluginInput.put("CMD", cmd);
	
		String result="";
		Map<String,Object> resultCmd =new HashMap<String, Object>();
		try {
			result = ExecLinuxCmd.execCmd(pluginInput,new HashMap<String, Object>());
			resultCmd = JSON.parseObject(result);
			resultCmd.put(Constants.Plugin.MESSAGE, resultCmd.get("saltInputStream").toString());
			resultCmd.remove("saltInputStream");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultCmd.put(Constants.Plugin.MESSAGE,getStackTrace(e));
			resultCmd.put(Constants.Plugin.RESULT, false);
		}
		
		execResultDto.setSuccess((Boolean)resultCmd.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultCmd.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	public JobExecResultDto sqlExecute(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String,Object> result= new HashMap<String,Object>();
		String driver=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DRIVER_KEY ))){
			driver=pluginInput.get(Constants.Plugin.DRIVER_KEY ).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("driver为空");
			return execResultDto;
		}
		String DB_url=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBURL_KEY ))){
			DB_url=pluginInput.get(Constants.Plugin.DBURL_KEY ).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("DB_url为空");
			return execResultDto;
		}
		String userName=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.USERNAME))){
			userName=pluginInput.get(Constants.Plugin.USERNAME).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("userName为空");
			return execResultDto;
		}
		String password=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.PASSWORD))){
			password=pluginInput.get(Constants.Plugin.PASSWORD).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("password为空");
			return execResultDto;
		}
		String sqlFilePath=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.SQLFILEPATH))){
			sqlFilePath=pluginInput.get(Constants.Plugin.SQLFILEPATH).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("sqlFilePath为空");
			return execResultDto;
		}
		File sqlFile = new File(sqlFilePath);
        List<File> sqlFiles = new ArrayList<>();
        if(sqlFile.getName().endsWith("*.*")||sqlFile.getName().endsWith("*.sql")){//遍历父目录，找到sql文件
        	File parentPath = sqlFile.getParentFile();
        	result =generateSqlFile(parentPath);
        	if("true".equals(result.get("result")+"")){
        		sqlFiles = (List)result.get("files"); 
        	}else{
        		execResultDto.setSuccess((Boolean)result.get("result"));
    			execResultDto.setMsg((String)result.get("message"));
        		return execResultDto;
        	}
        }else{
        	sqlFiles.add(sqlFile);
        }
        if(sqlFiles.size()==0){
        	execResultDto.setSuccess(true);
			execResultDto.setMsg("没有找到sql脚本，不需要执行sql脚本");
			return execResultDto;
        }
//        if(JudgeUtil.isEmpty(this.logFilePath)){
//        	result.put(Constants.Plugin.RESULT,false);
//			result.put(Constants.Plugin.MESSAGE,"logFilePath为空");
//			return JSON.toJSONString(result);
//		}
        StringBuffer sb = new StringBuffer();
        InputStream in =null;
        BufferedReader br=null;
        File outFile = null;
        List<File> tmpFiles = new ArrayList<>();
        try {
    		//输出到文件 sql.out 中；不设置该属性，默认输出到控制台   
			String path = Constants.DC_INSTALL_PATH + "/" + Constants.TEMP_DIR + "/"+UUID.randomUUID()+".out";
			String logFilePath = URLDecoder.decode(path, "utf-8");
			System.out.println(logFilePath);
			outFile = new File(logFilePath);
    		for(File file:sqlFiles){//遍历执行sql脚本
    			if(file.getName().endsWith("TmpCD.sql")){//记录临时文件，用于删除
    				tmpFiles.add(file);
    			}
    			SQLExec sqlExec = new SQLExec();
        		//设置数据库参数   
        		//oracle
        		sqlExec.setDriver(driver);   
        		sqlExec.setUrl(DB_url);   
        		sqlExec.setUserid(userName);   
        		sqlExec.setPassword(password);
        		sqlExec.setPrint(true); //设置是否输出   
        		//有出错的语句该如何处理   
    			sqlExec.setOnerror((SQLExec.OnError)(EnumeratedAttribute.getInstance(   
    					SQLExec.OnError.class, "abort")));  
        		sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错   
        		sqlExec.setOutput(outFile); 
    			//要执行的脚本    
    			sqlExec.setSrc(file);
    			if(!outFile.getParentFile().exists()){
    				outFile.getParentFile().mkdirs();
    			};
    			sqlExec.execute();
    			in = new FileInputStream(outFile);
    			br = new BufferedReader(new InputStreamReader(in, this.sysEncoding));
    			String str=null;
    			sb.append(file.getName()+"执行成功！");
    			while((str=br.readLine())!=null){
    				sb.append(System.lineSeparator()).append(str);
    			}
    		}
    		
    		result.put(Constants.Plugin.RESULT, true);
    		result.put(Constants.Plugin.MESSAGE, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.put(Constants.Plugin.RESULT, false);
    		result.put(Constants.Plugin.MESSAGE, "sql执行失败！"+e.getMessage());
		}finally{
			try {
				if(br !=null){
						br.close();
				}
				if(in !=null){
					in.close();
				}
				if(outFile !=null && outFile.exists()){
					outFile.delete();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(tmpFiles.size()>0){
				for(File tmpFile:tmpFiles){
					tmpFile.delete();
				}
			}
		}
		execResultDto.setSuccess((Boolean)result.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)result.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	//doc文档的执行，要在sql之前
		public void recordSqlFile(File parentFile,List<File> docFiles,List<File> sqlFiles){
			File[] dirFileTmps = parentFile.listFiles(new sqlFilter("dir"));
			File[] sqlFileTmps=  parentFile.listFiles(new sqlFilter("sql"));
			File[] docFileTmps=  parentFile.listFiles(new sqlFilter("doc"));
			List<File> sqllist =Arrays.asList(sqlFileTmps);
			List<File> doclist =Arrays.asList(docFileTmps);
			//同类型文件按文件名排序
			if(sqllist.size()>1){
				Collections.sort(sqllist, new NameComparator());
			}
			if(doclist.size()>1){
				Collections.sort(doclist, new NameComparator());
			}
			//递归遍历
			for(File file :dirFileTmps){
				recordSqlFile(file,docFiles,sqlFiles);
			}
			docFiles.addAll(doclist);
			sqlFiles.addAll(sqllist);
		}
		
		
		public Map<String,Object> generateSqlFile(File parentFile){
			Map<String,Object> result = new HashMap<>();
			List<File> docFiles = new ArrayList<>();
			List<File> sqlFiles = new ArrayList<>();
			recordSqlFile(parentFile, docFiles, sqlFiles);
			List<File> allSqlFiles = new ArrayList<>();
			for(File file:docFiles){
				File tmp = FileUtil.changeDoc2Sql(file);
				if(tmp !=null){
					allSqlFiles.add(tmp);
				}else{
					result.put("result", false);
					result.put("message", "抽取"+file.getAbsolutePath()+"中的sql脚本异常！");
					return result;
				}
			}
			allSqlFiles.addAll(sqlFiles);
			result.put("result", true);
			result.put("files", allSqlFiles);
			return result;
		}	
	private String getSaltMinionId(String nodeIp, String userName,String password) {
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
	
	public JobExecResultDto CMD4Analysis(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		String CMD=(String)pluginInput.get(Constants.Plugin.CMD_KEY);
		Long timeout=60000L;
		if(!JudgeUtil.isEmpty(pluginInput.get("timeout"))) {
			timeout=Long.valueOf((String)pluginInput.get("timeout"));
		}
		String SUCCESSRE=JudgeUtil.isEmpty((String)pluginInput.get(Constants.Plugin.SUCCESSRE_KEY))?null:(String)pluginInput.get(Constants.Plugin.SUCCESSRE_KEY);
		String FAILRE=JudgeUtil.isEmpty((String)pluginInput.get(Constants.Plugin.FAILRE_KEY))?null:(String)pluginInput.get(Constants.Plugin.FAILRE_KEY);
		String logPath=(String)pluginInput.get(Constants.Plugin.LOGPATH);
		if(JudgeUtil.isEmpty(CMD)){
			execResultDto.setSuccess(false);
			execResultDto.setMsg("CMD命令为空");
			return execResultDto;
		}
		Map<String,Object> result = new HashMap<String,Object>();

		List<String> list = new ArrayList<String>();
		list.add("cmd");
		list.add("/c");
		list.add(CMD);
		System.out.println(list);
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
		try {
			Process process = pb.start();
			File logFile = new File(logPath);
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
//			if(process.isAlive()){
				process.destroy();
//			}
			if(!result.containsKey(Constants.Plugin.RESULT)){
				result.put(Constants.Plugin.RESULT, false);	
				result.put(Constants.Plugin.MESSAGE, "cmd timeout");
			}else{
				result.put(Constants.Plugin.MESSAGE, msger.getResult());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		execResultDto.setSuccess((Boolean)result.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)result.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
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
	
	public class sqlFilter implements FileFilter{
		String fileType="sql";
		
		public sqlFilter(String fileType){
			this.fileType=fileType;
		}

		@Override
		public boolean accept(File file) {
			// TODO Auto-generated method stub
			String fileName = file.getName().toLowerCase();
			if(("sql".equals(fileType)&&fileName.endsWith(".sql"))||
					("doc".equals(fileType)&&fileName.endsWith(".doc"))||
					("dir".equals(fileType)&&file.isDirectory())){
				return true;
			}else{
				return false;
			}
		}
		
	}
	
	public static void main(String[] arg){
		ScriptUtilsPlugin scriptUtilsPlugin  = new ScriptUtilsPlugin();
		Map<String,Object> pluginInput = new HashMap<String, Object>();
		JobDetailDto detailDTO = new JobDetailDto();
		/*
		 * "pluginCode": "CD_PLUGIN_SCRIPTUTILS",
			"typeCode": "CMD4Analysis",
			"CMD": "",
			"successRE": "",
			"failRE": "",
			"gf_variable": "",
			"logPath": "",
			"timeout": "60000"
		 */
		pluginInput.put("CMD", "echo aa");
		pluginInput.put("successRE", "aa");
		pluginInput.put("failRE", "bb");
		pluginInput.put("timeout", "5000");
		pluginInput.put("logPath", "F:/test/hsn.log");
		try {
			scriptUtilsPlugin.CMD4Analysis(detailDTO, pluginInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class NameComparator implements Comparator {
		// 按照文件名比较   
		public int compare(Object o1, Object o2) {
			String filePath1=((File)o1).getAbsolutePath();
			String filePath2=((File)o2).getAbsolutePath();
			int result = filePath1.compareTo(filePath2);
			return result;
		}
	}
}