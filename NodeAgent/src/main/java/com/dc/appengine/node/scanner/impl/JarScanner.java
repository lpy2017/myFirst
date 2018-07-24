/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scanner.impl;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.node.scanner.ResourceScanner;


/**
 * JarScanner.java
 * @author liubingj
 */
public class JarScanner implements ResourceScanner {

	/* (non-Javadoc)
	 * @see com.dc.appengine.framework.resource.ResourceScanner#scan(java.lang.Object, com.dc.appengine.framework.resource.ResourceFilter)
	 */
	public < R > void scan( R resource, ResourceFilter filter )
			throws Exception {
		if ( ! ( resource instanceof JarFile ) ) {
			throw new IllegalArgumentException( "The resource must be an instance of java.util.jar.JarFile." );
		}
		final JarFile jar = ( JarFile ) resource;
		final Enumeration< JarEntry > enume = jar.entries();
		JarEntry entry = null;
		for ( ; enume.hasMoreElements(); ) {
			entry = enume.nextElement();
			filter.doFilter( entry );
		}
	}

}
