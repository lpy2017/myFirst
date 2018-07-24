package com.dc.appengine.appmaster.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.IRoleDao;
import com.dc.appengine.appmaster.dao.IUserDao;
import com.dc.appengine.appmaster.dao.IUserResourceDao;
import com.dc.appengine.appmaster.entity.UserResource;
import com.dc.appengine.appmaster.service.IUserResourceService;

@Service("userResourceService")
public class UserResourceService implements IUserResourceService{
	private static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	@Qualifier("userResourceDao")
	private IUserResourceDao userResourceDao;
	
	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	@Autowired
	@Qualifier("roleDao")
	private IRoleDao roleDao;
	
	//使用资源
	@Override
	public String updateDecUserResource(long cpu, long disk, long memory , long userId) {
		// TODO Auto-generated method stub
		
		String roleName = roleDao.getRoleName(userDao.getRoleOfUser(userId));
		if(roleName.equals("管理员") || roleName.equals("运维人员")){
			return "{\"result\":true}";
		}
		UserResource ur = userResourceDao.getUserResourceByUId(userId);
		if(ur != null){
			//需要的个数与已使用的和小于总配额 才能尽享分配
			boolean cpu_flag = ur.getCpu_used()+cpu > ur.getCpu();
			boolean disk_flag = ur.getDisk_used()+disk > ur.getDisk();
			boolean memory_flag = ur.getMemory_used()+memory > ur.getMemory();
			if(cpu_flag || disk_flag || memory_flag){
				return "{\"result\":false,\"message\":\"" + "该用户配额资源不足！" + "\"}";
			}else{
				ur.setCpu_used(ur.getCpu_used() + cpu);
				ur.setDisk_used(ur.getDisk_used() + disk);
				ur.setMemory_used(ur.getMemory_used() + memory);
				if(userResourceDao.updateUserResource(ur) > 0){
					return "{\"result\":true,\"message\":\"" + "更新成功" + "\"}";
				}
				return "{\"result\":false,\"message\":\"" + "更新失败" + "\"}";
			}
		}
		return "{\"result\":false,\"message\":\"" + "该用户无配额资源" + "\"}";
		
	}

	//回收资源
	@Override
	public String updateIncUserResource(long cpu, long disk, long memory , long userId) {
		// TODO Auto-generated method stub
		String roleName = roleDao.getRoleName(userDao.getRoleOfUser(userId));
		if(roleName.equals("管理员") || roleName.equals("运维人员")){
			return "{\"result\":true}";
		}
		UserResource ur = userResourceDao.getUserResourceByUId(userId);
		if(ur != null){
			if(ur.getCpu_used() < cpu){
				ur.setCpu_used(0);
			}else{
				ur.setCpu_used(ur.getCpu_used() - cpu);
			}
			
			if(ur.getDisk_used() < disk){
				ur.setDisk_used(0);
			}else{
				ur.setDisk_used(ur.getDisk_used() - disk);
			}
			
			if(ur.getMemory_used() < memory){
				ur.setMemory_used(0);
			}else{
				ur.setMemory_used(ur.getMemory_used() - memory);
			}
			if(userResourceDao.updateUserResource(ur) > 0){
				return "{\"result\":true,\"message\":\"" + "更新成功" + "\"}";
			}
			return "{\"result\":false,\"message\":\"" + "更新失败" + "\"}";
		}
		
		return "{\"result\":false,\"message\":\"" + "该用户无配额资源" + "\"}";
		
	}
	
	//判断是否有足够资源
	@Override
	public String isAvailable(long cpu, long disk, long memory , long userId) {
		// TODO Auto-generated method stub
		
		String roleName = roleDao.getRoleName(userDao.getRoleOfUser(userId));
		if(roleName.equals("管理员") || roleName.equals("运维人员")){
			return "{\"result\":true}";
		}
		//测试 开发
		UserResource ur = userResourceDao.getUserResourceByUId(userId);
		if(ur != null){
			boolean cpu_flag = ur.getCpu_used() + cpu > ur.getCpu();
			boolean disk_flag = ur.getDisk_used() + disk > ur.getDisk();
			boolean memory_flag = ur.getMemory_used() + memory > ur.getMemory();
			if(cpu_flag || disk_flag || memory_flag){
				return "{\"result\":false,\"message\":\"" + "该用户配额资源不足！" + "\"}";
			}
			return "{\"result\":true,\"message\":\"" + "该用户资源充足" + "\"}";
		}
		return "{\"result\":false,\"message\":\"" + "该用户无配额资源" + "\"}";
	}
	
	//查看用户配额对象
	public UserResource getUserResourceByUid(long userId){
		return userResourceDao.getUserResourceByUId(userId);
	}
}
