package com.dc.appengine.plugins.command.analyser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultAnalyser extends AbstractAnalyser<String> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				this.result = reader.readLine();
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
