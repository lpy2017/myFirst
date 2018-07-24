package com.dc.appengine.appmaster.status.running.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dc.appengine.appmaster.entity.Cpu;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.entity.StrategyItem;
import com.dc.appengine.appmaster.message.bean.MVNode;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.INodeResourceService;
import com.dc.appengine.appmaster.status.running.checker.INodeResourceChecker;
import com.dc.appengine.appmaster.status.running.checker.NodeResourceCheckerBuilder;
import com.dc.appengine.appmaster.status.running.checker.StrategyItemsUtils;
import com.dc.appengine.appmaster.utils.ConfigHelper;
import static java.lang.Math.min;
public class InDBNodeSelector {
	
	@Autowired
	@Qualifier("applicationService")
	IApplicationService applicationService;
	
	@Autowired
	@Qualifier("nodeResourceService")
	INodeResourceService nodeResourceService;
	
	private static final INodeResourceChecker<?> checker = NodeResourceCheckerBuilder
			.getInstance().getChecker();
	
	private static InDBNodeSelector selector = null;

	public static InDBNodeSelector getInstance() {
		if (selector == null) {
			synchronized (InDBNodeSelector.class) {
				if (selector == null) {
					selector = new InDBNodeSelector();
				}
			}
		}
		return selector;
	}

	public List<MVNode> selectNode(List<StrategyItem> items, String appId, String runtimeCount) {
		return selectNode(items, appId, false, runtimeCount);
	}

	public List<MVNode> selectNode(List<StrategyItem> items, String appId, boolean cached, String runtimeCount) {
		Map<String, String> strategyMap = StrategyItemsUtils.ItemsToMap(items);
		// 初始化部署策略值
		int memory = Integer.parseInt(ConfigHelper.getValue("app.xmx")), instanceNum = 1, cpuNum = 0, disk = 0;
		boolean sameHost = true, alone = false, isolate = false, sharedCpu = true;
		String nodeType = "", nodeGroup = "";// node类型和是否是集群，目前不用
		if (strategyMap.containsKey(Item.CODE_XMX)) {
			memory = Integer.parseInt(strategyMap.get(Item.CODE_XMX));
		}
		if (strategyMap.containsKey(Item.CODE_DISK)) {
			disk = Integer.parseInt(strategyMap.get(Item.CODE_DISK));
		}
		if (strategyMap.containsKey(Item.CODE_INSTANCE_NUMBER) && (runtimeCount == null || "".equals(runtimeCount))) {
			// 取部署策略中的实例数
			instanceNum = Integer.parseInt(strategyMap.get(Item.CODE_INSTANCE_NUMBER));
		} else if (runtimeCount != null && !"".equals(runtimeCount)) {
			// 取运行时计算获得的实例数
			instanceNum = Integer.valueOf(runtimeCount);
		}
		if (strategyMap.containsKey(Item.CODE_SAMEHOST)) {
			sameHost = Boolean.parseBoolean(strategyMap.get(Item.CODE_SAMEHOST));
		}
		if (strategyMap.containsKey(Item.CODE_ALONE)) {
			alone = Boolean.parseBoolean(strategyMap.get(Item.CODE_ALONE));
		}
		if (strategyMap.containsKey(Item.CODE_CPU_NUMBER)) {
			cpuNum = Integer.parseInt(strategyMap.get(Item.CODE_CPU_NUMBER));
		}
		if (strategyMap.containsKey(Item.CODE_STRONG_ISOLATA)) {
			isolate = Boolean.parseBoolean(strategyMap.get(Item.CODE_STRONG_ISOLATA));
		}
		if (strategyMap.containsKey(Item.CODE_SHARED_CPU)) {
			sharedCpu = Boolean.parseBoolean(strategyMap.get(Item.CODE_SHARED_CPU));
		}
		// 强隔离必须至少指定一个cpu
		if (isolate && cpuNum == 0) {
			cpuNum = 1;
			strategyMap.put(Item.CODE_CPU_NUMBER, "1");
		}
		List<MVNode> ns = null;
		ns = commonSelect(memory, instanceNum, disk, sameHost, alone, nodeType, cpuNum, nodeGroup, isolate, sharedCpu,
				strategyMap, appId);
		if (ns != null) {
			if (!cached) {
				updateNode(ns, memory, alone, cpuNum, nodeType, nodeGroup, isolate, disk);
			} else {
				int num = ns.size();
				memory = memory / num;
				updateNode(ns, memory, alone, cpuNum, nodeType, nodeGroup, isolate, disk);
			}
		}
		return ns;
	}

