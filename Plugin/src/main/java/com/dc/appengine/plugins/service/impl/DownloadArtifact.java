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
import com.dc.appengine.plugins.utils.LogRecord;

public class DownloadArtifact extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DownloadArtifact.class);
	private static final String DEPLOYPATH_KEY="deployPath";
	public static final String RESOUCEURL = "artifactURL";//工件包资源的url key
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
		String url = this.resouceUrl;
		Map<String,Object> resultMap = new HashMap<>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("resourceUrl is null");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "resourceUrl is null");
			return JSON.toJSONString(resultMap);
		}
		String localPath = this.deployPath;
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
		File file = null;
		FtpTransferer ft =null;
		Boolean openResult= false;
		FtpInfo ftpInfo = null;
		try {
			ftpInfo = new FtpInfo(url);
			ft = new FtpTransferer(ftpInfo, localPath);
			openResult= ft.open();
			String remotePath = ftpInfo.getFile();
			if(openResult){
				Map<String,Object> checkResult = ft.checkFileExistence(url);
				Object result = checkResult.get("result");
				if(!(Boolean)result){
					resultMap =  checkResult;
				}else{
					file = ft.download(remotePath, new File(remotePath).getName());
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
				}
			}else{
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, LogRecord.getStackTrace(e));
		}finally {
			if(ftpInfo==null){
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp url异常！");
			}
			if(ft !=null && openResult){
				ft.close();
			}
		}
		return JSON.toJSONString(resultMap);
	}


	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DEPLOYPATH_KEY ))){
			this.deployPath=this.pluginInput.get(DEPLOYPATH_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.messageMap.get(RESOUCEURL ))){
			this.resouceUrl=decryptRoot(RESOUCEURL);
		}
	}


	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	
  public static void main(String[] args){
//	  String url="ftp://paas:123456@10.1.108.33/packages/6932a861-ac6b-4b93-8d81-443a33eb05da/tomcat.zip";
//	  FtpInfo ftpInfo = new FtpInfo(url);
	  String filePath="packages/6932a861-ac6b-4b93-8d81-443a33eb05da/tomcat.zip";
	  System.out.println(filePath);
	  File file = new File(filePath);
	  String parentPath = file.getParentFile().getPath();
	  System.out.println(file.getName());
//	  FtpTransferer ft =null;
//	  Boolean openResult= false;
//	  ft = new FtpTransferer(ftpInfo, "F:/test/");
//	  openResult= ft.open();
//	  System.out.println(openResult);
  }
}
