package com.dc.appengine.plugins.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class SqlExecute extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(SqlExecute.class);

	private static final String DRIVER_KEY  = "driver";
	private static final String DBURL_KEY  = "DB_url";
	private static final String USERNAME = "userName";
	private static final String PASSWORD = "password";
	private static final String SQLFILEPATH = "sqlFilePath";
//	private static final String LOGFILEPATH = "logFilePath";

	private String driver;
	private String DB_url;
	private String userName;
	private String password;
	private String sqlFilePath;
	private String logFilePath;



	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}


	@Override
	public String doAgent() throws Exception {
		Map<String,Object> result= new HashMap<>();
		if(JudgeUtil.isEmpty(this.driver)){
			result.put(Constants.Plugin.RESULT,false);
			result.put(Constants.Plugin.MESSAGE,"driver为空");
			return JSON.toJSONString(result);
		}
        if(JudgeUtil.isEmpty(this.DB_url)){
        	result.put(Constants.Plugin.RESULT,false);
			result.put(Constants.Plugin.MESSAGE,"DB_url为空");
			return JSON.toJSONString(result);
		}
        if(JudgeUtil.isEmpty(this.userName)){
        	result.put(Constants.Plugin.RESULT,false);
			result.put(Constants.Plugin.MESSAGE,"userName为空");
			return JSON.toJSONString(result);
		}
        if(JudgeUtil.isEmpty(this.password)){
        	result.put(Constants.Plugin.RESULT,false);
			result.put(Constants.Plugin.MESSAGE,"password为空");
			return JSON.toJSONString(result);
		}
        if(JudgeUtil.isEmpty(this.sqlFilePath)){
        	result.put(Constants.Plugin.RESULT,false);
			result.put(Constants.Plugin.MESSAGE,"sqlFilePath为空");
			return JSON.toJSONString(result);
		}
        File sqlFile = new File(this.sqlFilePath);
        List<File> sqlFiles = new ArrayList<>();
        if(sqlFile.getName().endsWith("*.*")||sqlFile.getName().endsWith("*.sql")){//遍历父目录，找到sql文件
        	File parentPath = new File(this.sqlFilePath).getParentFile();
        	result =generateSqlFile(parentPath);
        	if("true".equals(result.get("result")+"")){
        		sqlFiles = (List)result.get("files"); 
        	}else{
        		return JSON.toJSONString(result);
        	}
        }else{
        	sqlFiles.add(sqlFile);
        }
        if(sqlFiles.size()==0){
        	result.put(Constants.Plugin.RESULT, true);
    		result.put(Constants.Plugin.MESSAGE, "没有找到sql脚本，不需要执行sql脚本");
        	return JSON.toJSONString(result);
        }
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
			if(br !=null){
				br.close();
			}
			if(in !=null){
				in.close();
			}
			if(outFile !=null && outFile.exists()){
				outFile.delete();
			}
			if(tmpFiles.size()>0){
				for(File tmpFile:tmpFiles){
					tmpFile.delete();
				}
			}
		}
		return JSON.toJSONString(result);

	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DRIVER_KEY ))){
			this.driver=this.pluginInput.get(DRIVER_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DBURL_KEY ))){
			this.DB_url=this.pluginInput.get(DBURL_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(USERNAME))){
			this.userName=this.pluginInput.get(USERNAME).toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get(PASSWORD))){
			this.password=this.pluginInput.get(PASSWORD).toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get(SQLFILEPATH))){
			this.sqlFilePath=this.pluginInput.get(SQLFILEPATH).toString();
		}

//		if(!JudgeUtil.isEmpty(this.pluginInput.get(LOGFILEPATH))){
//			this.logFilePath=this.pluginInput.get(LOGFILEPATH).toString();
//		}

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
	
	class NameComparator implements Comparator {
		// 按照文件名比较  
		 public int compare(Object o1, Object o2) {
			 String filePath1=((File)o1).getAbsolutePath();
			 String filePath2=((File)o2).getAbsolutePath();
			 int result = filePath1.compareTo(filePath2);
			 return result;
		 }
	}
	public static void main(String [] args) throws UnsupportedEncodingException{
		JSONObject param = new JSONObject();
		JSONObject message = new JSONObject();
		JSONObject pluginParam = new JSONObject();
		message.put("instanceId", "33sdgfdsgsdgsd555");
		message.put("hostIp", "10.1.108.11");
		message.put("SqlExecute_-1", pluginParam);
		
		//新引擎需要的参数
		param.put("pluginName", "SqlExecute_-1");
		param.put("message", message);
		pluginParam.put("driver", "oracle.jdbc.driver.OracleDriver");
		pluginParam.put("DB_url", "jdbc:oracle:thin:@10.126.3.203:1521:xe");
		pluginParam.put("gf_variable", "cmd_gf_variable");
		pluginParam.put("userName", "system");
		pluginParam.put("password", "oracle");
		pluginParam.put("sqlFilePath", "F:/test/db/*.sql");

		SqlExecute sqlExecute = new SqlExecute();
		try {
			sqlExecute.initPlugin(param.toJSONString(),null);
			String result="";
			result=sqlExecute.doAgent();
			System.out.println(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
