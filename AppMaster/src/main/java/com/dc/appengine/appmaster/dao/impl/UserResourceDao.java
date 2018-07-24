package com.dc.appengine.appmaster.dao.impl;


import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IUserResourceDao;
import com.dc.appengine.appmaster.entity.UserResource;

@Component("userResourceDao")
public class UserResourceDao implements IUserResourceDao{

	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	

	@Override
	public int updateUserResource(UserResource userResource) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("userResource.updateUserResource", userResource);
	}
	

	public UserResource getUserResourceByUId(long userId){

		return (UserResource) sqlSessionTemplate.selectOne("userResource.getURByUId", userId);
	}
}
