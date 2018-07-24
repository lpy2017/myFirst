package com.dc.appengine.adapter.job;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.adapter.util.WSClient;

public class NoticeMasterUpdateNodeJob implements Runnable {
	
	private String ip;
	private String op;
	private Logger log = LoggerFactory.getLogger(NoticeMasterUpdateNodeJob.class);
	
	public NoticeMasterUpdateNodeJob(String ip, String op) {
		this.ip = ip;
		this.op = op;
	}
	
	@Override
	public void run() {
		WebTarget target = WSClient.getMasterWebTarget();
		Form form = new Form();
		form.param("ip", ip);
		form.param("op", op);
		log.debug("notice master {} node: {}", op, ip);
		String result = target.path("node").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		log.debug("notice master result {}", result);
	}

}
