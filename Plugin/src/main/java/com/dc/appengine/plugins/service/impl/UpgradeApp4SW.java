package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.jaxb.AppFile;
import com.dc.appengine.plugins.jaxb.AppFiles;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.XMLParser;

public class UpgradeApp4SW extends AbstractPlugin {
	private static final String APPROOTPATH  = "appRootPath";
	private static final String APPPATCHROOTPATH  = "appPatchRootPath";
	private static final String UPGRADE  = "upGrade";
	private static final String BAKPATH  = "bakPath";
	private static final String FILEPATH  = "filePath";
    private String appRootPath=""; 
    private String resourceVersionId="";
    private String filePath ="";
    private String appPatchRootPath ="";
    private String bakPath="";
    private Boolean upGrade=true;
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(UpgradeApp4SW.class);
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
		JSONObject resultMap = new JSONObject();
		if(JudgeUtil.isEmpty(appRootPath)){
        	resultMap.put("result", false);
    		resultMap.put("message", "应用路径为空");
    		return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(bakPath)){
			resultMap.put("result", false);
			resultMap.put("message", "备份路径为空");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(appPatchRootPath)){
			resultMap.put("result", false);
			resultMap.put("message", "应用补丁的根路径为空");
			return JSON.toJSONString(resultMap);
		}
		//升级
		String backUpPath = bakPath+File.separator+resourceVersionId+File.separator+"app";
		String ruleFilePath=backUpPath+File.separator+Constants.APP_RULE_XML;
		File ruleFile =new File(ruleFilePath);
		if(ruleFile.exists()){
			if(upGrade){
				if(JudgeUtil.isEmpty(filePath) || !new File(filePath).getParentFile().exists()){
					resultMap.put("result", false);
					resultMap.put("message", "补丁文件不存在");
					return JSON.toJSONString(resultMap);
				}
				appPatchRootPath=new File(appPatchRootPath).getAbsolutePath();
				resultMap=upgrade(appPatchRootPath,appRootPath,ruleFilePath,upGrade);
			}else{
				//判断备份路径
				File[] backUpFiles= new File(backUpPath).listFiles();
				if(backUpFiles.length<2){
					resultMap.put("result", false);
					resultMap.put("message", "不存在备份文件！");
				}else{
					//回退文件
					resultMap=upgrade(backUpPath,appRootPath,ruleFilePath,upGrade);
				}
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "不存在规则文件！");
		}
		return JSON.toJSONString(resultMap);
	}

	private JSONObject upgrade(String sourceRootPath, String appRootPath, String ruleFilePath,Boolean upgrad){
		JSONObject result = new JSONObject();
		try {
			File ruleFile =new File(ruleFilePath);
			InputStream xml = new FileInputStream(ruleFile);
			XMLParser xp = new XMLParser();
			AppFiles appFiles = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			List<AppFile> list = appFiles.getFiles();
			for(AppFile file : list){
				String sourcePath = sourceRootPath+file.getPath();
				String destPath = appRootPath+file.getPath();
				if(!new File(sourcePath).exists()){
					if(file.getIsPatch()){
						result.put("result", false);
						result.put("message", "file ["+sourcePath+"] 不存在");
						break;
					}else{
						continue;
					}

				}else{
					Boolean isNew =file.getIsNew();
					if(!upgrad){//回滚操作
						if(isNew){//补丁是新增的，需要将补丁删除
							File destFile =new File(destPath);
							if(destFile.exists()){
								destFile.delete();
							}
						}
//						else{
//							FileUtils.copyFile(new File(sourcePath), new File(destPath));//覆盖文件
//						}
					}else{ //升级
						FileUtils.copyFile(new File(sourcePath), new File(destPath));//覆盖文件
					}
				}
			}
			if(!upgrad){
				//回滚备份的文件
				copyFile(ruleFile.getParentFile(), sourceRootPath, appRootPath);
			}
			
		} catch (IOException e) {
			result.put("result", false);
			result.put("message", LogRecord.getStackTrace(e));
		}
		if(result.isEmpty()){
			result.put("result", true);
			result.put("message", upGrade?"升级成功！":"回滚成功！");
		}
		return result;
	}
	public static void copyFile(File sourceFile,String sourceRootPath,String targetRootPath) throws IOException{
		File[] files = sourceFile.listFiles();
		//递归遍历文件夹
		for(File file :files){
			if(file.isDirectory()){
				copyFile(file, sourceRootPath, targetRootPath);
			}else{
				if(!file.getName().equals(Constants.APP_RULE_XML)){
					String sourceFilePath=file.getAbsolutePath();
					String path=sourceFilePath.substring(sourceFilePath.indexOf("app")+3);
					String targetFilePath=targetRootPath+path;
					FileUtils.copyFile(file, new File(targetFilePath));//覆盖文件
				}
			}
		}
	}
	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(APPROOTPATH))){
			this.appRootPath = this.pluginInput.get(APPROOTPATH).toString();
		}
		if(!JudgeUtil.isEmpty(this.messageMap.get("resourceVersionId"))){
			this.resourceVersionId = this.messageMap.get("resourceVersionId").toString();
		}
//		if(!JudgeUtil.isEmpty(this.pluginInput.get(FILEPATH))){
//			this.filePath = (String)pluginInput.get(FILEPATH);
//		}else{
//			if(!JudgeUtil.isEmpty(this.messageMap.get(FILEPATH))){
//				this.filePath = (String)messageMap.get(FILEPATH);
//			}
//		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(UPGRADE))){
			this.upGrade = Boolean.valueOf(this.pluginInput.get(UPGRADE).toString());
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(BAKPATH))){
			this.bakPath = (String)pluginInput.get(BAKPATH);
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(APPPATCHROOTPATH))){
			this.appPatchRootPath = (String)pluginInput.get(APPPATCHROOTPATH);
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

	public void setUpGrade(Boolean upGrade) {
		this.upGrade = upGrade;
	}

	public static void main(String[]args){
		String result = "";
		UpgradeApp4SW upgradeApp = new UpgradeApp4SW();
		upgradeApp.setResourceVersionId("hsntest");
		upgradeApp.setAppRootPath("F:/test/xinan/webApp/hsswcl");
		upgradeApp.setFilePath("F:/test/xinan/patch/hsswcl.zip");
		upgradeApp.setBakPath("F:/test/xinan/tmp");
		upgradeApp.setUpGrade(true);
		try {
			result = upgradeApp.doAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result=="+result);
	}
}
