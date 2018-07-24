package com.dc.appengine.appmaster.message.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.type.Alias;

import com.dc.appengine.appmaster.entity.Cpu;



/**
 * 节点类
 * @author yangleiv
 *
 */
@Alias("mvnode")
public class MVNode implements Comparable<MVNode>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public static final byte ISOLATA_CPU_INIT = 0;
	public static final byte ISOLATA_CPU_WEAK = 1;
	public static final byte ISOLATA_CPU_STRONG = 2;
	/**
	 * 节点Id
	 */
	private String name;
	/**
	 * 内存大小
	 */
	private double memory;
	/**
	 * 硬盘大小
	 */
	private int disk;
	/**
	 * cpu核数
	 */
	private int cpuNum;
	/**
	 * CPU使用率
	 */
	private double cpuStatus;
	/**
	 * 权重
	 */
	private int weight;
	/**
	 * 权重下标
	 */
	private int weightIndex;
	
	/**
	 * node 上的实例数
	 */
	private int instanceNum;
	/**
	 * 按照策略计算出可以部署的实例个数（不同策略对应不同的数量）
	 */
	private int deployableNum;
	/**
	 * node所在物理机
	 */
	private String hostName;
	
	/**
	 * 是否为独享节点
	 */
	private boolean alone;
	
	/**
	 * cpu使用情况
	 */
	private boolean[] cpus;
	
	/**
	 * cpu使用情况，字符串表示（用于数据库字段）
	 */
	private String cpuStr;
	
	/**
	 * 是否被lb监控
	 */
	private boolean running = false;
	
	/**
	 * 是否强隔离CPU
	 */
	private byte isolateState = 0;
	
	/**
	 * 节点类型
	 */
	private String nodeType;
	
	/**
	 * 节点分组
	 */
	private String nodeGroup;
	
	/**
	 * 是否使用lxc
	 */
	private boolean usedLxc;
	
	/**
	 * 是否为新node
	 */
	private boolean newNode;
	
	/**
	 * 是否为归还的node
	 */
	private boolean removeNode;
	
	/**
	 * 为node资源加乐观锁
	 */
	private long version;
	
	/**
	 * node当前状态
	 */
	private String status;
	
	private String ip;
	
	private List<Cpu> cpuList;
	private String adapterNodeId;
	private String clusterId;
	private List<Map> labelList;

	public List<Map> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Map> labelList) {
		this.labelList = labelList;
	}

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

	public MVNode(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCpuStatus() {
		return cpuStatus;
	}

	public void setCpuStatus(double cpuStatus) {
		this.cpuStatus = cpuStatus;
	}

	public double getMemory() {
		return memory;
	}
	
	public void setMemory(double memory) {
		this.memory = memory;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}
	
	public MVNode(String name, String ip, double memory,
			double cpuStatus, double memoryStatus, int weight) {
		this.name = name;
		this.memory = memory;
		this.cpuStatus = cpuStatus;
		this.weight = weight;
	}
	
	public int compareTo(MVNode n) {
		if(this.weight > n.getWeight()){
			return 1;
		} else if(this.weight < n.getWeight()){
			return -1;
		}
		return 0;
	}
	
	public boolean equals( Object other ){
		if( other instanceof MVNode ){
			if( other!=null && this.name.equals( ((MVNode)other).getName() ) ){
				return true;
			}
			return false;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	public void setWeightIndex(int weightIndex) {
		this.weightIndex = weightIndex;
	}
	public int getWeightIndex() {
		return weightIndex;
	}
	public void setInstanceNum(int instanceNum) {
		this.instanceNum = instanceNum;
	}
	public int getInstanceNum() {
		return instanceNum;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getHostName() {
		return hostName;
	}
	public void setAlone(boolean alone) {
		this.alone = alone;
	}
	public boolean isAlone() {
		return alone;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}
	public boolean isRunning() {
		return running;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpus(boolean[] cpus) {
		this.cpus = cpus;
	}

	public boolean[] getCpus() {
		return cpus;
	}

	public String getCpuStr() {
		return cpuStr;
	}

	public void setCpuStr(String cpuStr) {
		this.cpuStr = cpuStr;
	}

	public void setIsolateState(byte isolateState) {
		this.isolateState = isolateState;
	}

	public byte getIsolateState() {
		return isolateState;
	}

	public void setDeployableNum(int deployableNum) {
		this.deployableNum = deployableNum;
	}

	public int getDeployableNum() {
		return deployableNum;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeGroup(String nodeGroup) {
		this.nodeGroup = nodeGroup;
	}

	public String getNodeGroup() {
		return nodeGroup;
	}

	public void setUsedLxc(boolean usedLxc) {
		this.usedLxc = usedLxc;
	}

	public boolean getUsedLxc() {
		return usedLxc;
	}

	public void setNewNode(boolean newNode) {
		this.newNode = newNode;
	}

	public boolean getNewNode() {
		return newNode;
	}

	public void setRemoveNode(boolean removeNode) {
		this.removeNode = removeNode;
	}

	public boolean getRemoveNode() {
		return removeNode;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setDisk(int disk) {
		this.disk = disk;
	}

	public int getDisk() {
		return disk;
	}

	public List<Cpu> getCpuList() {
		return cpuList;
	}

	public void setCpuList(List<Cpu> cpuList) {
		this.cpuList = cpuList;
	}

	@Override
	public String toString() {
		return "nodeName:"+this.name
				+"_memory:"+this.memory
				+"_version:"+this.version
				+"_deployableNumber:"+this.deployableNum;
	}
	
	
}
