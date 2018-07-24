/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.portscan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.cache.PortsCache;
import com.dc.appengine.node.configuration.model.NodeProperties;

/**
 * PortScanner.java
 * @author liubingj
 */
public class PortScanner {
	
	private static Logger LOG = LoggerFactory.getLogger( PortScanner.class );

	private static final int worker_count = 10;
	
	private CyclicBarrier barrier;
	
	private List< Worker > workers;
	
	private ThreadPoolExecutor executor;

	public PortScanner() {
		this.workers = new ArrayList< Worker >( worker_count );
		this.executor = ( ThreadPoolExecutor ) Executors.newFixedThreadPool( 
				worker_count, new ThreadFactory() {
					ThreadGroup group = new ThreadGroup( "PortScanner-Group" );
					int count = 0;
					public Thread newThread( Runnable r ) {
						return new Thread( group, r, group.getName().concat( "-" + count++ ) );
					}
				} );
		this.barrier = new CyclicBarrier( worker_count, new Runnable() {
			public void run() {
				merge();
			}

			private void merge() {
				for ( Worker worker : workers ) {
					final boolean[] ports = worker.getPorts();
					final int index = worker.getStartPort() 
							- PortsCache.getInstance().getStart();
					for ( int i = 0; i < ports.length; i++ ) {
						PortsCache.getInstance().set( index + i, ports[ i ] );
					}
				}
				LOG.debug( "Clear the works list." );
				workers.clear();
				LOG.debug( "Shutdown the pool." );
				executor.shutdown();
			}
		} );
	}
	
	public void process() throws Exception {
//		final int groupCount = PortsCache.getInstance().getCount() / ( worker_count - 1 );
//		final int lastCount = PortsCache.getInstance().getCount() % ( worker_count - 1 );
//		
//		LOG.info( "Port scan beginning..." );
//		String nodeIp = NodeEnv.getInstance().getNodeEnvDefinition().getNodeip();
//		boolean isDocker = NodeProperties.isDocker();
//		
//		boolean isIsolate = false;
//		if(isDocker)
//			isIsolate = true;
//		//Set pre-group worker
//		for ( int i = 0; i < worker_count - 1; i++ ) {
//			Worker worker = new Worker( 
//					PortsCache.getInstance().getStart() + i * groupCount, 
//					groupCount, 
//					nodeIp,
//					isIsolate,
//					this.barrier );
//			workers.add( worker );
//			this.executor.execute( worker );
//		}
//		//Set last worker
//		Worker lastWorker = new Worker( 
//				PortsCache.getInstance().getStart() + ( worker_count - 1 ) * groupCount, 
//				lastCount, 
//				nodeIp,
//				isIsolate,
//				this.barrier );
//		workers.add( lastWorker );
//		this.executor.execute( lastWorker );
//		waitUtilDone();
//		LOG.info( "Port scan ended." );
	}

	/**
	 * 
	 */
	private void waitUtilDone() {
		while ( this.executor.getActiveCount() > 0 ) {
			continue;
		}
	}

}