	private void updateNode(List<MVNode> list, int memory, boolean alone, int cpuNum, String nodeType, String nodeGroup,
			boolean isolate, int disk) {
		if (list != null) {
			Map<String, MVNode> map = new HashMap<String, MVNode>();
			for (MVNode node : list) {
				if (!map.containsKey(node.getName())) {
					MVNode mv = new MVNode();
					BeanUtils.copyProperties(node, mv);
					mv.setMemory(memory);
					mv.setDisk(disk);
					mv.setInstanceNum(1);
					mv.setAlone(alone);
					byte b = (byte) (mv.getCpus() != null ? 2 : 1);
					mv.setIsolateState(b);
					if (mv.getCpus() != null) {
						boolean[] cpus = new boolean[mv.getCpus().length];
						System.arraycopy(mv.getCpus(), 0, cpus, 0, cpus.length);
						mv.setCpus(cpus);
					}
					map.put(node.getName(), mv);
				} else {
					MVNode mv = map.get(node.getName());
					mv.setMemory(mv.getMemory() + memory);
					mv.setDisk(mv.getDisk() + disk);
					mv.setInstanceNum(mv.getInstanceNum() + 1);
					mv.setAlone(alone);
					mv.setCpus(useCpus(mv.getCpus(), node.getCpus()));
				}
			}
			Set<String> keyset = map.keySet();
			Iterator<String> iterator = keyset.iterator();
			while (iterator.hasNext()) {
				String name = iterator.next();
				MVNode node = map.get(name);
				// -1表示资源不足，0表示数据过期，需要重新计算，大于0表示扣除成功
				int rows = nodeResourceService.updateNode(node);
				if (rows == 0 || rows == -1) {
					throw new RuntimeException("资源扣除失败");
				}
			}
		}
	}

	private boolean[] useCpus(boolean[] cpus, boolean[] usedCpus) {
		if (cpus != null && usedCpus != null) {
			for (int i = 0; i < cpus.length; i++) {
				if (usedCpus[i]) {
					cpus[i] = true;
				}
			}
		}
		return cpus;
	}

	private List<MVNode> commonSelect(int memory, int instanceNum, int disk, boolean sameHost, boolean alone,
			String nodeType, int cpuNum, String nodeGroup, boolean isolata, boolean sharedCpu,
			Map<String, String> strategyMap, String appId) {
		// 根据cluserId和nodeIds选本地node
		String clusterId = strategyMap.get(Item.CODE_CLUSTERID);
		String nodeIds = strategyMap.get(Item.CODE_NODEIDS);
		List<MVNode> list = nodeResourceService.findAllRunning(clusterId, nodeIds);// list
																					// 部分nodeids
		// 通过checker的cpu 内存 磁盘 隔离 独享 过滤出可用nodes
		list = filterNodes(strategyMap, appId, list, sharedCpu, sameHost, alone);
		// 根据samehost和alone过滤list,如果node节点不够用，申请新节点
		return select(memory, instanceNum, disk, sameHost, alone, nodeType, cpuNum, nodeGroup, isolata, sharedCpu,
				strategyMap, list, appId);
	}

	private List<MVNode> filterNodes(Map<String, String> strategyMap, String appId, List<MVNode> list,
			boolean sharedCpu, boolean sameHost, boolean alone) {
		// 获取策略
		int memory = Integer.parseInt(ConfigHelper.getValue("app.xmx")), cpuNum = 0, desk = 0;
		if (strategyMap.containsKey(Item.CODE_XMX)) {
			memory = Integer.parseInt(strategyMap.get(Item.CODE_XMX));
		}
		if (strategyMap.containsKey(Item.CODE_CPU_NUMBER)) {
			cpuNum = Integer.parseInt(strategyMap.get(Item.CODE_CPU_NUMBER));
		}
		if (strategyMap.containsKey(Item.CODE_DISK)) {
			desk = Integer.parseInt(strategyMap.get(Item.CODE_DISK));
		}

		List<MVNode> nodes = new ArrayList<MVNode>();
		for (MVNode node : list) {
			// 2016-10-11 14:14 yangzhec 现在master管理所有node，没有归还的问题，去掉判断
			if (checker.check(strategyMap, node, false)) {
				int deployableNumber = getDeployableNumber(node, appId, memory, cpuNum, desk, sharedCpu, sameHost,
						alone);
				node.setDeployableNum(deployableNumber);
				if (deployableNumber > 0) {
					nodes.add(node);
				}
			}
		}
		return nodes;

	}

