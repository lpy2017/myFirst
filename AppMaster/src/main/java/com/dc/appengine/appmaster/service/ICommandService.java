package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Node;
import com.dc.appengine.appmaster.entity.StrategyItem;

public interface ICommandService {
	Map<Object, Object> operationDeploy(Application app,boolean doDeploy);
	List<Node> selectNode( List<StrategyItem> strategyItems,String appId,String runtimeCount);
}
