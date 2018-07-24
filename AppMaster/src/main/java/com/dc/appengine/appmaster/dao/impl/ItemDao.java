package com.dc.appengine.appmaster.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IItemDao;

@Component("itemDao")
public class ItemDao implements IItemDao {
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	@Override
	public Long findIdByCodeAndType(String itemCode, String type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", itemCode);
		map.put("type", type);
		return sqlSessionTemplate.selectOne("item.findIdByCode", map);
	}

}
