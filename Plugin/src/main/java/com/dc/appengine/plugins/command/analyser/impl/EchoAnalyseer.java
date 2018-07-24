package com.dc.appengine.plugins.command.analyser.impl;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class EchoAnalyseer extends AbstractAnalyser<String> {
private static final Logger log= LoggerFactory.getLogger(EchoAnalyseer.class);
	@Override
	public void analysis(InputStream is) throws Exception {
		result="";
		Scanner scann= new Scanner(is);
		while(scann.hasNext()){
			String temp=scann.nextLine();
			log.debug("-"+temp);
			result+=temp+System.lineSeparator();
		}
		scann.close();
	}

}
