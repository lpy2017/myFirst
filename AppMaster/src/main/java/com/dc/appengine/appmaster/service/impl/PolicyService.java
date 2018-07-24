package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IPolicyDao;
import com.dc.appengine.appmaster.dao.IPolicyMenuDao;
import com.dc.appengine.appmaster.dao.IRoleDao;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;
import com.dc.appengine.appmaster.entity.PolicyMenu;
import com.dc.appengine.appmaster.entity.PolicySearch;
import com.dc.appengine.appmaster.service.IPolicyService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.SortUtil;

@Component("policyService")
public class PolicyService implements IPolicyService{

	private static final Logger log=LoggerFactory.getLogger(ApplicationService.class);
	
	@Autowired
	@Qualifier("policyDao")
	private IPolicyDao policyDao;
	
	@Autowired
	@Qualifier("roleDao")
	private IRoleDao roleDao;
	
	@Autowired
	@Qualifier("policyMenuDao")
	private IPolicyMenuDao policyMenuDao;
	
	@Override
	public List<Policy> getAllPolicys(String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		List<Policy> policyList = policyDao.getAllPolicys(null,null);
		List<PolicyMenu> pmList = policyMenuDao.getAllPolicyMenus();//所有菜單
		for(Policy policy : policyList){
			List<PolicyMenu> temp = new ArrayList<>();
			for(PolicyMenu policyMenu : pmList){
				String pmId = policyMenu.getId().split("_")[0];
				if(Integer.valueOf(pmId) == policy.getPage_id()){
					temp.add(policyMenu);
				}
			}
			policy.setPolicyMenu(temp);
		}
		return policyDao.getAllPolicys(sortName,sortOrder);
	}

	@Override
	public String deletePolicy(String page_id) {
		// TODO Auto-generated method stub
		log.debug("texts: " + page_id);
		String[] str = page_id.split(",");
		for(String text : str){
			policyDao.deleteOneByPolicyText(text);
		}
		return "{\"result\":true,\"message\":\"" + "删除成功" + "\"}";
	}

	@Override
	public String createPolicy(String policyInfos) {
		// TODO Auto-generated method stub
		log.debug("policyInfos: " + policyInfos);
		Policy p = JSON.parseObject(policyInfos, Policy.class);
		String text = p.getText();
		String icon = p.getIcon();
		String sref = p.getSref();
		String title = p.getTitle();
		String type = p.getType();
		int serialNum = p.getSerialNum();
		if(policyDao.addPolicy(text,icon,sref,title,type,serialNum) != null){
			return "{\"result\":true,\"message\":\"" + "新建成功" + "\"}";
		}
		return "{\"result\":false,\"message\":\"" + "新建失败" + "\"}";
	}

	@Override
	public String modifyPolicy(String policyInfos) {
		// TODO Auto-generated method stub
		String policy = policyInfos.replace("policyId", "page_id");
		Policy p = JSON.parseObject(policy, Policy.class);
		int i = policyDao.updatePolicy(p);
		if(i > 0){
			return "true";
		}
		return "false";
	}


