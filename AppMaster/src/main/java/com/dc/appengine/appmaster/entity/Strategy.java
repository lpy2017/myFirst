package com.dc.appengine.appmaster.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("strategy")
public class Strategy implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String DEPLOY_DEFAULT = "DEPLOY_DEFAULT";
	public static final String SCALE_DEFAULT = "SCALE_DEFAULT";
//	public static final String CONTRACT_DEFAULT = "CONTRACT_DEFAULT";
	
	public static final String TYPE_DEPLOY = "DEPLOY";
//	public static final String TYPE_EXTEND = "EXTEND";
//	public static final String TYPE_CONTRACT = "CONTRACT";
	public static final String TYPE_SCALE = "SCALE";
	
	private Long id;
	private String name;
	private String description;
	private Long userId;
	private String userName;
	private String type;
	private Object authCondition;
	private List<StrategyItem> list;
	
	public Strategy(){
		
	}
	public Strategy(String name,String description,Long userId,String type){
		this.name = name;
		this.description = description;
		this.userId = userId;
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getAuthCondition() {
		return authCondition;
	}
	public void setAuthCondition(Object authCondition) {
		this.authCondition = authCondition;
	}
	public void setList(List<StrategyItem> list) {
		this.list = list;
	}
	public List<StrategyItem> getList() {
		return list;
	}
}
