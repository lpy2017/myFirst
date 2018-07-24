package com.dc.appengine.node.ws;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/ws/*")
public class MyApplication extends ResourceConfig {
	public MyApplication(){
		register(ServerExceptionResponseFilter.class);
		register(ConstraintViolationExceptionMapper.class);
	}
}
