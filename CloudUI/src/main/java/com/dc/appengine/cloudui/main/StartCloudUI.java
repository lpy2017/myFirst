package com.dc.appengine.cloudui.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="com.dc")
public class StartCloudUI extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(StartCloudUI.class, args);
	}
}