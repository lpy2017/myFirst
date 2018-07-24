package com.dc.appengine.node.message;

import java.io.IOException;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.dc.appengine.node.configuration.PAASConfig;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.impls.esb.client.ClientConfig;
import com.dcfs.impls.esb.client.ConsumerClient;
import com.dcfs.interfaces.esb.UnknowServiceException;
import com.dcfs.interfaces.esb.admin.IManageComponent;
import com.dcfs.interfaces.esb.client.IClientConfig;
import com.dcfs.interfaces.esb.client.IServiceHandler;

public class InvokerInit {
	private static final Logger log = LoggerFactory.getLogger(InvokerInit.class);
	public static void init(){
		ConsumerClient listener = ConsumerClient.getInstance();
		try {
			listener.setConfig(ClientConfig.getInstance());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		IManageComponent manager = ManagerFactory.getManager();
		manager.setReady(false);
		startAdminListener();
	}
	private static void startAdminListener() {
		IServiceHandler handler = null;
		String handerImplName = PAASConfig.getConfig().getProperty("com.dcfs.esb.client.service.invoker");
		String location = "";
		try {
			location = ClientConfig.getInstance().getLocation();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (SAXException e2) {
			e2.printStackTrace();
		} catch (ParserConfigurationException e2) {
			e2.printStackTrace();
		}
		if (handerImplName == null) {
			log.error("There is no implement class configuration for serviceHander");
		} else
			try {
				handler = (IServiceHandler) Class.forName(handerImplName)
						.newInstance();
			} catch (Throwable e) {
				e.printStackTrace();
			}

		ConsumerClient listener = ConsumerClient.getInstance();
		IClientConfig cf = null;
		try {
			cf = ClientConfig.getInstance();
			String messageSelector = "";
			if (!location.equals("")) {
				messageSelector = null;// messageSelector在消息接口中定义相应的头属性distinct_to;
				if (log.isInfoEnabled())
					log.info("MessageSelector for client " + location
							+ " is " + messageSelector);
			}
			listener.setConfig(cf);
			listener.startProviderListener(handler, PAASConfig
					.createThreadPool(), messageSelector);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (UnknowServiceException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
