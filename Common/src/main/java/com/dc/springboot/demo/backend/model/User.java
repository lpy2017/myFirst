package com.dc.springboot.demo.backend.model;

import java.io.Serializable;
import java.util.Date;
/**
 *<p>Title:用户类</p>
 *<p>Description: 用户的基本信息，与数据库表一一对应,无特殊属性。</p>
 *<p>Copyright: 神州信息</p>
 *<p>Company: 神州信息 </p>
 *@author 金晓东
 *@version 0.1
 *@date 2017/6/10
 */
public class User implements Serializable{
    private Integer id;
    private String loginid;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String repository;
    private Integer defaultRoleid;
    private String newcomer;
    private String deleted;
    private Date createtime;
    private Date updatetime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	
	public Integer getDefaultRoleid() {
		return defaultRoleid;
	}
	public void setDefaultRoleid(Integer defaultRoleid) {
		this.defaultRoleid = defaultRoleid;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted == null ? null : deleted.trim();
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
	public String getNewcomer() {
		return newcomer;
	}
	public void setNewcomer(String newcomer) {
		this.newcomer = newcomer;
	}  
}