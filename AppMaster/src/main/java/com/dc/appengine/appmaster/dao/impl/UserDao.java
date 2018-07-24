package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.IUserDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.User;



@Component("userDao")
public class UserDao  extends PageQuery implements IUserDao {
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	
	/* 
	 * 
	 */
	public User login(Map<String, String> param) {
		return (User) sqlSessionTemplate.selectOne("user.login", param);
	}

	@Override
	public long getUserId(String userName) {
		return (Long) sqlSessionTemplate.selectOne("user.getUserId", userName);
	}

	@Override
	public List<String> getAllUsers() {
		return sqlSessionTemplate.selectList("user.getAllUsers");
	}


	@Override
	public String getUserName(long id) {
		return (String) sqlSessionTemplate.selectOne("user.getUserName", id);
	}

	@Override
	public long add(String userName, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("password", password);
		sqlSessionTemplate.insert("user.add", map);
		return (Long)map.get("id");
	}

	@Override
	public boolean existsUserName(String userName) {
		return (Integer) sqlSessionTemplate.selectOne("user.existsUserName", userName) > 0;
	}

	@Override
	public User getUserInfo(long userId) {
		Object a =  sqlSessionTemplate.selectOne("user.getUserInfo", userId);
		if(a==null){
			return null;
		}
		return (User)a;
	}

	/** 
	 * 
	 * @return 
	 */
	@Override
	public long add(String userName, String password, int roleId,long parentId) {
		
		long newUserId;
		
		//向accounts_info表插入数据
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("userName", userName);
		map1.put("password", password);
		sqlSessionTemplate.insert("user.add",map1);
		newUserId=(Long)map1.get("id");
		//向user_role表插入数据
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("userId", newUserId);
		map2.put("roleId", roleId);
		sqlSessionTemplate.insert("user.addUserRole",map2);
		//向user_relationship插入数据
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("userId", newUserId);
		map3.put("parentId", parentId);
		sqlSessionTemplate.insert("user.addUserRelationship",map3);
		return  newUserId;
	}

	 
	@Override
	public int getRoleOfUser(long userId) {
		return (int) sqlSessionTemplate.selectOne("user.getRoleOfUser", userId);
	}

	@Override
	public long delUser(long id) {
		
		//accounts_info表中删除
		sqlSessionTemplate.delete("user.deleteAccountsInfoItem", id);
		//user_role表中删除
		sqlSessionTemplate.delete("user.deleteUserRoleItem",id);
		//向user_relationship插入数据
//		sqlSessionTemplate.delete("user.deleteUserRelationshipItem",id);
		
		return id;
	}

	@Override
	public List<Long> getUsersOfFather(long userId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("user.getUsersOfFather", userId);
	}

	@Override
	public Map<Long, String> getUserMaps(List<Long> userIds) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> s = sqlSessionTemplate.selectList("user.getUserMaps",userIds);
		Map<Long,String> map = new HashMap<>();
		for(Map<String,Object> m : s ){
			map.put((long)m.get("id"), m.get("userid").toString());
		}
		return map;
	}
	
	//运维人员总人数
		@Override
		public int getUserAccount() {
			// TODO Auto-generated method stub
			return (int) sqlSessionTemplate.selectOne("user.numOfUser");
		}
		
		//分页查询运维人员名字列表
		@SuppressWarnings("unchecked")
		public List<String> getUsersByPage(Page page){
			return sqlSessionTemplate.selectList("user.getUsersByPage", page);
		}

		@Override
		public int countAppUser(Map<String, String> params) {
			// TODO Auto-generated method stub
			return (int) sqlSessionTemplate.selectOne("user.countAppUser", params);
		}

		@Override
		public Page getAllAppUser(int pageSize, int pageNum, JSONObject params) {
			// TODO Auto-generated method stub
			return pageQuery(params, pageNum, pageSize,"user.getAllAppUsers");
		}

		@Override
		public int updateUser(Map<String, Object> params) {
			// TODO Auto-generated method stub
			return sqlSessionTemplate.update("user.updateUser", params);
		}

		@Override
		public List<Map<String, Object>> getUsers(Map<String, Object> params) {
			// TODO Auto-generated method stub
			return sqlSessionTemplate.selectList("user.getUsers", params);
		}

		@Override
		public Map<String, Object> getUserInfoByName(String name) {
			return sqlSessionTemplate.selectOne("user.getUserInfoByName", name);
		}

		@Override
		public int setNotNew(String name) {
			return sqlSessionTemplate.update("user.setNotNew", name);
		}
}
