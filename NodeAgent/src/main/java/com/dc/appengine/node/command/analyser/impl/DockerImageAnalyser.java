package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DockerImageAnalyser extends AbstractAnalyser<String> {

	@Override
	public void analysis(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String result= "OK";
				String line = "";
				while((line = reader.readLine())!=null){
					if(line.contains("Error")){
						result = line;
						break;
					}
					if(line.contains("Please login prior")){
						result = line;
						break;
					}
				} 
				this.result = result;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(reader!=null){
					reader.close();
					reader = null;
				}
			}
			
			
			
		}
	}

}
