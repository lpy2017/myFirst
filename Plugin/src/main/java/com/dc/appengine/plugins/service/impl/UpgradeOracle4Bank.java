package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.jaxb.Filter;
import com.dc.appengine.plugins.jaxb.FilterList;
import com.dc.appengine.plugins.jaxb.Item;
import com.dc.appengine.plugins.jaxb.SqlFile;
import com.dc.appengine.plugins.jaxb.SqlFileList;
import com.dc.appengine.plugins.utils.CommandUtil;
import com.dc.appengine.plugins.utils.FileFilterClass;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.XMLParser;


public class UpgradeOracle4Bank extends AbstractPlugin {
	private static final String ORACLE_URL  = "oracleUrl";
	private static final String APPNAME  = "appName";
	private static final String BAKPATH  = "bakPath";
	private static final String FILEPATH  = "filePath";
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String TIMEOUT_KEY  = "timeOut";
	private static final String UPGRADE  = "upGrade";
    private String resourceVersionId=""; //组件版本的id
    private String patchFileRootPath =""; //补丁文件的根路径
    private String filePath=""; //补丁文件的路径
    private String bakPath=""; //备份路径
    private String oracleUrl="";//oracle数据库实例的url
    private String appName="";
    private String FAILRE;
	private String SUCCESSRE;
	private int timeOut;
	private Boolean upGrade=true;
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(Config.class);
	
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
	public String doAgent() throws IOException, InterruptedException{
		Map<String,Object> resultMap = new HashMap<>();
		patchFileRootPath =new File(filePath).getParentFile().getAbsolutePath();
		File patchRootFile = new File(patchFileRootPath);
		if (patchRootFile.exists()) {
			File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory",appName));//多个应用
			//升级
			if(upGrade){
				for (int i = 0; i < appFiles.length; i++) {
					File[] dbXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.DB,Constants.FILE_SUFFIX_XML));
					if (dbXMLFiles.length != 0) {
						File[] patchParents = dbXMLFiles[0].getParentFile().listFiles(new FileFilterClass("isDirectory",Constants.DB));
						resultMap=parseXml(patchParents[0].getAbsolutePath(),dbXMLFiles[0].getAbsolutePath());
						return JSON.toJSONString(resultMap);
					} else {
						resultMap.put("result", false);
						resultMap.put("message", "数据库升级规则文件不存在");
					}
				}
				resultMap.put("result", true);
				resultMap.put("message", "升级成功！");
			}else{
				File bakFile= new File(bakPath+File.separator+resourceVersionId+File.separator+appName);
				if(bakFile.exists()){
					File[] files = bakFile.listFiles();
					StringBuffer sb = new StringBuffer();
					if(files.length==0){
						resultMap.put("result", false);
						resultMap.put("message", "备份路径的目录["+bakFile.getAbsolutePath()+"]，不存在备份文件");	
						return JSON.toJSONString(resultMap);
					}else{
						for (int i = 0; i < files.length; i++) {
							resultMap = excuteRollBack(files[i].getAbsolutePath());
							sb.append("===============" + resultMap.get("message") + System.lineSeparator()
							+ "===============" + System.lineSeparator());
							if (!(Boolean) resultMap.get("result")) {
								resultMap.put("message", sb.toString());
								break;
							}
						}
					}
				}else{
					resultMap.put("result", false);
					resultMap.put("message", "备份路径的目录["+bakFile.getAbsolutePath()+"]不存在");
					return JSON.toJSONString(resultMap);
				}
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录["+patchFileRootPath+"]不存在");
		}
		return JSON.toJSONString(resultMap);
	}

	
	private Map<String,Object> parseXml(String patchRootPath, String xmlFile) {
		Map<String,Object> resultMap = new HashMap<>();
		InputStream xml=null;
		StringBuffer sb = new StringBuffer();
		try {
			xml = new FileInputStream(xmlFile);
			XMLParser xp = new XMLParser();
			SqlFileList sqlFileList = (SqlFileList) xp.unmarshall(xml, SqlFileList.class);
			List<SqlFile> sqlFiles =sqlFileList.getSqlFiles();
			Collections.sort(sqlFiles);//按seq对sqlFiles进行排序
			for(SqlFile sqlFile:sqlFiles){
				String abPath = patchRootPath+File.separator+sqlFile.getPath();
				//修改脚本，追加命令退出标识"exit"
				if(abPath.endsWith(".pkb")){
					FileUtil.modifyFile(abPath, Constants.FILE_SUFFIX_PKB);
				}else{
					FileUtil.modifyFile(abPath, Constants.FILE_SUFFIX_SQL);
				}
				resultMap =excuteUpgrade(abPath);
				sb.append("==============="+resultMap.get("message")+System.lineSeparator()+"==============="+System.lineSeparator());
				if(!(Boolean)resultMap.get("result")){
					break;
				}
			}
			resultMap.put("message", sb.toString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "补丁的升级规则文件不存在");
		} finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return resultMap;
	}

	private Map<String,Object> excuteUpgrade(String sqlPath) {
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<>();
		String CMD="";
		String filePath=sqlPath;
		//执行pkb的备份
		if(!isWin&&filePath.contains("\\")){
			filePath=filePath.replace("\\", File.separator);
		}else if(isWin&&filePath.contains("//")){
			filePath=filePath.replace("//", File.separator);
		}
		//执行命令
		CMD="sqlplus "+oracleUrl+" @"+filePath;
		//执行命令
		String message="";
		System.out.println("==========CMD "+CMD);
		JSONObject cmdParams = new JSONObject();
		cmdParams.put("CMD", CMD);
		cmdParams.put("timeOut", timeOut+"");
		cmdParams.put("SUCCESSRE", SUCCESSRE);
		cmdParams.put("FAILRE", FAILRE);
		result =CommandUtil.excuteCommmand(cmdParams,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		if((Boolean)result.get("result")){
			result.put("result", true);
			String messageTmp="["+filePath+"]"+"执行成功："+result.get("message");
			message=message+System.lineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", "["+filePath+"]"+"执行失败："+result.get("message"));
		}
		return result;
	}
	
	private Map<String,Object> excuteRollBack(String sqlPath) {
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<>();
		String CMD="";
		String filePath=sqlPath;
		//执行pkb的备份
		if(!isWin&&filePath.contains("\\")){
			filePath=filePath.replace("\\", File.separator);
		}else if(isWin&&filePath.contains("//")){
			filePath=filePath.replace("//", File.separator);
		}
		if(filePath.endsWith(Constants.FILE_SUFFIX_PKB)){
			CMD="sqlplus "+oracleUrl+" @"+filePath;
			FileUtil.modifyFile(filePath, Constants.FILE_SUFFIX_PKB);
		}else if(filePath.endsWith(Constants.FILE_SUFFIX_DMP)){
			CMD="imp "+this.oracleUrl+" file="+filePath+" full=y";
		}
		//执行命令
		String message="";
		System.out.println("==========CMD "+CMD);
		JSONObject cmdParams = new JSONObject();
		cmdParams.put("CMD", CMD);
		cmdParams.put("timeOut", timeOut+"");
		cmdParams.put("SUCCESSRE", SUCCESSRE);
		cmdParams.put("FAILRE", FAILRE);
		result =CommandUtil.excuteCommmand(cmdParams,JSONObject.parseObject(JSONObject.toJSONString(this.paramMap)));
		if((Boolean)result.get("result")){
			result.put("result", true);
			String messageTmp="["+filePath+"]"+"执行成功："+result.get("message");
			message=message+System.lineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", "["+filePath+"]"+"执行失败："+result.get("message"));
		}
		return result;
	}
	
	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.messageMap.get("resourceVersionId"))){
			this.resourceVersionId = this.messageMap.get("resourceVersionId").toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FILEPATH))){
			this.filePath = this.pluginInput.get(FILEPATH).toString();
		}else{
			if(!JudgeUtil.isEmpty(this.messageMap.get(FILEPATH))){
				this.filePath = (String)messageMap.get(FILEPATH);
			}
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(BAKPATH))){
			this.bakPath = (String)pluginInput.get(BAKPATH);
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(ORACLE_URL))){
			this.oracleUrl = (String)pluginInput.get(ORACLE_URL);
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(APPNAME))){
			this.appName = (String)pluginInput.get(APPNAME);
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(TIMEOUT_KEY))){
			this.timeOut = Integer.valueOf(this.pluginInput.get(TIMEOUT_KEY).toString());
		}else{
			timeOut =60;//默认超时时间是60s
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(UPGRADE))){
			this.upGrade = Boolean.valueOf(this.pluginInput.get(UPGRADE).toString());
		}
	}
	
	public void setResourceVersionId(String resourceVersionId) {
		this.resourceVersionId = resourceVersionId;
	}

	public void setBakPath(String bakPath) {
		this.bakPath = bakPath;
	}

	public void setOracleUrl(String oracleUrl) {
		this.oracleUrl = oracleUrl;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public void setUpGrade(Boolean upGrade) {
		this.upGrade = upGrade;
	}

	public void setPatchFileRootPath(String patchFileRootPath) {
		this.patchFileRootPath = patchFileRootPath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static void main(String [] args){
		String configPath ="D:/1226Code/SmartCD/Plugin/target/Plugin-workFlow/plugins_conf";
		System.setProperty("configPath",configPath);
		String resourceVersionId="symbols_versionId"; //组件版本的id
	    String patchFileRootPath ="F:/test/banktest4Oracle/EDRBANK_1.0_Build0260"; //补丁文件的根路径
	    String filePath="F:/test/banktest4Oracle/EDRBANK_1.0_Build0260/EDRBANK_1.0_Build0260.zip"; //补丁文件的路径
	    String bakPath="F:/test/banktest4Oracle/bak"; //备份路径
	    String oracleUrl="system/oracle@10.126.3.203:1521/xe";//oracle数据库实例的url
	    String appName="symbols";
	    Boolean upGrade=false;
	    int timeOut=60;
	    UpgradeOracle4Bank upgradeOracle4Bank = new UpgradeOracle4Bank();
	    upgradeOracle4Bank.setAppName(appName);
	    upgradeOracle4Bank.setResourceVersionId(resourceVersionId);
	    upgradeOracle4Bank.setPatchFileRootPath(patchFileRootPath);
	    upgradeOracle4Bank.setFilePath(filePath);
	    upgradeOracle4Bank.setBakPath(bakPath);
	    upgradeOracle4Bank.setOracleUrl(oracleUrl);
	    upgradeOracle4Bank.setUpGrade(upGrade);
	    upgradeOracle4Bank.setTimeOut(timeOut);
	    String result ="";
	    try {
	    	result = upgradeOracle4Bank.doAgent();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(result);
	
	}
}