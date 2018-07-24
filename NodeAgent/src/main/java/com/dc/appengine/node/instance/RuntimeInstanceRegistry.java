/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RuntimeContainerRegistry.java
 * 
 * @author liubingj
 */
public class RuntimeInstanceRegistry {

	private static final RuntimeInstanceRegistry _instance;

	static {
		_instance = new RuntimeInstanceRegistry();
	}

	private Map<String, InstanceModel> instances = new ConcurrentHashMap<String, InstanceModel>(
			16);

	private RuntimeInstanceRegistry() {
	}

	public static RuntimeInstanceRegistry getInstance() {
		return _instance;
	}

	public synchronized void put(String key, InstanceModel nativeExeUnit) {
		this.instances.put(key, nativeExeUnit);
	}

	public synchronized void putandcheck(String key, InstanceModel nativeExeUnit) {
		if (!this.instances.containsKey(key)) {
			this.instances.put(key, nativeExeUnit);
		}
	}

	public InstanceModel get(String key) {
		InstanceModel result = null;
		if (this.instances.containsKey(key)) {
			result = this.instances.get(key);
		} else {
			result = InstanceRegistry.getInstance().create(key);
		}
		return result;
	}

	public synchronized InstanceModel remove(String key) {
		InstanceModel result = null;
		if (this.instances.containsKey(key)) {
			result = this.instances.remove(key);
		}
		return result;
	}

	public int size() {
		return instances.size();
	}

	public Set<String> keySet() {
		return instances.keySet();
	}

	public boolean containsKey(String key) {
		return instances.containsKey(key);
	}

	public void clear() {
		instances.clear();
	}

}
