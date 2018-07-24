package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class Snapshot extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Config.class);
	private static final String SS_VARIABLE_KEY  = "ss_variable";
	private static final String SNAPSHOTID_KEY  = "snapshotId";
	private String ss_variable;
	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		// TODO Auto-generated method stub
		final String json = JSON.toJSONString(paramMap);;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
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
				// 传递快照id
				if (!JudgeUtil.isEmpty(ss_variable)) {
					setGFVariable(ss_variable,resultMap.get(SNAPSHOTID_KEY).toString(),paramMap);
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
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		// 调用master生成快照接口
		Form form = new Form();
		form.param(Constants.Plugin.INSTANCEID, "" + messageMap.get(Constants.Plugin.INSTANCEID));
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target(ConfigHelper.getValue("masterRest") + "/ws/deployedApp/saveSnapshotOfInstance");
		String result = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		JSONObject json = JSON.parseObject(result);
		String snapshotId = null;
		Map<String,Object> pluginResult = new HashMap<>();
		if (!json.getBooleanValue(Constants.Plugin.RESULT)) {
			// 生成快照失败
			snapshotId = "";
			pluginResult.put(SNAPSHOTID_KEY, snapshotId);
			pluginResult.put(Constants.Plugin.RESULT, false);
			pluginResult.put(Constants.Plugin.MESSAGE, "生成快照失败");
			return JSON.toJSONString(pluginResult);
		} else {
			snapshotId = json.getString("snapId");
			pluginResult.put(SNAPSHOTID_KEY,snapshotId);
			pluginResult.put(Constants.Plugin.RESULT,true);
			pluginResult.put(Constants.Plugin.MESSAGE,"success");
		}
		return JSON.toJSONString(pluginResult);
	}

	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SS_VARIABLE_KEY ))){
			this.ss_variable=this.pluginInput.get(SS_VARIABLE_KEY ).toString();
		}
	}

	public void setSs_variable(String ss_variable) {
		this.ss_variable = ss_variable;
	}
	
}