	private List<MVNode> select(int memory, int instanceNum, int disk, boolean sameHost, boolean alone, String nodeType,
			int cpuNum, String nodeGroup, boolean isolata, boolean sharedCpu, Map<String, String> strategyMap,
			List<MVNode> list, String appId) {
		int count = getDeployableNumber(list);
		if (count < instanceNum) {
			return null;
		}
		List<MVNode> copyList = new ArrayList<MVNode>(list);
		Collections.sort(copyList, new Comparator<MVNode>() {
			@Override
			public int compare(MVNode o1, MVNode o2) {
				// 能部署的数目比较少的排在前面，能部署数目多的排在后面
				return o1.getDeployableNum() - o2.getDeployableNum();
			}
		});
		List<MVNode> result = new ArrayList<MVNode>();
		for (MVNode mvNode : copyList) {
			// deployNum最小为1
			int deployNum = mvNode.getDeployableNum();
			for (int i = 0; i < deployNum; i++) {
				result.add(getMV(mvNode, cpuNum, sharedCpu, sameHost, alone));
				mvNode.setDeployableNum(mvNode.getDeployableNum() - 1);
				if (result.size() == instanceNum) {
					return result;
				}
			}
		}
		return null;
	}

	// 2016-10-11 16:26 yangzhec 根据内存，cpu数目，是否共享cpu，
	// 是否可以在一个主机部署同一应用的多个实例，是否是实例独占主机来确定该node能部署多少个实例
	// 注意，该node已经经过filterNode方法，所以满足独占的要求
	private int getDeployableNumber(MVNode node, String appId, int memory, int cpuNum, int desk, boolean sharedCpu,
			boolean sameHost, boolean alone) {
		double memorySize = node.getMemory();
		int memoryNum = new Double(memorySize / memory).intValue();
		int deskNum = 0;
		if (desk == 0) {
			deskNum = Integer.MAX_VALUE;
		} else {
			deskNum = node.getDisk() / desk;
		}
		if (memoryNum == 0) {
			return 0;
		}
		if (deskNum == 0) {
			return 0;
		}
		List<Cpu> cpuList = node.getCpuList();
		if (alone) {
			return 1;
		} else {
			if (!sameHost) {
				// 一实例一主机
				// 判断该主机上有没有该应用的实例，如果有，return 0
				if (isSameHost(node.getName(), appId)) {
					return 0;
				}
				if (sharedCpu) {
					// 共享cpu
					int available = getUsableSharedCpu(cpuList);
					if (available >= cpuNum) {
						return 1;
					} else {
						return 0;
					}
				} else {
					// 独占cpu
					int available = getUnusedCpu(cpuList);
					if (available >= cpuNum) {
						return 1;
					} else {
						return 0;
					}
				}
			} else {
				// 如果可以多实例一主机
				if (sharedCpu) {
					// 共享cpu
					if (getUsableSharedCpu(cpuList) < cpuNum) {
						return 0;
					}
					// 不改变cpuList，复制一份
					List<Cpu> copy = new ArrayList<Cpu>();
					for (Cpu cpu : cpuList) {
						if (cpu.usable()) {
							copy.add(cpu.copy());
						}
					}
					Comparator<Cpu> comparator = new Comparator<Cpu>() {
						@Override
						public int compare(Cpu o1, Cpu o2) {
							return o1.getUsedCount() - o2.getUsedCount();
						}
					};
					int available = 0;
					while (true) {
						removeFullCpu(copy);
						if (copy.size() < cpuNum) {
							break;
						}
						Collections.sort(copy, comparator);
						for (int i = 0; i < cpuNum; i++) {
							Cpu c = copy.get(i);
							c.setUsedCount(c.getUsedCount() + 1);
						}
						available++;
					}
					int byCpuNum = available / cpuNum;
					return min(min(memoryNum, byCpuNum), deskNum);
				} else {
					// 独占cpu
					int available = getUnusedCpu(cpuList);
					int byCpuNum = available / cpuNum;
					return min(min(memoryNum, byCpuNum), deskNum);
				}
			}
		}
	}

