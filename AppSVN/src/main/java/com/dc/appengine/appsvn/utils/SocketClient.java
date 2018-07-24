package com.dc.appengine.appsvn.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class SocketClient {
	public static String send(String ip,int port,String message) {
		Socket socket=null;
		try {
			socket =new Socket(ip,port);
			DataInputStream input = new DataInputStream(socket.getInputStream());    
            //向服务器端发送数据    
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
			out.writeUTF(message);
			String ret = input.readUTF();
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String MD5(String source) {
		
		String md5codet="";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update("admin:admin".getBytes());
			md5codet=new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		
		return md5codet;
	}
	public static void testList( ) throws NoSuchAlgorithmException {
		Map<String,Object> payload = new HashMap<String,Object>();
		payload.put("op", "listuser");
		payload.put("auth", MD5("admin:admin"));
		String messageSend=JSON.toJSONString(payload);
		String ret=SocketClient.send("192168.32.133", 9909, messageSend);
		System.out.println(ret);
	}
	public static void testUpdate(){
		Map<String,Object> payload = new HashMap<String,Object>();
		Map<String,Object> content = new HashMap<String,Object>();
		payload.put("op", "updateuser");
		content.put("name", "test");
		content.put("password", "123456");
		content.put("writeable", true);
		payload.put("auth", MD5("admin:admin"));
		payload.put("content", content);
		String messageSend=JSON.toJSONString(payload);
		String ret=SocketClient.send("127.0.0.1", 9909, messageSend);
		System.out.println(ret);
	}
	public static void testDelete(){
		Map<String,Object> payload = new HashMap<String,Object>();
		Map<String,Object> content = new HashMap<String,Object>();
		payload.put("op", "deleteuser");
		content.put("name", "test");
		payload.put("auth", MD5("admin:admin"));
		payload.put("content", content);
		String messageSend=JSON.toJSONString(payload);
		String ret=SocketClient.send("127.0.0.1", 9909, messageSend);
		System.out.println(ret);
	}
	public static void main(String[] args) throws NoSuchAlgorithmException {
		testUpdate();
	}
}
