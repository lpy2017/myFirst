package com.dc.bank.appengine.plugins.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;
import com.dc.bank.appengine.plugins.constants.Constants;
import com.dc.bank.appengine.plugins.jaxb.AppFile;
import com.dc.bank.appengine.plugins.jaxb.AppFiles;

public class AppUtil {
	public Map<String,Object> parsePatchPackage(String appPatchRootPath,String appRootPaths,String bakPath){
		Map<String,Object> resultMap = new HashMap<String,Object>();
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
		Map<String,Object> resultMap = new HashMap<String,Object>();
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
		Map<String,Object> resultMap = new HashMap<String,Object>();
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
							message="备份的源文件 "+sourcePath+"异常!"+getStackTrace(e);
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
					message="生成规则文件异常!"+getStackTrace(e);
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
	
	public JSONObject copyFiles(String sourceRootPath, String appRootPath, String xmlPath,Boolean upgrad){
		JSONObject result = new JSONObject();
		try {
			Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
			InputStream xml = new FileInputStream(new File(xmlPath));
			XMLParser xp = new XMLParser();
			AppFiles appFiles = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			List<AppFile> list = appFiles.getFiles();
			for(AppFile file : list){
				String patchFilePath = file.getPath();
				if(!isWin&&patchFilePath.contains("\\")){
					patchFilePath=patchFilePath.replace("\\", File.separator);
				}else if(isWin&&patchFilePath.contains("//")){
					patchFilePath=patchFilePath.replace("//", File.separator);
				}
				
				String sourcePath = sourceRootPath+File.separator+patchFilePath;
				String destPath = appRootPath+File.separator+patchFilePath;
				if(!upgrad){//回滚操作
					Boolean isNew =file.getIsNew();
					if(isNew){//补丁是新增的，需要将补丁删除
						File destFile =new File(destPath);
						if(destFile.exists()){
							destFile.delete();
						}
					}else{
						if(!new File(sourcePath).exists()){
							result.put("result", false);
							result.put("message", "file ["+sourcePath+"] 不存在");
							break;
						}
						FileUtils.copyFile(new File(sourcePath), new File(destPath));//覆盖文件
					}
				}else{ //升级
					if(!new File(sourcePath).exists()){
						result.put("result", false);
						result.put("message", "file ["+sourcePath+"] 不存在");
						break;
					}
					if(file.getIsApendDeploy()&&patchFilePath.endsWith("jar")){
						FileUtil.mergeZipPackage(new File(destPath), new File(sourcePath));
					}else{
						FileUtils.copyFile(new File(sourcePath), new File(destPath));//覆盖文件
					};
				}
			}
			
		} catch (IOException e) {
			result.put("result", false);
			result.put("message", getStackTrace(e));
		}
		if(result.isEmpty()){
			result.put("result", true);
			result.put("message", upgrad?"升级成功！":"回滚成功！");
		}
		return result;
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
}
