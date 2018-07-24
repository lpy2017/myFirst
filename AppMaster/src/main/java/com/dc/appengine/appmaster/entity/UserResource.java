package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("userResource")
public class UserResource implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7143309001061308879L;

	private long userId;
	private long cpu;
	private long memory;
	private long disk;
	private long cpu_used;
	private long memory_used;
	private long disk_used;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCpu() {
		return cpu;
	}
	public void setCpu(long cpu) {
		this.cpu = cpu;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
	public long getDisk() {
		return disk;
	}
	public void setDisk(long disk) {
		this.disk = disk;
	}
	public long getCpu_used() {
		return cpu_used;
	}
	public void setCpu_used(long cpu_used) {
		this.cpu_used = cpu_used;
	}
	public long getMemory_used() {
		return memory_used;
	}
	public void setMemory_used(long memory_used) {
		this.memory_used = memory_used;
	}
	public long getDisk_used() {
		return disk_used;
	}
	public void setDisk_used(long disk_used) {
		this.disk_used = disk_used;
	}
	

}
