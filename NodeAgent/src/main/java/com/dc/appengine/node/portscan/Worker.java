/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.portscan;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Worker.java
 * @author liubingj
 */
class Worker implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger( Worker.class );
	
	private int startPort;
	
	private int count;
	
	private boolean[] ports;
	
	private CyclicBarrier barrier;
	
	private String nodeIp;
	
	private boolean isIsolate;
	
	Worker( int startPort, int count, String nodeIp,boolean isIsolate,CyclicBarrier barrier ) {
		this.startPort = startPort;
		this.count = count;
		this.barrier = barrier;
		this.nodeIp = nodeIp;
		this.isIsolate = isIsolate;
		this.ports = new boolean[ count ];
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Socket socket = new Socket();
		boolean isUsed;
		String ip = "127.0.0.1";
		if(isIsolate)
			ip = nodeIp;
		for ( int i = 0; i < count; i++ ) {
			try {
//				socket = new Socket( ip, startPort + i );
				InetSocketAddress endpoint = new InetSocketAddress(ip, startPort + i );
				socket.connect(endpoint, 5000);
				isUsed = true;
				if ( LOG.isDebugEnabled() ) {
					LOG.debug( "The port [" + ( startPort + i ) + "] has been used." );
				}
			} catch ( Exception e ) {
				LOG.trace( e.getMessage(), e );
				isUsed = false;
			} finally {
				if ( socket != null ) {
					try {
						socket.close();
					} catch ( Exception e ) {
						LOG.error( e.getMessage(), e );
					}
				}
			} 
			
			if(!isUsed){
				ServerSocket server = null;
				try {
					server = new ServerSocket(startPort + i);
//					System.out.println("not used00");
				    isUsed=false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					if(e instanceof java.net.BindException) { 
						isUsed=true; 
						if ( LOG.isDebugEnabled() ) {
							LOG.debug( "The port [" + ( startPort + i ) + "] has been used." );
						}
		            }
				}finally{
					if(server!=null&&!server.isClosed()){
						try {
							server.close();
						} catch (IOException e) {
							 
						}
					}
				}
			}
			  
			ports[ i ] = isUsed;
		}
		
		try {
//			LOG.debug( "barrier await..." );
			barrier.await();
//			LOG.debug( "barrier await over." );
		} catch ( InterruptedException e ) {
			LOG.error( e.getMessage(), e );
		} catch ( BrokenBarrierException e ) {
			LOG.error( e.getMessage(), e );
		}
	}

	public int getStartPort() {
		return startPort;
	}

	public boolean[] getPorts() {
		return ports;
	}
	
}
