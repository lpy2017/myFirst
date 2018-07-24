package com.dc.appengine.node.docker;



public class DockerStatus {
	private String appId;
	private String instanceId;
	private String conatainer;//容器名称 
	private String ip;//容器Ip 10.0.5.3
	private boolean isRunning; //true/false
	private String memLimits="0";//内存限制大小 128MiB
	private String memUsage="0";//内存使用大小56.7MiB
	private String cpu="0";//cpu利用率 43%
	private String cpuCore="0";//cpu使用核 0,1
	private String mem="0";//内存使用大小44.29%
	private String netIO="0.0/0.0";//网络吞吐量 645KiB/500KiB
	private String diskIO="0.0/0.0";
	private String innerPort;//内部端口7000,7001
	private String port;//node 端口7000,7001
	private int linkCount=0;//连接数 3
	private boolean masterStopped;
	private String neuopstat;
	private String appCpu="0.0";
    private String appMem="0.0";	
	
	public DockerStatus() {
	}
	public DockerStatus(String appId, String instanceId, String conatainer) { 
		this.appId = appId;
		this.instanceId = instanceId;
		this.conatainer = conatainer;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(String cpuCore) {
		this.cpuCore = cpuCore;
	}
	public String getConatainer() {
		return conatainer;
	}
	public void setConatainer(String conatainer) {
		this.conatainer = conatainer;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isRunning() {
		return isRunning;
	}
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	public String getMemLimits() {
		return memLimits;
	}
	public void setMemLimits(String memLimits) {
		this.memLimits = memLimits;
	}
	 
	public String getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(String memUsage) {
		this.memUsage = memUsage;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMem() {
		return mem;
	}
	public void setMem(String mem) {
		this.mem = mem;
	}
	public String getNetIO() {
		return netIO;
	}
	public void setNetIO(String netIO) {
		this.netIO = netIO;
	}
	 
	public String getInnerPort() {
		return innerPort;
	}
	public void setInnerPort(String innerPort) {
		this.innerPort = innerPort;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getLinkCount() {
		return linkCount;
	}
	public void setLinkCount(int linkCount) {
		this.linkCount = linkCount;
	}
	public boolean isMasterStopped() {
		return masterStopped;
	}
	public void setMasterStopped(boolean masterStopped) {
		this.masterStopped = masterStopped;
	}
	public String getNeuopstat() {
		return neuopstat;
	}
	public void setNeuopstat(String neuopstat) {
		this.neuopstat = neuopstat;
	}
	public String getAppCpu() {
		return appCpu;
	}
	public void setAppCpu(String appCpu) {
		this.appCpu = appCpu;
	}
	public String getAppMem() {
		return appMem;
	}
	public void setAppMem(String appMem) {
		this.appMem = appMem;
	}
	public String getDiskIO() {
		return diskIO;
	}
	public void setDiskIO(String diskIO) {
		this.diskIO = diskIO;
	}
     

}
