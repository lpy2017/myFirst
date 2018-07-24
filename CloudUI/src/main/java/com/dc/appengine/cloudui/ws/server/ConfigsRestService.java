package com.dc.appengine.cloudui.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dcits.Common.entity.User;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.ConfigsRestClient;

@Path("configs")
public class ConfigsRestService {

	
	@Autowired
	@Qualifier("configsRestClient")
	ConfigsRestClient configsRestClient;
	
	@GET
	@Path("list")
	public String configListByPage(@Context HttpServletRequest request,@QueryParam("pageSize") int pageSize, @QueryParam("pageNum") int pageNum,@QueryParam("keyword") String keyword){
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg",
					"can not list images if not logged in");
		}
		User user = (User) u;
		String userId = user.getId() + "";
		if("1".equals(userId)){
			userId="";
		}
		// 获取多个id
		return configsRestClient.getConfigByPage( userId,pageSize, pageNum, keyword);
	}
	
	@GET
	@Path("configinfo")
	public String configinfo(@Context HttpServletRequest request,@QueryParam("appId") int appId){
		Object u = request.getSession().getAttribute("user");
		if (u == null) {
			return MessageHelper.wrap("result", false, "msg",
					"can not list images if not logged in");
		}
		User user = (User) u;
		String userId = user.getId() + "";
		if("1".equals(userId)){
			userId="";
		}
		// 获取多个id
		return configsRestClient.getConfiginfos( userId,appId);
	}
}
