package com.dc.appengine.quartz.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MasterEnv {
	public static String MASTER_REST;
	
	public MasterEnv(@Value("${masterRest}") String MASTER_REST) {
		MasterEnv.MASTER_REST = MASTER_REST;
		
	}
}
