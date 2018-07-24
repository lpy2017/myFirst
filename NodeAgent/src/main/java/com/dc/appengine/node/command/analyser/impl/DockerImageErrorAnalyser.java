package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class DockerImageErrorAnalyser extends AbstractAnalyser<String> {

	private static Logger LOG = LoggerFactory.getLogger(DockerImageErrorAnalyser.class);
	@Override
	public void analysis(InputStream err) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = null; 
		if (err != null) {
			
			try {
				reader = new BufferedReader(new InputStreamReader(err));
				String temp = "";
				int icount = 0;
				this.result = "OK";
				while ((temp = reader.readLine()) != null) {
					icount++;
					if( "warn".equals(temp.substring(0,4).toLowerCase()) )
						LOG.warn(temp);
					else
						LOG.error(temp);
					if(icount==1){
						this.result =temp;
					}else if (icount > 5)
						break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
			
		}
	}

}
