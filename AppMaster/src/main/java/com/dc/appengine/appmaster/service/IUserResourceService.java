package com.dc.appengine.appmaster.service;

import com.dc.appengine.appmaster.entity.UserResource;

public interface IUserResourceService {
	
	//更新用户配额表_回收资源
	public String updateIncUserResource(long cpu, long disk, long memory , long userId);
	//更新用户配额表_使用资源
	public String updateDecUserResource(long cpu, long disk, long memory , long userId);
	
	//判断是否有可用配额
	String isAvailable(long cpu, long disk, long memory, long userId);
	
	public UserResource getUserResourceByUid(long userId);
	
}