	private static int getUsableSharedCpu(List<Cpu> cpuList) {
		int available = 0;
		for (Cpu cpu : cpuList) {
			if (cpu.isShared() == null || (true && cpu.isShared() && cpu.usable())) {
				available++;
			}
		}
		return available;
	}
	
	private static int getUnusedCpu(List<Cpu> cpuList){
		int available = 0;
		for(Cpu cpu : cpuList){
			if(cpu.isShared() == null || cpu.getUsedCount() == 0){
				available ++;
			}
		}
		return available;
	}
	
	private static void removeFullCpu(List<Cpu> cpuList){
		Iterator<Cpu> it = cpuList.iterator();
		while(it.hasNext()){
			Cpu c = it.next();
			if(c.getUsedCount() == c.getMaxCount()){
				it.remove();
			}
		}
	}
	
	private boolean isSameHost(String nodeName, String appId) {
		List<Map<String, Object>> result = applicationService.findAppByNodeName(nodeName);
		List<String> appIds = new ArrayList<String>();
		if( result != null && result.size() > 0){
			for( Map<String,Object> r : result ){
				String app = (String) r.get("APP_ID");
				if( !appIds.contains( app ) ){
					appIds.add( app );
				}
			}
		}
		return appIds.contains(appId);
	}
	
	private MVNode getMV(MVNode node, int cpuNum, boolean sharedCpu, boolean sameHost, boolean alone) {
		MVNode mv = new MVNode();
		
		mv.setName(node.getName());
		mv.setHostName(node.getHostName());
		mv.setCpuList(node.getCpuList());
		if (cpuNum > 0 && node.getCpus() != null) {
			//确定使用哪几个cpu，使用为true，不使用为false，此事务内更新cpu表，统一提交
			mv.setCpus(selectCpu(node, cpuNum, sharedCpu,sameHost,alone));
		}
		return mv;
	}
	
	private boolean[] selectCpu(MVNode node, int cpuNum, boolean sharedCpu,
			boolean sameHost, boolean alone){
		//cpu信息尚未被修改，还和数据库中的一致
		List<Cpu> cpuList = node.getCpuList();
		boolean[] nodeCpus = node.getCpus();
		boolean[] cpus = new boolean[nodeCpus.length];
		if(alone){
			//如果是独占主机
			for(int i= 0;i<cpuNum;i++){
				Cpu c = cpuList.get(i);
				c.setShared(false);
				cpus[i] = true;
				nodeCpus[i] = true;
				int usedCount = c.getUsedCount() + 1;
				c.setUsedCount(usedCount);
				updateCpuShared(c.getId(),false,usedCount);
			}
		}else{
			if(sharedCpu){
				Comparator<Cpu> comparator = new Comparator<Cpu>() {
					@Override
					public int compare(Cpu o1, Cpu o2) {
						return o1.getUsedCount() - o2.getUsedCount();
					}
				};
				List<Cpu> copy = new ArrayList<Cpu>(cpuList);
				removeFullCpu(copy);
				Collections.sort(copy,comparator);
				for(int i=0;i<cpuNum;i++){
					Cpu c = copy.get(i);
					int usedCount = c.getUsedCount()+1;
					c.setUsedCount(usedCount);
					c.setShared(true);
					updateCpuShared(c.getId(),true,usedCount);
					for(int j = 0;j<cpuList.size();j++){
						Cpu real = cpuList.get(j);
						if(real.equals(c)){
							cpus[j] = true;
							nodeCpus[j] = true;
							break;
						}
					}
				}
			}else{
				int temp = 0;
				for(int i = 0;i<cpuList.size();i++){
					Cpu c = cpuList.get(i);
					if(c.isShared() == null || c.getUsedCount() == 0){
						c.setShared(false);
						int usedCount = c.getUsedCount()+1;
						c.setUsedCount(usedCount);
						updateCpuShared(c.getId(), false, usedCount);
						cpus[i] = true;
						nodeCpus[i] = true;
						temp++;
						if(temp == cpuNum){
							break;
						}
					}
				}
			}
		}
		return cpus;
	}
	
	private int getDeployableNumber(List<MVNode> nodes) {
		int count = 0;
		if (nodes != null) {
			for (MVNode node : nodes) {
				count += node.getDeployableNum();
			}
		}
		return count;
	}
	
	private void updateCpuShared(long id, Boolean b, int usedCount) {
		nodeResourceService.updateCpuShared(id, b, usedCount);
	}
}
