package com.dc.appengine.plugins.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.jaxb.AppFile;
import com.dc.appengine.plugins.jaxb.AppFiles;
import com.dc.appengine.plugins.utils.FileFilterClass;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.XMLParser;

public class BackUpApp4Bank extends AbstractPlugin {
	private static final String APPROOTPATH  = "appRootPath";
	private static final String BAKPATH  = "bakPath";
	private static final String FILEPATH  = "filePath";
    private String appRootPath="";        //目标应用的根路径
    private String resourceVersionId=""; //组件版本的id
    private String appPatchRootPath =""; //补丁文件的根路径
    private String filePath=""; //补丁文件的路径
    private String bakPath=""; //备份路径
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(BackUpApp4Bank.class);
	@Override
	public String doPreAction() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		// TODO Auto-generated method stub
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//1.生成规则文件 2.按规则文件备份应用
		//备份文件的根路径rootPath
		//rootPath+"/"+${resourceVersionId}+"/"+app
        if(JudgeUtil.isEmpty(appRootPath)){
        	resultMap.put("result", false);
    		resultMap.put("message", "应用路径为空");
    		return JSON.toJSONString(resultMap);
		}else{
			File file = new File(appRootPath);
			if(!file.exists()){
				resultMap.put("result", false);
	    		resultMap.put("message", "应用路径 "+appRootPath+" 不存在！");
	    		return JSON.toJSONString(resultMap);
			}
		}
		if(JudgeUtil.isEmpty(bakPath)){
			resultMap.put("result", false);
			resultMap.put("message", "备份路径为空");
			return JSON.toJSONString(resultMap);
		}
		if(!JudgeUtil.isEmpty(filePath)&&new File(filePath).getParentFile().exists()){
			appPatchRootPath =new File(filePath).getParentFile().getAbsolutePath();
			/*解析补丁描述文件，生成部分规则文件,
				补丁包中，可能有多个应用,
				遍地补丁的根目录，如果没有补丁描述文件，则该目录是多个应用的目录，需要遍历备份，备份路径，自动追加一级（应用名目录），
			*/
			resultMap = parsePatchPackage(appPatchRootPath,appRootPath,bakPath+File.separator+resourceVersionId);
			if(!(Boolean)resultMap.get("result")){
				return JSON.toJSONString(resultMap);
			}else{
				resultMap.put("result", true);
				resultMap.put("message", "备份成功!");
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁文件不存在!");
		}
		return JSON.toJSONString(resultMap);
		
	}
	public Map<String,Object> parsePatchPackage(String appPatchRootPath,String appRootPaths,String bakPath){
		Map<String,Object> resultMap = new HashMap<>();
		Boolean successFlag=true;
		String message="";
		File patchRootFile = new File(appPatchRootPath);
		if(patchRootFile.exists()){
			File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory"));//多个应用
			String[] appPaths = appRootPaths.split("#");
			for(int i=0;i<appFiles.length;i++){
				String bakRootPath=bakPath+File.separator+appFiles[i].getName();
				File[] appXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.APP,Constants.FILE_SUFFIX_XML));
				String appRootPath="";
				if(appXMLFiles.length!=0){
					for(int j=0;j<appPaths.length;j++){
						String appNameStr = appPaths[j].substring(appPaths[j].lastIndexOf("/"));
						if(appNameStr.contains(appFiles[i].getName())){
							appRootPath = appPaths[j];
							break;
						}
					}
					Map<String,Object> result= parseAppXML(appXMLFiles[0],appRootPath,bakRootPath);
					if(!(Boolean)result.get("result")){
						return result;
					}
				}else{
					continue;
				}
			}
			successFlag=true;
			message="补丁备份成功！";
		}else{
			successFlag=false;
			message="补丁的根目录不存在";
		}
		resultMap.put("result", successFlag);
		resultMap.put("message", message);
		return resultMap;
	}
	public Map<String,Object> parseAppXML(File appXMLFile,String appRootPath,String bakRootPath){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> result = generateRuleAndBakFile(appXMLFile, appRootPath, bakRootPath);
		if(!(Boolean)result.get("result")){
			return result;
		}
		resultMap.put("result", true);
		resultMap.put("message", "解析描述文件成功");
		return resultMap;
	}
	
	public Map<String,Object> generateRuleAndBakFile(File xmlFile,String appRootPath,String bakRootPath){
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> resultMap = new HashMap<>();
		Boolean successFlag=true;
		String message="";
		InputStream xml=null;
		try {
			xml = new FileInputStream(xmlFile);
			XMLParser xp = new XMLParser();
			AppFiles appFiles = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			List<AppFile> list =appFiles.getFiles();
			for(AppFile appFile:list){
				String patchFilePath = appFile.getPath();
				if(!isWin&&patchFilePath.contains("\\")){
					patchFilePath=patchFilePath.replace("\\", File.separator);
				}else if(isWin&&patchFilePath.contains("//")){
					patchFilePath=patchFilePath.replace("//", File.separator);
				}
				//目标文件路径：应用的根路径+补丁的相对路径
				String appFilePath = appRootPath+File.separator+patchFilePath;
				if(new File(appFilePath).exists()){
					appFile.setIsNew(false);
				}else{
					appFile.setIsNew(true);
				}
				Boolean backUp=!appFile.getIsNew();
				if(backUp){//是否需要备份
					String sourcePath = appRootPath+File.separator+patchFilePath;
					String destPath=bakRootPath+File.separator+patchFilePath;
					if(new File(sourcePath).exists()){
						try {
							FileUtils.copyFile(new File(sourcePath), new File(destPath));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							successFlag=false;
							message="备份的源文件 "+sourcePath+"异常!"+LogRecord.getStackTrace(e);
							break;
						}
					}else{
						successFlag=false;
						message="备份的源文件 "+sourcePath+"不存在！";
						System.out.println("需备份的源文件 "+sourcePath+" 不存在！");
						break;
					}
				}else{
					continue;
				}
			}
			if(successFlag){
				String ruleFilePath=bakRootPath+File.separator+Constants.APP_RULE_XML;
				String fileContent = xp.marshall(appFiles, AppFiles.class);
				BufferedWriter writer=null;
				try {
					File ruleFile = new File(ruleFilePath);
					if(!ruleFile.getParentFile().exists()){
						ruleFile.getParentFile().mkdirs();
					}
					if(ruleFile.exists()){
						ruleFile.delete();
					}else{
						ruleFile.createNewFile();
					}
					writer = new BufferedWriter(new FileWriter(ruleFile));
					writer.write(fileContent);
					writer.flush();
					successFlag=true;
					message="备份成功!";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					successFlag=false;
					message="生成规则文件异常!"+LogRecord.getStackTrace(e);
				}finally {
					if(writer !=null)
						try {
							writer.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				successFlag=true;
				message="补丁备份成功";
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			successFlag=false;
			message="补丁描述文件不存在";
			e1.printStackTrace();
		} finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		resultMap.put("result", successFlag);
		resultMap.put("message", message);
		return resultMap;
	
	}
	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(APPROOTPATH))){
			this.appRootPath = this.pluginInput.get(APPROOTPATH).toString();
		}
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
	}
	public void setAppRootPath(String appRootPath) {
		this.appRootPath = appRootPath;
	}


	public void setResourceVersionId(String resourceVersionId) {
		this.resourceVersionId = resourceVersionId;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setBakPath(String bakPath) {
		this.bakPath = bakPath;
	}

	public void setAppPatchRootPath(String appPatchRootPath) {
		this.appPatchRootPath = appPatchRootPath;
	}
	public static void main(String[]args){
		String result="";
		BackUpApp4Bank backUpApp = new BackUpApp4Bank();
		backUpApp.setResourceVersionId("v1");
		backUpApp.setAppRootPath("F:/test/banktest/teller");
		backUpApp.setFilePath("F:/test/banktest/patch/EDRBANK_1.0_Build0260.zip");
		backUpApp.setBakPath("F:/test/banktest/bak");
		try {
			result=backUpApp.doAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result=="+result);
	}
}
