package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class ReleaseIntegration {
	private int id;
	private String name;
	private String type;
	private String systemId;
	private String description;
	private String url;
	private String user;
	private String password;
	private String creater;
	private Date createTime;
	private Date updateTime;
	private Date synTime;

	private String systemName;

	public ReleaseIntegration() {

	};

	public ReleaseIntegration(String name, String type, String systemId, String description, String url, String user,
			String password, String creater) {
		this.name = name;
		this.type = type;
		this.systemId = systemId;
		this.description = description;
		this.url = url;
		this.user = user;
		this.password = password;
		this.creater = creater;
	}

	public ReleaseIntegration(int integrationId, String name, String type, String systemId, String description,
			String url, String user, String password) {
		this.id = integrationId;
		this.name = name;
		this.type = type;
		this.systemId = systemId;
		this.description = description;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getSynTime() {
		return synTime;
	}

	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
