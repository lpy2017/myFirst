package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class PlayAnalyser extends AbstractAnalyser<String> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				this.result = null;
				String line = null;
				while((line = reader.readLine())!=null){
					if(line.contains("pid is")){
						this.result = line.split(" ")[3].trim();
						break;
					}
				}
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
