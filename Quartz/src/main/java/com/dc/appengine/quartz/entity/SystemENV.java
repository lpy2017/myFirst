package com.dc.appengine.quartz.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemENV {
	
	public static int sessionTimeout;
	
	public SystemENV(@Value("${sessionTimeout:1800}") int sessionTimeout) {
		SystemENV.sessionTimeout = sessionTimeout;
	}

}
