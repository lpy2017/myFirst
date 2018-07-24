package com.dc.appengine.node.exception;

@SuppressWarnings("serial")
public class DockerContainerException extends Exception {
	private String merrorCode;

	public DockerContainerException(String ex, String errorCode) {
		super(ex);
		merrorCode = errorCode;
	}

	public String getErrorCode() {
		return merrorCode;
	}
}
