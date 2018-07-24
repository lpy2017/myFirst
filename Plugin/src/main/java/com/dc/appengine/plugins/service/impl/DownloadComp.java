package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.FtpInfo;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.RecursionDownload;

public class DownloadComp extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DownloadComp.class);
	private static final String DEPLOYPATH_KEY="deployPath";
	public static final String RESOUCEURL = "compURL";//组件资源的url key
	private static final String FILEPATH_KEY="filePath";
	private String deployPath;
	private String resouceUrl;
	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}


	@Override
	public String doInvoke() {
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}
	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		String url= this.resouceUrl;
		Map<String,Object> resultMap = new HashMap<>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("resourceUrl is null");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "resourceUrl is null");
			return JSON.toJSONString(resultMap);
		}
		
		String localPath=this.deployPath;
		if (JudgeUtil.isEmpty(localPath)) {
			log.error("deployPath is null");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "deployPath is null");
			return JSON.toJSONString(resultMap);
		}
		
		localPath = FilePath.getFilePath(localPath);
		if (!localPath.endsWith("/")) {
			localPath += "/";
		}
		FtpInfo ftpInfo = new FtpInfo(url);
		String filePath = ftpInfo.getFile();
		String ftpBasePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		String downloadDir = filePath.substring(filePath.lastIndexOf("/") + 1);
		RecursionDownload download = new RecursionDownload(ftpBasePath, localPath, ftpInfo);
		download.connect();
		download.getDir(downloadDir);
		resultMap = download.getResult();
		download.close();
		if(resultMap.get("result") ==null){
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "下载成功");
		}	
		return JSON.toJSONString(resultMap);
	}


	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DEPLOYPATH_KEY ))){
			this.deployPath=this.pluginInput.get(DEPLOYPATH_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(messageMap.get(RESOUCEURL))){
			this.resouceUrl=decryptRoot(RESOUCEURL);
		}
	}


	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	

}
