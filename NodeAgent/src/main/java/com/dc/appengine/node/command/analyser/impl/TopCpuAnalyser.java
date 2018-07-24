package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class TopCpuAnalyser extends AbstractAnalyser<String> {
	//top -n1 |grep -i cpu |grep -iv mem
	//Cpu(s):  0.2%us,  0.1%sy,  0.0%ni, 99.3%id,  0.1%wa,  0.0%hi,  0.2%si,  0.0%st
	DecimalFormat model= new DecimalFormat("0.00");
	@Override
	public void analysis(InputStream is) throws Exception {
		Scanner scann= new Scanner(is);
		String idStr="";
		while(scann.hasNext()){
			String line=scann.nextLine();
			if(line.contains(",")){
				for(String p:line.split(",")){
					if(p.contains("id")){
						idStr=p.trim();
					}
				}
			}
		}
		scann.close();
		if(idStr.contains("%")){
			idStr=idStr.replace("%id", "");
		}else{
			idStr=idStr.split("\\s+")[0];
		}
		double s=(double)100-Double.valueOf(idStr);
		result=model.format(s);
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String line2="Cpu(s):  0.2%us,  0.1%sy,  0.0%ni, 99.3%id,  0.1%wa,  0.0%hi,  0.2%si,  0.0%st";
		String line="%Cpu(s):  2.7 us,  2.1 sy,  0.0 ni, 94.9 id,  0.1 wa,  0.0 hi,  0.2 si,  0.0 st";
		DecimalFormat model= new DecimalFormat("0.00");
		String idStr="";
		if(line.contains(",")){
			for(String p:line.split(",")){
				if(p.contains("id")){
					idStr=p.trim();
				}
			}
		}
		if(idStr.contains("%")){
			idStr=idStr.replace("%id", "");
		}else{
			idStr=idStr.split("\\s+")[0];
		}
		
		System.out.println(idStr);
		double s=(double)100-Double.valueOf(idStr);
		System.out.println(model.format(s));
	}
}
