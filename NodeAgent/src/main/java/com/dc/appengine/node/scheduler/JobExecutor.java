package com.dc.appengine.node.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.scheduler.model.JobDefinition;
import com.dc.appengine.node.scheduler.model.StepDefinition;

public class JobExecutor implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger( JobExecutor.class );

	private JobDefinition job;
	
	public JobExecutor( JobDefinition job ) {
		this.job = job;
	}

	public void run() {
		if ( LOG.isTraceEnabled() ) {
			LOG.trace( "The Job [" + job.getId() +"] beginning..." );
		}
		LOG.debug( "The Job [" + job.getId() +"] beginning..." );
		for ( StepDefinition step : job.getSteps() ) {
			try {
				Object obj = null;
				if ( step.isSingleton() ) {
					final String instanceId = job.getId().concat( ":" ).concat( step.getClazz() );
					if ( ! InstanceRegistry.getInstance().contains( instanceId ) ) {
						Class< ? > clazz = Class.forName( step.getClazz() );
						obj = clazz.newInstance();
						InstanceRegistry.getInstance().put( instanceId, obj );
					} else {
						obj = InstanceRegistry.getInstance().get( instanceId );
					}
					
				} else {
					Class< ? > clazz = Class.forName( step.getClazz() );
					obj = clazz.newInstance();
				}
				LOG.debug( "The step [" + step.getClazz() +"] beginning..." );
				obj.getClass().getMethod( step.getMethod(), new Class[]{} ).invoke( obj, new Object[]{} );
				LOG.debug( "The step [" + step.getClazz() +"] end..." );
			} catch ( Exception e ) {
				LOG.error( e.getMessage(), e );
			}
		}
		LOG.debug( "The Job [" + job.getId() +"] ended..." );
		if ( LOG.isTraceEnabled() ) {
			LOG.trace( "The Job [" + job.getId() +"] ended." );
		}
	}

}
