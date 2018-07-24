package com.dc.itsm.appengine.plugins.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class SqlFile implements Comparable<SqlFile>{
	String seq;
	String path;
	Boolean isSendFull;
	@XmlAttribute(name="seq")
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	@XmlElement(name="path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@XmlElement(name="isSendFull")
	public Boolean getIsSendFull() {
		return isSendFull;
	}
	public void setIsSendFull(Boolean isSendFull) {
		this.isSendFull = isSendFull;
	}
	@Override
	public int compareTo(SqlFile o) {
		int i = Integer.valueOf(this.getSeq()) - Integer.valueOf(o.getSeq());//先按照年龄排序  
        return i;
	}
	
}
