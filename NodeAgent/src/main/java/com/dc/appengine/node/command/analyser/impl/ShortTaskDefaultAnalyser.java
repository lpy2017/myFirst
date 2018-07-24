package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.Scanner;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ShortTaskDefaultAnalyser extends AbstractAnalyser<String> {

	@Override
	public void analysis(InputStream is) throws Exception {
		String lastresult="";
		StringBuilder sbre= new StringBuilder();
		Scanner scann= new Scanner(is);
		while(scann.hasNextLine()){
			lastresult=scann.nextLine();
			sbre.append(lastresult);
		}
		if("success".equals(lastresult)){
			this.result="success";
		}else{
			this.result=sbre.toString();
		}
		
	}

}
