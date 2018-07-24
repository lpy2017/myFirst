package com.dc.appengine.appmaster.entity;

import org.apache.ibatis.type.Alias;

@Alias("rolePolicyInfoMapping")
public class RolePolicyInfoMapping {

	private int role_id;
	private Policy policyInfo;
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public Policy getPolicyInfo() {
		return policyInfo;
	}
	public void setPolicyInfo(Policy policyInfo) {
		this.policyInfo = policyInfo;
	}
}
