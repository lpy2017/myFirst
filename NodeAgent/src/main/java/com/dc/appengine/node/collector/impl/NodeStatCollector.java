/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.collector.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.NodeConstant;
import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.node.cache.StateCache;
import com.dc.appengine.node.collector.Collectable;
import com.dc.appengine.node.command.analyser.impl.DiskIOAnalyser;
import com.dc.appengine.node.command.analyser.impl.NetIOAnalyser;
import com.dc.appengine.node.command.analyser.impl.TopCpuAnalyser;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.docker.cad.CadVisorClient;
import com.dc.appengine.node.flow.DefaultNodeFlowContext;
import com.dc.appengine.node.flow.SimpleFlowEngine;
import com.dc.appengine.node.message.InnerMessage;
import com.dc.appengine.node.message.topic.TopicInit;
import com.dc.appengine.node.redis.JedisClientImpl;
import com.dc.appengine.node.utils.UnitChangeUtil;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.CommandResult;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.analyser.ListAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

/**
 * NodeStatCollector.java
 * 
 * @author liubingj
 */
public class NodeStatCollector implements Collectable {
	private static JedisClientImpl jedis = new JedisClientImpl();
	private static Logger log = LoggerFactory
			.getLogger(NodeStatCollector.class);
	private static long lastRbytes;
	private static long lastTbytes;
	private static long lastTime;
	public static int HostCpuCore;
	private List<String> defaultNetDevice = new ArrayList<String>();
	private String memUnit = "Mb";

	public NodeStatCollector() {
		defaultNetDevice = CommandResult.getResult(new ListAnalyser(),
				CommandWaitExecutor.class, "getNetDevice", false);
		log.debug("==the physics network devices are :" + defaultNetDevice);
	}

