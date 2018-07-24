package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class SnapkillAnalyser extends AbstractAnalyser<String> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				this.result = reader.readLine();
				Thread.sleep(1000);
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
