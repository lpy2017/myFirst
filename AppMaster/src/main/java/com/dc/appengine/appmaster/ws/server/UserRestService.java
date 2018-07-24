package com.dc.appengine.appmaster.ws.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Policy;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IRoleService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/user")
public class UserRestService {
	

	@Resource
	IUserService userService;
	
	@Resource
	IRoleService roleService;
	
	@Resource
	IAudit auditService;
	
	private static final Logger log = LoggerFactory.getLogger(UserRestService.class);
	
	/**
	 * 进行用户信息校验
	 * @param userName
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "login",method = RequestMethod.GET)
	@ResponseBody
	public String login(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd) {
		boolean isLogin = userService.authenticate(userName, pwd);
		if(isLogin){
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run() {
					auditService.save(new AuditEntity(userName, ResourceCode.USERMANAGER, userName, ResourceCode.LOGIN, 1, null));
				}
			});
			//==============添加审计end=====================
		}
		return String.valueOf(isLogin);
	}
	

	@RequestMapping(value = "getPolicies",method = RequestMethod.GET)
	@ResponseBody
	public String getPolicies(@RequestParam("userName") String userName){
		System.out.println(userName);
		List<Policy> tempPolicies=userService.getPoliciesOfUser(userName);
		if (tempPolicies!=null) {
			//将tempPolicies转换成jsonStrings
			return JSON.toJSONString(tempPolicies);
		}else {
			return null;
		}
	}
	
	/**
	 * 登录认证，认证通过返回true，否则返回false
	 * @param userName
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "authenticate",method = RequestMethod.GET)
	@ResponseBody
	public String authenticate(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd) {
		return String.valueOf(userService.authenticate(userName, pwd));
		//return "true";
	}
	
	/**
	 * 获取用户id
	 * @param userName
	 * @return
	 */
	@RequestMapping(value = "getUserId",method = RequestMethod.GET)
	@ResponseBody
	public String getUserId(@RequestParam("userName") String userName) {
		return String.valueOf(userService.getUserId(userName));
		//return "1";
	}
	

	/**
	 * 添加账户
	 * @param userName
	 * @param password
	 * @return
	 */

	@RequestMapping(value = "add",method = RequestMethod.GET)
	@ResponseBody
	public String add(@RequestParam("userName") String userName,
			@RequestParam("password") String password) {
		int rId = 2;
		long pId = 0;
		if(pId == 0){
			long parentId = userService.getUserId("admin");
			return userService.add(userName, password, rId, parentId);
		}
		
		return userService.add(userName, password, rId, pId);
	}
	

	@RequestMapping(value = "getUserInfoByName",method = RequestMethod.GET)
	@ResponseBody
	public String getUserInfoByName(@RequestParam("userName") String userName){
		if(userService.getUserInfoByName(userName) != null){
			return JSON.toJSONString(userService.getUserInfoByName(userName));
		}
		return "{\"result\":false,\"message\":\"" + "用户不存在" + "\"}";
	}
	
	@RequestMapping(value = "getResources",method = RequestMethod.GET)
	@ResponseBody
	public String getAccesses(@RequestParam("userId") long userId) {
		return String.valueOf(userService.getResources(userId));
	}
	
	@RequestMapping(value = "getUserInfoBySession", method = RequestMethod.GET)
	public Object getUserInfoBySession(HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			return userService.getUserMapByName(user.getName());
		} catch (Exception e) {
			log.error("", e);
		}
		return Collections.emptyMap();
	}
	
	@RequestMapping(value = "setNotNew", method = RequestMethod.PUT)
	public Object setNotNew(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			User user = (User) request.getSession().getAttribute("user");
			int i = userService.setNotNew(user.getName());
			if (i == 1) {
				result.put("result", true);
			} else {
				result.put("result", false);
				result.put("info", "更新数据库失败");
			}
		} catch (Exception e) {
			log.error("", e);
			result.put("result", false);
			result.put("info", e.getMessage());
		}
		return result;
	}
}
