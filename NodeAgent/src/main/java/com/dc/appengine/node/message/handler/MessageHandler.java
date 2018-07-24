package com.dc.appengine.node.message.handler;

import java.util.Properties;

import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.admin.IAdminMessage;
import com.dcfs.interfaces.esb.client.IServiceHandler;





public class MessageHandler implements IServiceHandler{

	
	public byte[] dispatch(String serviceName, byte[] msg, Properties appHeaders) {
		if (IAdminMessage.ADMIN_MESSAGE.equalsIgnoreCase(appHeaders
				.getProperty(IAdminMessage.MSG_TYPE))) {
			// if (log.isDebugEnabled()) {
			// log.debug("ESB接入容器调用管理监控命令处理");
			// }
			return ManagerFactory.getManager().process(msg, appHeaders);
		}
		return null;
	}

}
