package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.dao.IPolicyMenuDao;
import com.dc.appengine.appmaster.dao.IUserDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.PolicyMenu;
import com.dc.appengine.appmaster.service.IPolicyMenuService;

@Service("policyMenuService")
public class PolicyMenuService implements IPolicyMenuService{

	@Autowired
	@Qualifier("policyMenuDao")
	private IPolicyMenuDao policyMenuDao;

	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	//是否拥有访问权限
	@Override
	public String isAccess(String userId, String menuId) {
		// TODO Auto-generated method stub
		int roleId = userDao.getRoleOfUser(Long.parseLong(userId));
		List<String> menus = policyMenuDao.getMenuId(roleId+"");
		if(menus != null && menus.size() !=0){
			for(String mId : menus){
				if(mId.equals(menuId)){
					return "{\"result\":true,\"message\":\"" + "拥有此权限" + "\"}";
				}
			}
		}
		return "{\"result\":false,\"message\":\"" + "您没有此权限！" + "\"}";
	}
	
	//添加按钮菜单
	public String addPolicyMenu(String policyMenuInfo){
		PolicyMenu pm = JSON.parseObject(policyMenuInfo, PolicyMenu.class);
		Integer temp = policyMenuDao.addPolicyMenu(pm);
		if(temp != null){
			return "{\"result\":true,\"message\":\"" + "创建成功" + "\"}";
		}
		return "{\"result\":false,\"message\":\"" + "创建失败！" + "\"}";
	}

	//分页查询按钮列表
	@Override
	public String findMenuByPage(int pageSize, int pageNum) {
		// TODO Auto-generated method stub
		int count = policyMenuDao.getCountOfMenu();
		Page page = new Page(pageSize, count);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		List<PolicyMenu> pmList = policyMenuDao.findMenuByPage(page);
		return JSON.toJSONString(pmList);
	}
	
	//分页查询角色对应的按钮列表()
	public String findMenuByPageOfRole(int pageSize, int pageNum, String roleId){
		int count = policyMenuDao.getCountOfMenu();
		Page page = new Page(pageSize, count);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		List<PolicyMenu> pmList = policyMenuDao.findMenuByPage(page);
		List<String> pmRole = policyMenuDao.getMenuId(roleId);
		List<PolicyMenu> result = new ArrayList<>();
		for(PolicyMenu pm : pmList){
			for(String mId : pmRole){
				if(mId.equals(pm.getId())){
					pm.setMenu_flag(true);
				}
			}
			result.add(pm);
		}
		return JSON.toJSONString(result);
	}
}
