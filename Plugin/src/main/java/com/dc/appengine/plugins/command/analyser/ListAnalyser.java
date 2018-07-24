package com.dc.appengine.plugins.command.analyser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListAnalyser extends AbstractAnalyser<List<String>>{

	/**
	 * 获取所有的结果集无论对错
	 * 
	 * */
	@Override
	public void analysis(InputStream is) throws Exception {
		this.result=new ArrayList<String>();
		Scanner scann= new Scanner(is);
		while(scann.hasNextLine()){
			this.result.add(scann.nextLine());
		}
		
	}

}
