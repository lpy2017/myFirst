package com.dc.appengine.appmaster.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemENV {
	
	public static int sessionTimeout;
	public static int tryTimeOut=-1;
	public static String uploadFileTempPath;
	
	public SystemENV(@Value("${sessionTimeout:1800}") int sessionTimeout,
			@Value("${tryOutTime:60}") int tryOutTime,
			@Value("${uploadFileTempPath:./}") String uploadFileTempPath) {
		SystemENV.sessionTimeout = sessionTimeout;
		SystemENV.uploadFileTempPath = uploadFileTempPath;
		SystemENV.tryTimeOut = tryOutTime;//单位天，默认值是60天
		if(SystemENV.tryTimeOut > 60) {//max tryTimeOut
			SystemENV.tryTimeOut=60;
		}
	}

}
