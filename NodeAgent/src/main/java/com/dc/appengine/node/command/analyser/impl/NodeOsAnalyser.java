package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class NodeOsAnalyser extends AbstractAnalyser<Map<String,Object>>{

	@Override
	public void analysis(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		result=new HashMap<String,Object>();
		Scanner scann= new Scanner(is);
		while(scann.hasNextLine()){
			String line=scann.nextLine();
			if(line.contains("Server Version:")){
				String dockerVersion=line.replace("Server Version:", "").trim();
				result.put("docker", dockerVersion);
			}
			if(line.contains("Operating System:")){
				String osVersion=line.replace("Operating System:", "").trim();
				result.put("os", osVersion);
			}
			if(line.contains("CPUs:")){
				String cpus=line.replace("CPUs:", "").trim();
				result.put("cpu", cpus);
			}
			if(line.contains("Total Memory:")){
				String totalMemory=line.replace("Total Memory:", "").trim();
				result.put("mem", totalMemory);
			}
		}
		
	}

}
