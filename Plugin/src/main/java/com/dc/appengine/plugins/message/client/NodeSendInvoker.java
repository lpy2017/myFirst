package com.dc.appengine.plugins.message.client;

import java.util.Properties;

import com.dc.appengine.plugins.message.callback.Callback;
import com.dc.appengine.plugins.utils.CallBackThreadPool;
import com.dcfs.interfaces.esb.AdminServiceException;
import com.dcfs.interfaces.esb.admin.ICompInvoker;

public class NodeSendInvoker implements ICompInvoker{
	
	@Override
	public byte[] invoke(byte[] bytes, Properties properties) throws AdminServiceException {

		CallBackThreadPool.getInstance().execute(new Callback(bytes));//线程池执行回调
		return null;
	}

}
