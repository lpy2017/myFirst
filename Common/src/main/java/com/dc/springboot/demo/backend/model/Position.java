package com.dc.springboot.demo.backend.model;

import java.io.Serializable;
import java.util.Date;
/**
 *<p>Title:职位类</p>
 *<p>Description: 职位的基本信息，与数据库表一一对应无特殊属性</p>
 *<p>Copyright: 神州信息</p>
 *<p>Company: 神州信息 </p>
 *@author 金晓东
 *@version 0.1
 *@date 2017/7/10
 */
public class Position implements Serializable{
	private Integer id;
	private String title;
	private String ismanager;
	private String description;
	private String deleted;
    private Date createtime;
    private Date updatetime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getIsmanager() {
		return ismanager;
	}
	public void setIsmanager(String ismanager) {
		this.ismanager = ismanager;
	}
	
}