package com.dc.appengine.cloudui.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.master.service.impl.AdService;
import com.dc.appengine.cloudui.utils.ServiceBeanContext;
import com.dc.appengine.cloudui.ws.client.AdRestClient;
import com.dc.appengine.cloudui.ws.client.UserRestClient;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.dcits.Common.entity.User;

@Path("admins")
public class AdRestService {
	private static final Logger log = LoggerFactory.getLogger(AdRestService.class);
	@Autowired
	@Qualifier("adService")
	AdService adService;
	
	@Autowired
	@Qualifier("adRestClient")
	AdRestClient adRestClient;
	
	@GET
	@Path("listUsers") //运维人员列表
	public String listUsers(@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("key") String key,
			@QueryParam("sortName") @DefaultValue("") String sortName, 
		    @QueryParam("sortOrder") @DefaultValue("") String sortOrder){
		return WSRestClient.getMasterWebTarget().path("admin/listUsers")
				.queryParam("pageSize", pageSize).queryParam("pageNum", pageNum).
				queryParam("key", key).
				queryParam("sortName", sortName).
				queryParam("sortOrder", sortOrder).
				request().get(String.class);
	}
	
	//创建用户
	@POST
	@Path("createUser")
	public String createUser(
			@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("rolename") String rolename,
			@FormParam("pId") long pId,
			@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		return adService.createUser(username, password, rolename, pId,user.getName());
	}
	
	//删除运维用户
	@POST
	@Path("delUser")
	public String delUser(@FormParam("userName") String userName){
		System.out.println(userName);
		return adService.deleUser(userName);
	}
	
	//角色列表
	@GET
	@Path("listRoles") //运维人员列表
	public String listRoles(@QueryParam("sortName") @DefaultValue("") String sortName, 
		      @QueryParam("sortOrder") @DefaultValue("") String sortOrder){
		return adService.listRoles(sortName,sortOrder);
	}
	
	//新建角色
	@POST
	@Path("createRole")
	public String createRole(
			@FormParam("roleName") String roleName,@Context HttpServletRequest request){
			String returnMsg = adService.createRole(roleName);
			//==============添加审计start===================
			final User user = (User) request.getSession().getAttribute("user");
			final String threadRoleName = roleName;
			JSONObject jo = JSONObject.parseObject(returnMsg);
			final int operateResult = jo.getBoolean("result")?1:0;
			new Thread(new Runnable(){
				@Override
				public void run(){
					RestTemplate restUtil = new RestTemplate();
					MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
					message.add("userId", user.getName());
					message.add("resourceType", "权限管理");
					message.add("resourceName", threadRoleName);
					message.add("operateType", "add");
					message.add("operateResult", operateResult);
					message.add("detail", "新建角色 :"+threadRoleName);
				    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
				}
			}).start();
			//==============添加审计end=====================
			return returnMsg;
		
	}
	
	//判断用户是否存在
	@POST
	@Path("isExitUser")
	public String isExitUser(@FormParam("userName") String userName){
		return adService.isExitUser(userName);
	}
	
	//判断角色是否存在
	@POST
	@Path("isExitRole")
	public String isExitRole(@FormParam("roleName") String roleName){
		return adService.isExitRole(roleName);
	}
	
	//删除角色
	@POST
	@Path("delRole")
	public String delRole(@FormParam("roleName") String roleName,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		return adService.delRole(roleName,user.getName());
	}
	
	//查看角色的权限
	@POST
	@Path("policysOfRole")
	public String policysOfRole(@FormParam("roleName") String roleName){
		return adService.policysOfRole(roleName);
	}
	
	//保存角色的权限
	@POST
	@Path("savePolicysOfRole")
	public String savePolicysOfRole(@FormParam("roleId") int roleId,
			@FormParam("policyIds") String policyIds,@FormParam("menuIds") String menuIds,@FormParam("policyFlag") String policyFlag,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		return adRestClient.addPolicysOfRole(roleId+"", policyIds, menuIds,user.getName(),policyFlag);
	}
	
	//取消角色的权限
	@POST
	@Path("cancelPolicysOfRole")
	public String cancelPolicysOfRole(@FormParam("roleName") String roleName, 
			@FormParam("policyIds") String policyIds){
		return adService.cancelPolicysOfRole(roleName, policyIds);
	}
	
//	//添加角色的权限
//	@POST
//	@Path("addPolicysOfRole")
//	public String addPolicysOfRole(@FormParam("roleName") String roleName,
//			@FormParam("policyIds") String policyIds,
//			@FormParam("menuIds") String menuIds,@Context HttpServletRequest request){
//		User user = (User) request.getSession().getAttribute("user");
//		return adRestClient.addPolicysOfRole(roleName, policyIds, menuIds,user.getName());
//	}
	
