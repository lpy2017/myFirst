package com.dc.springboot.demo.backend.vo;

import java.io.Serializable;
import java.util.List;

import com.dc.springboot.demo.backend.model.Department;
import com.dc.springboot.demo.backend.model.Position;
import com.dc.springboot.demo.backend.model.Role;
/**
 *<p>Title:用户包装类</p>
 *<p>Description: 包装类封装数据</p>
 *<p>Copyright: 神州信息</p>
 *<p>Company: 神州信息 </p>
 *@author 金晓东
 *@version 0.1
 *@date 2017/7/10
 */
public class UserVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserVo()
    {
    }
    //private Role role;
    private List<Role> roleList;
    private List<Position> positionList;
    private List<Department> departmentList;
    private int departmentId;
    private int positionId;
    private String rolename;
    private Integer id;
    private Integer defaultRoleid;
    private String deleted;
    private String email;
    private String loginid;
    private String name;
    private String password;
    private String phone;
    private String newPassword;
    private String repository;
    private Integer vmCount;
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	private String sessionid;
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	
	
	public List<Position> getPositionList() {
		return positionList;
	}
	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}
	
	public List<Department> getDepartmentList() {
		return departmentList;
	}
	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
		this.deleted = deleted;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginid() {
		return loginid;
	}
	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public int getPositionId() {
		return positionId;
	}
	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public Integer getVmCount() {
		return vmCount;
	}
	public void setVmCount(Integer vmCount) {
		this.vmCount = vmCount;
	}
    
}