	@Override
	public int getRoleCountOfPolicy(String policyId) {

		return policyDao.getRoleCountOfPolicy(policyId);
	}
	//角色的一级菜单和二级菜单
	//获取角色的一级菜单以及二级菜单权限展示(暂且不用)
		public String getRootPolicyOfRole(int roleId){
			List<Policy> policys = policyDao.getAllPolicys(null,null);//全部一级
			List<Policy> policysOfRole = roleDao.listPolicysOfRole(roleId);//有权限一级
			List<PolicyChild> pcList = policyDao.getAllPolicyChilds();//所有二级
			List<PolicyChild> pcList_is = policyDao.getPolicysChildsOfRole(roleId);//有权限二级
			List<PolicySearch> list = new ArrayList<>();
			for(Policy p : policys){
				PolicySearch e = new PolicySearch();
				e.setHave(false);
				if(policysOfRole != null && policysOfRole.size() != 0){
					for(Policy pr : policysOfRole){
						if(pr.getPage_id() == p.getPage_id()){
							e.setHave(true);
						}
					}
					
					/*List<PolicyChild> pcList_temp = new ArrayList<PolicyChild>();
					//判断该菜单是否有拥有权限的二级菜单
					for(PolicyChild pc : pcList){
						
						if(pc.getParent_id() == p.getPage_id()){
							pc.setFlag(false);
							for(PolicyChild pc_is : pcList_is){
								if(pc_is.getParent_id() == p.getPage_id()){
									pc.setFlag(true);
								}
							}
							pcList_temp.add(pc);
						}	
					}
					p.setPolicyChild(pcList_temp);*/
					
					
				}/*else{
					List<PolicyChild> pcList_temp = new ArrayList<PolicyChild>();
					for(PolicyChild pc : pcList){
						if(pc.getParent_id() == p.getPage_id()){
							pc.setFlag(false);
							for(PolicyChild pc_is : pcList_is){
								if(pc_is.getParent_id() == p.getPage_id()){
									pc.setFlag(true);
								}
							}
							pcList_temp.add(pc);
						}	
					}
					p.setPolicyChild(pcList_temp);
					e.setPolicy(p);
					list.add(e);
				}*/
				e.setPolicy(p);
				list.add(e);
			}
			return JSON.toJSONString(list);
		}
	//角色的二级菜单和按钮
	public String getPolicysOfRole(int roleId){
		List<Policy> policys = policyDao.getAllPolicys(null,null);
		List<Policy> policysOfRole = roleDao.listPolicysOfRole(roleId);
		List<PolicySearch> list = new ArrayList<>();
		List<PolicyMenu> pmList = policyMenuDao.getAllPolicyMenus();
		//有权限的菜单列表
		List<String> menuList = policyMenuDao.getMenuId(roleId+"");
		//有权限的二级菜单
		List<PolicyChild> policyChilds = policyDao.getPolicysChildsOfRole(roleId);
		//所有的二级菜单
		List<PolicyChild> policyChildsAll = policyDao.getAllPolicyChilds();
		if(policyChildsAll != null && policyChildsAll.size() > 0){
			for(PolicyChild pc : policyChildsAll){
				String text = pc.getText();
				long pId = pc.getParent_id();
				for(Policy p : policys){
					if(p.getPage_id() == pId){
						pc.setText(p.getText() + "_" + text);
					}
				}
			}
		}
		for(PolicyChild pc : policyChildsAll){
			PolicySearch e = new PolicySearch();
			e.setHave(false);
			if(policyChilds != null && policyChilds.size() != 0){
				for(PolicyChild pr : policyChilds){
					if(pc.getId() == pr.getId()){
						e.setHave(true);
					}
					//判断该页面中是否有拥有权限的菜单
					List<PolicyMenu> policyMenusList = new ArrayList<PolicyMenu>();
					for(PolicyMenu policyMenu : pmList){
						
						String[] pmId = policyMenu.getId().split("_");
						if(pmId.length != 0){
							
							if(pc.getId() == Integer.valueOf(pmId[0])){
								policyMenu.setMenu_flag(false);
								for(String s : menuList){
									if(pc.getId() == Integer.valueOf(pmId[0])){
										if(s.equals(policyMenu.getId())){
											policyMenu.setMenu_flag(true);
										}
									}
									
								}
								policyMenusList.add(policyMenu);
							}
						}
					}
					pc.setPolicyMenu(policyMenusList);

				}
				e.setPolicyChild(pc);
				list.add(e);
			}else{
				List<PolicyMenu> temp = new ArrayList<>();
				for(PolicyMenu policyMenu : pmList){
					String[] pmId = policyMenu.getId().split("_");
					if(pmId.length != 0){
						
						if(pc.getId() == Integer.valueOf(pmId[0])){
							policyMenu.setMenu_flag(false);
							temp.add(policyMenu);
						}
					}
					
				}
				pc.setPolicyMenu(temp);
				e.setPolicyChild(pc);
				list.add(e);
			}
		}
		return JSON.toJSONString(list);
	}	
	
	//菜单页面 展示二级菜单列表+没有二级菜单的一级菜单
	public List<PolicyChild> getAllPolicyChilds(String sortName,String sortOrder){
		//所有的一级菜单
		List<Policy> policys = policyDao.getAllPolicys(null,null);
		//所有的二级菜单
		List<PolicyChild> policyChildsAll = policyDao.getAllPolicyChilds();
		/*if(policyChildsAll != null && policyChildsAll.size() > 0){
			for(PolicyChild pc : policyChildsAll){
				String text = pc.getText();
				long pId = pc.getParent_id();
				for(Policy p : policys){
					if(p.getPage_id() == pId){
						pc.setpText(p.getText());
					}
				}
			}
		}*/
		for(Policy p : policys){
			int count = 0;
			int pId = p.getPage_id();
			for(PolicyChild pc : policyChildsAll){
				long pcId = pc.getParent_id();
				if(pId == pcId){
					pc.setpText(p.getText());
					count ++;
				}
			}
			if(count == 0){
				PolicyChild  temp = new PolicyChild();
				temp.setType(p.getType());
				temp.setFlag(false);
				temp.setIcon(p.getIcon());
				temp.setId(pId);
				temp.setParent_id(0);//一级菜单
				temp.setpText(p.getText());
				temp.setSref(p.getSref());
				temp.setText(p.getText());
				policyChildsAll.add(temp);
			}
		}
		if(!JudgeUtil.isEmpty(policyChildsAll)){
			List tmpList = JSONArray.parseArray(JSONArray.toJSONString(policyChildsAll));
			SortUtil.sort(tmpList, SortUtil.getColunmName("policy", sortName), sortOrder);
			List<PolicyChild> list = new ArrayList<PolicyChild>();
			list = JSONArray.parseArray(JSONArray.toJSONString(tmpList),PolicyChild.class);
			return list;
		}else{
			return policyChildsAll;
		}
	}
	
