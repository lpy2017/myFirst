package com.dc.appengine.node.preloader.impl;

import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.preloader.Preloadable;
import com.dc.appengine.plugins.service.impl.PluginService;

public class PluginMannagerPreloader implements Preloadable {

	@Override
	public void preload() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ftp.url", NodeProperties.ftpUrl);
		map.put("ftp.user", NodeProperties.ftpUser);
		map.put("ftp.pwd", NodeProperties.ftpPwd);
		map.put("ftp.port", NodeProperties.ftpPort);
		map.put("ftp.uploadDir", NodeProperties.ftpUploadDir);
		map.put("ftp.tmpDir", System.getProperty("com.dc.install_path") + "/"
				+ NodeProperties.ftpTmpDir);
		map.put("ftp.timeOut", NodeProperties.ftpTimeOut);
		map.put("upload.mode", NodeProperties.uploadMode);

		map.put("shellExecutor", NodeProperties.shellExecutor);
		map.put("masterRest", NodeProperties.masterRest);
		map.put("frameRest", NodeProperties.frameRest);
		
		map.put("sender", NodeProperties.sender);
		map.put("password", NodeProperties.password);
		map.put("smtpHost", NodeProperties.smtpHost);
		map.put("smtpPort", NodeProperties.smtpPort);
		
		// map.put("hostBasePath", NodeProperties.hostBasePath);
		// map.put("sshAuthMethod", NodeProperties.sshAuthMethod);
		// map.put("keyLocation", NodeProperties.keyLocation);
		// map.put("hostUser", NodeProperties.hostUser);
		// map.put("hostPassword", NodeProperties.hostPassword);
		PluginService.getInstance().load("node", map);

		// String command = "/home/test.sh";
		// String params = "p1 p2 p3 p4";
		// String result = CommandResult.getResult(new EchoAnalyseer(),
		// CommandWaitExecutor.class,
		// command, false, params, params);
		// System.out.println(result);
	}
}
