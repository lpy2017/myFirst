/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.dispatcher.DispatcherRegistry;
import com.dc.appengine.node.message.InnerMessage;

/**
 * DispatchProvider.java
 * 
 * @author liubingj
 */
public class DispatchService implements Executable {

	private static Logger LOG = LoggerFactory.getLogger(DispatchService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dc.appengine.Executable#execute(com.dc.appengine.Context)
	 */
	@SuppressWarnings("unchecked")
	public <R> R execute(Context context) throws Exception {
		if (context.getPayload() == null) {
			throw new IllegalArgumentException(
					"The payload of context can not be null.");
		}
		if (!(context.getPayload() instanceof InnerMessage<?>)) {
			throw new IllegalArgumentException(
					"The payload of context must be an instance of "
							+ InnerMessage.class.getName());
		}
		final InnerMessage<?> message = context.getPayload();
		try {
			// TODO
			DispatcherRegistry.getInstance().get(message.getOp().toString())
					.execute(context);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			((Map<String, String>) message.getContent()).put("RESULT",
					Boolean.FALSE.toString());
			((Map<String, String>) message.getContent()).put(
					Constants.App.APP_STATE, Boolean.FALSE.toString());
			((Map<String, String>) message.getContent()).put(
					Constants.App.REASON, e.getMessage());
		}
		return null;
	}

}
