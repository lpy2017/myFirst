package com.dc.appengine.appmaster.status.running.checker;

public class NodeResourceCheckerBuilder {
	
	private static NodeResourceCheckerBuilder builder;

	public static NodeResourceCheckerBuilder getInstance(){
		if( builder == null ){
			builder = new NodeResourceCheckerBuilder();
		}
		return builder;
	}
	
	private NodeResourceCheckerBuilder(){
		
	}
	
	public INodeResourceChecker<?> getChecker(){
		NodeIsolataChecker isolata = new NodeIsolataChecker();
		NodeLabelChecker label = new NodeLabelChecker();
		NodeCpuChecker cpu = new NodeCpuChecker();
		NodeMemoryChecker memory = new NodeMemoryChecker();
		NodeAloneChecker alone = new NodeAloneChecker();
		NodeDiskChecker disk = new NodeDiskChecker();
		isolata.setChecker( label );
		label.setChecker(cpu);
		cpu.setChecker( memory );
		memory.setChecker( alone );
		alone.setChecker( disk );
		return isolata;
	}
}
