package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("policyMenu")
public class PolicyMenu implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private String sref;
	private boolean menu_flag;
	
	public boolean getMenu_flag() {
		return menu_flag;
	}
	public void setMenu_flag(boolean menu_flag) {
		this.menu_flag = menu_flag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSref() {
		return sref;
	}
	public void setSref(String sref) {
		this.sref = sref;
	}
	
}
