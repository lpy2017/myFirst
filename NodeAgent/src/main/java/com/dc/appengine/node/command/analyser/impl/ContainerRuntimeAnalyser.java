package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.docker.DockerStatus;
import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

public class ContainerRuntimeAnalyser extends AbstractAnalyser<Map<String, DockerStatus>> {
 
	private static Logger LOG = LoggerFactory.getLogger(ContainerRuntimeAnalyser.class);
	String SPACE="\\s+"; 
	String B="\\s+B";
	String K="\\s+K";
	String k="\\s+k";
	String M="\\s+M";
	String G="\\s+G";
//	String diagonal ="\\s+/\\s+";
	public void analysis(InputStream is) throws Exception {
		BufferedReader reader = null;
		if (is != null) {
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				 
				Map<String, DockerStatus> map = new HashMap<String, DockerStatus>();
 
				String tmpLine = reader.readLine();
				 
				if(tmpLine!=null&&tmpLine.contains("Error")) {
					LOG.error("collect container runtime info error:"+tmpLine);
					return;
				}
				DockerStatus entity = null;
				while((tmpLine = reader.readLine())!=null){
					tmpLine = tmpLine.replaceAll(B, "B"); 
					tmpLine = tmpLine.replaceAll(K, "K");
					tmpLine = tmpLine.replaceAll(k, "K");
					tmpLine = tmpLine.replaceAll(M, "M");
					tmpLine = tmpLine.replaceAll(G, "G");
					tmpLine = tmpLine.replaceAll("/", " ");
					String []infos = tmpLine.split(SPACE);
					entity = new DockerStatus();
					entity.setConatainer(infos[0]);
					entity.setCpu(infos[1]);
					entity.setMemUsage(infos[2]);
					entity.setMemLimits(infos[3]);
					
					entity.setMem(infos[4]);
					entity.setNetIO(infos[5]+"/"+infos[6]);
					
					map.put(infos[0], entity);
					
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
