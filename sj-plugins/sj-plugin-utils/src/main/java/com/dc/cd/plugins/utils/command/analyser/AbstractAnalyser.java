/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.cd.plugins.utils.command.analyser;

import com.dc.cd.plugins.utils.command.Analytic;
import com.dc.cd.plugins.utils.command.Filter;

/**
 * AbstractAnalyser.java
 * @author liubingj
 */
public abstract class AbstractAnalyser< R > implements Analytic< R > {
	
	protected static final String SPLIT_REGEX = "\\s+";
	
	protected Filter filter;
	
	protected R result;

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.command.Analytic#getResult()
	 */
	public R getResult() {
		return result;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.command.Analytic#setFilterChain(com.dc.appengine.node.command.Filter)
	 */
	public void setFilter( Filter filter ) {
		this.filter = filter;
	}
	

}
