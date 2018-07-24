package com.dc.appengine.appmaster.dao;

import java.util.List;

import com.dc.appengine.appmaster.entity.MenuRole;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.PolicyMenu;

/**
 * 处理按钮级别菜单
 * @author chenxiaod
 *
 */
public interface IPolicyMenuDao {

	public List<String> getMenuId(String roleId);
	
	public PolicyMenu getPolicyMenu(String menuId);

	public List<PolicyMenu> getAllPolicyMenus();

	public int deletePolicyMenu(int roleId);
	
	public Integer addPolicyMenu(PolicyMenu policyMenu);

	public int getCountOfMenu();

	public List<PolicyMenu> findMenuByPage(Page page);

}
