package com.dc.appengine.appmaster.entity;

import java.util.Date;

public class ReleaseTaskBean {
	private int id;
	private String name;
	private String type; // 类型
	private String level; // 优先级
	private String status;
	private String label;
	private String dependId; // 依赖任务	json数组[]
	private int parentId; // 父任务	空则为-1
	private String systemId; // 系统
	private String systemName;
	private String systemRequireId; // 系统需求
	private String systemRequireName;
	private String businessRequireId;// 业务需求
	private String businessRequireName;
	private String environmentId; // 环境id
	private String environmentName;
	private String codeBranchName; // 分支
	private String codeBaselineName;// 基线
	private String creater;
	private String principal; // 责任人
	private Date createTime;
	private Date updateTime;
	private Date expectTime;
	private boolean autoExecute;
	private String cronExpression;
	private String description;
	private String remark;
	private String source; // 来源
	private String attachment; // 附件
	private String inventoryId; // 所属任务清单	json数组[]
	private String releaseId; // 绑定发布id
	private String releasePhaseId; // 绑定发布阶段id
	
	private int integrationId;//同步集成id
	
	private String parentName;	//父任务名称
	private String dependName;	//依赖任务名称	json数组[]
	private String inventoryName;	//所属清单		json数组[]

	private String blueprintFlowInstance;
	
	private String releaseName;
	private String releasePhaseName;
	
	private String blueprintTemplate;
	private String blueprintInstance;
	private String blueprintFlow;
	
	private String blueprintName;
	private String blueprintInstanceName;
	private String blueprintFlowName;
	
	public ReleaseTaskBean() {

	}
	
	public ReleaseTaskBean(String name, String type, String level, String status, String label, 
			String systemName, String releasePhaseId, String creater, String principal,  Date createTime,  
			Date updateTime,  Date expectTime, String description, String remark, String source){
		this.name = name;
		this.type = type;
		this.level = level;
		this.status = status;
		this.label = label;
		this.systemName = systemName;
		this.releasePhaseId = releasePhaseId;
		this.creater = creater;
		this.principal = principal;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.expectTime = expectTime;
		this.description = description;
		this.remark = remark;
		this.source = source;
	}
	
	public ReleaseTaskBean(String name, String type, String level, String label, String dependId, int parentId, String systemId, 
			String systemName, String systemRequireId, String systemRequireName,  String businessRequireId, String businessRequireName,
			String codeBranchName, String codeBaselineName, String creater, String principal, Date expectTime, 
			boolean autoExecute, String cronExpression, String description, String remark, String source, String attachment){
		this.name = name;
		this.type = type;
		this.level = level;
		this.label = label;
		this.dependId = dependId;
		this.parentId = parentId;
		this.systemId = systemId;
		this.systemName = systemName;
		this.systemRequireId = systemRequireId;
		this.systemRequireName = systemRequireName;
		this.businessRequireId = businessRequireId;
		this.businessRequireName = businessRequireName;
		this.codeBranchName = codeBranchName;
		this.codeBaselineName = codeBaselineName;
		this.creater = creater;
		this.principal = principal;
		this.expectTime = expectTime;
		this.autoExecute = autoExecute;
		this.cronExpression = cronExpression;
		this.description = description;
		this.remark = remark;
		this.source = source;
		this.attachment = attachment;
	}
	
	public ReleaseTaskBean(int id, String name, String type, String level, String label, String dependId, int parentId, String systemId, 
			String systemName, String systemRequireId, String systemRequireName,  String businessRequireId, String businessRequireName,
			String codeBranchName, String codeBaselineName, String creater, String principal, Date expectTime, 
			boolean autoExecute, String cronExpression, String description, String remark, String source, String attachment){
		this.id = id;
		this.name = name;
		this.type = type;
		this.level = level;
		this.label = label;
		this.dependId = dependId;
		this.parentId = parentId;
		this.systemId = systemId;
		this.systemName = systemName;
		this.systemRequireId = systemRequireId;
		this.systemRequireName = systemRequireName;
		this.businessRequireId = businessRequireId;
		this.businessRequireName = businessRequireName;
		this.codeBranchName = codeBranchName;
		this.codeBaselineName = codeBaselineName;
		this.creater = creater;
		this.principal = principal;
		this.expectTime = expectTime;
		this.autoExecute = autoExecute;
		this.cronExpression = cronExpression;
		this.description = description;
		this.remark = remark;
		this.source = source;
		this.attachment = attachment;
	}
	
