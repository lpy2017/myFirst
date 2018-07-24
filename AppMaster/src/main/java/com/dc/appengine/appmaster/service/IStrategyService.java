package com.dc.appengine.appmaster.service;

import java.util.List;

import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;

public interface IStrategyService {
	Long save(Strategy strategy,List<StrategyItem> list);

	Strategy findById(Long strategyId);
}
