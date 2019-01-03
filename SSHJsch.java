package com.dc.appengine.appmaster.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.utils.FireflyUtil;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class SSHJsch {

	private final static Logger log = LoggerFactory.getLogger(SSHJsch.class);

	private JSch jsch;
	private Session session;
	private ChannelExec channelExec;
	private InputStream in;

	public SSHJsch(String host, String user, String password, int port) throws JSchException {
		this.jsch = new JSch();
		this.session = jsch.getSession(user, host, port);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(password);
		session.connect();
	}

	public InputStream exeCommand(String command) throws JSchException, IOException {
		channelExec = (ChannelExec) session.openChannel("exec");
		in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		return in;
	}

	public void exeCommandAndClose(String command) throws JSchException, IOException {
		channelExec = (ChannelExec) session.openChannel("exec");
		in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.connect();
		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
//				log.info(new String(tmp, 0, i));
			}
			if (channelExec.isClosed()) {
				if (in.available() > 0)
					continue;
				log.info("isclosed exit-status: " + channelExec.getExitStatus());
				break;
			}
			while (channelExec.getExitStatus() < 0) {
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
				if (channelExec.getExitStatus() > 0) {
					break;
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}
		log.info(command + "end----");
		log.info("exit-status: " + channelExec.getExitStatus());
		channelExec.disconnect();
		return;
	}

	public void sftpDownload(String fileName, String downloadPath, String filePath) {

		ChannelSftp sftp = null;
		try {
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.cd(downloadPath);
			sftp.get(fileName, filePath);
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}

	public void sftpDownload(String srcFile, String dstFile) {

		ChannelSftp sftp = null;
		try {
			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();
			sftp.get(srcFile, dstFile);
			;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}

	public void sftpDownloadFile4Git(List<String> srcFileList) {

		ChannelSftp sftp = null;
		try {
			Map<String, Integer> hasDir = new HashMap<>();

			sftp = (ChannelSftp) session.openChannel("sftp");
			sftp.connect();

			for (int i = 0; i < srcFileList.size(); i++) {
				String path = srcFileList.get(i);
				String[] split = path.split("/");
				String dir = "";
				for (int j = 1; j < split.length - 1; j++) {
					dir = dir + "/" + split[j];
				}
				if (dir.length() > 0) {
					FireflyUtil.excuteCommmand("mkdir -p " + dir).close();
				}
				int temp = 600;
				String file = split[split.length - 1];
				while (temp > 0) {
					String str = "";
					for (Object object : sftp.ls(dir)) {
						str = object.toString();
						str = str.substring(str.lastIndexOf(" ")).trim();
						if (str.equals(file)) {
							break;
						}
					}
					if (str.equals(file)) {
						break;
					} else {
						Thread.sleep(1000);
						temp--;
					}
				}
				MyMonitor monitor = new MyMonitor();
				sftp.get(path, path, monitor);
				int time = 600;
				while (monitor.endStatus < 0 && time > 0) {
					Thread.sleep(1000);
					time--;
				}
				log.info("sftp.get monitor endStatus " + monitor.endStatus + " time " + (60 - time) + " file path "
						+ path);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e + " " + e.getMessage());
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
		}
	}

	public void close() {
		if (channelExec != null) {
			channelExec.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}

	public static void main(String[] args) throws IOException, JSchException {
		// TODO Auto-generated method stub

		// long lg2 = System.currentTimeMillis();
		// System.out.println(lg2);
		// long lg = 1542786244198l;
		// Date a = new Date(lg);
		// Date a2 = new Date(lg2);
		// DateFormat df = new SimpleDateFormat("yyyy-MM-dd E a HH:mm:ss");
		// System.out.println(df.format(a));
		// System.out.println(df.format(a2));

		// String host = "10.0.202.102";
		// int port = 22;
		// String user = "root";
		// String password = "root";
		// String command = "cat /home/test/in.xml";
		//// exeCommand(host,port,user,password,command);
		SSHJsch ssh = new SSHJsch("10.126.3.244", "root", "123456", 22);
		ssh.exeCommandAndClose("cd /home/repo/个人网银系统/受控基线库; /root/firefly_client/Firefly/Client/bin/hff" + 
				" init -h 10.126.3.244 -port 4759 -u admin -pwd admin"
				+" -i -proj  个人网银系统 -p 受控基线库  -d /home/repo/个人网银系统/受控基线库");
		ssh.close();
		
//		SSHJsch ssh = new SSHJsch("10.0.220.63", "root", "root123", 22);
//		ChannelSftp sftp = (ChannelSftp) ssh.session.openChannel("sftp");
//		sftp.connect();
//		Vector ls;
//		try {
//			ls = sftp.ls("/tmp/firefly/00001_个人网银系统/受控非基线库/uuid:hex:1026BECF-BA7D-0A35-4F28-3FBDE68F334C/源代码/cis");
//			for (Object obj : ls) {
//				System.out.println(ls.size());
//				String str = obj.toString();
//				str = str.substring(str.lastIndexOf(" ")).trim();
//				System.out.println(str);
//				if (str.trim().equals("aa3.txt")) {
//
//					System.out.println(str);
//				}
//			}
//
//		} catch (SftpException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			sftp.disconnect();
//		}
		// ssh.exeCommandAndClose("cd
		// /home/chy/firefly_project;/root/firefly_client/Firefly/Client/bin/hff
		// bringover");
		// ssh.exeCommand2("cd
		// /home/chy/firefly_project;/root/firefly_client/Firefly/Client/bin/hff" +
		// " br getcs -h 10.0.220.63 -u admin -pwd admin -proj chyproject -b master
		// uuid:hex:7103FDE6-9EC3-88A2-60B4-44594DB67728"
		// + " -o /home/getcs.txt");
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(in,"gb2312"));
		// String line = reader.readLine();
		// while(!line.contains("<")) {
		//
		// System.out.println(line);
		// line = reader.readLine();
		// }
		// System.out.println(line);
		// List<FireflyEntity> changeSetInfo =
		// FireflyUtil.getChangeSetInfo(ssh.exeCommand(command));
		// ssh.exeCommand("mv /home/out.xml /home/test/");
		// ssh.close();
	}

	public class MyMonitor implements SftpProgressMonitor {
		public int endStatus = -1;

		@Override
		public void init(int op, String src, String dest, long max) {
		}

		@Override
		public boolean count(long count) {
			return false;
		}

		@Override
		public void end() {
			endStatus = 1;
		}

	}
}
