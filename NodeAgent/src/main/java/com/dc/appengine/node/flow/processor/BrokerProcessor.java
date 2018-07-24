/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.processor;

import java.net.URI;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.flow.SimpleFlowEngine;
import com.dc.appengine.node.flow.model.BrokerDefinition;
import com.dc.appengine.node.flow.model.PropertyDefinition;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.IMXSDMessage;
import com.dcfs.interfaces.esb.admin.IManageComponent;

/**
 * BrokerProcessor.java
 * @author liubingj
 */
public class BrokerProcessor extends BeanUriProcessor< BrokerDefinition > {
	private static Logger LOG = LoggerFactory.getLogger( SimpleFlowEngine.class );
	/**
	 * @param definition
	 */
	public BrokerProcessor( BrokerDefinition definition ) {
		super( definition );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.processor.Processor#process(java.lang.Object)
	 */
	public < T, R > R process( T t ) throws Exception {
		if ( ! ( t instanceof Context ) ) {
			throw new IllegalArgumentException( "The argument must be an instance of " + Context.class.getName() );
		}
		Context context = ( Context ) t;
//		CommWorker worker = null;
//		final String id = getDefinition().getId();
//		if ( ConnectorInstancePool.getInstance().containsKey( id ) ) {
//			worker = ConnectorInstancePool.getInstance().get( id );
//			( ( QueueClientWorker ) worker ).setConfiguration( getDefinition() );
//		} else {
//			final URI uri = URI.create( getDefinition().getUri() );
//			super.parseUri( uri );
//			worker = new QueueClientWorker( getDefinition() );
//			worker.connect();
//		}
//		worker.send( context );
		IManageComponent manager = ManagerFactory.getManager();
		if( LOG.isDebugEnabled() ){
			LOG.debug("send msg"+new String(( byte[] )context.getPayload()));
		}
		List<PropertyDefinition> proplist =  getDefinition().getProperties();
		Properties header  = new Properties();
		for(PropertyDefinition prop:proplist){
			header.put(prop.getName(), prop.getValue());
		}
//		header.put(IMXSDMessage.FROM, null);
		header.put(IMXSDMessage.TIME_STAMP, String.valueOf(System.currentTimeMillis()));
//		String[] infos =  MsgRegistry.getInstance().get(context.getId());
//		if(infos!=null && infos.length>0){
//			header.setProperty(IMXSDMessage.DISTINCT_TO, infos[0]);
//		}
		byte[] recmsg = manager.process((byte[])context.getPayload(), header);
		
		context.setPayload(recmsg);

		return null;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.flow.processor.BeanUriProcessor#callback(java.lang.Object)
	 */
	protected < R > void callback( R r ) {
		final URI uri = ( URI ) r;
		String uriStr = uri.toString();
		getDefinition().setProviderUrl( uriStr.substring( 0, uriStr.indexOf( "?" ) ) );
		getDefinition().setQueueName( uri.getQuery() );
	}

}
