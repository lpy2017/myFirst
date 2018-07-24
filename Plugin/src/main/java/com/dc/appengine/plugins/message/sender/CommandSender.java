package com.dc.appengine.plugins.message.sender;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.constants.Constants.App;
import com.dc.appengine.plugins.context.Context;
import com.dc.appengine.plugins.context.ContextBuilder;
import com.dc.appengine.plugins.context.ContextPropertiesConverter;
import com.dc.appengine.plugins.message.bean.InnerMessage;
import com.dc.appengine.plugins.message.client.topic.PaasTopicClient;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.MessageHelper;
import com.dcfs.impls.esb.ESBConfig;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.admin.IAdminMessage;
import com.dcfs.interfaces.esb.admin.IManageComponent;

public abstract class CommandSender {
	private static final Logger log = LoggerFactory.getLogger(CommandSender.class);

	/**
	 * 发送消息
	 * 
	 * @param body
	 * @param header
	 * @return
	 */
	public byte[] send(String body, Properties header) {
		IManageComponent manager = ManagerFactory.getManager();
		manager.process(body.getBytes(), header);
		return null;
	}

	public void sendMessageByMOM(Map<String,Object> paramMap,Map<String,Object> messageMap, String op) {
		// 具体这个下载实现类的具体操作 master通过mom给agent发消息
		InnerMessage<Map<String, Object>> request = new InnerMessage<Map<String, Object>>();
		request.setContent(paramMap);// map中包含下载的基本信息，主要实例id,下载路径
		request.setOp(op);
		Context context = ContextBuilder.build(messageMap.get(Constants.Plugin.NODEIP).toString(), IAdminMessage.ADMIN_MESSAGE, null, request,
				Constants.Plugin.NODECOMMANDINVOKER);
		Properties header = ContextPropertiesConverter.context2Properties(context);

		header.setProperty("SLEEP_TIME", ESBConfig.getConfig().getProperty("SLEEP_TIME"));
		header.setProperty(App.INSTANCE_ID, messageMap.get(Constants.Plugin.INSTANCEID).toString());
		if (log.isDebugEnabled()) {
			log.debug("发送到" + messageMap.get(Constants.Plugin.NODEIP).toString() + "信息：" + JSONObject.toJSONString(context.getPayload()));
		}
		LogRecord.send2Master(paramMap.get(Constants.Plugin.WORKITEMID).toString(), 
				LogRecord.getCurrentTime()+paramMap.get(Constants.Plugin.PLUGINNAME)+"send mom message to node" + messageMap.get(Constants.Plugin.NODEIP).toString() + "信息：" + JSONObject.toJSONString(context.getPayload()));
		send(JSONObject.toJSONString(context.getPayload()), header);
	}
	
	public String sendNodeTopic(Map<String, Object> param, String op) {
		try {
			InnerMessage<Map<String, Object>> message = new InnerMessage<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			message.setOp("pluginUpdate");
			map.put("plugin", param);
			map.put("op", op);
			message.setContent(map);
			final byte[] bs = JSON.toJSONString(message, SerializerFeature.DisableCircularReferenceDetect)
					.getBytes();
			final Properties headers = new Properties();
			log.debug("发布主题("+"nodemsg"+")消息：load plugin "+JSON.toJSONString(message));
			PaasTopicClient.getInstance().publish("nodemsg", bs, headers);
			return MessageHelper.wrap("result", true);
		} catch (Exception e) {
			log.error("failed to notify node: " + op + ", " + JSON.toJSONString(param), e);
			return MessageHelper.wrap("result", false, "info", e.getMessage());
		}
	}
}
