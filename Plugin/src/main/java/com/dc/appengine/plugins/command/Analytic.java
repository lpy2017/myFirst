/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.command;

import java.io.InputStream;

/**
 * Analytic.java
 * @author liubingj
 */
public interface Analytic< R > {

	public void analysis( InputStream is ) throws Exception;
	
	public void setFilter( Filter filter );
	
	public R getResult();

}
