/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.cd.plugins.utils.command.executor;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.cd.plugins.utils.command.Analytic;
import com.dc.cd.plugins.utils.command.ConnectionSession;
import com.dc.cd.plugins.utils.utils.ConfigHelper;

import ch.ethz.ssh2.Session;

/**
 * GenericCommandExecutor.java
 * 
 * @author liubingj
 */
public abstract class GenericCommandExecutor implements CommandExecutor {

	private static Logger LOG = LoggerFactory
			.getLogger(GenericCommandExecutor.class);

	private Analytic<?> analytic;

	private Analytic<?> errAnalytic;

	public GenericCommandExecutor(Analytic<?> analytic) {
		this.analytic = analytic;
	}

	public Analytic<?> getErrAnalytic() {
		return errAnalytic;
	}

	public void setErrAnalytic(Analytic<?> errAnalytic) {
		this.errAnalytic = errAnalytic;
	}

	public boolean exec(String command) {
		// 获取配置
//		String execMethod = ConfigHelper.getValue("shellExecutor");
		String execMethod = "shell";
		boolean status = false;
		if ("".equals(execMethod) || "shell".equals(execMethod)) {
			status = execShell(command);
		} else if ("ssh".equals(execMethod)) {
			status = execSsh(command);
		}
		return status;

	}

	public boolean execSsh(String command) {
		boolean status = false;
		Session session = ConnectionSession.getInstance().getSession();
		try {
			session.execCommand(command);
			InputStream in = session.getStdout();
			InputStream err_in = session.getStderr();

			getAnalytic().analysis(in);

			if (getErrAnalytic() != null)
				getErrAnalytic().analysis(err_in);
			status = true;
		} catch (IOException e) {
			e.printStackTrace();
			status = false;
		} catch (Exception ex) {
			ex.printStackTrace();
			status = false;
		} finally {
			ConnectionSession.getInstance().closeSession(session);
		}

		return status;

	}

	public boolean execShell(String command) {
		boolean status;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			if (getAnalytic() != null) {
				process.getOutputStream().close();
				InputStream stream = process.getInputStream();
				InputStream errStream = process.getErrorStream();
				getAnalytic().analysis(stream);
				if (getErrAnalytic() != null)
					getErrAnalytic().analysis(errStream);
			}
			status = waitFor(process) == 0 ? true : false;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			status = false;
		} finally {
			if (process != null) {
				try {
					process.getErrorStream().close();
					process.getInputStream().close();
					process.getOutputStream().close();
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
				}
				// process.destroy();
				process = null;
			}
		}
		return status;
	}

	public Analytic<?> getAnalytic() {
		return analytic;
	}

	public void setAnalytic(Analytic<?> analytic) {
		this.analytic = analytic;
	}

	protected abstract int waitFor(Process process) throws Exception;

}