	//新建二级菜单
	public String createPolicyChild(String policyInfos,String policytext) {
		// TODO Auto-generated method stub
		
		log.debug("policyInfos: " + policyInfos);
		if(policytext == null || policytext.isEmpty()){
			Policy p = JSON.parseObject(policyInfos, Policy.class);
			String text = p.getText();
			String icon = p.getIcon();
			String sref = p.getSref();
			String title = p.getTitle();
			String type = p.getType();
			int serialNum = p.getSerialNum();
			if(checkMenuNameExist(text)){
				return "{\"result\":false,\"message\":\"" + "存在同名菜单 \"}";
			}
			long temp = policyDao.addPolicy(text,icon,sref,title,type,serialNum);
			if(temp > 0){
				return "{\"result\":true,\"message\":\"" + "新建成功" + "\",\"id\":\"" + temp + "\"}";
			}
			return "{\"result\":false,\"message\":\"" + "新建失败" + "\"}";
		}else{
			PolicyChild p = JSON.parseObject(policyInfos, PolicyChild.class);
			String text = p.getText();
			String icon = p.getIcon();
			String sref = p.getSref();
			String type = p.getType();
			if(checkMenuNameExist(text)){
				return "{\"result\":false,\"message\":\"" + "存在同名菜单 \"}";
			}
			int pId = policyDao.getPolicyIdByText(policytext);
			long temp =policyDao.addPolicyChild(text,icon,sref,type,pId);
			if(temp > 0){
				return "{\"result\":true,\"message\":\"" + "新建成功" + "\",\"id\":\"" + temp + "\"}";
			}
			return "{\"result\":false,\"message\":\"" + "新建失败" + "\"}";
		}
		
	}

	//角色是否关联
	@Override
	public int getRoleCountOfPolicyChild(String policyChildId,int pId) {
		// TODO Auto-generated method stub
		if(pId == 0){
			//一级菜单删除
			return policyDao.getRoleCountOfPolicy(policyChildId);
		}
		return policyDao.getRoleCountOfPolicyChild(policyChildId);
	}
	
	//删除二级菜单
	@Override
	public Object deletePolicyChild(String policyChildId,int pId) {
		// TODO Auto-generated method stub
		if(pId == 0){
			//一级菜单删除
			deletePolicy(policyChildId);
		}else{
			policyDao.deleteOneByPolicyChild(policyChildId);
			}
		return "{\"result\":true,\"message\":\"" + "删除成功" + "\"}";
	}
	
	
	//更新二级菜单
	@Override
	public String modifyPolicyChild(String policyInfos) {
		// TODO Auto-generated method stub
		String policy = policyInfos.replace("policyId", "id");
		PolicyChild p = JSON.parseObject(policy.replace("pId", "parent_id"), PolicyChild.class);
		int i = 0;
		if(p.getParent_id() == 0){
			//一级
			Policy temp = new Policy();
			temp.setIcon(p.getIcon());
			temp.setPage_id(p.getId());
			temp.setSref(p.getSref());
			temp.setText(p.getText());
			temp.setType(p.getType());
			i = policyDao.updatePolicy(temp);
		}else{
			//二级
			i = policyDao.updatePolicyChild(p);
		}
		
		if(i > 0){
			return "true";
		}
		return "false";
	}
	
	public Boolean checkMenuNameExist(String menuName){
		int page_id= policyDao.getPolicyIdByText(menuName);
		int id= policyDao.getChrildPolicyIdByText(menuName);
		if(page_id >0 || id>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String getTextByPolicyChild(String policyId) {
		return policyDao.getTextByPolicyChild(policyId);
	}
	
	
}
