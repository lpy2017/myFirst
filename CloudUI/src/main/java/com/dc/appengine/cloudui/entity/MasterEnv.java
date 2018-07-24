package com.dc.appengine.cloudui.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MasterEnv {
	public static String MASTER_REST;
	public static String CLOUDUI_SERVER_PORT;
	public static String server_resource;
	public static String server_descriptor;
	public static String loginPage;
	public static String pageFilter;
	public static String restFilter;
	public static String server_name;
	
	public MasterEnv(@Value("${masterRest}") String MASTER_REST,
			@Value("${server.port}") String CLOUDUI_SERVER_PORT,
			@Value("${server.resource}") String server_resource,
			@Value("${server.descriptor}") String server_descriptor,
			@Value("${loginPage}") String loginPage,
			@Value("${pageFilter}") String pageFilter,
			@Value("${restFilter}") String restFilter,
			@Value("${server.name}") String server_name) {
		MasterEnv.MASTER_REST = MASTER_REST;
		MasterEnv.server_resource = server_resource;
		MasterEnv.loginPage = loginPage;
		MasterEnv.server_descriptor = server_descriptor;
		MasterEnv.loginPage = loginPage;
		MasterEnv.pageFilter = pageFilter;
		MasterEnv.restFilter = restFilter;
		MasterEnv.server_name = server_name;
		
	}
}
