package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class RunConIdAnalyser extends AbstractAnalyser<Map<String, Boolean>> {
  
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is)); 
				Map<String, Boolean> map = new HashMap<String, Boolean>(); 
				String tmpLine = null; 
				while((tmpLine = reader.readLine())!=null){ 
					map.put(tmpLine, true);
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
