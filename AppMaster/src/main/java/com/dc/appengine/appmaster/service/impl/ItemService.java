package com.dc.appengine.appmaster.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.dao.IItemDao;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.service.IItemService;

@Service("itemService")
public class ItemService implements IItemService {
	
	@Autowired
	@Qualifier("itemDao")
	private IItemDao dao;

	@Override
	public Long findIdByCodeAndType(String itemCode, String type) {
		if(!"DEPLOY".equals(type)){
			type = Item.TYPE_SCALE;
		}
		return dao.findIdByCodeAndType(itemCode, type);
	}

}
