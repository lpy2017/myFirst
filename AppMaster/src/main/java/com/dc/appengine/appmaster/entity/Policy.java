package com.dc.appengine.appmaster.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;
@Alias("policy")
public class Policy {
	private int page_id;
	private String text;
	private String icon;
	private String sref;
	private String title;
	private String type;
	private int serialNum;
	private List<PolicyMenu> policyMenu;
	private List<PolicyChild> policyChild;
	
	
	
	public List<PolicyMenu> getPolicyMenu() {
		return policyMenu;
	}
	public void setPolicyMenu(List<PolicyMenu> policyMenu) {
		this.policyMenu = policyMenu;
	}
	public int getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}
	public int getPage_id() {
		return page_id;
	}
	public void setPage_id(int page_id) {
		this.page_id = page_id;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<PolicyChild> getPolicyChild() {
		return policyChild;
	}
	public void setPolicyChild(List<PolicyChild> policyChild) {
		this.policyChild = policyChild;
	}
	
	
	
}
