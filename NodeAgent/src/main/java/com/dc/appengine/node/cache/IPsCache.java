/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.cache;

import java.util.BitSet;
import java.util.concurrent.locks.ReentrantLock;

import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.configuration.model.NodeProperties;

/**
 * PortsCache.java
 * 
 * @author liubingj
 */
public class IPsCache {

	private static final IPsCache instance = new IPsCache();

	private int count;

	private int start;

	private BitSet ips;

	private ReentrantLock lock = new ReentrantLock();

	private IPsCache() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.ips = new BitSet();

//		if (NodeEnv.getInstance().getNodeEnvDefinition() != null) {
//
//			int endip = NodeProperties.getIpEnd();
//			this.start = NodeProperties
//					.getIpStart();
//			this.count = endip - start > 0 ? endip - start : 0;
//		}
	}

	public static IPsCache getInstance() {
		return instance;
	}

	public void reset() {
		if (NodeEnv.getInstance().getNodeEnvDefinition() != null) {

//			int endip = NodeProperties.getIpEnd();
//			this.start = NodeProperties
//					.getIpStart();
//			this.count = endip - start > 0 ? endip - start : 0;
		}
	}

	public void set(int index, boolean isUsed) {
		this.lock.lock();
		try {
			this.ips.set(index, isUsed);
		} finally {
			this.lock.unlock();
		}
	}

	public void revertIP(String ip) {
		this.lock.lock();
		try {
			int ipnum = Integer.parseInt(ip.substring(ip.lastIndexOf(".") + 1));
			int index = ipnum - this.start;
			this.ips.set(index, false);
		} finally {
			this.lock.unlock();
		}
	}

	public void markIP(String ip) {
		this.lock.lock();
		try {
			int ipnum = Integer.parseInt(ip.substring(ip.lastIndexOf(".") + 1));
			int index = ipnum - this.start;
			this.ips.set(index, true);
		} finally {
			this.lock.unlock();
		}
	}

	public boolean get(int index) {
		this.lock.lock();
		try {
			return this.ips.get(index);
		} finally {
			this.lock.unlock();
		}
	}

	public int setFirstUnused() {
		int index = -1;
		if (!checkFull()) {
			this.lock.lock();
			try {
				index = this.ips.nextClearBit(0);
				ips.set(index, true);
			} finally {
				this.lock.unlock();
			}
		}
		return index;
	}

	public boolean checkFull() {
		this.lock.lock();
		try {
			return this.ips.cardinality() == this.count;
		} finally {
			this.lock.unlock();
		}
	}

	public int getCount() {
		return count;
	}

	public int getStart() {
		return start;
	}

	public int getFirstUnusedPort() {
		int index = this.setFirstUnused();
		if (index < 0)
			return this.start;
		else
			return this.start + index;
	}

	// 设置一级路由对应的ip；参数：需要创建的外部ip的个数
	public String setIp(int ipNum) {
		this.lock.lock();
		String IPs = ""; // 所有待创建的ip
//		try {
//			int partIp; // ip第四段
//			String ipHead = NodeProperties
//					.getIpHead(); // ip前三段172.0.0.
//
//			for (int i = 0; i < ipNum; i++) {
//				partIp = IPsCache.getInstance().getFirstUnusedPort();
//				IPs += "," + ipHead + partIp;
//			}
//		} finally {
//			this.lock.unlock();
//		}
		return IPs.substring(1);
	}
}
