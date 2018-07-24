/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.message;

import com.dc.appengine.node.configuration.Context;

/**
 * 消息处理器接口
 * <p>
 * 	定义组包assemble方法与解包disassemble方法。用于转换通讯报文与内部数据对象。
 * </p>
 * @author liubingj
 */
public interface MessageHandler {
	
	/**
	 * 组包方法，将内部数据对象组装成通讯报文
	 * @param context
	 * @throws Exception
	 */
	public void assemble( Context context ) throws Exception;
	
	/**
	 * 解包方法，将通讯报文拆解成内部数据对象
	 * @param context
	 * @throws Exception
	 */
	public void disassemble( Context context ) throws Exception;

}
