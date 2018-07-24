package com.dc.appengine.plugins.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class RecursionDownload {
    private String ftpBasePath;  
    private String localBasePath;
    private String root;
    private FtpInfo ftpInfo;
    private Map<String, Object> result = new HashMap<>();
      
    private FTPClient ftpClient = new FTPClient();  
      
    public boolean connect() {
		try {
			ftpClient.setRemoteVerificationEnabled(false);
			ftpClient.connect(ftpInfo.getIp(), ftpInfo.getPort());
			boolean open = ftpClient.login(ftpInfo.getUserName(), ftpInfo.getPassword());
			if (!open) {
				System.out.println("连接异常！");
				result.put("result", false);
				result.put("message", "连接异常！");
			} else {
				root = ftpClient.printWorkingDirectory();
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
//				result.put("result", true);
//				result.put("message", "登陆ftpServer成功");
			}
			return open;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
    public RecursionDownload(String ftpBasePath, String localBasePath,FtpInfo ftpInfo) {  
        this.ftpBasePath = ftpBasePath;  
        this.localBasePath = localBasePath;
        this.ftpInfo = ftpInfo;
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
            if (ftpClient.changeWorkingDirectory(ftpBasePath + ftpPath)) {  
                FTPFile[] files = ftpClient.listFiles();  
                //将该FTP目录下面的文件及其相对ftpBasePath的相对路径  
                //放入到List当中。  
                for (int i = 0; i < files.length; i++) {  
                    if (files[i].isFile()) {  
                        String currentPwd = ftpClient.printWorkingDirectory();  
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
                        String currentPwd = ftpClient.printWorkingDirectory();  
                        String dirName = files[i].getName();  
                        String absolutePath = currentPwd + "/" + dirName;  
                        String relativePath = absolutePath.substring(ftpBasePath.length(),  
                                absolutePath.length());  
                        dirList.add(relativePath);  
                    }  
                }  
            }else{
            	result.put("result", false);
				result.put("message", ftpBasePath + ftpPath+"目录不存在！");
				return;
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        //先下载当前FTP目录下的文件到本地  
        syncFiles(fileList);  
        //下载当前FTP目录中的子目录到本地  
        syncDirs(dirList);  
          
    }
    
    
    private void syncFiles(ArrayList<SyncFTPFile> fileList) {  
        try {  
            System.out.println("ftp working pwd is: " + ftpClient.printWorkingDirectory());  
        } catch (IOException e1) {  
            // TODO Auto-generated catch block  
            e1.printStackTrace();  
        }  
        //将FTP上的文件下载到本地，此时文件的路径是相对ftpBasePath的，因此  
        //在下载是需要增加ftpBasePath构成全路径，作为retrieveFile的第一个参数  
        //传入FTPFile对象的主要作用是保留了ftp上文件的相关属性，比如：时间戳。  
        for (int i = 0; i < fileList.size(); i++) {  
            //crete local file  
            String remoteName = fileList.get(i).getPath();  
            String absolutePath = ftpBasePath +remoteName;  
            File localFile = new File(this.localBasePath + remoteName); 
//            !localFile.exists() ||   
//            compareTimeStamp(fileList.get(i).getFile(), localFile)
//            if () {}     
//            else {  
//                System.out.println("    ignore the file..." + remoteName);  
//            }  
            try {
                System.out.println("    downloading file......" + remoteName);  
                FileOutputStream fos = new FileOutputStream(localFile);  
                ftpClient.retrieveFile(absolutePath, fos);  
                fos.close();  
                //设置本地文件的时间戳为ftp文件的时间戳，  
                localFile.setLastModified(fileList.get(i).getFile()  
                        .getTimestamp().getTime().getTime());  
            } catch (FileNotFoundException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        
        }  
    }
    
    private void syncDirs(ArrayList<String> dirList) {  
        //递归调用getDir，下载各个子目录  
        for (int i = 0; i < dirList.size(); i++) {  
            getDir(dirList.get(i));  
        }  
    }
    public boolean close() {
		try {
			boolean result = ftpClient.logout();
			if (!result) {
			}
			ftpClient.disconnect();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
    
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url ="ftp://paas:123456@10.1.108.33/test";
	   FtpInfo ftpInfo = new FtpInfo(url);
      RecursionDownload test =new RecursionDownload("/", "F:/test/",ftpInfo);
      test.connect();
      test.getDir("test");
	}
	public Map<String, Object> getResult() {
		return result;
	}
	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

}
