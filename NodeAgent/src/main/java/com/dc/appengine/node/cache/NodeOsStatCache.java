package com.dc.appengine.node.cache;

import java.util.concurrent.atomic.AtomicBoolean;

public class NodeOsStatCache {
	private static NodeOsStatCache instance;

	public static NodeOsStatCache getInstance() {
		synchronized (NodeOsStatCache.class) {
			if (instance == null) {
				instance = new NodeOsStatCache();
			}
		}
		return instance;
	}
	public AtomicBoolean IsStormNimbusHere =new AtomicBoolean(false);
	private String osType; // 系统类型 版本
	private String dockerVersion; // docker版本
	private int cpuCore; // 内核数量
	private double memory; // Gb
	private double disk; // Mb

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public String getDockerVersion() {
		return dockerVersion;
	}

	public void setDockerVersion(String dockerVersion) {
		this.dockerVersion = dockerVersion;
	}

	public int getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(int cpuCore) {
		this.cpuCore = cpuCore;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getDisk() {
		return disk;
	}

	public void setDisk(double disk) {
		this.disk = disk;
	}

}
