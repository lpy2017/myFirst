/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.dispatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.dispatcher.model.DispatcherDefinition;
import com.dc.appengine.node.dispatcher.model.OperationDefinition;
import com.dc.appengine.node.preloader.AbstractProcessor;

/**
 * 
 * @author liubingj
 */
public class DispatcherProcessor extends
		AbstractProcessor<DispatcherDefinition> {

	private static Logger LOG = LoggerFactory
			.getLogger(DispatcherProcessor.class);

	public DispatcherProcessor(DispatcherDefinition definition) {
		super(definition);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dc.appengine.configuration.processor.Processor#process(java.lang.
	 * Object)
	 */
	public <T, R> R process(T t) throws Exception {
		if (getDefinition() != null) {
			for (OperationDefinition operation : getDefinition()
					.getOperations()) {
				String clazz = operation.getClassPath();
				Object instance;
				try {
					instance = Class.forName(clazz).newInstance();
					if (Executable.class.isInstance(instance)) {
						DispatcherRegistry.getInstance().put(
								operation.getName().toString(),
								(Executable) instance);
					} else {
						LOG.error(clazz
								.concat(" is not an instance of Executable."));
					}
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					throw e;
				}
			}
		}
		return null;
	}

}
