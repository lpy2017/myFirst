package com.dc.bank.appengine.plugins.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.print.DocFlavor.STRING;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.bank.appengine.plugins.constants.Constants;

public class FileUtil {
	private static Logger log = Logger.getLogger( FileUtil.class );
	//将文件patchFile合并到文件srcFile中
		 public static void mergeZipPackage(File srcFile,File patchFile){
				String jarFileOutTmp=srcFile.getParentFile().getAbsolutePath()+File.separator+"tmp_"+srcFile.getName();
				ArchiveOutputStream out=null;
				ArchiveInputStream srcIn = null;
				ArchiveInputStream patchIn = null;
				try {
					FileUtils.copyFile(srcFile, new File(jarFileOutTmp), true);
					BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(srcFile));
					BufferedInputStream srcFileIn = new BufferedInputStream(new FileInputStream(jarFileOutTmp));
					BufferedInputStream PathFileIn = new BufferedInputStream(new FileInputStream(patchFile));
					out = new ArchiveStreamFactory()
							.createArchiveOutputStream(ArchiveStreamFactory.JAR, fileOut);
					srcIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, srcFileIn);
					patchIn = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, PathFileIn);
					//拷贝源文件
					JarArchiveEntry entrySrc = null;
					while((entrySrc=(JarArchiveEntry)srcIn.getNextEntry())!=null){
						out.putArchiveEntry(entrySrc);
						IOUtils.copy(srcIn, out);  
		                out.closeArchiveEntry();
					};
					//拷贝补丁文件
					JarArchiveEntry entryPath = null;
					while((entryPath=(JarArchiveEntry)patchIn.getNextEntry())!=null){
						out.putArchiveEntry(entryPath);
						IOUtils.copy(patchIn, out);  
		                out.closeArchiveEntry();
					};
		            out.flush();  
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						if(out !=null){
							out.close();
						}
						if(srcIn !=null){
							srcIn.close();
						}
						if(patchIn !=null){
							patchIn.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
		 }
		 
		 public static Map<String,Object> generateScriptFile(String CMD){
				Map<String,Object> resultMap = new HashMap<String,Object>();
				Boolean result=true;
				String message ="";
				//生成脚本文件
				String fileName="";
				boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
				if(isWin){
					fileName="tmpCMDScript"+UUID.randomUUID()+".bat";
				}else{
					fileName="tmpCMDScript"+UUID.randomUUID()+".sh";
				}
				String filePath="../tmp/"+fileName;
				File file = new File(filePath);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				FileOutputStream out=null;
				OutputStreamWriter outWR=null;
				BufferedWriter wr =null;
				try {
					out = new FileOutputStream(file);
					outWR=new OutputStreamWriter(out);
					wr=new BufferedWriter(outWR);
					wr.write(CMD);
					wr.flush();
					//设置文件权限
					file.setExecutable(true);
					file.setWritable(true);
					file.setReadable(true);
					log.debug("file is execute allow : " + file.canExecute());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					result=false;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					result=false;
					e.printStackTrace();
				}finally{
					try {
						if(wr !=null){
							wr.close();
						}
						if(outWR !=null){
							outWR.close();
						}
						if(out !=null){
							out.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(!result){
					message="生成scriptFile error";
				}else{
					try {
						String tmpFilePath =URLDecoder.decode(file.getAbsolutePath(), "utf-8");
						resultMap.put("filePath", tmpFilePath);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				resultMap.put("result",result);
				resultMap.put("message",message);
				return resultMap;
			}
		 
		 public static void modifyFile(String filePath,String type){
		    	Boolean needModify=true;
		    	//判断是否需要修改
		    	File file = new File(filePath);
		    	file.setExecutable(true);
		    	file.setReadable(true);
		    	file.setWritable(true);
		    	FileWriter fw=null;
		    	RandomAccessFile rfile =null;
		    	try {
					rfile = new RandomAccessFile(filePath, "r");
					long lenth = rfile.length();
					long start=rfile.getFilePointer();
					long nextend = start+lenth-1;
					String line;
					int c=-1;
					while (nextend > start) {
						rfile.seek(nextend);
						c=rfile.readByte();
						if(c== '\n' || c=='\r'){
							line = rfile.readLine();
							if(line !=null && !"".equals(line)){
								if(line.contains("exit")){
									needModify=false;
								}
								break;
							}
						}
						nextend--;
					}
					rfile.close();
					rfile=null;
					//写文件
					if(needModify){
						fw = new FileWriter(filePath, true);
						if(Constants.FILE_SUFFIX_PKB.equals(type)){
							fw.write("\r\n"+"/");
						}
						fw.write("\r\n"+"exit;");
						fw.flush();
						fw.close();
						fw=null;
					}

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					try {
						if(rfile!=null){
							rfile.close();
						}
						if(fw !=null ){
							fw.close();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    }
}
