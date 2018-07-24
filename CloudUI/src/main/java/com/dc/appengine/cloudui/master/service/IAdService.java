package com.dc.appengine.cloudui.master.service;

public interface IAdService {

	public String listUsers();
	public String createUser(String username, String password, String roleId, long pId,String operateUserId);
	public String listRoles();

	public String createRole(String roleName) ;

	public String delRole(String roleName,String operateUserId);

	public String policysOfRole(String roleId);

	public String cancelPolicysOfRole(String roleId, String policyIds);
	
	public String addPolicysOfRole(String roleId, String policyIds,String menuIds,String operateUserId,String policyFlag);
	
	public String listPolicys(String sortName,String sortOrder);

	public String delPolicys(String policyIds);
	
	public String createPolicy(String policyInfos);
	
	public String modifyPolicy(String policyId);
	
	public String userOfCluster(String userId);
	public String deleUser(String userId);

	public String relationUser(String clusterId, String userId);

	public String getRoleOfUser(String userId) ;

	public String getSonsOfUser(String userId);
	
	public String isExitUser(String userName);
	
	public String isExitRole(String roleName);
	
	public String getPolicysOfRole(int roleId);
}