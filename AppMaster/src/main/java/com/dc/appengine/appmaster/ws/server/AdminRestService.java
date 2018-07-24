package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IPolicyMenuService;
import com.dc.appengine.appmaster.service.IPolicyService;
import com.dc.appengine.appmaster.service.IRoleService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MD5Util;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/admin")
public class AdminRestService {

	private static final Logger log = LoggerFactory.getLogger(ApplicationRestService.class);
	
	@Resource
	IRoleService roleService;
	
	@Resource
	IUserService userService;
	
	@Resource
	IPolicyService policyService;
	
	@Resource
	IPolicyMenuService policyMenuService;

	@Resource
	IAudit auditService;
	
	@RequestMapping(value = "listUsers",method = RequestMethod.GET)
	@ResponseBody
	public String listUsers(@RequestParam("pageSize") int pageSize,
			@RequestParam("pageNum") int pageNum,
			@RequestParam("key") String key,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		if(key.equals("_") || key.equals("%") || key.equals("^") || key.equals("*")){
			key = "\\" + key;
		}
		return userService.getAllAppUser(pageSize, pageNum, key,sortName,sortOrder,"2");
	}
	

	@RequestMapping(value = "isExitUser",method = RequestMethod.POST)
	@ResponseBody
	public String isExitUser(@RequestParam("userName") String userName){
		System.out.println("ad  "+userService.isExitUser(userName) == "false");
		if(userService.isExitUser(userName) == "false"){
			return "{\"result\":false}";
		}
		return "{\"result\":true}";
	}
	

	@RequestMapping(value = "isExitRole",method = RequestMethod.POST)
	@ResponseBody
	public String isExitRole(@RequestParam("roleName") String roleName){
		if(roleService.isExitRole(roleName).equals("false")){
			return "{\"result\":false}";
		}
		return "{\"result\":true}";
	}
	

	@RequestMapping(value = "listRoles",method = RequestMethod.GET)
	@ResponseBody
	public String listRoles(@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		return JSON.toJSONString(roleService.listRoles(sortName,sortOrder));
	}
	
	@RequestMapping(value = "addRole",method = RequestMethod.POST)
	@ResponseBody
	public String createRole(@RequestParam("roleName") String roleName){
		if (log.isDebugEnabled()) {
			log.debug("addRole  name"+"="+roleName);
		}
		if(roleService.isExitRole(roleName).equals("false")){
			return "{\"result\":false,\"message\":\"" + "该角色已存在" + "\"}";
		}
		if(roleService.createRole(roleName) != null ){
			return "{\"result\":true,\"message\":\"" + "创建成功" + "\"}";
		}
		//==============添加审计end=====================
		return "{\"result\":false,\"message\":\"" + "创建失败" + "\"}";
	}
	

