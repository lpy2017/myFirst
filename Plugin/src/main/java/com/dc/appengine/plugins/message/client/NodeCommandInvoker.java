package com.dc.appengine.plugins.message.client;

import java.util.Properties;

import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.message.ConsumerClient4PaaS;
import com.dc.appengine.plugins.service.impl.PAASConfig;
import com.dc.messageclient.impl.jms.command.MessageHeaderConstant;
import com.dcfs.impls.esb.client.ConsumerClient;
import com.dcfs.interfaces.esb.AdminServiceException;
import com.dcfs.interfaces.esb.admin.ICompInvoker;


public class NodeCommandInvoker implements ICompInvoker {

	private static final Log log = LogFactory.getLog(NodeCommandInvoker.class);
	
	private ConsumerClient client;
	
	@SuppressWarnings("unchecked")
	public byte[] invoke(byte[] body, Properties headers)
			throws AdminServiceException {
//		client = ConsumerClient.getInstance();
		boolean flag = false;
		long sleepTime = Long.parseLong( headers.getProperty( "SLEEP_TIME" ) == null 
				? PAASConfig.getConfig().getProperty("node_sleep_time") : headers.getProperty( "SLEEP_TIME" ) );
		do {
			try {
				if( flag ){
					headers.remove("MXSD_DISTINCT_FROM");
					headers.remove("MXSD_FROM");
					headers.remove("mailbox_string_value");
					headers.remove("MXSD_TO");
				}
				headers.put( MessageHeaderConstant.TIMEOUT,
						PAASConfig.getConfig().getProperty("node_message_timeout") );
				log.info("master send to node message header :"+JSON.toJSONString(headers));
				//若消息尚未发出，mom挂掉，则将消息存入文件
				ConsumerClient4PaaS client = new ConsumerClient4PaaS(
						ConsumerClient.getInstance(), Constants.App.MOM_TASKFILE);
				headers.setProperty("MXSD_ADMIN_SUB_TYPE", "NodeCommandInvoker");
				client.sendOneWay("NodeService", body, sleepTime, headers);
				flag = false;
			} catch (JMSException e) {
				log.error( e.getMessage(), e );
				flag = true;
			} catch (Exception e) {
				log.error( e.getMessage(), e );
			}
			if( flag ){
				try {
					Thread.sleep( 1000l );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}			
		} while( flag );
		//返回值不被使用
		return null;
	}
	
}
