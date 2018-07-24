package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class MemAnalyser extends AbstractAnalyser<String> {
	@Override
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {

			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String temp = "";

				String line = null;
				while ((temp = reader.readLine()) != null) {
					if (temp.toLowerCase().contains("mem")) {
						line = temp;
						break;
					}
				}
				if (line == null) {
					this.result = "nocontent";
				}
				return;
				// 找到

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}

		}
	}
}
