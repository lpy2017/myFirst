package com.dc.appengine.appmaster.dao;

import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;

public interface IStrategyDao {
	Long save(Strategy strategy);
	Long saveStrategy(StrategyItem strategyItem);
	Strategy findById(Long id);
}
