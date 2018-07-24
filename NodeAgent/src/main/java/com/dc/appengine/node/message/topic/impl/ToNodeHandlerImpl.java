package com.dc.appengine.node.message.topic.impl;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.plugin.AbstractPlugin;
import com.dc.appengine.node.plugin.impl.UpdatePlugin;
import com.dcfs.interfaces.esb.client.IServiceHandler;

public class ToNodeHandlerImpl implements IServiceHandler {

	private final static Logger log = LoggerFactory
			.getLogger(ToNodeHandlerImpl.class);
	private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(NodeConstant.POOL_SIZE, new ThreadFactory() {
				private int count = 0;
				ThreadGroup group = new ThreadGroup("ToNodeTopicHandler-Thread");

				public Thread newThread(Runnable r) {
					return new Thread(group, r, group.getName().concat("-")
							+ count++);
				}
			});

	@SuppressWarnings("unchecked")
	@Override
	public byte[] dispatch(String serviceName, byte[] msg, Properties addHeaders) {
		if (msg != null) {
			String jsonStr = new String(msg);
			if (log.isDebugEnabled()) {
				log.debug("============rec msg is("
						+ addHeaders.getProperty("MXSD_TOPIC_NAME") + ")"
						+ jsonStr);
			}
			String strTopicName = addHeaders.getProperty("MXSD_TOPIC_NAME");
			if (strTopicName != null) {
				final InnerMessage<Map<String, Object>> message = JSON
						.parseObject(jsonStr, InnerMessage.class);
				if (message == null)
					return null;
				// plugin管理信息
				if ("nodemsg".equals(strTopicName)) {
					if (message != null && message.getContent() != null) {
						log.debug("POOL SIZE:" + pool.getPoolSize()
								+ ", active size:" + pool.getActiveCount());
						pool.submit(new Callable<InnerMessage<Map<String, Object>>>() {
							public InnerMessage<Map<String, Object>> call() {
								try {
									AbstractPlugin.getInstance(
											UpdatePlugin.class).excute(message);
								} catch (Exception e) {
									log.error(e.getMessage(), e);
									AbstractPlugin.errorResult(message,
											e.getMessage());
								}
								return message;
							}
						});
					}
				}
			}
		}
		return null;
	}
}