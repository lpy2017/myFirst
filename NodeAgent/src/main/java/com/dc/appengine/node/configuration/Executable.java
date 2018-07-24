/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration;


/**
 * 
 * @author liubingj
 */
public interface Executable {

	public < R > R execute( Context context ) throws Exception; 

}
