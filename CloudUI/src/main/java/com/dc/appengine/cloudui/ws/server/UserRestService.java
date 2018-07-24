package com.dc.appengine.cloudui.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.ws.client.UserRestClient;
import com.dcits.Common.entity.User;

@Path("user")
public class UserRestService {
	private static final Logger log = LoggerFactory
			.getLogger(UserRestService.class);
//	private UserRestClient userRestClient = (UserRestClient) ServiceBeanContext
//			.getInstance().getBean("userRestClient");
	@Autowired
	@Qualifier("userRestClient")
	UserRestClient userRestClient;
	//添加
	@GET
	@Path("getPolicies")
	public String getPolicies(@QueryParam("userName") String userName){
		return userRestClient.getPolicies(userName);
	}
	
	@Path("logout")
	@GET
	public String logout(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		//==============添加审计start===================
		User user = (User) request.getSession().getAttribute("user");
		final String userId = user.getName();
		new Thread(new Runnable(){
			@Override
			public void run(){
				RestTemplate restUtil = new RestTemplate();
				MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
				message.add("userId", userId);
				message.add("resourceType", "用户");
				message.add("resourceName", userId);
				message.add("operateType", "logout");
				message.add("operateResult", 1);
				message.add("detail", "注销登录 :"+userId);
			    restUtil.postForObject(MasterEnv.MASTER_REST+"/audit/save", message, String.class);
			}
		}).start();
		//==============添加审计end=====================
		request.getSession().invalidate();
		return "true";
	}

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String login(@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@FormParam("username") String username,
			@FormParam("password") String password) {
		// String userName = request.getParameter("username");
		// String pwd = request.getParameter("password");
		String b = userRestClient.login(username, password);
		String result = null;
		if ("true".equals(b)) {
			long userId = userRestClient.getUserId(username);
			User user = new User();
			user.setId(userId);
			user.setName(username);
			request.getSession().setAttribute("user", user);
			log.debug("user: " + username + "login success ");
			result = "true";
		} else {
			log.error("user: " + username + "login failed");
			result = "false";
			// result = "登陆失败，用户信息错误或无登录权限！";
		}
		return result;
	}

	@Path("userInfo")
	@POST
	public String getUserInfor(@Context HttpServletRequest request,
			@Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			// re
			return null;
		} else {
			String flag = userRestClient.isAdmin(user.getId());
			if(flag.equals("true")){
				return "{\"result\":true,\"id\":\"" + user.getId() + "\",\"name\":\"" + user.getName() + "\"}";
			}
			return "{\"result\":false,\"id\":\"" + user.getId() + "\",\"name\":\"" + user.getName() + "\"}";
		}
	}

	@Path("signin")
	@POST
	public String signin(@FormParam("username") String username,
			@FormParam("password") String password) {
		String res = userRestClient.signin(username, password);
		if ("false".equals(res)) {
			log.error("user: " + username + "signin failed : user name exist ");
		} else {
			log.debug("user: " + username + "signin success ");
		}
		return res;
	}
}