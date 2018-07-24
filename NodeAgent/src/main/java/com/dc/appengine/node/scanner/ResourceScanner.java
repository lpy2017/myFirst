/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scanner;


/**
 * ResourceScanner.java
 * @author liubingj
 */
public interface ResourceScanner {

	public < R > void scan( R resource, ResourceFilter filter ) throws Exception;
	
}
