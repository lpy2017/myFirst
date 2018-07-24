/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow.handler;

import java.net.URI;

/**
 * SchemeHandler.java
 * @author liubingj
 */
public interface SchemeHandler {

	public < R > R handle( URI uri ) throws Exception;

}
