package com.dc.appengine.appmaster.ws.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.dc.appengine.appmaster.filter.ClientExceptionResponseFilter;
import com.dc.appengine.appmaster.utils.ConfigHelper;

public class AdapterWSRestClient {
	
	private static WebTarget webResource;
	
	public static WebTarget getWebResource(){
		if( webResource == null ){
			Client client = ClientBuilder.newClient();
			client.register(ClientExceptionResponseFilter.class);
			String path = ConfigHelper.getValue( "adapterUrl" );
			webResource = client.target( path );
		}
		return webResource;
	}
}
