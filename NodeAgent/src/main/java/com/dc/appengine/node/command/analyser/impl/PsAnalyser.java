/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

/**
 * PsAnalyser.java
 * @author liubingj
 */
public class PsAnalyser extends AbstractAnalyser< String[] > {
//	private static Logger LOG = LoggerFactory.getLogger(PsAnalyser.class);
	/* (non-Javadoc)
	 * @see com.dc.appengine.node.command.Analytic#analysis(java.io.InputStream)
	 */
	public void analysis( InputStream input ) throws Exception {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader( new InputStreamReader( input ) );
			String line = null ;
//			LOG.info("line bigin");
			while ( ( line = reader.readLine() ) != null ) {
//				LOG.info("psline:"+line);
				if ( this.filter.doFilter( line ) ) {
					break;
				}
			}
			
			if ( line != null ) {
				this.result = line.trim().split( SPLIT_REGEX );
			}
		} finally {
			if ( reader != null ) {
				reader.close();
				reader = null;
			}
		}
	}

}
