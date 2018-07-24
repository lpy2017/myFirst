package com.dc.scriptUtils.appengine.plugins.service.impl.tail;    
  
/** 
 * LogTail侦听 
 * @author youling 
 * 
 */  
public interface TailNotify {  
  
    /** 
     * LogTail有日志滚动事件产生后调用此方法 
     * @return 
     */  
    public void notifyMsg(String msg);  
      
}  