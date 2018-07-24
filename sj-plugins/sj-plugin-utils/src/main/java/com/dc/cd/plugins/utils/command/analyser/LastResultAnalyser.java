package com.dc.cd.plugins.utils.command.analyser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LastResultAnalyser extends AbstractAnalyser<String> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while((line=reader.readLine())!=null){
					this.result = line;
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
