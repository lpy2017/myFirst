/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.processor;

import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.exception.NoMethodDeclaredException;
import com.dc.appengine.node.exception.NoSuchServiceException;
import com.dc.appengine.node.flow.model.BeanDefinition;
import com.dc.appengine.node.preloader.AbstractProcessor;
import com.dc.appengine.node.service.ServiceRegistry;

/**
 * BeanProcessor.java
 * 
 * @author liubingj
 */
public class BeanProcessor extends AbstractProcessor<BeanDefinition> {

	/**
	 * @param definition
	 */
	public BeanProcessor(BeanDefinition definition) {
		super(definition);
	}

	public <T, R> R process(T t) throws Exception {
		Object obj = null;
		if (ServiceRegistry.getInstance().containsKey(getDefinition().getId())) {
			obj = ServiceRegistry.getInstance().get(getDefinition().getId());
		} else if (getDefinition().getClazz() != null
				&& !"".equals(getDefinition().getClazz())) {
			Class<?> clazz = Class.forName(getDefinition().getClazz());
			obj = clazz.newInstance();
			if (getDefinition().isSingleton()) {
				ServiceRegistry.getInstance().put(getDefinition().getId(), obj);
			}
		} else {
			throw new NoSuchServiceException(
					"No instance or definition of service named "
							+ getDefinition().getId());
		}
		String methodName = null;
		if (getDefinition().getMethod() != null
				&& !"".equals(getDefinition().getMethod())) {
			methodName = getDefinition().getMethod();
		} else if (obj.getClass().getDeclaredMethods().length == 1) {
			methodName = obj.getClass().getDeclaredMethods()[0].getName();
		} else {
			throw new NoMethodDeclaredException("No method declared in the uri");
		}

		obj.getClass().getDeclaredMethod(methodName, Context.class)
				.invoke(obj, t);
		return null;
	}

}
