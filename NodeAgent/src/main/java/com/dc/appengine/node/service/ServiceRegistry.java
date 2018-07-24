/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceRegistry.java
 * 
 * @author liubingj
 */
public class ServiceRegistry {

	private static volatile ServiceRegistry _instance;

	private Map<String, Object> services = new ConcurrentHashMap<String, Object>(
			16);

	private ServiceRegistry() {
	}

	public static ServiceRegistry getInstance() {
		if (_instance == null) {
			synchronized (ServiceRegistry.class) {
				_instance = new ServiceRegistry();
			}
		}
		return _instance;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) services.get(key);
	}

	public void put(String key, Object value) {
		services.put(key, value);
	}

	public boolean containsKey(String key) {
		return services.containsKey(key);
	}

}
