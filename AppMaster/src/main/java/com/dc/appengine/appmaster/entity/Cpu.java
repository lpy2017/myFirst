package com.dc.appengine.appmaster.entity;

import org.apache.ibatis.type.Alias;

@Alias("cpu")
public class Cpu {
	private long id;
	private long nodeId;
	private int cpuIndex;
	private Boolean shared;
	private int maxCount;
	private int usedCount;

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getUsedCount() {
		return usedCount;
	}

	public void setUsedCount(int usedCount) {
		this.usedCount = usedCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public int getCpuIndex() {
		return cpuIndex;
	}

	public void setCpuIndex(int cpuIndex) {
		this.cpuIndex = cpuIndex;
	}

	public Boolean isShared() {
		return shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}
	
	public boolean usable(){
		return this.usedCount < this.maxCount;
	}
	
	public Cpu copy(){
		Cpu copy = new Cpu();
		copy.setId(this.id);
		copy.setCpuIndex(this.cpuIndex);
		copy.setMaxCount(this.maxCount);
		copy.setNodeId(this.nodeId);
		copy.setShared(this.shared);
		copy.setUsedCount(this.usedCount);
		return copy;
	}
}
