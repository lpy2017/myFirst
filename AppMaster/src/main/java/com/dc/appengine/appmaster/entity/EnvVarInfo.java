package com.dc.appengine.appmaster.entity;

import java.util.Map;

public class EnvVarInfo {
	private long objId;
	private String objType;
	private String varName;
	private String varValue;
	private String description;
	private Map<String, String> valueFrom;

	public long getObjId() {
		return objId;
	}

	public void setObjId(long objId) {
		this.objId = objId;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getVarName() {
		return varName;
	}

	public void setVarName(String varName) {
		this.varName = varName;
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(Map<String, String> valueFrom) {
		this.valueFrom = valueFrom;
	}
}