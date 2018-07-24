package com.dc.cd.plugins.utils.transfer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.dc.cd.plugins.utils.transfer.FtpUtil;
import com.dc.cd.plugins.utils.transfer.ITransferer;
import com.dc.cd.plugins.utils.utils.ConfigHelper;
import com.dc.cd.plugins.utils.utils.FileUtil;
import com.dc.cd.plugins.utils.utils.FtpInfo;

public class FtpTransferer implements ITransferer {
	private String url;
	private int port;
	private String user;
	private String pwd;
	private String uploadDir;
	private String tmpDir;
	private String timeOut;
	private static Properties properties = new Properties();
	private FtpUtil util;

	public FtpTransferer(FtpInfo ftpInfo, String localPath) {
		this.url = ftpInfo.getIp();
		this.port = ftpInfo.getPort();
		this.user = ftpInfo.getUserName();
		this.pwd = ftpInfo.getPassword();
		this.timeOut = ConfigHelper.getValue("ftp.timeOut");
		this.uploadDir = ConfigHelper.getValue("ftp.uploadDir");
		this.tmpDir = localPath;
		util = new FtpUtil();
		util.setIp(url);
		util.setPort(port);
		util.setPassword(pwd);
		util.setUserName(user);
		util.setTimeout(Integer.valueOf(timeOut));
		setUploadDir(uploadDir);
		setTmpDir(tmpDir);
	}
	
	public FtpTransferer(FtpInfo ftpInfo, String localPath, String timeOut, String uploadDir) {
		this.url = ftpInfo.getIp();
		this.port = ftpInfo.getPort();
		this.user = ftpInfo.getUserName();
		this.pwd = ftpInfo.getPassword();
		this.timeOut = timeOut;
		this.uploadDir = uploadDir;
		this.tmpDir = localPath;
		util = new FtpUtil();
		util.setIp(url);
		util.setPort(port);
		util.setPassword(pwd);
		util.setUserName(user);
		util.setTimeout(Integer.valueOf(timeOut));
		setUploadDir(uploadDir);
		setTmpDir(tmpDir);
	}

	public FtpTransferer(String url, Integer port, String user, String pwd,
			String timeOut, String uploadDir, String tmpDir) {
		if (url == null || "".equals(url)) {
			url = ConfigHelper.getValue("ftp.url");
			port = Integer.valueOf(ConfigHelper.getValue("ftp.port"));
			user = ConfigHelper.getValue("ftp.user");
			pwd = ConfigHelper.getValue("ftp.pwd");
			timeOut = ConfigHelper.getValue("ftp.timeOut");
			uploadDir = ConfigHelper.getValue("ftp.uploadDir");
			tmpDir = ConfigHelper.getValue("ftp.tmpDir");
		}
		this.url = url;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
		this.timeOut = timeOut;
		this.uploadDir = uploadDir;
		this.tmpDir = tmpDir;

		util = new FtpUtil();
		util.setIp(url);
		util.setPort(port);
		util.setUserName(user);
		util.setPassword(pwd);
		util.setTimeout(Integer.valueOf(timeOut));
		setUploadDir(uploadDir);
		setTmpDir(tmpDir);
	}

	public FtpTransferer() {
		if(url ==null || "".equals(url)){
			url=ConfigHelper.getInstance().getValue("ftp.url");
			port=Integer.valueOf(ConfigHelper.getInstance().getValue("ftp.port"));
			user=ConfigHelper.getInstance().getValue("ftp.user");
			pwd=ConfigHelper.getInstance().getValue("ftp.pwd");
			timeOut=ConfigHelper.getInstance().getValue("ftp.timeOut");
			uploadDir=ConfigHelper.getInstance().getValue("ftp.uploadDir");
			tmpDir=ConfigHelper.getInstance().getValue("ftp.tmpDir");
		}
		util = new FtpUtil();
		util.setIp(url);
		util.setPort(port);
		util.setUserName(user);
		util.setPassword(pwd);
		util.setTimeout(Integer.valueOf(timeOut));
		setUploadDir(uploadDir);
		setTmpDir(tmpDir);
	}

	public FtpTransferer(FtpInfo ftpInfo,String ftpBasePath, String localPath) {
		this.url = ftpInfo.getIp();
		this.port = ftpInfo.getPort();
		this.user = ftpInfo.getUserName();
		this.pwd = ftpInfo.getPassword();
		this.timeOut = ConfigHelper.getValue("ftp.timeOut");
		this.uploadDir = ConfigHelper.getValue("ftp.uploadDir");
		this.tmpDir = localPath;
		util = new FtpUtil();
		util.setIp(url);
		util.setPort(port);
		util.setUserName(user);
		util.setPassword(pwd);
		util.setTimeout(Integer.valueOf(timeOut));
		setUploadDir(uploadDir);
		setTmpDir(tmpDir);
	}
	
