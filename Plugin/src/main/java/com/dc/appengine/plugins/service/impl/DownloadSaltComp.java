package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.FtpInfo;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.RecursionDownload;

public class DownloadSaltComp extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DownloadSaltComp.class);
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
		final String json=JSON.toJSONString(paramMap);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result;
				try {
					result = doActive(json);
					resultMap = JSON.parseObject(result,new TypeReference<Map<String,Object>>(){});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.error(LogRecord.getStackTrace(e));
					resultMap.put(Constants.Plugin.RESULT, false);
					resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
				}
				if(gf_variable != null){//设置方法的执行结果
					messageMap.put(gf_variable,(Boolean)resultMap.get(Constants.Plugin.RESULT));
					AbstractPlugin.setGFVariable(gf_variable, resultMap.get(Constants.Plugin.RESULT).toString(),paramMap);
				}
				messageMap.put(PLUGIN_RESULT_KEY,(Boolean)resultMap.get(Constants.Plugin.RESULT));
				//记录日志
				messageMap.put(Constants.Plugin.RESULT, JSON.toJSONString(resultMap));
				paramMap.put(Constants.Plugin.MESSAGE, messageMap);
				updatePluginLog(paramMap.get(Constants.Plugin.PLUGINNAME)+"do doActive is end outputParam==="+JSON.toJSONString(paramMap)
				+System.lineSeparator()+"plugin_result_active:"+resultMap.get(Constants.Plugin.RESULT)+System.lineSeparator()+"result_message:"+System.lineSeparator()+resultMap.get(Constants.Plugin.RESULT_MESSAGE));
				//触发工作流
				invokeWorkflowServer(paramMap);
			}
		};
		Thread t = new Thread(runnable);
		t.start();
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
		if(!JudgeUtil.isEmpty(this.messageMap.get(DEPLOYPATH_KEY ))){
			this.deployPath=this.messageMap.get(DEPLOYPATH_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(messageMap.get(RESOUCEURL))){
			this.resouceUrl=decryptRoot(RESOUCEURL);
		}
	}


	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	

}
