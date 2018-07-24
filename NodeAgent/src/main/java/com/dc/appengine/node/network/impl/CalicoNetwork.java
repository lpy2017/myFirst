package com.dc.appengine.node.network.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.exception.DockerContainerException;
import com.dc.appengine.node.exception.PAASECodes;
import com.dc.appengine.node.instance.InstanceModel;
import com.dc.appengine.node.network.NodeNetwork;
import com.dc.appengine.plugins.command.Analytic;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class CalicoNetwork extends NodeNetwork {

	private static Logger LOG = LoggerFactory.getLogger(CalicoNetwork.class);
	private final Analytic<String> analysic = new DefaultAnalyser();
	private CommandWaitExecutor executor = new CommandWaitExecutor(analysic);
	/**
	 * calico网络模型为容器分配ip
	 */
	@Override
	public void createNetwork(InstanceModel instanceModel)
			throws DockerContainerException {

		String strContainerName = instanceModel.getContainerName();
		String profileName = this.getProfileName();
		Boolean DualNic = instanceModel.isDualNic();

		final String command = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("cal-containerAddStart"), false,
				strContainerName, instanceModel.getContainerIp(), profileName);
		// executor.setErrAnalytic(errorAnalytic);
		LOG.debug("Calico network set ip command:[" + command + "]");
		executor.exec(command);
		String result = (String) analysic.getResult();
		// String errors = (String) errorAnalytic.getResult();

		LOG.debug("container ip set result is : " + result);
		if (result.toLowerCase().indexOf("error") != -1) {
			LOG.error("Docker IPSET ERROR 1 ******************");
			throw new DockerContainerException(
					"cant set docker ip container nameed" + strContainerName,
					PAASECodes.CODE_NA_E000201);
		}

		if (DualNic) {
			// 加入一级负载网络
			this.setLoadNetwork(strContainerName, "append", profileName);
		}

		LOG.debug("DOCKER CONTAINER  IS START NOW");
	}

	/**
	 * 停止容器前，删除容器网络
	 * 
	 * @throws DockerContainerException
	 */
	@Override
	public void dropNetwork(String network) throws DockerContainerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void dropNetwork(InstanceModel instanceModel)
			throws DockerContainerException {
		String strContainerName = instanceModel.getContainerName();
		Boolean DualNic = instanceModel.isDualNic();

		final String command = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("cal-containerRemvStop"), false,
				strContainerName);
		// executor.setErrAnalytic(errorAnalytic);
		LOG.debug("remove container ip: " + command);
		executor.exec(command);
		String rs = (String) analysic.getResult();
		// String errors = (String) errorAnalytic.getResult();
		LOG.debug("result is : " + rs);
		if (rs.toLowerCase().indexOf("error") != -1) {
			LOG.error("Calico container remove ERROR 1 ******************");
			// LOG.error("error is : "+errors);
			throw new DockerContainerException(
					"cant set docker ip container nameed" + strContainerName,
					PAASECodes.CODE_NA_E000511);
		}
		// 删除容器的一级负载网络
		if (DualNic) {
			String profileName = this.getProfileName();
			this.setLoadNetwork(strContainerName, "remove", profileName);
		}
	}

	@Override
	public void createBridge() {

	}

	// 获得profile name
	private String getProfileName() {
		final String command = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("cal-getProfileName"), false);
		// executor.setErrAnalytic(errorAnalytic);
		executor.exec(command);
		String result = (String) analysic.getResult();
		// String errors = (String)errorAnalytic.getResult();
		LOG.debug("get profile name : " + result);
		if (result.toLowerCase().indexOf("error") != -1) {
			// LOG.error("error is : "+ errors);
			return "";
		}
		return result;
	}

	// 容器加入一级负载网络
	private void setLoadNetwork(String cName, String op, String profileName)
			throws DockerContainerException {
		final String command = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("cal-containerProfile"), false,
				cName, op, profileName);
		// executor.setErrAnalytic(errorAnalytic);
		LOG.debug("set load network command: [" + command + "]");
		executor.exec(command);
		String result = analysic.getResult();
		// String errors = errorAnalytic.getResult();
		LOG.debug(op + " container profile result is :" + result);
		if (result.toLowerCase().indexOf("error") != -1) {
			LOG.error("get profile name failed .");
			// LOG.error("error is : "+ errors);
			throw new DockerContainerException(
					"cant set docker ip container nameed" + cName,
					PAASECodes.CODE_NA_E000512);
		}
	}

	@Override
	public String createNetWorkForContainer(String containerName,
			Map<String, String> ips) {
		// TODO Auto-generated method stub
		return "true";
	}

	// 返回容器内能访问外部网络的ip,此时容器只有一个ip
	@Override
	public String getOutGoingIP(InstanceModel instanceModel) {
		return instanceModel.getContainerIp();
	}

	@Override
	public boolean addEthn(String type, InstanceModel instanceModel) {
		return true;
	}

}
