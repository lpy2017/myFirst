package com.dc.appengine.cloudui.ws.client;

import org.springframework.stereotype.Service;

@Service("userRestClient")
public class UserRestClient {
	public String login(String username, String password) {
		String ret = WSRestClient.getMasterWebTarget().path("user")
				.path("login").queryParam("userName", username)
				.queryParam("pwd", password).request().get(String.class);
		return ret;
	}

	public long getUserId(String userName) {
		String res = WSRestClient.getMasterWebTarget().path("user")
				.path("getUserId").queryParam("userName", userName).request()
				.get(String.class);
		return Long.parseLong(res);
	}

	public String signin(String username, String password) {
		String ret = WSRestClient.getMasterWebTarget().path("user").path("add")
				.queryParam("userName", username)
				.queryParam("password", password).request().get(String.class);
		return ret;
	}

	public String getPolicies(String userName) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("user").path("getPolicies")
				.queryParam("userName", userName).request().get(String.class);
	}

	public String isAdmin(Long id) {
		// TODO Auto-generated method stub
		return WSRestClient.getMasterWebTarget().path("admin").path("isAdmin")
				.queryParam("userId", id+"").request().get(String.class);
	}
}