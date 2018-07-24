package com.dc.appengine.appmaster.dao.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IStrategyDao;
import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;

@Component("strategyDao")
public class StrategyDao implements IStrategyDao {
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public Long save(Strategy strategy) {
		sqlSessionTemplate.insert("strategy.save",strategy);
		return strategy.getId();
	}

	@Override
	public Long saveStrategy(StrategyItem strategyItem) {
		sqlSessionTemplate.insert("strategy.saveStrategy",strategyItem);
		return strategyItem.getId();
	}

	@Override
	public Strategy findById(Long id) {
		return sqlSessionTemplate.selectOne("strategy.findById", id);
	}

}
