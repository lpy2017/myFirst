package com.dc.appengine.plugins.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class Jenkins extends AbstractPlugin {
	
	private String jenkinsUrl;
	private String jobName;
	private String userName;
	private String userPassword;
	private String auth;
//	private String build;
	private String env;
	private String jobs;
	
	private static final Logger log = LoggerFactory.getLogger(Jenkins.class);

	@Override
	public String doPreAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doInvoke() throws Exception {
		final String json=JSON.toJSONString(paramMap);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String result;
				try {
					result = doActive(json);
					resultMap = JSON.parseObject(result,new TypeReference<Map<String,Object>>(){});
				}catch (Exception e) {
					log.error("", e);
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
//		DoAgentThreadPool.getInstance().execute(runnable);
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Map<String, Object> buildNew() {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(jenkinsUrl).path("crumbIssuer").path("api").path("json");
			JSONObject jsonObj = target.request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
			
			target = client.target(jenkinsUrl).path("job").path(jobName).path("build");
			Map<String, Object> callFrom = new HashMap<>();
			callFrom.put("name", "callfrom");
			callFrom.put("value", "cd");
			List<Map<String, Object>> parameters = new ArrayList<>();
			parameters.add(callFrom);
			Map<String, Object> jsonMap = new HashMap<>();
			jsonMap.put("parameter", parameters);
			Form form = new Form();
			form.param("json", JSON.toJSONString(jsonMap));
			Response response = target.request().header("Authorization", "Basic " + this.auth).header(jsonObj.getString("crumbRequestField"), jsonObj.get("crumb")).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
			
			target = client.target(jenkinsUrl).path("job").path(jobName).path("lastBuild").path("buildNumber");
			String buildNumber = target.request().header("Authorization", "Basic " + this.auth).get(String.class);
			
			String result = null;
			while (true) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.error("", e);
				}
				target = client.target(jenkinsUrl).path("job").path(jobName).path("" + buildNumber).path("api").path("json");
				jsonObj = target.request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
				if (!jsonObj.getBooleanValue("building")) {
					result = jsonObj.getString("result");
					break;
				}
			}
			
			target = client.target(jenkinsUrl).path("job").path(jobName).path("" + buildNumber).path("consoleText");
			String log = target.request().header("Authorization", "Basic " + this.auth).get(String.class);
			resultMap.put(Constants.Plugin.RESULT, "SUCCESS".equals(result));
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, log);
		} catch (Exception e) {
			log.error("", e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
			return resultMap;
		} catch (NoSuchMethodError e) {
			log.error("", e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
			return resultMap;
		}
		return resultMap;
	}
	
	private Map<String, Object> build(boolean fromAPI) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(jenkinsUrl).path("crumbIssuer").path("api").path("json");
			JSONObject jsonObj = target.request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
			
			target = client.target(jenkinsUrl).path("job").path(jobName).path("buildWithParameters");
//			Map<String, Object> callFrom = new HashMap<>();
//			callFrom.put("name", "callfrom");
//			callFrom.put("value", "cd");
//			List<Map<String, Object>> parameters = new ArrayList<>();
//			parameters.add(callFrom);
//			Map<String, Object> jsonMap = new HashMap<>();
//			jsonMap.put("parameter", parameters);
			Form form = new Form();
//			form.param("json", JSON.toJSONString(jsonMap));
			form.param("callfrom", "cd");
			if (fromAPI) {
				JSONObject env = JSON.parseObject(this.env);
				Object obj = env.get("jobs");
				if (obj instanceof List) {
					form.param("jobs", JSON.toJSONString(obj));
				}
				if (obj instanceof String) {
					form.param("jobs", env.getString("jobs"));
				}
			} else {
				form.param("jobs", jobs);
			}
//			form.param("jobs", jobs);
			Response response = target.request().header("Authorization", "Basic " + this.auth).header(jsonObj.getString("crumbRequestField"), jsonObj.get("crumb")).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
			String location = response.getHeaderString("Location");
			int i = location.indexOf("item/");
			int queueId = Integer.valueOf(location.substring(i + 5, location.length() - 1));
			
//			target = client.target(jenkinsUrl).path("job").path(jobName).path("api").path("json");
//			jsonObj = target.queryParam("tree", "builds[number,queueId]").request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
//			List<Map<String, Object>> builds = (List<Map<String,Object>>) jsonObj.get("builds");
			int buildNumber = 0;
			while (true) {
				if (buildNumber == 0) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						log.error("", e);
					}
				} else {
					break;
				}
				target = client.target(jenkinsUrl).path("job").path(jobName).path("api").path("json");
				jsonObj = target.queryParam("tree", "builds[number,queueId]").request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
				List<Map<String, Object>> builds = (List<Map<String,Object>>) jsonObj.get("builds");
				for (Map<String, Object> build : builds) {
					if (queueId == Integer.valueOf(build.get("queueId").toString())) {
						buildNumber = Integer.valueOf(build.get("number").toString());
						break;
					}
				}
			}
