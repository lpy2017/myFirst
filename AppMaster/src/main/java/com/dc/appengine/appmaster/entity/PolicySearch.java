package com.dc.appengine.appmaster.entity;

import org.apache.ibatis.type.Alias;

@Alias("policySearch")
public class PolicySearch {

	private PolicyChild policyChild;
	private boolean isHave;
	private Policy policy;
	public PolicyChild getPolicyChild() {
		return policyChild;
	}
	public void setPolicyChild(PolicyChild policyChild) {
		this.policyChild = policyChild;
	}
	public boolean isHave() {
		return isHave;
	}
	public void setHave(boolean isHave) {
		this.isHave = isHave;
	}
	public Policy getPolicy() {
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
	
}
