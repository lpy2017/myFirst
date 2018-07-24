/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

/**
 * Processor.java
 * @author liubingj
 */
public interface Processor {

	public < T, R > R process( T t ) throws Exception;

}
