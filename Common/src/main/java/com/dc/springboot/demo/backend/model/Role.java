package com.dc.springboot.demo.backend.model;

import java.io.Serializable;
import java.util.Date;
/**
 *<p>Title：角色类</p>
 *<p>Description: 角色基本信息， 与数据库表一一对应无特殊属性</p>
 *<p>Copyright: 神州信息</p>
 *<p>Company: 神州信息 </p>
 *@author 
 *@version 0.1
 *@date 2017/6/10
 */
public class Role implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private int cpid;
	private String Name;
	private String description;
	private String system;
	private String deleted;
    private Date createtime;
    private Date updatetime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
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
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public int getCpid() {
		return cpid;
	}
	public void setCpid(int cpid) {
		this.cpid = cpid;
	}
    
}