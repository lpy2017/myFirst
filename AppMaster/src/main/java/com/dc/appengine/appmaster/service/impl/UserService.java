package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.IPolicyDao;
import com.dc.appengine.appmaster.dao.IRoleDao;
import com.dc.appengine.appmaster.dao.IUserDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.PolicyChild;
import com.dc.appengine.appmaster.entity.User;
import com.dc.appengine.appmaster.entity.UserRole;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.MD5Util;
import com.dc.appengine.appmaster.utils.SortUtil;

@Service("userService")
public class UserService implements IUserService {
	private static Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	@Qualifier("userDao")
	private IUserDao userDao;
	
	@Autowired
	@Qualifier("policyDao")
	private IPolicyDao policyDao;

	@Autowired
	@Qualifier("roleDao")
	private IRoleDao roleDao;
	/*private IRoleService roleService = (RoleService)ServiceBeanContext.getInstance()
			.getBean("roleService");*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dc.appengine.appmaster.service.IUserService#login(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String login(String userName, String pwd) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", userName);
		param.put("pwd", pwd);
		User user = userDao.login(param);
		if (user != null) {
			return JSON.toJSONString(user);
		}
		return "{}";
	}

	@Override
	public List<Policy> getPoliciesOfUser(String userName) {
		if (userName!=null && !userName.equals("")) {
			long userId= userDao.getUserId(userName);
			//根据userId在role表中查出roleId
			long tempRoleId=userDao.getRoleOfUser(userId);
			log.debug("roleId :  "+tempRoleId);
			log.debug("roleId(int) :  "+(int)tempRoleId);
			//根据查出的role_id去role_policy_infomapping表查出所有policies
			List<Policy> tempPolicies=policyDao.getAllPolicys((int)tempRoleId);
			//根据role——id查出存在的二级菜单
			List<PolicyChild> policyChilds = policyDao.getPolicysChildsOfRole((int)tempRoleId);
			List<Policy> temp = new ArrayList<>();
			for(Policy p : tempPolicies){
				List<PolicyChild> pcList = new ArrayList<>();
				for(PolicyChild pc : policyChilds){
					if(pc.getParent_id() == p.getPage_id()){
						pcList.add(pc);
					}
				}
				p.setPolicyChild(pcList);
				temp.add(p);
			}
			return temp;
		}
		return null;
	}
	
	@Override
	public boolean authenticate(String userName, String password) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userName", userName);
		param.put("pwd", password);
		User user = userDao.login(param);
		return user != null;
	}

	@Override
	public long getUserId(String userName) {
		return userDao.getUserId(userName);
	}

	@Override
	public String getUserName(long id) {
		return userDao.getUserName(id);
	}

	/*@Override
	public boolean hasLoginPermission(String userName, String resource,
			String action) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", userName);
		map.put("resource", resource);
		map.put("action", action);
		return userDao.hasLoginPermission(map) > 0;
	}*/

	@Transactional
	@Override
	public String add(String userName, String password, int roleId ,long parentId) {
		if (userDao.existsUserName(userName)) {
			if (log.isDebugEnabled()) {
				log.debug("adding user failed, user name exist: " + userName);
			}
			return "{\"result\":false,\"message\":\"" + "用户已存在，请重新注册" + "\"}";
		}
		/*
		if(!updateRegistryUser(userName,password)){
			log.error("add registry user failed!see svn log");
			return  "{\"result\":\"false\",\"message\":\"" + "创建失败" + "\"}";
		}*/
		//updateRegistryUser(userName,password);
		String md5Pwd=MD5Util.md5(password);
		String result = userDao.add(userName, md5Pwd, roleId, parentId) > 0 ? "true" : "false";
		log.debug("receive adding user request, result : " + result);
		
		if(result.equals("true")){
			
			return "{\"result\":true,\"message\":\"" + "创建成功" + "\"}";
		}else{
			return "{\"result\":false,\"message\":\"" + "创建失败" + "\"}";
		}
	}
	/*public boolean updateRegistryUser(String userName,String password){
		Form form = new Form();
		form.param("userName", userName);
		form.param("password", password);
		String result=SvnWSRestClient.getWebResource().path("datacenter").path("updateRegistryUser")
		.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),String.class);
		return result.contains("ok");
	}*/
	
	@Override
	public User getUserInfo(long userId){
		return userDao.getUserInfo(userId);
	}
	
	//用户列表
	public List<String> listUsers(){
		return userDao.getAllUsers();
			
	}

	/*@Override
	public List<Long> getUsersOfFatherId(long uId) {
		// TODO Auto-generated method stub
		return userDao.getUsersOfFather(uId);
	}*/

	@Override
	public int getUserRole(long userId) {
		// TODO Auto-generated method stub
		return userDao.getRoleOfUser(userId);
	}

	
	@Override
	public String isExitUser(String userName) {
		// TODO Auto-generated method stub
		System.out.println(userDao.existsUserName(userName));
		if(userDao.existsUserName(userName) == true){
			return "false";
		}
		return "true";
	}

	@Override
	public String delUser(String userName) {
		// TODO Auto-generated method stub
		log.debug("userName: " + userName);
		//判断是否有集群
		
		long id = userDao.getUserId(userName);
		long temp = userDao.delUser(id);
		if(temp != 0){
			return "{\"result\":true,\"message\":\"" + "该用户删除成功" + "\"}";
		}
		return "{\"result\":false,\"message\":\"" + "该用户删除失败" + "\"}";
	}

