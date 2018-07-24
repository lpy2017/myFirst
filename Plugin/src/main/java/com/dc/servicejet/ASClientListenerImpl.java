package com.dc.servicejet;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.service.impl.AbstractPlugin;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.bd.auto.client.ASClientListener;
import com.dc.bd.auto.client.AgentInfo;
import com.dc.bd.auto.client.FileResult;
import com.dc.bd.auto.client.JobExecFileResult;
import com.dc.bd.auto.client.JobExecResult;
import com.dc.bd.auto.client.JobFileDownload;

public class ASClientListenerImpl implements ASClientListener {
	
	private static final Logger log = LoggerFactory.getLogger(ASClientListenerImpl.class);
	private List<String> downloadPlugins = Arrays.asList("download", "DownloadArtifact", "DownloadSaltArtifact");

	@Override
	public void onAgentStart(AgentInfo agentInfo) {
		log.info("onAgentStart");
		log.info("agent domain:{}", agentInfo.getDomain());
		log.info("agent ip:{}", agentInfo.getIp());
		log.info("agent osType:{}", agentInfo.getOsType());
		log.info("agent plugins:{}", agentInfo.getPlugins());
		log.info("agent uuid:{}", agentInfo.getUuid());
		log.info("agent version:{}", agentInfo.getVersion());
	}

	@Override
	public void onClientConnected(String agentIps) {
		log.info("onClientConnected");
		log.info("agent ips:{}", agentIps);
	}

	@Override
	public FileResult onDownloadFile(JobFileDownload file) {
		log.info("onDownloadFile");
		log.info("job file id:{}", file.getJobFileId());
		log.info("md5:{}", file.getMd5());
		log.info("type:{}", file.getType());
		log.info("uuid:{}", file.getUuid());
		File downloadFile = new File("downloadtest.txt");
		try {
			FileWriter fw = new FileWriter(downloadFile);
			fw.write("download test");
			fw.close();
		} catch (Exception e) {
			log.error("", e);
		}
		return new FileResult(downloadFile, true);
	}

	@Override
	public FileResult onDownloadPlugin(String pluginCode) {
		log.info("onDownloadPlugin");
		log.info("pluginCode:{}", pluginCode);
		return null;
	}

//	@Override
//	public void onHeartbeat(List heartbeatBeanList) {
//		log.info("onHeartbeat");
//		log.info("heartbeatBeanList:{}", heartbeatBeanList);
//	}

	@Override
	public void onJobFiles(String uuid, List<JobExecFileResult> jobExecFileResult) {
		log.info("onJobFiles");
		log.info("uuid:{}", uuid);
		for (JobExecFileResult one : jobExecFileResult) {
			log.info("{} abs path:{}", one.getFileName(), one.getResultfile().getAbsolutePath());
		}
	}

	@Override
	public void onJobResult(JobExecResult jobExecResult) {
		log.info("onJobResult");
		log.info("code:{}", jobExecResult.getCode());
		log.info("data:{}", jobExecResult.getData());
		log.info("msg:{}", jobExecResult.getMsg());
		log.info("uuid:{}", jobExecResult.getUuid());
		log.info("VarParams:{}", jobExecResult.getVarParams());
		log.info("isSuccess:{}", jobExecResult.isSuccess());
		
		try {
			String data = jobExecResult.getData();
			if (data != null && !"".equals(data)) {
				Map<String, Object> jsonObj = JSON.parseObject(data);
				String pluginCode = jsonObj.get("pluginCode").toString();
				String typeCode = jsonObj.get("typeCode").toString();
				Map<String, Object> jobData = (Map<String, Object>) jsonObj.get("data");
			}
		} catch (Exception e) {
			log.error("jobExecResult中的data不符合CD返回标准", e);
		}
		
//		String tokenId = jobExecResult.getUuid().split("-")[1];
		String tokenId = jobExecResult.getUuid();
		Client client = ClientBuilder.newClient();
		String frameRest = ConfigHelper.getValue("frameRest").toString();
		try {
			String result = client.target(frameRest).path("WFService").path("getTokenVariable.wf").queryParam("tokenID", tokenId).queryParam("variableName", "_taskCP").request().get(String.class);
			if (result != null && !"".equals(result)) {
				Map<String, Object> jsonObj = JSON.parseObject(result);
				Map<String, Object> paramMap = JSON.parseObject(((Map<String, Object>) jsonObj.get("data")).get("value").toString());
				Map<String, Object> message = (Map<String, Object>) paramMap.get(Constants.Plugin.MESSAGE);
				Map<String, Object> pluginInput = (Map<String, Object>) message.get(paramMap.get(Constants.Plugin.PLUGINNAME).toString());
				// 更新message内容
				message.put("plugin_result", jobExecResult.isSuccess());
				Map<String, Object> messageResult = new HashMap<>();
				messageResult.put(Constants.Plugin.RESULT, jobExecResult.isSuccess());
				messageResult.put(Constants.Plugin.RESULT_MESSAGE, jobExecResult.getMsg());
				message.put(Constants.Plugin.RESULT, JSON.toJSONString(messageResult));
				// 设置节点执行状态
				AbstractPlugin.setGFVariable(pluginInput.get(AbstractPlugin.GF_VARIABLE_KEY).toString(), jobExecResult.isSuccess() + "", paramMap);
				try {
					String data = jobExecResult.getData();
					if (data != null && !"".equals(data)) {
						Map<String, Object> jobResultData = JSON.parseObject(data);
						String pluginCode = jobResultData.get("pluginCode").toString();
						String typeCode = jobResultData.get("typeCode").toString();
						Map<String, Object> jobData = (Map<String, Object>) jobResultData.get("data");
						// 处理loopcount
						if ("CD_PLUGIN_UTILS".equals(pluginCode) && "LoopCount".equals(typeCode)) {
							String key = jobData.get("counterKey").toString();
							String counter = jobData.get(key).toString();
							AbstractPlugin.setGFVariable(key, counter, paramMap);
						}
						// 处理snapshot
						if ("CD_PLUGIN_UTILS".equals(pluginCode) && "Snapshot".equals(typeCode)) {
							String key = jobData.get("ss_variable").toString();
							String ss_variable = jobData.get(key).toString();
							AbstractPlugin.setGFVariable(key, ss_variable, paramMap);
						}
						// 处理filePath
						if ("CD_PLUGIN_UTILS".equals(pluginCode) && downloadPlugins.contains(typeCode)) {
							String filePath = jobData.get("filePath").toString();
							message.put("filePath", filePath);
						}
					}
				} catch (Exception e) {
					log.error("jobExecResult中的data不符合CD返回标准", e);
				}
				// 记录执行日志
				LogRecord.send2Master(paramMap.get(Constants.Plugin.WORKITEMID).toString(),
						LogRecord.getCurrentTime()+paramMap.get(Constants.Plugin.PLUGINNAME)+"do doActive is end outputParam==="+System.lineSeparator()+
						JSON.toJSONString(paramMap)+System.lineSeparator()+"plugin_result_active:"+jobExecResult.isSuccess()+" "+"result_message:"+System.lineSeparator()+jobExecResult.getMsg());
				// 触发工作流执行后续操作
				AbstractPlugin.invokeWorkflowServer(paramMap);
			}
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public void onHeartbeat(String info) {
		log.info("heartBeat info:{}", info);
	}

}
