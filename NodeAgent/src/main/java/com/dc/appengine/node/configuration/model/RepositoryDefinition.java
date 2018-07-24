package com.dc.appengine.node.configuration.model;


public class RepositoryDefinition extends AbstractPropDefinition{
	private String address;
	private int port;
	private String user;
	private String password;
	private String deployDir;
	private String scheme;
	private String resCacheDir;
	private String biz_log_dir;
//	private int heartbeat_port;
	private String registryAddr; 
	private String java_home;
	private String nfs_dir;
	private String registryIp;
	
	
	public String getRegistryIp() {
		return registryIp;
	}

	public void setRegistryIp(String registryIp) {
		this.registryIp = registryIp;
	}

	public String getNfs_dir() {
		return nfs_dir;
	}

	public void setNfs_dir(String nfs_dir) {
		this.nfs_dir = nfs_dir;
	}

	public String getJava_home() {
		return java_home;
	}

	public void setJava_home(String java_home) {
		this.java_home = java_home;
	}

	public String getRegistryAddr() {
		return registryAddr;
	}

	public void setRegistryAddr(String registryAddr) {
		this.registryAddr = registryAddr;
	}

//	public int getHeartbeat_port() {
//		return heartbeat_port;
//	}
//
//	public void setHeartbeat_port(int heartbeat_port) {
//		this.heartbeat_port = heartbeat_port;
//	}

	public String getBiz_log_dir() {
		return biz_log_dir;
	}

	public void setBiz_log_dir(String biz_log_dir) {
		this.biz_log_dir = biz_log_dir;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress( String address ) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort( int port ) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser( String user ) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	public String getDeployDir() {
		return deployDir;
	}

	public void setDeployDir(String deployDir) {
		this.deployDir = deployDir;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getResCacheDir() {
		return resCacheDir;
	}

	public void setResCacheDir(String resCacheDir) {
		this.resCacheDir = resCacheDir;
	}
}
