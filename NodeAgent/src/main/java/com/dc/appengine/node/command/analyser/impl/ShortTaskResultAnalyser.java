package com.dc.appengine.node.command.analyser.impl;

import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ShortTaskResultAnalyser extends AbstractAnalyser<String>{

	/**
	 * 获取所有的结果集无论对错
	 * 
	 * */
	@Override
	public void analysis(InputStream is) throws Exception {
		StringBuilder sbre= new StringBuilder();
		Scanner scann= new Scanner(is);
		String reg="\\[[=]*\\s+\\]";
		Pattern p=Pattern.compile(reg);
		while(scann.hasNextLine()){
			String line=scann.nextLine();
			Matcher m=p.matcher(line);
			if(m.find()){
				continue;
			}
			sbre.append(System.lineSeparator());
			sbre.append(line );
		}
		this.result=sbre.toString();
	}
	//消除滚动条输入
public static void main(String[] args) {
	String reg="\\[[=]*\\s+\\]";
	String line="[========================         ]";
	Pattern p=Pattern.compile(reg);
	Matcher m=p.matcher(line);
	if(m.find()){
		System.out.println("get it");
	}
	System.out.println("finish");
}
}
