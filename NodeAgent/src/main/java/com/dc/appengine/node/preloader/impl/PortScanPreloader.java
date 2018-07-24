/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.impl;

import com.dc.appengine.node.portscan.PortScanner;
import com.dc.appengine.node.preloader.Preloadable;
import com.dc.appengine.plugins.command.Analytic;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

/**
 * InitPreloader.java
 * @author liubingj
 */
public class PortScanPreloader implements Preloadable {

	private static PortScanPreloader _instance = new PortScanPreloader();
	
	public static PortScanPreloader getInstance() {
		return _instance;
	}
	/* (non-Javadoc)
	 * @see com.dc.appengine.Preloadable#preload()
	 */
	public void preload() throws Exception {
		Analytic<String> analyser =new DefaultAnalyser();
		CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
		String lxcDeportCommand = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("lxcunport"), false);
		executor.exec(lxcDeportCommand);
		PortScanner scanner = new PortScanner();
		scanner.process();
	}

	

}
