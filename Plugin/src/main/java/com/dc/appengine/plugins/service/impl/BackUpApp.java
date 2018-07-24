package com.dc.appengine.plugins.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.XMLParser;

public class BackUpApp extends AbstractPlugin {
	private static final String APPROOTPATH  = "appRootPath";
	private static final String BAKPATH  = "bakPath";
	private static final String FILEPATH  = "filePath";
    private String appRootPath="";        //目标应用的根路径
    private String resourceVersionId=""; //组件版本的id
    private String appPatchRootPath =""; //补丁文件的根路径
    private String filePath=""; //补丁文件的路径
    private String bakPath=""; //备份路径
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(BackUpApp.class);
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
			File rootFile = new File(appPatchRootPath);
			Map<String,File> records = new HashMap<>();
			recordFiles(rootFile, records);
			if(!JudgeUtil.isEmpty(records)){
				String backUpPath = bakPath+File.separator+resourceVersionId+File.separator+"app";
				String ruleFilePath=backUpPath+File.separator+Constants.APP_RULE_XML;
				generateRuleFile(records,ruleFilePath,appRootPath);
				File rule =new File(ruleFilePath);
				if(rule.exists()){
					//使用规则文件，备份目标文件
					backUpFile(rule, backUpPath, appRootPath);
					resultMap.put("result", true);
					resultMap.put("message", "备份成功！");
				}else{
					resultMap.put("result", false);
					resultMap.put("message", "生成规则文件异常！");
				}
			}else{
				resultMap.put("result", false);
				resultMap.put("message", "获取补丁文件异常！");
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁文件不存在!");
		}
		return JSON.toJSONString(resultMap);
		
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
	
	public static void recordFiles(File rootFile,Map<String,File> records){
		File[] files = rootFile.listFiles();
		//递归遍历文件夹
		for(File file :files){
			if(file.isDirectory()){
				recordFiles(file, records);
			}else{
				String abPath = file.getAbsolutePath();
				String path ="";
				if(abPath.contains(File.separator+"classes"+File.separator)){
					int index = abPath.indexOf(File.separator+"classes"+File.separator);
					path =  abPath.substring(index);
					records.put(path,file);
				}else{
					continue;
				}
			}
		}
	}
	
	public static void generateRuleFile(Map<String, File> patchFilesMap,String filePath,String appRootPath){
		List<AppFile> List = new ArrayList<>();
		File appFile = new File(appRootPath);
		Map<String, File> appFilesMap= new HashMap<>();
		recordFiles(appFile, appFilesMap);
		for(Map.Entry<String, File> patch:patchFilesMap.entrySet()){
			String patchPath = patch.getKey();
			AppFile appfile = new AppFile();
			appfile.setIsApendDeploy(false);
			appfile.setIsNew(true);
			for(Map.Entry<String, File> app:appFilesMap.entrySet()){
				String appPath = app.getKey();
				if(patchPath.equals(appPath)){//目标文件和源文件相同，则备份
					appfile.setIsNew(false);
					break;
				}
			}
			appfile.setPath(patchPath);
			List.add(appfile);
		}
		XMLParser xp = new XMLParser();
		AppFiles appFiles = new AppFiles();
		appFiles.setFiles(List);
		String fileContent = xp.marshall(appFiles, AppFiles.class);
		BufferedWriter writer=null;
		try {
			File ruleFile = new File(filePath);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer !=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public static void backUpFile(File ruleFile,String backUpPath,String appRootPath) throws IOException{
		InputStream xml=null;
		try {
			xml = new FileInputStream(ruleFile);
			XMLParser xp = new XMLParser();
			AppFiles appFiles = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			List<AppFile> list =appFiles.getFiles();
			for(AppFile appFile:list){
				Boolean backUp=!appFile.getIsNew();
				String patchFilePath = appFile.getPath();
				if(backUp){//是否需要备份
					if(!appRootPath.endsWith("WEB-INF")){
						appRootPath=appRootPath+File.separator+"WEB-INF";
					}
					String sourcePath = appRootPath+File.separator+patchFilePath;
					String destPath=backUpPath+File.separator+patchFilePath;
					FileUtils.copyFile(new File(sourcePath), new File(destPath));//备份
				}else{
					continue;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
		BackUpApp backUpApp = new BackUpApp();
		backUpApp.setResourceVersionId("hsntest");
		backUpApp.setAppRootPath("F:/test/webApp");
		backUpApp.setFilePath("F:/test/patch/class123.zip");
		backUpApp.setBakPath("F:/test/tmp");
		try {
			result=backUpApp.doAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result=="+result);
	}
}
