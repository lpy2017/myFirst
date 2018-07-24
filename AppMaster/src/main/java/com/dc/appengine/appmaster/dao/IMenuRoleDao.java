package com.dc.appengine.appmaster.dao;

import java.util.List;

import com.dc.appengine.appmaster.entity.MenuRole;
import com.dc.appengine.appmaster.entity.PolicyMenu;

/**
 * 处理按钮级别菜单
 * @author chenxiaod
 *
 */
public interface IMenuRoleDao {

	public void savePolicyMenuOfRole(MenuRole menuRole);
}
