package com.dc.appengine.node.network.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.exception.DockerContainerException;
import com.dc.appengine.node.exception.PAASECodes;
import com.dc.appengine.node.instance.InstanceModel;
import com.dc.appengine.node.network.NodeNetwork;
import com.dc.appengine.plugins.command.Analytic;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.analyser.ErrorAnalyser;
import com.dc.appengine.plugins.command.analyser.LastResultAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class DockerNetwork extends NodeNetwork {

	private static Logger log = LoggerFactory.getLogger(DockerNetwork.class);
	private final Analytic<String> analyser = new DefaultAnalyser();
	private CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
	private final Analytic<String> errorAnalyser = new ErrorAnalyser();

	/**
	 * docker的网络模式为容器设置ip
	 */
	@Override
	public void createNetwork(InstanceModel instanceModel)
			throws DockerContainerException {
		// 完成ip设置
//		String lxc_ip = instanceModel.getContainerIp();
//		String strContainerName = instanceModel.getContainerName();
//
//		final String dockerIPset = CommandGenerator.getInstance().generate(
//				Commands.getInstance().get("docker-ipset"), false,
//				strContainerName, lxc_ip,
//				NodeProperties.getBridgeIp());
//		log.debug("Docker network Ip set command:[" + dockerIPset + "]");
//		executor.setErrAnalytic(errorAnalyser);
//		executor.exec(dockerIPset);
//		String result = analyser.getResult();
//		String errresult = errorAnalyser.getResult();
//		if (result == null || !result.equals("ok")
//				|| !errresult.equals("no error")) {
//			log.error("Docker IPSET ERROR 1 ******************");
//			log.error("result is:" + result);
//			log.error("errresult is:" + errresult);
//			throw new DockerContainerException(
//					"cant set docker ip container nameed" + strContainerName,
//					PAASECodes.CODE_NA_E000201);
//		}

	}

	@Override
	public void dropNetwork(InstanceModel instanceModel) {

	}

	@Override
	public void createBridge() {
//		String bridgeNet = NodeProperties
//				.getBridgeIp()
//				+ "/24";
//		String bridgeIp = NodeProperties
//				.getBridgeIp();
//		String mask = NodeProperties.getMask();
//		String ostype = NodeProperties
//				.getLinuxOsType();
//
//		String command = CommandGenerator.getInstance().generate(
//				Commands.getInstance().get("docker-bridgeCreate"), false,
//				bridgeNet, bridgeIp, mask, ostype);
//		log.debug("bridge create:[" + command + "]");
//		AbstractAnalyser<String> analyser = new LastResultAnalyser();
//		CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
//		executor.exec(command);
//		String reString = analyser.getResult();
//		log.debug("Docker bridge create " + reString);
	}

	@Override
	public String createNetWorkForContainer(String containerName,
			Map<String, String> ips) {
		// TODO Auto-generated method stub
		return "false";
	}

	@Override
	public String getOutGoingIP(InstanceModel instanceModel) {
		// 返回容器可以访问外部的ip
		return instanceModel.getContainerIp();
	}

	@Override
	public boolean addEthn(String type, InstanceModel instanceModel) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void dropNetwork(String network) throws DockerContainerException {
		// TODO Auto-generated method stub

	}
}