	public <T> T collect() {
		if (NodeEnv.getInstance().getNodeEnvDefinition() != null
				&& NodeEnv.getInstance().getNodeEnvDefinition().getNodeName() != null) {
			Map<String, Object> vmstate = new HashMap<String, Object>(20);
			vmstate.put("nodeName", NodeEnv.getInstance()
					.getNodeEnvDefinition().getNodeip());
			try {
				Map<String, Object> vm1 = getHostInfo();
				if (vm1 != null) {
					vmstate.putAll(vm1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			StateCache.getInstance().put(StateCache.Attributes.NODE, vmstate);
			saveToRedis();
			// this.sendToLB();
		}
		return null;
	}

	private void saveToRedis() {
		String key = NodeConstant.NODE_STATUS + "_"
				+ NodeEnv.getInstance().getNodeEnvDefinition().getNodeName();
		Map<String, Object> map = StateCache.getInstance().cloneContent();
		// map.put("timestamp", new Date().getTime());
		String value = JSON.toJSONString(map);
		int expire = NodeProperties
				.getRedisDataTimeOut();
		log.debug("save key: {} value {} to redis ", key, value);
		jedis.set(key, value, expire);

	}

	@SuppressWarnings("unused")
	private void sendToLB() {
		try {
			Map<String, Object> content = new HashMap<>();
			InnerMessage<Map<String, Object>> msg = new InnerMessage<Map<String, Object>>(
					NodeConstant.HEART_BEAT, content);
			byte[] contentBytes = JSON.toJSONString(msg).getBytes();
			final Context context = new DefaultNodeFlowContext(contentBytes);
			SimpleFlowEngine.getInstance().exec("sync_request", context);
			Properties header = new Properties();
			final byte[] response = context.getPayload();
			if (TopicInit.client == null) {
				log.info("TopicInit.client == null");
				return;
			}
			log.debug("send to LB: {}", new String(response));
			TopicInit.client.publish("lb", response, header);
		} catch (Exception e) {
			log.error("", e);
		}
	}

	private Map<String, Object> getHostInfo() {
//		boolean isDocker = NodeProperties
//				.isDocker();
//		if (!isDocker) {
			return getOsStat();
//		} else {
//			return CadVisorClient.getInstance().getHostInfo();
//		}
	}

	private Map<String, Object> getOsStat() {
		if (HostCpuCore == 0) {
			NodeOsStatCache node = NodeOsStatCache.getInstance();
			HostCpuCore = node.getCpuCore();
		}

		Map<String, Object> vmstat = new HashMap<String, Object>();

		DecimalFormat model = new DecimalFormat("0.00");
		// cpu id cpu使用率 top
		String cpuUsage = CommandResult.getResult(new TopCpuAnalyser(),
				CommandWaitExecutor.class, "cpustats", false);
		vmstat.put("id", cpuUsage);

		// 内存 cache缓存 free可用的 total内存总量 used使用量 memusage内存使用率 free -m
		/**
		 * 
		 cache cache,memUnit free free,memUnit total memLimits,memUnit used
		 * memUsage,memUnit memusage memoryUseRate;
		 * */
		// total used free cache memusage=used/total
		long memLimits = 0;
		long memUsage = 0;
		long free = 0;
		long cache = 0;
		double memoryUseRate = 0.0;
		String memResult = CommandResult.getResult(new DefaultAnalyser(),
				CommandWaitExecutor.class, "memcollect", false);
		String[] memArray = memResult.split("\\s+");
		// log.debug("Centos:shell get the memInfo,currect analytic result!");

		memLimits = Long.parseLong(memArray[0]) * 1024 * 1024;
		memUsage = Long.parseLong(memArray[1]) * 1024 * 1024;
		memoryUseRate = ((double) memUsage) / memLimits;
		free = Long.parseLong(memArray[2]) * 1024 * 1024;
		cache = Long.parseLong(memArray[3]) * 1024 * 1024;
		vmstat.put("cache", UnitChangeUtil.changeUnit(cache, memUnit));
		vmstat.put("free", UnitChangeUtil.changeUnit(free, memUnit));
		vmstat.put("total", UnitChangeUtil.changeUnit(memLimits, memUnit));
		vmstat.put("used", UnitChangeUtil.changeUnit(memUsage, memUnit));
		vmstat.put("memusage", model.format(memoryUseRate));
		// 网络io

		NetIOAnalyser netIOAnalyser = new NetIOAnalyser();
		CommandWaitExecutor executor_NETIO = new CommandWaitExecutor(
				netIOAnalyser);
		long RBNow = 0;
		long TBNow = 0;
		long timeNow = 0;
		String command_base = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("netio"), false);
		for (String netDevice : defaultNetDevice) {
			String cNetD = command_base + " " + netDevice;
			executor_NETIO.exec(cNetD);
			RBNow += netIOAnalyser.getRB();
			TBNow += netIOAnalyser.getTB();
			timeNow = netIOAnalyser.getTimeNow();
		}
		if (lastRbytes == 0 && lastTbytes == 0) {
			vmstat.put("netIO", "0.0/0.0");
		} else {
			long rx = RBNow - lastRbytes;
			long tx = TBNow - lastTbytes;
			double timeSeconds = timeNow - lastTime;
			double netI = (double) rx / 1024 / (timeSeconds / 1000);
			BigDecimal bd = new BigDecimal(netI);
			netI = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			double netO = (double) tx / 1024 / (timeSeconds / 1000);
			BigDecimal bd2 = new BigDecimal(netO);
			netO = bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

			vmstat.put("netIO", netI + "/" + netO);
		}
		lastRbytes = RBNow;
		lastTbytes = TBNow;
		lastTime = timeNow;
		// 磁盘IO
		String commandDisk = "iostat -d -k";
		DiskIOAnalyser diskAnyic = new DiskIOAnalyser();
		CommandWaitExecutor executor_DISKIO = new CommandWaitExecutor(diskAnyic);
		executor_DISKIO.exec(commandDisk);
		vmstat.put("diskIO", diskAnyic.getDiskW() + "/" + diskAnyic.getDiskR());
		return vmstat;

	}
}
