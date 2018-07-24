/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.command.executor;

import com.dc.appengine.plugins.command.Analytic;


/**
 * CommandNoWaitExecutor.java
 * @author liubingj
 */
public class CommandNoWaitExecutor extends GenericCommandExecutor {

	/**
	 * @param analytic
	 */
	public CommandNoWaitExecutor( Analytic< ? > analytic ) {
		super( analytic );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.command.impl.GenericCommandExecutor#waitFor(java.lang.Process)
	 */
	protected int waitFor( Process process ) throws Exception {
		return 0;
	}

}
