package com.dc.appengine.plugins.context;

import com.dc.appengine.plugins.message.bean.InnerMessage;
import com.dcfs.interfaces.esb.IMXSDMessage;
import com.dcfs.interfaces.esb.admin.IAdminMessage;

public class ContextBuilder {
	
	@SuppressWarnings("unchecked")
	public static Context build(String toid, String msgType, String fromid, InnerMessage payload, String subType){
	
		Context context = new JMSContext();
		context.setAttribute(IAdminMessage.MSG_TYPE, msgType);//消息类型
		context.setAttribute(IMXSDMessage.DISTINCT_TO, toid);//目标地址
		context.setAttribute(IMXSDMessage.FROM, fromid);
		context.setAttribute(IMXSDMessage.TIME_STAMP, System.currentTimeMillis());
		context.setAttribute(IAdminMessage.SUB_TYPE, subType);
		context.setPayload(payload);
		
		return context;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Context build(String id, String toid, String msgType, String fromid, InnerMessage payload, String subType){
		Context context = null;
		if(id == null || "".equals(id)){
			context = new JMSContext();
		} else {
			context = new JMSContext(id);
		}		
		context.setAttribute(IAdminMessage.MSG_TYPE, msgType);//消息类型
		context.setAttribute(IMXSDMessage.DISTINCT_TO, toid);//目标地址
		context.setAttribute(IMXSDMessage.FROM, fromid);
		context.setAttribute(IMXSDMessage.TIME_STAMP, System.currentTimeMillis());
		context.setAttribute(IAdminMessage.SUB_TYPE, subType);
		context.setPayload(payload);
		
		return context;
		
	}
	
}
