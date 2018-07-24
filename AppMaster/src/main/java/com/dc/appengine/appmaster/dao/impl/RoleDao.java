package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IRoleDao;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.Role;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.SortUtil;

import tk.mybatis.orderbyhelper.OrderByHelper;

@Component("roleDao")
public class RoleDao  implements IRoleDao {
	private static final Logger log = LoggerFactory.getLogger(Role.class);

	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> listRoles(String sortName,String sortOrder) {
		//排序
		if(!JudgeUtil.isEmpty(sortName)&&SortUtil.judgeSortOrder(sortOrder)){
			OrderByHelper.orderBy(sortName+" "+sortOrder);
		}
		return sqlSessionTemplate.selectList("role.listRoles");
	}

	@Override
	public int getRoleId(String name) {
		
		Long temp =(Long) sqlSessionTemplate.selectOne("role.getRoleId",name);
		if(temp==null){
			return 0;
		}else{
			return temp.intValue();
		}
	}
	

	@Override
	public String addRole(int roleId, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("name", name);
		sqlSessionTemplate.insert("role.addRole", map);
		return String.valueOf(map.get("roleId"));
	}

	public int getNewRoleId() {
		int newRoleId=(int)sqlSessionTemplate.selectOne(
				"role.getNewRoleId")+1;
		return newRoleId;
	}
	
	/* 
	 * 为某roleId增加一条已存在的policy
	 * FIXME 需先判断这条policy是否存在
	 */
	@Override
	public String addPolicyOfRole(int roleId, String policyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("pageId", policyId);
		sqlSessionTemplate.insert("role.addPolicyOfRole", map);
		return "true";
	}

	/* 
	 * 删除某roleId对应的某条Policy
	 * 仿PolicyDao的addPolicy方法
	 */
	@Override
	public String deletePolicyOfRole(int roleId, String policyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("policy", policyId);
		return Integer.toString(sqlSessionTemplate.delete("role.deletePolicyOfRole",map));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Policy> listPolicysOfRole(int roleId) {
		
		List<Integer> pageIdList=sqlSessionTemplate.selectList("role.getPageId",roleId);
		if(pageIdList != null && pageIdList.size() != 0){
			return sqlSessionTemplate.selectList("role.listPolicysOfRole",pageIdList);
		}
		return null;
	}

	@Override
	public String getRoleName(int roleId) {
		
		return (String) sqlSessionTemplate.selectOne("role.getRoleName",roleId);
	}

	public String deleteRole(String id) {
		int temp =Integer.parseInt(id);
		return Integer.toString(sqlSessionTemplate.delete("role.deletRole",temp));
	}

	@Override
	public boolean isExitRole(String name) {
		
		int temp=getRoleId(name);
		if(temp==0){
			return false;
		}else{
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getUserOfRole(String rId) {
		// TODO Auto-generated method stub
		int roleId = Integer.valueOf(rId);
		return (int)sqlSessionTemplate.selectOne("role.getUserOfRole",roleId);
	}

	@Override
	public void deletePolicyChildOfRole(int roleId, String pcId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("policyChildId", pcId);
		sqlSessionTemplate.delete("role.deletePolicyChildOfRole",map);
	}

	@Override
	public void addPolicyChildOfRole(int roleId, String policyChild) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("pcId", policyChild);
		sqlSessionTemplate.insert("role.addPolicyChildOfRole", map);
	}

	

	
	
}
