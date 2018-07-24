/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.tcp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.dc.appengine.node.configuration.model.NodeProperties;

/**
 * SingleServerStarter.java
 * 
 * @author liubingj
 */
public class TcpServerStarter {

	private static final TcpServerStarter INSTANCE = new TcpServerStarter();

	private TcpServer server;

	private ExecutorService executor = Executors
			.newSingleThreadExecutor(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "TcpServerStarter-Thread");
				}
			});

	private TcpServerStarter() {
		server = new TcpServer();
	}

	public void startAlone() {
//		server.startup(NodeProperties.getHeartBeatPort());// NodeEnv.getInstance().getDefinition().getRepository().getHeartbeat_port());
//		if (executor.isShutdown()) {
//			executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
//				public Thread newThread(Runnable r) {
//					return new Thread(r, "TcpServerStarter-Thread");
//				}
//			});
//		}
//		executor.execute(server);
	}

	public void shutdown() {
		server.shutdown();
		executor.shutdown();
	}

	public static TcpServerStarter getInstance() {
		return INSTANCE;
	}

}
