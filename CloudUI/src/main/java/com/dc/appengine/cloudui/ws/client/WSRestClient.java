package com.dc.appengine.cloudui.ws.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.dc.appengine.cloudui.entity.ClusterENV;
import com.dc.appengine.cloudui.entity.FrameENV;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.filter.ExceptionResponseFilter;

public class WSRestClient {

	private static WebTarget masterWebTarget;
	private static WebTarget svnWebTarget;
	private static WebTarget adapterWebTarget;
	private static WebTarget monitorWebTarget;
	private static WebTarget devPortalWebTarget;
	private static WebTarget smartcloudWebTarget;
	private static WebTarget ciWebTarget;
	private static WebTarget frameWebTarget;

	public static WebTarget getMasterWebTarget() {
		if (masterWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			masterWebTarget = client
					.target(MasterEnv.MASTER_REST);
		}
		return masterWebTarget;
	}

	public static WebTarget getAdapterWebTarget() {
		if (adapterWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			adapterWebTarget = client.target(ClusterENV.ADAPTER_URL);
		}
		return adapterWebTarget;
	}

	/*public static WebTarget getSvnWebTarget() {
		if (svnWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(MultiPartFeature.class);
			client.register(ExceptionResponseFilter.class);
			svnWebTarget = client.target(ConfigHelper.getValue("svnRest"));
		}
		return svnWebTarget;
	}

	
	public static WebTarget getDevPortalTarget() {
		if (devPortalWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			devPortalWebTarget = client.target(ConfigHelper
					.getValue("devportalRest"));
		}
		return devPortalWebTarget;
	}


	public static WebTarget getSmartCloudTarget() {
		if (smartcloudWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			smartcloudWebTarget = client.target(ConfigHelper
					.getValue("smartcloudRest"));
		}
		return smartcloudWebTarget;
	}

	public static WebTarget getCiWebTarget() {
		if (ciWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			ciWebTarget = client.target(ConfigHelper
					.getValue("ciRest"));
		}
		return ciWebTarget;
	}*/
	public static WebTarget getFrameWebTarget() {
		if (frameWebTarget == null) {
			Client client = ClientBuilder.newClient();
			client.register(ExceptionResponseFilter.class);
			frameWebTarget = client.target(FrameENV.FRAME_URL);
//			frameWebTarget = client.target("http://10.1.108.33:8091/frame");
		}
		return frameWebTarget;
	}
}