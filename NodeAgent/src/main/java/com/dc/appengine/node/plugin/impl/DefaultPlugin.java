package com.dc.appengine.node.plugin.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.plugin.AbstractPlugin;
import com.dc.appengine.plugins.service.impl.PluginService;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class DefaultPlugin extends
		AbstractPlugin<InnerMessage<Map<String, Object>>> {
	private static Logger log = LoggerFactory.getLogger(DefaultPlugin.class);

	@Override
	public InnerMessage<Map<String, Object>> excute(
			InnerMessage<Map<String, Object>> message) throws Exception {
		String op = message.getOp();
		Map<String, Object> pMap = PluginService.getInstance()
				.getPluginInstance(op, NodeConstant.AGENT);
		log.debug("agent active " + pMap);
		Object o = pMap.get(NodeConstant.CLASS_INSTANCE);
		Method m = (Method) pMap.get(NodeConstant.METHOD_INSTANCE);
		Map<String, Object> content = message.getContent();
		String result = (String) m.invoke(o, JSON.toJSONString(content));
		Map<String, Object> mMap = (JSON.parseObject(content.get("message")
				.toString()));
		//=========2017.10.17 hsn设置执行结果
		Map<String, Object> resultMap =  JSON.parseObject(result);
		Boolean result_state = (Boolean)resultMap.get("result");
		mMap.put("plugin_result", result_state);//记录插件的执行结果
		//filePath是内置插件参数，下载插件执行成功后，需将filePath放入message中
		if(result_state && !JudgeUtil.isEmpty(resultMap.get("filePath"))){
			mMap.put("filePath", resultMap.get("filePath"));
		}
		//=========2017.10.17 hsn设置执行结果
		mMap.put("result", result);
		content.put("message", mMap);
		return message;
	}
}