	@Override
	public List<Long> getUsersOfFatherId(long userId) {
		// TODO Auto-generated method stub
		return userDao.getUsersOfFather(userId);
	}
	
	public Map<Long,String> getUserMaps(List<Long> userIds){
		return userDao.getUserMaps(userIds);
	}
	
	
	public String getSonsOfUser( long userId){
		int roleId = getUserRole(userId);
		String roleName = roleDao.getRoleName(roleId);
		Map<Long,String> mapsNull = new HashMap<>();
		if(roleName != null ){
			if(roleName.equals("管理员") || roleName.equals("门户")){
				//获取所有的运维人员
				List<Long> userList = getUsersOfFatherId(1);
				List<Long> lists = new ArrayList<>();
				for(Long uId : userList){
					//获取运维人员的普通用户
					lists.add(uId);
					List<Long> usersList = getUsersOfFatherId(uId);
					for(Long er : usersList){
						lists.add(er);
					}
				}
				//管理员本身
				lists.add(userId);
				//通过id列表获取用户id，name,map集合
				Map<Long,String> maps = getUserMaps(lists);
				return JSON.toJSONString(maps);
			}else
			if(roleName.equals("运维人员")){
				//该运维人员的子用户
				List<Long> userList = getUsersOfFatherId(userId);
				//运维人员本身
				userList.add(userId);
				//通过id列表获取用户id，name,map集合
				Map<Long,String> maps = getUserMaps(userList);
				return JSON.toJSONString(maps);
			}else{
				List<Long> userList = new ArrayList<>();
				//只返回自己
				userList.add(userId);
				Map<Long,String> maps = getUserMaps(userList);
				return JSON.toJSONString(maps);
			}
		}
		return JSON.toJSONString(mapsNull);
	}

	@Override
	public String isAdmin(String userId) {
		// TODO Auto-generated method stub
		long id = Long.valueOf(userId);
		int roleId = userDao.getRoleOfUser(id);
		if(roleId == 1){
			return "true";
		}
		return "false";
	}
	
	public User getUserInfoByName(String userName){
		boolean flag = userDao.existsUserName(userName);
		if(flag){
			long userId = userDao.getUserId(userName);
			return userDao.getUserInfo(userId);
		}
		return null;
	}

	//分页查询运维用户
	@Override
	public String getAllAppUser(int pageSize, int pageNum, String key,String sortName,String sortOrder,String role) {
		// TODO Auto-generated method stub
		JSONObject condition = new JSONObject();
		condition.put("key", key);
		condition.put("sortName", SortUtil.getColunmName("user", sortName));
		condition.put("sortOrder", sortOrder);
		condition.put("role", role);
		Page page =userDao.getAllAppUser(pageSize, pageNum, condition);
		return JSON.toJSONString(page);
	}
	
	@Override
	public String getResources(long userId) {
		List<String> resources = getResourceListOfUser(userId);
		// TODO Auto-generated method stub
		return JSON.toJSONString(resources);
	}
	//通过userId获取资源列表
	public List<String> getResourceListOfUser(long userId){
		//userId获取角色id-有权限的一级二级按钮id
		int roleId = getUserRole(userId);
		List<String> policyResourceList = policyDao.getPolicyResourceList(roleId);
		List<String> pChildResourceList = policyDao.getPChildResList(roleId);
		List<String> menuResList = policyDao.getMenuResList(roleId);
		List<String> result = new ArrayList<>();
		if(policyResourceList != null && policyResourceList.size() > 0){
			result.addAll(policyResourceList);
		}
		if(pChildResourceList != null && pChildResourceList.size() > 0){
			result.addAll(pChildResourceList);
		}
		if(menuResList != null && menuResList.size() > 0){
			result.addAll(menuResList);
		}
		return result;
	}

	@Override
	public String updateUser(Map<String, Object> params) {
		JSONObject result = new JSONObject();
		User user = userDao.getUserInfo(Long.valueOf(params.get("id").toString()));
		if(user ==null){
			result.put("result", false);
			result.put("message", "用户不存在");
		}else{
			int i =userDao.updateUser(params);
			if(i>0){
				result.put("result", true);
				result.put("message", "更新成功");
			}else{
				result.put("result", false);
				result.put("message", "更新失败");
			}
		}
		return result.toJSONString();
	}

	@Override
	public String delUserById(String userId) {
		JSONObject result = new JSONObject();
		userDao.delUser(Long.valueOf(userId));
		result.put("result", true);
		result.put("message", "删除成功");
		return result.toJSONString();
	}

	@Override
	public String checkUserName(String userName, String userId) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		Map<String,Object> params= new HashMap<>();
		params.put("userName", userName);
		params.put("id", userId);
		List list=userDao.getUsers(params);
		if(list !=null && list.size()>0){
			result.put("result", false);
			result.put("message", "存在同名用户");
		}else{
			result.put("result", true);
			result.put("message", "不存在同名用户");
		}
		return result.toJSONString();
	}

	@Override
	public Map<String, Object> getUserMapByName(String name) {
		return userDao.getUserInfoByName(name);
	}

	@Override
	public int setNotNew(String name) {
		return userDao.setNotNew(name);
	}
}
