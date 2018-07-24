package com.dc.appengine.appmaster.dao;

import java.util.List;

import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.Role;

public interface IRoleDao {

	
	//列出所有Role
	public List<Role> listRoles(String sortName,String sortOrder);

	//根据Role name返回roleId
	public int getRoleId(String name);
		
	//添加一条新的角色，roleId重复则添加失败
	public String addRole(int roleId,String name);

	//为某roleId增加一条已存在的policy
	public String addPolicyOfRole(int roleId, String policyId);

	//删除某roleId对应的某条Policy
	public String deletePolicyOfRole(int roleId, String policyId);

	/**
		 * 列出某roleId的所有Policy
		 * @param roleId
		 * @return
	 */
	public List<Policy> listPolicysOfRole(int roleId);

	/**
	 * 根据某roleId获取对应的RoleName
	 * @param roleId
	 * @return
	 */
	public String getRoleName(int roleId);
	
	
	
	/**
	 * 根据id删除某一个Role
	 * @param name
	 * @return
	 */
	public String deleteRole(String id);
	
	
	/**
	 * 根据name判断用户是否存在
	 * @param name
	 * @return
	 */
	public boolean isExitRole(String name);
	
	public int getNewRoleId();

	public int getUserOfRole(String roleName);

	public void deletePolicyChildOfRole(int roleId, String string);

	public void addPolicyChildOfRole(int roleId, String policyChild);
	
}
