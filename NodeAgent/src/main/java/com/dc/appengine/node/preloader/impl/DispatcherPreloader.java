/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.ConfigReader;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.dispatcher.DispatcherRegistry;
import com.dc.appengine.node.dispatcher.model.DispatcherDefinition;
import com.dc.appengine.node.dispatcher.model.OperationDefinition;
import com.dc.appengine.node.message.JsonHandler;
import com.dc.appengine.node.preloader.Preloadable;
import com.dc.appengine.node.service.DispatchService;
import com.dc.appengine.node.service.ServiceRegistry;
import com.dc.appengine.node.utils.FileUtil;

/**
 * DispatcherPreloadImpl.java
 * 
 * @author liubingj
 */
public class DispatcherPreloader implements Preloadable {

	private static Logger log = LoggerFactory
			.getLogger(DispatcherPreloader.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dc.appengine.Preloadable#preload()
	 */
	public void preload() throws Exception {
		ServiceRegistry.getInstance().put("dispatcher", new DispatchService());
		ServiceRegistry.getInstance().put("jsonHandler", new JsonHandler());
		final StringBuffer package_info = new StringBuffer(
				DispatcherDefinition.class.getPackage().getName());
//		package_info.append(":").append(OpEnum.class.getPackage().getName());
		final File file = FileUtil.getInstance().getFile(
				"dispatcher/dispatchers.xml", Constants.Env.BASE_CONF);
		DispatcherDefinition definition = ConfigReader.getInstance()
				.parseXmlToModel(file, package_info.toString(),
						DispatcherDefinition.class.getClassLoader());

		// 直接解析dispacher.xml文件
		if (definition != null) {
			for (OperationDefinition operation : definition.getOperations()) {
				String clazz = operation.getClassPath();
				Object instance;
				try {
					instance = Class.forName(clazz).newInstance();
					if (Executable.class.isInstance(instance)) {
						DispatcherRegistry.getInstance().put(
								operation.getName().toString(),
								(Executable) instance);
					} else {
						log.error(clazz
								.concat(" is not an instance of Executable."));
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw e;
				}
			}
		}
		// definition.createProcessor().process( null );
	}

}
