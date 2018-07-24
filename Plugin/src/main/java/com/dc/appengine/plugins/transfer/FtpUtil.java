package com.dc.appengine.plugins.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import com.dc.appengine.plugins.utils.SyncFTPFile;

public class FtpUtil {
	private static Logger logger = Logger.getLogger(FtpUtil.class);

	private String ip;
	private int port;
	private String userName;
	private String password;
	private FTPClient client;

	private String root;
	private String pwd;
	private String ftpBasePath;  //ftp文件（文件夹)的父路径
    private String localBasePath; //下载到本地的父路径
    private Map<String,Object> result= new HashMap<>(); //下载到本地的父路径

	public String getFtpBasePath() {
		return ftpBasePath;
	}

	public void setFtpBasePath(String ftpBasePath) {
		this.ftpBasePath = ftpBasePath;
	}

	public String getLocalBasePath() {
		return localBasePath;
	}

	public void setLocalBasePath(String localBasePath) {
		this.localBasePath = localBasePath;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setTimeout(int timeout) {
		client.setConnectTimeout(timeout);
		client.setDataTimeout(timeout);
		client.setDefaultTimeout(timeout);
//		try {
//			client.setSoTimeout(timeout);
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
	}

	public FtpUtil() {
		client = new FTPClient();
		client.setRemoteVerificationEnabled(false);
	}

	/**
	 * ftp上传
	 * 
	 * @param is
	 *            本地数据流
	 * @param path
	 *            上传目的目录
	 * @param fileName
	 *            上传后保存名称
	 * @return
	 */
	public String upload(InputStream is, String path, String fileName) {
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		try {
			cdRoot();
			cd(path);
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			boolean result = client.appendFile(fileName, is);
			if (!result) {
				logger.error("FTP 上传数据失败！");
			}
			return String.valueOf(result);
		} catch (IOException e) {
			logger.error("FTP 上传数据出错！", e);
			e.printStackTrace();
		}
		return "false";
	}

	public boolean rename(String path, String oldName, String newName) {
		try {
			cdRoot();
			cd(path);
			boolean result = client.rename(oldName, newName);
			if (!result) {
				logger.error("FTP 重命名文件失败！");
			}
			return result;
		} catch (IOException e) {
			logger.error("FTP 重命名文件出错！", e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ftp上传文件
	 * 
	 * @param file
	 *            本地文件
	 * @param destDir
	 *            上传目的目录
	 * @param destName
	 *            上传后保存名称
	 * @return
	 */
	public String upload(File file, String path, String destName) {
//		logger.info("ftp upload dest name: " + destName);
		if (destName.startsWith("/")) {
			destName = destName.substring(1);
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
//			logger.info("before cdRoot() pwd: "
//					+ client.printWorkingDirectory());
			cdRoot();
//			logger
//					.info("after cdRoot() pwd: "
//							+ client.printWorkingDirectory());
			cd(path);
//			logger.info("after cd(" + path + ") pwd: "
//					+ client.printWorkingDirectory());
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			boolean result = client.storeFile(destName, is);
			if (!result) {
				logger.error("FTP 上传文件失败！");
			}
			return String.valueOf(result);
		} catch (FileNotFoundException e) {
			logger.error("创建文件输入流失败！", e);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "false";
	}

	/**
	 * ftp下载
	 * 
	 * @param localPath
	 *            下载后本地保存路径
	 * @param remotePath 远程路径不用以File.seperator开头
	 *            远程文件路径
	 * @return
	 */
	public String download(String localPath, String remotePath) {
		FileOutputStream fos = null;
		try {
			File localFile = new File(localPath);
			File parent = localFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			} else {
				if (localFile.exists()) {
					localFile.delete();
				}
			}
			fos = new FileOutputStream(localFile);
			cdRoot();
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.setRemoteVerificationEnabled(false);
//			client.enterLocalPassiveMode();
			remotePath = new String(remotePath.getBytes("UTF-8"),"ISO-8859-1");
			if (client.retrieveFile(remotePath, fos)) {
				return "true";
			} else {
				logger.error("FTP 下载文件失败！");
			}
		} catch (IOException e) {
			logger.error("FTP 下载文件出错！", e);
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "false";
	}

	public InputStream download(String remotePath) {
		try {
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.enterLocalPassiveMode();
			InputStream is = client.retrieveFileStream(remotePath);
			client.completePendingCommand();
			return is;
		} catch (IOException e) {
			logger.error("FTP 下载出错！", e);
			e.printStackTrace();
		}
		return null;
	}

	public boolean connect() {
		try {
			client.connect(ip, port);
			boolean result = client.login(userName, password);
			if (!result) {
				logger.error("FTP 登陆失败！");
			} else {
				client.enterLocalPassiveMode();
				root = client.printWorkingDirectory();
				pwd = root;
			}
			return result;
		} catch (IOException e) {
			logger.error("FTP 连接异常！", e);
			e.printStackTrace();
		}
		return false;
	}

	public boolean disconnect() {
		try {
			boolean result = client.logout();
			if (!result) {
				logger.error("FTP 退出登陆失败！");
			}
			client.disconnect();
			return result;
		} catch (IOException e) {
			logger.error("FTP连接关闭异常！", e);
			e.printStackTrace();
		}
		return false;
	}

	public void cd(String dir) {
		try {
			dir = dir.replaceAll("\\\\", "/");
			String[] dirs = dir.split("/");
			for (String d : dirs) {
				if (!client.changeWorkingDirectory(d)) {
					boolean b = false;
					try {
						b = client.makeDirectory(d);
					} catch (Exception e) {
						logger.error("FTP 创建路径失败: " + d);
						e.printStackTrace();
					}
					if (b) {
						boolean result = client.changeWorkingDirectory(d);
						if (!result) {
							logger.error("FTP 切换路径失败: " + d);
							break;
						}
						pwd = client.printWorkingDirectory();
					} else {
						break;
					}
				}
			}
		} catch (IOException e) {
			logger.error("FTP 切换路径出错！", e);
			e.printStackTrace();
		}
	}

	public void cdup() {
		try {
			boolean result = client.changeToParentDirectory();
			if (!result) {
				logger.error("FTP 切换到上级目录失败！");
			}
			pwd = client.printWorkingDirectory();
		} catch (IOException e) {
			logger.error("FTP 切换到上级目录出错！", e);
			e.printStackTrace();
		}
	}

	public boolean mkdirs(String dirs) {
		try {
			dirs = new String(dirs.getBytes(), "ISO-8859-1");
			String[] d = dirs.split("/");
			if (d.length > 0) {
				for (int i = 0; i < d.length; i++) {
					if (d[i].equals(".") || d[i].equals("/")) {
						continue;
					}
					if (!client.changeWorkingDirectory(d[i])) {
						boolean result = client.makeDirectory(d[i]);
						if (!result) {
							logger.error("FTP 创建目录失败: " + dirs);
							break;
						}
						client.changeWorkingDirectory(d[i]);
						pwd = client.printWorkingDirectory();
					}
				}
			}
			return true;
		} catch (IOException e) {
			logger.error("FTP 创建路径出错！", e);
			e.printStackTrace();
		}
		return false;
	}

	public boolean delete(String path, String fileName) {
		try {
			cdRoot();
			cd(path);
			boolean result = client.deleteFile(fileName);
			if (!result) {
				boolean exist = checkFileExistence(path, fileName);
				if (exist) {
					logger.error("FTP 删除文件失败！文件：" + path + "/" + fileName);
				}
			}
			return result;
		} catch (IOException e) {
			logger.error("FTP 删除文件出错！文件：" + path + "/" + fileName, e);
			e.printStackTrace();
		}
		return false;
	}

	public String pwd() {
		return pwd;
	}

	private void cdRoot() {
		try {
			boolean result = client.changeWorkingDirectory(root);
			if (!result) {
				logger.error("FTP 切换到根目录失败！");
			}
			pwd = client.printWorkingDirectory();
		} catch (IOException e) {
			logger.error("FTP 切换到根目录出错！", e);
			e.printStackTrace();
		}
	}

	public String downloadDir(String tmpFilePath, String toDir) {
		File f = new File(toDir);
		f.mkdirs();
		try {
			cdRoot();
			cd(tmpFilePath);
			client.setListHiddenFiles(true);
			client.enterLocalPassiveMode();
			FTPFile[] files = client.listFiles();
			if (files != null && files.length > 0) {
				for (FTPFile file : files) {
					if (file.getName().equals(".")
							|| file.getName().equals("..")) {
						continue;
					}
					if (file.isDirectory()) {
						downloadDir(tmpFilePath + "/" + file.getName(), toDir
								+ "/" + file.getName());
						cdup();
					} else {
						download(f.getAbsolutePath() + "/" + file.getName(),
								file.getName());
					}
				}
			}
			client.setListHiddenFiles(false);
			return f.getPath();
		} catch (IOException e) {
			logger.error("FTP 下载目录出错！", e);
			e.printStackTrace();
		}
		return null;
	}

	public void uploadDir(File dir, String toDir) {
		cdRoot();
		mkdirs(toDir);
		try {
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				for (File f : files) {
					if (f.isDirectory()) {
						uploadDir(f, toDir + "/" + f.getName());
						cdup();
					} else {
						delete(toDir, f.getName());
						uploadToCurDir(f, f.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("FTP 上传目录出错！", e);
			e.printStackTrace();
		}
	}

	private boolean uploadToCurDir(File f, String name) {
		InputStream is = null;
		try {
			is = new FileInputStream(f);
			client.printWorkingDirectory();
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
			boolean result = client.storeFile(name, is);
			if (!result) {
				logger.error("FTP 上传到当前目录失败！");
			}
			return result;
		} catch (IOException e) {
			logger.error("FTP 上传到当前目录出错！", e);
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean deleteDir(String dir) {
		try {
			cdRoot();
			cd(dir);
			client.setListHiddenFiles(true);
			client.enterLocalPassiveMode();
			FTPFile[] files = client.listFiles();
			if (files != null && files.length > 0) {
				for (FTPFile file : files) {
					if (file.getName().equals(".")
							|| file.getName().equals("..")) {
						continue;
					}
					if (file.isDirectory()) {
						deleteDir(dir + "/" + file.getName());
					} else {
						delete(dir, file.getName());
					}
				}
			}
			client.setListHiddenFiles(false);
			cdup();
			if (dir.endsWith("/")) {
				dir = dir.substring(0, dir.length() - 1);
			}
			String pathName = dir.substring(dir.lastIndexOf("/") + 1);
			boolean b = client.removeDirectory(pathName);
			if (!b) {
				logger.error("FTP 删除目录失败: " + dir);
			}
			return b;
		} catch (IOException e) {
			logger.error("FTP 删除目录出错！", e);
			e.printStackTrace();
		}
		return false;
	}

	public boolean checkFileExistence(String path, String fileName) {
		try {
			cdRoot();
			cd(path);
			client.setControlEncoding("UTF-8");
			String[] names = client.listNames();
			if (names != null && names.length > 0) {
				for (String s : names) {
					if (fileName.equals(s)) {
						return true;
					}
				}
			}
			return false;
		} catch (IOException e) {
			logger.error("FTP 检查文件是否存在出错！", e);
			e.printStackTrace();
		}
		return false;
	}
	 public void getDir(String ftpPath) {  
	        //create the dir named ftpPath in localPath  
	        ArrayList<SyncFTPFile> fileList = new ArrayList<SyncFTPFile>();  
	        ArrayList<String> dirList = new ArrayList<String>();  
	        //在本地目录下面建立要下载的FTP目录  
	        File localDir = new File(this.localBasePath +ftpPath);  
	        if (!localDir.exists()) {  
	            if (localDir.mkdirs()) {  
	                System.out.println("create dir success." + localDir);  
	            }  
	        }  
	        try {  
	            //切换到需要下载的FTP目录  
	            if (client.changeWorkingDirectory(ftpBasePath + ftpPath)) {  
	                FTPFile[] files = client.listFiles();  
	                //将该FTP目录下面的文件及其相对ftpBasePath的相对路径  
	                //放入到List当中。  
	                for (int i = 0; i < files.length; i++) {  
	                    if (files[i].isFile()) {  
	                        String currentPwd = client.printWorkingDirectory();  
	                        String fileName = files[i].getName();  
	                        String absolutePath = currentPwd + "/" + fileName;  
	                        String relativePath = absolutePath.substring(ftpBasePath.length(),  
	                                absolutePath.length());  
	                        SyncFTPFile syncFile = new SyncFTPFile();  
	                        syncFile.setFile(files[i]);  
	                        syncFile.setPath(relativePath);  
	                        fileList.add(syncFile);  
	                    }  
	                    //将该FTP目录下面的目录相对ftpBasePath的相对路径  
	                    //放入到List当中  
	                    if (files[i].isDirectory() && !files[i].getName().equalsIgnoreCase(".")   
	                            && !files[i].getName().equalsIgnoreCase("..")) {  
	                        String currentPwd = client.printWorkingDirectory();  
	                        String dirName = files[i].getName();  
	                        String absolutePath = currentPwd + "/" + dirName;  
	                        String relativePath = absolutePath.substring(ftpBasePath.length(),  
	                                absolutePath.length());  
	                        dirList.add(relativePath);  
	                    }  
	                }  
	            }  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	            this.result.put("result", false);
	            this.result.put("message", e.getMessage());
	        }  
	        //先下载当前FTP目录下的文件到本地  
	        syncFiles(fileList);  
	        //下载当前FTP目录中的子目录到本地  
	        syncDirs(dirList);  
	          
	    }
	    
	    
	    private void syncFiles(ArrayList<SyncFTPFile> fileList) {  
	        //将FTP上的文件下载到本地，此时文件的路径是相对ftpBasePath的，因此  
	        //在下载是需要增加ftpBasePath构成全路径，作为retrieveFile的第一个参数  
	        //传入FTPFile对象的主要作用是保留了ftp上文件的相关属性，比如：时间戳。  
	        for (int i = 0; i < fileList.size(); i++) {  
	            //crete local file  
	            String remoteName = fileList.get(i).getPath();  
	            String absolutePath = ftpBasePath +remoteName;  
	            File localFile = new File(this.localBasePath + remoteName); 
	            try {
	                System.out.println("    downloading file......" + remoteName);  
	                FileOutputStream fos = new FileOutputStream(localFile);
//	                client.setBufferSize(1024);
//	    			client.setControlEncoding("UTF-8");
//	    			client.setFileType(FTP.BINARY_FILE_TYPE);
//	    			client.enterLocalPassiveMode();
	                client.retrieveFile(absolutePath, fos);  
	                fos.close();  
	                //设置本地文件的时间戳为ftp文件的时间戳，  
//	                localFile.setLastModified(fileList.get(i).getFile()  
//	                        .getTimestamp().getTime().getTime());  
	            } catch (FileNotFoundException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	                this.result.put("result", false);
		            this.result.put("message", e.getMessage());
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	                this.result.put("result", false);
		            this.result.put("message", e.getMessage());
	            }  
	        
	        }  
	    }
	    
	    private void syncDirs(ArrayList<String> dirList) {  
	        //递归调用getDir，下载各个子目录  
	        for (int i = 0; i < dirList.size(); i++) {  
	            getDir(dirList.get(i));  
	        }  
	    }
	
	
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public FTPClient getClient() {
		return client;
	}

	public void setClient(FTPClient client) {
		this.client = client;
	}

	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public boolean isDirectory(String path) {
		try {
			cdRoot();
			client.setBufferSize(1024);
			client.setControlEncoding("UTF-8");
			client.enterLocalPassiveMode();
			FTPFile[] files = client.listFiles(path);
			if (files != null && files.length > 0) {
				if (files.length == 1 && files[0].isFile()
						&& path.endsWith(files[0].getName())) {
					return false;
				}
			}
			return true;
		} catch (IOException e) {
		}
		return false;
	}
	
	public Boolean checkFileDirExistence(String dirPath){
		try {
			if(client.changeWorkingDirectory(dirPath)){
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
