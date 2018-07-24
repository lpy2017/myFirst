package com.dc.cd.plugins.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dtools.ini.BasicIniFile;
import org.dtools.ini.BasicIniSection;
import org.dtools.ini.IniFile;
import org.dtools.ini.IniItem;
import org.dtools.ini.IniSection;
import org.dtools.ini.IniValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;
import com.dc.cd.plugins.utils.command.CommandResult;
import com.dc.cd.plugins.utils.command.analyser.impl.EchoAnalyseer;
import com.dc.cd.plugins.utils.command.executor.CommandNoWaitExecutor;
import com.dc.cd.plugins.utils.constants.Constants;
import com.dc.cd.plugins.utils.ini.INIFile;
import com.dc.cd.plugins.utils.ini.IniFileReader;
import com.dc.cd.plugins.utils.ini.IniFileWriter;
import com.dc.cd.plugins.utils.ini.Item;
import com.dc.cd.plugins.utils.ini.Section;
import com.dc.cd.plugins.utils.transfer.impl.FtpTransferer;
import com.dc.cd.plugins.utils.utils.ExecLinuxCmd;
import com.dc.cd.plugins.utils.utils.FilePath;
import com.dc.cd.plugins.utils.utils.FileUtil;
import com.dc.cd.plugins.utils.utils.FtpInfo;
import com.dc.cd.plugins.utils.utils.JavaZipUtil;
import com.dc.cd.plugins.utils.utils.JudgeUtil;
import com.dc.cd.plugins.utils.utils.RecursionDownload;
import com.dc.sdk.SDKUtil;

public class UtilsPlugin extends BaseAgentPlugin {

