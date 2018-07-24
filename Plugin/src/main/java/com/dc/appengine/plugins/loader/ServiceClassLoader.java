package com.dc.appengine.plugins.loader;

import java.net.URLClassLoader;

public class ServiceClassLoader extends ClassLoader{
	private String classPath;
	private URLClassLoader ucl;

	ServiceClassLoader(ClassLoader cl) {
		super(cl);
	}
	
	ServiceClassLoader(ClassLoader cl, String classPath) {
		super(cl);
		this.classPath = classPath;
	}

	public synchronized Class<?> loadClass(String className)
	throws ClassNotFoundException{
		try{
			Class c = Class.forName(className);
			return c;
		}
		catch(ClassNotFoundException e){
			try {
				if (ucl == null)
					ucl = ClassLoaderFactory.getIndependentCL(this.classPath);
				return ucl.loadClass(className);
			}catch (Exception e2) {
				throw new ClassNotFoundException("Can not find class: " + className);
			}
//			return super.loadClass(className);
		}
	}

}
