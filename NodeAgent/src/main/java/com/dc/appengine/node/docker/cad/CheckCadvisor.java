package com.dc.appengine.node.docker.cad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.command.analyser.impl.CadCheckAnalyser;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class CheckCadvisor {
	private static Logger log= LoggerFactory.getLogger(CheckCadvisor.class);
	public static boolean start=false;
	public static  String check(){
		log.debug("start check cadvisor status....");
		
		CadCheckAnalyser analyser= new CadCheckAnalyser();
		CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
		String checkCmd = CommandGenerator.getInstance()
				.generate(Commands.getInstance().get("cadvisor"), false);
		executor.exec(checkCmd);
		String result =analyser.getResult();
		if("ok".equals(result)){
			start=true;
		}
		log.debug("check cadvisor result:"+result);
		return result;
	}
}
