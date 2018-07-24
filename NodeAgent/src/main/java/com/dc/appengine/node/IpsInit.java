package com.dc.appengine.node;

import com.dc.appengine.node.cache.IPsCache;
import com.dc.appengine.node.configuration.model.NodeProperties;


public class IpsInit {
	private static IpsInit _instatnce = new IpsInit();

	public static IpsInit getInstance() {
		return _instatnce;
	}

	public void initip() {
//		IPsCache.getInstance().reset();
//		int cunt = NodeProperties.getIpEnd()
//				- NodeProperties.getIpStart();
//		for (int index = 0; index < cunt; index++) {
//			IPsCache.getInstance().set(index, false);
//		}
	}
}
