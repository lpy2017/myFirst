package com.dc.appengine.appsvn.utils.registry;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.alibaba.fastjson.JSON;

public class TokenUtil {
	private WebTarget webTarget;
	public static String service = "Docker registry";

	public TokenUtil(String domain, String port) {
		webTarget = ClientBuilder.newClient().target(
				"http://" + domain + ":" + port + "/AuthServer/ws/auth/apply");
	}

	public String getToken(String account, String password, String[] scope) {
		WebTarget target = webTarget.queryParam("account", account).queryParam(
				"service", service);
		String authInfo = "MD5 " + account + ":" + password;
		for (String s : scope) {
			target = target.queryParam("scope", s);
		}
		Builder builder = target.request().header("Authorization", authInfo);
		Response response = builder.get();
		String token = response.readEntity(String.class);
		token = JSON.parseObject(token).getString("token");
		return token;
	}
}
