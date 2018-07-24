package com.dc.appengine.plugins.message.client.topic;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.dcfs.impls.esb.client.topic.TopicClient;
import com.dcfs.impls.esb.client.topic.TopicConf;
import com.dcfs.interfaces.esb.client.IServiceHandler;

/**
 * topic 客户端
 * 
 */
public class PaasTopicClient {
	private static TopicClient client = null;
	private TopicConf conf = null;
	private static PaasTopicClient factory;

	private static final Logger LOG = LoggerFactory.getLogger(PaasTopicClient.class);

	private PaasTopicClient() {
		init();
	}

	private void init() {
		try {
			client = TopicClient.getInstance();
			conf = TopicConf.getInstance();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		client.setTopicConf(conf);
	}

	public static PaasTopicClient getInstance() {
		if (factory == null) {
			synchronized (PaasTopicClient.class) {
				if (factory == null) {
					factory = new PaasTopicClient();
//					IServiceHandler handler = new NodenInstanceStatusHandlerImpl();
//					client.subscribersReady(handler);
					if (LOG.isDebugEnabled())
						LOG.debug("subscriber is ready...");
				}
			}
		}
		return factory;
	}
	
	public void publish(String serviceName, byte[] body, Properties headers){
		try{
			client.publish(serviceName, body, headers);
		} catch ( Exception e ){
			if( LOG.isDebugEnabled() ){
				LOG.debug( "发布订阅消息(" + serviceName + ")失败！" );
			}
			e.printStackTrace();
		}
	}
}
