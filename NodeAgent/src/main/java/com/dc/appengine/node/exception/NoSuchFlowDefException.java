/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.exception;

/**
 * 无流程定义异常类
 * <p>
 * 当Node欲执行流程时，若当前并无对应名称的流程定义存在，则抛出此异常。
 * </p>
 * @author liubingj
 */
public class NoSuchFlowDefException extends RuntimeException {

	/**
	 * @param string
	 */
	public NoSuchFlowDefException( String message ) {
		super( message );
	}

	private static final long serialVersionUID = 1L;

}
