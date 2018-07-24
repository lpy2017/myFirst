package com.dc.appengine.appsvn.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class FileWorker {
	private static FileWorker instance=null;
	private ExecutorService pool =null;
	public static FileWorker getInstance(){
		synchronized (FileWorker.class) {
			if(instance==null){
				instance=new FileWorker();
			}
		}
		return instance;
	}
	private Map<String,String> templates= new HashMap<String,String>();
	private FileWorker (){
		//clear();
		templateInit(); 
		//初始化线程池 用来加载倒计时任务
		pool = Executors.newFixedThreadPool(10);
		if(pool.isShutdown()){
			pool=Executors.newFixedThreadPool(10);
		}
		
	}
	private static Logger log= LoggerFactory.getLogger(FileWorker.class);
	public void clear(){
		String workDir=ConfigHelper.getValue("workPath");
		if(workDir.length()==1){
			return;
		}
		if("/home".equals(workDir) || "/root".equals(workDir)){
			return;
		}
		File dir= new File(workDir);
		if(!dir.exists() || !dir.isDirectory()){
			return;
		}
		for(File file:dir.listFiles()){
			try {
				FileUtil.forceDelete(file);
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
		}
	}
	public void templateInit(){
		print("init templates ..");
		URL url=FileWorker.class.getClassLoader().getResource("ziptemplate");
		if(url==null){
			return;
		}
		File baseDir = new File(url.getPath());
		
		if(baseDir.exists()&& baseDir.isDirectory()){
			File[] files=baseDir.listFiles();
			for(File file:files){
				if(file.isDirectory()){
					String tName=file.getName();
					String tPath=file.getAbsolutePath();
					templates.put(tName, tPath);
				}
			}
		}
		print("init templates ok");
	}
	public String uploadFileForNextOp(InputStream is,String fileName){
		print("receiving file["+fileName+"] from client");
		String fileId=UUID.randomUUID().toString();
		String workDir=ConfigHelper.getValue("workPath");
		String waitTime=ConfigHelper.getValue("workWait");
		long deleteWaitTime=10000;
		boolean deleteAfterUpload=false;
		if(waitTime!=null) {
			deleteAfterUpload=true;
			deleteWaitTime=Long.parseLong(waitTime);
		}
		String destFilePath=workDir+"/"+fileId;
		File f= new File(destFilePath);
		if(!f.exists()){
			f.mkdirs();
		}
		if(deleteAfterUpload){
			boolean writeOk=writeFile(is,destFilePath+"/"+fileName);
			if(writeOk){
				pool.execute(new Thread(new TempFileKiller(destFilePath,deleteWaitTime)));
				return fileId;
			}else{
				pool.execute(new Thread(new TempFileKiller(destFilePath,0)));
				return "error";
			}
		}
		print(fileName+" upload ok");
		return fileId;
		
	}
	public boolean writeFile(InputStream is, String destPath){
		FileOutputStream out=null;
		try{
			File destFile= new File(destPath);
			destFile.createNewFile();
			out= new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int rc=0;
			while((rc=is.read(buffer, 0, buffer.length))>0){
				out.write(buffer, 0, rc);
			}
			out.flush();
		}catch(Exception e){
			log.error("文件写入失败");
			return false;
		}finally{
			try {
				is.close();
				out.close();
			} catch (IOException e1) {
				 
			}
		}
		return true;
	}
 
	public boolean uploadToftp(String workId,String ftpUrl,String folder,boolean deleteWork){
		String workDir=ConfigHelper.getValue("workPath");
		String ftpHome=ConfigHelper.getValue("ftphome");
		boolean needUpload=true;
		if(ftpHome!=null){
			needUpload=false;
		}
		
		
		File dir=new File(workDir+"/"+workId);
		if(!dir.exists() || !dir.isDirectory()){
			return false;
		}
		File[]  files=dir.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				continue;
			}
			String name=file.getName();
			String remotePath=ftpUrl+"/"+folder+"/"+name;
			String localPath=workDir+"/"+workId+"/"+name;
			FtpInfo ftp = new FtpInfo(remotePath);
			print("start to upload file["+localPath+"] to "+remotePath);
			if(needUpload){
				boolean re=FtpUtil.upload(ftp, localPath);
				print("upload file["+localPath+"] ok" );
				if(!re){
					print("upload file["+localPath+"] error" );
					return re;
				}
			}else{
				
				String ftpPath=ftpHome+"/"+folder ;
				if(folder.startsWith("/")){
					ftpPath=ftpHome+folder;
				}
				print("start tp upload(cp) file["+localPath+"] ->"+ftpPath+"/"+name );
				boolean copyr=CopyFileUtil.copyFile(localPath, ftpPath+"/"+name, true);
				if(!copyr){
					print("upload file["+localPath+"] error" );
					return copyr;
				}
				print("copy result is"+copyr);
			}
			
		}
		if(deleteWork){
			try {
				FileUtil.forceDelete(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	public String getWorkSource(String fileId){
		
		String workRoot=ConfigHelper.getValue("workPath");
		
		File workDir= new File(workRoot+"/"+fileId);
		if(!workDir.exists() || !workDir.isDirectory())
		{
			log.error("work dir is not found");
			return null;
		}
		File[] files=workDir.listFiles();
		if(files.length==1){
			return files[0].getName();
		}else{
			log.error("file num in workdir ["+workDir.getAbsolutePath()+"] is not 1");
		}
		return null;
	}
	public  void mkdirFileList(String workId,String fileName){
		String workDir=ConfigHelper.getValue("workPath");
		String fileListFile=workDir+"/"+workId+"/temp/bin/filelist";
		File f= new File(fileListFile);
		try(BufferedWriter w= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)))){
			w.write(fileName);
		}catch(Exception e){
			log.error("write filelist error");
		}
	}
	public void mkZip(String workId,String template ,String zipName ){
		print ("start tp make "+template+" to"+zipName);
		if(!zipName.endsWith(".zip")){
			zipName=zipName+".zip";
		}
		String workDir=ConfigHelper.getValue("workPath");
		String fileName=getWorkSource(workId);
		workDir+="/"+workId;
		String tDir=templates.get( template );
		//step1  copy
		File tdirfile= new File(tDir);
		if(!tdirfile.exists()){
			return;
		}
		CopyFileUtil.copyDirectory(tDir,workDir+"/temp",true);
		CopyFileUtil.copyFile(workDir+"/"+fileName,workDir+"/temp/"+fileName, true);
		//step2 edit
		mkdirFileList(workId,fileName);
		//step3 compress
		ZipUtil.doCompress( new File(workDir+"/temp") ,  new File(workDir+"/"+zipName) );
		//step4 clear
		try {
			FileUtil.forceDelete(new File(workDir+"/temp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		print ("make "+template+" ok");
	}
 
	public static void print(String msg){
		System.out.println(msg);
		log.debug(msg);
	}
	 
}

class TempFileKiller implements Runnable{
	Logger log= LoggerFactory.getLogger(TempFileKiller.class);
	long timeWait=0l;
	String workPath="";
	public TempFileKiller(String workPath,long timeWait){
		this.timeWait=timeWait;
		this.workPath=workPath;
	}
	@Override
	public void run() {
		if(timeWait!=0l){
			try {
				Thread.sleep(timeWait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//获取开关
		File f= new File(workPath);
		if(f.exists()){
			try {
				FileUtil.forceDelete(f);
			} catch (IOException e) {
				log.debug("delete file error");
			}
		}
		
	}
	
}
