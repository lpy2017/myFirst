package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class NodeOsTypeAnalyser extends AbstractAnalyser<String> {
	//cat /proc/version 
	//String proc="Linux version 3.10.0-327.el7.x86_64 (builder@kbuilder.dev.centos.org) (gcc version 4.8.3 20140911 (Red Hat 4.8.3-9) (GCC) ) #1 SMP Thu Nov 19 22:10:57 UTC 2015";
	String regexOsInfo="\\([.A-Za-z0-9\\s]+\\(([-.A-Za-z0-9\\s]+)\\)([\\s]+\\(.*\\)[\\s]+)?\\)";
	Pattern p=null;
	public NodeOsTypeAnalyser(){
		p=Pattern.compile(regexOsInfo);
	}
	@Override
	public void analysis(InputStream is) throws Exception {
		NodeOsStatCache node=NodeOsStatCache.getInstance();
		Scanner scann= new Scanner(is);
		while(scann.hasNext()){
			String line=scann.nextLine();
			Matcher m=p.matcher(line);
			if(m.find()){
				String osType=m.group(1);
				node.setOsType(osType);
				break;
			}
			
		}
		scann.close();
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String line="Linux version 2.6.18-164.el5 (mockbuild@x86-002.build.bos.redhat.com) "
				+ "(gcc version 4.1.2 20080704 (Red Hat 4.1.2-46)) #1 SMP Tue Aug 18 15:51:54 EDT 2009";
		String line2="Linux version 3.10.0-327.el7.x86_64 (builder@kbuilder.dev.centos.org) "
				+ "(gcc version 4.8.3 20140911 (Red Hat 4.8.3-9) (GCC) ) #1 SMP Thu Nov 19 22:10:57 UTC 2015";
		String regexOsInfo="\\([.A-Za-z0-9\\s]+\\(([-.A-Za-z0-9\\s]+)\\)([\\s]+\\(.*\\)[\\s]+)?\\)";
		Pattern p=Pattern.compile(regexOsInfo);
		Matcher m=p.matcher(line2);
		if(m.find()){
			System.out.println("find  it");
			String osType=m.group(1);
			System.out.println(osType);
		}
	}
}
