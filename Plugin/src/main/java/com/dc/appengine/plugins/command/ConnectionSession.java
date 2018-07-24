package com.dc.appengine.plugins.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.dc.appengine.plugins.utils.ConfigHelper;

public class ConnectionSession {
	private Connection conn = null;
	private static ConnectionSession instance;
	private Logger log = LoggerFactory.getLogger(ConnectionSession.class);

	public static ConnectionSession getInstance() {
		synchronized (ConnectionSession.class) {
			if (instance == null) {
				instance = new ConnectionSession();
			}
		}
		return instance;
	}

	public ConnectionSession() {
		reload();
		test();
	}

	private void reload() {
		// 初始化连接
		// NodeEnv.getInstance().getPortDefinition().getNodeip();//NodeEnv.getInstance().getLxcDefinition().getHostUser();//ConfigHelper.getInstance().getValue("remoteAddress");
		String host = "localhost";
		String authMethod = ConfigHelper.getValue("sshAuthMethod");

		conn = new Connection(host);
		try {
			conn.connect();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		boolean success = false;
		if ("key".equals(authMethod)) {
			log.debug("auth with public key");
			String privateKey = ConfigHelper.getValue("keyLocation");// ConfigHelper.getInstance().getValue("privateKey");
			File file = new File(privateKey);
			if (!file.exists()) {
				log.error("private key file :" + file.getAbsolutePath()
						+ " not found!");
				return;
			}
			try {
				conn.authenticateWithPublicKey("root", file, "");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if ("password".equals(authMethod)) {
			log.debug("auth with password");
			String userName = ConfigHelper.getValue("hostUser");
			String password = ConfigHelper.getValue("hostPassword");
			try {
				conn.authenticateWithPassword(userName, password);
			} catch (IOException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

		}
		success = conn.isAuthenticationComplete();
		if (!success) {
			System.out.println("connect filed!");
			return;
		}

	}

	public void test() {
		Session s = getSession();
		try {
			s.execCommand("echo test ocu!");
			InputStream is = s.getStdout();
			Scanner c = new Scanner(is);
			while (c.hasNext()) {
				System.out.println(c.nextLine());
			}
			c.close();
			closeSession(s);
		} catch (IOException e) {
			System.out.println("测试脚本执行失败！");
			e.printStackTrace();
		}
	}

	public Session getSession() {
		while (!conn.isAuthenticationComplete()) {
			reload();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Session session = null;
		try {
			session = conn.openSession();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return session;
	}

	public void close() {
		conn.close();
	}

	public void closeSession(Session session) {
		new Thread(new CloseSessionTask(session)).start();
	}

	class CloseSessionTask implements Runnable {
		private Session closeSession = null;

		CloseSessionTask(Session closeSession) {
			this.closeSession = closeSession;
		}

		@Override
		public void run() {
			closeSession.close();
		}

	}

}
