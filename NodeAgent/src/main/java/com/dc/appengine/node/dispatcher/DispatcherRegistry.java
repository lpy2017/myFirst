/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.dispatcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dc.appengine.node.configuration.Executable;

/**
 * 
 * @author liubingj
 */
public class DispatcherRegistry {

	private static DispatcherRegistry instance = new DispatcherRegistry();

	private Map<String, Executable> registry;

	private DispatcherRegistry() {
		registry = new ConcurrentHashMap<String, Executable>(16);
	}

	public static DispatcherRegistry getInstance() {
		return instance;
	}

	public void put(String key, Executable executor) {
		if (!registry.containsKey(key)) {
			this.registry.put(key, executor);
		} else {

		}
	}

	public boolean remove(String key) {
		if (this.registry.containsKey(key)) {
			this.registry.remove(key);
			return true;
		}
		return false;
	}

	public Executable get(String key) {
		return this.registry.get(key);
	}

	public boolean containsKey(String key) {
		return this.registry.containsKey(key);
	}

}
