package com.dc.appengine.appmaster.entity;

import org.apache.ibatis.type.Alias;

@Alias("menuRole")
public class MenuRole {

	private String menuId;
	private String roleId;
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
}
