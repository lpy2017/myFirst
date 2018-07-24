package com.dc.appengine.quartz.utils;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class Audit2Master implements Runnable{
	
	public String userId;
	public String resourceType;
	public String resourceName;
	public String operateType;
	public int operateResult;
	public String detail;

	public Audit2Master(String userId, String resourceType, String resourceName,
			String operateType, int operateResult, String detail) {
		this.userId = userId;
		this.resourceType = resourceType;
		this.resourceName = resourceName;
		this.operateType = operateType;
		this.operateResult = operateResult;
		this.detail = detail;
	}

	@Override
	public void run() {
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, Object> message = new LinkedMultiValueMap<String, Object>();
		message.add("userId", userId);
		message.add("resourceType", resourceType);
		message.add("resourceName", resourceName);
		message.add("operateType", operateType);
		message.add("operateResult", operateResult);
		message.add("detail", detail);
		restUtil.postForObject(MasterEnv.MASTER_REST + "/audit/save",
				message, String.class);
	}
}
