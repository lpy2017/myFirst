package com.dc.appengine.node.message.topic;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.dc.appengine.node.message.topic.impl.ToNodeHandlerImpl;
import com.dcfs.impls.esb.client.topic.TopicClient;
import com.dcfs.impls.esb.client.topic.TopicConf;
import com.dcfs.interfaces.esb.client.IServiceHandler;

public class TopicInit {
	public static TopicClient client = null;
	private static TopicConf conf = null;

	public static void init() {

		try {
			client = TopicClient.getInstance();
			conf = TopicConf.getInstance();
			client.setTopicConf(conf);

			IServiceHandler handler = new ToNodeHandlerImpl();
			client.subscribersReady(handler);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
