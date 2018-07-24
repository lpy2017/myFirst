package com.dc.springboot.demo.backend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 *<p>Title:部门类</p>
 *<p>Description: 与表字段一一对应无特殊属性</p>
 *<p>Copyright: 神州信息</p>
 *<p>Company: 神州信息 </p>
 *@author 金晓东
 *@version 0.1
 *@date 2017/6/25
 */
public class Department implements Serializable{
	
	
private int id;
private String name;
private String description;
private String deleted;
private int parentid;
private String parentname;
private boolean topLevelDepartment;
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
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getParentid() {
	return parentid;
}
public void setParentid(int parentid) {
	this.parentid = parentid;
}
public String getDeleted() {
	return deleted;
}
public void setDeleted(String deleted) {
	this.deleted = deleted;
}
public List<User> getUserList() {
	return userList;
}
public void setUserList(List<User> userList) {
	this.userList = userList;
}
private List<User> userList;
private Date createtime;
private Date updatetime;
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
public boolean isTopLevelDepartment() {
	return topLevelDepartment;
}
public void setTopLevelDepartment(boolean topLevelDepartment) {
	this.topLevelDepartment = topLevelDepartment;
}
public String getParentname() {
	return parentname;
}
public void setParentname(String parentname) {
	this.parentname = parentname;
}
}
