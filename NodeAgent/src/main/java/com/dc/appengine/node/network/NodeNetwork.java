package com.dc.appengine.node.network;

import java.util.Map;

import com.dc.appengine.node.exception.DockerContainerException;
import com.dc.appengine.node.instance.InstanceModel;
import com.dc.appengine.node.network.impl.CalicoNetwork;
import com.dc.appengine.node.network.impl.DockerNetwork;

/**
 * 实现node的不同网络模式
 * 
 * @author xukqa
 */
public abstract class NodeNetwork {

	public static NodeNetwork build(String type) {
		if ("docker".equalsIgnoreCase(type)) {
			DockerNetwork docker = new DockerNetwork();
			return docker;
		} else if ("calico".equalsIgnoreCase(type)) {
			CalicoNetwork calico = new CalicoNetwork();
			return calico;
		} else {
			return null;
		}
	}

	public abstract void createNetwork(InstanceModel instanceModel)
			throws DockerContainerException;

	public abstract void dropNetwork(InstanceModel instanceModel)
			throws DockerContainerException;

	public abstract void dropNetwork(String network)
			throws DockerContainerException;

	public abstract void createBridge();

	public abstract String createNetWorkForContainer(String containerName,
			Map<String, String> ips);

	public abstract String getOutGoingIP(InstanceModel instanceModel);

	public abstract boolean addEthn(String type, InstanceModel instanceModel);

	public void resumeEthn(String type, InstanceModel instanceModel) {
	}
}
