package com.dc.appengine.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(basePackages="com.dc")
public class StartSVN  extends SpringBootServletInitializer{
	
	public static void main(String[] args) throws Exception{
		SpringApplication.run(StartSVN.class, args);
	}
}
