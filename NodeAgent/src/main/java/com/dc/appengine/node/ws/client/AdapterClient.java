package com.dc.appengine.node.ws.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.PropWriter;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.exception.IaasPropException;
import com.dc.appengine.node.utils.FileUtil;
import com.dc.appengine.node.ws.RestClient;

public class AdapterClient {
	private static Logger log = LoggerFactory.getLogger(AdapterClient.class);
	private RestClient restClient = new RestClient();
//	private String addr = NodeProperties.getAdapterRest();
	private static String vmId; // 兼容老版本的资源池
	private static String iaasId;// 重新注册时使用

	/**
	 * node向adapter注册 http://127.0.0.1:5011/Adapter/ws/node/register
	 * 
	 * @param： cpu=2&memory=512&disk=20&os_version=Ubuntu 14.04.1 LTS
	 *         &docker_version=1.9.1 &agent_version=1.0 cpu：cpu数量;
	 *         memory：内存大小，单位M;disk：磁盘大小，单位G; os版本;docker版本;node版本
	 * @return
	 */
	// public boolean registerToAdapter(String cpu, String memory, String disk,
	// String osVersion, String dockerVersion, String nodeVersion) {
	//
	// // if(null == nodeVersion){
	// nodeVersion = "1.0";
	// // }
	//
	// Form form = new Form();
	// form.param("cpu", cpu);
	// form.param("memory", memory);
	// form.param("disk", disk);
	// form.param("os_version", osVersion);
	// form.param("docker_version", dockerVersion);
	// form.param("agent_version", nodeVersion);
	//
	// log.debug("Send to Adapter --node regist:[" + form.asMap() + "]");
	// // 测试
	// log.debug("adapter ip is :" + addr);
	// WebTarget web = restClient.ipToWegt(addr).path("node").path("register");
	// String response = restClient.addAuthHeader(web.request(), new Date(),
	// "name", "pwd").put(
	// Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED),
	// String.class);
	// log.debug("regist to Adapter :[" + response + "]");
	//
	// // if(response.indexOf("true")!=-1){
	// Map<String, Object> rs = new HashMap<String, Object>();
	// NodeEnv.getInstance().getNodeEnvDefinition().setRegister(true);
	// rs.put("isRegister", "true");
	// setFile(rs, NodeConstant.NODE_CONF);
	// return true;
	// // }else{
	// // log.error("register failed:"+response);
	// // return false;
	// // }
	// }

