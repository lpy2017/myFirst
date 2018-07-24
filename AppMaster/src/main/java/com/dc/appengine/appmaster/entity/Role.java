package com.dc.appengine.appmaster.entity;

import org.apache.ibatis.type.Alias;

@Alias("role")
public class Role {
	
	//角色Id
	private String roleId;
	//角色名称
	private String name;
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
