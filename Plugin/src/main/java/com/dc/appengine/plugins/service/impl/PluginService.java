package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.loader.PluginClassLoader;
import com.dc.appengine.plugins.transfer.ITransferer;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.FileUtil;

public class PluginService {
	private static final Logger log = LoggerFactory.getLogger(PluginService.class);
	private static Map<String ,AbstractPlugin> plugins = new HashMap<String,AbstractPlugin>();
	private static Map<String, Object> puginsRecordMap = Collections.synchronizedMap(new HashMap<String, Object>());
	private static Map<String ,PluginClassLoader> puginsClMap = new HashMap<String,PluginClassLoader>();
	private static PluginService instance;
	public static PluginService getInstance(){
		synchronized (PluginService.class) {
			if(instance == null){
				instance = new PluginService();
			}
			return instance;
		}
	}
	private PluginService(){
		if(Constants.WORKFLOW.equals(ConfigHelper.getInstance().getValue("useName"))){
			try {
				String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
				String pathR= URLDecoder.decode(path, "utf-8");
				String confPath=pathR.substring(0, pathR.lastIndexOf("/"));
				System.setProperty("configPath", confPath+"/plugins_conf");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 load();
			 
		}
	}
	/*
	 * 获取插件列表
	 */
//	public String getPluginList() {
//		String json = "";
//		Map<String,Object> map = new HashMap<String,Object>();
//		if (puginsRecordMap == null) {
//			json = map.toString();
//		}else{
//			json = puginsRecordMap.toString();
//		}
//		return json;
//	}

	/*
	 * 获取插件
	 * 
	 * @
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPlugin(String pluginName) {
//		boolean defaultP = label != null ? false : true;
		//插件名称_key，例如"pluginName":" download _-345"
		if(pluginName.contains("_")){
			pluginName = pluginName.split("_")[0];
		}
		Map<String, Object> map = null;
		if(puginsRecordMap !=null){
			map = (Map<String, Object>) puginsRecordMap.get(pluginName);
		}
		return map;
	}
	//加载插件基本信息
	public  void loadPluginsRecord(String path) {
		String json = "";
		try {
			String pathP = URLDecoder.decode(path, "utf-8");
			Scanner scann = null;
			scann = new Scanner(new FileInputStream(pathP));
			StringBuilder jsonBuilder = new StringBuilder();
			while (scann.hasNext()) {
				jsonBuilder.append(scann.nextLine());
			}
			json = jsonBuilder.toString().trim();
			if (json == null || "".equals(json)) {
//				log.error("none plugin info");
			}
			Map<String, Object> map = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
			});
			puginsRecordMap.putAll(map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 加载（非CD平台内置的）插件实例
	 */
	@SuppressWarnings("unchecked")
	public  void loadPluginsCl(String path) {
		String pathP=null;
		try {
			pathP = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(Map.Entry<String, Object> entry:puginsRecordMap.entrySet()){
			String key=entry.getKey();
			Map<String,Object> value =(Map<String,Object>)entry.getValue();
			loadPluginCl(pathP, (Map<String,Object>)value);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getPluginInstance(String pluginName,String method){
		Map<String, Object> map= new HashMap<String, Object>();
		try{
			Map<String, Object> pluginMap= getPlugin(pluginName);
			String classImpl =pluginMap.get(method).toString();
			String path=pluginMap.get("path").toString();
			ClassLoader cl =null;;
			if(path==null||"".equals(path)){
			    cl = Thread.currentThread().getContextClassLoader();
			}else{
				cl =getLoader(pluginName);
			}
			String[] classM = classImpl.split("#");
            Class<?> clzj = Class.forName(classM[0], true, cl);//this.pluginMap.get(pluginName).loadClass(packagename);
            Method mj = clzj.getMethod(classM[1],String.class);
			mj.setAccessible(true);
			map.put("classInstance", clzj.newInstance());//类实例
			map.put("methodInstance",mj);//方法实例
        }catch(Exception e){
            e.printStackTrace();
            log.error("获取插件"+"pluginName="+pluginName+" method="+method+" 实例异常！");
        }
		return map;
	}
	
	public void load(String useName,Map<String,Object>ftpParm){
		log.debug("pluginManager init......."+"userName= "+useName+" ftpParm"+JSON.toJSONString(ftpParm));
//		log.debug("pluginManager init......."+"userName= "+useName+" ftpParm"+JSON.toJSONString(ftpParm));
		if(Constants.NODE.equals(useName)){
			ConfigHelper.setProperties(ftpParm);
			System.setProperty("configPath", System.getProperty("com.dc.install_path"));
		}
		pullAndLoadPlugins();
	}
	public void load(){
		//pull去插件，并加载插件基本信息，获取热部署插件的classloader
		pullAndLoadPlugins();
		//加载插件实例
		initPlugins();
	}
	
	/*
	 * 拉取插件列表，并加载插件
	 */
	public void pullAndLoadPlugins(){
		File pluginFile = pullPlugins();
		loadPlugins(pluginFile);
	}
	
	/*
	 * 加载插件
	 */
	public void loadPlugins(File pluginFile){
		String pluginsPath=pluginFile.getAbsolutePath();
		loadPluginsRecord(pluginsPath);
		loadPluginsCl(ConfigHelper.getValue("ftp.tmpDir"));
	}
	/*
	 * 从ftp上下载plugins.json
	 */
	public File pullPlugins(){
		Map<String, Object> map = new HashMap<>();
		ITransferer transferer =null;
		File pluginsFile =null;
		try {
			// 测试连接
//			ITransferer transferer = TransfererFactory.getInstance().getTransferer();
			transferer = new  FtpTransferer();
			boolean testResult = transferer.open();
			if (!testResult) {
				log.error("无法连接存储目标！");
			}
			pluginsFile = transferer.download("plugins.json", "plugins.json");//tmpDir+"/"+plugins.json;
			if (pluginsFile != null) {
				map = FileUtil.readPluginsJsonToMap(pluginsFile.getAbsolutePath());
				String key=null;
				Map<String,Object> value=null;
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					key= entry.getKey();
					value=(Map<String, Object>) entry.getValue();
					if(value.get("path")!= null && !"".equals(value.get("path").toString())){
						String pluginPath=value.get("path").toString();
						transferer.download(pluginPath, pluginPath);
					}
				}
			}else{
				log.error("目标文件 plugins.json不存在！");
			}
		} finally {
			if(transferer !=null){
				transferer.close();
			}
		}
		return pluginsFile;
	}
	/*
	 * 加载非内置插件的类加载器
	 */
	@SuppressWarnings("unchecked")
	public  void loadPluginCl(String path,Map<String,Object> plugin) {
		String pathP=null;
		try {
			pathP = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(plugin.get("path")!= null && !"".equals(plugin.get("path").toString())){
			String pluginName= plugin.get("pluginName").toString();
			String filePath = plugin.get("path").toString();
			String jarPath= pathP+File.separator+filePath.replace(".zip", ".jar");
			File pathFile = new File(jarPath);
			FileUtil.unZipFile(pathP+File.separator+filePath, pathFile.getParent());
			loadPlugin(pluginName, pathFile);
		}
	}
    public PluginClassLoader getLoader(String pluginName){
    	if(pluginName.contains("_")){
			pluginName = pluginName.split("_")[0];
		}
    	PluginClassLoader pcl=puginsClMap.get(pluginName);
        return pcl;
    }
    public static void loadPlugin(String pluginName,File file){
        puginsClMap.remove(pluginName);
        PluginClassLoader loader = new PluginClassLoader();
        String pluginurl = "jar:file:"+file.getAbsolutePath()+"!/";
        log.debug(" jar packagePath["+pluginName+"]"+pluginurl);
        URL url = null;
        try {
            url = new URL(pluginurl);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        loader.addURLFile(url);
        puginsClMap.put(pluginName, loader);
        log.debug("load " + pluginName + "success"+": pluginsClMap=="+JSON.toJSONString(puginsClMap));
    }
    public static void unloadPluginCl(String pluginName){
        puginsClMap.get(pluginName).unloadJarFiles();
        puginsClMap.remove(pluginName);
    }
    public static void addPuginsRecordMap(String pluginName,Map<String,Object> plugin){
    	puginsRecordMap.remove(pluginName);
    	puginsRecordMap.put(pluginName, plugin);
    }
    public static void deletePuginsRecordMap(String pluginName){
    	puginsRecordMap.remove(pluginName);
    }
    public Map<String,Object> getPuginsRecordMap(){
    	return puginsRecordMap;
    }

    public void initPlugins(){
		try{
			for(Map.Entry<String, Object> one:puginsRecordMap.entrySet()){
				String pluginName = one.getKey();
				Map<String, Object> pluginMap = (Map<String, Object>) one.getValue();
				String classImpl =pluginMap.get("preAction").toString();//只使用前处理方法就可以获取，插件的全类名
				String path=pluginMap.get("path").toString();
				ClassLoader cl =null;;
				if(path==null||"".equals(path)){
					cl = Thread.currentThread().getContextClassLoader();
				}else{
					cl =getLoader(pluginName);
				}
				String[] classM = classImpl.split("#");
				Class<?> clzj = Class.forName(classM[0], true, cl);//this.pluginMap.get(pluginName).loadClass(packagename);
				clzj.newInstance();
				plugins.put(pluginName, (AbstractPlugin)clzj.newInstance());
			}
        }catch(Exception e){
            e.printStackTrace();
            log.error("加载插件实例，失败!");
        }
    }
    public AbstractPlugin getPluginRT(String pluginName){
    	if(pluginName.contains("_")){
			pluginName = pluginName.split("_")[0];
		}
		return plugins.get(pluginName);
	} 
	public static void main(String [] args){
//		System.out.println(PluginService.getInstance().getPlugin("download"));
		String pluginName ="test";
		File file = new File("F:/test/CMDTest.jar");
		PluginService.loadPlugin(pluginName, file);
		ClassLoader cl = puginsClMap.get(pluginName);
		String classTest = "com.dc.appengine.plugins.service.impl.CMDTest";
        Class<?> clzj;
		try {
			clzj = Class.forName(classTest, true, cl);
			AbstractPlugin cmd =(AbstractPlugin) clzj.newInstance();
			cmd.preAction("test");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