	@Override
	public boolean open() {
		return util.connect();
	}

	@Override
	public void close() {
		util.disconnect();
	}

	@Override
	public String getRemotePath(String path, String name) {
		String s = path + "/" + name;
		s = s.replaceAll("//", "/");
		if (s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}
		s = uploadDir + s;
		return s;
	}

	@Override
	public String getLocalPath(String path, String name) {
		String s = path + "/" + name;
		s = s.replaceAll("//", "/");
		if (s.endsWith("/")) {
			s = s.substring(0, s.length() - 1);
		}
		if (!tmpDir.isEmpty()) {
			s = tmpDir + "/" + s;
		}
		return s;
	}

	@Override
	public boolean delete(String path, String fileName) {
		return util.delete(path, fileName);
	}

	@Override
	public String upload(File tmpFile, String path) {
		return util.upload(tmpFile, path, tmpFile.getName());
	}

	@Override
	public File download(String remotePath, String localPath) {
		if (localPath.startsWith(uploadDir)) {
			localPath = localPath.substring(uploadDir.length());

		}
		String s = util.download(tmpDir + "/" + localPath, remotePath);
		File file = null;
		if (s.equals("true")) {
			file = new File(tmpDir + "/" + localPath);
		} else {
			file = null;
		}
		return file;
	}

	@Override
	public void downloadContainer(String remotePath, String localPath) {
		if (localPath.startsWith(uploadDir)) {
			localPath = localPath.substring(uploadDir.length());
		}
		util.download(localPath, remotePath);
	}

	@Override
	public boolean deleteDir(String dir) {
		return util.deleteDir(dir);
	}

	@Override
	public void removeTmpFiles(File file, String dir) {
		try {
			FileUtil.forceDelete(file);
			FileUtil.deleteDirectory(new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public FtpUtil getUtil() {
		return util;
	}

	public void setUtil(FtpUtil util) {
		this.util = util;
	}

	@Override
	public void uploadDir(File tmpFile, String toDir) {
		util.uploadDir(tmpFile, toDir);
	}

	@Override
	public boolean delete(String path) {
		path = path.replaceAll("\\\\", "/");
		int i = path.lastIndexOf("/");
		if (i >= 0) {
			return delete(path.substring(0, i), path.substring(i + 1));
		} else {
			return delete("/", path);
		}
	}

	@Override
	public boolean checkFileExistence(String path, String fileName) {
		return util.checkFileExistence(uploadDir + path, fileName);
	}

	@Override
	public String upload(String filePathLocal, String filePathRemote,
			String fileNameRemote) {
		File tmpFile = new File(filePathLocal);
		if (filePathRemote.startsWith("/")) {
			filePathRemote = filePathRemote.substring(1);
		}
		return util.upload(tmpFile, uploadDir + filePathRemote, fileNameRemote);
	}

	@Override
	public String getRelativePath(String path) {
		if (!uploadDir.isEmpty() && path.startsWith(uploadDir)) {
			return path.substring(uploadDir.length());
		}
		return path;
	}

	@Override
	public void deleteTmpFile(File patchFile) {
		patchFile.delete();
	}

	@Override
	public boolean isDirectory(String path) {
		return util.isDirectory(path);
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	public Map<String,Object> RecursionDownload(String ftpBasePath, String localBasePath,String downloadDir) {  
		util.setFtpBasePath(ftpBasePath);
		util.setLocalBasePath(localBasePath);
		util.getDir(downloadDir);
		return util.getResult();
    }
	public Map<String,Object> checkFileExistence(String url){
		Map<String,Object> result = new HashMap<String, Object>();
		FtpInfo ftpInfo = new FtpInfo(url);
		String filePath=ftpInfo.getFile();
		File file = new File(filePath);
		String fileName=file.getName();
		String parentPath = file.getParentFile().getPath();
		if(!util.checkFileDirExistence(parentPath)){
			result.put("result", false);
			result.put("message", url+"异常【file parentDir donot exist in ftpServer】");
			return result;
		}else{
			if(!util.checkFileExistence(parentPath, fileName)){
				result.put("result", false);
				result.put("message", url+"异常【file donot exist in ftpServer】");
			}else{
				result.put("result", true);
				result.put("message", "文件存在ftpServer上");
			}
			return result;
		}
	}
	public static void main(String [] args){
		String ip="10.1.108.33";
		int port=21;
		int timeout=30000;
		String password="123456";
		String userName="paas";
		FtpUtil ftp = new FtpUtil();
		ftp.setIp(ip);
		ftp.setPort(port);
		ftp.setPassword(password);
		ftp.setUserName(userName);
	    ftp.setTimeout(timeout);
	    ftp.connect();
	    ftp.disconnect();
	    System.out.println("111-----");
	    ftp.connect();
	    System.out.println("222-----");
//	    ftp.download("../tmp/plugins.json", "plugins.json");
	}
}
