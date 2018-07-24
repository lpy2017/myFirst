package com.dc.itsm.appengine.plugins.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Filter{
	String id;
	Item item;
	@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@XmlElement(name="item")
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}	
}
