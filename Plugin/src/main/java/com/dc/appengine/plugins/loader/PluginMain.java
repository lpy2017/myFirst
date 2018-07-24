package com.dc.appengine.plugins.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.dc.appengine.plugins.manager.service.impl.LoadPlugin;
import com.dc.appengine.plugins.service.impl.PluginService;
import com.dc.appengine.plugins.transfer.ITransferer;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.FileUtil;

public class PluginMain {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		PluginService pluginService= PluginService.getInstance();
		LoadPlugin loadPlugin = new LoadPlugin();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入指令:1.加载指令 [load pluginName ftpPluginPath] \r\n "
				+ "2.触发指令 [do pluginName methodName] \r\n "
				+ "3.结束操作 [bye]");
		String cmd = null;

		try {
			cmd = br.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (!cmd.equals("bye")) {
			if (cmd.startsWith("do")) {
				if(cmd.split(" ").length !=3){
					System.out.println("请按do命令的参数结构输入参数!");
				}else{
					String pluginName = cmd.split(" ")[1];
					String methodName = cmd.split(" ")[2];
					Map<String, Object> param = pluginService.getPlugin(pluginName);
					if(param ==null || param.isEmpty()){
						System.out.println("内存中不存在插件 "+pluginName);
					}
					Map<String, Object> classParam = pluginService.getPluginInstance(pluginName,methodName);
					if(classParam ==null || classParam.isEmpty()){
						System.out.println("jvm 中没有加载插件 "+pluginName+" 的cl");
					}else{
						Object o = classParam.get("classInstance");
						Method m = (Method) classParam.get("methodInstance");
						String result=null;
						try {
							result = (String) m.invoke(o,"test");
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println(result);
					}
					
				}
			}
			if (cmd.startsWith("load")) {
				if(cmd.split(" ").length !=3){
					System.out.println("请按load命令的参数结构输入参数!");
				}else{
					String pluginName = cmd.split(" ")[1];
					String pluginPath=cmd.split(" ")[2];
					Map<String, Object> param = pluginService.getPlugin(pluginName);
					if(param ==null || param.isEmpty()){
						System.out.println("内存中不存在插件 "+pluginName+" 需重新pull plugin");
						ITransferer transferer =null;
						File pluginsFile =null;
						try {
							transferer = new  FtpTransferer();
							boolean testResult = transferer.open();
							if (!testResult) {
								System.out.println("无法连接存储目标！");
							}
							pluginsFile = transferer.download("plugins.json", "plugins.json");//tmpDir+"/"+plugins.json;
							if (pluginsFile != null) {
								Map<String,Object> map = FileUtil.readPluginsJsonToMap(pluginsFile.getAbsolutePath());
								String key=null;
								Map<String,Object> value=null;
								for (Map.Entry<String, Object> entry : map.entrySet()) {
									key= entry.getKey();
									value=(Map<String, Object>) entry.getValue();
									if(key.equals(pluginName)){
										param=value;
									}
								}
							}else{
								System.out.println("目标文件 plugins.json不存在！");
							}
						} finally {
							if(transferer !=null){
								transferer.close();
							}
						}
						loadPlugin.dealPlugin(param, "add");
					}else{
						loadPlugin.dealPlugin(param, "add");
					}
					
				}
			}
//			if (cmd.startsWith("update")) {
//				String pluginName = cmd.split(" ")[1];
//				Map<String, Object> param = pluginService.getPlugin(pluginName);
//				if(param ==null || param.isEmpty()){
//					System.out.println("内存中不存在插件 "+pluginName);
//				}
//				loadPlugin.dealPlugin(param, "update");
//			}
			if (cmd.startsWith("unload")) {
				if(cmd.split(" ").length !=2){
					System.out.println("请按unload命令的参数结构输入参数!");
				}else{
					String pluginName = cmd.split(" ")[1];
					Map<String, Object> param = pluginService.getPlugin(pluginName);
					if(param ==null || param.isEmpty()){
						System.out.println("ftp 的plugins.json中不存在 插件"+pluginName);
					}
					loadPlugin.dealPlugin(param, "delete");
				}
			}
			System.out.println("请输入指令:1.加载指令 [load pluginName] \r\n 2.卸载指令 [unload pluginName] \r\n 3.触发指令1.加载指令 [do pluginName methodName] \r\n ");
			try {
				cmd = br.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
