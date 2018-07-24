/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.preloader.AbstractFilePreloader;
import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.plugins.command.Commands;

/**
 * CommandPreloader.java
 * 
 * @author liubingj
 */
public class CommandPreloader extends AbstractFilePreloader {

	// private static Logger LOG =
	// LoggerFactory.getLogger(CommandPreloader.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dc.appengine.Preloadable#preload()
	 */
	public void preload() throws Exception {
		final ResourceFilter rf = new CommandResourceFilter();
		super.process("commands", rf);
		List<File> files = rf.getResult();
		String name = null;
		for (File file : files) {
			int index = file.getName().lastIndexOf(".");
			name = file.getName().substring(0, index);
			Commands.getInstance().put(name, file.getAbsolutePath());
		}
		// if (LOG.isInfoEnabled()) {
		// LOG.info(Commands.getInstance().toString());
		// }
		synchronized (NodeEnv.olock) {
			NodeEnv.olock.notify();
		}
	}

	private class CommandResourceFilter implements ResourceFilter {

		private List<File> files = new ArrayList<File>(10);
		private String osType = System.getProperty("os.name").toLowerCase();

		public <T> void doFilter(T t) throws Exception {
			final File file = (File) t;

			if (osType.toLowerCase().contains("linux")) {
				if (file.getName().endsWith(".sh")) {
					files.add(file);
				}
			} else if (osType.contains("windows")) {
				if (file.getName().endsWith(".bat")) {
					files.add(file);
				}
			}
		}

		@SuppressWarnings("unchecked")
		public <T> T getResult() {
			return (T) files;
		}

	}

}
