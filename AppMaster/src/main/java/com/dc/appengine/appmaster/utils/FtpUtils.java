package com.dc.appengine.appmaster.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.entity.RequestClientENV;

public class FtpUtils {
	private static final Logger log = LoggerFactory.getLogger(FtpUtils.class);
	public FTPClient ftp;  
	public ArrayList<String> arFiles;  
	List nullList = new ArrayList();
	String strencoding = "GBK";


	public FtpUtils(){
		ftp = new FTPClient();  
		ftp.setRemoteVerificationEnabled(false);
		arFiles = new ArrayList<String>();  
		ftp.setDataTimeout(RequestClientENV.ftpDataTimeout);       //设置传输超时时间为30秒 
		ftp.setConnectTimeout(RequestClientENV.ftpConnectTimeout);
	}
	/** 
	 * 重载构造函数 
	 * @param isPrintCommmand 是否打印与FTPServer的交互命令 
	 */  
	public FtpUtils(boolean isPrintCommmand){  
		ftp = new FTPClient(); 
		ftp.setRemoteVerificationEnabled(false);
		arFiles = new ArrayList<String>();  
		if(isPrintCommmand){  
			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));  
		}  
	}  
	/** 
	 * 登陆FTP服务器 
	 * @param host FTPServer IP地址 
	 * @param port FTPServer 端口 
	 * @param username FTPServer 登陆用户名 
	 * @param password FTPServer 登陆密码 
	 * @return 是否登录成功 
	 * @throws IOException 
	 */  
	public boolean login(String host,int port,String username,String password) throws IOException{  
		this.ftp.connect(host,port);  
		if(FTPReply.isPositiveCompletion(this.ftp.getReplyCode())){  
			if(this.ftp.login(username, password)){  
				this.ftp.enterLocalPassiveMode();
				this.ftp.setControlEncoding(strencoding);  
				return true;  
			}  
		}  
		if(this.ftp.isConnected()){  
			this.ftp.disconnect();  
		}  
		return false;  
	}  

	/** 
	 * 关闭数据链接 
	 * @throws IOException 
	 */  
	public void disConnection() throws IOException{  
		if(this.ftp.isConnected()){  
			this.ftp.disconnect();  
		}  
	}  
	/** 
	 * 递归遍历出目录下面所有文件 
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束 
	 * @throws IOException 
	 */  
	public void List(String pathName) throws IOException{  
		if(pathName.startsWith("/")&&pathName.endsWith("/")){  
			String directory = pathName;  
			//更换目录到当前目录  
			this.ftp.changeWorkingDirectory(directory);  
			FTPFile[] files = this.ftp.listFiles();  
			for(FTPFile file:files){  

				if(file.isFile()){  
					arFiles.add(directory+file.getName());  

				}else if(file.isDirectory()){  
					List(directory+file.getName()+"/");  
				}  
			}  
		}  
	}  

	/** 
	 * 递归遍历目录下面指定的文件名 
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束 
	 * @param ext 文件的扩展名 
	 * @throws IOException  
	 */  
	public void List(String pathName,String ext) throws IOException{  
		if(pathName.startsWith("/")&&pathName.endsWith("/")){  
			String directory = pathName;  
			//更换目录到当前目录  
			this.ftp.changeWorkingDirectory(directory);  
			FTPFile[] files = this.ftp.listFiles();  
			for(FTPFile file:files){  
				if(file.isFile()){  
					if(file.getName().endsWith(ext)){  
						arFiles.add(directory+file.getName());  
					}  
				}else if(file.isDirectory()){  
					List(directory+file.getName()+"/",ext);  
				}  
			}  
		}  
	}  

	/** 
	 * 递归遍历出目录下面所有文件 返回树形结构的json
	 * @param pathName 需要遍历的目录，必须以"/"开始和结束 
	 * @throws IOException 
	 */  
	public List<String> List2Tree(String pathName) throws IOException{  
		List<String> arFiles = new ArrayList<String>() ;  
		List<String> nullList = new ArrayList<String>();
		if(pathName.startsWith("/")&&pathName.endsWith("/")){  
			String directory = pathName;  
			//更换目录到当前目录  
			this.ftp.changeWorkingDirectory(directory);  
			FTPFile[] files = this.ftp.listFiles();  
			for(FTPFile file:files){  
				if(file.isDirectory()){  
					String s = "{\"directory\": \""+file.getName()+"\",\"children\":"
							+ List2Tree(directory+file.getName()+"/") +"}";
					arFiles.add(s);
				}else if(file.isFile()){
					String s = "{\"file\": \""+file.getName()+"\",\"children\":"
							+ nullList +"}";
					arFiles.add(s);

				}  
			}

			return arFiles;
		}else{
			return nullList;  
		}
	}  
	
	public static List ListLocal2Tree(String pathName) throws IOException{  
		List<String> arFiles = new ArrayList() ;  
		List nullList = new ArrayList();
		File filexxx = new File(pathName);
		File[] files = filexxx.listFiles();
		if (null != files) {
		for(File file:files){  
			if(file.isDirectory()){  
				String s = "{\"directory\": \""+file.getName()+"\",\"children\":"
						+ ListLocal2Tree(file.getAbsolutePath()) +"}";
				arFiles.add(s);
			}else if(file.isFile()){
				String s = "{\"file\": \""+file.getName()+"\",\"children\":"
						+ nullList +"}";
				arFiles.add(s);

			}  
		}
			return arFiles;
		}else{
			return nullList;
		}
	}

	public  List listFiles(String pathName)throws IOException{
		List<String> arFiles = new ArrayList() ;  
		String directory = pathName;  
		//更换目录到当前目录  
		this.ftp.changeWorkingDirectory(directory); 
		FTPFile[] files = this.ftp.listFiles();  
		for(FTPFile file:files){  
			arFiles.add(file.getName());
		}
		return arFiles;


	}
	public String getFileText(String fileName) throws IOException{
		StringBuilder builder = null;
		int end = fileName.lastIndexOf("/");
		String pathName = fileName.substring(0, end+1);
		fileName = fileName.substring(end+1,fileName.length());
//		ftp.setConnectTimeout(1000);
		this.ftp.changeWorkingDirectory(pathName);
//		FTPFile[] files = this.ftp.listFiles();
//		String[] filenames = this.ftp.listNames();
//		for (String file : filenames) {
//			if (fileName.equals(file)) {
				InputStream ins = this.ftp.retrieveFileStream(fileName);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(ins, strencoding));
				//		byte[] b = new byte[1024];
				//		try (InputStream in = ins) {
				//			IOUtils.read(in, b);
				//			
				//		}
				String line;
				builder = new StringBuilder(150);
				while ((line = reader.readLine()) != null) {
					builder.append(line+"\n");
				}
				reader.close();
				//	   System.out.println(builder);
				if (ins != null) {
					ins.close();
				}
