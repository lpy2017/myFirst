package com.dc.appengine.plugins.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.DoAgentThreadPool;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class LoopCount extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(LoopCount.class);
	public static final String LoopCounter_Key = "counter_variable";
	public static final String CounterOperation_Key = "counter_operation";
	public static final String Operation_Regex = "^[\\+,\\-]{1}[1-9]+$";
	public static final String Operation_Regex_Plus = "^\\+[1-9]+$";
	public static final String Operation_Regex_Minus = "^\\-[1-9]+$";
	public static final String Default_Operation_Plus = "+1";
	public static final String Default_Operation_Minus = "-1";
	public String loopCounter;
	public String counterOperation;

	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		final String json = JSON.toJSONString(paramMap);
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
					AbstractPlugin.setGFVariable(gf_variable, resultMap.get(Constants.Plugin.RESULT).toString(),paramMap);
					messageMap.put(gf_variable,(Boolean)resultMap.get(Constants.Plugin.RESULT));
				}
				if (!JudgeUtil.isEmpty(loopCounter)) {
					setGFVariable(loopCounter, resultMap.get(loopCounter).toString(),paramMap);
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
	public String doAgent() throws Exception {
		Map<String, Object> result = new HashMap<>();
		JSONObject insvarMap = (JSONObject) paramMap.get(Constants.Plugin.INSVARMAP);
		boolean isOp = Pattern.matches(Operation_Regex, counterOperation);
		if (!isOp) {
			log.error("计数器自增/减表达式：" + counterOperation + "格式错误，请写成+1或-1！");
			throw new Exception("计数器自增/减表达式：" + counterOperation + "格式错误，请写成+1或-1！");
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
				throw new Exception("计数器的值:" + counterNum + "非整型，无法自增/减！");
			}
		} else {
			result.put(Constants.Plugin.RESULT, true);
			result.put(Constants.Plugin.MESSAGE, "未配置计数器变量，无自增/减操作！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(pluginInput.get(LoopCounter_Key))) {
			this.loopCounter = "" + pluginInput.get(LoopCounter_Key);
			if (!JudgeUtil.isEmpty(pluginInput.get(CounterOperation_Key))) {
				String op = "" + pluginInput.get(CounterOperation_Key);
				counterOperation = op;
			} else {
				this.counterOperation = Default_Operation_Plus;
			}
		}
	}

}
