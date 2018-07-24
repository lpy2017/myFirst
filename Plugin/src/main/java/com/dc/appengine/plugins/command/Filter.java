/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.command;


/**
 * Filter.java
 * @author liubingj
 */
public abstract class Filter {

	protected String cond;
	
	public Filter _next;
	
	protected Filter( String cond ) {
		this.cond = cond;
	}
	
	protected Filter getNext() {
		return _next;
	}

	public void setNext( Filter _next ) {
		this._next = _next;
	}

	public abstract boolean doFilter( String src ) throws Exception;

}
