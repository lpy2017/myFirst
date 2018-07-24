package com.dc.appengine.appmaster.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IPolicyMenuDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.PolicyMenu;

@Component("policyMenuDao")
public class PolicyMenuDao implements IPolicyMenuDao{

	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMenuId(String roleId) {
		// TODO Auto-generated method stub
		return  sqlSessionTemplate.selectList("getMenuId",roleId);
	}
	
	public PolicyMenu getPolicyMenu(String menuId){
		return (PolicyMenu) sqlSessionTemplate.selectOne("getPolicyMenu",menuId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyMenu> getAllPolicyMenus() {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("policyMenu.getAllPolicyMenu");
	}

	@Override
	public int deletePolicyMenu(int roleId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("policyMenu.deletPolicyMenuByRoleId", roleId+"");
	}

	@Override
	public Integer addPolicyMenu(PolicyMenu policyMenu) {
		// TODO Auto-generated method stub
		return (int) sqlSessionTemplate.insert("policyMenu.addPolicyMenu", policyMenu);
	}

	@Override
	public int getCountOfMenu() {
		// TODO Auto-generated method stub
		return (int) sqlSessionTemplate.selectOne("policyMenu.getCount");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyMenu> findMenuByPage(Page page) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("policyMenu.findMenuByPage",page);
	}

}
