package com.dc.appengine.appsvn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static void doCompress(File src, File dest) {
		try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dest))) {
			if (src.isDirectory()) {
				for (File file : src.listFiles()) {
					doCompress(src,file, out);
				}
			} else {
				doCompress(src,src, out);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void doCompress(File baseFolder,File src, ZipOutputStream out) {
		int lengthCut=baseFolder.getAbsolutePath().length()+1;
		if (src.isDirectory()) {
			for (File file : src.listFiles()) {
				doCompress(baseFolder,file, out);
			}
		} else {
			try (InputStream ins = new FileInputStream(src)) {
				byte[] bufs = new byte[1024 * 10];
				String name=src.getAbsolutePath().substring(lengthCut);
				ZipEntry zipEntry = new ZipEntry(name);
				 
				out.putNextEntry(zipEntry);
				int read = 0;
				while ((read = ins.read(bufs, 0, bufs.length)) != -1) {
					out.write(bufs, 0, read);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public static void getRelativePath(String basePath){
		File dir=new File(basePath);
		String fPath=dir.getAbsolutePath();
		int lengthCut=fPath.length();
		System.out.println(lengthCut);
		for(File f :dir.listFiles()){
			String allPath=f.getAbsolutePath();
			System.out.println(allPath );
			System.out.println(allPath.substring(lengthCut+1));
		}
	}
	public static void main(String[] args) {
		String workDir = "D:\\svnworker\\99e41763-560d-434e-b74d-92941543715d";
		ZipUtil.doCompress(new File(workDir + "/temp"), new File(workDir + "/a.zip"));
		//ZipUtil.getRelativePath(workDir);
	}
}
