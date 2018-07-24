package com.dc.appengine.node.collector.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.node.collector.Collectable;
import com.dc.appengine.node.command.analyser.impl.DiskAnalyic;
import com.dc.appengine.node.command.analyser.impl.NodeCpuCoreAnalyser;
import com.dc.appengine.node.command.analyser.impl.NodeOsAnalyser;
import com.dc.appengine.node.command.analyser.impl.NodeOsTypeAnalyser;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.plugins.command.CommandResult;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class OsInfoCollector implements Collectable {
	private static Logger log = Logger.getLogger(OsInfoCollector.class);
	private NodeOsStatCache node = NodeOsStatCache.getInstance();

	@Override
	public <T> T collect() throws Exception {

//		boolean isDocker = NodeProperties
//				.isDocker();
//		if (isDocker) {
//			// node_os.sh内容
//			// docker info
//			Map<String, Object> os = CommandResult.getResult(
//					new NodeOsAnalyser(), CommandWaitExecutor.class, "node_os",
//					false);
//			if (os.get("os") != null) {
//				node.setOsType(os.get("os").toString());
//			}
//			if (os.get("cpu") != null) {
//				node.setCpuCore(Integer.valueOf(os.get("cpu").toString()));
//			}
//			if (os.get("docker") != null) {
//				node.setDockerVersion(os.get("docker").toString());
//			}
//			setHostDiskMem();
//		}
//		if (!isDocker || node.getOsType() == null) {
//			CommandResult.getResult(new NodeOsTypeAnalyser(),
//					CommandWaitExecutor.class, "cat /proc/version", true);
//			CommandResult.getResult(new NodeCpuCoreAnalyser(),
//					CommandWaitExecutor.class, "cat /proc/cpuinfo", true);
//			if (node.getDockerVersion() == null
//					|| "".equals(node.getDockerVersion())) {
//				node.setDockerVersion("NO docker");
//			}
//			// disk
//			List<Map<String, String>> list = CommandResult.getResult(
//					new DiskAnalyic(), CommandWaitExecutor.class,
//					"diskcollect", false);
//			double diskAll = 0;
//			for (Map<String, String> unit : list) {
//				String allM = unit.get("all");
//				if (allM == null) {
//					continue;
//				}
//				int m = Integer.valueOf(allM);
//				diskAll += m;
//			}
//			node.setDisk(diskAll);
//			setHostDiskMem();
//		}
//		log.debug("Node os: " + node.getOsType());
//		log.debug("cpu cores: " + node.getCpuCore());
//		log.debug("docker version: " + node.getDockerVersion());
//		log.debug("disk: " + node.getDisk());
//		log.debug("mem: " + node.getMemory());
		return null;
	}

	private void setHostDiskMem() {
		String diskAndMemResult = CommandResult.getResult(
				new DefaultAnalyser(), CommandWaitExecutor.class,
				"hostDiskMem", false);
		double disk = Double.parseDouble(diskAndMemResult.split(",")[0]);
		double mem = Double.parseDouble(diskAndMemResult.split(",")[1]);
		node.setDisk(disk);
		node.setMemory(mem);
	}
}
