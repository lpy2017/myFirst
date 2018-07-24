package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("item")
public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 策略编码
	 */
	public static final String CODE_VIP="VIP";
	public static final String CODE_XMX="JVM_XMX";
	public static final String CODE_INSTANCE_NUMBER="INSTANCE_NUMBER";
	public static final String CODE_SAMEHOST="SAMEHOST";
	public static final String CODE_ALONE="ALONE";
	public static final String CODE_PORT_NUMBER="PORT_NUMBER";
	public static final String CODE_NODE_TYPE="NODE_TYPE";
	public static final String CODE_CPU_NUMBER="CPU_NUMBER";
	public static final String CODE_NODE_GROUP="NODE_GROUP";
	public static final String CODE_STRONG_ISOLATA="STRONG_ISOLATA";
	public static final String CODE_AUTO_OPERATE="AUTO_OPERATE";
	public static final String CODE_MEMORY="MEMORY";
	public static final String CODE_DISK="DISK";
	public static final String CODE_DOCKER_IMAGE="DOCKER_IMAGE";
	public static final String CODE_SHARED_CPU="SHARED_CPU";// 共享cpu
	public static final String CODE_CPU_QUOTA="CODE_CPU_QUOTA";// cpu配额
	public static final String CODE_REPLICAS="REPLICAS";
	public static final String CODE_RESOURCE_UPDATE_TYPE = "RESOURCE_UPDATE_TYPE";
	public static final String CODE_OUTGOING_IPS = "OUTGOING_IPS";
	public static final String CODE_LABELS = "LABELS";
	/**
	 * node集群id
	 */
	public static final String CODE_CLUSTERID="CLUSTERID";
	
	/**
	 * node ids
	 */
	public static final String CODE_NODEIDS="NODEIDS";
	/**
	 * 隔离级别
	 */
	public static final String VALUE_STRONG_ISOLATA="1";
	public static final String VALUE_WEAK_ISOLATA="0";
	/**
	 * 策略维度类型
	 */
	public static final String TYPE_DEPLOY="DEPLOY"; //部署策略
	public static final String TYPE_AUTOOP="AUTOOP";//扩展策略
	public static final String TYPE_SCALE="SCALE";//扩展策略
//	public static final String TYPE_EXTEND="EXTEND";//扩展策略
//	public static final String TYPE_CONTRACT="CONTRACT";//收缩策略

	
	private Long id;
	private String name;
	private String description;
	private String type;
	private String code;
	private String nullable;
	
	public Item(){
		
	}
	
	public Item(String name, String description, String type,
			String code) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.code = code;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}

	public void setNullable(String nullable) {
		this.nullable = nullable;
	}

	public String getNullable() {
		return nullable;
	}

	
	
}
