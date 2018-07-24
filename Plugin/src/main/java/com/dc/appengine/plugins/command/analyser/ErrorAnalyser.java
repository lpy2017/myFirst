package com.dc.appengine.plugins.command.analyser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorAnalyser extends AbstractAnalyser<String> {
	private static Logger LOG = LoggerFactory.getLogger(ErrorAnalyser.class);

	public void analysis(InputStream err) throws Exception {
		BufferedReader reader = null;
		if (err != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(err));
				String temp = "";
				int icount = 0;
				this.result = "no error";
				while ((temp = reader.readLine()) != null) {
					icount++;
					LOG.error("instance error:" + temp);
					if(icount==1){
						this.result =temp;
					}else if (icount > 5)
						break;
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
