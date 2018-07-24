package com.dc.appengine.node.docker.cad;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.node.cache.StateCache;
import com.dc.appengine.node.collector.impl.NodeStatCollector;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.docker.DockerStatus;
import com.dc.appengine.node.utils.UnitChangeUtil;
import com.dc.appengine.plugins.command.Analytic;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.analyser.ListAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class CadVisorClient {
	private static CadVisorClient instance = null;
	private String address = "";
	private List<String> defaultNetDevice = new ArrayList<String>();
	private String memCommand = null;
	private String deviceCommand = null;
	private static final Logger log = LoggerFactory
			.getLogger(CadVisorClient.class);

	private String netUnit = "Kb";
	private String memUnit = "Mb";
	private String diskUnit = "Mb";

	private CadVisorClient() {
		// 初始化参数
//		this.address = NodeProperties
//				.getCadvisorUrl();
		this.memCommand = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("memcollect"), false);

		// 查找默认网卡
		this.deviceCommand = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("getNetDevice"), false);
		Analytic<List<String>> anytic = new ListAnalyser();
		CommandWaitExecutor netExec = new CommandWaitExecutor(anytic);
		netExec.exec(deviceCommand);
		defaultNetDevice = anytic.getResult();
		log.debug("==the physics network devices are :" + defaultNetDevice);
	}

	public static CadVisorClient getInstance() {
		synchronized (CadVisorClient.class) {
			if (instance == null) {
				instance = new CadVisorClient();
			}
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public String getHostBaseInfo() {
		String unit = "Mb";
		int num_cores = 0;
		double diskAll = 0;
		double memAll = 0;
		Request request = new Request("http://" + address + "/api/v1.0/machine");
		String result = WebClient.get(request);
		if (result == null) {
			log.debug("no content,try to check container [ cadvisor ]");
			// CheckCadvisor.check();
			return null;
		}
		Map<String, Object> infoMap = new HashMap<String, Object>();
		if (request.getStatus() == 200) {
			infoMap = JSON.parseObject(result);
		} else {
			System.out.println(result);
			return null;
		}
		num_cores = (Integer) infoMap.get("num_cores");
		long memB = getLong(infoMap.get("memory_capacity"));
		memAll = UnitChangeUtil.changeUnit(memB, unit);
		Map<String, Map<String, Object>> diskMap = (Map<String, Map<String, Object>>) infoMap
				.get("disk_map");
		long diskB = 0;
		for (String key : diskMap.keySet()) {
			Map<String, Object> disktemp = diskMap.get(key);
			String name = disktemp.get("name").toString();
			if (name.contains("sd")) {
				diskB += getLong(disktemp.get("size"));
			}
		}
		diskAll = UnitChangeUtil.changeUnit(diskB, unit);

		// String osName=System.getProperty("os.name");

		NodeOsStatCache node = NodeOsStatCache.getInstance();
		node.setDisk(diskAll);
		node.setMemory(memAll);
		if (node.getOsType() == null) {
			node.setOsType(System.getProperty("os.version"));
		}
		if (node.getCpuCore() == 0) {
			node.setCpuCore(num_cores);
		}
		// System.out.println(osName+" "+osVersion);
		// System.out.println("cpunum:"+num_cores);
		// System.out.println("mem :"+memAll+" "+unit);
		// System.out.println("disk :"+diskAll +" "+unit);
		return "ok";
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getHostInfo() {

		DecimalFormat model = new DecimalFormat("0.00");
		Map<String, Object> vmstat = new HashMap<String, Object>();
		double cpu_usage = 0;
		Request request = new Request("http://" + address
				+ "/api/v1.0/containers/");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num_stats", 2);
		map.put("num_samples", 0);
		request.setData(JSON.toJSONString(map));
		String result = WebClient.postJSON(request);
		if (result == null) {
			log.debug("no content,try to check container [ cadvisor ]");
			CheckCadvisor.check();
			// 设置结果集
			return vmstat;
		}

		Map<String, Object> machineInfo = JSON.parseObject(result, Map.class);
		// =========计算cpu使用率===============//
		List<Map<String, Object>> status = (List<Map<String, Object>>) machineInfo
				.get("stats");
		if (status == null || status.size() < 2) {
			log.debug("data length not support collect!");
			return vmstat;
		}
		Map<String, Object> state1 = status.get(1);
		Map<String, Object> state0 = status.get(0);
		double timePeriod = 2 * Math.pow(10, 9);
		try {
			if (state0.get("timestamp") == null
					|| state1.get("timestamp") == null) {
				timePeriod = 2 * Math.pow(10, 9);
				;
			} else {
				double time0 = getTimeStamp(state0.get("timestamp").toString());
				double time1 = getTimeStamp(state1.get("timestamp").toString());
				timePeriod = Math.abs(time1 - time0);
			}
			Map<String, Map<String, Object>> cpu0 = (Map<String, Map<String, Object>>) state0
					.get("cpu");
			Map<String, Map<String, Object>> cpu1 = (Map<String, Map<String, Object>>) state1
					.get("cpu");
			if (NodeStatCollector.HostCpuCore == 0) {
				int coreNum = 1;
				List<Object> per_cpu = (List<Object>) cpu0.get("usage").get(
						"per_cpu_usage");
				if (per_cpu != null && per_cpu.size() != 0) {
					coreNum = per_cpu.size();
				}
				NodeStatCollector.HostCpuCore = coreNum;
			}

			// timeUsedAll=/cpu/usage/total;
			long total0 = getLong(cpu0.get("usage").get("total"));
			long total1 = getLong(cpu1.get("usage").get("total"));
			double used = (double) (total1 - total0);
			cpu_usage = used / timePeriod / NodeStatCollector.HostCpuCore;
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		// =========计算内存使用率=============//
		long memLimits = 0;
		long memUsage = 0;
		long free = 0;
		long cache = 0;
		double memoryUseRate = 0.0;

		// 如果是centos 采用脚本采集
		NodeOsStatCache node = NodeOsStatCache.getInstance();
		String osName = node.getOsType().toLowerCase();
		if (osName != null && osName.contains("centos")) {
			/**
			 * 
			 cache cache,memUnit free free,memUnit total memLimits,memUnit
			 * used memUsage,memUnit memusage memoryUseRate;
			 * */
			// total used free cache memusage=used/total

			DefaultAnalyser defaultAnalytic = new DefaultAnalyser();
			CommandWaitExecutor executor = new CommandWaitExecutor(
					defaultAnalytic);
			executor.exec(memCommand);
			String memResult = defaultAnalytic.getResult();
			String[] memArray = memResult.split("\\s+");
			if (memArray.length == 4) {
				// log.debug("Centos:shell get the memInfo,currect analytic result!");
				memLimits = Long.parseLong(memArray[0]) * 1024 * 1024;
				memUsage = Long.parseLong(memArray[1]) * 1024 * 1024;
				free = Long.parseLong(memArray[2]) * 1024 * 1024;
				cache = Long.parseLong(memArray[3]) * 1024 * 1024;
			} else {
				log.debug("Centos:shell get the memInfo,uncurrect analytic result,use cadvisor result!");
				Map<String, Map<String, Object>> spec = (Map<String, Map<String, Object>>) machineInfo
						.get("spec");
				memLimits = getLong(spec.get("memory").get("limit"));
				Map<String, Object> memory = (Map<String, Object>) state1
						.get("memory");
				memUsage = getLong(memory.get("usage"));
				free = memLimits - memUsage;
				cache = getLong(memory.get("cache"));
			}
		} else {
			Map<String, Map<String, Object>> spec = (Map<String, Map<String, Object>>) machineInfo
					.get("spec");
			memLimits = getLong(spec.get("memory").get("limit"));
			Map<String, Object> memory = (Map<String, Object>) state1
					.get("memory");
			memUsage = getLong(memory.get("usage"));
			free = memLimits - memUsage;
			cache = getLong(memory.get("cache"));
		}
		memoryUseRate = (double) memUsage / (double) memLimits; // mem
		// ==========网络IO=================
		long inputAll0 = 0;
		long outputAll0 = 0;
		long inputAll1 = 0;
		long outputAll1 = 0;

		Map<String, Object> network0 = (Map<String, Object>) state0
				.get("network");
		Map<String, Object> network1 = (Map<String, Object>) state1
				.get("network");
		List<Map<String, Object>> interfaces0 = (List<Map<String, Object>>) network0
				.get("interfaces");
		List<Map<String, Object>> interfaces1 = (List<Map<String, Object>>) network1
				.get("interfaces");

		for (Map<String, Object> ifc : interfaces0) {
			if (defaultNetDevice.contains(ifc.get("name").toString())) {
				inputAll0 += getLong(ifc.get("rx_bytes"));
				outputAll0 += getLong(ifc.get("tx_bytes"));
			}
		}
		for (Map<String, Object> ifc : interfaces1) {
			if (defaultNetDevice.contains(ifc.get("name").toString())) {
				inputAll1 += getLong(ifc.get("rx_bytes"));
				outputAll1 += getLong(ifc.get("tx_bytes"));
			}

		}
		long netInBytes = inputAll1 - inputAll0;
		long netOutBytes = outputAll1 - outputAll0;

		double netI = UnitChangeUtil.changeUnit(
				(double) netInBytes * Math.pow(10, 9) / timePeriod, netUnit);
		double netO = UnitChangeUtil.changeUnit(
				(double) netOutBytes * Math.pow(10, 9) / timePeriod, netUnit);

		// ==========文件IO====================
		Map<String, Object> disk0 = (Map<String, Object>) state0.get("diskio");
		List<Map<String, Object>> ioservice0 = (List<Map<String, Object>>) disk0
				.get("io_service_bytes");
		long read0 = 0;
		long write0 = 0;
		for (Map<String, Object> io0 : ioservice0) {
			Map<String, Object> ioMap = (Map<String, Object>) io0.get("stats");
			read0 += getLong(ioMap.get("Read"));
			write0 += getLong(ioMap.get("Write"));
		}

		Map<String, Object> disk1 = (Map<String, Object>) state1.get("diskio");
		List<Map<String, Object>> ioservice1 = (List<Map<String, Object>>) disk1
				.get("io_service_bytes");
		long read1 = 0;
		long write1 = 0;
		for (Map<String, Object> io1 : ioservice1) {
			Map<String, Object> ioMap = (Map<String, Object>) io1.get("stats");
			read1 += getLong(ioMap.get("Read"));
			write1 += getLong(ioMap.get("Write"));
		}
		long read_period = read1 - read0;
		long write_period = write1 - write0;

		double diskI = UnitChangeUtil.changeUnit(
				(double) write_period * Math.pow(10, 9) / timePeriod, diskUnit);
		double diskO = UnitChangeUtil.changeUnit(
				(double) read_period * Math.pow(10, 9) / timePeriod, diskUnit);

		// ===================汇总===================================================================//
		// System.out.println("cpu usage:"+cpu_usage +"%");
		// System.out.println("memAll:"+changeUnit(memLimits,memUnit)
		// +" "+memUnit);
		// System.out.println("memUsage:"+changeUnit(memUsage,memUnit)+" "+memUnit);
		// System.out.println("memoryUseRate:"+memoryUseRate*100+"%");
		// System.out.println("cache:"+changeUnit(cache,memUnit) +" "+memUnit);
		// System.out.println("netI/O:"+netI+"/"+netO+netUnit+"/s");
		// System.out.println("diskI/O:"+diskI+"/"+diskO+diskUnit+"/s");
		// ===================抽取结果============================
		vmstat.put("id", Double.valueOf(model.format(cpu_usage)) * 100);
		vmstat.put("cache", UnitChangeUtil.changeUnit(cache, memUnit));
		vmstat.put("free", UnitChangeUtil.changeUnit(free, memUnit));
		vmstat.put("total", UnitChangeUtil.changeUnit(memLimits, memUnit));
		vmstat.put("used", UnitChangeUtil.changeUnit(memUsage, memUnit));
		vmstat.put("memusage", model.format(memoryUseRate));
		vmstat.put("netIO", netI + "/" + netO);
		vmstat.put("diskIO", diskI + "/" + diskO);
		return vmstat;
	}

	@SuppressWarnings("unchecked")
	public DockerStatus getContainerInfo(String containerId, String netModel) {
		/**
		 * usage double cpu使用率 memLimits long 内存总量（上限） memoryUsage long 内存使用量
		 * memory
		 * */
		DockerStatus dstatus = new DockerStatus();
		double cpu_usage = 0;
		DecimalFormat model = new DecimalFormat("0.00");
		Request request = new Request("http://" + address
				+ "/api/v1.0/containers/docker/" + containerId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("num_stats", 2);
		map.put("num_samples", 0);
		request.setData(JSON.toJSONString(map));
		String result = WebClient.postJSON(request);
		if (result == null) {
			log.debug("no content,try to check container [ cadvisor ]");
			CheckCadvisor.check();
			return dstatus;
		}
		String netUnit = "Kb";
		String memUnit = "Mb";
		String diskUnit = "Mb";

		Map<String, Object> containerInfo = JSON.parseObject(result);
		// =========计算cpu使用率===============//
		List<Map<String, Object>> status = (List<Map<String, Object>>) containerInfo
				.get("stats");
		if (status == null || status.size() < 2) {
			return dstatus;
		}
		Map<String, Object> state0 = status.get(0);
		Map<String, Object> state1 = status.get(1);
		double timePeriod = 2 * Math.pow(10, 9);
		try {
			if (state0.get("timestamp") == null
					|| state1.get("timestamp") == null) {
				timePeriod = 2 * Math.pow(10, 9);
			} else {
				double time0 = getTimeStamp(state0.get("timestamp").toString());
				double time1 = getTimeStamp(state1.get("timestamp").toString());
				timePeriod = Math.abs(time1 - time0);
			}
			Map<String, Map<String, Object>> cpu0 = (Map<String, Map<String, Object>>) state0
					.get("cpu");
			Map<String, Map<String, Object>> cpu1 = (Map<String, Map<String, Object>>) state1
					.get("cpu");
			// timeUsedAll=/cpu/usage/total;
			long total0 = getLong(cpu0.get("usage").get("total"));
			long total1 = getLong(cpu1.get("usage").get("total"));
			int cpuCore = 1;
			List<Object> per_cpu = (List<Object>) cpu0.get("usage").get(
					"per_cpu_usage");
			if (per_cpu != null && per_cpu.size() != 0) {
				cpuCore = per_cpu.size();
			}
			double used = (double) (total1 - total0);
			cpu_usage = Double.valueOf(model.format(used / timePeriod * 100
					/ cpuCore));

		} catch (ParseException e) {
			log.error(e.getMessage());
		}

		// =========计算内存使用率=============//
		Map<String, Map<String, Object>> spec = (Map<String, Map<String, Object>>) containerInfo
				.get("spec");
		long memLimits = getLong(spec.get("memory").get("limit"));
		Map<String, Object> memory = (Map<String, Object>) state1.get("memory");
		long memUsage = getLong(memory.get("usage"));
		double memoryUseRate = Double.valueOf(model.format((double) memUsage
				/ (double) memLimits)); // mem
		// ==========网络IO=================

		if ("host".equals(netModel)) {
			// 拷贝宿主机网卡网络IO
			Object obj = StateCache.getInstance().get(
					StateCache.Attributes.NODE);
			if (obj != null && obj instanceof Map) {
				Map<String, String> nodemessage = (Map<String, String>) obj;
				String n = nodemessage.get("netIO");
				dstatus.setNetIO(n);
			}
		} else {
			double netI = 0.0;
			double netO = 0.0;
			long inputAll0 = 0;
			long outputAll0 = 0;
			long inputAll1 = 0;
			long outputAll1 = 0;
			Map<String, Object> network0 = (Map<String, Object>) state0
					.get("network");
			Map<String, Object> network1 = (Map<String, Object>) state1
					.get("network");
			List<Map<String, Object>> interfaces0 = (List<Map<String, Object>>) network0
					.get("interfaces");
			List<Map<String, Object>> interfaces1 = (List<Map<String, Object>>) network1
					.get("interfaces");

			if (interfaces0 == null || interfaces1 == null) {
				// System.out.println("网络设备不存在");
			} else {
				for (Map<String, Object> ifc : interfaces0) {
					inputAll0 += getLong(ifc.get("rx_bytes"));
					outputAll0 += getLong(ifc.get("tx_bytes"));
				}
				for (Map<String, Object> ifc : interfaces1) {
					inputAll1 += getLong(ifc.get("rx_bytes"));
					outputAll1 += getLong(ifc.get("tx_bytes"));
				}
				long netInBytes = inputAll1 - inputAll0;
				long netOutBytes = outputAll1 - outputAll0;
				netI = UnitChangeUtil.changeUnit(
						(double) netInBytes * Math.pow(10, 9) / timePeriod,
						netUnit);
				netO = UnitChangeUtil.changeUnit(
						(double) netOutBytes * Math.pow(10, 9) / timePeriod,
						netUnit);
			}
			dstatus.setNetIO(netI + "/" + netO);
		}

		// ==========文件IO====================
		Map<String, Object> disk0 = (Map<String, Object>) state0.get("diskio");
		List<Map<String, Object>> ioservice0 = (List<Map<String, Object>>) disk0
				.get("io_service_bytes");
		Map<String, Object> disk1 = (Map<String, Object>) state1.get("diskio");
		List<Map<String, Object>> ioservice1 = (List<Map<String, Object>>) disk1
				.get("io_service_bytes");
		long read0 = 0;
		long write0 = 0;
		long read1 = 0;
		long write1 = 0;
		if (ioservice0 != null && ioservice1 != null) {
			for (Map<String, Object> io0 : ioservice0) {
				Map<String, Object> ioMap = (Map<String, Object>) io0
						.get("stats");
				read0 += getLong(ioMap.get("Read"));
				write0 += getLong(ioMap.get("Write"));
			}

			for (Map<String, Object> io1 : ioservice1) {
				Map<String, Object> ioMap = (Map<String, Object>) io1
						.get("stats");
				read1 += getLong(ioMap.get("Read"));
				write1 += getLong(ioMap.get("Write"));
			}
		}
		long read_period = read1 - read0;
		long write_period = write1 - write0;

		double diskI = UnitChangeUtil.changeUnit(
				(double) write_period * Math.pow(10, 9) / timePeriod, diskUnit);
		double diskO = UnitChangeUtil.changeUnit(
				(double) read_period * Math.pow(10, 9) / timePeriod, diskUnit);

		// ===================汇总===================================================================//
		// System.out.println("cpu usage:"+cpu_usage +"%");
		// System.out.println("memAll:"+changeUnit(memLimits,memUnit)
		// +" "+memUnit);
		// System.out.println("memUsage:"+changeUnit(memUsage,memUnit)+" "+memUnit);
		// System.out.println("memoryUseRate:"+memoryUseRate*100+"%");
		// System.out.println("netI/O:"+netI+"/"+netO+netUnit+"/s");
		// System.out.println("diskI/O:"+diskI+"/"+diskO+diskUnit+"/s");
		// =============抽取信息================
		dstatus.setCpu(model.format(cpu_usage));
		dstatus.setMem(model.format(memoryUseRate * 100));
		dstatus.setMemLimits(model.format(UnitChangeUtil.changeUnit(memLimits,
				memUnit)));
		dstatus.setMemUsage(model.format(UnitChangeUtil.changeUnit(memUsage,
				memUnit)));
		dstatus.setDiskIO(diskI + "/" + diskO);

		return dstatus;
	}

	public double getTimeStamp(String timeStr) throws ParseException {
		String seconds = timeStr.replace("Z", "").split(":")[2];
		double ns = Double.valueOf(seconds) * Math.pow(10, 9);
		return ns;
	}

	public long getLong(Object obj) {
		long longNum = 0l;
		if (obj instanceof Integer) {
			longNum = Long.valueOf(obj.toString());
		} else {
			longNum = (Long) obj;
		}
		return longNum;

	}

}
