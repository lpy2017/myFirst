package com.dc.appengine.appsvn.ws.client;
import java.util.Date;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.dc.appengine.appsvn.utils.ConfigHelper;

public class MasterClient extends BaseService {
	public String getUserId(String userName) {
		Client client = ClientBuilder.newClient();
		String masterUrl=ConfigHelper.getValue("masterRest");
		WebTarget target = client.target(masterUrl).path("user").path(
				"getUserId").queryParam("userName", userName);
		String s = this.addAuthHeader(target.request(), new Date(),
				ConfigHelper.getValue("restUser"),
				ConfigHelper.getValue("restPwd")).get(String.class);
		return s;
	}
	public String getSonIds(String userId){
		Client client = ClientBuilder.newClient();
		String masterUrl=ConfigHelper.getValue("masterRest");
		WebTarget target = client.target(masterUrl).path("admin/getSonsOfUser").queryParam("userId", userId);
		String s = this.addAuthHeader(target.request(), new Date(),
				ConfigHelper.getValue("restUser"),
				ConfigHelper.getValue("restPwd")).get(String.class);
		return s;
		
	}
}
