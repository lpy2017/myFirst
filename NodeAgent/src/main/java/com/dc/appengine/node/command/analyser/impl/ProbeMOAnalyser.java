package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ProbeMOAnalyser extends AbstractAnalyser<Map<String, Boolean>> {
	public static String MOM_KEY="MOM";
	public static String OUT_KEY="OUT";
	public static String LOG_KEY="LOG";
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String tmpLine = "";
				Map<String, Boolean> map = new HashMap<String, Boolean>(2);
				map.put(MOM_KEY, false);
				map.put(OUT_KEY, false);
				map.put(LOG_KEY, false);
				while((tmpLine=reader.readLine())!=null){
					if( tmpLine.trim().contains("OUT")){
						map.put(MOM_KEY,true);
						map.put(OUT_KEY,true);
					}else if(tmpLine.trim().contains("LOG")){
						map.put(LOG_KEY,true);
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
