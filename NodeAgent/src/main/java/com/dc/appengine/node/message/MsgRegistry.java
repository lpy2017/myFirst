package com.dc.appengine.node.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MsgRegistry {

	private static final MsgRegistry _instance;

	private Map<String, String[]> msgFromInfo = new ConcurrentHashMap<String, String[]>(16);

	static {
		_instance = new MsgRegistry();
	}

	private MsgRegistry() {
	}

	public static MsgRegistry getInstance() {
		return _instance;
	}

	public String[] get(String strKey) {
		return msgFromInfo.get(strKey);
	}

	public void put(String strKey, String[] infos) {
		msgFromInfo.put(strKey, infos);
	}

	public void remove(String strKey) {
		msgFromInfo.remove(strKey);
	}

}