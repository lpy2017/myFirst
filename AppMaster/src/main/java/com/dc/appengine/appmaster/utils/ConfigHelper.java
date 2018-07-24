package com.dc.appengine.appmaster.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * 资源文件帮助类，加载配置信息
 * @author yangleiv
 *
 */
public class ConfigHelper {
	private static final Logger log=LoggerFactory.getLogger(ConfigHelper.class);
	private static Properties properties=new Properties();
	
	static{
		log.debug("Loading config.properties");
		
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key){
		return properties.getProperty(key);
	}
	
	public static void reload(){
		properties.clear();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
