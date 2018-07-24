package com.dc.appengine.node.tcp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.flow.DefaultNodeFlowContext;

/**
 * 
 * <p>
 * </p>
 * 
 * @author bing.liu Date 2012-7-20
 */
public class SocketInvoker implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger(SocketInvoker.class);

	private static Configuration configuration;

	private Socket socket;

	public SocketInvoker(Socket socket) {
		this.socket = socket;
		init();
		configuration = this.new Configuration();
	}

	/**
	 * 
	 */
	private void init() {
		try {
			getSocket().setTcpNoDelay(true);
			getSocket().setReuseAddress(true);
		} catch (SocketException e) {
			e.printStackTrace();
			try {
				getSocket().close();
			} catch (IOException e1) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean isActive = true;
		long times = System.currentTimeMillis();
		DataInputStream input = null;
		OutputStream output = null;
		try {
			int length = 0;
			final int init_read_len = configuration.getStart() + configuration.getLength();
			byte[] tempBytes = new byte[init_read_len];
			getSocket().setSoTimeout(12000);
			getSocket().setTcpNoDelay(true);
			input = new DataInputStream(getSocket().getInputStream());
			output = getSocket().getOutputStream();
			while (isActive) {
				LOG.debug("@@@@@@@@@@@begin to getHead:" + times);
				int i = 0;
				while (i < 8) {
					int headpart = input.read();
					if (headpart == -1) {
						isActive = false;
						throw new IOException("client close the conn");
					}
					LOG.debug("##get head part:" + times + " part is :" + headpart);
					tempBytes[i] = (byte) headpart;
					i++;
				}

				LOG.debug("##pares bytes:" + times);

				length = Integer.parseInt(new String(tempBytes, configuration.getStart(), configuration.getLength()),
						10);

				LOG.debug("begin to getBody:" + times);
				byte[] contentBytes = new byte[length];

				input.readFully(contentBytes);

				String strMsg = new String(contentBytes);
				final Context context = new DefaultNodeFlowContext(contentBytes);
				LOG.debug("Current receive message is " + strMsg);
				long flagstamp = System.currentTimeMillis();
				LOG.debug("ready to heartbeat:" + flagstamp);
				long flagstampend = System.currentTimeMillis();
				LOG.debug("end to heartbeat:" + flagstamp + "end :" + flagstampend);
				final byte[] response = context.getPayload();
				output.write(assembleLen(response));
				output.flush();
				LOG.debug("flush ready heartbeat:" + flagstamp + " THREAD:" + times);

			}
		} catch (SocketTimeoutException soe) {
			LOG.error(soe.getMessage(), soe);
			LOG.error("#### read time out :" + times+"******************localport="+socket.getLocalPort()+";remoteport:"+socket.getPort());
			 
			isActive = false;
		} catch (IOException e) {
			if (e.getMessage().contains("client close the conn")) {
				LOG.debug(e.getMessage() + ":" + times+"******************localport="+socket.getLocalPort()+";remoteport:"+socket.getPort());
			} else {
				LOG.error(e.getMessage(), e);
				LOG.error("#### WRANGONG IS :" + times+"******************localport="+socket.getLocalPort()+";remoteport:"+socket.getPort());
			}
			isActive = false;
		}

		finally {
			try {
				if (input != null) {
					input.close();
				}
				if (output != null) {
					output.close();
				}
				if (getSocket() != null) {
					getSocket().close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage()+"******************localport="+getSocket().getLocalPort()+";remoteport:"+getSocket().getPort(), e);
			}
		}
	}

	/**
	 * @param response
	 * @return
	 */
	private byte[] assembleLen(byte[] response) {
		byte[] lenBytes = null;
		try {
			lenBytes = String.valueOf(response.length).getBytes();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		final byte[] totalBytes = new byte[response.length + configuration.getLength()];
		int fillLen = 0;
		if (lenBytes.length < configuration.getLength()) {
			fillLen = configuration.getLength() - lenBytes.length;
			for (int i = 0; i < fillLen; i++) {
				totalBytes[i] = '0';
			}
		}
		System.arraycopy(lenBytes, 0, totalBytes, fillLen, lenBytes.length);
		System.arraycopy(response, 0, totalBytes, configuration.getLength(), response.length);
		return totalBytes;
	}

	// private byte[] cutOut(byte[] bytes, int len) {
	// final byte[] dstBytes = new byte[len];
	// System.arraycopy(bytes, configuration.getLength(), dstBytes, 0, len);
	// return dstBytes;
	// }

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	private class Configuration {

		private int length = 8;

		private int start = 0;

		public int getLength() {
			return length;
		}

		public int getStart() {
			return start;
		}

	}

}
