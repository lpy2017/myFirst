package com.dc.appengine.adapter.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ENV {

	public static String THREAD_POOL_SIZE;
	public static String MASTER_URL;
	public static String NODE_URL;

	public ENV(@Value("${thread.pool.size}") String THREAD_POOL_SIZE,
			@Value("${paas.master.rest.url}") String MASTER_URL,
			@Value("${paas.node.rest.url}") String NODE_URL) {
		ENV.THREAD_POOL_SIZE = THREAD_POOL_SIZE;
		ENV.MASTER_URL = MASTER_URL;
		ENV.NODE_URL = NODE_URL;
	}

}
