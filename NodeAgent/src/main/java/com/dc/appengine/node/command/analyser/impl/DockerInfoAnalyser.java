package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DockerInfoAnalyser extends AbstractAnalyser<Map<String, String>> {
 
	String SPACE="\\s+"; 
	String B="\\s+B";
	String K="\\s+K";
	String M="\\s+M";
	String G="\\s+G";
//	String diagonal ="\\s+/\\s+";
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				 
				Map<String, String> map = new HashMap<String, String>(6);

				// res :::7000 users:(("java",16772,32))
				String tmpLine = reader.readLine();
				if(tmpLine==null||tmpLine.contains("Error")) {
					map.put("error", tmpLine+"");
				}else{
					tmpLine = tmpLine.replaceAll(B, "B"); 
					tmpLine = tmpLine.replaceAll(K, "K");
					tmpLine = tmpLine.replaceAll(M, "M");
					tmpLine = tmpLine.replaceAll(G, "G");
					tmpLine = tmpLine.replaceAll("/", " ");
					String []infos = tmpLine.split(SPACE);
					map.put("container", infos[0]);
					map.put("cpu", infos[1]);
					map.put("memUsage", infos[2]);
					map.put("memLimits", infos[3]);
					map.put("mem", infos[4]);
					map.put("netIO", infos[5]+"/"+infos[6]);
//					map.put("blockIO", infos[7]+"/"+infos[8]);
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
