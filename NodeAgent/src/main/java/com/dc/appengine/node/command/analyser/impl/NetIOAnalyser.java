package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;
 

public class NetIOAnalyser extends AbstractAnalyser<String> {
	private long RB=0l;
	private long TB=0l;
	private long timeNow=0l;
 
	@Override
	public void analysis(InputStream is) throws Exception {
		Scanner scann= new Scanner(is);
		timeNow=System.currentTimeMillis();
		String line=scann.nextLine();
		if(line.contains(":")){
			//String ethName=line.split(":")[0].trim();
			String temp=line.split(":")[1].trim();
			String[] datas=temp.split("\\s+");
			if(datas.length==16){
				RB=Long.parseLong(datas[0]);
				TB=Long.parseLong(datas[8]);
			}
		}
		 
		scann.close();
	}
	
	public long getRB() {
		return RB;
	}

	public void setRB(long rB) {
		RB = rB;
	}

	public long getTB() {
		return TB;
	}

	public void setTB(long tB) {
		TB = tB;
	}

	public long getTimeNow() {
		return timeNow;
	}

	public void setTimeNow(long timeNow) {
		this.timeNow = timeNow;
	}

	public static void main(String[] args) {
		String line="  eth0:1547524498 1372791    0    0    0     0          0      4971 79138596  603137    0    0    0     0       0          0";
		if(line.contains(":")){
			String ethName=line.split(":")[0].trim();
			System.out.println(ethName);
			String temp=line.split(":")[1].trim();
			String[] datas=temp.split("\\s+");
			System.out.println(datas.length);
			if(datas.length==16){
				System.out.println(Long.parseLong(datas[0]));
				System.out.println(Long.parseLong(datas[8]));
			}
		}
	}
}
