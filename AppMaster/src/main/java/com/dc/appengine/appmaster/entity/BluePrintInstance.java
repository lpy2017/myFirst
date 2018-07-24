package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("blueprintinstance")
public class BluePrintInstance implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String blueprint_instance_name;
	private String blueprint_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlueprint_instance_name() {
		return blueprint_instance_name;
	}
	public void setBlueprint_instance_name(String blueprint_instance_name) {
		this.blueprint_instance_name = blueprint_instance_name;
	}
	public String getBlueprint_id() {
		return blueprint_id;
	}
	public void setBlueprint_id(String blueprint_id) {
		this.blueprint_id = blueprint_id;
	}
}
