package com.dc.appengine.appmaster.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntryPredicate;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.entity.StartBluePrint;

public class Utils {
	private static Logger logger = Logger.getLogger(Utils.class);
	public static int BUFFER_SIZE = 2048;
	public static String TMPDIR;
	public static String strencoding = "UTF-8";

	// 获取递增的版本号
	public static String incrementalVersion(String version) {
		if (StringUtils.isEmpty(version)) {
			return "v1";
		} else {
			return "v" + (Integer.parseInt(version.replace("v", "")) + 1);
		}
	}

	public static Map<String, Object> newMap(Object... objects) {
		Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < objects.length - 1; i = i + 2) {
			result.put((String) objects[i], objects[i + 1]);
		}
		return result;
	}

	public static boolean uploadFile(String url, int port, String username, String password, String path,
			String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
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
			ftp.makeDirectory(path);
			ftp.changeWorkingDirectory(path);
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
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

	public static boolean downloadFile(String url, int port, String username, 
			String password, String path,
			String filename,String downloadParentPath) {
		boolean success = false;
		if(!downloadParentPath.endsWith(File.separator))
			downloadParentPath=downloadParentPath+File.separator;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
		ftp.setControlEncoding("UTF-8");
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
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(path);
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getName().equals(filename)) {
					File folder = new File(downloadParentPath);
					File f = new File(downloadParentPath+ftpFile.getName());
					if(f.exists())
						f.delete();
					if(!folder.exists())
						folder.mkdirs();
					f.createNewFile();
					OutputStream local = new FileOutputStream(f);
					ftp.retrieveFile(ftpFile.getName(), local);
					local.close();
				}
			}
			ftp.logout();
			success = true;
		} catch (IOException e) {
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
	public static Map<String,Object> unZipFile4Tar(File file,String name ,String targetPath,String type,List<String> fileNames){
		Map<String,Object> result= new HashMap<>();
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
				fileNames.add(entryName);
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
			result.put("message", "未找到文件 "+file+" : "+getStackTrace(e));
			result.put("result", false);
		} catch (ArchiveException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 压缩格式异常: "+getStackTrace(e));
			result.put("result", false);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 读写异常: "+getStackTrace(e));
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
	public static List<String> unZip(File zipfile, String destDir) throws Exception {
		List<String> fileNames = new ArrayList<String>();
		if (StringUtils.isBlank(destDir)) {
			destDir = zipfile.getParent();
		}
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		File targetDir = new File(destDir);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		if(zipfile.getName().endsWith("tar")){
			unZipFile4Tar(zipfile, "", destDir, "tar",fileNames);
			return fileNames;
		}else if(zipfile.getName().endsWith("tar.gz")){
			unZipFile4Tar(zipfile, "", destDir, "tar.gz",fileNames);
			return fileNames;
		}
		ZipArchiveInputStream is = null;

		try {
			is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipfile), BUFFER_SIZE));
			ZipArchiveEntry entry = null;
			while ((entry = is.getNextZipEntry()) != null) {
				fileNames.add(entry.getName());
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					if (directory.exists()) {
						directory.delete();
					}
					directory.mkdirs();
				} else {
					File file = new File(destDir, entry.getName());
					if (file.exists()) {
						file.delete();
					}
					OutputStream os = null;
					try {
						os = new BufferedOutputStream(new FileOutputStream(file), BUFFER_SIZE);
						IOUtils.copy(is, os);
					} finally {
						IOUtils.closeQuietly(os);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
		}

		return fileNames;
	}

	// 参数为一个BluePrint对象，根据启动线来计算权重值，值小的先启动，值大的后启动，值相同的同时启动
	@Deprecated
	public static HashMap<String, Integer> computePriority(BluePrint bluePrint) {
		List<StartBluePrint> links = bluePrint.getLinkDataArray();
		List<Element> elements = bluePrint.getNodeDataArray();
		// 如果某个元素没有线指向它，它也没有指向别人，那么，这个元素的优先级就是默认值
		Map<Long, List<Long>> map1 = new HashMap<>();
		Map<Long, List<Long>> map2 = new HashMap<>();
		for (StartBluePrint sbp : links) {
			Long from = sbp.getFrom();
			Long to = sbp.getTo();
			List<Long> list1 = null;
			List<Long> list2 = null;
			if (!map1.containsKey(from)) {
				list1 = new ArrayList<>();
				map1.put(from, list1);
			}
			list1 = map1.get(from);
			list1.add(to);

			if (!map2.containsKey(to)) {
				list2 = new ArrayList<>();
				map2.put(to, list2);
			}
			list2 = map2.get(to);
			list2.add(from);
		}

		Collection<Long> set = new HashSet<>();
		Map<Long, Integer> result = new HashMap<>();
		set.addAll(map1.keySet());
		set.addAll(map2.keySet());
		for (Long l : set) {
			result.put(l, 0);// 初始化的权重都是0
		}
		while (set.size() > 0) {
			compute(set, map1, map2, result);
		}
		System.out.println(result);
		return null;
	}

	@Deprecated
	private static <T> void compute(Collection<T> coll, Map<T, List<T>> map1, Map<T, List<T>> map2,
			Map<T, Integer> result) {
		Iterator<T> it = coll.iterator();
		while (it.hasNext()) {
			T unit = it.next();
			if (!map1.containsKey(unit)) {
				List<T> li2 = map2.get(unit);
				int p = result.containsKey(unit) ? result.get(unit) : 0;
				if (li2 != null && !li2.isEmpty()) {
					for (T s : li2) {
						if (result.get(s) > (p + 1)) {
							System.out.println("不再设置，目标Priority值足够高");
						} else {
							result.put(s, p + 1);
						}
						map1.get(s).remove(unit);
						if (map1.get(s).size() == 0) {
							map1.remove(s);
						}
					}
				}
				it.remove();
				break;
			}
		}
	}

	// TODO:产生一个全局唯一的id
	public static long getUniqueFlowId() {
		// 随后修改为redis控制
		return System.nanoTime();
	}

	public static Object deepClone(Object src) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(src);
		oos.close();
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		Object o = ois.readObject();
		ois.close();
		return o;
	}

	// 读取一个zip文件中的某个文件，返回一个byte[]
	public static byte[] readFileInZip(String zipFile, String targetFile) {
		byte[] error = "error".getBytes();
		try (ZipFile zip = new ZipFile(zipFile)) {
			Iterable<ZipArchiveEntry> files = zip.getEntries(targetFile);
			for (ZipArchiveEntry zae : files) {
				System.out.println(zae.getName());
				byte[] b = new byte[(int) zae.getSize()];
				try (InputStream in = zip.getInputStream(zae)) {
					IOUtils.read(in, b);
					return b;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return error;
		}
		return error;
	}

	// 覆盖zip中的某个文件，暂时没有考虑并发操作
	public static boolean coverFileInZip(String zipFile, final String targetFile, byte[] content) {
		try {
			File f = new File(zipFile);
			f.renameTo(new File(zipFile + "_copy"));
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		}
		try (RandomAccessFile newFile = new RandomAccessFile(zipFile, "rw");
				ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(newFile.getChannel());
				ZipFile z = new ZipFile(zipFile+"_copy")) {
			ZipArchiveEntry entry = new ZipArchiveEntry(targetFile);
			entry.setSize(content.length);
			zipOutput.putArchiveEntry(entry);
			zipOutput.write(content);
			zipOutput.closeArchiveEntry();
			z.copyRawEntries(zipOutput, new ZipArchiveEntryPredicate(){
				@Override
				public boolean test(ZipArchiveEntry zipArchiveEntry) {
					if(zipArchiveEntry.getName().equals(targetFile)){
						return false;						
					}else{
						return true;
					}
				}
			});
			new File(zipFile+"_copy").delete();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public static void main(String[] args) {
//		byte[] c = readFileInZip("C:\\Users\\yangzhec\\Desktop\\war.zip", "flows.properties");
//		String s = new String(c);
//		if (s.equals("error")) {
//			System.out.println("error");
//			return;
//		}
//		Properties p = new Properties();
//		try {
//			p.load(new StringReader(s));
//			System.out.println(p.getProperty("deploy"));
//			p.setProperty("deploy", "999999");
//			StringWriter sss = new StringWriter();
//			p.store(sss, "flows define");
//			System.out.println(sss.toString());
//			byte[] content = sss.toString().getBytes();
//			coverFileInZip("C:\\Users\\yangzhec\\Desktop\\war.zip", "flows.properties", content);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		boolean yes = Utils.deleteFolder("30000","10.1.108.217", 21, "paas", "123456", "packages","3f332184-2404-4cda-a6fe-08ac6857be19");
				System.out.println(yes);
	}
	
	public static boolean compareMap(String crPut, String ssPut) {
		Map<String, String> cr = JSON.parseObject(crPut, new TypeReference<Map<String, String>>() {
		});
		Map<String, String> ss = JSON.parseObject(ssPut, new TypeReference<Map<String, String>>() {
		});
		boolean flag = true;
		if (!ss.isEmpty()) {
			Iterator<Entry<String, String>> ssIter = ss.entrySet().iterator();
			while (ssIter.hasNext()) {
				Entry<String, String> ssEntry = ssIter.next();
				String key = ssEntry.getKey();
				String value = ssEntry.getValue();
				if (!value.equals(cr.get(key))) {
					flag = false;
					break;
				}
			}
		} else {
			if (!cr.isEmpty()) {
				flag = false;
			}
		}
		if (!cr.isEmpty()) {
			Iterator<Entry<String, String>> crIter = cr.entrySet().iterator();
			while (crIter.hasNext()) {
				Entry<String, String> crEntry = crIter.next();
				String key = crEntry.getKey();
				String value = crEntry.getValue();
				if (!value.equals(ss.get(key))) {
					flag = false;
					break;
				}
			}
		} else {
			if (!ss.isEmpty()) {
				flag = false;
			}
		}
		return flag;
	}
	
	public static boolean deleteFile(String url, int port, String username, String password, String parentPath,
			String filename) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
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
			ftp.changeWorkingDirectory(parentPath);
			boolean result =ftp.deleteFile(filename);//删除文件
			if (!result) {
				String[] names = ftp.listNames();
				if (names != null && names.length > 0) {
					for (String s : names) {
						if (filename.equals(s)) {
							result= false; //文件未删除
							logger.error("FTP 删除文件失败！文件：" + parentPath + "/" + filename);
							break;
						}
					}
				}
			}else{
				int i = parentPath.lastIndexOf("/");
				if(i < 0){
					ftp.cdup();
					ftp.removeDirectory(parentPath);//删除单级父目录
				}
			}
			ftp.logout();
			return result;
		} catch (IOException e) {
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
	
	public static boolean deleteFolder(String timeOut,String url, int port, String username, String password, String parentPath,
			String folder) {
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
			ftp.changeWorkingDirectory(parentPath);
			boolean deleSucc = deleteFolder(ftp,folder);//删除单级父目录
			ftp.logout();
			return deleSucc;
		} catch (Exception e) {
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
	
	public static boolean uploadFileAndUnzip(String url, int port, String username, String password, String path,
			String filename, InputStream input,String ftpHome) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
		ftp.setControlEncoding("UTF-8");  
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
			for(int i=0;i<paths.length;i++){
				ftp.makeDirectory(paths[i]);
				ftp.changeWorkingDirectory(paths[i]);
			}
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			if(null!=input || null!=filename){
				ftp.storeFile(filename, input);
				
//			Runtime.getRuntime().exec("chmod -R 777 " + ftpHome + path);
//				String desFolder = filename.substring(0,filename.lastIndexOf("."));
//				ftp.mkd(desFolder);
////			ftp.changeWorkingDirectory(desFolder);
//				String packageHome = ftpHome + path+"/";
////			Runtime.getRuntime().exec("unzip -o -d " + packageHome + desFolder+"/" + " " + packageHome + filename);
//				Map<String, Object> resultMap = unZipFile(new File(packageHome + filename),null,packageHome + desFolder+"/");
//				System.out.println(JSON.toJSONString(resultMap));
//				if(Boolean.valueOf(resultMap.get("result").toString())){
//					logger.info("FTP 解压文件成功：" + path + "/" + filename);
//					success = true;
//				}
//				input.close();
			}
			success = true;
			ftp.logout();
		} catch (IOException e) {
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
	/*
	 * mkdir 多级目录
	 */
	public static boolean uploadDirsFile(String url, int port, String username, String password, String path,
			String filename, InputStream input) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
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
			for(int i=0;i<paths.length;i++){
				ftp.makeDirectory(paths[i]);
				ftp.changeWorkingDirectory(paths[i]);
			}
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
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
	
	public static Map<String,Object> unZipFile(File file,String name ,String targetPath){
		Map<String,Object> result= new HashMap<>();
		ArchiveInputStream in =null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			//获得输出流
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
			in = new ArchiveStreamFactory()
					.createArchiveInputStream(ArchiveStreamFactory.JAR,
							bufferedInputStream);
			JarArchiveEntry entry = null;
			Boolean unzipSubFile=false;
			if(name ==null || "".equals(name)){
				unzipSubFile=true;
			}
			String entryName="";
			//循环遍历解压
			while ((entry = (JarArchiveEntry) in.getNextEntry()) != null) {
				entryName = entry.getName();
				if(unzipSubFile){
//					if(entryName.startsWith(name)){
						if (entry.isDirectory()) {
							new File(targetPath, entry.getName()).mkdir();
						} else {
							OutputStream out = FileUtils.openOutputStream(new File(
									targetPath, entry.getName()));
							IOUtils.copy(in, out);
							out.close();
						}	
//					}
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
			result.put("message", "未找到文件 "+file+" : "+e.getMessage());
			result.put("result", false);
		} catch (ArchiveException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 压缩格式异常: "+e.getMessage());
			result.put("result", false);
		} catch (IOException e) {
			e.printStackTrace();
			result.put("message", "文件 "+file+" 读写异常: "+e.getMessage());
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
	
	public static InputStream getInputStream(String hostname, int port, String username, String password, String pathName, String fileName) {
		FTPClient ftp = new FTPClient();
		ftp.setRemoteVerificationEnabled(false);
		try {
			ftp.connect(hostname, port);
			ftp.login(username, password);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return null;
			}
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(pathName);
			FTPFile[] ftpFiles = ftp.listFiles();
			for (FTPFile ftpFile : ftpFiles) {
				if (ftpFile.getName().equals(fileName)) {
					return ftp.retrieveFileStream(fileName);
				}
			}
		} catch (SocketException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		};
		return null;
	}
	
}
