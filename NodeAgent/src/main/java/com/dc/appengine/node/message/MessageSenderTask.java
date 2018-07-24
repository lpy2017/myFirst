package com.dc.appengine.node.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dcfs.impls.esb.client.ConsumerClient;
import com.dcfs.interfaces.esb.UnknowServiceException;
import com.dcfs.interfaces.esb.client.IllegalAppHeaderException;

public class MessageSenderTask implements Runnable {
	
	private String filePath = null;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private long sleeptime = 1000;
	private ConsumerClient client = null;
	
	public MessageSenderTask(String filePath, ConsumerClient client) {
		this.filePath = filePath;
		this.client = client;
	}
	
	public MessageSenderTask(String filePath, ConsumerClient client, long sleeptime) {
		this.filePath = filePath;
		this.client = client;
		this.sleeptime = sleeptime;
	}

	@Override
	public void run() {
		File directory = new File(filePath);
		File[] files = directory.listFiles();
		Map<Long, File> fileDict = new HashMap<Long, File>();
		List<Long> list = new LinkedList<Long>();
		for (File file : files) {
			fileDict.put(file.lastModified(), file);
			list.add(file.lastModified());
		}
		Collections.sort(list);
		for (Long time : list) {
			File file = fileDict.get(time);
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) ois.readObject();
				ois.close();
				boolean flag = true;
				do {
					try {
						Properties header = (Properties) map.get("header");
						header.remove("MXSD_FROM");
						header.remove("MXSD_TO");
						client.sendOneWay((String) map.get("servicename"), (byte[]) map.get("body"), (Long) map.get("sleeptime"), (Properties) map.get("header"));
						flag = false;
					} catch (JMSException e) {
						log.error("JMSException", e);
						try {
							Thread.sleep(sleeptime);
						} catch (InterruptedException e1) {
							log.error("InterruptedException", e1);
						}
					} catch (UnknowServiceException e) {
						log.error("UnknowServiceException", e);
					} catch (NamingException e) {
						log.error("NamingException", e);
					} catch (IllegalAppHeaderException e) {
						log.error("IllegalAppHeaderException", e);
					}
				} while (flag);
				file.delete();
			} catch (FileNotFoundException e) {
				log.error("FileNotFoundException", e);
			} catch (IOException e) {
				log.error("IOException", e);
			} catch (ClassNotFoundException e) {
				log.error("ClassNotFoundException", e);
			}
		}
	}

}