	@RequestMapping(value = "createUser",method = RequestMethod.POST)
	@ResponseBody
	public String createUser(@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("roleId") String roleId,
			@RequestParam("pId") long pId,
			@RequestParam("userId") String userId){
		String roleName = "运维人员";
		int rId = 2;
		String returnMsg = userService.add(username, password, rId , pId);
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				JSONObject jo = JSONObject.parseObject(returnMsg);
				boolean result = jo.getBoolean("result");
				auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,username, ResourceCode.Operation.ADD, result?1:0, "创建用户:" + username));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}
	

	@RequestMapping(value = "delUser",method = RequestMethod.POST)
	@ResponseBody
	public String delUser(@RequestParam("userId") String userId){
	
		//return "{\"result\":false,\"message\":\"" + "该用户存在集群，删除失败" + "\"}";
		return userService.delUser(userId);
		
	}
	

	@RequestMapping(value = "delRole",method = RequestMethod.POST)
	@ResponseBody
	public String delRole(@RequestParam("roleId") String roleId,@RequestParam("userId") String userId){
		String roleName = roleService.getRoleName(Integer.parseInt(roleId));
		String returnMsg =  roleService.delRole(roleId);
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				JSONObject jo = JSONObject.parseObject(returnMsg);
				boolean result = jo.getBoolean("result");
				auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,roleName, ResourceCode.Operation.DELETE, result?1:0, "删除角色:" + roleName));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}

	@RequestMapping(value = "createPolicy",method = RequestMethod.POST)
	@ResponseBody
	public String createPolicy(@RequestParam("policyInfos") String policyInfos){
		return policyService.createPolicy(policyInfos);
	}
	

	@RequestMapping(value = "delPolicys",method = RequestMethod.POST)
	@ResponseBody
	public String delPolicy(@RequestParam("policyIds") String policyIds){
		System.out.println(policyIds);
		//判断是否有关联角色
		if(policyService.getRoleCountOfPolicy(policyIds) > 0){
			return "{\"result\":false,\"message\":\"" + "该权限存在关联角色，不能删除" + "\"}";
		}else{
			if(policyService.deletePolicy(policyIds).equals("false")){
				return "{\"result\":false,\"message\":\"" + "删除失败" + "\"}";
			}
			return "{\"result\":true,\"message\":\"" + "删除成功" + "\"}";
		}
		
	}
	

	@RequestMapping(value = "listPolicys",method = RequestMethod.GET)
	@ResponseBody
	public String listPolicys(@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		return JSON.toJSONString(policyService.getAllPolicys(sortName,sortOrder));
	}
	

	@RequestMapping(value = "modifyPolicy",method = RequestMethod.POST)
	@ResponseBody
	public String modifyPolicy(@RequestParam("policyInfos") String policyInfos){
		if(policyService.modifyPolicy(policyInfos).equals("false")){
			return "{\"result\":false,\"message\":\"" + "更新失败" + "\"}";
		}
		return "{\"result\":true,\"message\":\"" + "更新成功" + "\"}";
	}
	
	@RequestMapping(value = "addPolicysOfRole",method = RequestMethod.POST)
	@ResponseBody
	public String addPolicysOfRole(@RequestParam("roleName") String roleName, 
			@RequestParam("policyIds") String policyIds){
		int roleId = roleService.getRoleId(roleName);
		return roleService.addPolicysOfRole(roleId, policyIds);
	}
	
	@RequestMapping(value = "cancelPolicysOfRole",method = RequestMethod.POST)
	@ResponseBody
	public String cancelPolicysOfRole(@RequestParam("roleName") String roleName, 
			@RequestParam("policyId") String policyId){
		int roleId = roleService.getRoleId(roleName);
		return roleService.cancelPolicysOfRole(roleId, policyId);
	}
	
	@RequestMapping(value = "policysOfRole",method = RequestMethod.POST)
	@ResponseBody
	public String policysOfRole(@RequestParam("roleName") String roleName){
		int roleId = roleService.getRoleId(roleName);
		if(roleService.policysOfRole(roleId) == null){
			return "false";
		}
		return JSON.toJSONString(roleService.policysOfRole(roleId));
	}
	
	
	@RequestMapping(value = "getRoleOfUser",method = RequestMethod.POST)
	@ResponseBody
	public String getRoleOfUser(@RequestParam("userId") String userId){
		long uId = userService.getUserId(userId);
		int uuId = userService.getUserRole(uId);
		if(Integer.valueOf(uuId) != null){
			return roleService.getRoleName(uuId);
		}
		return "{\"result\":false,\"message\":\"" + "无角色" + "\"}";
	}
	
	//查出所有的权限，该角色拥有的权限加标识符
	@RequestMapping(value = "getPolicysOfRole",method = RequestMethod.POST)
	@ResponseBody
	public String getPolicysOfRole(@RequestParam("roleId") int roleId){
		return policyService.getPolicysOfRole(roleId);
	}
	
	@RequestMapping(value = "savePolicysOfRole",method = RequestMethod.POST)
	@ResponseBody
	public String savePolicysOfRole(@RequestParam("roleId") int roleId, 
			@RequestParam("policyIds")String policyIds,
			@RequestParam("menuIds")String menuIds,@RequestParam("policyFlag")String policyFlag,@RequestParam("userId") String userId){
		String returnMsg = roleService.savePolicysOfRole(roleId, policyIds, menuIds,policyFlag);
		JSONObject result = JSONObject.parseObject(returnMsg);
		String modify=result.getString("modify");
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				String roleName = roleService.getRoleName(roleId);
				JSONObject jo = JSONObject.parseObject(returnMsg);
				boolean result = jo.getBoolean("result");
				auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,roleName, ResourceCode.Operation.UPDATE, result?1:0, "修改角色权限: " + modify));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}
	
	//根据userId获取到角色，最后返回它拥有的所有普通用户
	@RequestMapping(value = "getSonsOfUser",method = RequestMethod.GET)
	@ResponseBody
	public String getSonsOfUser(@RequestParam("userId")  long userId){
		System.out.println(userService.getSonsOfUser(userId));
		return userService.getSonsOfUser(userId);
	}
	
	//根据userName获取到角色，最后返回它拥有的所有普通用户
	@RequestMapping(value = "getSonsOfUserByName",method = RequestMethod.GET)
	@ResponseBody
	public String getSonsOfUserByName(@RequestParam("userName")  String userName){
		long userId = userService.getUserId(userName);
		System.out.println(userService.getSonsOfUser(userId));
		return userService.getSonsOfUser(userId);
	}

	//判断用户是否是管理员
	@RequestMapping(value = "isAdmin",method = RequestMethod.GET)
	@ResponseBody
	public String isAdmin(@RequestParam("userId") String userId){
		return userService.isAdmin(userId);
	}
	
	
	//判断用户是否有操作此按钮的权限，有true
	@RequestMapping(value = "isAccess",method = RequestMethod.GET)
	@ResponseBody
	public String isAccess(@RequestParam("userId") String userId,
			@RequestParam("menuId") String menuId){
		return policyMenuService.isAccess(userId, menuId);
	}

	@RequestMapping(value = "addPolicyMenu",method = RequestMethod.POST)
	@ResponseBody
	public String addPolicyMenu(@RequestParam("policyMenuInfo") String policyMenuInfo){
		return policyMenuService.addPolicyMenu(policyMenuInfo);
	}
	
	//分页查询按钮列表
	@RequestMapping(value = "findMenuByPage",method = RequestMethod.GET)
	@ResponseBody
	public String findMenuByPage(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum){
		return policyMenuService.findMenuByPage(pageSize, pageNum);
	}
	
	//分页查询角色的按钮列表
	@RequestMapping(value = "findMenuByPageOfRole",method = RequestMethod.GET)
	@ResponseBody
	public String findMenuByPageOfRole(@RequestParam("pageSize") int pageSize, 
			@RequestParam("pageNum") int pageNum,
			@RequestParam("roleId") String roleId){
		return policyMenuService.findMenuByPageOfRole(pageSize, pageNum, roleId);
	}
	
	@RequestMapping(value="getAllPolicyChilds",method=RequestMethod.GET)
	@ResponseBody
	public String getAllPolicyChilds(@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		return JSON.toJSONString(policyService.getAllPolicyChilds(sortName,sortOrder));
	}
	
	@RequestMapping(value="createPolicyChild" ,method=RequestMethod.POST)
	@ResponseBody
	public String createPolicyChild(@RequestParam("policyInfos") String policyInfos,
			@RequestParam(name="policytext", required=false) String policytext,@RequestParam("userId") String userId){
		String returnMsg = policyService.createPolicyChild(policyInfos, policytext);
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				Policy p = JSON.parseObject(policyInfos, Policy.class);
				JSONObject jo = JSONObject.parseObject(returnMsg);
				boolean result = jo.getBoolean("result");
				auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,p.getText(), ResourceCode.Operation.ADD, result?1:0, "新建菜单:" + p.getText()));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}
	
	@RequestMapping(value = "delPolicyChild",method = RequestMethod.POST)
	@ResponseBody
	public String delPolicyChild(@RequestParam("policyChildId") String policyChildId
			,@RequestParam("pId") int pId,@RequestParam("userId") String userId){
		System.out.println(policyChildId);
		//判断是否有关联角色
		if(policyService.getRoleCountOfPolicyChild(policyChildId,pId) > 0){
			return "{\"result\":false,\"message\":\"" + "该权限存在关联角色，不能删除" + "\"}";
		}else{
			String text = policyService.getTextByPolicyChild(policyChildId);
			if(policyService.deletePolicyChild(policyChildId,pId).equals("false")){
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,text, ResourceCode.Operation.DELETE, 0, "删除菜单:" + text));
					}
				});
				//==============添加审计end=====================
				return "{\"result\":false,\"message\":\"" + "删除失败" + "\"}";
			}
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,text, ResourceCode.Operation.DELETE, 1, "删除菜单:" + text));
				}
			});
			//==============添加审计end=====================
			return "{\"result\":true,\"message\":\"" + "删除成功" + "\"}";
		}
		
	}
	
	@RequestMapping(value = "modifyPolicyChild",method = RequestMethod.POST)
	@ResponseBody
	public String modifyPolicyChild(@RequestParam("policyInfos") String policyInfos,@RequestParam("userId") String userId){
		final Policy p = JSON.parseObject(policyInfos, Policy.class);
		JSONObject pTmp = JSON.parseObject(policyInfos);
		String menuName = null;
		List<PolicyChild> policyList = policyService.getAllPolicyChilds(null,null);
		for(PolicyChild policy : policyList){
			if(policy.getId()==(Integer)pTmp.get("policyId")){
				menuName = policy.getText();
				break;
			}
		}
		final String formerMenuName = menuName;
		if(policyService.modifyPolicyChild(policyInfos).equals("false")){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,p.getText(), ResourceCode.Operation.UPDATE, 0, "更新前菜单:"+formerMenuName +" 更新后菜单:" + p.getText()));
				}
			});
			//==============添加审计end=====================
			return "{\"result\":false,\"message\":\"" + "更新失败" + "\"}";
		}
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(userId, ResourceCode.AUTHORITY,p.getText(), ResourceCode.Operation.UPDATE, 1, "更新前菜单"+formerMenuName +" 更新后菜单:" + p.getText()));
			}
		});
		//==============添加审计end=====================
		return "{\"result\":true,\"message\":\"" + "更新成功" + "\"}";
	}
	
	@RequestMapping(value = "listPolicysTemp" ,method = RequestMethod.GET)
	@ResponseBody
	public String listPolicysTemp(){
		return JSON.toJSONString(policyService.getAllPolicys(null,null));
	}
	
	//查出所有的权限，该角色拥有的权限加标识符(一级二级)
		@RequestMapping(value = "getRootPolicysOfRole",method = RequestMethod.POST)
		@ResponseBody
		public String getRootPolicysOfRole(@RequestParam("roleId") int roleId){
			return policyService.getRootPolicyOfRole(roleId);
		}
		
		@RequestMapping(value = "updateUser",method = RequestMethod.POST)
		@ResponseBody
		public String updateUser(@RequestParam("username") String username,
				@RequestParam("password") String password,
				@RequestParam("id") long id,
				@Context HttpServletRequest request){
			String formerUserName = userService.getUserName(id);
			Map<String,Object> params = new HashMap<>();
			params.put("username", username);
			params.put("password", MD5Util.md5(password));
			params.put("id", id);
			String returnMsg = userService.updateUser(params);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					JSONObject jo = JSONObject.parseObject(returnMsg);
					boolean result = jo.getBoolean("result");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.AUTHORITY,username, ResourceCode.Operation.UPDATE, result?1:0, "更新前:"+formerUserName+" 更新后用户:" + username));
				}
			});
			//==============添加审计end=====================
			return returnMsg;
		}
		
		@RequestMapping(value = "delUserById",method = RequestMethod.GET)
		@ResponseBody
		public String delUserById(@RequestParam("userId") String userId,@Context HttpServletRequest request){
		
			//return "{\"result\":false,\"message\":\"" + "该用户存在集群，删除失败" + "\"}";
			String userName = userService.getUserName(Long.parseLong(userId));
			String returnMsg = userService.delUserById(userId);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					JSONObject jo = JSONObject.parseObject(returnMsg);
					boolean result = jo.getBoolean("result");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.AUTHORITY,userName, ResourceCode.Operation.DELETE, result?1:0, "删除用户:" + userName));
				}
			});
			//==============添加审计end=====================
			return returnMsg;
			
		}
		
		@RequestMapping(value = "checkUserName",method = RequestMethod.GET)
		@ResponseBody
		public String checkUserName(@RequestParam("userName") String userName,@RequestParam(name="userId",required=false) String userId){
			return JSON.toJSONString(userService.checkUserName(userName, userId));
		}
		
		@RequestMapping(value = "isCOP", method = RequestMethod.GET)
		public Object isCOP(HttpServletRequest request) {
			Map<String, Object> result = new HashMap<>();
			if (request.getSession().getAttribute("userId") != null) {
				result.put("isCOP", true);
			} else {
				result.put("isCOP", false);
			}
			return result;
		}
		
		@RequestMapping(value = "listAllUsers",method = RequestMethod.GET)
		@ResponseBody
		public String listAllUsers(@RequestParam("pageSize") int pageSize,
				@RequestParam("pageNum") int pageNum,
				@RequestParam("key") String key,
				@RequestParam(name="sortOrder",required=false) String sortOrder,
				@RequestParam(name="sortName",required=false) String sortName) {
			if(key.equals("_") || key.equals("%") || key.equals("^") || key.equals("*")){
				key = "\\" + key;
			}
			return userService.getAllAppUser(pageSize, pageNum, key,sortName,sortOrder,null);
		}
}
