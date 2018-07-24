package com.dc.appengine.adapter.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.dc.appengine.adapter.entity.ENV;


public class ExecutorServicePool {
	
	private static ExecutorServicePool pool;

	private transient ExecutorService services;
	
	private ExecutorServicePool(){
		int size = Integer.parseInt(ENV.THREAD_POOL_SIZE);
		services = Executors.newFixedThreadPool( size, new ThreadFactory() {
			private static final String name = "adapter-thread";
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		});
	}
	
	public static ExecutorServicePool getInstance(){
		if( pool == null ){
			synchronized( ExecutorServicePool.class ){
				if( pool == null ){
					pool = new ExecutorServicePool();
				}
			}
		}
		return pool;
	}


	public ExecutorService getServices() {
		return services;
	}
	
	public <T> Future<T> submit( Callable<T> task ){
		return services.submit( task );
	}
	
	public Future<?> submit( Runnable task ){
		return services.submit( task );
	}
	
	public void execute( Runnable command ){
		services.execute( command );
	}

}
