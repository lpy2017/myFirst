package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("blueprinttype")

public class BluePrintType implements Serializable {

	private static final long serialVersionUID = 1L;
    	
	private String id;
	private String blueprint_id;
	private Long flow_id;
	private String flow_type;
	private String flow_info;
	private String flow_name;
	private String flow_info_group;
	private String app_name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlueprint_id() {
		return blueprint_id;
	}
	public void setBlueprint_id(String blueprint_id) {
		this.blueprint_id = blueprint_id;
	}
	public Long getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(Long flow_id) {
		this.flow_id = flow_id;
	}
	public String getFlow_type() {
		return flow_type;
	}
	public void setFlow_type(String flow_type) {
		this.flow_type = flow_type;
	}
	public String getFlow_info() {
		return flow_info;
	}
	public void setFlow_info(String flow_info) {
		this.flow_info = flow_info;
	}
	public String getFlow_name() {
		return flow_name;
	}
	public void setFlow_name(String flow_name) {
		this.flow_name = flow_name;
	}
	public String getFlow_info_group() {
		return flow_info_group;
	}
	public void setFlow_info_group(String flow_info_group) {
		this.flow_info_group = flow_info_group;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public BluePrintType() {
		super();
	}
	public BluePrintType(String blueprint_id, Long flow_id, String flow_type) {
		super();
		this.blueprint_id = blueprint_id;
		this.flow_id = flow_id;
		this.flow_type = flow_type;
	}
	
	
	
}
