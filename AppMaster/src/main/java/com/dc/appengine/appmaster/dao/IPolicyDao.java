package com.dc.appengine.appmaster.dao;

import java.util.List;

import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;

public interface IPolicyDao {

	
	public List<Policy> getAllPolicys(String sortName,String sortOrder);

	
	//新增规则，返回添加成功后的规则Id
	public Long addPolicy(String text, String icon, String sref, String title,
				String type ,int serialNum);

	//删除规则
	public long deleteOneByPolicyText(String page_id);

	//获取某角色下的所有规则
	public List<Policy> getAllPolicys(int roleId);

	//修改规则
	public int updatePolicy(Policy policyInfo);

	public int getRoleCountOfPolicy(String policyIds);

	public int getPolicyId(String policyIds);


	public List<PolicyChild> getPolicysChildsOfRole(int tempRoleId);


	public List<PolicyChild> getAllPolicyChilds();


	public Long addPolicyChild(String text, String icon, String sref,
			String type, int pId);


	public int getPolicyIdByText(String policytext);


	public int getRoleCountOfPolicyChild(String policyChildId);


	public void deleteOneByPolicyChild(String policyChildId);


	public int updatePolicyChild(PolicyChild pc);
	public int getChrildPolicyIdByText(String policytext);
	
	public List<String> getPolicyResourceList(int roleId);
	public List<String> getPChildResList(int roleId);
	public List<String> getMenuResList(int roleId);
	
	public String getTextByPolicyChild(String policyId);
}
