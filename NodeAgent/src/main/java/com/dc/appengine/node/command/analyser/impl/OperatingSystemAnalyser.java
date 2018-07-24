package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class OperatingSystemAnalyser extends AbstractAnalyser<Map<String,Object>>{

	@Override
	public void analysis(InputStream is) throws Exception {
		result=new HashMap<String, Object>();
		Scanner scann= new Scanner(is);
		while(scann.hasNextLine()){
			String line=scann.nextLine();
			result.put("all", line);
		}
	}

}
