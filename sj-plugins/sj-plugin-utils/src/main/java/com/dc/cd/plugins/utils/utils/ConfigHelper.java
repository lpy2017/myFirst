package com.dc.cd.plugins.utils.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源文件帮助类，加载配置信息
 *
 */
public class ConfigHelper {
	private static final Logger log=LoggerFactory.getLogger(ConfigHelper.class);
	private static Properties properties=new Properties();
	private static ConfigHelper instance=null;
	public static ConfigHelper getInstance(){
		synchronized (ConfigHelper.class) {
			if(instance == null){
				instance = new ConfigHelper();
			}
			return instance;
		}
	}
	private ConfigHelper(){
		log.debug("Loading config.properties");
		try {
			String path = ConfigHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String pathR = URLDecoder.decode(path, "utf-8");
			String confPath=pathR.substring(0, pathR.lastIndexOf("/"));
			File configFile= new File(confPath+File.separator+"plugins_conf/conf/mxsd_process.properties");
			System.out.println("configFile " +configFile.getAbsolutePath() +" 是否存在 " +configFile.exists());
			if(configFile.exists()){
				InputStream input = new FileInputStream(configFile);
				properties.load(input);
			}
		} catch (IOException e) {
			log.error("Loading config.properties fails");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
//	static{
//		log.debug("Loading config.properties");
//		try {
//			String path = ConfigHelper.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//			String pathR = URLDecoder.decode(path, "utf-8");
//			String confPath=pathR.substring(0, pathR.lastIndexOf("/"));
//			File configFile= new File(confPath+File.separator+"plugins_conf/conf/mxsd_process.properties");
//			System.out.println("configFile " +configFile.getAbsolutePath() +" 是否存在 " +configFile.exists());
//			if(configFile.exists()){
//				InputStream input = new FileInputStream(configFile);
//				properties.load(input);
//			}
//		} catch (IOException e) {
//			log.error("Loading config.properties fails");
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static String getValue(String key){
		if(properties.isEmpty()){
			ConfigHelper.getInstance();
		}
		return properties.getProperty(key);
	}
	public static void setProperties(Map<String, Object> param){
		if(!param.isEmpty()){
			for(Map.Entry<String, Object> entry:param.entrySet()){
				String key=entry.getKey();
				String value=entry.getValue().toString();
				if(properties.containsKey(key)){
					properties.remove(key);
				}
				properties.put(key, value);
			}
		}
	}
}
