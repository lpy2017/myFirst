/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.provider;

import java.util.Map;

import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.message.InnerMessage;

/**
 * AbstractProvider.java
 * 
 * @author liubingj
 */
public abstract class AbstractProvider implements Executable {

	public <R> R execute(Context context) throws Exception {
		final InnerMessage<Map<String, Object>> message = context.getPayload();
		final Map<String, Object> content = message.getContent();
		final String appId = (String) content.get(Constants.App.APP_ID);
		final String instanceId = (String) content
				.get(Constants.App.INSTANCE_ID);
		final String id = (appId != null ? appId : "").concat("_").concat(
				instanceId != null ? instanceId : "");
		doAction(id, message);
		return null;
	}

	protected abstract void doAction(String id,
			InnerMessage<Map<String, Object>> message) throws Exception;

}
