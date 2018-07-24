/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.collector;

/**
 * Collectable.java
 * @author liubingj
 */
public interface Collectable {
	
	public < T > T collect() throws Exception;

}
