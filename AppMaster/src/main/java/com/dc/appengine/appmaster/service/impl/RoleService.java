package com.dc.appengine.appmaster.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.dc.appengine.appmaster.dao.IMenuRoleDao;
import com.dc.appengine.appmaster.dao.IPolicyDao;
import com.dc.appengine.appmaster.dao.IPolicyMenuDao;
import com.dc.appengine.appmaster.dao.IRoleDao;
import com.dc.appengine.appmaster.entity.MenuRole;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;
import com.dc.appengine.appmaster.entity.Role;
import com.dc.appengine.appmaster.service.IPolicyService;
import com.dc.appengine.appmaster.service.IRoleService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Service("roleService")
public class RoleService implements IRoleService {

	private static final Logger log=LoggerFactory.getLogger(ApplicationService.class);
	
	@Autowired
	@Qualifier("roleDao")
	private IRoleDao roleDao;

	@Autowired
	@Qualifier("policyMenuDao")
	private IPolicyMenuDao policyMenuDao;
	
	@Autowired
	@Qualifier("menuRoleDao")
	private IMenuRoleDao menuRoleDao;
	
	@Autowired
	@Qualifier("policyDao")
	private IPolicyDao policyDao;
	
	@Autowired
	@Qualifier("policyService")
	private IPolicyService policyService;
	
	//角色列表
	@Override
	public List<Role> listRoles(String sortName,String sortOrder) {
		// TODO Auto-generated method stub	
		return roleDao.listRoles(sortName,sortOrder);
		
	}

	
	//创建角色
	@Override
	public String createRole(String roleName) {
		// TODO Auto-generated method stub
		int roleId = roleDao.getNewRoleId();
		return roleDao.addRole(roleId, roleName);
	}

	//给角色分配权限
	@Override
	public String addPolicysOfRole(int roleId, String policyIds) {
		// TODO Auto-generated method stub
		String[] policys = policyIds.split(",");
		for(String policy : policys){
			roleDao.addPolicyOfRole(roleId, policy);
		}
		return "{\"result\":true,\"message\":\"" + "分配成功" + "\"}";
	}

	//取消权限
	@Override
	public String cancelPolicysOfRole(int roleId, String policyId) {
		// TODO Auto-generated method stub
		return roleDao.deletePolicyOfRole(roleId, policyId);
	}

	//角色的权限列表
	@Override
	public List<Policy> policysOfRole(int roleId) {
		// TODO Auto-generated method stub
		return roleDao.listPolicysOfRole(roleId);
	}

	//查看角色名
	@Override
	public String getRoleName(int uuId) {
		// TODO Auto-generated method stub
		return roleDao.getRoleName(uuId);
	}
	
	//通过角色名查Id
	public int getRoleId(String roleName){
		return roleDao.getRoleId(roleName);
	}

	//删除角色
	@Override
	public String delRole(String roleId) {
		// TODO Auto-generated method stub
		//判断是否存在用户关联
		if(roleDao.getUserOfRole(roleId) > 0){
			return "{\"result\":false,\"message\":\"" + "该角色存在关联用户，不能删除" + "\"}";
		}else{
			if(roleDao.deleteRole(roleId) != null){
				return "{\"result\":true,\"message\":\"" + "删除成功" + "\"}";
			}
			return "{\"result\":false,\"message\":\"" + "删除失败" + "\"}";
		}
		
	}

	@Override
	public String isExitRole(String roleName) {
		// TODO Auto-generated method stub
		if(roleDao.isExitRole(roleName) == true){
			return "false";
		}
		return "true";
	}
	
