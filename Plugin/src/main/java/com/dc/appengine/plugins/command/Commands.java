/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dc.appengine.plugins.utils.ConfigHelper;

/**
 * Commands.java
 * 
 * @author liubingj
 */
public class Commands {

	private static final Commands INSTANCE;

	private final Map<String, String> commands = new ConcurrentHashMap<String, String>(
			16);

	static {
		INSTANCE = new Commands();
	}

	private Commands() {
		init();
	}

	private void init() {

	}

	public static Commands getInstance() {
		return INSTANCE;
	}

	public void put(String key, String value) {
		commands.put(key, value);
	}

	public String get(String key) {
		// ssh 执行脚本的时候 修改get获取方式
		String execMethod = ConfigHelper.getValue("shellExecutor");
		String command = key;
		if ("".equals(execMethod) || "shell".equals(execMethod)) {
			command = commands.get(key);
		} else if ("ssh".equals(execMethod)) {
			String basePath = ConfigHelper.getValue("hostBasePath");
			StringBuilder sb = new StringBuilder();
			sb.append(basePath);
			if (!basePath.endsWith("/")) {
				sb.append("/");
			}
			sb.append(key);
			command = sb.toString();
		}
		return command;
	}

	public String toString() {
		return "Current commands are " + commands.toString();
	}

}
