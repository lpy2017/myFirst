package com.dc.appengine.appmaster.service;

import java.util.List;

import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.Role;

public interface IRoleService {

	/**
	 * 角色列表
	 */
	public List<Role> listRoles(String sortName,String sortOrder);
	
	/**
	 * create角色
	 */
	public String createRole(String roleName);

	/**
	 * 分配权限
	 * @param roleId
	 * @param policyIds
	 * @return
	 */
	public String addPolicysOfRole(int roleId, String policyIds);

	/*
	 * 取消权限
	 */
	public String cancelPolicysOfRole(int roleId, String policyId);

	public List<Policy> policysOfRole(int roleId);

	public String getRoleName(int uuId);

	public int getRoleId(String roleId);

	public String delRole(String roleName);

	public String isExitRole(String roleName);
	
	public String savePolicysOfRole(int roleId , String policyIds, String menuIds,String policyFlag);
}
