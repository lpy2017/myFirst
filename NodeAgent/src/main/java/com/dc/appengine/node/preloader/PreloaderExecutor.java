/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.ConfigReader;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.Executable;
import com.dc.appengine.node.preloader.model.PreloadersDefinition;
import com.dc.appengine.node.utils.FileUtil;

/**
 * PreloaderExecutor.java
 * @author liubingj
 */
public class PreloaderExecutor implements Executable {

	private static Logger LOG = LoggerFactory.getLogger( PreloaderExecutor.class );

	private static PreloaderExecutor instance;
	
	private PreloadersDefinition definition;
	
	private PreloaderExecutor() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		File file;
		try {
			file = FileUtil.getInstance().getFile( "preloader/preloaders.xml", Constants.Env.BASE_CONF );
			this.definition = ConfigReader.getInstance().parseXmlToModel( file, 
					PreloadersDefinition.class.getPackage().getName(), PreloadersDefinition.class.getClassLoader() );
		} catch ( Exception e ) {
			LOG.error( e.getMessage(), e );
		}
	}
	
	public static PreloaderExecutor getInstance() {
		synchronized ( PreloaderExecutor.class ) {
			if ( instance == null ) {
				instance = new PreloaderExecutor();
			}
		}
		return instance;
	}
	
	public < Null > Null execute( Context context ) throws Exception {
		this.definition.createProcessor().process( null );
		return null;
	}

	/** (non-Javadoc)
	 * @see com.dc.appengine.node.configuration.Executable#executor(com.dc.appengine.node.configuration.Context)
	 */
	public < R > R executor( Context context ) throws Exception {
		return null;
	}

}
