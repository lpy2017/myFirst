package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class JudgeEmpty extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(JudgeEmpty.class);
	public static final String Judge_Key = "JudgeKey";//需要是否为空的key
	public String appBak;//表示是否需要备份
	public String judgeKey;
	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		final String json = JSON.toJSONString(paramMap);
		final String appBakFlag=this.appBak;
		Runnable runnable= new Runnable() {
			@Override
			public void run() {
				String result;
				try {
					result = doActive(json);
					resultMap = JSON.parseObject(result,new TypeReference<Map<String,Object>>(){});
				} catch (Exception e) {
					e.printStackTrace();
					log.error(LogRecord.getStackTrace(e));
					resultMap.put(Constants.Plugin.RESULT, false);
					resultMap.put(Constants.Plugin.RESULT_MESSAGE, LogRecord.getStackTrace(e));
				}
				if(gf_variable != null){//设置方法的执行结果
					messageMap.put(gf_variable,(Boolean)resultMap.get(Constants.Plugin.RESULT));
					AbstractPlugin.setGFVariable(gf_variable, appBakFlag,paramMap);//流程变量值为appBakFlag的值，表示是否需要备份，用于判断节点
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
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put(Constants.Plugin.RESULT, true);
		result.put(Constants.Plugin.MESSAGE, "成功判断当前版本是否存在["+this.appBak+"]");
		return JSON.toJSONString(result);
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(pluginInput.get(Judge_Key))){
			this.judgeKey=(String)pluginInput.get(Judge_Key);
			if (!JudgeUtil.isEmpty(componentInput.get(this.judgeKey))) {
				this.appBak = "true";
			}else{
				this.appBak = "false";
			}
		}
	}

}
