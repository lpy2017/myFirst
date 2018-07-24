package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ProbeDefaultAnalyser extends AbstractAnalyser<Map<Integer,Integer>> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String tmpLine = "";
				Map<Integer,Integer> map = new HashMap<Integer, Integer>();
				while((tmpLine=reader.readLine())!=null){
					String[] arg = tmpLine.trim().split("\\s+");
					if(arg.length==2 && arg[0].trim().length()!=0){
					map.put(Integer.parseInt(arg[1]), Integer.parseInt(arg[0]));
					}
				}
				this.result = map;
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
