package com.dc.appengine.appmaster.entity;

import java.util.List;

public class PolicyChild {

	private int id;
	private String text;
	private String icon;
	private String sref;
	private String type;
	private int parent_id;
	private boolean flag;
	private List<PolicyMenu> policyMenu;
	private String pText;
	
	
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSref() {
		return sref;
	}
	public void setSref(String sref) {
		this.sref = sref;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public List<PolicyMenu> getPolicyMenu() {
		return policyMenu;
	}
	public void setPolicyMenu(List<PolicyMenu> policyMenu) {
		this.policyMenu = policyMenu;
	}
	public String getpText() {
		return pText;
	}
	public void setpText(String pText) {
		this.pText = pText;
	}
	
	
	
}
