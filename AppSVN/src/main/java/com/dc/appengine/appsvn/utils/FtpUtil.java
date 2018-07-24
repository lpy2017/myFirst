package com.dc.appengine.appsvn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class FtpUtil {
	private static Logger log= LoggerFactory.getLogger(FtpUtil.class);
	public static  FTPClient getClient(FtpInfo info){
		FTPClient ftpClient = new  FTPClient();
		log.debug("初始化ftp客户端.....");
		ftpClient.setControlEncoding("UTF-8");
		ftpClient.setDefaultPort(info.getPort());
		ftpClient.setDataTimeout(120000); 
		log.debug("ftp 开始连接....");
		try {
			ftpClient.connect(info.getIp());
		} catch (SocketException  e) {
			log.debug("ftp 连接失败"+e.getMessage());
			return null;
		} catch (IOException e) {
			log.debug("ftp 连接失败"+e.getMessage());
			return null;
		}
		try {
			log.debug("ftp 登录....");
			ftpClient.login(info.getUserName(),info.getPassword());
		} catch (IOException e) {
			log.debug("ftp 登录失败"+e.getMessage());
		}
		try {
			ftpClient.initiateListParsing("/");
		} catch (Exception e) {
			try {
				ftpClient.disconnect();
			} catch (IOException e1) {
			}
			log.debug("ftp 登录失败"+e.getMessage());
			return null;
		}
		return ftpClient;
		
	}
	public static boolean  mkDir(FtpInfo ftp,String dirName) {
		FTPClient ftpClient = getClient(ftp);
		int re=0;
		try {
			//550   文件夹已存在  257 创建成功
			re = ftpClient.mkd(dirName);
		} catch (IOException e) {
			log.error("mkdir "+dirName+" failed");
		}finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return re==250;
	}
	public static boolean deleteFile(FtpInfo ftp ,String filePath){
		FTPClient ftpClient = getClient(ftp);
		boolean re=false;
		try {
			//250  删除成功 550 删除失败
			re = ftpClient.deleteFile(filePath);
			if(!re){
				int ddt=ftpClient.rmd(filePath);
				if(ddt==250){
					re=true;
				}
			}
			System.out.println(ftpClient.user("paas"));
		} catch (IOException e) {
			log.error("delete  failed");
		}finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return re;
	}
	public static boolean upload(FtpInfo ftp ,String path,InputStream ins){
		FTPClient ftpClient = getClient(ftp);
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.appendFile(path, ins);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean fileExists(FtpInfo ftp ,String filePath){
		FTPClient ftpClient = getClient(ftp);
		File fileObj = new File(filePath);
		String fileName=fileObj.getName();
		if(fileName==null || "".equals(fileName)){
			return false;
		}
		String parentPath=fileObj.getParent();
		System.out.println(parentPath+"   "+fileName);
		try {
			ftpClient.changeWorkingDirectory(parentPath);
			FTPFile[] ftpfiles=ftpClient.listFiles();
			for(FTPFile file:ftpfiles){
				if(fileName.equals(file.getName())){
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 新版ftp 添加或更新用户信息
	 * @param ftpAddress
	 * @param managerPort
	 * @param adminName
	 * @param adminPassword
	 * @param userName
	 * @param password
	 * @return
	 */
	public static String updateUser(String ftpAddress,String managerPort,String adminName,String adminPassword,
			String userName,String password){
		Map<String,Object> payload = new HashMap<String,Object>();
		Map<String,Object> content = new HashMap<String,Object>();
		payload.put("op", "updateuser");
		content.put("name", userName);
		content.put("password",password);
		content.put("writeable", true);
		payload.put("auth",MD5Util.md5(adminName+":"+adminPassword));
		payload.put("content", content);
		String messageSend=JSON.toJSONString(payload);
		int port=Integer.valueOf(managerPort);
		String ret=SocketClient.send(ftpAddress, port, messageSend);
		log.debug("update user info result:"+ret);
		return ret;
	}
	/**
	 * 上传文件
	 * @param ftp
	 * @param localFile
	 * @return
	 */
	public static boolean upload(FtpInfo ftp,String localFile){
		FTPClient ftpClient = getClient(ftp);
		File localF= new File(localFile);
		if(!localF.exists()){
			return false;
		}
		if(ftpClient==null){
			log.debug("connect ftp error!");
			return false;
		}
		InputStream ins=null;
		try {
			ins=new FileInputStream(localF);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.appendFile(ftp.getFile(), ins);
			
		} catch ( Exception e) {
			e.printStackTrace();
			 return false;
		}finally{
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ins!=null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	public static void main(String[] args) throws IOException {
		FtpInfo ftp = new FtpInfo("ftp://paas:paas@10.1.155.23");
		String filePath="/test/storm_supervisor201611241s64919.zip";
		System.out.println(fileExists(ftp,filePath));
			
	}
}
