package com.dc.bank.appengine.plugins.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileFilterClass implements FileFilter{
	private static List<String> fileSuffix= new ArrayList<String>(Arrays.asList(".zip", ".xml"));
	private String filterType="isFile";
	private String[] filters= new String[]{};
	public FileFilterClass(String filterType,String...filters){
		this.filterType=filterType;
		if(filters!=null){
			this.filters= filters;
		}
	}
	@Override
	public boolean accept(File pathname) {
		String fileName = pathname.getName();
		Boolean isDirectory = pathname.isDirectory();
		if("isDirectory".equals(filterType)){
			if(isDirectory&&judgeName(fileName, filters)){
				return true;
			}else{
				return false;
			}
		}else{
			if(judgeName(fileName, filters)){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public Boolean judgeName(String fileName,String[] filters){
		for(int i=0;i<filters.length;i++){
			String filter =filters[i];
			if(fileSuffix.contains(filter)){//判断是否是文件后缀
				if(!fileName.endsWith(filter)){
					return false;
				}
			}else{
				if(!fileName.contains(filter)){
					return false;
				}
			}
		}
		return true;
	}
	
}
