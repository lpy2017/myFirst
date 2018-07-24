/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.dc.appengine.node.flow.processor.BrokerProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * BrokerDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "broker" )
public class BrokerDefinition extends BeanUriDefinition {
//	@XmlAttribute
//	private String contextFactory;
	@XmlTransient
	private String providerUrl;
	@XmlTransient
	private String queueName;

//	public String getContextFactory() {
//		if ( contextFactory == null ) {
//			this.contextFactory = NodeEnv.getInstance().getDefinition()
//					.getBroker().getContextFactory();
//		}
//		return contextFactory;
//	}
//
//	public void setContextFactory( String contextFactory ) {
//		this.contextFactory = contextFactory;
//	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl( String providerUrl ) {
		this.providerUrl = providerUrl;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName( String queueName ) {
		this.queueName = queueName;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.model.ProcessorDefinition#createProcessor()
	 */
	public Processor createProcessor() {
		return new BrokerProcessor( this );
	}
	
}
