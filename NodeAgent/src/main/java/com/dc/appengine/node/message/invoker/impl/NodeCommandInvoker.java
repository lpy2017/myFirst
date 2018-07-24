package com.dc.appengine.node.message.invoker.impl;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.flow.DefaultNodeFlowContext;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.plugin.AbstractPlugin;
import com.dc.appengine.node.plugin.impl.DefaultPlugin;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.AdminServiceException;
import com.dcfs.interfaces.esb.admin.ICompInvoker;
import com.dcfs.interfaces.esb.admin.IManageComponent;

public class NodeCommandInvoker implements ICompInvoker {
	private static Logger log = LoggerFactory
			.getLogger(NodeCommandInvoker.class);
	private static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(NodeConstant.POOL_SIZE, new ThreadFactory() {
				private int count = 0;
				ThreadGroup group = new ThreadGroup("NodeCommandInvoker-Thread");

				public Thread newThread(Runnable r) {
					return new Thread(group, r, group.getName().concat("-")
							+ count++);
				}
			});

	@SuppressWarnings("unchecked")
	@Override
	public byte[] invoke(byte[] body, Properties header)
			throws AdminServiceException {
		if (log.isDebugEnabled()) {
			log.debug("===============Current receive message is [".concat(
					new String(body)).concat("]"));
		}
//		if (!NodeEnv.getInstance().getNodeEnvDefinition().isRegister()) {
//			log.debug("The message is received before register,ignore the message!");
//			return null;
//		}
		String str = JSON.toJSONString(header);
		final Properties headers = JSON.parseObject(str,
				new TypeReference<Properties>() {
				});
		final InnerMessage<Map<String, Object>> message = JSON.parseObject(
				new String(body), InnerMessage.class);
		log.debug("POOL SIZE:" + pool.getPoolSize() + ", active size:"
				+ pool.getActiveCount());
		pool.submit(new Callable<InnerMessage<Map<String, Object>>>() {
			public InnerMessage<Map<String, Object>> call() {
				try {
					// 执行操作
					try {
						AbstractPlugin.getInstance(DefaultPlugin.class).excute(
								message);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						e.printStackTrace();
						AbstractPlugin.errorResult(message, e.getMessage());
					}
					final Context sendContext = new DefaultNodeFlowContext();
					// LOG.debug("node send op result to master "+message+"******************");
					sendContext.setPayload(message);
					headers.remove("MXSD_REPLY_TAG");
					headers.remove("MXSD_DISTINCT_FROM");
					headers.remove("MXSD_DISTINCT_TO");
					headers.remove("MXSD_FROM");// 不移出错
					headers.remove("MXSD_TO");// 不移出错
					headers.setProperty("MXSD_ADMIN_SUB_TYPE",
							"NodeSendInvoker");
					headers.setProperty("MXSD_MSG_TYPE", "ADMIN_MESSAGE");
					// LOG.debug("------------header before into nodeSendInvoker: "+headers);
					IManageComponent manager = ManagerFactory.getManager();
					manager.process(
							JSONObject.toJSONString(sendContext.getPayload())
									.getBytes(), headers);
				} catch (Exception e) {
					log.error("Reply result failed : " + e.getMessage(), e);
				}
				return message;
			}
		});
		// String result = "{\"received\":true, \"op\":\"" + op + "\"}";
		// log.debug("Current reply message is [ " + result + " ]");
		return null;
	}
}