//			for (Map<String, Object> build : builds) {
//				if (queueId == Integer.valueOf(build.get("queueId").toString())) {
//					buildNumber = Integer.valueOf(build.get("number").toString());
//					break;
//				}
//			}
			
			String result = null;
			while (true) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.error("", e);
				}
				target = client.target(jenkinsUrl).path("job").path(jobName).path("" + buildNumber).path("api").path("json");
				jsonObj = target.request().header("Authorization", "Basic " + this.auth).get(JSONObject.class);
				if (!jsonObj.getBooleanValue("building")) {
					result = jsonObj.getString("result");
					break;
				}
			}
			target = client.target(jenkinsUrl).path("job").path(jobName).path("" + buildNumber).path("consoleText");
			String log = target.request().header("Authorization", "Basic " + this.auth).get(String.class);
			resultMap.put(Constants.Plugin.RESULT, "SUCCESS".equals(result));
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, log);
		} catch (Exception e) {
			log.error("", e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
			return resultMap;
		} catch (NoSuchMethodError e) {
			log.error("", e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
			return resultMap;
		}
		return resultMap;
	}
	
	private Map<String, Object> getLog() {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			Client client = ClientBuilder.newClient();
			JSONObject jsonObj = JSON.parseObject(this.env);
			String buildNumber = jsonObj.get("buildNumber").toString();
			WebTarget target = client.target(jenkinsUrl).path("job").path(jobName).path(buildNumber).path("consoleText");
			String log = target.request().header("Authorization", "Basic " + this.auth).get(String.class);
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, log);
		} catch (Exception e) {
			log.error("", e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
		}
		return resultMap;
	}
	
//	public void testBuild() {
//		pluginInput.put("jenkinsUrl", "http://10.126.3.222:8080");
//		pluginInput.put("jobName", "test");
//		pluginInput.put("userName", "guojwe");
//		pluginInput.put("userPassword", "guojwe");
//		setFields();
//		log.info("build result:\n{}", JSON.toJSONString(build()));
//	}

	@Override
	public String doAgent() throws Exception {
		JSONObject jsonObj = JSON.parseObject(this.env);
		if (jsonObj.containsKey("buildNumber")) {
			return JSON.toJSONString(getLog());
		} else {
			if (jsonObj.containsKey("jobs")) {
				return JSON.toJSONString(build(true));
			} else {
				return JSON.toJSONString(build(false));
			}
//			return JSON.toJSONString(build());
//			return JSON.toJSONString(buildNew());
		}
//		if (this.build != null && !Boolean.valueOf(this.build)) {
//			return JSON.toJSONString(getLog());
//		} else {
//			return JSON.toJSONString(build());
//		}
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get("jenkinsUrl"))) {
			this.jenkinsUrl = this.pluginInput.get("jenkinsUrl").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("jobName"))) {
			this.jobName = this.pluginInput.get("jobName").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("userName"))) {
			this.userName = this.pluginInput.get("userName").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("userPassword"))) {
			this.userPassword = this.pluginInput.get("userPassword").toString();
		}
//		if (!JudgeUtil.isEmpty(this.pluginInput.get("build"))) {
//			this.build = this.pluginInput.get("build").toString();
//		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("env"))) {
			this.env = this.pluginInput.get("env").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("jobs"))) {
			this.jobs = this.pluginInput.get("jobs").toString();
		}
		if (this.userName != null && this.userPassword != null) {
			this.auth = Base64.getEncoder().encodeToString((this.userName + ":" + this.userPassword).getBytes());
		}
	}
	
//	public static void main(String[] args) {
//		new Jenkins().testBuild();
//	}

}
