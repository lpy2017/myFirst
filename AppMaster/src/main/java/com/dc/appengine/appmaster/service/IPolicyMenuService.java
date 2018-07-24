package com.dc.appengine.appmaster.service;

public interface IPolicyMenuService {

	public String isAccess(String userId, String menuId);
	
	public String addPolicyMenu(String policyMenuInfo);

	public String findMenuByPage(int pageSize, int pageNum);
	
	//分页查询角色对应的按钮列表()
	public String findMenuByPageOfRole(int pageSize, int pageNum, String roleId);
}
