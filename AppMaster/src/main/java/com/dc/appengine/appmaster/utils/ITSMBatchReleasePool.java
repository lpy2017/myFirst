package com.dc.appengine.appmaster.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ITSMBatchReleasePool {

	private static ITSMBatchReleasePool pool;
	private transient ExecutorService services;

	private ITSMBatchReleasePool() {
		services = Executors.newFixedThreadPool(100, new ThreadFactory() {
			int count = 0;
			ThreadGroup group = new ThreadGroup("itsm_batch_release");

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(group, r, Thread.currentThread().getName() + "itsm-thread-pool-" + (count++));
			}
		});
	}

	public static ITSMBatchReleasePool getInstance() {
		if (pool == null) {
			synchronized (ITSMBatchReleasePool.class) {
				if (pool == null) {
					pool = new ITSMBatchReleasePool();
				}
			}
		}
		return pool;
	}

	public ExecutorService getServices() {
		return services;
	}

	public void execute(Runnable command) {
		services.execute(command);
	}
}
