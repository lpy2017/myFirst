package com.dc.cd.plugins.utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.bank.appengine.plugins.constants.Constants;
import com.dc.bank.appengine.plugins.utils.AppUtil;
import com.dc.bank.appengine.plugins.utils.FileFilterClass;
import com.dc.bank.appengine.plugins.utils.JudgeUtil;
import com.dc.bank.appengine.plugins.utils.OracleUtil;
import com.dc.bank.appengine.plugins.utils.SystemUtil;
import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;

public class BankPlugin extends BaseAgentPlugin {

	private static final Logger log = LoggerFactory.getLogger(BankPlugin.class);
	public static String sysEncoding=System.getProperty("sun.jnu.encoding");//操作系统编码
	
	public JobExecResultDto execute(JobDetailDto detailDTO) {
		log.info("demoplugin receive job");
		log.info("action:{}", detailDTO.getAction());
		log.info("deviceIP:{}", detailDTO.getDeviceIp());
		log.info("JobDetailParam:{}", detailDTO.getJobDetailParam());
		log.info("Encode:{}", detailDTO.getEncode());
		log.info("jobid:{}", detailDTO.getJobId());
		log.info("JobInstId:{}", detailDTO.getJobInstId());
		log.info("jobname:{}", detailDTO.getJobName());
		log.info("NodeGrpId:{}", detailDTO.getNodeGrpId());
		log.info("nodeid:{}", detailDTO.getNodeId());
		log.info("PluginCode:{}", detailDTO.getPluginCode());
		log.info("ProtocolList:{}", detailDTO.getProtocolList());
		log.info("Timeout:{}", detailDTO.getTimeout());
		log.info("TypeCode:{}", detailDTO.getTypeCode());
		log.info("uuid:{}", detailDTO.getUuid());
		
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		try {
			Map<String, Object> pluginInput = JSON.parseObject(detailDTO.getJobDetailParam());
			if (Constants.Plugin.TYPE_CODE_BACKUPAPP4BANK.equals(detailDTO.getTypeCode())) {
				return backUpApp4Bank(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_UPGRADEAPP4BANK.equals(detailDTO.getTypeCode())) {
				return upgradeApp4Bank(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_BACKUPORACLE4BANK.equals(detailDTO.getTypeCode())) {
				return backUpOracle4Bank(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_UPGRADEORACLE4BANK.equals(detailDTO.getTypeCode())) {
				return upgradeOracle4Bank(detailDTO, pluginInput);
			}else{
				// 未找到匹配的typecode
				execResultDto.setSuccess(false);
				execResultDto.setMsg("未找到对应的typecode");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			execResultDto.setSuccess(false);
			execResultDto.setMsg(getStackTrace(e));
		}
		return execResultDto;
	}
	
	public JobExecResultDto backUpApp4Bank(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());

		Map<String, Object> resultMap = new HashMap<String, Object>();
		//1.生成规则文件 2.按规则文件备份应用
		//备份文件的根路径rootPath
		//rootPath+"/"+${resourceVersionId}+"/"+app
		String appRootPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPROOTPATH))){
			appRootPath = pluginInput.get(Constants.Plugin.APPROOTPATH).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("应用路径为空");
			return execResultDto;
		}
		String resourceVersionId="";
		if(!JudgeUtil.isEmpty(messageMap.get("resourceVersionId"))){
			resourceVersionId = messageMap.get("resourceVersionId").toString();
		}
		String filePath = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
		}else{
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
			}
		}
		String bakPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.BAKPATH))){
			bakPath = (String)pluginInput.get(Constants.Plugin.BAKPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "备份路径为空");
			return execResultDto;
		}
		
		File file = new File(appRootPath);
		if(!file.exists()){
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "应用路径 "+appRootPath+" 不存在！");
			return execResultDto;
		}
		if(!JudgeUtil.isEmpty(filePath)&&new File(filePath).getParentFile().exists()){
			String appPatchRootPath =new File(filePath).getParentFile().getAbsolutePath();
			/*解析补丁描述文件，生成部分规则文件,
				补丁包中，可能有多个应用,
				遍地补丁的根目录，如果没有补丁描述文件，则该目录是多个应用的目录，需要遍历备份，备份路径，自动追加一级（应用名目录），
			*/
			resultMap = new AppUtil().parsePatchPackage(appPatchRootPath,appRootPath,bakPath+File.separator+resourceVersionId);
			if(!(Boolean)resultMap.get("result")){
				execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
				execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
				return execResultDto;
			}else{
				resultMap.put("result", true);
				resultMap.put("message", "备份成功!");
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁文件不存在!");
		}
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	public JobExecResultDto upgradeApp4Bank(JobDetailDto detailDTO,Map<String,Object> pluginInput)  throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());

		JSONObject resultMap = new JSONObject();
		
		String appRootPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPROOTPATH))){
			appRootPath = pluginInput.get(Constants.Plugin.APPROOTPATH).toString();
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg("应用路径为空");
			return execResultDto;
		}
		String resourceVersionId="";
		if(!JudgeUtil.isEmpty(messageMap.get("resourceVersionId"))){
			resourceVersionId = messageMap.get("resourceVersionId").toString();
		}
		String filePath = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
		}else{
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
			}
		}
		String bakPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.BAKPATH))){
			bakPath = (String)pluginInput.get(Constants.Plugin.BAKPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "备份路径为空");
			return execResultDto;
		}
		Boolean upGrade=true;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.UPGRADE))){
			upGrade = Boolean.valueOf(pluginInput.get(Constants.Plugin.UPGRADE).toString());
		}
		
		String appPatchRootPath =new File(filePath).getParentFile().getAbsolutePath();
		File patchRootFile = new File(appPatchRootPath);
		if (patchRootFile.exists()) {
			String[] appPaths = appRootPath.split("#");
			if (upGrade) {
				File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory"));// 多个应用
				for (int i = 0; i < appFiles.length; i++) {
					File[] appXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.APP,Constants.FILE_SUFFIX_XML));
					String appRootPathTmp = "";
					if (appXMLFiles.length != 0) {
						for (int j = 0; j < appPaths.length; j++) {
							String appNameStr = appPaths[j].substring(appPaths[j].lastIndexOf("/"));
							if (appNameStr.contains(appFiles[i].getName())) {
								appRootPathTmp = appPaths[j];
								break;
							}
						}
						File[] patchParents = appXMLFiles[0].getParentFile().listFiles(new FileFilterClass("isDirectory","app"));
						resultMap=new AppUtil().copyFiles(patchParents[0].getAbsolutePath(),appRootPathTmp,appXMLFiles[0].getAbsolutePath(),upGrade);
						if(!resultMap.getBooleanValue("result")){
							execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
							execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
							return execResultDto;
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
						execResultDto.setSuccess(false);
						execResultDto.setMsg("不存在备份文件！");
						return execResultDto;
					}else{
						//回退文件
						String ruleFilePath=appFiles[i].getAbsolutePath()+File.separator+Constants.APP_RULE_XML;
						resultMap=new AppUtil().copyFiles(appFiles[i].getAbsolutePath(),appRootPath,ruleFilePath,upGrade);
						if(!resultMap.getBooleanValue("result")){
							execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
							execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
							return execResultDto;
						}
					}
				}
			}
		} else {
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录不存在");
		}
	
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	
	
	
	public JobExecResultDto backUpOracle4Bank(JobDetailDto detailDTO,Map<String,Object> pluginInput)  throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String oracleUrl;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.ORACLE_URL))){
			oracleUrl = (String)pluginInput.get(Constants.Plugin.ORACLE_URL);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "oracle数据库实例的url为空");
			return execResultDto;
		}
		String bakPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.BAKPATH))){
			bakPath = (String)pluginInput.get(Constants.Plugin.BAKPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "备份路径为空");
			return execResultDto;
		}
		String filePath = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
		}else{
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
			}
		}
		String appName = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPNAME))){
			appName = (String)pluginInput.get(Constants.Plugin.APPNAME);
		}
		String resourceVersionId="";
		if(!JudgeUtil.isEmpty(messageMap.get("resourceVersionId"))){
			resourceVersionId = messageMap.get("resourceVersionId").toString();
		}
		int timeOut=60;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.TIMEOUT_KEY))){
			timeOut = Integer.valueOf(pluginInput.get(Constants.Plugin.TIMEOUT_KEY).toString());
		}
		if (!JudgeUtil.isEmpty(filePath) && new File(filePath).getParentFile().exists()) {
			String patchFileRootPath = new File(filePath).getParentFile().getAbsolutePath();// 补丁的根路径
			/*
			 * 解析补丁描述文件，生成部分规则文件, 补丁包中，可能有多个应用,
			 * 遍地补丁的根目录，如果没有补丁描述文件，则该目录是多个应用的目录，需要遍历备份，备份路径，自动追加一级（应用名目录），
			 */
			resultMap = new OracleUtil(oracleUrl, timeOut).parsePatchPackage(patchFileRootPath, appName, bakPath + File.separator + resourceVersionId);
		} else {
			resultMap.put("result", false);
			resultMap.put("message", "补丁文件不存在!");
		}
	
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	public JobExecResultDto upgradeOracle4Bank(JobDetailDto detailDTO,Map<String,Object> pluginInput)  throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());

		String oracleUrl;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.ORACLE_URL))){
			oracleUrl = (String)pluginInput.get(Constants.Plugin.ORACLE_URL);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "oracle数据库实例的url为空");
			return execResultDto;
		}
		String filePath = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
		}else{
			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
			}
		}
		String bakPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.BAKPATH))){
			bakPath = (String)pluginInput.get(Constants.Plugin.BAKPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "备份路径为空");
			return execResultDto;
		}
		int timeOut=60;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.TIMEOUT_KEY))){
			timeOut = Integer.valueOf(pluginInput.get(Constants.Plugin.TIMEOUT_KEY).toString());
		}
		Boolean upGrade=true;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.UPGRADE))){
			upGrade = Boolean.valueOf(pluginInput.get(Constants.Plugin.UPGRADE).toString());
		}
		String appName = null;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPNAME))){
			appName = (String)pluginInput.get(Constants.Plugin.APPNAME);
		}
		String resourceVersionId="";
		if(!JudgeUtil.isEmpty(messageMap.get("resourceVersionId"))){
			resourceVersionId = messageMap.get("resourceVersionId").toString();
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String patchFileRootPath =new File(filePath).getParentFile().getAbsolutePath();
		File patchRootFile = new File(patchFileRootPath);
		if (patchRootFile.exists()) {
			File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory",appName));//多个应用
			//升级
			if(upGrade){
				for (int i = 0; i < appFiles.length; i++) {
					File[] dbXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.DB,Constants.FILE_SUFFIX_XML));
					if (dbXMLFiles.length != 0) {
						File[] patchParents = dbXMLFiles[0].getParentFile().listFiles(new FileFilterClass("isDirectory",Constants.DB));
						resultMap= new OracleUtil(oracleUrl, timeOut).parseXml(patchParents[0].getAbsolutePath(),dbXMLFiles[0].getAbsolutePath());
						execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
						execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
						return execResultDto;
					} else {
						resultMap.put("result", false);
						resultMap.put("message", "数据库升级规则文件不存在");
					}
				}
				resultMap.put("result", true);
				resultMap.put("message", "升级成功！");
			}else{
				File bakFile= new File(bakPath+File.separator+resourceVersionId+File.separator+appName);
				if(bakFile.exists()){
					File[] files = bakFile.listFiles();
					StringBuffer sb = new StringBuffer();
					if(files.length==0){
						execResultDto.setSuccess(false);
						execResultDto.setMsg("备份路径的目录["+bakFile.getAbsolutePath()+"]，不存在备份文件");
						return execResultDto;
					}else{
						for (int i = 0; i < files.length; i++) {
							resultMap = new OracleUtil(oracleUrl, timeOut).excuteRollBack(files[i].getAbsolutePath());
							sb.append("===============" + resultMap.get("message") + SystemUtil.getLineSeparator()
							+ "===============" + SystemUtil.getLineSeparator());
							if (!(Boolean) resultMap.get("result")) {
								resultMap.put("message", sb.toString());
								break;
							}
						}
					}
				}else{
					execResultDto.setSuccess(false);
					execResultDto.setMsg("备份路径的目录["+bakFile.getAbsolutePath()+"]不存在");
					return execResultDto;
				}
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录["+patchFileRootPath+"]不存在");
		}
		
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
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
