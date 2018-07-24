package com.dc.appengine.appmaster.entity;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("strategyItem")
public class StrategyItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long itemId;
	private Long strategyId;
	private String value;
	private Integer weight;
	private String operator;
	private String itemCode;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(Long strategyId) {
		this.strategyId = strategyId;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperator() {
		return operator;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
