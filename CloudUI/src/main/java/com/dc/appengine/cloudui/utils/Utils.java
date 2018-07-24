package com.dc.appengine.cloudui.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntryPredicate;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;


public class Utils {
	// 读取一个zip文件中的某个文件，返回一个byte[]
		public static byte[] readFileInZip(String zipFile, String targetFile) {
			byte[] error = "error".getBytes();
			byte[] notfound = "notfound".getBytes();
			try (ZipFile zip = new ZipFile(zipFile)) {
				Iterable<ZipArchiveEntry> files = zip.getEntries(targetFile);
				for (ZipArchiveEntry zae : files) {
					System.out.println(zae.getName());
					byte[] b = new byte[(int) zae.getSize()];
					try (InputStream in = zip.getInputStream(zae)) {
						IOUtils.readFully(in, b);
						return b;
					}
				}
				return notfound;
			} catch (IOException e) {
				e.printStackTrace();
				return error;
			}
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
}
