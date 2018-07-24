package com.dc.appengine.node.message.invoker.impl;

import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.configuration.PAASConfig;
import com.dc.appengine.node.message.ConsumerClient4PaaS;
import com.dcfs.impls.esb.client.ConsumerClient;
import com.dcfs.interfaces.esb.AdminServiceException;
import com.dcfs.interfaces.esb.UnknowServiceException;
import com.dcfs.interfaces.esb.admin.ICompInvoker;
import com.dcfs.interfaces.esb.client.IllegalAppHeaderException;

public class NodeSendInvoker implements ICompInvoker {

	private Logger log = LoggerFactory.getLogger(NodeSendInvoker.class);

	private static NodeSendInvoker nodeSend = new NodeSendInvoker();

	public static NodeSendInvoker getInstace() {
		return nodeSend;
	}

	/**
	 * 主动向master发送部署等执行完成的结果
	 */
	@SuppressWarnings("rawtypes")
	public byte[] invoke(byte[] body, Properties header)
			throws AdminServiceException {

		try {
			Map bodyMessage = JSON.parseObject(new String(body));
			log.debug("***********Send op result to master: "
					+ bodyMessage);
			ConsumerClient4PaaS consumerPaas = new ConsumerClient4PaaS(
					ConsumerClient.getInstance(), PAASConfig.getConfig()
							.getInstallRoot() + NodeConstant.NODE_MOM_TASKFILE);
			consumerPaas.sendOneWay("NodeService", body, PAASConfig.getConfig()
					.getSleepTime(), header);
		} catch (JMSException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
		} catch (UnknowServiceException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
		} catch (NamingException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
		} catch (IllegalAppHeaderException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage());
			}
		}
		return null;// recevMsg;
	}

}
