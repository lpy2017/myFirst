package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DockerProbePortAnalyser extends AbstractAnalyser<Map<String, String>> {

	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String tmpLine = "";
				String pid = "";
				String tmpPort = "";
				Map<String, String> map = new HashMap<String, String>(2);

				// res :::7000 users:(("java",16772,32))
				// ::ffff:127.0.0.1:7001 users:(("java",16772,19))
				String portregex = ":(\\d+)\\s+";
				String pidregex = ",(\\d+),";
				Pattern portPattern = Pattern.compile(portregex);
				Pattern pidPattern = Pattern.compile(pidregex);
				Matcher match = null;
				while ((tmpLine = reader.readLine()) != null) {
					match = portPattern.matcher(tmpLine);
					if (match.find()) {
						tmpPort = match.group(1);
						match = pidPattern.matcher(tmpLine);
						if (match.find()) {
							pid = match.group(1);
							// 获取pid 成功则返回pid，否则如果pid不匹配则返回-1
							if (this.filter != null) {
								if (this.filter.doFilter(pid)) {
									map.put(tmpPort, pid);
								} else {
									map.put(tmpPort, "-1");
								}
							}
						} else {
							map.put(tmpPort, "-1");
						}
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
