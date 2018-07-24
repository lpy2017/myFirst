/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.model.NodeProperties;

/**
 * PortsCache.java
 * 
 * @author liubingj
 */
public class PortsCache {
	private static Logger LOG = LoggerFactory.getLogger(PortsCache.class);

	private static final PortsCache instance = new PortsCache();

	private int count;

	private int start;

	private BitSet ports;

	private ReentrantLock lock = new ReentrantLock();

	private PortsCache() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.ports = new BitSet();
//		int startPort = NodeProperties
//				.getPortStart();
//		int endPort = NodeProperties.getPortEnd();
//		this.start = startPort;
//		this.count = endPort - start > 0 ? endPort - start : 0;
	}

	public static PortsCache getInstance() {
		return instance;
	}

	public void reinitports() {
//		int startPort = NodeProperties
//				.getPortStart();
//		int endPort = NodeProperties.getPortEnd();
//		this.start = startPort;
//		this.count = endPort - start > 0 ? endPort - start : 0;
	}

	public void set(int index, boolean isUsed) {
		this.lock.lock();
		try {
			this.ports.set(index, isUsed);
		} finally {
			this.lock.unlock();
		}
	}

	public void markport(int port) {
		this.lock.lock();
		try {
			int index = port - this.start;
			this.ports.set(index, true);
		} finally {
			this.lock.unlock();
		}
	}

	public void revertPort(int port) {
		this.lock.lock();
		try {
			int index = port - this.start;
			this.ports.set(index, false);
		} finally {
			this.lock.unlock();
		}
	}

	public boolean get(int index) {
		this.lock.lock();
		try {
			return this.ports.get(index);
		} finally {
			this.lock.unlock();
		}
	}

	public int setFirstUnused() {
		int index = -1;
		if (!checkFull()) {
			this.lock.lock();
			try {
				do {
					index = this.ports.nextClearBit(0);
				} while (checkPortState(index));
				ports.set(index, true);
			} finally {
				this.lock.unlock();
			}
		}
		return index;
	}

	/**
	 * @param index
	 * @return
	 */
	private boolean checkPortState(int index) {
		int currentPort = this.start + index;
		Socket socket = null;
		boolean isUsed = false;

		try {
			LOG.debug("# CHECK PORT:" + currentPort + "BEGIN");
			InetSocketAddress endpoint = new InetSocketAddress("127.0.0.1",
					currentPort);
			socket = new Socket();
			socket.connect(endpoint, 500);
			// socket = new Socket("127.0.0.1", currentPort);
			isUsed = true;
			LOG.debug("# CHECK PORT isUsed" + isUsed);
			set(index, isUsed);
		} catch (Exception e) {
			isUsed = false;
		} finally {
			LOG.debug("# CHECK PORT:" + currentPort + "ready");
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		if (isUsed == true) {
			LOG.debug("Port return1: Socket");
			return isUsed;
		}

		ServerSocket server = null;
		try {
			// server = new ServerSocket(currentPort);
			// 不停探测端口，bindException
			server = new ServerSocket();
			server.setReuseAddress(true);
			server.bind(new InetSocketAddress(currentPort));
			// System.out.println("not used00");
			isUsed = false;
		} catch (Exception e) {
			if (e instanceof java.net.BindException) {
				LOG.error("java.net.BindException " + e.getMessage());
				isUsed = true;
			}
		} finally {
			if (server != null && !server.isClosed()) {
				try {
					server.close();
				} catch (IOException e) {

				}
			}
		}
		LOG.debug("Port return2: ServerSocket");
		return isUsed;
	}

	public boolean checkFull() {
		this.lock.lock();
		try {
			return this.ports.cardinality() == this.count;
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
		return this.start + this.setFirstUnused();
	}

}
