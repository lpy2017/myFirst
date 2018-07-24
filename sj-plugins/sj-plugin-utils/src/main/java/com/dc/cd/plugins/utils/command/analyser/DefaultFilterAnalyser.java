package com.dc.cd.plugins.utils.command.analyser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultFilterAnalyser extends AbstractAnalyser<Boolean> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				if (filter == null) {
					throw new Exception("filter is empty!");
				}
				this.result = filter.doFilter(reader.readLine());
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
