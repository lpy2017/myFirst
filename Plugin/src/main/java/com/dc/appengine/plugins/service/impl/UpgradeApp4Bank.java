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
import com.dc.appengine.plugins.utils.FileFilterClass;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.XMLParser;

public class UpgradeApp4Bank extends AbstractPlugin {
	private static final String APPROOTPATH  = "appRootPath";
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
	private static Logger log = LoggerFactory.getLogger(UpgradeApp4Bank.class);
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
		appPatchRootPath =new File(filePath).getParentFile().getAbsolutePath();
		File patchRootFile = new File(appPatchRootPath);
		if (patchRootFile.exists()) {
			String[] appPaths = appRootPath.split("#");
			if (upGrade) {
				File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory"));// 多个应用
				for (int i = 0; i < appFiles.length; i++) {
					File[] appXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.APP,Constants.FILE_SUFFIX_XML));
					String appRootPath = "";
					if (appXMLFiles.length != 0) {
						for (int j = 0; j < appPaths.length; j++) {
							String appNameStr = appPaths[j].substring(appPaths[j].lastIndexOf("/"));
							if (appNameStr.contains(appFiles[i].getName())) {
								appRootPath = appPaths[j];
								break;
							}
						}
						File[] patchParents = appXMLFiles[0].getParentFile().listFiles(new FileFilterClass("isDirectory","app"));
						resultMap=copyFiles(patchParents[0].getAbsolutePath(),appRootPath,appXMLFiles[0].getAbsolutePath(),upGrade);
						if(!resultMap.getBooleanValue("result")){
							return JSON.toJSONString(resultMap);
						}
					} else {
						continue;
					}
				}
				resultMap.put("result", true);
				resultMap.put("message", "升级成功！");
			} else {
				String backUpRootPath = bakPath+File.separator+resourceVersionId;
				File[] appFiles = new File(backUpRootPath).listFiles(new FileFilterClass("isDirectory"));
				for(int i=0;i<appFiles.length;i++){
					//判断备份路径
					File[] backUpFiles= appFiles[i].listFiles();
					if(backUpFiles.length<2){
						resultMap.put("result", false);
						resultMap.put("message", "不存在备份文件！");
						return JSON.toJSONString(resultMap);
					}else{
						//回退文件
						String ruleFilePath=appFiles[i].getAbsolutePath()+File.separator+Constants.APP_RULE_XML;
						resultMap=copyFiles(appFiles[i].getAbsolutePath(),appRootPath,ruleFilePath,upGrade);
						if(!resultMap.getBooleanValue("result")){
							return JSON.toJSONString(resultMap);
						}
					}
				}
			}
		} else {
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录不存在");
		}
		return JSON.toJSONString(resultMap);
	}

	private JSONObject copyFiles(String sourceRootPath, String appRootPath, String xmlPath,Boolean upgrad){
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
			result.put("message", LogRecord.getStackTrace(e));
		}
		if(result.isEmpty()){
			result.put("result", true);
			result.put("message", upGrade?"升级成功！":"回滚成功！");
		}
		return result;
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
			this.filePath = (String)pluginInput.get(FILEPATH);
		}else{
			if(!JudgeUtil.isEmpty(this.messageMap.get(FILEPATH))){
				this.filePath = (String)messageMap.get(FILEPATH);
			}
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(UPGRADE))){
			this.upGrade = Boolean.valueOf(this.pluginInput.get(UPGRADE).toString());
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

	public void setUpGrade(Boolean upGrade) {
		this.upGrade = upGrade;
	}

	public static void main(String[]args){
		String result = "";
		UpgradeApp4Bank upgradeApp = new UpgradeApp4Bank();
		upgradeApp.setResourceVersionId("v1");
		upgradeApp.setAppRootPath("F:/test/banktest/teller");
		upgradeApp.setFilePath("F:/test/banktest/patch/EDRBANK_1.0_Build0260.zip");
		upgradeApp.setBakPath("F:/test/banktest/bak");
		upgradeApp.setUpGrade(false);
		try {
			result = upgradeApp.doAgent();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result=="+result);
	}
}
