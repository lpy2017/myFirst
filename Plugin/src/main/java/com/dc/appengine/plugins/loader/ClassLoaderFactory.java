package com.dc.appengine.plugins.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ClassLoaderFactory {
	private static final String shareLib = System.getenv("localpackage") + File.separator + "pluginClass";
	
	/**
	 * Get a new ServiceClassLoader
	 * @param classPath
	 * @return
	 */
	public static ServiceClassLoader getClassloader(String classPath) {
		return new ServiceClassLoader(Thread.currentThread().getContextClassLoader(), classPath);
	}
	
	/**
	 * Get Independent ClassLoader
	 * @param classPath
	 * @return
	 */
	public static URLClassLoader getIndependentCL(String classPath) {
		File cp = null;
		if (classPath != null && !"".equals(classPath)) {
			cp = new File(classPath);
		}
		
		File shared = new File(shareLib);
		try {
			List<URL> list = URLCollector.collect(shared);
			URL[] urls  = new URL[list.size() + (cp != null && cp.exists() ? 1 : 0)];
			
			list.toArray(urls);
			if (urls.length > list.size()) {
				urls[urls.length - 1] = cp.toURL();
			}
			
			return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader(), null);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new URLClassLoader(null); //never comes here
	}
	
}