//			}
//		}
		return builder.toString();
	}
	
	public boolean isLegalUrl(String url) throws IOException{
		boolean isLegalUrl = false;
//		url = url.replace("\\", "/");
		String pathName = url.substring(0, url.lastIndexOf("/")+1);
		String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
		this.ftp.changeWorkingDirectory(pathName);
//		this.ftp.doCommand("opts", "utf8 off");
		String[] filenames = this.ftp.listNames();
		FTPFile[] files = this.ftp.listFiles();
		for (FTPFile file : files) {
			fileName = new String(fileName.getBytes(),strencoding);
			if (fileName.equals(file.getName())) {
				isLegalUrl = true;
				break;
			}
		}
		return isLegalUrl;
	}
	
	/*
	 * 删除多级目录
	 */
	public static boolean deleteFolder(String timeOut,String url, int port, String username, String password, String path) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
		ftp.setConnectTimeout(Integer.parseInt(timeOut));
		ftp.setDataTimeout(Integer.parseInt(timeOut));
		ftp.setDefaultTimeout(Integer.parseInt(timeOut));
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			String[] paths = path.split("/");
			for(int i=0;i<paths.length-1;i++){
				ftp.changeWorkingDirectory(paths[i]);
			}
			boolean deleSucc = deleteFolder(ftp,paths[paths.length-1]);//删除单级父目录
			ftp.logout();
			return deleSucc;
		} catch (Exception e) {
//			log.info("thread "+Thread.currentThread().getName()+" delete ftp currentPath"+path+" error" +getStackTrace(e));
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return success;
	}
	
	
	/**
	 * 删除文件或文件夹
	 * 
	 * @param path
	 *            待删除文件的绝对路径
	 * @return boolean
	 */
	public static boolean deleteFolder(FTPClient ftpClient,String path) throws Exception{

		if(path == null || "".equals(path.trim()) || "/".equals(path.trim())){
			throw new Exception("==========当前待删除路径为ftp根目录==========");
		}
		//如果是文件直接删除
		boolean result = ftpClient.deleteFile(path);

		if (ftpClient.changeWorkingDirectory(path)) {
			//是文件夹
			FTPFile[] ftpFiles = ftpClient.listFiles();
			if (ftpFiles == null || ftpFiles.length <= 0) {
				//文件夹文空直接删除
				ftpClient.changeToParentDirectory();
				ftpClient.removeDirectory(path);
				return true;
			}
			for (FTPFile ftpFile : ftpFiles) {
				//文件夹不为空，先删文件再删文件夹
				if(ftpFile.isDirectory()){
					deleteFolder(ftpClient,ftpFile.getName());
				}else{
					ftpClient.deleteFile(ftpFile.getName());
				}
			}
			ftpClient.cdup();
			result = ftpClient.removeDirectory(path);
		}
		return result;
	}
	
	
	
	public static void main(String[] args) throws IOException {  
		FtpUtils f = new FtpUtils(); 
		
		List<String> fff = null;
		List<String> ss = null;
		long a = System.currentTimeMillis();
		System.out.println(a);
		if(f.login("10.1.108.33", 21, "paas", "123456")){  
			//			fff=  f.List2Tree("/war/931675f8-2dd2-4203-b444-f8ceea2326b4/dangqun/");  
			f.getFileText("/war/931675f8-2dd2-4203-b444-f8ceea2326b4/dangqun/WEB-INF/classes/config/environments/development-context.properties");
		}  
		f.disConnection();  
		//		long b = System.currentTimeMillis();
		//		System.out.println(b);
		//		System.out.println(b-a);
		//		String json = JSONArray.toJSONString(fff);
		//
		//
		//		json = json.replace("\\\"", "\"");
		//		json = json.replace("\"{", "{");
		//		json = json.replace("}\"", "}");
		//		System.out.println(json);


	}  
	
	public  static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

}
