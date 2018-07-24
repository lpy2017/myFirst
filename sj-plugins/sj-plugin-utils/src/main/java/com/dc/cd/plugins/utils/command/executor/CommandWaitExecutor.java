/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.cd.plugins.utils.command.executor;

import java.io.IOException;

import com.dc.cd.plugins.utils.command.Analytic;


/**
 * CommandWaitExecutor.java
 * @author liubingj
 */
public class CommandWaitExecutor extends GenericCommandExecutor {

	/**
	 * @param analytic
	 */
	public CommandWaitExecutor( Analytic< ? > analytic ) {
		super( analytic );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.command.impl.GenericCommandExecutor#waitFor(java.lang.Process)
	 */
	protected int waitFor( Process process ) throws Exception {
		int result = 0;
		try {
			result = process.waitFor();
		} finally {
			if ( process != null ) {
				try {
					process.getErrorStream().close();
					process.getInputStream().close();
					process.getOutputStream().close();
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				process.destroy();
				process = null;
			}
		}
		return result;
	}

}
