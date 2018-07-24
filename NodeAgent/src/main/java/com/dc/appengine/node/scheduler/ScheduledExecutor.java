package com.dc.appengine.node.scheduler;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.ConfigReader;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.scheduler.model.JobDefinition;
import com.dc.appengine.node.scheduler.model.ScheduledDefinition;
import com.dc.appengine.node.utils.FileUtil;

public class ScheduledExecutor {

	private static Logger LOG = LoggerFactory.getLogger( ScheduledExecutor.class );
	
	private static ScheduledExecutor instance;

	private ScheduledThreadPoolExecutor threadPool;
	
	private ScheduledDefinition config;
	
	private static Map< String, ScheduledFuture< ? > > FUTURES = new ConcurrentHashMap< String, ScheduledFuture< ? > >();
	
	private ScheduledExecutor() {
		try {
			init();
		} catch ( Exception e ) {
			LOG.error( e.getMessage(), e );
		}
	}

	public void init() throws Exception {
		final File file = FileUtil.getInstance().getFile( "scheduled/scheduled.xml", Constants.Env.BASE_CONF );
		this.config = ConfigReader.getInstance().parseXmlToModel( file, 
						ScheduledDefinition.class.getPackage().getName(), ScheduledDefinition.class.getClassLoader() );
		
		final int jobsCount = config.getJobs().size();
		
		if ( jobsCount > 0 ) {
			this.threadPool = ( ScheduledThreadPoolExecutor ) Executors.newScheduledThreadPool( jobsCount, new ThreadFactory() {
				ThreadGroup group = new ThreadGroup( "Scheduled-Thread" );
				int count = 0;
				public Thread newThread( Runnable runnable ) {
					return new Thread( group, runnable, group.getName().concat( "-" + count++ ) );
				}
			} );
		} else {
			LOG.info( "No job has been defined." );
		}
	}
	
	public void execute() throws Exception {
		
		if(this.threadPool.isShutdown()){
			init();
		}
		
		for ( JobDefinition jobDef : config.getJobs() ) {
			if (FUTURES.containsKey(jobDef.getId() )) {
				continue;
			}
			JobExecutor jobExec = new JobExecutor( jobDef );
//			ScheduledFuture< ? > future = this.threadPool.scheduleAtFixedRate( jobExec, jobDef.getDelay(), 
//					jobDef.getPeriod(), TimeUnit.valueOf( jobDef.getUnit().name() ) );
			ScheduledFuture< ? > future = this.threadPool.scheduleWithFixedDelay( jobExec, jobDef.getDelay(), 
					jobDef.getPeriod(), TimeUnit.valueOf( jobDef.getUnit().name() ) );
			
			FUTURES.put( jobDef.getId(), future );
		}
	}
	
	public boolean cancel( String id ) {
		boolean isCancelled = false;
		if ( FUTURES.containsKey( id ) ) {
			isCancelled = FUTURES.remove( id ).cancel( false );
		}
		return isCancelled;
	}
	
	public void cancelAll() throws Exception {
		this.threadPool.shutdown();
		FUTURES.clear();
	}
	
	public static ScheduledExecutor getInstance() {
		synchronized( ScheduledExecutor.class ) {
			if ( instance == null ) {
				instance = new ScheduledExecutor();
			}
		}
		return instance;
	}

	public ScheduledThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool( ScheduledThreadPoolExecutor threadPool ) {
		this.threadPool = threadPool;
	}

	public ScheduledDefinition getConfig() {
		return config;
	}

	public void setConfig( ScheduledDefinition config ) {
		this.config = config;
	}

}
