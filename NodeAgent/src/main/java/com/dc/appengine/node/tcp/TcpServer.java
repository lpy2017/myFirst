package com.dc.appengine.node.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * <p></p>
 * @author bing.liu
 * Date 2012-7-20
 */
public class TcpServer implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger( TcpServer.class );

	private ServerSocket serverSocket;

	private ThreadPoolExecutor pool = ( ThreadPoolExecutor ) Executors.newFixedThreadPool( 
			30 );

//	private ThreadPoolExecutor pool = ( ThreadPoolExecutor ) Executors.newCachedThreadPool();
	
	public TcpServer() {
	}

	public void startup(final int port) {
		try {
			InetSocketAddress address = new InetSocketAddress( "0.0.0.0", port);
			serverSocket = new ServerSocket();
			serverSocket.bind( address, pool.getCorePoolSize() );
		} catch ( IOException e ) {
//			e.printStackTrace();
			LOG.error(e.getMessage(),e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while ( true ) {
			try {
				if(pool.isShutdown()){
					pool = ( ThreadPoolExecutor ) Executors.newFixedThreadPool( 
							30 );
				} 
				pool.execute( new SocketInvoker( serverSocket.accept() ) );
			} catch ( IOException e ) {
//				e.printStackTrace();
				LOG.error(serverSocket.getLocalPort()+":"+e.getMessage(),e);
			}
		}
	}

	public void shutdown() {
		try {
			if(serverSocket!=null)
			serverSocket.close();
		} catch ( IOException e ) {
			LOG.error( e.getMessage(), e );
		}
		pool.shutdown();
	}
	
}
