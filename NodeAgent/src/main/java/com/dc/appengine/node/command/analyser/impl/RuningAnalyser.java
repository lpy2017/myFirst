package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class RuningAnalyser extends AbstractAnalyser<String> {
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String result = reader.readLine();
				if (result != null) {
					if (result.contains("httpd")) {
						result = reader.readLine();
						result = reader.readLine();
					}else if(result.contains("MySQL")){ 
						result = reader.readLine();
					}else if (result.contains("MongoDB")) {
						String pid = "-1";
						String regex = "pid=\\d{1,6}";
						Pattern pattern = Pattern.compile(regex);

						Matcher match = pattern.matcher(result);
						if (match.find()) {
							pid = match.group();
							pid = pid.substring(4);
						}
						result = pid;
					}
				}
				this.result = result;
				// String tmp = null;
				// int i=0;
				// while((tmp=reader.readLine())!=null && i<5){
				// System.out.println("$$$$$$$$$$$ container out:" +tmp);
				// i++;
				// }
				Thread.sleep(3000);
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}
	}
}
