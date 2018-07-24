package com.dc.appengine.appmaster.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.springframework.beans.factory.annotation.Value;

public class RollBackExecutorPool {

	@Value(value = "${rollback.threadPoolSize}")
	private int threadPoolSize;

	private static RollBackExecutorPool pool;
	private transient ExecutorService services;

	private RollBackExecutorPool() {
		services = Executors.newFixedThreadPool(50, new ThreadFactory() {
			int count = 0;

			ThreadGroup group = new ThreadGroup("master_app_rollback");

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(group, r, Thread.currentThread().getName() + "rollback-thread-pool-" + (count++));
			}
		});
	}

	public static RollBackExecutorPool getInstance() {
		if (pool == null) {
			synchronized (RollBackExecutorPool.class) {
				if (pool == null) {
					pool = new RollBackExecutorPool();
				}
			}
		}
		return pool;
	}

	public ExecutorService getServices() {
		return services;
	}

	public <T> Future<T> submit(Callable<T> task) {
		return services.submit(task);
	}

	public Future<?> submit(Runnable task) {
		return services.submit(task);
	}

	public void execute(Runnable command) {
		services.execute(command);
	}
}
