package com.dc.appengine.plugins.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import com.dc.appengine.plugins.service.impl.PAASConfig;

public class DoAgentThreadPool {
	
	private static DoAgentThreadPool pool;

	private transient ExecutorService services;
	
	private DoAgentThreadPool(){
		int size = Integer.parseInt( PAASConfig.getConfig().getProperty("Thread.DoAgentPoolSize") );
		services = Executors.newFixedThreadPool( size, new ThreadFactory() {
			private static final String name = "doAgent-thread";
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		});
	}
	
	public static DoAgentThreadPool getInstance(){
		if( pool == null ){
			synchronized( DoAgentThreadPool.class ){
				if( pool == null ){
					pool = new DoAgentThreadPool();
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
