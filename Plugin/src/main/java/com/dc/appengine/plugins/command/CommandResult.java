package com.dc.appengine.plugins.command;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.command.analyser.ErrorAnalyser;
import com.dc.appengine.plugins.command.analyser.ListAnalyser;
import com.dc.appengine.plugins.command.analyser.impl.EchoAnalyseer;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;
import com.dc.appengine.plugins.command.executor.GenericCommandExecutor;
import com.dc.appengine.plugins.command.executor.WinCommandNoWaitExecutor;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.service.IProcess;
import com.dc.appengine.plugins.utils.FilePath;

public class CommandResult {
	private static Logger log = LoggerFactory.getLogger(CommandResult.class);

	public static <T> T getResult(Analytic<T> analytic,
			Class<? extends GenericCommandExecutor> commandExecutor,
			String command, boolean execDirect) {
		String execCommand = execDirect ? command : CommandGenerator
				.getInstance().generate(Commands.getInstance().get(command),
						false);
		try {
			GenericCommandExecutor c = commandExecutor.getDeclaredConstructor(
					Analytic.class).newInstance(analytic);
			c.exec(execCommand);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error(e.getMessage(), e);
		}
		return (T) analytic.getResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> String getResult(Analytic<T> analytic,
			Class<? extends GenericCommandExecutor> commandExecutor,
			boolean execDirect, Map<String, Object> map) {
		 String command = "";
		 command = (String) map.get(Constants.Plugin.COMMAND_FILE);
		 command = FilePath.getFilePath(command);
		List<String> p = (List) map.get(Constants.Plugin.COMMAND_PARAMS);
		String params = "";
		if (p != null) {
			for (String s : p) {
				params += " " + s;
			}
		}
		if (analytic == null) {
			analytic = (Analytic<T>) new ListAnalyser();
		}
		if (commandExecutor == null) {
			commandExecutor = CommandWaitExecutor.class;
		}
		return JSON.toJSONString(getResult(analytic, commandExecutor, command,
				execDirect, params));
	}

	public static <T> Map<String, Object> getResult(Analytic<T> analytic,
			Class<? extends GenericCommandExecutor> commandExecutor,
			String command, boolean execDirect, String... params) {
		String execCommand = execDirect ? command : CommandGenerator
				.getInstance().generate(command, false, params);
		ErrorAnalyser errorAnalyser = new ErrorAnalyser();
		try {
			GenericCommandExecutor c = commandExecutor.getDeclaredConstructor(
					Analytic.class).newInstance(analytic);
			c.setErrAnalytic(errorAnalyser);
			c.exec(execCommand);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			log.error(e.getMessage(), e);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("no error".equals(errorAnalyser.getResult())) {
			resultMap.put("result", true);
			resultMap.put("message", (T) analytic.getResult());
		} else {
			resultMap.put("result", false);
			resultMap.put("message", errorAnalyser.getResult());
		}
		return resultMap;
	}

	public static void main(String args[]) {
		String result = CommandResult.getResult(new EchoAnalyseer(),
				WinCommandNoWaitExecutor.class, "echo 1", true);

		System.out.println(result);
	}
}
