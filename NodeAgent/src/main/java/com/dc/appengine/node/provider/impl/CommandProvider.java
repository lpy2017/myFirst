package com.dc.appengine.node.provider.impl;

import java.util.Map;

import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.instance.InstanceModel;
import com.dc.appengine.node.instance.RuntimeInstanceRegistry;
import com.dc.appengine.node.instance.snapshot.Consumer;
import com.dc.appengine.node.instance.snapshot.Op;
import com.dc.appengine.node.instance.snapshot.SnapshotParam;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.provider.AbstractProvider;

public class CommandProvider extends AbstractProvider {

	@Override
	protected void doAction(String id, InnerMessage<Map<String, Object>> message)
			throws Exception {
		InstanceModel instanceModel = RuntimeInstanceRegistry.getInstance()
				.get(id);

		final Map<String, Object> content = message.getContent();
		instanceModel.setAppId((String) content.get(Constants.App.APP_ID));
		instanceModel.setInstanceId((String) content
				.get(Constants.App.INSTANCE_ID));
		Consumer.getInstance().put(
				new SnapshotParam(Op.ADD, instanceModel.getId()));
		// nativeExeUnit.doAction(message);

	}

}
