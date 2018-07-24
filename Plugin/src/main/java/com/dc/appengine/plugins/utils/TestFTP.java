package com.dc.appengine.plugins.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;

public class TestFTP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestFTP ftp =new TestFTP();
//		ftp.downloadComTest();
		ftp.downloadAr();
//		ftp.downloadCom();
//		ftp.downloadAr();
//		ftp.downloadCom();
	}

	
	
	public void downloadAr(){
		String url="ftp://paas:123456@10.1.108.33/packages/6932a861-ac6b-4b93-8d81-443a33eb05da/tomcat.jar";
		FtpTransferer ft =null;
		Boolean openResult= false;
		FtpInfo ftpInfo = null;
		String localPath="F:/test2/";
		Map<String,Object> resultMap= new HashMap<>();
		File file=null;
		resultMap = new HashMap<>();
		try {
			ftpInfo = new FtpInfo(url);
			ft = new FtpTransferer(ftpInfo, localPath);
			openResult= ft.open();
			String remotePath = ftpInfo.getFile();
			if(openResult){
				Map<String,Object> checkResult = ft.checkFileExistence(url);
				Object result = checkResult.get("result");
				if(!(Boolean)result){
					resultMap =  checkResult;
				}else{
					file = ft.download(remotePath, new File(remotePath).getName());
					if (file != null) {
						resultMap.put(Constants.Plugin.RESULT, true);
						resultMap.put("filePath", file.getAbsolutePath());
						resultMap.put(Constants.Plugin.MESSAGE, "下载成功，filePath ="+file.getAbsolutePath());
					} else {
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "下载失败！");
					}
				}
			}else{
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, LogRecord.getStackTrace(e));
		}finally {
			if(ftpInfo==null){
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp url异常！");
			}
			if(ft !=null && openResult){
				ft.close();
			}
		}
		System.out.println("下载工件结果==="+JSON.toJSONString(resultMap));
	}
    public void downloadCom(){
    	String url1="ftp://paas:123456@10.1.108.217/srv/unsalt/327/current/";
		FtpTransferer ft =null;
		Boolean openResult= false;
		FtpInfo ftpInfo = null;
		String localPath="F:/test2/";
		Map<String,Object> resultMap= new HashMap<>();
		try {
			ftpInfo = new FtpInfo(url1);
			ft = new FtpTransferer(ftpInfo, localPath);
			openResult= ft.open();
			if(openResult){
				System.out.println("下载组件登陆成功！");
				String filePath = ftpInfo.getFile();
				String ftpBasePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
				String downloadDir = filePath.substring(filePath.lastIndexOf("/") + 1);
				resultMap =ft.RecursionDownload(ftpBasePath, localPath, downloadDir);
				if(resultMap.get("result") ==null){
					resultMap.put(Constants.Plugin.RESULT, true);
					resultMap.put(Constants.Plugin.MESSAGE, "下载成功");
				}
			}else{
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp连接异常！");
			}
		}catch(Exception e){
			e.printStackTrace();
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, LogRecord.getStackTrace(e));
		}finally {
			if(ftpInfo==null){
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "ftp url异常！");
			}
			if(ft !=null && openResult){
				ft.close();
			}
		}
		System.out.println("下载组件结果==="+JSON.toJSONString(resultMap));
	}
    
    public void downloadComTest(){
    	String url1="ftp://paas:123456@10.1.108.33/srv/unsalt/327/current/";
		String localPath="F:/test2/";
    	FtpInfo ftpInfo = new FtpInfo(url1);
		String filePath = ftpInfo.getFile();
		String ftpBasePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
		String downloadDir = filePath.substring(filePath.lastIndexOf("/") + 1);
		RecursionDownload download = new RecursionDownload(ftpBasePath, localPath, ftpInfo);
		download.connect();
		download.getDir(downloadDir);
		System.out.println(JSON.toJSONString(download.getResult()));
    }
}
