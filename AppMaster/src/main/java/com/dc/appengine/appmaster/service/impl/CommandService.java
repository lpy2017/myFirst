package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.entity.Node;
import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;
import com.dc.appengine.appmaster.message.bean.MVNode;
import com.dc.appengine.appmaster.message.sender.CommandSender;
import com.dc.appengine.appmaster.service.ICommandService;
import com.dc.appengine.appmaster.status.running.list.InDBNodeSelector;

public class CommandService extends CommandSender implements ICommandService {
	
	@Autowired
	@Qualifier("nodeService")
	private NodeService nodeService;
	
	
	@Override
	public Map<Object, Object> operationDeploy(Application app, boolean doDeploy) {
		
		return null;
	}
	
	private List<Node> selectNodes(Application app, int instances_number, Strategy deployStrategy) {
		List<Node> nodes = null;
		if (Strategy.TYPE_DEPLOY.equals(deployStrategy.getType())) {
			List<StrategyItem> strategyItems = deployStrategy.getList();
			if (instances_number > 0) {
				boolean hasInstanceNumber = false;
				for (StrategyItem item : strategyItems) {
					if (Item.CODE_INSTANCE_NUMBER.equals(item.getItemCode())) {
						item.setValue(String.valueOf(instances_number));
						hasInstanceNumber = true;
						break;
					}
				}
				if (!hasInstanceNumber) {
					StrategyItem item = new StrategyItem();
					item.setItemCode(Item.CODE_INSTANCE_NUMBER);
					item.setValue(String.valueOf(instances_number));
					strategyItems.add(item);
				}
			}
			nodes = selectNode(strategyItems, String.valueOf(app.getId()), null);
		}
		return nodes;
	}
	
	public List<Node> selectNode(List<StrategyItem> strategyItems, String appId, String runtimeCount) {
		List<MVNode> list = InDBNodeSelector.getInstance().selectNode(strategyItems, appId, runtimeCount);
		List<Node> nodes = new ArrayList<Node>();
		if (list != null) {
			for (MVNode node : list) {// 返回node list
				Node n = nodeService.findNodeByName(node.getName());
				if (n == null) {
					return selectNode(strategyItems, appId, runtimeCount);
				} else {
					Node sn = new Node();
					BeanUtils.copyProperties(n, sn);
					sn.setCpuList(node.getCpuList());
					sn.setCpus(node.getCpus());
					nodes.add(sn);
				}
			}
			return nodes;
		}
		return null;
	}

}
