package com.dc.appengine.appsvn.utils.registry;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.alibaba.fastjson.JSON;

public class ResponseUtil {
	public static Map<String, Object> getResponseMap(Response response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", response.getStatus());
		map.put("header", JSON.toJSONString(response.getHeaders()));
		map.put("body", response.readEntity(String.class));
		return map;
	}
}
