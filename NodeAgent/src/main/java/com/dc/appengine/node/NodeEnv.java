package com.dc.appengine.node;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.ConfigReader;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.model.NodeEnvDefinition;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.utils.FileUtil;
import com.dc.appengine.node.ws.client.AdapterClient;

public class NodeEnv {
	private static Logger log = LoggerFactory.getLogger(NodeEnv.class);
	private static NodeEnv _instance;

	private static String OS;
	public static Object olock = new Object();
	private static final String nodeBaseHome;

	private static final String nodeBaseConf;
	private NodeEnvDefinition nodeEnvDefinition;
	static {
		OperatingSystemMXBean osmb = ManagementFactory
				.getOperatingSystemMXBean();
		OS = osmb.getName();

		nodeBaseHome = System.getProperty(Constants.Env.BASE_HOME);
		nodeBaseConf = System.getProperty(Constants.Env.BASE_CONF);
	}

	private NodeEnv() {
		init();
	}

	public static NodeEnv getInstance() {
		if (_instance == null) {
			synchronized (NodeEnv.class) {
				if (_instance == null) {
					_instance = new NodeEnv();
				}
			}
		}
		return _instance;
	}

	private void init() {
		// log.info("nodePro is " + NodeProperties.isDocker);
		// NodeProperties.print();
		try {
			//放到预注册里AdapterClient.preRegister
/*			String nodeIp = System.getenv("HOST_IP");*/
			String nodeIp = NodeProperties.getNodeIp();
			Map<String, Object> mxsdConfMap = new HashMap<>();
			mxsdConfMap.put("com.dcfs.esb.client.location", nodeIp);
			mxsdConfMap.put("com.dcfs.esb.client.service.name", nodeIp);
			AdapterClient.setFile(mxsdConfMap, NodeConstant.NODE_MXSD_CONF);
			Map<String, Object> confMap = new HashMap<>();
			confMap.put("nodeName", nodeIp);
			confMap.put("nodeip", nodeIp);
			AdapterClient.setFile(confMap, NodeConstant.NODE_CONF);
			File portfile = FileUtil.getInstance().getFile(
					NodeConstant.NODE_CONF, Constants.Env.BASE_CONF);
			this.nodeEnvDefinition = ConfigReader.getInstance()
					.parsePropToModel(portfile, NodeEnvDefinition.class, null);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("node init fail");

		}

	}

	public String getOs() {
		return OS;
	}

	public void setOs(String name) {
		OS = name;
	}

	public static String getNodebasehome() {
		return nodeBaseHome;
	}

	public static String getNodebaseconf() {
		return nodeBaseConf;
	}

	public NodeEnvDefinition getNodeEnvDefinition() {
		return nodeEnvDefinition;
	}

	public void setNodeEnvDefinition(NodeEnvDefinition nodeEnvDefinition) {
		this.nodeEnvDefinition = nodeEnvDefinition;
	}

}
