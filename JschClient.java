package com.dc.appengine.appmaster.utils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public enum JschClient {
	
	client;
	
	private JSch jsch = new JSch();
	
	public void addHost(String host, String username, String password){
		Session session = null;
		try {
			session = jsch.getSession(username, host);
			session.setPassword(password);
			session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
			jsch.getHostKeyRepository().add(session.getHostKey(), null);
		}
		if (session.isConnected()) {
			session.disconnect();
		}
	}
	
	public Session connect(String host, String username, String password) {
		addHost(host, username, password);
		Session session = null;
		try {
			session = jsch.getSession(username, host);
			session.setPassword(password);
			session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return session;
	}
	
	public String exec(Session session, String command) throws JSchException, IOException {
		Channel channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		PipedOutputStream pos=new PipedOutputStream();
		PipedInputStream pis=new PipedInputStream(pos);
		channel.setOutputStream(pos);
		channel.setExtOutputStream(pos);
		channel.connect();
		String result = IOUtils.toString(pis, "utf-8");
		channel.disconnect();
		session.disconnect();
		return result;
	}
	
	public static void main(String[] args) throws JSchException, IOException {
		Session session = JschClient.client.connect("10.126.3.161", "root", "123456");
		System.out.println(JschClient.client.exec(session, "mkdir /tmp/guojwe ; ls -l /tmp|grep guojwe && ls -l /tmp|grep guojwe"));
	}

}
