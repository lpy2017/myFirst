package com.dc.appengine.node.ws;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerExceptionResponseFilter implements ContainerResponseFilter{
	private static Logger log = LoggerFactory.getLogger(ServerExceptionResponseFilter.class);
	
	@Override
	public void filter(ContainerRequestContext arg0, ContainerResponseContext arg1) 
			throws IOException {
		if(arg1.getStatus()/100 != 2){
			if(log.isDebugEnabled()){
				log.debug(arg0.getMethod()+" "
						+ arg0.getUriInfo().getRequestUri()+": "
						+ arg1.getStatus()+", "+arg1.getStatusInfo());
			}
		}
	}

}
