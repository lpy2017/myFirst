/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.message;

import java.io.Serializable;

/**
 * 内部消息类 <b>
 * <p>
 * 用于封装内部指令消息内容
 * </p>
 * 
 * @author liubingj
 */
public class InnerMessage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 操作
	 */
	private String op;

	/**
	 * 内容
	 */
	private T content;

	/**
	 * 默认构造函数
	 */
	public InnerMessage() {
		super();
	}

	/**
	 * 指定内容对象构造函数
	 * 
	 * @param content
	 */
	protected InnerMessage(T content) {
		super();
		this.content = content;
	}

	/**
	 * 指定操作类型与内容对象构造函数
	 * 
	 * @param op
	 * @param content
	 */
	public InnerMessage(String op, T content) {
		this.op = op;
		this.content = content;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	/**
	 * 根据传入的Class创建包含对应类型Content的InnerMessage对象
	 * 
	 * @param clazz
	 *            Content的class类型
	 * @return Content是Class类型对象的InnerMessage对象
	 * @throws Exception
	 */
	public static <T> InnerMessage<T> createInstance(Class<T> clazz)
			throws Exception {
		T t = clazz.newInstance();
		return new InnerMessage<T>(t);
	}

}
