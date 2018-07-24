package com.dc.appengine.cloudui.entity;

public class Host {
	private  String host;
	private  String username;
	private  String passwd;

	public  Host(){
	}

	public  Host(String host,String username,String passwd){
		this.host = host;
		this.username = username;
		this.passwd = passwd;
	}

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	@Override
	public String toString() {
		String content = host+":\n"
				+ "  host: "+host+"\n"
				+ "  user: "+username+"\n"
				+ "  password: "+passwd+"\n";

		return content;
	}
}
