package com.dc.scriptUtils.appengine.plugins.service.impl.tail;

import com.dc.scriptUtils.appengine.plugins.utils.SystemUtil;

/** 
 * tail通知实现 
 * @author youling 
 * 
 */  
public class TailNotifyImpl implements TailNotify {  
	StringBuilder sb=new StringBuilder();
	
    @Override  
    public void notifyMsg(String msg) {  
        if (msg != null && !"".equals(msg)) {  
//            System.out.println(msg);  
            sb.append(msg).append(SystemUtil.getLineSeparator());
        }  
    }  
    public String getResult(){
    	return sb.toString();
    }
}  