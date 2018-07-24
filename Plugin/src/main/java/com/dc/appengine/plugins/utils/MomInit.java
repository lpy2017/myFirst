package com.dc.appengine.plugins.utils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.constants.Constants.App;
import com.dc.appengine.plugins.context.Context;
import com.dc.appengine.plugins.context.ContextBuilder;
import com.dc.appengine.plugins.context.ContextPropertiesConverter;
import com.dc.appengine.plugins.message.InvokerInit;
import com.dc.appengine.plugins.message.bean.InnerMessage;
import com.dc.appengine.plugins.service.impl.PAASConfig;
import com.dc.servicejet.ASClient4CD;
import com.dcfs.impls.esb.ESBConfig;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.admin.IAdminMessage;
import com.dcfs.interfaces.esb.admin.IManageComponent;

public class MomInit {
	private static Logger log = LoggerFactory.getLogger(MomInit.class);
	private static MomInit instance;
	public static MomInit getInstance(){
		synchronized (MomInit.class) {
			if(instance==null){
				instance = new MomInit();
			}
		}
		return instance;
	}
	private MomInit(){
		load();
		//加载配置
		ConfigHelper.getInstance();
	}
	
	public void load(){
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//		URL url = getClass().getResource("../message/conf/mxsd_client.xml");
		try {
//			System.out.println(url.getPath());
//			String pathR = URLDecoder.decode(url.getPath(), "utf-8");
//			String confPath=pathR.substring(0, pathR.lastIndexOf("/conf"));
			String pathR = URLDecoder.decode(path, "utf-8");
			String confPath=pathR.substring(0, pathR.lastIndexOf("/"));
			System.out.println(confPath+"/plugins_conf");
			ESBConfig.setRoot(confPath+"/plugins_conf");
			PAASConfig.setRoot(confPath+"/plugins_conf");
			InvokerInit.init();//启动mom invoker
//			PaasTopicClient.getInstance();//启动topic客户端
			this.loadServiceJetClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadServiceJetClient() {
		ASClient4CD.getInstance();
	}
	
	public static void sendMessage4Test(){
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("test", "test");
		InnerMessage<Map<String, Object>> request = new InnerMessage<Map<String, Object>>();
		request.setContent(message);
		request.setOp("DOWNLOAD");
		Context context = ContextBuilder.build("node1", IAdminMessage.ADMIN_MESSAGE, null, request,
				"NodeCommandInvoker");
		Properties header = ContextPropertiesConverter.context2Properties(context);

		header.setProperty("SLEEP_TIME", ESBConfig.getConfig().getProperty("SLEEP_TIME"));
		header.setProperty(App.INSTANCE_ID, "instanceId_test1");
		System.out.println("发送到" + "10.1.108.163" + "信息：" + JSONObject.toJSONString(context.getPayload()));
		//发送消息
		IManageComponent manager = ManagerFactory.getManager();
		manager.process(JSONObject.toJSONString(context.getPayload()).getBytes(), header);
	}
	
}
