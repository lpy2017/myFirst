package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.User;

public interface IUserDao {

	/**
	 * 登陆时查询用户信息
	 * @param param
	 * @return
	 */
	public User login(Map<String, String> param);

	/**
	 * 获取用户id
	 * @param userName
	 * @return
	 */
	public long getUserId(String userName);

	/**
	 * 获取所有用户
	 * @return
	 */
	public List<String> getAllUsers();

	/**
	 * 获取用户名
	 * @param id
	 * @return
	 */
	public String getUserName(long id);


	/**
	 * 创建新用户
	 * @param userName
	 * @param password
	 * @return
	 */
	public long add(String userName, String password);

	/**
	 * 创建新用户并指定角色
	 * @param userName 待创建用户名
	 * @param password 待创建密码
	 * @param roleId 待创建的角色
	 * @param parentId 待创建用户的parent
	 * @return
	 */
	public long add(String userName, String password ,int roleId,long parentId);
	
	
	/**
	 * 检查用户名是否存在，存在返回true
	 * @param userName
	 * @return
	 */
	public boolean existsUserName(String userName);
	
	/**
	 * 列出用户信息
	 * @param userId
	 * */
	public User getUserInfo(long userId);

	
	/**
	 * 获得用户的角色
	 * @param userId
	 * @return
	 */
	public int getRoleOfUser(long userId);
	
	/**
	 * 删除用户
	 * @param id
	 * @return
	 */
	public long delUser(long id);

	public List<Long> getUsersOfFather(long userId);

	public Map<Long, String> getUserMaps(List<Long> userIds);

	public int getUserAccount();

	public List<String> getUsersByPage(Page page);

	public int countAppUser(Map<String, String> params);

	public Page getAllAppUser(int pageSize, int pageNum,JSONObject params);

	public int updateUser(Map<String, Object> params);
	public List<Map<String,Object>> getUsers(Map<String, Object> params);
	Map<String, Object> getUserInfoByName(String name);
	int setNotNew(String name);
}