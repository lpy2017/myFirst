/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scanner.impl;

import java.net.NetworkInterface;
import java.util.Enumeration;

import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.node.scanner.ResourceScanner;


/**
 * IpScanner.java
 * @author liubingj
 */
public class IpScanner implements ResourceScanner {

	/* (non-Javadoc)
	 * @see com.dc.appengine.framework.resource.ResourceScanner#scan(java.lang.Object, com.dc.appengine.framework.resource.ResourceFilter)
	 */
	public < R > void scan( R resource, ResourceFilter filter )
			throws Exception {
		final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces();
		
		while ( interfaces.hasMoreElements() ) {
			NetworkInterface ni = interfaces.nextElement();
			try {
				ni.isLoopback();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				continue;
			}
			filter.doFilter( ni );
		}
	}

}
