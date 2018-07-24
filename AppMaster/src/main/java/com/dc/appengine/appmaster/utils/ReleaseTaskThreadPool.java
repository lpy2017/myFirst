package com.dc.appengine.appmaster.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ReleaseTaskThreadPool {

	private static ReleaseTaskThreadPool instance;
	private transient ExecutorService service;

	public static ReleaseTaskThreadPool getInstance() {
		if (instance == null) {
			synchronized (ReleaseTaskThreadPool.class) {
				if (instance == null) {
					instance = new ReleaseTaskThreadPool();
				}
			}
		}
		return instance;
	}

	private ReleaseTaskThreadPool() {
		service = Executors.newFixedThreadPool(50, new ThreadFactory() {
			int index = 0;
			ThreadGroup group = new ThreadGroup("batch_release_task");

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(group, r, Thread.currentThread().getName() + "thread-pool- " + index++);
			}

		});
	}

	public ExecutorService getService() {
		return service;
	}

}
