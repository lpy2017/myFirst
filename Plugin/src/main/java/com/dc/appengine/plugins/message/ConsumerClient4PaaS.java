package com.dc.appengine.plugins.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executor;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dcfs.impls.esb.client.ConsumerClient;
import com.dcfs.interfaces.esb.IMXSDMessage;
import com.dcfs.interfaces.esb.UnknowServiceException;
import com.dcfs.interfaces.esb.admin.IAdminMessage;
import com.dcfs.interfaces.esb.client.IClientConfig;
import com.dcfs.interfaces.esb.client.IServiceHandler;
import com.dcfs.interfaces.esb.client.IllegalAppHeaderException;

public class ConsumerClient4PaaS extends ConsumerClient {
	
	private ConsumerClient client = null;
	private String filePath = null;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ConsumerClient4PaaS(ConsumerClient client, String filePath) {
		this.client = client;
		this.filePath = filePath;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset(String arg0) throws JMSException, NamingException, UnknowServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] send(String arg0, byte[] arg1, long arg2, Properties arg3)
			throws JMSException, UnknowServiceException, NamingException, IllegalAppHeaderException {
		return client.send(arg0, arg1, arg2, arg3);
	}

	@Override
	public byte[] send(String arg0, String arg1, byte[] arg2, long arg3, Properties arg4)
			throws JMSException, UnknowServiceException, NamingException, IllegalAppHeaderException {
		return client.send(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void sendOneWay(String arg0, byte[] arg1, long arg2, Properties arg3)
			throws JMSException, UnknowServiceException, NamingException, IllegalAppHeaderException {
		boolean flag = false;
		try {
			client.sendOneWay(arg0, arg1, arg2, arg3);
			flag = true;
		} catch (Exception e) {
			log.error("Exception", e);
		}
		if (flag) {
			return;
		} else {
			Properties header = arg3;
			String from = header.getProperty(IMXSDMessage.FROM);
			String to = header.getProperty(IMXSDMessage.DISTINCT_TO);
			String sub_type = header.getProperty(IAdminMessage.SUB_TYPE);
			String fileName = from + "_" + to + "_" + sub_type + "_" + UUID.randomUUID().toString();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("servicename", arg0);
			map.put("body", arg1);
			map.put("sleeptime", arg2);
			map.put("header", arg3);
			File file = new File(filePath + File.separator + fileName);
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
				oos.writeObject(map);
				oos.close();
			} catch (FileNotFoundException e) {
				log.error("FileNotFoundException", e);
			} catch (IOException e) {
				log.error("IOException", e);
			}
		}
	}

	@Override
	public void setConfig(IClientConfig arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setExecutor(Executor arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSecurityFlag(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startProviderListener(IServiceHandler arg0, Executor arg1, String arg2)
			throws JMSException, UnknowServiceException, NamingException {
		// TODO Auto-generated method stub
		
	}

}
