/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Map;

import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.collector.impl.OsInfoCollector;
import com.dc.appengine.node.command.analyser.impl.OperatingSystemAnalyser;
import com.dc.appengine.node.preloader.Preloadable;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

/**
 * InitPreloader.java
 * 
 * @author liubingj
 */
public class OsInfoCollectPreloader implements Preloadable {

	private static OsInfoCollectPreloader _instance = new OsInfoCollectPreloader();

	public static OsInfoCollectPreloader getInstance() {
		return _instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dc.appengine.Preloadable#preload()
	 */
	public void preload() throws Exception {

		final OperatingSystemMXBean osmb = ManagementFactory
				.getOperatingSystemMXBean();
		NodeEnv.getInstance().setOs(osmb.getName());
//		String command_Os = "cat /etc/redhat-release";
//		OperatingSystemAnalyser osAnyic = new OperatingSystemAnalyser();
//		CommandWaitExecutor executor_os = new CommandWaitExecutor(osAnyic);
//		executor_os.exec(command_Os);
//		Map<String, Object> osInfo = osAnyic.getResult();
//		String os = (String) osInfo.get("all");
//		if (os != null) {
//			NodeEnv.getInstance().setOs(os);
//		}
//		new OsInfoCollector().collect();
	}

}
