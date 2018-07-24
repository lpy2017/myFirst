package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;


import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.User;

public interface IUserService {

	public String login(String userName, String pwd);

	public long getUserId(String userName);

	public boolean authenticate(String userName, String password);

	public String getUserName(long id);

	//public boolean hasLoginPermission(String userName, String resource,String action);

	//public String isPaasAdmin(long userId);

	public String add(String userName, String password, int roleId, long parentId);
	
	public User getUserInfo(long userId);
	
	public List<String> listUsers();

	public int getUserRole(long userId);

	public String isExitUser(String userName);
	
	public List<Policy> getPoliciesOfUser(String userName);

	public String delUser(String userName);

	public List<Long> getUsersOfFatherId(long userId);

	public Map<Long,String> getUserMaps(List<Long> userIds);
	
	public String getSonsOfUser(long userId);

	public String isAdmin(String userId);
	
	public User getUserInfoByName(String userName);

	public String getAllAppUser(int pageSize, int pageNum, String key,String sortName,String sortOrder,String role);
	
	public String getResources(long userId);
	
	public String updateUser(Map<String,Object> params);

	public String delUserById(String userId);
	
	public String checkUserName(String userName,String userId);
	
	Map<String, Object> getUserMapByName(String name);
	
	int setNotNew(String name);

}