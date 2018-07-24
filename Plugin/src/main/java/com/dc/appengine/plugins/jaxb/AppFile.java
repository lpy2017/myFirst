package com.dc.appengine.plugins.jaxb;

import javax.xml.bind.annotation.XmlElement;

public class AppFile {
	private String path;
	private Boolean isApendDeploy;
	private Boolean isNew;
	private Boolean isPatch;
	
	@XmlElement(name="path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@XmlElement(name="isApendDeploy")
	public Boolean getIsApendDeploy() {
		return isApendDeploy;
	}
	public void setIsApendDeploy(Boolean isApendDeploy) {
		this.isApendDeploy = isApendDeploy;
	}
	@XmlElement(name="isNew")
	public Boolean getIsNew() {
		return isNew;
	}
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}
	
	@XmlElement(name="isPatch")
	public Boolean getIsPatch() {
		return isPatch;
	}
	public void setIsPatch(Boolean isPatch) {
		this.isPatch = isPatch;
	}
	
	
}
