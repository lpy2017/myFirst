package com.dc.appengine.plugins.utils;

import java.io.File;

import org.apache.commons.net.ftp.FTPFile;

public class SyncFTPFile {
	private String path;
	private FTPFile file;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public FTPFile getFile() {
		return file;
	}
	public void setFile(FTPFile file) {
		this.file = file;
	}
	
}
