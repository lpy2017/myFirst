package com.dc.appengine.cloudui.filter;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rest调用过程异常处理
 * 
 * @author xuxyc
 * 
 */
public class ExceptionResponseFilter implements ClientResponseFilter {
	private static Logger log = LoggerFactory.getLogger(ExceptionResponseFilter.class);

	@Override
	public void filter(ClientRequestContext arg0, ClientResponseContext arg1) throws IOException {
		if (arg1.getStatus() / 100 != 2) {
			if (log.isDebugEnabled()) {
				log.debug(arg0.getMethod() + " " + arg0.getUri() + " with status: " + arg1.getStatus() + ", "
						+ arg1.getStatusInfo());
			}
		}
	}

}
