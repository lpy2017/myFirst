/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

import java.io.File;

import org.slf4j.Logger;

import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.node.scanner.impl.FileScanner;
import com.dc.appengine.node.utils.FileUtil;

/**
 * AbstractFilePreloader.java
 * @author liubingj
 */
public abstract class AbstractFilePreloader implements Preloadable {

	protected Logger log;
	
	protected void process( String rootName, ResourceFilter filter ) throws Exception {
		final File root = FileUtil.getInstance().getFile( rootName,
				Constants.Env.BASE_CONF );
		final FileScanner scanner = new FileScanner();
		scanner.scan( root, filter );
	}

}
