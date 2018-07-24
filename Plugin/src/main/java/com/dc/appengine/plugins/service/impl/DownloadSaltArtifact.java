package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.CreateComponentFile;
import com.dc.appengine.plugins.utils.ExecLinuxCmd;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.FtpInfo;
import com.dc.appengine.plugins.utils.JavaZipUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.sdk.SDKUtil;

public class DownloadSaltArtifact extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DownloadSaltArtifact.class);
	private static final String DEPLOYPATH_KEY="deployPath";
	public static final String RESOUCEURL = "artifactURL";//工件资源的url key
	private static final String FILEPATH_KEY="filePath";
	private static final String ENCODING  = "encoding";
	private String encoding;
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
					if (file != null) {
						resultMap.put(Constants.Plugin.RESULT, true);
						resultMap.put(FILEPATH_KEY, file.getAbsolutePath());
						resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath ="+file.getAbsolutePath());

						//解压工件包 删除工件包  unzip  deleteFile
						Map<String,String> subFilesMap=new HashMap<>();
						if(subFilesMap.isEmpty()){
							subFilesMap.put(file.getName(), "/");
						}
						Map<String, Object> unzipResult = JavaZipUtil.unzip(file.getAbsolutePath(),true,subFilesMap);
						log.debug("unzip finished result is " + JSON.toJSONString(resultMap));
						//生成文件对应关系  traverseFolder
						 List<String> fileList = new ArrayList<String>();
						 fileList = FileUtil.traverseFolder(localPath,fileList);
						Map<String, String> fileMap = new HashMap<>();
						for (String string : fileList) {
							//			string = string.replace("\\", "/");
							fileMap.put(string, string);

						}
//						//获取组件input和output 以及CreateComponentFile commonMap
						String instanceId = (String) messageMap.get(Constants.Plugin.INSTANCEID);
						String logicalVersion =(String) messageMap.get(Constants.Plugin.LOGICALVERSION);
//						Map<String,Object> configMap   = CreateComponentFile.commonMap(instanceId+file.separator+logicalVersion);
//						Map<String,String> extreMap  = new  HashMap<String,String>();
//						Map<String, String> config = new HashMap<String,String>();
//						config.putAll((Map<String,String>) messageMap.get(Constants.Plugin.COMPONENTINPUT));
//						config.putAll((Map<String,String>) messageMap.get(Constants.Plugin.COMPONENTOUTPUT));
//						
//						for(Map.Entry<String,String> entry:config.entrySet()){
//							String key = entry.getKey();
//							String freeVlaue = 	"${".concat(key).concat("?if_exists}");
//							extreMap.put(key, freeVlaue);
//						}
//						configMap.putAll(extreMap);
//						SDKUtil sdkUtil = new SDKUtil();
//						//使用freemark 替换自身生成工件文件夹
//						Map<String,Object> freeMarkresult = sdkUtil.configSdk(configMap, fileMap,encoding);

						
						String replaceInstanceId = instanceId+file.separator+logicalVersion;
						for (Map.Entry<String, String> template : fileMap.entrySet()) {
							String filePath = template.getValue();
							File filea = new File(filePath);
							FileInputStream fi = null;
							FileOutputStream fo = null;
							try {
								fi = new FileInputStream(filea);
								String exStr=	IOUtils.toString(fi,encoding);
								String afStr = exStr.replace("${instanceId?if_exists}", replaceInstanceId);
								fo = new FileOutputStream(filea);
								IOUtils.write(afStr, fo,encoding);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}  catch (IOException e) {
								e.printStackTrace();
							}
							
							String path = "";
							String[] paths = localPath.split("/");
							for (int i = 0; i < paths.length - 1; i++) {
								path+=paths[i];
								path+="/";
							}

							
							String chmodcmd = "sudo chmod -R 755 "+path;
							pluginInput.put("CMD", chmodcmd);
							String  cmdResult = ExecLinuxCmd.execCmd(pluginInput,this.paramMap);
							
							log.info(cmdResult);
							System.out.println(cmdResult);
						}
						
//						if((boolean) freeMarkresult.get("result")){
//							log.info((String)freeMarkresult.get("message"));
//						}
					
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
		if(!JudgeUtil.isEmpty((String)messageMap.get(DEPLOYPATH_KEY))){
			this.deployPath = (String)messageMap.get(DEPLOYPATH_KEY);
		}
		if(!JudgeUtil.isEmpty(messageMap.get(RESOUCEURL))){
			this.resouceUrl=decryptRoot(RESOUCEURL);
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(ENCODING))){
			this.encoding = this.pluginInput.get(ENCODING).toString();
		}else{
			encoding ="UTF-8";
		}
	
	}


	public void setDeployPath(String deployPath) {
		this.deployPath = deployPath;
	}
	

}
