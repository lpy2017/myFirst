package com.dc.appengine.appmaster.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.AppMaster;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppMaster.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestDemo {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	public void test() {
		String result = this.restTemplate.getForObject("/ws/cluster/test", String.class);
		System.out.println(result);
		assertNotNull(result);
		List<Map<String, Object>> clusters = JSON.parseObject(result, new TypeReference<List<Map<String,Object>>>(){});
		assertTrue(clusters.size() >= 0);
	}

}
