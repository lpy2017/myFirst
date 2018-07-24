package com.dc.appengine.node.exception;

public class IaasPropException extends Exception {
	private static final long serialVersionUID = 6765690422056572923L;
private String merrorCode;
	public IaasPropException(String ex,String errorCode) {
		super(ex);
		merrorCode = errorCode;
	}
	public String getMerrorCode() {
		return merrorCode;
	}
}
