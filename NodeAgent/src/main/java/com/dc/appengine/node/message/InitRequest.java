/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.message;

import java.io.Serializable;

/**
 * 初始化请求对象
 * <p>
 * 封装组件初始化时向Master发送的相关信息
 * </p>
 * @author liubingj
 */
public class InitRequest implements Serializable {

	private static final long serialVersionUID = 7108324699122573296L;

	private Type type;
	
	private String[] ip;
	
	private String nodeName;
	
	public InitRequest() {
	}
	
	public InitRequest( Type type, String nodeName ) {
		this();
		this.type = type;
		this.nodeName = nodeName;
	}
	
	public InitRequest( Type type, String[] ips ) {
		this();
		this.type = type;
		this.ip = ips;
	}

	public Type getType() {
		return type;
	}

	public void setType( Type type ) {
		this.type = type;
	}

	public String[] getIp() {
		return ip;
	}

	public void setIp( String[] ip ) {
		this.ip = ip;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

}
