/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.flow;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.ConfigReader;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.exception.NoSuchFlowDefException;
import com.dc.appengine.node.flow.model.FlowDefinition;
import com.dc.appengine.node.flow.model.FlowsDefinition;
import com.dc.appengine.node.utils.FileUtil;

/**
 * Node简单流程引擎 <b>
 * <p>
 * 执行通过配置文件flow/flows.xml配置的流程。 当前包含两类流程。 IN类流程：Node作为接收方接收外部操作请求后所对应执行的相关流程。
 * OUT类流程：Node作为发送方发送对外操作（或回应）所执行的相关流程。 IN类与OUT类都可以是异步或者同步的。
 * </p>
 * 
 * @author liubingj
 */
public class SimpleFlowEngine {

	private static Logger LOG = LoggerFactory.getLogger(SimpleFlowEngine.class);

	private static SimpleFlowEngine _instance;

	private Map<String, FlowDefinition> flowDefs = new HashMap<String, FlowDefinition>(16);

	private ThreadPoolExecutor pool;

	private SimpleFlowEngine() {
	}

	public static SimpleFlowEngine getInstance() {
		synchronized (SimpleFlowEngine.class) {
			if (_instance == null) {
				_instance = new SimpleFlowEngine();
				_instance.init();
			}
		}
		return _instance;
	}

	private void init() {
		try {
			final File file = FileUtil.getInstance().getFile("flow/flows.xml", Constants.Env.BASE_CONF);
			StringBuffer buffer = new StringBuffer(FlowsDefinition.class.getPackage().getName());
			buffer.append(":");
			buffer.append("com.dc.appengine.node.flow.model");
			final FlowsDefinition flowsDefinition = ConfigReader.getInstance().parseXmlToModel(file, buffer.toString(),
					FlowsDefinition.class.getClassLoader());
			flowDefs.putAll(flowsDefinition.toMap());

			pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(flowsDefinition.getPoolSize(),
					new ThreadFactory() {
						private int count = 0;
						ThreadGroup group = new ThreadGroup("SimpleFlowEngine-Thread");

						public Thread newThread(Runnable r) {
							return new Thread(group, r, group.getName().concat("-") + count++);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param string
	 */
	public void exec(String key, final Context t) {
		if (!flowDefs.containsKey(key)) {
			throw new NoSuchFlowDefException("No such flow named " + key);
		}
		final FlowDefinition flow = flowDefs.get(key);
		long spandtime = System.currentTimeMillis();
		LOG.debug("POOL SIZE:" + pool.getPoolSize() + "active size:" + pool.getActiveCount()+"time:"+spandtime);
		Future<Context> future = pool.submit(new Callable<Context>() {
			public Context call() throws Exception {
				try {
					flow.createProcessor().process(t);
				} catch (Exception e) {
					LOG.error(e.getMessage(), e);
					throw e;
				}
				return t;
			}
		});
		if (flow.isSynchronous()) {
			int i = 0;
			while (!future.isDone()) {
				try {
					i++;
					if (i < 4) {
						Thread.sleep(i * 10);
					} else {
						Thread.sleep(i * 50);
					}
					LOG.debug("not done");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				LOG.debug(future.get()+"");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			LOG.debug("work is down begintime:"+spandtime +"end time"+System.currentTimeMillis());
//			pool.shutdown();
		}
	}

}
