package com.dc.appengine.cloudui.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.dc.appengine.cloudui.entity.Host;

public class FileUtil {
	private static final int buffer = 2048;  
	public static List<Host>  getHost(String path){
		BufferedReader br=null;
		try{
			InputStreamReader isr=new InputStreamReader(
					new FileInputStream(path),"utf-8");
			br=new BufferedReader(isr);
			String data="";
			String msg="";
			while((msg=br.readLine())!=null){
				data+=msg+"\n";
			}
			String[] hosts = data.split("\n");
			List<List<String>> hostsLists = new ArrayList<List<String>>();
			for (int i = 0; i < hosts.length; i+=4) {
				List<String> subhosts = new ArrayList<String>();
				for (int j = i; j < i+4; j++) {
					subhosts.add(hosts[j]);
				}
				hostsLists.add(subhosts);

			}
			Map<String,String> map = new HashMap<String,String>();
			List<Host> hostLists = new ArrayList<Host>();
			for (List<String> hostsList : hostsLists) {


				for (String string : hostsList) {
					String[] str = string.split(":");
					String key = str[0].trim();
					String value = "";
					try {
						value = str[1].trim();
					} catch (Exception e) {
						continue;
					}
					map.put(key, value);
				}
				Host host = new Host(map.get("host"), map.get("user"), map.get("password"));
				hostLists.add(host);
			}

//			Gson gson=new Gson();
//			Host model=gson.fromJson(data, Host.class);
			return hostLists;

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void writeHost(String data,String path){
		PrintWriter pw=null;
		File file=null;
		try{
			file=new File(path);
			OutputStreamWriter osr=new OutputStreamWriter(new FileOutputStream(file),"utf-8");
			pw=new PrintWriter(osr);
			pw.write(data);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(pw!=null){
				pw.close();
			}
		}
	}
	
	  /** 
	   * 解压Zip文件 
	   * @param path 文件目录 
	   */  
	  public static Map<String,String> unZip(String path) {
		   Map<String,String> returnMap = new HashMap();
	       int count = -1;  
	       String savepath = "";  

	       File file = null;  
	       InputStream is = null;  
	       FileOutputStream fos = null;  
	       BufferedOutputStream bos = null;  

	       savepath = path.substring(0, path.lastIndexOf(".")); //保存解压文件目录  
	       new File(savepath).mkdir(); //创建保存目录  
	       ZipFile zipFile = null;  
	       try  
	       {  
	           zipFile = new ZipFile(path,"gbk"); //解决中文乱码问题  
	           Enumeration<?> entries = zipFile.getEntries();  

	           while(entries.hasMoreElements())  
	           {  
	               byte buf[] = new byte[buffer];  

	               ZipEntry entry = (ZipEntry)entries.nextElement();  

	               String filename = entry.getName();  
	               boolean ismkdir = false;  
	               if(filename.lastIndexOf("/") != -1){ //检查此文件是否带有文件夹  
	                  ismkdir = true;  
	               }  
	               filename = savepath + File.separator + filename;  

	               if(entry.isDirectory()){ //如果是文件夹先创建  
	                  file = new File(filename);  
	                  file.mkdirs();  
	                   continue;  
	               }  
	               file = new File(filename);  
	               if(!file.exists()){ //如果是目录先创建  
	                  if(ismkdir){  
	                  new File(filename.substring(0, filename.lastIndexOf("/"))).mkdirs(); //目录先创建  
	                  }  
	               }  
	               file.createNewFile(); //创建文件  

	               is = zipFile.getInputStream(entry);  
	               fos = new FileOutputStream(file);  
	               bos = new BufferedOutputStream(fos, buffer);  

	               while((count = is.read(buf)) > -1)  
	               {  
	                   bos.write(buf, 0, count);  
	               }  
	               bos.flush();  
	               bos.close();  
	               fos.close();  

	               is.close();  
	               
	               String zipName = entry.getName().substring(entry.getName().lastIndexOf("/")+1,entry.getName().length());
	               if(zipName.endsWith(".zip")||zipName.endsWith(".war")||zipName.endsWith(".jar")
	            		   ||zipName.endsWith(".tar.gz")||zipName.endsWith(".tar")){//工件包
	            	   returnMap.put(zipName,filename);
	               }
	           }  

	           zipFile.close();  

	       }catch(IOException ioe){  
	           ioe.printStackTrace();  
	       }finally{  
	              try{  
	              if(bos != null){  
	                  bos.close();  
	              }  
	              if(fos != null) {  
	                  fos.close();  
	              }  
	              if(is != null){  
	                  is.close();  
	              }  
	              if(zipFile != null){  
	                  zipFile.close();  
	              }  
	              }catch(Exception e) {  
	                  e.printStackTrace();  
	              }  
	          }  
	       return returnMap;
	      }  
}
