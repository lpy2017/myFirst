package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IPolicyDao;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.SortUtil;

import tk.mybatis.orderbyhelper.OrderByHelper;


@Component("policyDao")
public class PolicyDao implements IPolicyDao{

	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	/* 
	 * 2016年9月28日09:43:21 
	 */
	@Override
	public Long addPolicy(String text,String icon,String sref,String title,String type,int serialNum) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("text", text);
		map.put("icon", icon);
		map.put("sref", sref);
		map.put("title", title);
		map.put("type", type);
		map.put("serialNum",serialNum);
		sqlSessionTemplate.insert("policy.addPolicy", map);
		return (Long)map.get("id");
	}

	/* 
	 * 2016年9月28日10:11:17
	 */
	@Override
	public long deleteOneByPolicyText(String id) {
		return sqlSessionTemplate.delete("policy.deleteOneByPolicyText",id);
	}


	@Override
	public int updatePolicy(Policy policyInfo) {
		return sqlSessionTemplate.update("policy.updatePolicyInfo", policyInfo);
	}

	
	//FIXME 待修改
	@SuppressWarnings("unchecked")
	public List<Policy> getAllPolicys(int roleId) {
		List<Integer> pageIdList= sqlSessionTemplate.selectList("policy.getPageId",roleId);
		if(pageIdList.size() != 0 && pageIdList != null){
			return sqlSessionTemplate.selectList("policy.selectAllPolicys",pageIdList);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Policy> getAllPolicys(String sortName,String sortOrder) {
		//排序
		if(!JudgeUtil.isEmpty(sortName)&&SortUtil.judgeSortOrder(sortOrder)){
			OrderByHelper.orderBy(sortName+" "+sortOrder);
		}
		return this.sqlSessionTemplate.selectList("policy.selectAllPolicys2");
	}

	@Override
	public int getRoleCountOfPolicy(String policyIds) {
		// TODO Auto-generated method stub
		return (int)sqlSessionTemplate.selectOne("policy.getRoleCountOfPolicy",policyIds);
	}

	@Override
	public int getPolicyId(String policyIds) {
		// TODO Auto-generated method stub
		return (int)sqlSessionTemplate.selectOne("policy.getPolicyId",policyIds);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyChild> getPolicysChildsOfRole(int roleId) {
		List<Integer> pcIdList=sqlSessionTemplate.selectList("policyChild.getChildId",roleId);
		if(pcIdList.size() != 0 && pcIdList != null){
			return sqlSessionTemplate.selectList("policyChild.getPCListOfRole",pcIdList);
		}
		return null;
	}

	@Override
	public List<PolicyChild> getAllPolicyChilds() {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("policyChild.getAllChildPolicys");
	}

	@Override
	public Long addPolicyChild(String text, String icon, String sref,
			String type, int pId) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("text", text);
		map.put("icon", icon);
		map.put("sref", sref);
		map.put("pId", pId);
		map.put("type", type);
		sqlSessionTemplate.insert("policy.addPolicyChild", map);
		return (Long)map.get("id");
	}

	@Override
	public int getPolicyIdByText(String policytext) {
		// TODO Auto-generated method stub
		Object page_id= sqlSessionTemplate.selectOne("policy.getPolicyIdByText",policytext);
		if(page_id !=null){
			return (int)page_id;
		}else{
			return 0;
		}
	}

	@Override
	public int getRoleCountOfPolicyChild(String policyChildId) {
		// TODO Auto-generated method stub
		return (int)sqlSessionTemplate.selectOne("policy.getRoleCountOfPolicyChild",policyChildId);
	}

	@Override
	public void deleteOneByPolicyChild(String policyChildId) {
		// TODO Auto-generated method stub
		sqlSessionTemplate.delete("policy.deleteOneByPolicyChild",policyChildId);
	}

	@Override
	public int updatePolicyChild(PolicyChild p) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("policy.updatePolicyChildInfo", p);
	}

	@Override
	public int getChrildPolicyIdByText(String policytext) {
		// TODO Auto-generated method stub
		Object id = sqlSessionTemplate.selectOne("policy.getChrildPolicyIdByText",policytext);
		if(id !=null){
			return (int)id;
		}else{
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPolicyResourceList(int roleId) {
		// TODO Auto-generated method stub
		List<Integer> pageIdList=this.sqlSessionTemplate.selectList("policy.getPageIdList",roleId);
		if(pageIdList != null && pageIdList.size() != 0){
			return this.sqlSessionTemplate.selectList("policy.getPolicyResourceList",pageIdList);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getPChildResList(int roleId) {
		// TODO Auto-generated method stub
		List<Integer> pChildIdList = this.sqlSessionTemplate.selectList("policy.getPChildId",roleId);
		if(pChildIdList != null && pChildIdList.size() > 0){
			return this.sqlSessionTemplate.selectList("policy.getPChildResList",pChildIdList);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getMenuResList(int roleId) {
		// TODO Auto-generated method stub
		List<String> menuIdList = this.sqlSessionTemplate.selectList("policy.getMenuIdList",roleId);
		if(menuIdList != null && menuIdList.size() > 0){
			return this.sqlSessionTemplate.selectList("policy.getMenuResList",menuIdList);
		}
		return null;
	}

	@Override
	public String getTextByPolicyChild(String policyId) {
		return this.sqlSessionTemplate.selectOne("policy.getTextByPolicyChild",policyId);
	}
}