	//保存角色的权限
	public String savePolicysOfRole(int roleId , String policyIds, String menuIds,String policyFlag){
		String modifyPolicy="";
		modifyPolicy=getModify(roleId, policyIds, menuIds, policyFlag);
		if(!JudgeUtil.isEmpty(policyFlag)&&"policy".equals(policyFlag)){
			//取消角色的所有权限
			List<Policy> list = roleDao.listPolicysOfRole(roleId);
			if(list != null && list.size() != 0){
				for(Policy p : list){
					int pId = p.getPage_id();
					roleDao.deletePolicyOfRole(roleId, pId+"");
				}
			}
			
			//保存要给角色添加的权限
			if(policyIds != null && !policyIds.equals("")){
				String[] policys = policyIds.split(",");
				for(String policy : policys){
					if(policy != ""){
						roleDao.addPolicyOfRole(roleId, policy);
					}				
				}
			}
		}else if(!JudgeUtil.isEmpty(policyFlag)&&"policyChild".equals(policyFlag)){
			//二级菜单的权限取消及保存
			List<PolicyChild> list = policyDao.getPolicysChildsOfRole(roleId);
			if(list != null && list.size() != 0){
				for(PolicyChild p : list){
					int pcId = p.getId();
					roleDao.deletePolicyChildOfRole(roleId, pcId+"");
				}
			}
			if(policyIds != null && !policyIds.equals("")){
				String[] policys = policyIds.split(",");
				for(String policyChild : policys){
					if(policyChild != ""){
						roleDao.addPolicyChildOfRole(roleId, policyChild);
					}				
				}
			}
			/*//取消角色的所有权限
			List<Policy> list = roleDao.listPolicysOfRole(roleId);
			if(list != null && list.size() != 0){
				for(Policy p : list){
					int pId = p.getPage_id();
					roleDao.deletePolicyOfRole(roleId, pId+"");
				}
			}
			
			//保存要给角色添加的权限
			if(policyIds != null && !policyIds.equals("")){
				String[] policys = policyIds.split(",");
				for(String policy : policys){
					if(policy != ""){
						roleDao.addPolicyOfRole(roleId, policy);
					}				
				}
			}*/
			//取消角色的所有三级级权限
			int temp = policyMenuDao.deletePolicyMenu(roleId);
			//保存要给角色的三级级权限
			if(menuIds != null && !menuIds.equals("")){
				String[] menus = menuIds.split(",");
				for(String menu : menus){
					MenuRole menuRole = new MenuRole();
					menuRole.setMenuId(menu);
					menuRole.setRoleId(roleId+"");
					menuRoleDao.savePolicyMenuOfRole(menuRole);
				}			
			}	
		}
		return MessageHelper.wrap("result",true,"message","权限分配成功","modify",modifyPolicy);
	}
	
	
	public String getModify(int roleId , String policyIds, String menuIds,String policyFlag){
		String modify ="";
		if(JudgeUtil.isEmpty(policyIds)){
			policyIds="";
		}
		if("policy".equals(policyFlag)){
			String policies = policyService.getRootPolicyOfRole(roleId);
			JSONArray policyList = JSONArray.parseArray(policies);
			String[] policys = policyIds.split(",");
			List<String> tem = Arrays.asList(policys);
			String add="";
			String delete="";
			for(int i=0;i<policyList.size();i++){
				Map<String,Object> map = (Map<String, Object>) policyList.get(i);
				Boolean have = (Boolean)map.get("have");
				Map<String,Object> policyChild = (Map<String, Object>) map.get("policy");
				String pageId =policyChild.get("page_id")+"";
				String text =policyChild.get("text")+"";
				if(have){
					if(!tem.contains(pageId)){
						//delete 的菜单
						delete=delete+text+" ";
					}
				}else{
					if(tem.contains(pageId)){
						//add 的菜单
						add=add+text+" ";
					}
				}
			}
			if(!JudgeUtil.isEmpty(add)){
				modify = "新增一级权限菜单: "+add+";";
			}
			if(!JudgeUtil.isEmpty(delete)){
				modify = modify+"删除一级权限菜单: "+delete+";";
			}
		}else{
			String policies = policyService.getPolicysOfRole(roleId);
			JSONArray list = JSONArray.parseArray(policies);
			String[] policys = policyIds.split(",");
			List<String> policyList = Arrays.asList(policys);
			String[] menus = menuIds.split(",");
			List<String> menuList = Arrays.asList(menus);
			String addPolicyChild="";
			String deletePolicyChild="";
			String addMenu="";
			String deleteMenu="";
			for(int i=0;i<list.size();i++){
				Map<String,Object> map = (Map<String, Object>) list.get(i);
				Boolean have = (Boolean)map.get("have");
				Map<String,Object> policyChild = (Map<String, Object>) map.get("policyChild");
				String pageId =policyChild.get("id")+"";
				String text =policyChild.get("text")+"";
				JSONArray policyMenuList = (JSONArray) policyChild.get("policyMenu");
				for(int j=0;j<policyMenuList.size();j++){
					Map<String,Object> policyMenu = (Map<String, Object>) policyMenuList.get(j);
					String menuId =policyMenu.get("id")+"";
					String menuText =policyMenu.get("title")+"";
					Boolean menuHave = (Boolean)policyMenu.get("menu_flag");
					if(menuHave){
						if(!menuList.contains(menuId)){
							//delete 的菜单
							deleteMenu=deleteMenu+menuText+" ";
						}
					}else{
						if(menuList.contains(menuId)){
							//add 的菜单
							addMenu=addMenu+menuText+" ";
						}
					}
				}
				if(have){
					if(!policyList.contains(pageId)){
						//delete 的菜单
						deletePolicyChild=deletePolicyChild+text+" ";
					}
				}else{
					if(policyList.contains(pageId)){
						//add 的菜单
						addPolicyChild=addPolicyChild+text+" ";
					}
				}
			}
			if(!JudgeUtil.isEmpty(addPolicyChild)){
				modify = "新增二级权限菜单: "+addPolicyChild+";";
			}
			if(!JudgeUtil.isEmpty(deletePolicyChild)){
				modify=modify+"删除二级权限菜单: "+deletePolicyChild+";";
			}
			if(!JudgeUtil.isEmpty(addMenu)){
				modify=modify+"新增三级权限菜单: "+addMenu+";";
			}
			if(!JudgeUtil.isEmpty(deleteMenu)){
				modify=modify+"删除三级权限菜单: "+deleteMenu+";";
			}
		}
		if(!JudgeUtil.isEmpty(modify)&&modify.endsWith(";")){
			modify = modify.substring(0, modify.length()-1);
		}
		return modify;
	}
}
