package com.dc.appengine.node.exception;

public class MasterPropException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String merrorCode;
	
	public MasterPropException(String ex,String errorCode){
		super(ex);
		merrorCode = errorCode;
	}
	
	public String getMerrorCode() {
		return merrorCode;
	}

}
