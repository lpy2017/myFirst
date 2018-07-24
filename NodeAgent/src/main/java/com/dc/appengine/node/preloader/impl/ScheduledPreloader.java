/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.impl;

import com.dc.appengine.node.cache.StateCache;
import com.dc.appengine.node.preloader.Preloadable;
import com.dc.appengine.node.scheduler.ScheduledExecutor;

/**
 * ScheduledPreloader.java
 * @author liubingj
 */
public class ScheduledPreloader implements Preloadable {

	private static ScheduledPreloader _instance = new ScheduledPreloader();
	
	public static ScheduledPreloader getInstance() {
		return _instance;
	}
	
	/* (non-Javadoc)
	 * @see com.dc.appengine.Preloadable#preload()
	 */
	public void preload() throws Exception {
		ScheduledExecutor.getInstance().execute();
	}
	
	public void cancel(){
		try {
			ScheduledExecutor.getInstance().cancelAll();
			StateCache.getInstance().clear();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
