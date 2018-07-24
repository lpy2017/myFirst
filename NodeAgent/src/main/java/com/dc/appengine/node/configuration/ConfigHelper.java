package com.dc.appengine.node.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.utils.FileUtil;

/**
 * 资源文件帮助类，加载配置信息
 * 
 * @author yangleiv
 * 
 */
public class ConfigHelper {
	private static final Logger log = LoggerFactory
			.getLogger(ConfigHelper.class);
	private static Properties properties = new Properties();

	static {
		log.debug("Loading config.properties");

		try {
			InputStream input = new FileInputStream(FileUtil.getInstance()
					.getFile("config.properties", Constants.Env.BASE_CONF));
			properties.load(input);
		} catch (IOException e) {
			log.error("Loading config.properties fails");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return properties.getProperty(key);
	}

	public static void reload() {
		properties.clear();
		try {
			InputStream input = new FileInputStream(FileUtil.getInstance()
					.getFile("config.properties", Constants.Env.BASE_HOME));
			// String path =
			// FileUtil.getInstance().getFile("config.properties",Constants.Env.BASE_HOME).getAbsolutePath();
			properties.load(input);
		} catch (IOException e) {
			log.error("Loading config.properties fails");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
