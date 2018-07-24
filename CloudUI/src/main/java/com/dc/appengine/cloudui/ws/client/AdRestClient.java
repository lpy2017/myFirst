package com.dc.appengine.cloudui.ws.client;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;


@Service("adRestClient")
public class AdRestClient {

	//运维人员列表
	public String listUsers() {
		return WSRestClient.getMasterWebTarget().path("admin/listUsers").request().get(String.class);
	}
	
	public String createUser(String username, String password ,String roleId, long pId,String userId) {
		Form form = new Form();
		form.param("username", username);
		form.param("password", password);
		form.param("roleId", roleId);
		form.param("pId", pId+"");
		form.param("userId", userId);
		String ret = WSRestClient.getMasterWebTarget().path("admin/createUser")
				.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		return ret;
	}
	
	//角色列表
	public String listRoles(String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/listRoles").
				queryParam("sortName", sortName).
				queryParam("sortOrder", sortOrder).
				request().get(String.class);
	}
	//创建角色
	public String createRole(String roleName) {
		// TODO Auto-generated method stub
		Form form = new Form();
		form.param("roleName", roleName);
		String ret = WSRestClient.getMasterWebTarget().path("admin/addRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		return ret;
	}
	//删除角色
	public String delRole(String roleId,String userId) {
		// TODO Auto-generated method stub
		Form form = new Form();
		form.param("roleId", roleId);
		form.param("userId", userId);
		return WSRestClient.getMasterWebTarget().path("admin/delRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//角色的权限列表
	public String policysOfRole(String roleId) {
		// TODO Auto-generated method stub
		Form form = new Form();
		form.param("roleId", roleId);
		return WSRestClient.getMasterWebTarget().path("admin/policysOfRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//取消角色的权限
	public String cancelPolicysOfRole(String roleId, String policyIds) {
		// TODO Auto-generated method stub
		Form form = new Form();
		form.param("roleId", roleId);
		form.param("policyIds", policyIds);
		return WSRestClient.getMasterWebTarget().path("admin/cancelPolicysOfRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//增加角色的权限
	public String addPolicysOfRole(String roleId, String policyIds, String menuIds,String operateUserId,String policyFlag){
		Form form = new Form();
		form.param("roleId", roleId);
		form.param("policyIds", policyIds);
		form.param("menuIds", menuIds);
		form.param("userId", operateUserId);
		form.param("policyFlag", policyFlag);
		return WSRestClient.getMasterWebTarget().path("admin/savePolicysOfRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//erji权限列表
	public String listPolicys(String sortName,String sortOrder){
		return WSRestClient.getMasterWebTarget().path("admin/getAllPolicyChilds").
				queryParam("sortName", sortName).
				queryParam("sortOrder", sortOrder).
				request().get(String.class);
	}
	//删除权限
	public String delPolicys(String policyIds){
		Form form = new Form();
		form.param("policyIds", policyIds);
		return WSRestClient.getMasterWebTarget().path("admin/delPolicys").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//添加权限
	public String createPolicy(String policyInfos){
		Form form = new Form();
		form.param("policyInfos", policyInfos);
		return WSRestClient.getMasterWebTarget().path("admin/createPolicy").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//修改权限
	public String modifyPolicy(String policyInfos){
		Form form = new Form();
		form.param("policyInfos", policyInfos);
		return WSRestClient.getMasterWebTarget().path("admin/modifyPolicy").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	//获取运维下属的所有用户
	public String usersOfCluster(String userId){
		return WSRestClient.getMasterWebTarget().path("admin/usersOfCluster").path(userId)
				.request().get(String.class);
	}
	//用户关联集群
	public String relationUser(String clusterId, String userId) {
		return WSRestClient.getMasterWebTarget().path("admin/relationUser").path(clusterId).path(userId)
				.request().get(String.class);
	}
	//查询用户的角色
	public String getRoleOfUser(String userId){
		Form form = new Form();
		form.param("userId", userId);
		return WSRestClient.getMasterWebTarget().path("admin/getRoleOfUser").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	//删除运维人员
	public String delUser(String userId) {
		// TODO Auto-generated method stub
		Form form = new Form();
		form.param("userId", userId);
		return WSRestClient.getMasterWebTarget().path("admin/delUser").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	public String getSonsOfUser(String userId) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/getSonsOfUser")
				.queryParam("userId", userId).request().get(String.class);
	}
	public String isExitUser(String userName) {
		// TODO Auto-generated method stub
		Form form= new Form();
		form.param("userName", userName);
		return WSRestClient.getMasterWebTarget().path("admin/isExitUser")
				.request().post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	public String isExitRole(String roleName) {
		// TODO Auto-generated method stub
		Form form= new Form();
		form.param("roleName", roleName);
		return WSRestClient.getMasterWebTarget().path("admin/isExitRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	public String getPolcysOfRole(int roleId) {
		// TODO Auto-generated method stub
		Form form= new Form();
		form.param("roleId", roleId+"");
		return WSRestClient.getMasterWebTarget().path("admin/getPolicysOfRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	public String savePolicysOfRole(int roleId, String policyIds) {
		// TODO Auto-generated method stub
		Form form= new Form();
		form.param("roleId", roleId+"");
		form.param("policyIds", policyIds);
		return WSRestClient.getMasterWebTarget().path("admin/savePolicysOfRole").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}

	public String userListByPage(int pageSize, int pageNum) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/userListByPage")
				.queryParam("pageSize", pageSize).queryParam("pageNum", pageNum).request()
				.get(String.class);
	}

	public String isAccess(String userId, String menuId) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/isAccess")
				.queryParam("userId", userId).queryParam("menuId", menuId).request()
				.get(String.class);
	}

	public String addPolicyMenu(String policyMenuInfo) {
		// TODO Auto-generated method stub
		Form form= new Form();
		form.param("policyMenuInfo", policyMenuInfo);
		return WSRestClient.getMasterWebTarget().path("admin/addPolicyMenu").request().
				post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}

	public String findMenuByPage(int pageSize, int pageNum) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/findMenuByPage")
				.queryParam("pageSize", pageSize).queryParam("pageNum", pageNum).request()
				.get(String.class);
	}

	public String findMenuByPageOfRole(int pageSize, int pageNum, String roleId) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin/findMenuByPageOfRole")
				.queryParam("pageSize", pageSize).queryParam("pageNum", pageNum)
				.queryParam("roleId", roleId).request()
				.get(String.class);
	}
	
}