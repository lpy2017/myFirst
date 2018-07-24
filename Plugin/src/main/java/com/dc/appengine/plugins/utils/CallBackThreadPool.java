package com.dc.appengine.plugins.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.dc.appengine.plugins.service.impl.PAASConfig;

public class CallBackThreadPool {
	
	private static CallBackThreadPool pool;

	private transient ExecutorService services;
	
	private CallBackThreadPool(){
		int size = Integer.parseInt( PAASConfig.getConfig().getProperty("Thread.callBackPoolSize") );
		services = Executors.newFixedThreadPool( size, new ThreadFactory() {
			private static final String name = "callback-thread";
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		});
	}
	
	public static CallBackThreadPool getInstance(){
		if( pool == null ){
			synchronized( CallBackThreadPool.class ){
				if( pool == null ){
					pool = new CallBackThreadPool();
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
