/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liubingj
 */
public class InstanceRegistry {

	private static final InstanceRegistry _instance = new InstanceRegistry();

	private Map<String, Class<? extends InstanceModel>> ims = new ConcurrentHashMap<String, Class<? extends InstanceModel>>(
			16);

	private InstanceRegistry() {
	}

	public static InstanceRegistry getInstance() {
		return _instance;
	}

	public void put(String key, Class<? extends InstanceModel> clazz) {
		this.ims.put(key, clazz);
	}

	public InstanceModel create(String key) {
		InstanceModel result = null;
		Class<? extends InstanceModel> clazz = null;
		if (ims.containsKey(key)) {
			clazz = ims.get(key);
		} else {
			clazz = DefaultInstance.class;
		}
		try {
			result = clazz.getConstructor(String.class).newInstance(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String toString() {
		return ims.toString();
	}

}