	public ReleaseTaskBean(String name, String type, String level, String label, String status, String dependId, int parentId, 
			String systemId, String systemName, String releasePhaseId, 
			String creater, String principal, Date expectTime, 
			String description, String remark, String source, String attachment,
			String systemRequireId, String systemRequireName,  String businessRequireId, String businessRequireName,
			String codeBranchName, String codeBaselineName){
				this.name = name;
				this.type = type;
				this.level = level;
				this.label = label;
				this.status = status;
				this.dependId = dependId;
				this.parentId = parentId;
				this.systemId = systemId;
				this.systemName = systemName;
				this.releasePhaseId = releasePhaseId;
				this.creater = creater;
				this.principal = principal;
				this.expectTime = expectTime;
				this.description = description;
				this.remark = remark;
				this.source = source;
				this.attachment = attachment;
				this.systemRequireId = systemRequireId;
				this.systemRequireName = systemRequireName;
				this.businessRequireId = businessRequireId;
				this.businessRequireName = businessRequireName;
				this.codeBranchName = codeBranchName;
				this.codeBaselineName = codeBaselineName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDependId() {
		return dependId;
	}

	public void setDependId(String dependId) {
		this.dependId = dependId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemRequireId() {
		return systemRequireId;
	}

	public void setSystemRequireId(String systemRequireId) {
		this.systemRequireId = systemRequireId;
	}

	public String getSystemRequireName() {
		return systemRequireName;
	}

	public void setSystemRequireName(String systemRequireName) {
		this.systemRequireName = systemRequireName;
	}

	public String getBusinessRequireId() {
		return businessRequireId;
	}

	public void setBusinessRequireId(String businessRequireId) {
		this.businessRequireId = businessRequireId;
	}

	public String getBusinessRequireName() {
		return businessRequireName;
	}

	public void setBusinessRequireName(String businessRequireName) {
		this.businessRequireName = businessRequireName;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentName() {
		return environmentName;
	}

	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}

	public String getCodeBranchName() {
		return codeBranchName;
	}

	public void setCodeBranchName(String codeBranchName) {
		this.codeBranchName = codeBranchName;
	}

	public String getCodeBaselineName() {
		return codeBaselineName;
	}

	public void setCodeBaselineName(String codeBaselineName) {
		this.codeBaselineName = codeBaselineName;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}

	public boolean isAutoExecute() {
		return autoExecute;
	}

	public void setAutoExecute(boolean autoExecute) {
		this.autoExecute = autoExecute;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}

	public String getReleasePhaseId() {
		return releasePhaseId;
	}

	public void setReleasePhaseId(String releasePhaseId) {
		this.releasePhaseId = releasePhaseId;
	}

	public int getIntegrationId() {
		return integrationId;
	}

	public void setIntegrationId(int integrationId) {
		this.integrationId = integrationId;
	}

	public String getDependName() {
		return dependName;
	}

	public void setDependName(String dependName) {
		this.dependName = dependName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}


	public String getBlueprintFlowInstance() {
		return blueprintFlowInstance;
	}

	public void setBlueprintFlowInstance(String blueprintFlowInstance) {
		this.blueprintFlowInstance = blueprintFlowInstance;
	}

	public String getReleaseName() {
		return releaseName;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public String getReleasePhaseName() {
		return releasePhaseName;
	}

	public void setReleasePhaseName(String releasePhaseName) {
		this.releasePhaseName = releasePhaseName;
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

	public String getBlueprintName() {
		return blueprintName;
	}

	public void setBlueprintName(String blueprintName) {
		this.blueprintName = blueprintName;
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

}
