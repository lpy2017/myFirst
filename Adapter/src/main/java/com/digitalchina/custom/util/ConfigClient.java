package com.digitalchina.custom.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.digitalchina.custom.util.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigClient {
	private String host;
	private int port=9000;
	private String userName;
	private String password;
	private String type="md5";
	private String module;
	private long timeout=6000;
	private String returnUrl="127.0.0.1";
	private boolean success=false;
 
	private  Map<String,List<Map<String,String>>>  content = null;
	/**
	 * @author hannn
	 * @param ip the ip of configcenter
	 * @param port port of configcenter default 5091
	 * @param userName paas用户名
	 * @param password paas密码
	 * @param password paas密码
	 * @param appName 应用名
	 * @param appVersion 应用版本
	 * 初始化配置中心客户端基本参数
	 * */
	public ConfigClient(String host ,String userName,String password,String moduleName, int port){
		this.host=host;
		this.port=port;
		this.userName=userName;
		this.password=password;
		this.module=moduleName;
		this.returnUrl="127.0.0.1";
	}
	public ConfigClient(){
		
	}
	/**
	 * 获取配置中心配置 初步进行解析
	 * @return
	 */
	private boolean getConfig(){
		if(success){
			return true;
		}
		int timeUnit=1000;
		int time=0;
		while(!success){
			if(!check()){
				return false;
			}
			String url=buildUrl();
			String params=buildParams();
			success=sendRequest(url,params);	
		 
			time++;
			if((time*timeUnit)>=timeout){
				System.out.println("connect time out!");
				break;
			}
			try {
				Thread.sleep(timeUnit);
			} catch (InterruptedException e) {
			}
		}
		return true;
	}
	/**
	 * 获取配置中心地址
	 * @return
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 设置配置中心地址
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 获取配置中心端口
	 * @return
	 */
	public int getPort() {
		return port;
	}
	/**
	 * 设置配置中心端口 默认9000
	 * @return
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * 获取配置中心用户名
	 * @return
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置配置中心用户名
	 * @return
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取配置中心密码
	 * @return
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置配置中心密码
	 * @return
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取配置中心密码类型 （md5默认 ,  password）
	 * md5 表示当前密码为md5之后的结果，
	 * password 表示当前密码为明文密码 
	 */
	public String getType() {
		return type;
	}
	/**
	 *	设置密码类型
	 */
	public void setType(String type) {
		this.type = type;
	}
	 
	 
 
 
	private boolean check(){
		if (host==null || "".equals(host)){
			System.out.println("please set the host of configcenter");
			return false;
		}
		if (userName==null || "".equals(userName)){
			System.out.println("please set the userName of configcenter");
			return false;
		}
		if (password==null || "".equals(password)){
			System.out.println("please set the userName of configcenter user:"+userName);
			return false;
		}
		if (module==null || "".equals(module)){
			System.out.println("please set the moduleName of the config you want");
			return false;
		}
		 
		return true;
	}
	private String buildUrl(){
		StringBuilder urlB= new StringBuilder("http://");
		urlB.append(host);
		urlB.append(":");
		urlB.append(port);
		urlB.append("/configcenter/ws/config/getConfig");
		return urlB.toString();
	}
	private String buildParams(){
		StringBuilder sbF= new StringBuilder("type=");
		sbF.append(type);
		sbF.append("&userName=");
		sbF.append(userName);
		sbF.append("&passWord=");
		sbF.append(password);
		sbF.append("&moduleType=");
		sbF.append(module);
		sbF.append("&hostIp=");
		sbF.append(returnUrl);
		sbF.append("&hostPort=");
		sbF.append(9090);
		return sbF.toString();
	}
	 
	private boolean sendRequest(String url,String params){
		String temp;
		try {
			temp = WebClient.sendPost(url,params);
		} catch (Exception e) {
			return false;
		}
		Map<String,String> map=JSON.parseObject(temp, new TypeReference<Map<String,String>>(){});
		if(map.get("result")!=null &&  "false".equals(map.get("result"))){
			String msg=map.get("message");
			System.out.println("get config error:"+msg+" try again");
			return false;
		}
		 
 
		Object rowObject= map.get("content");
		if(rowObject ==null){
			System.out.println("no content");
			return false;
		}
		
		try{
			content=JSON.parseObject(rowObject.toString(), new TypeReference<Map<String,List<Map<String,String>>>>(){});
		}catch(Exception e){
			System.out.println("parse json error");
			return false;
		}
		return true;
	}
	public void clear(){
		success=false;
	}
	public Properties getPropertyConfig(String type){
		if(type==null || "".equals(type)){
			type="normal";
		}
		Properties properties= new Properties();
		getConfig();
		if(success && content.containsKey(type)){
			List<Map<String,String>> list=  content.get(type) ;
			if(list!=null && list.size()>0){
				for(Map<String,String> unit:list){
					String key=unit.get("key");
					String value=unit.get("value");
					properties.setProperty(key, value);
				}
			}
		}
		return properties;
	}
	

	
	public List<Map<String,String>> getMapConfig(String type){
		if(type==null || "".equals(type)){
			type="normal";
		}
		getConfig();
		if(success && content.containsKey(type)){
			return content.get(type);
		}
		return Collections.emptyList();
		
	}
	
}
