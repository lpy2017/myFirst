package com.dc.appengine.appsvn.utils.structurecheck;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckerDefinition {
	@XmlAttribute(name="name")
	private String name;
	@XmlAttribute(name="type")
	private String type;
	@XmlAttribute(name="filetype")
	private String filetype;
	 
	@XmlAttribute(name="op")
	private String op;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
}
