package com.dc.appengine.appmaster.status.running.checker;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Cpu;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.message.bean.MVNode;

public class NodeCpuChecker extends NodeResourceChecker<Integer> {

	@Override
	public boolean check(Map<String, String> strategy, MVNode node, boolean op) {
		Integer usedCpu = getStrategyValue( strategy );
		
		if( usedCpu != null ) {
			//2016-10-11 14:57 yangzhec master保存Node的时候，ma_node中的ISOLATA_STATE是1，
			//然后保存node_resource表的时候，isolate_state是0 used_lxc是1
			if( ( MVNode.ISOLATA_CPU_STRONG == node.getIsolateState() 
					|| MVNode.ISOLATA_CPU_INIT == node.getIsolateState() ) 
					&& this.docheck(strategy, node) ){//use cpu as resource and has enough cpu number
				
				boolean sharedCpu = true;
				if (strategy.containsKey(Item.CODE_SHARED_CPU)) {
					sharedCpu = Boolean.parseBoolean(strategy
							.get(Item.CODE_SHARED_CPU));
				}
				List<Cpu> cpus = node.getCpuList();
				int available = 0;
				if (cpus != null && !cpus.isEmpty()) {
					for (Cpu cpu : cpus) {
						//添加共享次数限制
						if (cpu.isShared() == null ||
							( sharedCpu &&cpu.isShared() && cpu.usable() )) {
							available++;
						}
						if (available == usedCpu) {
							break;
						}
					}
				}
				
				if (available >= usedCpu) {
					if (checker != null) {
						return this.checker.check(strategy, node, true);
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {//use cpu as resource and hasn't enough cpu number or node type is ISOLATA_CPU_WEAK
				return false;
			}
		} else {// usen't cpu as resource 
			
			if( MVNode.ISOLATA_CPU_WEAK == node.getIsolateState()  
					|| MVNode.ISOLATA_CPU_INIT == node.getIsolateState() ){// node type is ISOLATA_CPU_WEAK or ISOLATA_CPU_INIT
				if( checker != null ){
					return this.checker.check(strategy, node, true);
				} else {
					return true;
				}
			} else{
				return false;
			}
		}
	}

	@Override
	public boolean compare(Integer value, MVNode node) {
		return node.getCpuNum() >= value;
	}

	@Override
	public Integer getStrategyValue(Map<String, String> strategy) {
		return strategy.get( Item.CODE_CPU_NUMBER ) != null ? 
					Integer.parseInt( strategy.get( Item.CODE_CPU_NUMBER ) ) : null ;		
	}

	


}
