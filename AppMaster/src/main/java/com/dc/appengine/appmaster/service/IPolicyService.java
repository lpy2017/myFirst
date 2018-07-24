package com.dc.appengine.appmaster.service;

import java.util.List;

import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;

public interface IPolicyService {
	
	public List<Policy> getAllPolicys(String sortName,String sortOrder);
	
	public String deletePolicy(String policyRes);
	
	public String createPolicy(String policyRes);

	public String modifyPolicy(String policyRes);

	public int getRoleCountOfPolicy(String policyIds);
	
	public String getPolicysOfRole(int roleId);
	
	public List<PolicyChild> getAllPolicyChilds(String sortName,String sortOrder);
	
	public String createPolicyChild(String policyInfos,String policytext);

	public int getRoleCountOfPolicyChild(String policyChildId, int pId);

	public Object deletePolicyChild(String policyChildId, int pId);

	public String modifyPolicyChild(String policyInfos);
	
	public String getRootPolicyOfRole(int roleId);
	
	public String getTextByPolicyChild(String policyId);
}
