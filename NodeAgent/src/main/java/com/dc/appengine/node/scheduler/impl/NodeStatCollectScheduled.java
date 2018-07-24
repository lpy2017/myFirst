/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scheduler.impl;

import com.dc.appengine.node.collector.impl.NodeStatCollector;
import com.dc.appengine.node.scheduler.Schedulable;

public class NodeStatCollectScheduled implements Schedulable {

	private static NodeStatCollectScheduled _instance = new NodeStatCollectScheduled();

	public static NodeStatCollectScheduled getInstance() {
		return _instance;
	}

	@Override
	public void execute() {
//		NodeStatCollector nodeStatCollector = new NodeStatCollector();
//		nodeStatCollector.collect();
	}

}