	@POST
	@Path("getPolicysOfRole")
	public String getPolicysOfRole(@FormParam("roleId") int roleId){
		return adService.getPolicysOfRole(roleId);
	}
	
	//权限列表
	@GET
	@Path("listPolicys")
	public String listPolicys(@QueryParam("sortName") @DefaultValue("") String sortName, 
		      @QueryParam("sortOrder") @DefaultValue("") String sortOrder){
		return adService.listPolicys(sortName,sortOrder);
	}
	
	//删除权限
	@POST
	@Path("delPolicy")
	public String delPolicys(@FormParam("policyId") String policyId){
		log.debug("policyId:  " + policyId);
		return adService.delPolicys(policyId);
	}
	
	//添加权限
	@POST
	@Path("createPolicy")
	public String createPolicy(@FormParam("policyInfos") String policyInfos){
		
		return adService.createPolicy(policyInfos);
	}
	
	//修改权限
	@POST
	@Path("modifyPolicy")
	public String modifyPolicy(@FormParam("policyInfos") String policyInfos){
		return adService.modifyPolicy(policyInfos);
	}
	
	//获取运维下属的所有用户
	@POST
	@Path("usersOfCluster")
	public String usersOfCluster(@PathParam("userId") String userId){
		return adService.userOfCluster(userId);
	}
	
	//关联用户到集群
	@POST
	@Path("relationUser")
	public String relationUser(@FormParam("clusterId") String clusterId,
			@FormParam("userId") String userId){
		return adService.relationUser(clusterId, userId);
	}
	
	//查询用户的角色
	@POST
	@Path("getRoleOfUser")
	public String getRoleOfUser(@FormParam("userId") String userId){
		return adService.getRoleOfUser(userId);
	}
	
	//查看用户是否有操作按钮级别菜单的权限
	@GET
	@Path("isAccess")
	public String isAccess(@QueryParam("userId") String userId ,
			@QueryParam("menuId") String menuId){
		return adRestClient.isAccess(userId, menuId);
	}
	
	@POST
	@Path("addPolicyMenu")
	public String addPolicyMenu(@FormParam("policyMenuInfo") String policyMenuInfo){
		return adRestClient.addPolicyMenu(policyMenuInfo);
	}
	
	//分页查询按钮列表
	@GET
	@Path("findMenuByPage")
	public String findMenuByPage(@QueryParam("pageSize") int pageSize, @QueryParam("pageNum") int pageNum){
		return adRestClient.findMenuByPage(pageSize, pageNum);
	}
	
	//分页查询角色的按钮列表
	@GET
	@Path("findMenuByPageOfRole")
	public String findMenuByPageOfRole(@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("roleId") String roleId){
		return adRestClient.findMenuByPageOfRole(pageSize, pageNum, roleId);
	}
	
	//添加菜单（一级或二级）
	@POST
	@Path("createPolicyChild")
	public String createPolicyChild(@FormParam("policyInfos") String policyInfos,
			@FormParam("policytext") String policytext,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		Form form = new Form();
		form.param("policyInfos", policyInfos);
		form.param("policytext", policytext);
		form.param("userId", user.getName());
		
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("admin").path("createPolicyChild").request()
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

	}
	
	//删除二级菜单
	@POST
	@Path("delPolicyChild")
	public String delPolicyChild(@FormParam("policyChildId") int policyChildId,
			@FormParam("pId") int pId,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		Form form = new Form();
		form.param("policyChildId", policyChildId+"");
		form.param("pId", pId+"");
		form.param("userId", user.getName());
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("admin").path("delPolicyChild").request()
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

	}
	
	//更新二级菜单
	@POST
	@Path("modifyPolicyChild")
	public String modifyPolicyChild(@FormParam("policyInfos") String policyInfos,@Context HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		Form form = new Form();
		form.param("policyInfos", policyInfos);
		form.param("userId", user.getName());
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("admin").path("modifyPolicyChild").request()
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

	}
	
	//所有的一级菜单
	@GET
	@Path("listPolicysTemp")
	public String getAllPolicysList(){
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("admin/listPolicysTemp").request()
		.get(String.class);
	}
	
	@POST
	@Path("getRootPolicysOfRole")
	public String getRootPolicysOfRole(@FormParam("roleId") int roleId){
		Form form = new Form();
		form.param("roleId", roleId+"");
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("admin").path("getRootPolicysOfRole").request()
				.post(Entity.entity(form,MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);

	}
}
