package com.dc.appengine.quartz.manager;

import java.io.Serializable;
import java.util.Map;

public class ScheduleJob implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 任务实体类
	 * 任务的描述信息
	 */
	private String jobName;//任务名称
	private String jobId;//任务id
	private String creTime;//创建时间
	private String jobCron;//时间表达式
	private Map<String,Object> excuteInfo;//执行体信息
	private String jobGroup;//任务组
	private String jobDesc;//任务描述
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getCreTime() {
		return creTime;
	}
	public void setCreTime(String creTime) {
		this.creTime = creTime;
	}
	public String getJobCron() {
		return jobCron;
	}
	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public Map<String,Object> getExcuteInfo() {
		return excuteInfo;
	}
	public void setExcuteInfo(Map<String,Object> excuteInfo) {
		this.excuteInfo = excuteInfo;
	}

}