	public static void setFile(Map<String, Object> map, String strFile) {
		try {
			try {
				if (map != null && map.size() > 0) {
					final File conf = FileUtil.getInstance().getFile(strFile,
							Constants.Env.BASE_CONF);
					PropertiesConfiguration confprop = PropWriter.getprop(conf);

					Set<String> set = map.keySet();
					for (String key : set) {
						confprop.setProperty(key, map.get(key));
					}
					PropWriter.writetoprop(conf, confprop);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new IaasPropException(e.getMessage(),
						"write to file failed");
			}
		} catch (IaasPropException e) {
			log.error("error:" + e);
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private Map<String, String> get() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("vm", vmId);
		result.put("iaas", iaasId);
		return result;
	}

	@SuppressWarnings("unused")
	private void set(String vm, String iaas) {
		vmId = vm;
		iaasId = iaas;
	}

	/**
	 * 获取网络ID，即也是CIDR表示的最小IP
	 * 
	 * @param ipCidr
	 *            CIDR法表示的IP，例如：172.16.0.0/12
	 * @return 网络ID，即也是CIDR表示的最小IP
	 */
	@SuppressWarnings("unused")
	private String getNetworkId(String ipCidr, String mask) {
		String[] ipMaskLen = ipCidr.split("\\/");
		String[] ips = ipMaskLen[0].split("\\.");
		String[] masks = mask.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			sb.append(Integer.parseInt(ips[i]) & Integer.parseInt(masks[i]));
			if (i != 3) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	/**
	 * 获取掩码
	 * 
	 * @param maskLength
	 *            网络ID位数
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getMask(String ipCidr) {
		String[] ipMaskLen = ipCidr.split("\\/");
		int maskLength = Integer.parseInt(ipMaskLen[1]);
		int binaryMask = 0xFFFFFFFF << (32 - maskLength);
		StringBuffer sb = new StringBuffer();
		for (int shift = 24; shift > 0; shift -= 8) {
			sb.append(Integer.toString((binaryMask >>> shift) & 0xFF));
			sb.append(".");
		}
		sb.append(Integer.toString(binaryMask & 0xFF));
		return sb.toString();
	}

	/**
	 * node向adapter请求容器的eth1对应的主机网关
	 * 地址：http://127.0.0.1:5011/Adapter/ws/node/bridge_ip
	 * 
	 * @return json{"result": true, "bridge_ip"："10.0.1.1/24","network":
	 *         "OVS"/"Calico" ip=10.0.1.2,ip:node所在的主机ip} 或{"result":
	 *         false,"info": "fail info"}
	 */
	// public boolean getNodeIp() {
	// WebTarget web = restClient.ipToWegt(addr).path("node")
	// .path("bridge_ip");
	// String response = restClient.addAuthHeader(web.request(), new Date(),
	// "name", "pwd").get(String.class);
	// log.debug("receive bridge ip from Adapter:" + response + ".");
	// Map<String, Object> map = JSON.parseObject(response,
	// new TypeReference<Map<String, Object>>() {
	// });
	// // 单独测试node
	// // Map<String,Object> map = new HashMap<String, Object>();
	// // map.put("result", true);
	// // map.put("bridge_ip", "10.0.1.1/24");
	// // map.put("network","OVS");
	//
	// Boolean result = (Boolean) map.get("result");
	// if (!result) {
	// log.error("========cannot get bridge ip from Adapter: "
	// + map.get("info"));
	// return false;
	// } else {
	// String nodeIp = (String) map.get("ip");
	// String network = (String) map.get("network"); // 设置网络模式为ovs
	// String bridgeNet = (String) map.get("bridge_ip");// CIDR地址
	// String bmask = this.getMask(bridgeNet);
	// String bridgeIp = bridgeNet.split("/")[0];
	//
	// Map<String, Object> nodeConfMap = new HashMap<String, Object>();
	// nodeConfMap.put("bridgeIp", bridgeIp);
	// nodeConfMap.put("mask", bmask);
	// nodeConfMap.put("networkType", network);
	// nodeConfMap.put("nodeName", nodeIp);
	// nodeConfMap.put("nodeip", nodeIp);
	//
	// NodeProperties.setNetworkType(network);
	// NodeProperties.setBridgeIp(bridgeIp);
	// NodeProperties.setMask(bmask);
	// NodeEnv.getInstance().getNodeEnvDefinition().setNodeip(nodeIp);
	// NodeEnv.getInstance().getNodeEnvDefinition().setNodeName(nodeIp);
	// setFile(nodeConfMap, NodeConstant.NODE_CONF);
	//
	// Map<String, Object> mxsdConfMap = new HashMap<>();
	// mxsdConfMap.put("com.dcfs.esb.client.location", nodeIp);
	// mxsdConfMap.put("com.dcfs.esb.client.service.name", nodeIp);
	// setFile(mxsdConfMap, NodeConstant.NODE_MXSD_CONF);
	// log.debug("set node ip， bridge ip and bridge0 finished");
	//
	// return true;
	// }
	// }

	/**
	 * node向adapter预注册，解决旧环境node入新环境问题 从环境变量取参数：cluster_id:集群id; name: node名;
	 * labels adapter接口地址：http://127.0.0.1:5011/Adapter/ws/node/preregister
	 */
	public boolean preRegister(String cpu, String memory, String disk,
			String osVersion, String dockerVersion, String nodeVersion) {

//		JSONObject form = new JSONObject();
//		form.put("cluster_id", System.getenv("cluster_id"));
//		form.put("name", System.getenv("node_name"));
//		form.put("labels", System.getenv("labels"));
//		form.put("cpu", cpu);
//		form.put("memory", memory);
//		form.put("disk", disk);
//		form.put("os_version", osVersion);
//
//		// Map<String, String> vmIaas = this.get();
//		// if (null != vmIaas.get("vm") && null != vmIaas.get("iaas")) {
//		// form.param("vm_id", vmIaas.get("vm"));
//		// form.param("iaas_id", vmIaas.get("iaas"));
//		// this.set(null, null);
//		// }
//		log.debug("Previous register to adapter : " + form.toJSONString());
//		WebTarget web = restClient.ipToWegt(addr).path("node")
//				.path("preregister");
//		boolean quest = true;
//		String result = "";
//		while (quest) {
//			try {
//				result = web.request().post(
//						Entity.entity(form, MediaType.APPLICATION_JSON),
//						String.class);
//				log.debug("register result: " + result);
//				quest = false;
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//				if (e.getMessage().indexOf("Connection refused") != -1
//						|| e.getMessage().indexOf("拒绝连接") != -1) {
//					log.error("connection refused: please make sure that adapter is started up");
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e1) {
//						log.error(e1.getMessage(), e1);
//					}
//					continue;
//				} else
//					quest = false;
//			}
//		}
//		Map resultObject = JSON.parseObject(result, Map.class);
//		if (Boolean.valueOf(resultObject.get("result").toString())) {
//			String nodeIp = String.valueOf(resultObject.get("ip"));
//			
//			Map<String, Object> mxsdConfMap = new HashMap<>();
//			mxsdConfMap.put("com.dcfs.esb.client.location", nodeIp);
//			mxsdConfMap.put("com.dcfs.esb.client.service.name", nodeIp);
//			setFile(mxsdConfMap, NodeConstant.NODE_MXSD_CONF);
//			
//			Map<String, Object> rs = new HashMap<String, Object>();
//			NodeEnv.getInstance().getNodeEnvDefinition().setRegister(true);
//			NodeEnv.getInstance().getNodeEnvDefinition().setNewNode(false);
//			NodeEnv.getInstance().getNodeEnvDefinition().setNodeip(nodeIp);
//			NodeEnv.getInstance().getNodeEnvDefinition().setNodeName(nodeIp);
//			rs.put("isRegister", "true");
//			rs.put("isNewNode", "false");
//			rs.put("nodeName", nodeIp);
//			rs.put("nodeip", nodeIp);
//			setFile(rs, NodeConstant.NODE_CONF);
//			return true;
//		} else
			return false;
	}
}