package com.dc.appengine.node.command.analyser.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ClassPathAnalyser extends AbstractAnalyser<Set<String>> {

	@Override
	public void analysis(InputStream is) throws Exception {
		result = new HashSet<String>();
		StringBuilder sb= new StringBuilder();
		try (Scanner scann = new Scanner(is)) {
			while(scann.hasNext()){
				sb.append(scann.nextLine());
			}
		} catch (Exception e) {

		}
		String content=sb.toString();
		String[] files = content.split(":");
		// getFiles
		for (String usedFile : files) {
			String unit = usedFile.trim();
			if (unit.endsWith(".jar")) {
				File f= new File(unit);
				if(f.exists()){
					result.add(f.getParent());
				}
			}
		}
		

	}
 
}
