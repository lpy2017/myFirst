package com.dc.appengine.appmaster.entity;

public class VersionFtl {
	public String id;
	public String resourceVersionId;
	public String ftlName;
	public String ftlText;
	public String templates;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getResourceVersionId() {
		return resourceVersionId;
	}
	public void setResourceVersionId(String resourceVersionId) {
		this.resourceVersionId = resourceVersionId;
	}
	public String getFtlName() {
		return ftlName;
	}
	public void setFtlName(String ftlName) {
		this.ftlName = ftlName;
	}
	public String getFtlText() {
		return ftlText;
	}
	public void setFtlText(String ftlText) {
		this.ftlText = ftlText;
	}
	public String getTemplates() {
		return templates;
	}
	public void setTemplates(String templates) {
		this.templates = templates;
	}
	
	public VersionFtl(){
		
	}
	public VersionFtl(String id,String resourceVersionId,String templates,String ftlName,String ftlText){
		super();
		this.id = id ; 
		this.resourceVersionId = resourceVersionId;
		this.templates = templates;
		this.ftlName = ftlName;
		this.ftlText = ftlText;
	}
		
}
