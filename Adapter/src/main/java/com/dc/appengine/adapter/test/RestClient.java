package com.dc.appengine.adapter.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

public class RestClient {
	
	private RestTemplate restTemplate;
	
	public void test() {
		List<Map<String, Object>> json = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("result", true);
		map.put("key", 123);
		map.put("k1", "v1");
		json.add(map);
		restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<List<Map<String, Object>>>(json, headers);
//		HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(json), headers);
//		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		String str = restTemplate.postForObject("http://localhost:8080/demo/post", entity, String.class);
		System.out.println(str);
	}
	
	public void test1() {
		restTemplate = new RestTemplate();
		String str = restTemplate.getForObject("http://localhost:8080/demo/get", String.class);
		System.out.println(str);
	}
	
	public static void main(String[] args) {
		new RestClient().test();
	}

}
