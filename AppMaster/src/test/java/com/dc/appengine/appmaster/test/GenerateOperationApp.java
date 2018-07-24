package com.dc.appengine.appmaster.test;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.entity.OperationApp;
import com.dc.appengine.appmaster.entity.OperationResource;

public class GenerateOperationApp {
	public static void main(String[] args) {
		OperationApp o = new OperationApp();
		OperationResource resource = new OperationResource();
		resource.setAccessPort(10001);
		resource.setCmd("task");
		resource.setImage("ftp://XXXXXXXXXX");
		o.setAppName("yangze");
		o.setDescription("test");
		o.setFromTemplate(false);
		o.setNetModel("host");
		o.setResourceInfo(resource);
		System.out.println(JSON.toJSONString(o));
	}
}
