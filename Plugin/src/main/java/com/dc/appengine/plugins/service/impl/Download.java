package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.FtpInfo;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class Download extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Download.class);
	private static final String DEPLOYPATH_KEY="deployPath";
	public static final String RESOUCEURL = "resouceUrl";//资源的url key
	private static final String FILEPATH_KEY="filePath";
	private static final String SALT_DEFAULT="salt_default";
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
		Object result =messageMap.get("result");
		if(result != null){
			Map<String, Object> resultMap= JSON.parseObject(result.toString(), new TypeReference<Map<String, Object>>() {
			});
			messageMap.put(FILEPATH_KEY, resultMap.get("filePath"));
		}
		paramMap.put(Constants.Plugin.MESSAGE, messageMap);
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		String url = (String) messageMap.get(Constants.Plugin.URL);
		if(!JudgeUtil.isEmpty(this.resouceUrl)){
			url = this.resouceUrl;
		}
		
		Map<String,Object> resultMap = new HashMap<>();
		if (url == null) {
			log.error("resourceUrl is null");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "resourceUrl is null");
			return JSON.toJSONString(resultMap);
		}
		String localPath = (String)pluginInput.get(DEPLOYPATH_KEY);
		if (localPath == null) {
			log.error("deployPath is null");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "deployPath is null");
			return JSON.toJSONString(resultMap);
		}
		
		localPath = FilePath.getFilePath(localPath);
		if (!localPath.endsWith("/")) {
			localPath += "/";
		}

		File file = null;
		FtpTransferer ft =null;
		Boolean openResult= false;
		try {
			FtpInfo ftpInfo = new FtpInfo(url);
			ft = new FtpTransferer(ftpInfo, localPath);
			openResult= ft.open();
			String remotePath = ftpInfo.getFile();
			if(openResult){
				file = ft.download(remotePath, new File(remotePath).getName());
			}else{
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		} finally {
			if(ft !=null && openResult)
			ft.close();
		}
		log.debug("download finished result is "
				+ (file != null ? file.getAbsolutePath() : false));

		if (file != null) {
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(FILEPATH_KEY, file.getAbsolutePath());
			resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath ="+file.getAbsolutePath());
		} else {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "下载失败！");
		}
		return JSON.toJSONString(resultMap);
	}


	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DEPLOYPATH_KEY ))){
			this.deployPath=this.pluginInput.get(DEPLOYPATH_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(RESOUCEURL ))){
			this.resouceUrl=this.pluginInput.get(RESOUCEURL ).toString();
		}
	}


	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	

}
