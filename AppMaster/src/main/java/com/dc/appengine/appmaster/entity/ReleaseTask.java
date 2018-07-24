package com.dc.appengine.appmaster.entity;

import java.util.Date;
import java.util.List;

public class ReleaseTask {
	private int id;
	private String name;
	private String initiator;
	private String system;
	private String module;
	private Date startTime;
	private Date endTime;
	private Date stopTime;
	private String description;
	private String status;
	private String blueprintTemplate;
	private String blueprintInstance;
	private String blueprintFlow;
	private String blueprintFlowInstance;
	private int busId;
	private int dependId;
	private boolean autoExecute;
	private String cronExepression;
	private List<ReleaseApproval> approvals;
	
	private String blueprintName;
	private String blueprintInstanceName;
	private String blueprintFlowName;
	private String busName;
	private String dependName;
	
	public ReleaseTask(){
		
	}
	
	public ReleaseTask(int busId, String name, String initiator, String system, String module, Date startTime, Date stopTime, 
			String description, String status, String blueprintTemplate, String blueprintInstance, String blueprintFlow, 
			int dependId, boolean autoExecute, String cronExpression){
		this.busId = busId;
		this.name = name;
		this.initiator = initiator;
		this.system = system;
		this.module = module;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.description = description;
		this.status = status;
		this.blueprintTemplate = blueprintTemplate;
		this.blueprintInstance = blueprintInstance;
		this.blueprintFlow = blueprintFlow;
		this.dependId = dependId;
		this.autoExecute = autoExecute;
		this.cronExepression = cronExpression;
	}
	
	public ReleaseTask(int id, int busId, String name, String initiator, String system, String module, Date startTime, Date stopTime, String description,
			String status, String blueprintTemplate, String blueprintInstance, String blueprintFlow, int dependId, boolean autoExecute, 
			String cronExpression){
		this.id = id;
		this.busId = busId;
		this.name = name;
		this.initiator = initiator;
		this.system = system;
		this.module = module;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.description = description;
		this.status = status;
		this.blueprintTemplate = blueprintTemplate;
		this.blueprintInstance = blueprintInstance;
		this.blueprintFlow = blueprintFlow;
		this.dependId = dependId;
		this.autoExecute = autoExecute;
		this.cronExepression = cronExpression;
	}
	
	public ReleaseTask(int id, String name, String initiator, String system, String module, Date startTime, Date endTime, String description,
			String status, String blueprintTemplate, String blueprintInstance, String blueprintFlow, String blueprintFlowInstance){
		this.id = id;
		this.name = name;
		this.initiator = initiator;
		this.system = system;
		this.module = module;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.status = status;
		this.blueprintTemplate = blueprintTemplate;
		this.blueprintInstance = blueprintInstance;
		this.blueprintFlow = blueprintFlow;
		this.blueprintFlowInstance = blueprintFlowInstance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBlueprintTemplate() {
		return blueprintTemplate;
	}

	public void setBlueprintTemplate(String blueprintTemplate) {
		this.blueprintTemplate = blueprintTemplate;
	}

	public String getBlueprintInstance() {
		return blueprintInstance;
	}

	public void setBlueprintInstance(String blueprintInstance) {
		this.blueprintInstance = blueprintInstance;
	}

	public String getBlueprintFlow() {
		return blueprintFlow;
	}

	public void setBlueprintFlow(String blueprintFlow) {
		this.blueprintFlow = blueprintFlow;
	}

	public String getBlueprintFlowInstance() {
		return blueprintFlowInstance;
	}

	public void setBlueprintFlowInstance(String blueprintFlowInstance) {
		this.blueprintFlowInstance = blueprintFlowInstance;
	}

	public List<ReleaseApproval> getApprovals() {
		return approvals;
	}

	public void setApprovals(List<ReleaseApproval> approvals) {
		this.approvals = approvals;
	}

	public String getBlueprintInstanceName() {
		return blueprintInstanceName;
	}

	public void setBlueprintInstanceName(String blueprintInstanceName) {
		this.blueprintInstanceName = blueprintInstanceName;
	}

	public String getBlueprintFlowName() {
		return blueprintFlowName;
	}

	public void setBlueprintFlowName(String blueprintFlowName) {
		this.blueprintFlowName = blueprintFlowName;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public int getBusId() {
		return busId;
	}

	public void setBusId(int busId) {
		this.busId = busId;
	}

	public int getDependId() {
		return dependId;
	}

	public void setDependId(int dependId) {
		this.dependId = dependId;
	}

	public boolean isAutoExecute() {
		return autoExecute;
	}

	public void setAutoExecute(boolean autoExecute) {
		this.autoExecute = autoExecute;
	}

	public String getCronExepression() {
		return cronExepression;
	}

	public void setCronExepression(String cronExepression) {
		this.cronExepression = cronExepression;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getDependName() {
		return dependName;
	}

	public void setDependName(String dependName) {
		this.dependName = dependName;
	}

	public String getBlueprintName() {
		return blueprintName;
	}

	public void setBlueprintName(String blueprintName) {
		this.blueprintName = blueprintName;
	}

}