	private static final Logger log = LoggerFactory.getLogger(UtilsPlugin.class);
	private static final String MESSAGE = "message";
	private static final String RESULT = "result";
	private static final String FILE_PATH = "filePath";
	private static final String PLUGIN_CODE = "CD_PLUGIN_UTILS";

	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	private Map<String, Object> detectUsedPort(String ip, String port, int timeOut) {
		Boolean success = true;
		;
		Boolean timeOutFlag = false;
		Boolean continueFlag = true;
		Map<String, Object> resultCmd = new HashMap<String, Object>();
		long startTime = System.currentTimeMillis();
		while (continueFlag) {
			long diff = System.currentTimeMillis() - startTime;
			if (diff > timeOut * 1000) {
				timeOutFlag = true;
				continueFlag = false;
				break;
			}
			Socket socket = new Socket();
			try {
				socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)));
				success = true;
			} catch (IOException e) {
				success = false;
				try {
					Thread.sleep(7000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!timeOutFlag && !success) {
				continue;
			} else {
				break;
			}
		}
		resultCmd.put(RESULT, success);
		if (!timeOutFlag) {
			resultCmd.put(MESSAGE, "探测 ip【" + ip + "】 端口【" + port + "】成功！");
		} else {
			resultCmd.put(MESSAGE, "探测 ip【" + ip + "】 端口【" + port + "】失败，探测超时！");
		}
		return resultCmd;
	}

	private Map<String, Object> detectUnUsedPort(String ip, String[] ports) {
		Boolean result = true;
		Map<String, Object> resultCmd = new HashMap<String, Object>();
		List<String> usedPorts = new ArrayList<String>();
		for (int i = 0; i < ports.length; i++) {
			Socket socket = new Socket();
			int detectPort = Integer.valueOf(ports[i]);
			try {
				socket.connect(new InetSocketAddress(ip, detectPort));
				usedPorts.add(ports[i]);
			} catch (IOException e) {
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (usedPorts.size() > 0) {
			result = false;
			resultCmd.put(MESSAGE, "端口 " + JSON.toJSONString(usedPorts) + " 已被占用");
		} else {
			result = true;
			resultCmd.put(MESSAGE, "端口" + JSON.toJSONString(ports) + " 未被使用");
		}
		resultCmd.put(RESULT, result);
		return resultCmd;
	}

	private JobExecResultDto detectPort(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		String ip = pluginInput.get("ip").toString();
		String port = pluginInput.get("port").toString();
		int timeOut = Integer.valueOf(pluginInput.get("timeOut").toString());
		boolean unUsed = Boolean.valueOf(pluginInput.get("unUsed").toString());
		Boolean success = true;
		StringBuffer sb = new StringBuffer();
		if (ip == null || "".equals(ip)) {
			success = false;
			sb.append("探测 ip 为空!");
		}
		if (port == null || "".equals(port)) {
			success = false;
			sb.append("探测 port 为空");
		}
		Pattern p = Pattern.compile("^[1-9]([0-9]*)(#[1-9]([0-9]*))*$");// 端口的格式8080#8081
		Matcher match = p.matcher(port);
		String[] ports = {};
		if (match.find()) {
			ports = port.split("#");
		} else {
			success = false;
			sb.append("端口 " + port + " 格式有问题");
		}
		if (!success) {
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(success);
			execResultDto.setMsg(sb.toString());
			return execResultDto;
		} else {
			Map<String, Object> resultCmd;
			if (!unUsed) {
				resultCmd = detectUsedPort(ip, port, timeOut);
			} else {
				resultCmd = detectUnUsedPort(ip, ports);
			}
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(Boolean.valueOf(resultCmd.get(RESULT).toString()));
			execResultDto.setMsg(resultCmd.get(MESSAGE).toString());
			return execResultDto;
		}
	}

	private JobExecResultDto download(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		String url = pluginInput.get(Constants.Plugin.URL).toString();

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (url == null) {
			log.error("resourceUrl is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("resourceUrl is null");
			return execResultDto;
		}
		String localPath = (String) pluginInput.get("deployPath");
		if (localPath == null) {
			log.error("deployPath is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("deployPath is null");
			return execResultDto;
		}

		localPath = FilePath.getFilePath(localPath);
		if (!localPath.endsWith("/")) {
			localPath += "/";
		}

		File file = null;
		FtpTransferer ft = null;
		Boolean openResult = false;
		try {
			FtpInfo ftpInfo = new FtpInfo(url);
			// ft = new FtpTransferer(ftpInfo, localPath);
			ft = new FtpTransferer(ftpInfo, localPath, pluginInput.get("timeOut").toString(), "/");
			openResult = ft.open();
			String remotePath = ftpInfo.getFile();
			if (openResult) {
				file = ft.download(remotePath, new File(remotePath).getName());
			} else {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		} finally {
			if (ft != null && openResult)
				ft.close();
		}
		log.debug("download finished result is " + (file != null ? file.getAbsolutePath() : false));

		if (file != null) {
			resultMap.put(Constants.Plugin.RESULT, true);
			 resultMap.put(FILE_PATH, file.getAbsolutePath());
			resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath =" + file.getAbsolutePath());
		} else {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "下载失败！");
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		Map<String, Object> filePath = new HashMap<String, Object>();
		filePath.put(FILE_PATH, resultMap.get(FILE_PATH));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pluginCode", PLUGIN_CODE);
		data.put("typeCode", "download");
		data.put("data", filePath);
		execResultDto.setData(JSON.toJSONString(data));
		try {
			pluginServerService.uploadResultData(execResultDto);
		} catch (Exception e) {
			log.error("", e);
		}
		return execResultDto;
	}

	private JobExecResultDto downloadArtifact(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		String url = pluginInput.get("artifactURL").toString();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("artifactURL is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("artifactURL is null");
			return execResultDto;
		}
		String localPath = pluginInput.get("deployPath").toString();
		if (JudgeUtil.isEmpty(localPath)) {
			log.error("deployPath is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("deployPath is null");
			return execResultDto;
		}

		localPath = FilePath.getFilePath(localPath);
		if (!localPath.endsWith("/")) {
			localPath += "/";
		}
		File file = null;
		FtpTransferer ft = null;
		Boolean openResult = false;
		FtpInfo ftpInfo = null;
		try {
			ftpInfo = new FtpInfo(url);
			// ft = new FtpTransferer(ftpInfo, localPath);
			ft = new FtpTransferer(ftpInfo, localPath, pluginInput.get("timeOut").toString(), "/");
			openResult = ft.open();
			String remotePath = ftpInfo.getFile();
			if (openResult) {
				Map<String, Object> checkResult = ft.checkFileExistence(url);
				Object result = checkResult.get("result");
				if (!(Boolean) result) {
					resultMap = checkResult;
				} else {
					file = ft.download(remotePath, new File(remotePath).getName());
					log.debug("download finished result is " + (file != null ? file.getAbsolutePath() : false));
					if (file != null) {
						resultMap.put(Constants.Plugin.RESULT, true);
						 resultMap.put(FILE_PATH, file.getAbsolutePath());
						resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath =" + file.getAbsolutePath());
					} else {
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "下载失败！");
					}
				}
			} else {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, getStackTrace(e));
		} finally {
			if (ftpInfo == null) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp url异常！");
			}
			if (ft != null && openResult) {
				ft.close();
			}
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		Map<String, Object> filePath = new HashMap<String, Object>();
		filePath.put(FILE_PATH, resultMap.get(FILE_PATH));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pluginCode", PLUGIN_CODE);
		data.put("typeCode", "DownloadArtifact");
		data.put("data", filePath);
		execResultDto.setData(JSON.toJSONString(data));
		try {
			pluginServerService.uploadResultData(execResultDto);
		} catch (Exception e) {
			log.error("", e);
		}
		return execResultDto;
	}

	private JobExecResultDto downloadComp(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		String url = pluginInput.get("compURL").toString();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("compURL is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("compURL is null");
			return execResultDto;
		}

		String localPath = pluginInput.get("deployPath").toString();
		if (JudgeUtil.isEmpty(localPath)) {
			log.error("deployPath is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("deployPath is null");
			return execResultDto;
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
		if (resultMap.get("result") == null) {
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "下载成功");
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		return execResultDto;
	}

	private JobExecResultDto downloadSaltArtifact(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String url = messageMap.get("artifactURL").toString();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("artifactURL is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("artifactURL is null");
			return execResultDto;
		}
		String localPath = messageMap.get("deployPath").toString();
		if (JudgeUtil.isEmpty(localPath)) {
			log.error("deployPath is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("deployPath is null");
			return execResultDto;
		}

		localPath = FilePath.getFilePath(localPath);
		if (!localPath.endsWith("/")) {
			localPath += "/";
		}
		File file = null;
		FtpTransferer ft = null;
		Boolean openResult = false;
		FtpInfo ftpInfo = null;
		try {
			ftpInfo = new FtpInfo(url);
			// ft = new FtpTransferer(ftpInfo, localPath);
			ft = new FtpTransferer(ftpInfo, localPath, pluginInput.get("timeOut").toString(), "/");
			openResult = ft.open();
			String remotePath = ftpInfo.getFile();
			if (openResult) {
				Map<String, Object> checkResult = ft.checkFileExistence(url);
				Object result = checkResult.get("result");
				if (!(Boolean) result) {
					resultMap = checkResult;
				} else {
					file = ft.download(remotePath, new File(remotePath).getName());
					if (file != null) {
						resultMap.put(Constants.Plugin.RESULT, true);
						 resultMap.put(FILE_PATH, file.getAbsolutePath());
						resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath =" + file.getAbsolutePath());

						// 解压工件包 删除工件包 unzip deleteFile
						Map<String, String> subFilesMap = new HashMap<String, String>();
						if (subFilesMap.isEmpty()) {
							subFilesMap.put(file.getName(), "/");
						}
						Map<String, Object> unzipResult = JavaZipUtil.unzip(file.getAbsolutePath(), true, subFilesMap);
						log.debug("unzip finished result is " + JSON.toJSONString(resultMap));
						// 生成文件对应关系 traverseFolder
						List<String> fileList = new ArrayList<String>();
						fileList = FileUtil.traverseFolder(localPath, fileList);
						Map<String, String> fileMap = new HashMap<String, String>();
						for (String string : fileList) {
							// string = string.replace("\\", "/");
							fileMap.put(string, string);

						}
						// //获取组件input和output 以及CreateComponentFile commonMap
						// String instanceId = (String)
						// messageMap.get(Constants.Plugin.INSTANCEID);
						String instanceId = messageMap.get("instanceId").toString();
						// String logicalVersion =(String)
						// messageMap.get(Constants.Plugin.LOGICALVERSION);
						String logicalVersion = messageMap.get("logicalVersion").toString();
						// Map<String,Object> configMap =
						// CreateComponentFile.commonMap(instanceId+file.separator+logicalVersion);
						// Map<String,String> extreMap = new
						// HashMap<String,String>();
						// Map<String, String> config = new
						// HashMap<String,String>();
						// config.putAll((Map<String,String>)
						// messageMap.get(Constants.Plugin.COMPONENTINPUT));
						// config.putAll((Map<String,String>)
						// messageMap.get(Constants.Plugin.COMPONENTOUTPUT));
						//
						// for(Map.Entry<String,String>
						// entry:config.entrySet()){
						// String key = entry.getKey();
						// String freeVlaue =
						// "${".concat(key).concat("?if_exists}");
						// extreMap.put(key, freeVlaue);
						// }
						// configMap.putAll(extreMap);
						// SDKUtil sdkUtil = new SDKUtil();
						// //使用freemark 替换自身生成工件文件夹
						// Map<String,Object> freeMarkresult =
						// sdkUtil.configSdk(configMap, fileMap,encoding);

						String replaceInstanceId = instanceId + file.separator + logicalVersion;
						for (Map.Entry<String, String> template : fileMap.entrySet()) {
							String filePath = template.getValue();
							File filea = new File(filePath);
							FileInputStream fi = null;
							FileOutputStream fo = null;
							try {
								fi = new FileInputStream(filea);
								String exStr = IOUtils.toString(fi, pluginInput.get("encoding").toString());
								String afStr = exStr.replace("${instanceId?if_exists}", replaceInstanceId);
								fo = new FileOutputStream(filea);
								IOUtils.write(afStr, fo, pluginInput.get("encoding").toString());
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}

							String path = "";
							String[] paths = localPath.split("/");
							for (int i = 0; i < paths.length - 1; i++) {
								path += paths[i];
								path += "/";
							}

							String chmodcmd = "sudo chmod -R 755 " + path;
							pluginInput.put("CMD", chmodcmd);
							String cmdResult = ExecLinuxCmd.execCmd(pluginInput, null);

							log.info(cmdResult);
							System.out.println(cmdResult);
						}

						// if((boolean) freeMarkresult.get("result")){
						// log.info((String)freeMarkresult.get("message"));
						// }

					} else {
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "下载失败！");
					}
				}
			} else {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, getStackTrace(e));
		} finally {
			if (ftpInfo == null) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp url异常！");
			}
			if (ft != null && openResult) {
				ft.close();
			}
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		Map<String, Object> filePath = new HashMap<String, Object>();
		filePath.put(FILE_PATH, resultMap.get(FILE_PATH));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pluginCode", PLUGIN_CODE);
		data.put("typeCode", "DownloadSaltArtifact");
		data.put("data", filePath);
		execResultDto.setData(JSON.toJSONString(data));
		try {
			pluginServerService.uploadResultData(execResultDto);
		} catch (Exception e) {
			log.error("", e);
		}
		return execResultDto;
	}

	private JobExecResultDto downloadSaltComp(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String url = messageMap.get("compURL").toString();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(url)) {
			log.error("compURL is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("compURL is null");
			return execResultDto;
		}
		String localPath = messageMap.get("deployPath").toString();
		if (JudgeUtil.isEmpty(localPath)) {
			log.error("deployPath is null");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("deployPath is null");
			return execResultDto;
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
		if (resultMap.get("result") == null) {
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "下载成功");
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		return execResultDto;
	}

	private JobExecResultDto config(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 替换配置 map key:模板路径 value:替换路径
		SDKUtil sdkUtil = new SDKUtil();
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		Map<String, String> templateMap = (Map<String, String>) messageMap.get(Constants.Plugin.CONFIGTEMPLATE);
//		Map<String, String> Map = (Map<String, String>) messageMap.get(Constants.Plugin.COMPONENTINPUT);
		Map<String, String> Map = JSON.parseObject(messageMap.get(Constants.Plugin.COMPONENTINPUT).toString(), new TypeReference<Map<String, String>>(){});

		Map<String, Object> actualMap = new HashMap<String, Object>();

		for (Entry<String, String> entry : Map.entrySet()) {
			if (entry.getKey().contains(".")) {
				String key = entry.getKey().replace(".", "_");
				actualMap.put(key, entry.getValue());
			} else {
				actualMap.put(entry.getKey(), entry.getValue());
			}
		}

		Map<String, String> actualTemplateMap = new HashMap<String, String>();
		for (Entry<String, String> entry : templateMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			key = key.substring(1, key.length());
			if (value.startsWith(".")) {
				value = value.substring(1, value.length());
				actualTemplateMap.put(deployPath + key, deployPath + value);
			} else {
				actualTemplateMap.put(deployPath + key, value);
			}
		}
		String encoding = pluginInput.get("encoding").toString();
		result = sdkUtil.configSdk(actualMap, actualTemplateMap, encoding);

		for (Map.Entry<String, String> template : actualTemplateMap.entrySet()) {
			String filePath = template.getValue();
			File file = new File(filePath);
			FileInputStream fi = null;
			FileOutputStream fo = null;
			try {
				fi = new FileInputStream(file);
				String exStr = IOUtils.toString(fi, encoding);
				String afStr = exStr.replaceAll("WANGYB", "\\$");
				fo = new FileOutputStream(file);
				IOUtils.write(afStr, fo, encoding);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fo != null) {
					try {
						fo.flush();
						fo.close();
					} catch (IOException e) {
						log.error("", e);
					}
				}
				if (fi != null) {
					try {
						fi.close();
					} catch (IOException e) {
						log.error("", e);
					}
				}
			}
		}

		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(result.get(Constants.Plugin.MESSAGE).toString());
		return execResultDto;
	}

	private JobExecResultDto saltConfig(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		// 配置的key中若有 ".",替换成"_" 模版文件中key值中也要对应修改为_
		String deployPath = messageMap.get(Constants.Plugin.DEPLOYPATH).toString();
		Map<String, String> templateMap = (Map<String, String>) messageMap.get(Constants.Plugin.CONFIGTEMPLATE);
		Map<String, String> actualTemplateMap = new HashMap<String, String>();
		for (Entry<String, String> entry : templateMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			key = key.substring(1, key.length());
			if (value.startsWith(".")) {
				value = value.substring(1, value.length());
				actualTemplateMap.put(deployPath + key, deployPath + value);
			} else {
				actualTemplateMap.put(deployPath + key, value);
			}
		}

//		Map<String, String> Map = (Map<String, String>) messageMap.get(Constants.Plugin.COMPONENTINPUT);
		Map<String, String> Map = JSON.parseObject(messageMap.get(Constants.Plugin.COMPONENTINPUT).toString(), new TypeReference<Map<String, String>>(){});

		Map<String, Object> actualMap = new HashMap<String, Object>();

		for (Entry<String, String> entry : Map.entrySet()) {
			if (entry.getKey().contains(".")) {
				String key = entry.getKey().replace(".", "_");
				actualMap.put(key, entry.getValue());
			} else {
				actualMap.put(entry.getKey(), entry.getValue());
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		// 替换配置 map key:模板路径 value:替换路径
		SDKUtil sdkUtil = new SDKUtil();
		String encoding = pluginInput.get("encoding").toString();
		result = sdkUtil.configSdk(actualMap, actualTemplateMap, encoding);
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(result.get(Constants.Plugin.MESSAGE).toString());
		return execResultDto;
	}
	
	private JobExecResultDto unzip(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String,String> subFilesMap=new HashMap<String, String>();
		String fileParams = null;//path1#targetPath1,path2#targetPath2
		if (pluginInput.get("fileParams") != null) {
			fileParams = pluginInput.get("fileParams").toString();
		}
		if(fileParams !=null){
			String [] params = fileParams.split(",");
			for(String one : params){
				String [] subFileParam= one.split("#");
				if(subFileParam.length == 1){
					subFilesMap.put(subFileParam[0], subFileParam[1]);
				}
			}
		}
		String packagePath = pluginInput.get("packagePath").toString();
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
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(resultMap.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(resultMap.get(Constants.Plugin.MESSAGE).toString());
		return execResultDto;
	}
	
	private JobExecResultDto getCompCurrentVersion(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(true);
		execResultDto.setMsg("获取当前版本配置成功");
		return execResultDto;
	}
	
	private JobExecResultDto getCompTargetVersion(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(true);
		execResultDto.setMsg("获取目标版本配置成功");
		return execResultDto;
	}
	
	private JobExecResultDto judgeEmpty(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		Map<String, Object> componentInput = (Map<String, Object>) messageMap.get("componentInput");
		String judgeKey = pluginInput.get("JudgeKey").toString();
		boolean empty = true;
		if (componentInput.containsKey(judgeKey)) {
			empty = false;
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(!empty);
		execResultDto.setMsg("成功读取组件的input配置");
		return execResultDto;
	}
	
	private JobExecResultDto loopCount(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> paramMap = JSON.parseObject(pluginInput.get("frame_param").toString());
		JSONObject insvarMap = (JSONObject) paramMap.get(Constants.Plugin.INSVARMAP);
		String counterOperation = pluginInput.get("counter_operation").toString();
		String loopCounter = pluginInput.get("counter_variable").toString();
		String Operation_Regex = "^[\\+,\\-]{1}[1-9]+$";
		String Operation_Regex_Plus = "^\\+[1-9]+$";
		String Operation_Regex_Minus = "^\\-[1-9]+$";
//		String Default_Operation_Plus = "+1";
//		String Default_Operation_Minus = "-1";
		boolean isOp = Pattern.matches(Operation_Regex, counterOperation);
		if (!isOp) {
			log.error("计数器自增/减表达式：" + counterOperation + "格式错误，请写成+1或-1！");
			JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
			execResultDto.setCode("100");
			execResultDto.setUuid(detailDTO.getUuid());
			execResultDto.setSuccess(false);
			execResultDto.setMsg("计数器自增/减表达式：" + counterOperation + "格式错误，请写成+1或-1！");
			return execResultDto;
		}
		if (!JudgeUtil.isEmpty(loopCounter)) {
			String counterNum = "" + insvarMap.get(loopCounter);
			try {
				int num = Integer.parseInt(counterNum);
				if (Pattern.matches(Operation_Regex_Plus, counterOperation)) {
					num = num + Integer.parseInt(counterOperation.substring(1));
				} else if (Pattern.matches(Operation_Regex_Minus, counterOperation)) {
					num = num - Integer.parseInt(counterOperation.substring(1));
				} else {
				}
				result.put(loopCounter, num);
				result.put(Constants.Plugin.RESULT, true);
				result.put(Constants.Plugin.MESSAGE, "自增/减成功！");
			} catch (Exception e) {
				log.error("计数器的值:" + counterNum + "非整型，无法自增/减！" + e.getMessage(), e);
				JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
				execResultDto.setCode("100");
				execResultDto.setUuid(detailDTO.getUuid());
				execResultDto.setSuccess(false);
				execResultDto.setMsg("计数器的值:" + counterNum + "非整型，无法自增/减！");
				return execResultDto;
			}
		} else {
			result.put(Constants.Plugin.RESULT, true);
			result.put(Constants.Plugin.MESSAGE, "未配置计数器变量，无自增/减操作！");
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(result.get(Constants.Plugin.MESSAGE).toString());
		if (result.containsKey(loopCounter)) {
			Map<String, Object> counter = new HashMap<String, Object>();
			counter.put(loopCounter, result.get(loopCounter));
			counter.put("counterKey", loopCounter);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("pluginCode", "CD_PLUGIN_UTILS");
			data.put("typeCode", "LoopCount");
			data.put("data", counter);
			execResultDto.setData(JSON.toJSONString(data));
		}
		try {
			pluginServerService.uploadResultData(execResultDto);
		} catch (Exception e) {
			log.error("", e);
		}
		return execResultDto;
	}
	
	private JobExecResultDto snapshot(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String masterRest = pluginInput.get("masterRest").toString();
		String ss_variable = pluginInput.get("ss_variable").toString();
		// http client实现
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost post = new HttpPost(masterRest + "/ws/deployedApp/saveSnapshotOfInstance");
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		form.add(new BasicNameValuePair(Constants.Plugin.INSTANCEID, "" + messageMap.get(Constants.Plugin.INSTANCEID)));
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(form, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("", e);
		}
		post.setEntity(entity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(post);
		} catch (ClientProtocolException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		HttpEntity responseEntity = response.getEntity();
		String result = null;
		if (responseEntity != null) {
			InputStream instream = null;
			try {
				instream = responseEntity.getContent();
			} catch (IllegalStateException e) {
				log.error("", e);
			} catch (IOException e) {
				log.error("", e);
			}
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
				StringBuilder sb = new StringBuilder();
				String str = null;
				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}
				result = sb.toString();
			} catch (IOException ex) {
				log.error("", ex);
			} catch (RuntimeException ex) {
				log.error("", ex);
			} finally {
				// Closing the input stream will trigger connection release
				if (instream != null) {
					try {
						instream.close();
					} catch (IOException e) {
						log.error("", e);
					}
				}
			}
		}
		// 调用master生成快照接口
//		Form form = new Form();
//		form.param(Constants.Plugin.INSTANCEID, "" + messageMap.get(Constants.Plugin.INSTANCEID));
//		Client client = ClientBuilder.newClient();
//		WebTarget target = client
//				.target(masterRest + "/ws/deployedApp/saveSnapshotOfInstance");
//		String result = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		JSONObject json = JSON.parseObject(result);
		String snapshotId = null;
		Map<String, Object> pluginResult = new HashMap<String, Object>();
		String SNAPSHOTID_KEY  = "snapshotId";
		if (!json.getBooleanValue(Constants.Plugin.RESULT)) {
			// 生成快照失败
			snapshotId = "";
			pluginResult.put(SNAPSHOTID_KEY, snapshotId);
			pluginResult.put(Constants.Plugin.RESULT, false);
			pluginResult.put(Constants.Plugin.MESSAGE, "生成快照失败");
		} else {
			snapshotId = json.getString("snapId");
			pluginResult.put(SNAPSHOTID_KEY, snapshotId);
			pluginResult.put(Constants.Plugin.RESULT, true);
			pluginResult.put(Constants.Plugin.MESSAGE, "success");
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(pluginResult.get(Constants.Plugin.RESULT).toString()));
		execResultDto.setMsg(pluginResult.get(Constants.Plugin.MESSAGE).toString());
		Map<String, Object> snapshotInfo = new HashMap<String, Object>();
		snapshotInfo.put(ss_variable, pluginResult.get(SNAPSHOTID_KEY));
		snapshotInfo.put("ss_variable", ss_variable);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("pluginCode", "CD_PLUGIN_UTILS");
		data.put("typeCode", "Snapshot");
		data.put("data", snapshotInfo);
		execResultDto.setData(JSON.toJSONString(data));
		return execResultDto;
	}
	
	private Map<String, Object> updateIni(String filePath,
			List<String> updateList,
			List<String> removeList,
			List<String> removeSectionList,
			String encoding,
			String itemNameRegEx,
			boolean isCaseSensitive,
			List<String> sectionPreComment,
			List<String> sectionPostComment,
			List<String> sectionEndlineComment,
			List<String> itemPreComment,
			List<String> itemPostComment,
			List<String> itemEndlineComment) {
		IniFile iniFile = new BasicIniFile(isCaseSensitive);
		IniValidator validator = iniFile.getValidator();
		validator.setItemNameRegEx(Pattern.compile(itemNameRegEx));
		validator.setSectionNameRegEx(Pattern.compile(itemNameRegEx));
		File file = new File(filePath);
		IniFileReader reader = new IniFileReader(iniFile, file);
		IniFileWriter writer = new IniFileWriter(iniFile, file);
		try {
			// 读取item
			reader.read(encoding);
			for (String removeSection : removeSectionList) {
				if (iniFile.hasSection(removeSection)) {
					iniFile.removeSection(removeSection);
				}
			}
			for (String remove : removeList) {
				int i = remove.indexOf("\\");
				String section = remove.substring(0, i);
				String key = remove.substring(i + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					if (iniSection.hasItem(key)) {
						iniSection.removeItem(key);
					}
				}
			}
			for (String update : updateList) {
				int i = update.indexOf("=");
				String sectionAndKey = update.substring(0, i);
				String value = update.substring(i + 1);
				int ii = sectionAndKey.indexOf("\\");
				String section = sectionAndKey.substring(0, ii);
				String key = sectionAndKey.substring(ii + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					if (iniSection.hasItem(key)) {
						IniItem item = iniSection.getItem(key);
						item.setValue(value);
					} else {
						IniItem item = new IniItem(key, validator, isCaseSensitive);
						item.setValue(value);
//						iniSection.addItem(item);
						if (!iniSection.addItem(item)) {
							throw new Exception("section add item fail");
						}
					}
				} else {
					IniSection iniSection = new BasicIniSection(section, validator, isCaseSensitive);
					IniItem item = new IniItem(key, validator, isCaseSensitive);
					item.setValue(value);
//					iniSection.addItem(item);
					if (!iniSection.addItem(item)) {
						throw new Exception("section add item fail");
					}
//					iniFile.addSection(iniSection);
					if (!iniFile.addSection(iniSection)) {
						throw new Exception("file add section fail");
					}
				}
			}
			for (String pre : sectionPreComment) {
				int i = pre.indexOf("=");
				String section = pre.substring(0, i);
				String comment = pre.substring(i + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					iniSection.setPreComment(comment);
				}
			}
			for (String post : sectionPostComment) {
				int i = post.indexOf("=");
				String section = post.substring(0, i);
				String comment = post.substring(i + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					iniSection.setPostComment(comment);
				}
			}
			for (String endline : sectionEndlineComment) {
				int i = endline.indexOf("=");
				String section = endline.substring(0, i);
				String comment = endline.substring(i + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					iniSection.setEndLineComment(comment);
				}
			}
			for (String pre : itemPreComment) {
				int i = pre.indexOf("=");
				String sectionAndItem = pre.substring(0, i);
				String comment = pre.substring(i + 1);
				int ii = sectionAndItem.indexOf("\\");
				String section = sectionAndItem.substring(0, ii);
				String item = sectionAndItem.substring(ii + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					if (iniSection.hasItem(item)) {
						IniItem iniItem = iniSection.getItem(item);
						iniItem.setPreComment(comment);
					}
				}
			}
			for (String post : itemPostComment) {
				int i = post.indexOf("=");
				String sectionAndItem = post.substring(0, i);
				String comment = post.substring(i + 1);
				int ii = sectionAndItem.indexOf("\\");
				String section = sectionAndItem.substring(0, ii);
				String item = sectionAndItem.substring(ii + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					if (iniSection.hasItem(item)) {
						IniItem iniItem = iniSection.getItem(item);
						iniItem.setPostComment(comment);
					}
				}
			}
			for (String endline : itemEndlineComment) {
				int i = endline.indexOf("=");
				String sectionAndItem = endline.substring(0, i);
				String comment = endline.substring(i + 1);
				int ii = sectionAndItem.indexOf("\\");
				String section = sectionAndItem.substring(0, ii);
				String item = sectionAndItem.substring(ii + 1);
				if (iniFile.hasSection(section)) {
					IniSection iniSection = iniFile.getSection(section);
					if (iniSection.hasItem(item)) {
						IniItem iniItem = iniSection.getItem(item);
						iniItem.setEndLineComment(comment);
					}
				}
			}
			writer.write(encoding);
		} catch (Exception e) {
			log.error("", e);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(RESULT, false);
			map.put(MESSAGE, e.getMessage());
			return map;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT, true);
		map.put(MESSAGE, "成功更新ini文件");
		return map;
	}
	
	public void testUpdateIni() {
		String filePath = "C:/Users/guojwe/Desktop/utf-8.ini";
		String encoding = "utf-8";
		String itemNameRegEx = ".*";
		List<String> updateList = Arrays.asList("guojwe\\key=value1", "InterfaceParam\\XINAN_CheckClient_Version=2");
		List<String> removeList = Arrays.asList("InterfaceParam\\XINAN_CheckClient_WarnInfo");
		List<String> removeSectionList = Arrays.asList("client_risk_level");
		List<String> emptyList = Collections.emptyList();
		System.out.println(updateIni(filePath, updateList, removeList, removeSectionList, encoding, itemNameRegEx, true, emptyList, emptyList, emptyList, emptyList, emptyList, emptyList));
	}
	
	private JobExecResultDto updateIni(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
//		String encoding = pluginInput.get("encoding").toString();
//		String itemNameRegEx = pluginInput.get("itemNameRegEx").toString();
		List<Map<String, Object>> files = JSON.parseObject(pluginInput.get("rule").toString(), new TypeReference<List<Map<String,Object>>>(){});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT, true);
		Map<String, Object> message = new HashMap<String, Object>();
		result.put(MESSAGE, message);
		for (Map<String, Object> file : files) {
			String filePath = file.get("file").toString();
			boolean isCaseSensitive = Boolean.valueOf(file.get("caseSensitive").toString());
			List<String> emptyList = Collections.emptyList();
			List<String> update = file.get("update") == null || "".equals(file.get("update")) ? emptyList : (List<String>) file.get("update");
			List<String> remove = file.get("remove") == null || "".equals(file.get("remove")) ? emptyList : (List<String>) file.get("remove");
			List<String> removeSection = file.get("removeSection") == null || "".equals(file.get("removeSection")) ? emptyList : (List<String>) file.get("removeSection");
			List<String> sectionPreComment = file.get("sectionPreComment") == null || "".equals(file.get("sectionPreComment")) ? emptyList : (List<String>) file.get("sectionPreComment");
			List<String> sectionPostComment = file.get("sectionPostComment") == null || "".equals(file.get("sectionPostComment")) ? emptyList : (List<String>) file.get("sectionPostComment");
			List<String> sectionEndlineComment = file.get("sectionEndlineComment") == null || "".equals(file.get("sectionEndlineComment")) ? emptyList : (List<String>) file.get("sectionEndlineComment");
			List<String> itemPreComment = file.get("itemPreComment") == null || "".equals(file.get("itemPreComment")) ? emptyList : (List<String>) file.get("itemPreComment");
			List<String> itemPostComment = file.get("itemPostComment") == null || "".equals(file.get("itemPostComment")) ? emptyList : (List<String>) file.get("itemPostComment");
			List<String> itemEndlineComment = file.get("itemEndlineComment") == null || "".equals(file.get("itemEndlineComment")) ? emptyList : (List<String>) file.get("itemEndlineComment");
			String encoding = file.get("encoding").toString();
			String itemNameRegEx = file.get("itemNameRegEx").toString();
			Map<String, Object> fileResult = updateIni(filePath, update, remove, removeSection, encoding, itemNameRegEx, isCaseSensitive, sectionPreComment, sectionPostComment, sectionEndlineComment, itemPreComment, itemPostComment, itemEndlineComment);
			message.put(filePath, fileResult);
			if (!Boolean.valueOf(fileResult.get(RESULT).toString())) {
				result.put(RESULT, false);
				break;
			}
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(RESULT).toString()));
		execResultDto.setMsg(JSON.toJSONString(result.get(MESSAGE)));
		return execResultDto;
	}
	
	private Map<String, Object> setMessage(boolean result, String message) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RESULT, result);
		map.put(MESSAGE, message);
		return map;
	}
	
	private Map<String, Object> updateXML(String filePath,
			String encoding,
			boolean checkDTD,
			List<String> updateText,
			List<String> updateAttribute,
			List<String> insertElement,
			List<String> removeText,
			List<String> removeAttribute,
			List<String> removeElement) {
		try {
			SAXReader reader = new SAXReader();
			reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", checkDTD);
			Document document = reader.read(new File(filePath).toURI().toURL());
			for (String text : removeText) {
				List<Node> nodes = document.selectNodes(text);
				for (Node node : nodes) {
					node.detach();
				}
			}
			for (String attribute : removeAttribute) {
				List<Node> nodes = document.selectNodes(attribute);
				for (Node node : nodes) {
					node.detach();
				}
			}
			for (String element : removeElement) {
				List<Node> nodes = document.selectNodes(element);
				for (Node node : nodes) {
					node.detach();
				}
			}
			for (String text : updateText) {
				int i = text.indexOf("->");
				String xpath = text.substring(0, i);
				String textContent = text.substring(i + 2);
				List<Node> nodes = document.selectNodes(xpath);
				if (nodes.isEmpty()) {
					return setMessage(false, "未找到需要设定text的xpath");
				}
				for (Node node : nodes) {
					node.setText(textContent);
				}
			}
			for (String attribute : updateAttribute) {
				int i = attribute.indexOf("->");
				String xpath = attribute.substring(0, i);
				String value = attribute.substring(i + 2);
				int ii = xpath.indexOf("@");
				String element = xpath.substring(0, ii - 1);
				String key = xpath.substring(ii + 1);
				List<Node> nodes = document.selectNodes(element);
				if (nodes.isEmpty()) {
					return setMessage(false, "未找到需要设定attribute的xpath");
				}
				for (Node node : nodes) {
					Element e = (Element) node;
					e.addAttribute(key, value);
				}
			}
			for (String element : insertElement) {
				int i = element.indexOf("->");
				String xpath = element.substring(0, i);
				String content = element.substring(i + 2);
				Document dom = DocumentHelper.parseText(content);
				List<Node> nodes = document.selectNodes(xpath);
				if (nodes.isEmpty()) {
					return setMessage(false, "未找到需要插入元素的xpath");
				}
				for (Node node : nodes) {
					Element e = (Element) node;
					e.add(dom.getRootElement());
				}
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			OutputStream os = new FileOutputStream(filePath);
			XMLWriter writer = new XMLWriter(os, format);
			writer.write(document);
		} catch (Exception e) {
			log.error("", e);
			return setMessage(false, e.getMessage());
		}
		return setMessage(true, "更新xml成功");
	}
	
	public void testUpdateXML() {
		String filePath = "C:/Users/guojwe/Desktop/logback.xml";
		String encoding = "utf-8";
		List<String> updateText = Arrays.asList("/configuration/timestamp->aaaaa", "/configuration/appender/File->bbbbbb");
		List<String> updateAttribute = Arrays.asList("/configuration/appender[1]/@name->ccccccc");
		List<String> insertElement = Arrays.asList("/configuration/root-><ddddd/>", "/configuration/appender[@name=\"FILE\"]/triggeringPolicy-><eeeee/>");
		List<String> removeText = Arrays.asList("/configuration/appender/encoder/Pattern/text()");
		List<String> removeAttribute = Arrays.asList("/configuration/root/level/@value");
		List<String> removeElement = Arrays.asList("/configuration/root/appender-ref");
		System.out.println(updateXML(filePath, encoding, false, updateText, updateAttribute, insertElement, removeText, removeAttribute, removeElement));
	}
	
	private JobExecResultDto updateXML(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		List<Map<String, Object>> files = JSON.parseObject(pluginInput.get("rule").toString(), new TypeReference<List<Map<String,Object>>>(){});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT, true);
		Map<String, Object> message = new HashMap<String, Object>();
		result.put(MESSAGE, message);
		for (Map<String, Object> file : files) {
			String filePath = file.get("file").toString();
			boolean checkDTD = Boolean.valueOf(file.get("checkDTD").toString());
			List<String> emptyList = Collections.emptyList();
			List<String> updateText = file.get("updateText") == null || "".equals(file.get("updateText")) ? emptyList : (List<String>) file.get("updateText");
			List<String> updateAttribute = file.get("updateAttribute") == null || "".equals(file.get("updateAttribute")) ? emptyList : (List<String>) file.get("updateAttribute");
			List<String> insertElement = file.get("insertElement") == null || "".equals(file.get("insertElement")) ? emptyList : (List<String>) file.get("insertElement");
			List<String> removeText = file.get("removeText") == null || "".equals(file.get("removeText")) ? emptyList : (List<String>) file.get("removeText");
			List<String> removeAttribute = file.get("removeAttribute") == null || "".equals(file.get("removeAttribute")) ? emptyList : (List<String>) file.get("removeAttribute");
			List<String> removeElement = file.get("removeElement") == null || "".equals(file.get("removeElement")) ? emptyList : (List<String>) file.get("removeElement");
			String encoding = file.get("encoding").toString();
			Map<String, Object> fileResult = updateXML(filePath, encoding, checkDTD, updateText, updateAttribute, insertElement, removeText, removeAttribute, removeElement);
			message.put(filePath, fileResult);
			if (!Boolean.valueOf(fileResult.get(RESULT).toString())) {
				result.put(RESULT, false);
				break;
			}
		}
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(RESULT).toString()));
		execResultDto.setMsg(JSON.toJSONString(result.get(MESSAGE)));
		return execResultDto;
	}
	
	private List<INIFile> parseIni(String content) {
		String[] lines = content.split("\n");
		List<INIFile> files = new ArrayList<INIFile>();
		INIFile currentFile = null;
		String currentAction = "";
		Section currrentSection = null;
		StringBuilder currentComment = new StringBuilder();
		List<String> actions = Arrays.asList("新增", "修改", "删除");
		for (String line : lines) {
			if (line.endsWith(".ini")) {
				currentFile = new INIFile(line);
				currrentSection = new Section("[]");
				currentFile.getSections().add(currrentSection);
				files.add(currentFile);
				continue;
			}
			if (actions.contains(line)) {
				currentAction = line;
				continue;
			}
			if (line.startsWith("[") && line.endsWith("]")) {
				currrentSection = new Section(line.substring(1, line.length() - 1));
				currentFile.getSections().add(currrentSection);
				if (!currentComment.toString().equals("")) {
					currrentSection.setComment(currentComment.toString().substring(1));
					currentComment = new StringBuilder();
				}
				continue;
			}
			if (line.startsWith(";")) {
				currentComment.append("\n" + line.substring(1));
				continue;
			}
			if (line.contains("=")) {
				int i = line.indexOf("=");
				String key = line.substring(0, i);
				String value = line.substring(i + 1);
				Item item = new Item(key, value);
				if (!currentComment.toString().equals("")) {
					item.setComment(currentComment.toString().substring(1));
					currentComment = new StringBuilder();
				}
				if (line.contains(";")) {
					int ii = value.indexOf(";");
					String valueNew = value.substring(0, ii);
					String lineComment = value.substring(ii + 1);
					item.setValue(valueNew);
					item.setLineComment(lineComment);
				}
				if (actions.get(0).equals(currentAction)) {
					currrentSection.getAddItems().add(item);
				}
				if (actions.get(1).equals(currentAction)) {
					currrentSection.getUpdateItems().add(item);
				}
				if (actions.get(2).equals(currentAction)) {
					currrentSection.getRemoveItems().add(item);
				}
				continue;
			}
		}
		return files;
	}
	
	public void testParseINI() {
		String content = "utf-8.ini\n\n新增\n;key1 comment\nkey1=v\n;key2 comment\nkey2=v\n;section1 comment\n;multi line\n;section1 comment\n[section1]\n;item1 comment\nitem1=v1\nitem2=v2\n\n修改\n;section2 comment\n[section2]\nitem1=v1\n;item2 comment\nitem=v2\n删除\n[section3]\nitem=v\n\n\n\nutf-1.ini\n新增\n;section1 comment\n[section1]\n;item1 comment\nitem1=v1\nitem2=v2\n修改\n;section2 comment\n[section2]\nitem1=v1\n;item2 comment\nitem=v2\n删除\n[section3]\nitem=v\n\n\n\n\nutf-2.ini\n新增\n;section1 comment\n[section1]\n;item1 comment\nitem1=v1\nitem2=v2\n修改\n;section2 comment\n[section2]\nitem1=v1\n;item2 comment\nitem=v2\n删除\n[section3]\nitem=v";
		System.out.println(JSON.toJSONString(this.parseIni(content)));
	}
	
	private Map<String, Object> getIniItem(IniFile iniFile, String itemName, String sectionName) {
		List<IniSection> sections = new ArrayList<IniSection>();
		for (IniSection section : iniFile.getSections()) {
			if (section.hasItem(itemName)) {
				if (sectionName != null) {
					if (sectionName.equals(section.getName())) {
						sections.add(section);
					}
				} else {
					sections.add(section);
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		if (sections.size() == 1) {
			result.put("result", true);
			result.put("section", sections.get(0));
			return result;
		} else if (sections.size() == 0) {
			result.put("result", false);
			result.put("info", itemName + " doesn't exsit");
			return result;
		} else {
			result.put("result", false);
			StringBuilder sb = new StringBuilder();
			for (IniSection iniSection : sections) {
				sb.append(",").append(iniSection.getName());
			}
			result.put("info", itemName + " exsits in " + sb.toString().substring(1));
			return result;
		}
	}
	
	private void copyFile(File src, File dest) throws IOException {
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		InputStream is = new FileInputStream(src);
		OutputStream os = new FileOutputStream(dest);
		byte[] b = new byte[1024];
		int bNum = 0;
		while ((bNum = is.read(b)) != -1) {
			os.write(b, 0, bNum);
		}
		is.close();
		os.close();
	}
	
	private int getItemLocation(IniSection section, String itemName) {
		for (int i = 0; i < section.getNumberOfItems(); i++) {
			IniItem item = section.getItem(i);
			if (item.getName().equals(itemName)) {
				return i;
			}
		}
		return -1;
	}
	
	private Map<String, Object> getItemLocationInfo(Item item) {
		if (item.getComment() == null) {
			return Collections.emptyMap();
		} else {
			String[] lines = item.getComment().split("\n");
			Pattern before = Pattern.compile("[ ]*(.+)=(.+)[ ]+前面");
			Pattern after = Pattern.compile("[ ]*(.+)=(.+)[ ]+后面");
			Matcher matcher = null;
			for (String line : lines) {
				matcher = before.matcher(line);
				if (matcher.find()) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("location", "before");
					result.put("itemKey", matcher.group(1));
					return result;
				}
				matcher = after.matcher(line);
				if (matcher.find()) {
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("location", "after");
					result.put("itemKey", matcher.group(1));
					return result;
				}
			}
		}
		return Collections.emptyMap();
	}
	
	private String getRelativePath(File file, String root) throws Exception {
		if (file.getPath().startsWith(new File(root).getPath())) {
			return file.getPath().substring(root.length());
		} else {
			throw new Exception(file.getPath() + " and " + new File(root).getPath() + "don't match");
		}
	}
	
	private String trimComment(Item item) {
		if (item.getComment() == null) {
			return null;
		}
		String[] lines = item.getComment().split("\n");
		List<String> trimLines = new ArrayList<String>();
		for (int i = 0; i < lines.length; i++) {
			Pattern before = Pattern.compile("[ ]*(.+)=(.+)[ ]+前面");
			Pattern after = Pattern.compile("[ ]*(.+)=(.+)[ ]+后面");
			Pattern comment = Pattern.compile("注释掉");
			if (before.matcher(lines[i]).find()) {
				continue;
			}
			if (after.matcher(lines[i]).find()) {
				continue;
			}
			if (comment.matcher(lines[i]).find()) {
				continue;
			}
			trimLines.add(lines[i]);
		}
		if (trimLines.isEmpty()) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String line : trimLines) {
			sb.append("\n").append(line);
		}
		return sb.toString().substring(1);
	}
	
	private boolean isCommentItem(Item item) {
		if (item.getComment() == null) {
			return false;
		} else if (item.getComment().contains("注释掉")) {
			return true;
		} else {
			return false;
		}
	}
	
	private void commentItem(IniFile iniFile, Item item) throws Exception {
		Map<String, Object> itemInfo = getIniItem(iniFile, item.getName(), null);
		if ((Boolean) itemInfo.get("result")) {
			IniSection section = (IniSection) itemInfo.get("section");
			int location = getItemLocation(section, item.getName());
			if (location == 0) {
				section.removeItem(item.getName());
				if (section.getPostComment() == null || "".equals(section.getPostComment())) {
					section.setPostComment(item.getName() + "=" + item.getValue());
				} else {
					section.setPostComment(section.getPostComment() + "\n" + item.getName() + "=" + item.getValue());
				}
			} else {
				IniItem preItem = section.getItem(location - 1);
				if (preItem.getPostComment() == null || "".equals(preItem.getPostComment())) {
					preItem.setPostComment(item.getName() + "=" + item.getValue());
				} else {
					preItem.setPostComment(preItem.getPostComment() + "\n" + item.getName() + "=" + item.getValue());
				}
				section.removeItem(item.getName());
			}
		} else {
			throw new Exception("can't find " + item.getName() + "=" + item.getValue());
		}
	}
	
	private Map<String, Object> updateIniByContent(List<INIFile> files, String encoding, String itemNameRegEx, boolean isCaseSensitive, String bakPath, String appRootPath) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (INIFile file : files) {
			Map<String, Object> fileResult = new HashMap<String, Object>();
			fileResult.put("result", true);
			IniFile iniFile = new BasicIniFile(isCaseSensitive);
			IniValidator validator = iniFile.getValidator();
			validator.setItemNameRegEx(Pattern.compile(itemNameRegEx));
			validator.setSectionNameRegEx(Pattern.compile(itemNameRegEx));
			File fileI = new File(file.getName());
			IniFileReader reader = new IniFileReader(iniFile, fileI);
			IniFileWriter writer = new IniFileWriter(iniFile, fileI);
			try {
				copyFile(fileI, new File(bakPath + getRelativePath(fileI, appRootPath)));
				reader.read(encoding);
				for (Section section : file.getSections()) {
					if (section.getName().equals("[]")) {
						for (Item item : section.getRemoveItems()) {
							Map<String, Object> itemInfo = getIniItem(iniFile, item.getName(), null);
							if ((Boolean) itemInfo.get("result")) {
								IniSection targetSection = (IniSection) itemInfo.get("section");
								targetSection.removeItem(item.getName());
							} else {
								throw new Exception(itemInfo.get("info").toString());
							}
						}
						for (Item item : section.getUpdateItems()) {
							if (isCommentItem(item)) {
								commentItem(iniFile, item);
								continue;
							}
							Map<String, Object> itemInfo = getIniItem(iniFile, item.getName(), null);
							if ((Boolean) itemInfo.get("result")) {
								IniSection targetSection = (IniSection) itemInfo.get("section");
								IniItem iniItem = targetSection.getItem(item.getName());
								iniItem.setValue(item.getValue());
								if (item.getComment() != null) {
									String comment = trimComment(item);
									if (comment != null) {
										iniItem.setPreComment(comment);
									}
								}
								if (item.getLineComment() != null) {
									iniItem.setEndLineComment(item.getLineComment());
								}
							} else {
								throw new Exception(itemInfo.get("info").toString());
							}
						}
						if (section.getAddItems().size() > 0) {
							StringBuilder sb = new StringBuilder();
							for (Item item : section.getAddItems()) {
								Map<String, Object> location = getItemLocationInfo(item);
								if (location.isEmpty()) {
									sb.append(",").append(item.getName());
								}
							}
							if (sb.toString().length() > 0) {
								throw new Exception("Can't add items: " + sb.toString().substring(1) + " without section or location info");
							}
							for (Item item : section.getAddItems()) {
								Map<String, Object> location = getItemLocationInfo(item);
								Map<String, Object> itemInfo = getIniItem(iniFile, location.get("itemKey").toString(), null);
								if ((Boolean) itemInfo.get("result")) {
									IniSection targetSection = (IniSection) itemInfo.get("section");
									int position = getItemLocation(targetSection, location.get("itemKey").toString());
									IniItem iniItem = new IniItem(item.getName(), validator, isCaseSensitive);
									iniItem.setValue(item.getValue());
									if (item.getComment() != null) {
										String comment = trimComment(item);
										if (comment != null) {
											iniItem.setPreComment(comment);
										}
									}
									if (item.getLineComment() != null) {
										iniItem.setEndLineComment(item.getLineComment());
									}
									if (targetSection.hasItem(item.getName())) {
										throw new Exception(targetSection.getName() + " already has " + item.getName());
									}
									if ("before".equals(location.get("location"))) {
										targetSection.addItem(iniItem, position);
									}
									if ("after".equals(location.get("location"))) {
										targetSection.addItem(iniItem, position + 1);
									}
								} else {
									throw new Exception(itemInfo.get("info").toString());
								}
							}
						}
						continue;
					}
					if (section.getAddItems().isEmpty() && section.getUpdateItems().isEmpty() && section.getRemoveItems().isEmpty()) {
						iniFile.removeSection(section.getName());
						continue;
					}
					IniSection iniSection = iniFile.getSection(section.getName());
//					if (section.getComment() != null) {
//						iniSection.setPreComment(section.getComment());
//					}
					for (Item item : section.getRemoveItems()) {
						iniSection.removeItem(iniSection.getItem(item.getName()));
					}
					for (Item item : section.getUpdateItems()) {
						if (isCommentItem(item)) {
							commentItem(iniFile, item);
							continue;
						}
						IniItem iniItem = iniSection.getItem(item.getName());
						if (iniItem == null) {
							throw new Exception("Can't find " + item.getName() + " in " + iniSection.getName());
						}
						iniItem.setValue(item.getValue());
						if (item.getComment() != null) {
							String comment = trimComment(item);
							if (comment != null) {
								iniItem.setPreComment(comment);
							}
						}
						if (item.getLineComment() != null) {
							iniItem.setEndLineComment(item.getLineComment());
						}
					}
					for (Item item : section.getAddItems()) {
						Map<String, Object> location = getItemLocationInfo(item);
						if (!location.isEmpty()) {
							Map<String, Object> itemInfo = getIniItem(iniFile, location.get("itemKey").toString(), section.getName());
							if ((Boolean) itemInfo.get("result")) {
								IniSection targetSection = (IniSection) itemInfo.get("section");
								int position = getItemLocation(targetSection, location.get("itemKey").toString());
								IniItem iniItem = new IniItem(item.getName(), validator, isCaseSensitive);
								iniItem.setValue(item.getValue());
								if (item.getComment() != null) {
									String comment = trimComment(item);
									if (comment != null) {
										iniItem.setPreComment(comment);
									}
								}
								if (item.getLineComment() != null) {
									iniItem.setEndLineComment(item.getLineComment());
								}
								if (targetSection.hasItem(item.getName())) {
									throw new Exception(targetSection.getName() + " already has " + item.getName());
								}
								if ("before".equals(location.get("location"))) {
									targetSection.addItem(iniItem, position);
								}
								if ("after".equals(location.get("location"))) {
									targetSection.addItem(iniItem, position + 1);
								}
							} else {
								IniItem iniItem = new IniItem(item.getName(), validator, isCaseSensitive);
								iniItem.setValue(item.getValue());
								if (item.getComment() != null) {
									iniItem.setPreComment(trimComment(item));
								}
								if (item.getLineComment() != null) {
									iniItem.setEndLineComment(item.getLineComment());
								}
								if (iniSection == null) {
									iniSection = new BasicIniSection(section.getName(), validator, isCaseSensitive);
									iniFile.addSection(iniSection);
								}
								if (iniSection.hasItem(item.getName())) {
									throw new Exception(iniSection.getName() + " already has " + item.getName());
								}
								iniSection.addItem(iniItem);
								if (fileResult.containsKey("message")) {
									fileResult.put("message", fileResult.get("message") + "\nCan't find " + location.get("itemKey").toString() + " in " + section.getName() + ", add " + item.getName() + " to the end of " + section.getName());
								} else {
									fileResult.put("message", "Can't find " + location.get("itemKey").toString() + " in " + section.getName() + ", add " + item.getName() + " to the end of " + section.getName());
								}
							}
						} else {
							IniItem iniItem = new IniItem(item.getName(), validator, isCaseSensitive);
							iniItem.setValue(item.getValue());
							if (item.getComment() != null) {
								iniItem.setPreComment(trimComment(item));
							}
							if (item.getLineComment() != null) {
								iniItem.setEndLineComment(item.getLineComment());
							}
							if (iniSection == null) {
								iniSection = new BasicIniSection(section.getName(), validator, isCaseSensitive);
								iniFile.addSection(iniSection);
							}
							if (iniSection.hasItem(item.getName())) {
								throw new Exception(iniSection.getName() + " already has " + item.getName());
							}
							iniSection.addItem(iniItem);
						}
					}
					if (section.getComment() != null) {
						iniSection.setPreComment(section.getComment());
					}
				}
				writer.write(encoding);
			} catch (Throwable e) {
				log.error("", e);
				fileResult.put("result", false);
				fileResult.put("message", getStackTrace(e));
			}
			result.put(file.getName(), fileResult);
		}
		return result;
	}
	
	private JobExecResultDto updateIniByContent(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		Map<String, Object> messageMap = JSON.parseObject(pluginInput.get("cd_message").toString());
		String encoding = pluginInput.get("encoding").toString();
		String content = pluginInput.get("content").toString();
		String bakPath = pluginInput.get("bakPath").toString();
		if (!bakPath.endsWith("/")) {
			bakPath = bakPath + "/";
		}
		
		bakPath = bakPath + messageMap.get("resourceVersionId").toString() + "/app/";
		String appRootPath = pluginInput.get("appRootPath").toString();
		if (!appRootPath.endsWith("/")) {
			appRootPath = appRootPath + "/";
		}
		boolean isCaseSensitive = Boolean.valueOf(pluginInput.get("isCaseSensitive").toString());
		String itemNameRegEx = "[^=]*";
		List<INIFile> files = this.parseIni(content);
		Map<String, Object> updateResult = this.updateIniByContent(files, encoding, itemNameRegEx, isCaseSensitive, bakPath, appRootPath);
		Set<String> keys = updateResult.keySet();
		boolean flag = true;
		for (String key : keys) {
			Map<String, Object> fileResult = (Map<String, Object>) updateResult.get(key);
			if (!Boolean.valueOf(fileResult.get("result").toString())) {
				flag = false;
				break;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(RESULT, flag);
		result.put(MESSAGE, updateResult);
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(Boolean.valueOf(result.get(RESULT).toString()));
		execResultDto.setMsg(JSON.toJSONString(result.get(MESSAGE)));
		return execResultDto;
	}

	public JobExecResultDto execute(JobDetailDto detailDTO) {
		log.info("utilsPlugin receive job");
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

		Map<String, Object> pluginInput = JSON.parseObject(detailDTO.getJobDetailParam());

		if ("DetectPort".equals(detailDTO.getTypeCode())) {
			return detectPort(detailDTO, pluginInput);
		}
		if ("download".equals(detailDTO.getTypeCode())) {
			return download(detailDTO, pluginInput);
		}
		if ("DownloadArtifact".equals(detailDTO.getTypeCode())) {
			return downloadArtifact(detailDTO, pluginInput);
		}
		if ("DownloadComp".equals(detailDTO.getTypeCode())) {
			return downloadComp(detailDTO, pluginInput);
		}
		if ("DownloadSaltArtifact".equals(detailDTO.getTypeCode())) {
			return downloadSaltArtifact(detailDTO, pluginInput);
		}
		if ("DownloadSaltComp".equals(detailDTO.getTypeCode())) {
			return downloadSaltComp(detailDTO, pluginInput);
		}
		if ("config".equals(detailDTO.getTypeCode())) {
			return config(detailDTO, pluginInput);
		}
		if ("SaltConfig".equals(detailDTO.getTypeCode())) {
			return saltConfig(detailDTO, pluginInput);
		}
		if ("unzip".equals(detailDTO.getTypeCode())) {
			return unzip(detailDTO, pluginInput);
		}
		if ("GetCompTargetVersion".equals(detailDTO.getTypeCode())) {
			return getCompTargetVersion(detailDTO, pluginInput);
		}
		if ("GetCompCurrentVersion".equals(detailDTO.getTypeCode())) {
			return getCompCurrentVersion(detailDTO, pluginInput);
		}
		if ("JudgeEmpty".equals(detailDTO.getTypeCode())) {
			return judgeEmpty(detailDTO, pluginInput);
		}
		if ("LoopCount".equals(detailDTO.getTypeCode())) {
			return loopCount(detailDTO, pluginInput);
		}
		if ("Snapshot".equals(detailDTO.getTypeCode())) {
			return snapshot(detailDTO, pluginInput);
		}
		if ("updateIni".equals(detailDTO.getTypeCode())) {
			return updateIni(detailDTO, pluginInput);
		}
		if ("updateXML".equals(detailDTO.getTypeCode())) {
			return updateXML(detailDTO, pluginInput);
		}
		if ("updateIniByContent".equals(detailDTO.getTypeCode())) {
			return updateIniByContent(detailDTO, pluginInput);
		}
		// 未找到匹配的typecode
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(false);
		execResultDto.setMsg("未找到对应的typecode");
		return execResultDto;
	}
	
	public static void main(String[] args) {
//		new UtilsPlugin().testUpdateIni();
//		new UtilsPlugin().testUpdateXML();
		new UtilsPlugin().testParseINI();
	}

}