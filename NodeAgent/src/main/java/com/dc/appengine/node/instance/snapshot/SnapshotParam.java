/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance.snapshot;


/**
 * SnapshotParam.java
 * @author liubingj
 */
public class SnapshotParam {

	private Op op;
	
	private String name;
	
	public SnapshotParam( Op op, String name ) {
		this.op = op;
		this.name = name;
	}

	public Op getOp() {
		return op;
	}

	public void setOp( Op op ) {
		this.op = op;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

}
