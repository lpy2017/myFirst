package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.command.CommandResult;
import com.dc.appengine.plugins.command.analyser.impl.EchoAnalyseer;
import com.dc.appengine.plugins.command.executor.CommandNoWaitExecutor;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.JavaZipUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class Unzip extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Unzip.class);
	private static final String PACKAGEPATH_KEY  = "packagePath";
	private static final String FILEPARAMS_KEY  = "fileParams";
	private String packagePath;
	private String fileParams;//path1#targetPath1,path2#targetPath2
	@Override
	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		Map<String,String> subFilesMap=new HashMap<>();
		if(fileParams !=null){
			String [] params = fileParams.split(",");
			for(String one : params){
				String [] subFileParam= one.split("#");
				if(subFileParam.length == 1){
					subFilesMap.put(subFileParam[0], subFileParam[1]);
				}
			}
		}
		String file = FilePath.getFilePath(packagePath);
		File zipFile = new File(file);
		if(subFilesMap.isEmpty()){
			subFilesMap.put(zipFile.getName(), "/");
		}
		String target = new File(file).getParent();
		Map<String, Object> resultMap = JavaZipUtil.unzip(file,true,subFilesMap);
		log.debug("unzip finished result is " + JSON.toJSONString(resultMap));
		boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		if(!isWin){
			CommandResult.getResult(new EchoAnalyseer(),
					CommandNoWaitExecutor.class, "chmod -R +x " + target, true);
		}
		return JSON.toJSONString(resultMap);
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(PACKAGEPATH_KEY ))){
			this.packagePath=this.pluginInput.get(PACKAGEPATH_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FILEPARAMS_KEY ))){
			this.fileParams=this.pluginInput.get(FILEPARAMS_KEY ).toString();
		}
	}

	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}

	public void setFileParams(String fileParams) {
		this.fileParams = fileParams;
	}

	public static void main(String[] args){
		Map<String,String> subFilesMap=new HashMap<>();
		String file = "F:/test/itsm_war_0815.zip";
		File zipFile = new File(file);
		if(subFilesMap.isEmpty()){
			subFilesMap.put(zipFile.getName(), "/");
		}
		Map<String, Object> resultMap = JavaZipUtil.unzip(file,false,subFilesMap);
		log.debug("unzip finished result is " + JSON.toJSONString(resultMap));
	}

}
