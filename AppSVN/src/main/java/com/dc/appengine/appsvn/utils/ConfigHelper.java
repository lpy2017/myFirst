package com.dc.appengine.appsvn.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

/**
 * 资源文件帮助类，加载配置信息
 * 
 * @author xuxyc
 * 
 */ 
@ConfigurationProperties
@PropertySource("classpath:configs/config.properties")
public class ConfigHelper {
	private static final Logger log = LoggerFactory
			.getLogger(ConfigHelper.class);
	private static Properties properties = new Properties();

	static {
		log.debug("Loading config.properties");
//		InputStream input = ConfigHelper.class.getClassLoader()
//				.getResourceAsStream("config.properties");
		try {
			InputStream input = new FileInputStream(ResourceUtils.getFile("classpath:configs/config.properties"));
			properties.load(input);
			// 配置参数从页面下发
			// if(properties.getProperty("management").equals("true")) {
			// reload();
			// }
		} catch (IOException e) {
			log.error("Loading config.properties fails");
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return properties.getProperty(key);
//		return properties.get(key);
	}

	// 重新加载配置信息
	public static void reload() throws Exception {
		properties.clear();
		load();
	}

	
	
	public static void load(){
		log.debug("Loading config.properties");
		InputStream input = ConfigHelper.class.getClassLoader()
				.getResourceAsStream("config.properties");
		File file = new File("config.properties");
		String filepath = file.getAbsolutePath();
		try {
			properties.load(input);
		} catch (IOException e) {
			log.error("Loading config.properties fails");
			e.printStackTrace();
		}
	}
}
