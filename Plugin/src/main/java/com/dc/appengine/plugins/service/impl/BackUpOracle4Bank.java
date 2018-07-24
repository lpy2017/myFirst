package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.types.CommandlineJava.SysProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.jaxb.Filter;
import com.dc.appengine.plugins.jaxb.FilterList;
import com.dc.appengine.plugins.jaxb.Item;
import com.dc.appengine.plugins.utils.CommandUtil;
import com.dc.appengine.plugins.utils.FileFilterClass;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.XMLParser;


public class BackUpOracle4Bank extends AbstractPlugin {
	private static final String ORACLE_URL  = "oracleUrl";
	private static final String APPNAME  = "appName";
	private static final String BAKPATH  = "bakPath";
	private static final String FILEPATH  = "filePath";
	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String TIMEOUT_KEY  = "timeOut";
    private String resourceVersionId=""; //组件版本的id
    private String patchFileRootPath =""; //补丁文件的根路径
    private String filePath=""; //补丁文件的路径
    private String bakPath=""; //备份路径
    private String oracleUrl="";//oracle数据库实例的url
    private String appName="";
    private String FAILRE;
	private String SUCCESSRE;
	private int timeOut;
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(BackUpOracle4Bank.class);
	
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
	public String doAgent() throws IOException, InterruptedException {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(oracleUrl)) {
			resultMap.put("result", false);
			resultMap.put("message", "oracle数据库实例的url为空");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(bakPath)) {
			resultMap.put("result", false);
			resultMap.put("message", "备份路径为空");
			return JSON.toJSONString(resultMap);
		}
		if (!JudgeUtil.isEmpty(filePath) && new File(filePath).getParentFile().exists()) {
			patchFileRootPath = new File(filePath).getParentFile().getAbsolutePath();// 补丁的根路径
			/*
			 * 解析补丁描述文件，生成部分规则文件, 补丁包中，可能有多个应用,
			 * 遍地补丁的根目录，如果没有补丁描述文件，则该目录是多个应用的目录，需要遍历备份，备份路径，自动追加一级（应用名目录），
			 */
			resultMap = parsePatchPackage(patchFileRootPath, appName, bakPath + File.separator + resourceVersionId);
		} else {
			resultMap.put("result", false);
			resultMap.put("message", "补丁文件不存在!");
		}
		return JSON.toJSONString(resultMap);
	}

	public Map<String,Object> parsePatchPackage(String patchFileRootPath,String appName,String bakPath){
		Map<String,Object> resultMap = new HashMap<>();
		File patchRootFile = new File(patchFileRootPath);
		if(patchRootFile.exists()){
			File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory",appName));//多个应用
			for(int i=0;i<appFiles.length;i++){
				String bakRootPath=bakPath+File.separator+appFiles[i].getName();
				File[] fileXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.FILTER,Constants.FILE_SUFFIX_XML));
				if(fileXMLFiles.length!=0){
					resultMap= generateBakFile(fileXMLFiles[0],bakRootPath);
					if(!(Boolean)resultMap.get("result")){
						return resultMap;
					}
				}else{
					continue;
				}
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录不存在");
		}
		return resultMap;
	}
	public Map<String,Object> generateBakFile(File xmlFile,String bakRootPath){
		Map<String,Object> resultMap = new HashMap<>();
		InputStream xml=null;
		StringBuffer sb = new StringBuffer();
		try {
			xml = new FileInputStream(xmlFile);
			XMLParser xp = new XMLParser();
			FilterList filters = (FilterList) xp.unmarshall(xml, FilterList.class);
			List<Filter> list =filters.getFilter();
			for(Filter filter:list){
				String id = filter.getId();
				Item item =filter.getItem();
				if(item ==null){
					continue;
				}
				if(!Constants.FITER_TABLE.equals(id)&&!Constants.FITER_PKB.equals(id)){
					continue;
				}
				resultMap = bake(item, id,bakRootPath);
				sb.append("==============="+resultMap.get("message")+System.lineSeparator()+"==============="+System.lineSeparator());
				if(!(Boolean)resultMap.get("result")){
					break;
				}
			}
			resultMap.put("message", sb.toString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "补丁描述文件不存在");
		} finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return resultMap;
	}
	
	public Map<String,Object> bake(Item item,String type,String bakPath){
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<>();
		String CMD="";
		String filePath="";
		String name=item.getName();
		if(Constants.FITER_TABLE.equals(type)){
			SUCCESSRE="导出终止成功";
			FAILRE="导出终止失败";
			String isBackupData=item.getIsBackupData();
			String query="\\\" where 1=1\\\"";
			if(!"true".equals(isBackupData)){
				query="\\\" where 1=2\\\"";
			}
			filePath=bakPath+File.separator+name+".dmp";
			if(!isWin&&filePath.contains("\\")){
				filePath=filePath.replace("\\", File.separator);
			}else if(isWin&&filePath.contains("//")){
				filePath=filePath.replace("//", File.separator);
			}
			File file = new File(filePath);
			File fileParent = new File(bakPath);
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}else{
				if(file.exists()){
					file.delete();
				}
			}
			CMD="exp "+this.oracleUrl+" tables="+name+" file="+filePath+" query="+query;
		}else if(Constants.FITER_PKB.equals(type)){
			//执行pkb的备份
			filePath=bakPath+File.separator+name+".pkb";
			if(!isWin&&filePath.contains("\\")){
				filePath=filePath.replace("\\", File.separator);
			}else if(isWin&&filePath.contains("//")){
				filePath=filePath.replace("//", File.separator);
			}
			File file = new File(filePath);
			File fileParent = new File(bakPath);
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}else{
				if(file.exists()){
					file.delete();
				}
			}
			String params=filePath+" "+Constants.PKBObject+" "+name+" "+Constants.PKB;
			String templateFile = System.getProperty("configPath")+File.separator+"scriptTemplate"+File.separator+"PkbBakTemplate.sql";
			if(!new File(templateFile).exists()){
				result.put("result", false);
				result.put("message", "pkb的模板文件："+templateFile+" 不存在");
				return result;
			}
			//执行备份命令
			CMD="sqlplus "+oracleUrl+" @"+templateFile+" "+params;
		}
		//执行备份命令
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
			String messageTmp=type+"["+name+"]"+"备份成功："+result.get("message");
			message=message+System.lineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", type+"["+name+"]"+"备份失败："+result.get("message"));
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
	}
	
	public void setResourceVersionId(String resourceVersionId) {
		this.resourceVersionId = resourceVersionId;
	}

	public void setPatchFileRootPath(String patchFileRootPath) {
		this.patchFileRootPath = patchFileRootPath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public static void main(String [] args){
		String configPath ="D:/1226Code/SmartCD/Plugin/target/Plugin-workFlow/plugins_conf";
		System.setProperty("configPath",configPath);
		String resourceVersionId="symbols_versionId"; //组件版本的id
	    String patchFileRootPath ="F:/test/banktest4Oracle/EDRBANK_1.0_Build0260"; //补丁文件的根路径
	    String filePath="F:/test/banktest4Oracle/EDRBANK_1.0_Build0260/EDRBANK_1.0_Build0260.zip"; //补丁文件的路径
	    String bakPath="F:/test/banktest4Oracle/bak"; //备份路径
	    String oracleUrl="system/oracle@10.126.3.203:1521/xe";//oracle数据库实例的url
	    String appName="symbols";
	    int timeOut=60;
	    BackUpOracle4Bank backUpOracle4Bank = new BackUpOracle4Bank();
	    backUpOracle4Bank.setAppName(appName);
	    backUpOracle4Bank.setResourceVersionId(resourceVersionId);
	    backUpOracle4Bank.setPatchFileRootPath(patchFileRootPath);
	    backUpOracle4Bank.setFilePath(filePath);
	    backUpOracle4Bank.setBakPath(bakPath);
	    backUpOracle4Bank.setOracleUrl(oracleUrl);
	    backUpOracle4Bank.setAppName(appName);
	    backUpOracle4Bank.setTimeOut(timeOut);
	    String result ="";
	    try {
	    	result = backUpOracle4Bank.doAgent();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(result);
		
	}
}