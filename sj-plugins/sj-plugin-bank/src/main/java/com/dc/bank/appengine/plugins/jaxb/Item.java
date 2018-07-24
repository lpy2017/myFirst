package com.dc.bank.appengine.plugins.jaxb;

import javax.xml.bind.annotation.XmlAttribute;

public class Item{
	String name;
	String isBackupData;
	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute(name="isBackupData")
	public String getIsBackupData() {
		return isBackupData;
	}

	public void setIsBackupData(String isBackupData) {
		this.isBackupData = isBackupData;
	}
	
}
