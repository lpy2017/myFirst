package com.dc.cd.plugins.utils.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.cd.plugins.utils.UtilsPlugin;

public class JavaZipUtil {
	private static Logger log = LoggerFactory.getLogger(JavaZipUtil.class);
	public static Map<String,Object> unzip(String sourceFile,
			boolean delSource,Map<String,String> subFilesMap) {
		Map<String,Object> result=new HashMap<String, Object>();
		File file = new File(sourceFile);
		String parentPath= file.getParent();
		if (!file.exists()) {
			log.error("zip file:" + sourceFile + " not found");
			result.put("message", "zip file:" + sourceFile + " not found");
			result.put("result", false);
			return result;
		}
		if(!sourceFile.endsWith(".zip")&&!sourceFile.endsWith(".war")&&!sourceFile.endsWith(".jar")
				&&!sourceFile.endsWith(".tar")&&!sourceFile.endsWith(".tar.gz")){
			log.error("the type of file [" + sourceFile + "] is not in[.zip .war .jar .tar .tar.gz]");
			result.put("message", "the type of file [" + sourceFile + "] is not in[.zip .war .jar .tar .tar.gz]");
			result.put("result", false);
			return result;
		}
		String outputFolder="";
		try {
			if(subFilesMap.size() !=0){
				if(subFilesMap.containsKey("/")){//拷贝压缩包
					outputFolder = subFilesMap.get("/").toString();
					if (!outputFolder.endsWith("/")) {
						outputFolder += "/";
					}
					if("/".equals(outputFolder)){
						outputFolder=parentPath;
					}
					Boolean cpresult=copyFile(new FileInputStream(file.getAbsolutePath()), outputFolder+File.separator+file.getName(), true);
					if(cpresult){
						result.put("message", "文件 "+file+"解压成功！");
						result.put("result", true);
						subFilesMap.remove("/");//移除已执行过的key
					}else{
						result.put("message", "文件 "+file+"解压失败！");
						result.put("result", false);
						return result;
					}
				}
				if(subFilesMap.containsKey(file.getName())){//解压zip文件
					outputFolder = subFilesMap.get(file.getName()).toString();
					if (!outputFolder.endsWith("/")) {
						outputFolder += "/";
					}
					if("/".equals(outputFolder)){
						outputFolder=parentPath;
					}
					result=unZipFile(file,"", outputFolder);
					Boolean uzresult = (Boolean) result.get("result");
					if(uzresult){
						subFilesMap.remove(file.getName());//移除已执行过的key
					}else{
						return result;
					}
				}
				for(Map.Entry<String, String> map : subFilesMap.entrySet()){//将压缩包中，指定的文件，解压到特定目录
					String key=map.getKey().toString();
					String value = map.getValue().toString();
					outputFolder = value;
					if (!outputFolder.endsWith("/")) {
						outputFolder += "/";
					}
					if("/".equals(outputFolder)){
						outputFolder=parentPath;
					}
					result=unZipFile(file,key, outputFolder);
					Boolean uzresult = (Boolean) result.get("result");
					if(!uzresult){
						return result;
					}
				}
			}
		} catch (IOException e) {
			log.error("zip解压失败" + e.getMessage(), e);
			result.put("message", "zip解压失败" + e.getMessage());
			result.put("result", false);
			return result;
		}
		if (delSource) {
			log.debug("delete file: " + sourceFile);
			file.delete();
		}
		return result;
	}
	public static Map<String,Object> unZipFile(File file,String name ,String targetPath){
		if(file.getName().endsWith("tar")){
			return unZipFile4Tar(file, name, targetPath, "tar");
		}else if(file.getName().endsWith("tar.gz")){
			return unZipFile4Tar(file, name, targetPath, "tar.gz");
		}
		Map<String,Object> result= new HashMap<String, Object>();
		ArchiveInputStream in =null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			//获得输出流
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
			in = new ArchiveStreamFactory()
					.createArchiveInputStream(ArchiveStreamFactory.JAR,
							bufferedInputStream,"GBK");
			JarArchiveEntry entry = null;
			Boolean unzipSubFile=false;
			if(!JudgeUtil.isEmpty(name)){
				unzipSubFile=true;
			}
			String entryName="";
			//循环遍历解压
			while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
				entryName = entry.getName();
				if(unzipSubFile){
					if(entryName.startsWith(name)){
						if (entry.isDirectory()) {
							new File(targetPath, entry.getName()).mkdir();
						} else {
							OutputStream out = FileUtils.openOutputStream(new File(
									targetPath, entry.getName()));
							IOUtils.copy(in, out);
							out.close();
						}	
					}
				}else{
					if (entry.isDirectory()) {
						new File(targetPath, entry.getName()).mkdir();
					} else {
						OutputStream out = FileUtils.openOutputStream(new File(
								targetPath, entry.getName()));
						IOUtils.copy(in, out);
						out.close();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result.put("message", "未找到文件 "+file+" : "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		} catch (ArchiveException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 压缩格式异常: "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 读写异常: "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		}finally{
			try {
				if(in !=null){
					in.close();
				}
				if(fis !=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result.put("message", "完成文件 "+file+" 的解压！");
		result.put("result", true);
		return result;
	}
	public static Map<String,Object> unZipFile4Tar(File file,String name ,String targetPath,String type){
		Map<String,Object> result= new HashMap<String,Object>();
		ArchiveInputStream in =null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			//获得输出流
			BufferedInputStream bufferedInputStream =null;
			if("tar.gz".equals(type)){
				bufferedInputStream = new BufferedInputStream(new GzipCompressorInputStream(fis));
			}else{
				bufferedInputStream = new BufferedInputStream(fis);
			}
			in = new ArchiveStreamFactory()
					.createArchiveInputStream(ArchiveStreamFactory.TAR,
							bufferedInputStream,"GBK");
			TarArchiveEntry entry = null;
			Boolean unzipSubFile=false;
			if(!JudgeUtil.isEmpty(name)){
				unzipSubFile=true;
			}
			String entryName="";
			//循环遍历解压
			while ((entry = (TarArchiveEntry) in.getNextEntry()) != null) {
				entryName = entry.getName();
				if(unzipSubFile){
					if(entryName.startsWith(name)){
						if (entry.isDirectory()) {
							new File(targetPath, entry.getName()).mkdir();
						} else {
							OutputStream out = FileUtils.openOutputStream(new File(
									targetPath, entry.getName()));
							IOUtils.copy(in, out);
							out.close();
						}	
					}
				}else{
					if (entry.isDirectory()) {
						new File(targetPath, entry.getName()).mkdir();
					} else {
						OutputStream out = FileUtils.openOutputStream(new File(
								targetPath, entry.getName()));
						IOUtils.copy(in, out);
						out.close();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result.put("message", "未找到文件 "+file+" : "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		} catch (ArchiveException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 压缩格式异常: "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 读写异常: "+UtilsPlugin.getStackTrace(e));
			result.put("result", false);
		}finally{
			try {
				if(in !=null){
					in.close();
				}
				if(fis !=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result.put("message", "完成文件 "+file+" 的解压！");
		result.put("result", true);
		return result;
	}
	public static void main(String[] args) {
		String subFileParams=null;
		
		Map<String,String> subFilesMap=new HashMap<String, String>();
		if(!JudgeUtil.isEmpty(subFileParams)){
			String [] params = subFileParams.split(",");
			for(String one : params){
				String [] subFileParam= one.split("#");
				if(subFileParam.length == 2){
					subFilesMap.put(subFileParam[0], subFileParam[1]);
				}
			}
		}
//		String file = "F:/test/CMDTest.jar";
//		String file = "F:/test/CMDTest.war";
//		String file = "F:/test/CMDTest.zip";
		String file = "F:/test/tf-tomcat-8.5.9.zip";
		File zipFile = new File(file);
		if(subFilesMap.isEmpty()){
			subFilesMap.put(zipFile.getName(), "/");
		}
		String target = new File(file).getParent();
		Map<String, Object> resultMap = JavaZipUtil.unzip(file,false,subFilesMap);
		System.out.println(JSON.toJSONString(resultMap));
	}
	
	public static boolean copyFile(InputStream srcFile,String destFileName,boolean overlay){
		//判断目标文件是否存在
		File destFile=new File(destFileName);
		if(destFile.exists()){
			if(overlay){
				//删除已经存在的目标文件
				new File(destFileName).delete();
			}
		}else{
			//判断文件所在的目录是否存在
			if(!destFile.getParentFile().exists()){
				if(!destFile.getParentFile().mkdirs())
				{
					return false;
				}
				
			}
		}
		//复制文件
		int byteread=0;
		BufferedInputStream in=null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(srcFile);
			out = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] buffer = new byte[1024];
			while((byteread = in.read(buffer)) !=-1){
				out.write(buffer, 0, byteread);
			}
			out.flush();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally{
			try{
				if(out !=null)
					out.close();
				if(in !=null)
					in.close();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
