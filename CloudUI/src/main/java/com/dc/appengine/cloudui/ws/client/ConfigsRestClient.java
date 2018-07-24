package com.dc.appengine.cloudui.ws.client;

import org.springframework.stereotype.Service;

@Service("configsRestClient")
public class ConfigsRestClient {
	
	public String getConfigByPage(String userId,int pageSize,int pageNum,String keyword){
		String json = WSRestClient.getMasterWebTarget()
				.path("configs/configlist")
				.queryParam("userId", userId)
				.queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.queryParam("keyword", keyword)
				.request().get(String.class);
		return json;
	}

	public String getConfiginfos(String userId, int appId) {
		String json = WSRestClient.getMasterWebTarget()
				.path("configs/configinfos")
				.queryParam("userId", userId)
				.queryParam("appId", appId)
				.request().get(String.class);
		return json;
	}
}
