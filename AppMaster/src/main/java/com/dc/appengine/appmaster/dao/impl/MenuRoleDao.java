package com.dc.appengine.appmaster.dao.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IMenuRoleDao;
import com.dc.appengine.appmaster.entity.MenuRole;

@Component("menuRoleDao")
public class MenuRoleDao implements IMenuRoleDao{

	@Resource
	SqlSessionTemplate sqlSessionTemplate;


	@Override
	public void savePolicyMenuOfRole(MenuRole menuRole) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.insert("savePolicyMenuOfRole",menuRole);
	}
}
