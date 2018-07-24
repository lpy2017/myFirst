package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class ReleaseApproval {
	private int id;
	private int taskId;
	private String approver;
	private String performer;
	private Date applyTime;
	private Date approveTime;
	private int approveOrder;
	private String status;
	private String taskName;
	private String taskInitiator;
	private Date taskStartTime;
	
	public ReleaseApproval(){
		
	}
	
	public ReleaseApproval(int taskId, String approver, int approveOrder, String status){
		this.taskId = taskId;
		this.approver = approver;
		this.approveOrder = approveOrder;
		this.status = status;
	}
	
	public ReleaseApproval(int taskId, String approver, Date applyTime, int approveOrder, String status){
		this.taskId = taskId;
		this.approver = approver;
		this.applyTime = applyTime;
		this.approveOrder = approveOrder;
		this.status = status;
	}
	
	public ReleaseApproval(int id, int taskId, String approver, Date applyTime, Date approveTime, int approveOrder, String status){
		this.id = id;
		this.taskId = taskId;
		this.approver = approver;
		this.applyTime = applyTime;
		this.approveTime = approveTime;
		this.approveOrder = approveOrder;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public Date getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	public int getApproveOrder() {
		return approveOrder;
	}
	public void setApproveOrder(int approveOrder) {
		this.approveOrder = approveOrder;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskInitiator() {
		return taskInitiator;
	}

	public void setTaskInitiator(String taskInitiator) {
		this.taskInitiator = taskInitiator;
	}

	public Date getTaskStartTime() {
		return taskStartTime;
	}

	public void setTaskStartTime(Date taskStartTime) {
		this.taskStartTime = taskStartTime;
	}

}
