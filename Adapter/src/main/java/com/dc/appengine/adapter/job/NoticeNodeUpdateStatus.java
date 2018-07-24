package com.dc.appengine.adapter.job;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.adapter.util.WSClient;

public class NoticeNodeUpdateStatus implements Runnable {
	
	private String status;
	private String ip;
	private Logger log = LoggerFactory.getLogger(NoticeNodeUpdateStatus.class);
	
	public NoticeNodeUpdateStatus(String ip, String status) {
		this.status = status;
		this.ip = ip;
	}

	@Override
	public void run() {
		WebTarget target = WSClient.getNodeWebTarget(ip);
		Form form = new Form();
		form.param("status", status);
		log.debug("notice node {} udpate status {}", ip, status);
		String result = target.path("updatestatus").request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		log.debug("notice node {} update status result {}", ip, result);
	}

}
