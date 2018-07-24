/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance.snapshot;


/**
 * Consumer.java
 * @author liubingj
 */
public class Consumer {

	private static final Consumer INSTANCE = new Consumer();
	
	private Consumer() {
	}
	
	public static Consumer getInstance() {
		return INSTANCE;
	}
	
	public void put( SnapshotParam param ) {
		ExchangeChannel.getInstance().put( param );
	}

}
