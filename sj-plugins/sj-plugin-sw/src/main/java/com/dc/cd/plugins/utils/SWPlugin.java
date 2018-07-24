package com.dc.cd.plugins.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;
import com.dc.sw.appengine.plugins.constants.Constants;
import com.dc.sw.appengine.plugins.jaxb.AppFile;
import com.dc.sw.appengine.plugins.jaxb.AppFiles;
import com.dc.sw.appengine.plugins.utils.JudgeUtil;
import com.dc.sw.appengine.plugins.utils.XMLParser;

public class SWPlugin extends BaseAgentPlugin {

	private static final Logger log = LoggerFactory.getLogger(SWPlugin.class);
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
			if (Constants.Plugin.TYPE_CODE_BACKUPAPP.equals(detailDTO.getTypeCode())) {
				return backUpApp(detailDTO, pluginInput);
			}else if (Constants.Plugin.TYPE_CODE_UPGRADEAPP.equals(detailDTO.getTypeCode())) {
				return upgradeApp(detailDTO, pluginInput);
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
	//===========================================bak app
	public JobExecResultDto backUpApp(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
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
//		String filePath = null;
//		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
//			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
//		}else{
//			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
//				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
//			}
//		}
		String appPatchRootPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPPATCHROOTPATH))){
			appPatchRootPath = (String)pluginInput.get(Constants.Plugin.APPPATCHROOTPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "应用补丁的根路径为空");
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
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String bakFiles=null;
		List<String> bakFileList = new ArrayList<String>();
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.BAKFILES))){
			bakFiles = (String)pluginInput.get(Constants.Plugin.BAKFILES);
			bakFileList = Arrays.asList(bakFiles.split("#"));
		}
		Map<String,File> records = new HashMap<String,File>();
		appRootPath = new File(appRootPath).getAbsolutePath();
		if(bakFileList.size()>0){
			for(String bakFile:bakFileList){
				String bakFileTmp =null;
				int index =bakFile.indexOf("/");
				bakFileTmp = bakFile.substring(index+1);
				if(!bakFileTmp.startsWith(File.separator)){
					bakFileTmp=File.separator+bakFileTmp;
				}
				File fileTmp = new File(appRootPath+bakFileTmp);
				if(fileTmp.exists()){
					if(fileTmp.isDirectory()){
						recordFiles(fileTmp, records,appRootPath,"app");
					}else{
						records.put(bakFileTmp,fileTmp);
					}
				}
			}
		}
		if(new File(appPatchRootPath).exists()){
			File rootFile = new File(appPatchRootPath);
			appPatchRootPath=rootFile.getAbsolutePath();
			recordFiles(rootFile, records,appPatchRootPath,"appPath");
		}
		if(!JudgeUtil.isEmpty(records)){
			String backUpPath = bakPath+File.separator+resourceVersionId+File.separator+"app";
			String ruleFilePath=backUpPath+File.separator+Constants.APP_RULE_XML;
			generateRuleFile(records,ruleFilePath,appRootPath);
			File rule =new File(ruleFilePath);
			if(rule.exists()){
				//使用规则文件，备份目标文件
				backUpFile(rule, backUpPath, appRootPath);
				resultMap.put("result", true);
				resultMap.put("message", "备份成功！");
			}else{
				resultMap.put("result", false);
				resultMap.put("message", "生成规则文件异常！");
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "获取补丁文件异常！");
		}
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}
	
	
	
	
	public  void recordFiles(File rootFile,Map<String,File> records,String fileRootPath,String type){
		File[] files = rootFile.listFiles();
		//递归遍历文件夹
		for(File file :files){
			if(file.isDirectory()){
				recordFiles(file, records,fileRootPath,type);
			}else{
				String abPath = file.getAbsolutePath();
				String path ="";
				if("app".equals(type)){
					path = abPath.replace(fileRootPath,"");
				}else{
					path = abPath.replace(fileRootPath,"");
					int index2 = path.indexOf(File.separator);
					path =  path.substring(index2+1);
				}
				if(!path.startsWith(File.separator)){
					path=File.separator+path;
				}
				records.put(path,file);
			}
		}
	}
	
	
	public static void generateRuleFile(Map<String, File> patchFilesMap,String filePath,String appRootPath){
		List<AppFile> List = new ArrayList<AppFile>();
		for(Map.Entry<String, File> patch:patchFilesMap.entrySet()){
			String patchPath = patch.getKey();
			File file =patch.getValue();
			AppFile appfile = new AppFile();
			appfile.setIsApendDeploy(false);
			appfile.setIsNew(true);
			File fileApp = new File(appRootPath+patchPath);
			if(file.getAbsolutePath().startsWith(appRootPath)){
				appfile.setIsPatch(false);
			}else{
				appfile.setIsPatch(true);
			}
			if(fileApp.exists()){
				appfile.setIsNew(false);
			}
			appfile.setPath(patchPath);
			List.add(appfile);
		}
		XMLParser xp = new XMLParser();
		AppFiles appFiles = new AppFiles();
		appFiles.setFiles(List);
		String fileContent = xp.marshall(appFiles, AppFiles.class);
		BufferedWriter writer=null;
		try {
			File ruleFile = new File(filePath);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer !=null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public static void backUpFile(File ruleFile,String backUpPath,String appRootPath) throws IOException{
		InputStream xml=null;
		try {
			xml = new FileInputStream(ruleFile);
			XMLParser xp = new XMLParser();
			AppFiles appFiles = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			List<AppFile> list =appFiles.getFiles();
			for(AppFile appFile:list){
				Boolean backUp=!appFile.getIsNew();
				String patchFilePath = appFile.getPath();
				if(backUp){
					String sourcePath = appRootPath+File.separator+patchFilePath;
					String destPath=backUpPath+File.separator+patchFilePath;
					FileUtils.copyFile(new File(sourcePath), new File(destPath));//备份
				}else{
					continue;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	//=========================================================upgrade app

	
	
	public JobExecResultDto upgradeApp(JobDetailDto detailDTO,Map<String,Object> pluginInput) throws Exception{
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
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
//		String filePath = null;
//		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.FILEPATH))){
//			filePath = pluginInput.get(Constants.Plugin.FILEPATH).toString();
//		}else{
//			if(!JudgeUtil.isEmpty(messageMap.get(Constants.Plugin.FILEPATH))){
//				filePath = (String)messageMap.get(Constants.Plugin.FILEPATH);
//			}
//		}
		String appPatchRootPath;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPPATCHROOTPATH))){
			appPatchRootPath = (String)pluginInput.get(Constants.Plugin.APPPATCHROOTPATH);
		}else{
			execResultDto.setSuccess(false);
			execResultDto.setMsg( "应用补丁的根路径为空");
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
		Boolean upGrade=true;
		if(!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.UPGRADE))){
			upGrade = Boolean.valueOf(pluginInput.get(Constants.Plugin.UPGRADE).toString());
		}

		JSONObject resultMap = new JSONObject();

		//升级
		String backUpPath = bakPath+File.separator+resourceVersionId+File.separator+"app";
		String ruleFilePath=backUpPath+File.separator+Constants.APP_RULE_XML;
		File ruleFile =new File(ruleFilePath);
		if(ruleFile.exists()){
			if(upGrade){
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
	
		execResultDto.setSuccess((Boolean)resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String)resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
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
			result.put("message", getStackTrace(e));
		}
		if(result.isEmpty()){
			result.put("result", true);
			result.put("message", upgrad?"升级成功！":"回滚成功！");
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
