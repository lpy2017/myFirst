/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scanner;

/**
 * ResourceFilter.java
 * @author liubingj
 */
public interface ResourceFilter {

	public < T > void doFilter( T t ) throws Exception;
	
	public < T > T getResult();

}
