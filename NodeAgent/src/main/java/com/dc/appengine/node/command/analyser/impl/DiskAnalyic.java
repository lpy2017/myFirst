package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DiskAnalyic extends AbstractAnalyser<List<Map<String,String>>> {
	private static String SPLIT="\\s+";
	private static String [] keys={"name","all","used","available","useage","fs"};
	@Override
	public void analysis(InputStream is) throws Exception {
		result= new ArrayList<Map<String,String>>();
		Scanner scann= new Scanner(is);
		int lineNum=0;
		while(scann.hasNext()){
			lineNum++;
			String line=scann.nextLine();
			if(lineNum==1){
				continue;
			}
			String [] ss=line.split(SPLIT);
			Map<String,String> map= new HashMap<String,String>();
			if( ss.length ==keys.length){
				for(int i=0;i<keys.length;i++){
					String key=keys[i].trim();
					String value=ss[i].trim().replace("%", "");
					map.put(key, value);
				}
				result.add(map);
			}
			
		}
		scann.close();
	}
	public static void main(String[] args) {
		// diskname all used available useagerate
		String line="/dev/mapper/centos-root 51175 29253 21923   58% /";
		String [] ss=line.split(SPLIT);
		Map<String,String> map= new HashMap<String,String>();
		if( ss.length ==keys.length){
			for(int i=0;i<keys.length;i++){
				String key=keys[i].trim();
				String value=ss[i].trim().replace("%", "");
				map.put(key, value);
			}
		}
		System.out.println(map);
	}
}
