package com.dc.appengine.adapter.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.dc.appengine.adapter.entity.ENV;

public class WSClient {
	
	private static WebTarget masterWebTarget = null;
//	private static WebTarget svnWebTarget = null;
	private static Map<String, WebTarget> nodeWebTargets = new HashMap<String, WebTarget>();
	
	public static WebTarget getMasterWebTarget() {
		if (masterWebTarget == null) {
			Client client = ClientBuilder.newClient();
			masterWebTarget = client.target(ENV.MASTER_URL);
		}
		return masterWebTarget;
	}
	
	public static WebTarget getNodeWebTarget(String nodeIP) {
		if (!nodeWebTargets.containsKey(nodeIP)) {
			Client client = ClientBuilder.newClient();
			nodeWebTargets.put(nodeIP, client.target(ENV.NODE_URL.replaceFirst("127\\.0\\.0\\.1", nodeIP)));
		}
		return nodeWebTargets.get(nodeIP);
	}
	
//	public static WebTarget getSVNWebTarget() {
//		if (svnWebTarget == null) {
//			Client client = ClientBuilder.newClient();
//			svnWebTarget = client.target(ENV.SVN_URL);
//		}
//		return svnWebTarget;
//	}

}
