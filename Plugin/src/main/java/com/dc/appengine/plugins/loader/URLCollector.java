package com.dc.appengine.plugins.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class URLCollector {

	public synchronized static List<URL> collect(File dir) throws MalformedURLException {
		List<URL> list = new ArrayList<URL>();
		if (dir.exists() && dir.isDirectory()) {
			for(File f : listAllFiles(dir)) {
				list.add(f.toURL());
			}
		}
		return list;
	}
	
	private static List<File> listAllFiles(File dir) {
		List<File> list = new ArrayList<File>();
		File[] files = dir.listFiles();
		if (files != null && files.length > 0) {
			for (File f : files) {
				if (f.isDirectory()) {
					List<File> subList = listAllFiles(f);
					if (subList.size() > 0) {
						list.addAll(subList);
					}
				}
			}
		}
		return list;		
	}
}
