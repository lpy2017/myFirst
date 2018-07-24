package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("node")
public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ip;
	private String description;
	private String name;
	private Long tcpPort;
	private String address;
	private String status;
	private String memory;
	private Integer usedMem;
	private String usedCpus;
	private String isolata;
	private double freeMem;
	private double freeCpu;
	private int freeDisk;
	private double residualMem;
	private double residualCpu;
	private int residualDisk;
	
	private int cpucount;
	private long memorysize;
	private long disksize;
	private String hostname;
	private String enname;
	private String nodeGroup;
	private int instanceNum;
	private boolean alone;
	boolean[] cpus;
	private String cpuStr;
	
	private List<Cpu> cpuList;
	private boolean upgrading;
	
	public static final String CPUCOUNT="cpucount";
	public static final String MEMORYSIZE="memorysize";
	public static final String DISKSIZE="disksize";
	public static final String EMNAME="enname";
	public static final String GROUP="NODE_GROUP";
	public static final String TCP_PORT="tcp_port";
	
	public static final String ISOLATA_STATE_WEAK = "0";
	public static final String ISOLATA_STATE_STRONG = "1";
	private String adapterNodeId;
	private String clusterId;
	private String containerIp;
	private String userName;
	private String userPassword;
	public String getAdapterNodeId() {
		return adapterNodeId;
	}
	public void setAdapterNodeId(String adapterNodeId) {
		this.adapterNodeId = adapterNodeId;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getContainerIp() {
		return containerIp;
	}
	public void setContainerIp(String containerIp) {
		this.containerIp = containerIp;
	}
	public int getCpucount() {
		return cpucount;
	}
	public void setCpucount(int cpucount) {
		this.cpucount = cpucount;
	}
	public long getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(long memorysize) {
		this.memorysize = memorysize;
	}
	public long getDisksize() {
		return disksize;
	}
	public void setDisksize(long disksize) {
		this.disksize = disksize;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getTcpPort() {
		return tcpPort;
	}
	public void setTcpPort(Long tcpPort) {
		this.tcpPort = tcpPort;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getMemory() {
		return memory;
	}
	public void setUsedMem(Integer usedMem) {
		this.usedMem = usedMem;
	}
	public Integer getUsedMem() {
		return usedMem;
	}
	public void setInstanceNum(int instanceNum) {
		this.instanceNum = instanceNum;
	}
	public int getInstanceNum() {
		return instanceNum;
	}
	public void setAlone(boolean alone) {
		this.alone = alone;
	}
	public boolean isAlone() {
		return alone;
	}
	public void setUsedCpus(String usedCpus) {
		this.usedCpus = usedCpus;
	}
	public String getUsedCpus() {
		return usedCpus;
	}
	public boolean[] getCpus() {
		return cpus;
	}
	public void setCpus(boolean[] cpus) {
		this.cpus = cpus;
	}
	public String getCpuStr() {
		return cpuStr;
	}
	public void setCpuStr(String cpuStr) {
		this.cpuStr = cpuStr;
	}
	public void setIsolata(String isolata) {
		this.isolata = isolata;
	}
	public String getIsolata() {
		return isolata;
	}
	public double getFreeMem() {
		return freeMem;
	}
	public void setFreeMem(double freeMem) {
		this.freeMem = freeMem;
	}
	public double getFreeCpu() {
		return freeCpu;
	}
	public void setFreeCpu(double freeCpu) {
		this.freeCpu = freeCpu;
	}
	public int getFreeDisk() {
		return freeDisk;
	}
	public void setFreeDisk(int freeDisk) {
		this.freeDisk = freeDisk;
	}
	public double getResidualMem() {
		return residualMem;
	}
	public void setResidualMem(double residualMem) {
		this.residualMem = residualMem;
	}
	public double getResidualCpu() {
		return residualCpu;
	}
	public void setResidualCpu(double residualCpu) {
		this.residualCpu = residualCpu;
	}
	public int getResidualDisk() {
		return residualDisk;
	}
	public void setResidualDisk(int residualDisk) {
		this.residualDisk = residualDisk;
	}
	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}
	public String getNodeGroup() {
		return nodeGroup;
	}
	public List<Cpu> getCpuList() {
		return cpuList;
	}
	public void setCpuList(List<Cpu> cpuList) {
		this.cpuList = cpuList;
	}
	public boolean isUpgrading() {
		return upgrading;
	}
	public void setUpgrading(boolean upgrading) {
		this.upgrading = upgrading;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
