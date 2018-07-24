package com.dc.appengine.plugins.service.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class UpDateReleaseTaskStatus extends UpDateTaskStatus {

	private static final String TASKSTATUS = "taskStatus";

	private String taskStatus;

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
	public String doAgent() throws IOException, InterruptedException {
		if (JudgeUtil.isEmpty(taskStatus)) {
			//默认07处理成功
			taskStatus = RELEASE_TASK_SUCCESS;
		} else {
			//默认状态不允许设置 00-初始化 01-创建 02-更新 03-处理中 04-已解决 05-已关闭 06-重开启 07处理成功 08处理失败
			if ("00".equals(taskStatus) || "01".equals(taskStatus) || "02".equals(taskStatus) || "03".equals(taskStatus)
					|| "04".equals(taskStatus) || "05".equals(taskStatus) || "06".equals(taskStatus)
					|| "08".equals(taskStatus)) {
				taskStatus = RELEASE_TASK_SUCCESS;
			}
		}
		String result = updateReleaseTaskStatus(paramMap, taskStatus);
		String mailContend = "";
		if (!JudgeUtil.isEmpty(pluginInput.get(MAILCONTENT_KEY))) {
			mailContend = pluginInput.get(MAILCONTENT_KEY).toString();
			JSONObject json = JSON.parseObject(result);
			json.put(Constants.Plugin.MESSAGE, mailContend);// 按指定消息发送邮件
			result = json.toJSONString();
		}
		return result;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(TASKSTATUS))) {
			this.taskStatus = this.pluginInput.get(TASKSTATUS).toString();
		}
	}

}
