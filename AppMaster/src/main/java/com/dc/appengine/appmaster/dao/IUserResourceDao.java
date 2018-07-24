package com.dc.appengine.appmaster.dao;

import com.dc.appengine.appmaster.entity.UserResource;

public interface IUserResourceDao {

	public int updateUserResource(UserResource userResource);
	
	public UserResource getUserResourceByUId(long userId);

}
