//package com.dc.appengine.node.ws.server;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.FormParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.dc.appengine.node.NodeConstant;
//import com.dc.appengine.node.NodeEnv;
//import com.dc.appengine.node.StartNode;
//import com.dc.appengine.node.command.Analytic;
//import com.dc.appengine.node.command.CommandGenerator;
//import com.dc.appengine.node.command.Commands;
//import com.dc.appengine.node.command.analyser.AbstractAnalyser;
//import com.dc.appengine.node.command.analyser.ErrorAnalyser;
//import com.dc.appengine.node.command.analyser.LastResultAnalyser;
//import com.dc.appengine.node.command.executor.CommandWaitExecutor;
//import com.dc.appengine.node.ws.client.AdapterClient;
//
//@Path("adapter")
//public class NodeRestService {
//
//	private static Logger log = LoggerFactory.getLogger(NodeRestService.class);
//	private Map<String, Object> result = new HashMap<String, Object>();
//	private AbstractAnalyser<String> analyser = new LastResultAnalyser();
//	private CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
//	private Analytic<String> errorAnaly = new ErrorAnalyser();
//	private AdapterClient adapterClient = new AdapterClient();
//
//	/**
//	 * adapter通知node重新注册
//	 * 参数：cluster_id=uuid&name=node_001&vm_id=uuid&iaas_id=uuid
//	 * 返回：json{"result": true }或{"result": false,"info": "fail info"}
//	 */
//	@POST
//	@Path("reregister")
//	@Consumes("application/x-www-form-urlencoded")
//	public String reRegister(@FormParam("cluster_id") String clusterId,
//			@FormParam("name") String name, @FormParam("vm_id") String vmId,
//			@FormParam("iaas_id") String iaasId) {
//		NodeEnv.getInstance().getNodeEnvDefinition().setRegister(false);
//		log.debug("*********Accept from adapter: Redo register.*******");
//		Map<String, Object> result = new HashMap<String, Object>();
//
//		if (vmId != null) {
//			result.put("result", true);
//		} else {
//			result.put("result", false);
//			result.put("info", "set param failed");
//		}
//		StartNode.reRegister();
//		return JSON.toJSONString(result);
//	}
//
//	/**
//	 * adapter通知node与指定主机创建或者删除隧道 node_ip：要建立隧道的远程node的IP, bridge_ip：本地网桥的cidr地址
//	 * op：create或者delete
//	 */
//	@POST
//	@Path("tunnel")
//	@Consumes("application/x-www-form-urlencoded")
//	public String opTunnel(@FormParam("node_ip") String remote_ip,
//			@FormParam("bridge_ip") String bridge_ip, @FormParam("op") String op) {
//
//		String node_ip = NodeEnv.getInstance().getNodeEnvDefinition()
//				.getNodeip();
//		if (remote_ip.equals(node_ip)) {
//			log.error("remote ip=" + remote_ip
//					+ ", cannot create tunnel with node itself.");
//			result.put("result", false);
//			result.put("info", "error remote node ip");
//			return JSON.toJSONString(result);
//		}
//
//		executor.setAnalytic(analyser);
//		executor.setErrAnalytic(errorAnaly);
//		String command = CommandGenerator.getInstance().generate(
//				Commands.getInstance().get("ovs-trunk"), false, op, remote_ip);
//		log.debug("Create tunnel, command will executed: [" + command + "]");
//		executor.exec(command);
//		String reString = analyser.getResult();
//		String errorResult = errorAnaly.getResult();
//		log.info("vxlan " + op + " result is: [" + reString
//				+ "], errorResult is: [" + errorResult + "]");
//
//		if (reString != null && "OK".equals(reString)) {
//			result.put("result", true);
//		} else {
//			result.put("result", false);
//			result.put("info", reString);
//		}
//		return JSON.toJSONString(result);
//	}
//
//	/**
//	 * status=master status：node归谁管理，取值为adapter或者master
//	 * 
//	 * @return
//	 */
//	@PUT
//	@Path("updatestatus")
//	@Consumes("application/x-www-form-urlencoded")
//	public String updateStatus(@FormParam("status") String status) {
//		log.debug("Received message from adapter ,change node's status :"
//				+ status);
//		Map<String, Object> result = new HashMap<String, Object>();
//		boolean isNewNode = NodeEnv.getInstance().getNodeEnvDefinition()
//				.isNewNode();
//		if (!isNewNode && "adapter".equals(status)) {
//			try {
//				StartNode.start();
//				Map<String, String> nodeConfMap = new HashMap<String, String>();
//				nodeConfMap.put("isNewNode", "true");
//				adapterClient.setFile(nodeConfMap, NodeConstant.NODE_CONF);
//				NodeEnv.getInstance().getNodeEnvDefinition().setNewNode(true);
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//			}
//
//		}
//		isNewNode = NodeEnv.getInstance().getNodeEnvDefinition().isNewNode();
//		if (status.equals(isNewNode)) {
//			log.debug("node status change successfuly");
//		} else {
//			log.error("node status change failed");
//			result.put("info", "change node status failed.");
//		}
//		result.put("result", isNewNode);
//		return JSON.toJSONString(result);
//	}
//
//	/**
//	 * adapter通知node启动/停止calico 地址：http://127.0.0.1:15091/node/ws/adapter/calico
//	 * 
//	 * @param etcd_address
//	 *            =10.0.0.1&op=start/stop
//	 */
//	@POST
//	@Path("calico")
//	@Consumes("application/x-www-form-urlencoded")
//	public String opCalico(@FormParam("etcd_address") String etcdAddr,
//			@FormParam("op") String op) {
//		boolean isDocker = NodeEnv.getInstance().getNodeEnvDefinition()
//				.isDocker();
//		if (!isDocker) {
//			result.put("result", true);
//			return JSON.toJSONString(result);
//		}
//		String nodeIp = NodeEnv.getInstance().getNodeEnvDefinition()
//				.getNodeip();
//		Map<String, String> type = new HashMap<String, String>();
//		type.put("networkType", "calico");
//		adapterClient.setFile(type, NodeConstant.NODE_CONF);
//		NodeEnv.getInstance().getNodeEnvDefinition().setNetworkType("calico");
//
//		Map<String, Object> result = new HashMap<String, Object>();
//		String command = "";
//		if ("start".equals(op)) {
//			command = CommandGenerator.getInstance().generate(
//					Commands.getInstance().get("cal-nodeStart"), false, nodeIp,
//					etcdAddr + ":2379");
//		} else if ("stop".equals(op)) {
//			command = CommandGenerator.getInstance().generate(
//					Commands.getInstance().get("cal-nodeStop"), false);
//		} else {
//			log.error("unknow operation: " + op);
//			result.put("result", false);
//			result.put("info", "unknow operation: " + op);
//		}
//
//		// CalicoAnalyser caAnalyser = new CalicoAnalyser(); //??
//		executor.setAnalytic(analyser);
//		executor.setErrAnalytic(errorAnaly);
//		executor.exec(command);
//		String rs = analyser.getResult();
//		String errors = errorAnaly.getResult();
//		log.debug("Calico network:" + op + " the command is [" + command
//				+ "],and result= " + rs + ", errors=" + errors);
//		if ("OK".equals(rs) || rs.toLowerCase().indexOf("error") == -1
//				|| "calico-node".equals(rs)) {
//			log.debug("operation successed");
//			result.put("result", true);
//		} else {
//			log.error("operate failed: " + rs);
//			result.put("result", false);
//			result.put("info", rs);
//		}
//		return JSON.toJSONString(result);
//	}
//
//	/**
//	 * adapter通知node创建/删除profile，添加/删除网段
//	 * 地址：http://127.0.0.1:15091/node/ws/adapter/profileAndNetwork
//	 * 
//	 * @param profile_name
//	 *            =admin-cluster001&network=10.0.1.0/24&op=add/remove
//	 */
//	@POST
//	@Path("profileAndNetwork")
//	@Consumes("application/x-www-form-urlencoded")
//	public String profileNetwork(@FormParam("profile_name") String proName,
//			@FormParam("network") String network, @FormParam("op") String op) {
//		boolean isDocker = NodeEnv.getInstance().getNodeEnvDefinition()
//				.isDocker();
//		if (!isDocker) {
//			result.put("result", true);
//			return JSON.toJSONString(result);
//		}
//		Map<String, Object> result = new HashMap<String, Object>();
//		executor.setAnalytic(analyser);
//		// executor.setErrAnalytic(errorAnaly);
//
//		final String command = CommandGenerator.getInstance().generate(
//				Commands.getInstance().get("cal-nodeProfileOp"), false,
//				proName, network, op);
//		log.debug("Begin " + op + " profile, the command is [" + command + "]");
//		executor.exec(command);
//		String rs = analyser.getResult();
//		// String errorrs = errorAnaly.getResult();
//		log.debug("operate profile: " + rs);
//		if (rs.contains("Created profile")) {
//			log.debug("operation successed");
//			result.put("result", true);
//		} else {
//			result.put("result", false);
//			result.put("info", rs);
//		}
//		return JSON.toJSONString(result);
//	}
//}
