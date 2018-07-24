/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.cd.plugins.utils.command.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.cd.plugins.utils.command.Analytic;


/**
 * CommandNoWaitExecutor.java
 * 
 * @author liubingj
 */
public class WinCommandNoWaitExecutor extends GenericCommandExecutor {
	private static Logger LOG = LoggerFactory
			.getLogger(WinCommandNoWaitExecutor.class);

	/**
	 * @param analytic
	 */
	public WinCommandNoWaitExecutor(Analytic<?> analytic) {
		super(analytic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dc.appengine.node.command.impl.GenericCommandExecutor#waitFor(java
	 * .lang.Process)
	 */
	protected int waitFor(Process process) throws Exception {
		return 0;
	}

	public boolean execwinstart(String command) {
		boolean status;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);

			new Thread(new SerializeTask(process.getInputStream())).start();
			new Thread(new SerializeTask(process.getErrorStream())).start();

			status = waitFor(process) == 0 ? true : false;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		}
		return status;
	}

}

class SerializeTask implements Runnable {
	private InputStream in;

	public SerializeTask(InputStream in) {
		this.in = in;

	}

	@Override
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
