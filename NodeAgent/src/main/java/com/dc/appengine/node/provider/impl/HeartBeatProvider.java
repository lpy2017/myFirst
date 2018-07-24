/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.provider.impl;

import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.node.cache.StateCache;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.message.InnerMessage;

public class HeartBeatProvider implements Executable {
	private static Logger LOG = Logger.getLogger(HeartBeatProvider.class);

	public <R> R execute(Context context) throws Exception {
		InnerMessage<Map<String, Object>> message = context.getPayload();
		message.setContent(StateCache.getInstance().cloneContent());
		if (message.getContent() != null) {
			LOG.debug("message content:" + JSONObject.toJSONString(message));
		}
		return null;
	}

}
