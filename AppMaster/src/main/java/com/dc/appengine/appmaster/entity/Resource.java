package com.dc.appengine.appmaster.entity;

import java.util.List;

public class Resource {
	private String id;
	private String resourceName;
	private String resourceType;
	private String resourceDesc;
	private List<Version> versions;
	private String icon = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceDesc() {
		return resourceDesc;
	}
	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	public List<Version> getVersions() {
		return versions;
	}
	public void setVersions(List<Version> versions) {
		this.versions = versions;
	}
	public Resource(String id, String resourceName, String resourceType, String resourceDesc) {
		super();
		this.id = id;
		this.resourceName = resourceName;
		this.resourceType = resourceType;
		this.resourceDesc = resourceDesc;
	}
	public Resource() {
		
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
