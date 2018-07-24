package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

public interface IMessageService {
	public List<Object> messages(List<String> instancelist,String currentOp);
	//封装from snapshot的消息
	public List<Object> messagesFromSnapShot(List<Map<String,Object>> instancelist,String currentOp);
	//封装for rollback的消息
    public List<Object> messagesForCurrentInstances(List<Map<String,Object>> instancelist,String currentOp);
}
