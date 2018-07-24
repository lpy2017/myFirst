/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance.snapshot;


/**
 * Provider.java
 * @author liubingj
 */
public class Provider {
	
	private static final Provider INSTANCE = new Provider();
	
	private Provider() {
	}
	
	public static Provider getInstance() {
		return INSTANCE;
	}

	public SnapshotParam get() throws Exception {
		return ExchangeChannel.getInstance().get();
	}

}
