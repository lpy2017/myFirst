package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class CadCheckAnalyser  extends AbstractAnalyser<String>{
	private static Logger log=  LoggerFactory.getLogger(CadCheckAnalyser.class);
	@Override
	public void analysis(InputStream is) throws Exception {
		Scanner scann =new Scanner(is);
		
		while(scann.hasNextLine()){
			String line=scann.nextLine();
			if(line.contains("error")){
				this.result="error";
				break;
			}
			log.debug(line);
		}
		this.result="ok";
		
	}

}
