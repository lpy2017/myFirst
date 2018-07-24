package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;
public class IoStatAnalyser extends AbstractAnalyser<String>{
	private static Logger LOG = LoggerFactory.getLogger(IoStatAnalyser.class);
	@Override
	public void analysis(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		BufferedReader reader = null; 
		if (is != null) {
			
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				String temp = "";
				double inAll=0;
				double outAll=0;
				
				while ((temp = reader.readLine()) != null) {
					if(temp.contains("install")){
						this.result="iostat not install";
						LOG.error(this.result);
						break;
					}else{
						StringTokenizer st= new StringTokenizer(temp);
						String in=st.nextToken();
						double ind=Double.valueOf(in);
						inAll+=ind;
						String out=st.nextToken();
						double outd=Double.valueOf(out);
						outAll+=outd;
						
					}
				}
				this.result=inAll+"/"+outAll;
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
