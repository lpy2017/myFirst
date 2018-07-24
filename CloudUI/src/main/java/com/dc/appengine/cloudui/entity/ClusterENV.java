package com.dc.appengine.cloudui.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClusterENV {
	
	public static String NODE_INSTALL_SHELL;
	public static String ADAPTER_URL;
	
	public ClusterENV(@Value("${node.install.shell.url}") String NODE_INSTALL_SHELL,
			@Value("${paas.adapter.url}") String ADAPTER_URL) {
		ClusterENV.NODE_INSTALL_SHELL = NODE_INSTALL_SHELL;
		ClusterENV.ADAPTER_URL = ADAPTER_URL;
	}

}
