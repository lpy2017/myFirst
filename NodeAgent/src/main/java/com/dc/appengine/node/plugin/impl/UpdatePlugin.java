package com.dc.appengine.node.plugin.impl;

import java.util.Map;

import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.plugin.AbstractPlugin;
import com.dc.appengine.plugins.manager.service.impl.LoadPlugin;

public class UpdatePlugin extends
		AbstractPlugin<InnerMessage<Map<String, Object>>> {

	@SuppressWarnings("unchecked")
	@Override
	public InnerMessage<Map<String, Object>> excute(
			InnerMessage<Map<String, Object>> message) throws Exception {
		if ("pluginUpdate".equals(message.getOp())) {
			LoadPlugin loadPlugin = new LoadPlugin();
			loadPlugin.dealPlugin((Map<String, Object>)message.getContent().get("plugin"), (String)message.getContent().get("op"));
		}
		return null;
	}

}
