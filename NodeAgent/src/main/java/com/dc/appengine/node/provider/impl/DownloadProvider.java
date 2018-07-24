package com.dc.appengine.node.provider.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.provider.AbstractProvider;

public class DownloadProvider extends AbstractProvider {
	private static Logger LOG = Logger.getLogger(DownloadProvider.class);
	private static final String TYPE = "type";
	private static final String URL = "url";
	private static final String AUTH = "auth";

	@SuppressWarnings("unused")
	@Override
	protected void doAction(String id, InnerMessage<Map<String, Object>> message)
			throws Exception {
		Map<String, Object> map = message.getContent();
		String type = (String) map.get(TYPE);
		String url = (String) map.get(URL);
		String auth = (String) map.get(AUTH);

		message.getContent().put("reply", "sucess");
		LOG.debug("id: " + id + " message: " + JSON.toJSONString(message));

	}
	
}
