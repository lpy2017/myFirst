package com.dc.appengine.plugins.transfer;

import java.io.File;

public interface ITransferer {
	boolean open();
	void close();
	String getRemotePath(String path, String name);
	String getLocalPath(String path, String name);
	boolean delete(String path, String filePath);
	String upload(File tmpFile, String path);
	File download(String remotePath, String localPath);
	boolean deleteDir(String dir);
	void removeTmpFiles(File file, String dir);
	void uploadDir(File tmpFile, String toDir);
	boolean delete(String path);
	boolean checkFileExistence(String path, String fileName);
	String upload(String fileLocalName, String filePathRemote, String fileNameRemote);
	String getRelativePath(String path);
	void deleteTmpFile(File patchFile);
	boolean isDirectory(String path);
	void downloadContainer(String path, String value);
}