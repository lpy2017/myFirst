package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.dc.appengine.plugins.service.IPAASConfig;


/**
 * 
 * 参数配置类
 * 
 * @author jiangchuan
 *
 */
public class PAASConfig implements IPAASConfig {
	private static final Logger log = LoggerFactory.getLogger(PAASConfig.class);
	private static PAASConfig paasConfig;
	// private Properties props = new Properties();
	private HashMap<String, String> props = new HashMap<String, String>();
	private static String appRoot;
	private static String instroot = null;
	public static void setRoot(String path) {
		appRoot = path;
		if (!isAbsolutePath(path)) {
			appRoot = new File(path).getAbsolutePath();
		}
		instroot = path;
	}

	private PAASConfig() throws IOException, SAXException, ParserConfigurationException {
		init();
	}

	private void init() {
		Properties properties = new Properties();
		if(instroot == null){			
			instroot = System.getProperty("com.dc.install_path");
		}
		// if (instroot == null)
		// instroot = System.getProperty("com.dc.install_path");
		if (!isAbsolutePath(instroot)) {
			instroot = new File(instroot).getAbsolutePath();
		}
		FileInputStream fin = null;
		if (instroot == null) {
			instroot = appRoot;
			if (log.isDebugEnabled()) {
				log.debug("Undefine  the parameter [-Dcom.bis.install_path],using specifid:"
						+ appRoot);
			}
		}
		try {
			fin = new FileInputStream(new File(instroot + File.separator + "conf",
					"mxsd_process.properties"));
			properties.load(fin);
		} catch (Exception ex) {
			if (log.isErrorEnabled()) {
				log.error("Load system config error.", ex);
			}
		} finally {
			try {
				fin.close();
			} catch (Exception e) {
			}
		}
		properties.setProperty(INSTALL_ROOT, instroot);
		Enumeration en = properties.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			props.put(key, properties.getProperty(key));
		}
	}

	// add by wu.bo 2009-05-04
	// modified by wu.bo 2010-08-19
	/**
	 * the configurations can be modified by the Monitor, then the
	 * configurations should be reload.
	 * 
	 */
	public void reload() {
		paasConfig._reload();
	}

	private void _reload() {
		if (props == null) {
			props = new HashMap<String, String>();
		} else {
			props.clear();
			init();
		}

	}

	// modified by wu.bo 2009-01-08
	/**
	 * 
	 * you can specify an executor yourself in the configuration file
	 * mxsd_process.properties as follows:
	 * <p>
	 * thread_pool_impl=com.dcfs.demo.MyExecutor
	 * <p>
	 * which implements the interface java.util.concurrent.Executor. you should
	 * initialize all your executor properties(e.g. pool size 銆?? policy) with
	 * the default constructor. <BR>
	 * Otherwise,an default executor will be created,and which size is 20.
	 * Also,you can specify the size by property "thread_pool_size"(Attention
	 * please : Default Executor Only);
	 * 
	 * @see java.util.concurrent.Executor;
	 * @return
	 */
	public static final Executor createThreadPool() {
		if (getConfig().getProperty(IPAASConfig.THREAD_POOL_IMPL) != null
				&& !"".equalsIgnoreCase(getConfig().getProperty(IPAASConfig.THREAD_POOL_IMPL))) {
			String clazzExecutor = getConfig().getProperty(IPAASConfig.THREAD_POOL_IMPL);
			try {
				if (log.isInfoEnabled()) {
					log.info("specified Excutor.[" + clazzExecutor + "]");
				}
				Class.forName(clazzExecutor);
				return (Executor) Class.forName(clazzExecutor).newInstance();
			} catch (Throwable th) {
				if (log.isWarnEnabled()) {
					log.warn("Unable to initialize the specified Excutor.[" + clazzExecutor + "]");
				}
			}
		}
		return getDefaultExecutor();

	}

	private static Executor getDefaultExecutor() {
		if (getConfig().getProperty(IPAASConfig.THREAD_POOL_SIZE) != null
				&& !"".equalsIgnoreCase(getConfig().getProperty(IPAASConfig.THREAD_POOL_SIZE))) {
			try {
				if (log.isInfoEnabled()) {
					log.info("Executor size : "
							+ getConfig().getProperty(IPAASConfig.THREAD_POOL_SIZE));
				}
				return Executors.newFixedThreadPool(Integer.parseInt((String) getConfig()
						.getProperty(IPAASConfig.THREAD_POOL_SIZE)));
			} catch (Throwable th) {
				if (log.isWarnEnabled()) {
					log.warn("Create default executor error:" + th);
				}
			}
		}
		if (log.isInfoEnabled()) {
			log.info("Executor size : 20 [default]");
		}
		return Executors.newFixedThreadPool(20);
	}

	public static final IPAASConfig getConfig() {
		if (paasConfig == null)
			synchronized (PAASConfig.class) {
				if (paasConfig == null)
					try {
						paasConfig = new PAASConfig();
					} catch (Exception ex) {
						if (log.isErrorEnabled()) {
							log.error("Load ESB config error.", ex);
						}
					}
			}
		return paasConfig;
	}

	public String getProperty(String name) {
		return props.get(name);
	}

	public String getInstallRoot() {

		return props.get(INSTALL_ROOT);
	}

	private static boolean isAbsolutePath(String url) {
		if (url == null || url.length() < 1) {
			return true;
		}
		if (url.startsWith(".")) {
			return false;
		}
		return true;

	}

	public boolean isEncrypt() {
		return getBooleanProperty(IS_ENCRYPT, false);
	}

	private boolean getBooleanProperty(String name, boolean defaultValue) {
		String prop = getProperty(name);
		boolean bRet = defaultValue;
		if (prop != null) {
			try {
				bRet = new Boolean(prop).booleanValue();
			} catch (Exception e) {
			}
		}
		return bRet;
	}

	public int getMaxSessions() {

		String sessionSize = props.get(MAX_SESSIONS);

		if (sessionSize == null || "".equals(sessionSize.trim())) {
			return 0;
		} else {
			return Integer.parseInt(sessionSize);
		}
	}

	public int getSleepTime() {
		String sleepTime = props.get(SLEEP_TIME);

		if (sleepTime == null || "".equals(sleepTime.trim())) {
			return 0;
		} else {
			return Integer.parseInt(sleepTime);
		}
	}

	public long getFlowControlTimeOut() {
		String flowcontroltimeout = props.get(FLOWCONTROL_TIMEOUT);

		if (flowcontroltimeout == null || "".equals(flowcontroltimeout.trim())) {
			return 20000;
		} else {
			return Long.parseLong(flowcontroltimeout);
		}
	}

	public int getNodeType() {
		String type = props.get(NODE_TYPE);
		if (type == null || "".equals(type.trim())) {
			return 0;
		} else {
			return Integer.parseInt(type);
		}
	}

	/**
	 * 
	 * @return LogInvoker is enable or not
	 */
	public boolean isLogInvokerEnable() {
		return (props.get(LOGINVOKER_SWITCH) != null && props.get(LOGINVOKER_SWITCH)
				.equalsIgnoreCase("on")) ? true : false;
	}

}
