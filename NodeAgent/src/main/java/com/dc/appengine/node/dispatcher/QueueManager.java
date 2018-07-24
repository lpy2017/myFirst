package com.dc.appengine.node.dispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.message.InnerMessage;

public class QueueManager {

	private static Map<String, TaskExecutor> appOP;
	private static volatile QueueManager instance;
	public static ThreadPoolExecutor pool = null;

	private QueueManager() {
		init();
	}

	public void init() {
		appOP = Collections
				.synchronizedMap(new HashMap<String, TaskExecutor>());
		pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(30,
				new ThreadFactory() {
					public Thread newThread(Runnable r) {
						return new Thread(r, "MsgQueue-thread");
					}
				});
	}

	public static QueueManager getInstance() {
		synchronized (QueueManager.class) {
			if (instance == null)
				instance = new QueueManager();
		}
		return instance;
	}

	// 如果首次操作该实例，为其创建一个blockingQueue；
	// 若该实例已经存在queue，则将操作添加到队列中
	public void addOperate(Map<String, Object> msg) {
		InnerMessage<Map<String, String>> bodyMsg = getMsg((Context) msg
				.get("context"));
		String instanceId = bodyMsg.getContent().get("INSTANCE_ID");
		String appId = bodyMsg.getContent().get("APP_ID");

		if (appOP.containsKey(appId + "_" + instanceId)) {
			TaskExecutor task = appOP.get(appId + "_" + instanceId);
			task.put(msg);
		} else {
			TaskExecutor task = new TaskExecutor();
			task.set(appId + "_" + instanceId);
			task.start();
			task.put(msg);
			appOP.put(appId + "_" + instanceId, task);
		}
		// System.out.println("accept op: "+bodyMsg.getOp());
		// System.out.println("Current values of QueueManager: "+appOP.size());
	}

	// iptables操作全部放入一个队列
	public synchronized void addOperate(Map<String, Object> msg, String id) {
		if (appOP.containsKey(id)) {
			TaskExecutor task = appOP.get(id);
			task.put(msg);
		} else {
			TaskExecutor task = new TaskExecutor();
			task.set(id);
			task.start();
			task.put(msg);
			appOP.put(id, task);
		}
	}

	public void delOperate(InnerMessage<Map<String, String>> message) {
		String instanceId = message.getContent().get("INSTANCE_ID");
		String appId = message.getContent().get("APP_ID");
		if (appOP.containsKey(appId + "_" + instanceId)) {
			TaskExecutor task = appOP.remove(appId + "_" + instanceId);
			task.stop();
			task = null;
		}
		// System.out.println("************delete "+appId+" pool");
		// System.out.println("************Current values of appOP: "+
		// appOP.size());
	}

	@SuppressWarnings("rawtypes")
	private static InnerMessage<Map<String, String>> getMsg(Context context) {
		byte[] bytes = (byte[]) context.getPayload();
		Map bodyMessage = JSON.parseObject(new String(bytes));
		String content = bodyMessage.get("content").toString();
		String op = bodyMessage.get("op").toString();
		@SuppressWarnings("unchecked")
		Map<String, String> conMap = JSON.parseObject(content,
				new TypeReference<Map>() {
				});
		InnerMessage<Map<String, String>> message = new InnerMessage<Map<String, String>>();
		message.setOp(op);
		message.setContent(conMap);
		return message;
	}

}
