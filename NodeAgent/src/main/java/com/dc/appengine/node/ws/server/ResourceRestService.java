//package com.dc.appengine.node.ws.server;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.QueryParam;
//
//import com.dc.appengine.node.NodeEnv;
//import com.dc.appengine.node.cache.PortsCache;
//
//@Path("resource")
//public class ResourceRestService {
//	/**
//	 * 申请动态端口
//	 * 
//	 * @return
//	 */
//	@GET
//	@Path("port/apply")
//	public String applyForDynamicPort() {
//		int startport = Integer.valueOf(NodeEnv.getInstance()
//				.getNodeEnvDefinition().getPortStart());
//		int outPortStr = PortsCache.getInstance().getFirstUnusedPort();
//		if (outPortStr < startport) {
//			PortsCache.getInstance().revertPort(outPortStr);
//		}
//		return outPortStr + "";
//
//	}
//
//	@GET
//	@Path("port/revert")
//	public String revertForDynamicPort(@QueryParam("port") int port) {
//		PortsCache.getInstance().revertPort(port);
//		return "ok";
//
//	}
//}
