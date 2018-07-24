package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DiskIOAnalyser extends AbstractAnalyser<String> {
	//String commandDisk="iostat -d -k |grep sda";
	//磁盘名称   tps   read(kb/s) write(kb/s) read(kb)  write(kb)
	//sda   17.18 52.65      654.19       86723954 1077532047
	private double diskR=0.0;
	private double diskW=0.0;
	@Override
	public void analysis(InputStream is) throws Exception {
		Scanner scann= new Scanner(is);
		
		String line=null;
		while(scann.hasNext()){
			line=scann.nextLine().trim();
			if(line.contains("sda"));
				break;
		}
		String [] units=line.split("\\s+");
		if(units.length==6){
			diskR=Double.parseDouble(units[2]);
			diskW=Double.parseDouble(units[3]);
		}
		//System.out.println(diskW+"/"+diskR);
		diskR=(double)diskR/1024;
		BigDecimal bd = new BigDecimal(diskR);
		diskR=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		diskW=(double)diskW/1024;
		BigDecimal bd2 = new BigDecimal(diskW);
		diskW=bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//System.out.println(diskW+"/"+diskR);
		scann.close();
	}
	
	public double getDiskR() {
		return diskR;
	}

	public void setDiskR(double diskR) {
		this.diskR = diskR;
	}

	public double getDiskW() {
		return diskW;
	}

	public void setDiskW(double diskW) {
		this.diskW = diskW;
	}

	public static void main(String[] args) {
		String line="sda   17.18 52.65      654.19       86723954 1077532047".trim();
		String [] units=line.split("\\s+");
		double diskR=0.0;
		double diskW=0.0;
		if(units.length==6){
			diskR=Double.parseDouble(units[2]);
			diskW=Double.parseDouble(units[3]);
		}
		System.out.println(diskW+"/"+diskR);
		diskR=(double)diskR/1024;
		BigDecimal bd = new BigDecimal(diskR);
		diskR=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		diskW=(double)diskW/1024;
		BigDecimal bd2 = new BigDecimal(diskW);
		diskW=bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		System.out.println(diskW+"/"+diskR);
	}
}
