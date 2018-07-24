package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DockerListAnalyser extends AbstractAnalyser<List<String>> {

	@Override
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String dockerID = "";
				List<String> dockerList = new ArrayList<String>();
				while ((dockerID = reader.readLine()) != null) {
					if(dockerID.contains("CN")){
						dockerList.add(dockerID.split("/")[1]);
					}
					
				}
				this.result = dockerList;
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
		}

	}

}
