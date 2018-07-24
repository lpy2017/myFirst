package com.dc.appengine.cloudui.master.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.cloudui.master.service.IAdService;
import com.dc.appengine.cloudui.ws.client.AdRestClient;

@Service("adService")
public class AdService extends BaseService implements IAdService{

	private static final Logger log = LoggerFactory.getLogger(AdService.class);
	
	@Autowired
	@Qualifier("adRestClient")
	private AdRestClient adRestClient;
	
	public String listUsers() {
		// TODO Auto-generated method stub
		return adRestClient.listUsers();
	}
	
	public String createUser(String username, String password, String roleId ,long pId,String operateUserId) {
		// TODO Auto-generated method stub
		return adRestClient.createUser(username, password, roleId, pId,operateUserId);
	}

	public String listRoles(String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		return adRestClient.listRoles(sortName,sortOrder);
	}

	public String createRole(String roleName) {
		// TODO Auto-generated method stub
		return adRestClient.createRole(roleName);
	}

	public String delRole(String roleName,String operateUserId) {
		// TODO Auto-generated method stub
		return adRestClient.delRole(roleName,operateUserId);
	}

	public String policysOfRole(String roleId) {
		// TODO Auto-generated method stub
		return adRestClient.policysOfRole(roleId);
	}

	public String cancelPolicysOfRole(String roleId, String policyIds) {
		// TODO Auto-generated method stub
		return adRestClient.cancelPolicysOfRole(roleId, policyIds);
	}
	
	public String addPolicysOfRole(String roleId, String policyIds, String menuIds,String operateUserid,String policyFlag) {
		// TODO Auto-generated method stub
		return adRestClient.addPolicysOfRole(roleId, policyIds,menuIds,operateUserid,policyFlag);
	}
	
	public String listPolicys(String sortName,String sortOrder){
		return adRestClient.listPolicys(sortName,sortOrder);
	}

	public String delPolicys(String policyIds){
		return adRestClient.delPolicys(policyIds);
	}
	
	public String createPolicy(String policyInfos){
		return adRestClient.createPolicy(policyInfos);
	}
	
	public String modifyPolicy(String policyId){
		return adRestClient.modifyPolicy(policyId);
	}
	
	public String userOfCluster(String userId){
		return adRestClient.usersOfCluster(userId);
	}
	public String deleUser(String userId) {
		// TODO Auto-generated method stub
		return adRestClient.delUser(userId);
	}

	public String relationUser(String clusterId, String userId) {
		// TODO Auto-generated method stub
		return adRestClient.relationUser(clusterId, userId);
	}

	public String getRoleOfUser(String userId) {
		// TODO Auto-generated method stub
		return adRestClient.getRoleOfUser(userId);
	}

	public String getSonsOfUser(String userId) {
		// TODO Auto-generated method stub
		return adRestClient.getSonsOfUser(userId);
	}

	public String isExitUser(String userName) {
		// TODO Auto-generated method stub
		return adRestClient.isExitUser(userName);
	}

	public String isExitRole(String roleName) {
		// TODO Auto-generated method stub
		return adRestClient.isExitRole(roleName);
	}

	public String getPolicysOfRole(int roleId) {
		// TODO Auto-generated method stub
		return adRestClient.getPolcysOfRole(roleId);
	}

	public String savePolicysOfRole(int roleId, String policyIds) {
		// TODO Auto-generated method stub
		return adRestClient.savePolicysOfRole(roleId, policyIds);
	}

	public String userListByPage(int pageSize, int pageNum) {
		// TODO Auto-generated method stub
		return adRestClient.userListByPage(pageSize,pageNum);
	}

	@Override
	public String listRoles() {
		// TODO Auto-generated method stub
		return null;
	}

}