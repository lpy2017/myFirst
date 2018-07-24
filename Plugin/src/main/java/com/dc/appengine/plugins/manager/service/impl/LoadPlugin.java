package com.dc.appengine.plugins.manager.service.impl;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.plugins.service.impl.PluginService;
import com.dc.appengine.plugins.transfer.ITransferer;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.MessageHelper;


public class LoadPlugin {
	private static final Logger  log =  LoggerFactory.getLogger(LoadPlugin.class);

	public LoadPlugin() {
	}
	
	public String dealPlugin(Map<String, Object> param,String op){
		String result = MessageHelper.wrap("result", false, "message", "插件load失败！");
		if("update".equals(op) || "add".equals(op)){
			// TODO Auto-generated method stub
			String pluginName = param.get("pluginName").toString();
			String resultM =MessageHelper.wrap("result", false, "message", "插件"+pluginName+"热加载失败！"); 
			String parentPath=null;
			ITransferer transferer =null;
			String filePath =null;
			try {
				filePath = param.get("path").toString();
				if(filePath ==null || "".equals(filePath)){
					PluginService.getInstance().addPuginsRecordMap(pluginName, param);
					resultM =MessageHelper.wrap("result", true, "message", "插件"+pluginName+"更新成功！");
				}else{
					String fileName = param.get("fileName").toString();
					String remotePath= filePath.substring(0, filePath.lastIndexOf("/"));
					String localTmp = remotePath;
					// 测试连接
					transferer = new  FtpTransferer();
					boolean testResult = transferer.open();
					if (!testResult) {
						log.error("无法连接存储目标！");
						resultM = MessageHelper.wrap("result", false, "message", "无法连接存储目标！");
					}
					File pluginFile = transferer.download(filePath, localTmp+File.separator+fileName.replace(".jar", ".zip"));
					PluginService.getInstance().addPuginsRecordMap(pluginName, param);
					PluginService.getInstance().loadPluginCl(ConfigHelper.getValue("ftp.tmpDir"), param);
					resultM =MessageHelper.wrap("result", true, "message", "插件"+pluginName+"热加载成功！");
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error(resultM, e.getMessage());
			}finally {
				if (parentPath != null) {
					File f = new File(parentPath);
					if (f.exists()) {
						FileUtil.delDirectory(f);
					}
				}
				if(transferer !=null){
					transferer.close();
				}
			}
			return resultM;
		}else if("delete".equals(op)){
			String pluginName = param.get("pluginName").toString();
			String resultM =MessageHelper.wrap("result", false, "message", "插件"+pluginName+"热卸载失败！"); 
			String filePath = param.get("path").toString();
			String fileName = param.get("fileName").toString();
			String localTmp = ConfigHelper.getInstance().getValue("ftp.tmpDir");
			if(filePath!=null && !"".equals(filePath)){
				PluginService.getInstance().unloadPluginCl(pluginName);
			}
			PluginService.getInstance().deletePuginsRecordMap(pluginName);
			//删除文件
			File file = new File(localTmp+File.separator+filePath);
			File fileParent=new File(file.getParent());
			log.debug("插件"+pluginName+"存放路径"+file.getAbsolutePath());
			if(!File.separator.equals(file.getParent()) && fileParent.exists()){
				FileUtil.delDirectory(fileParent);
			}
			resultM =MessageHelper.wrap("result", true, "message", "插件"+pluginName+"热卸载成功！"); 
			return resultM;
		}
		return result;
	}

}
