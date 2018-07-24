package com.dc.appengine.appmaster.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.IStrategyDao;
import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;
import com.dc.appengine.appmaster.service.IItemService;
import com.dc.appengine.appmaster.service.IStrategyService;

@Service("strategyService")
public class StrategyService implements IStrategyService {
	
	@Autowired
	@Qualifier("strategyDao")
	private IStrategyDao strategyDao; 
	
	@Autowired
	@Qualifier("itemService")
	private IItemService itemService;

	@Override
	public Long save(Strategy strategy, List<StrategyItem> list) {
		Long strategyId = strategyDao.save(strategy);
		for(StrategyItem strategyItem : list){
			strategyItem.setStrategyId(strategyId);
			if( strategyItem.getItemId() == null && strategyItem.getItemCode() != null ){
				strategyItem.setItemId(itemService.findIdByCodeAndType(
						strategyItem.getItemCode(), strategy.getType()));
			}
			strategyDao.saveStrategy(strategyItem);
		}
		return strategyId;
	}
	public Strategy findById(Long id){
		return strategyDao.findById(id);
	}
}
