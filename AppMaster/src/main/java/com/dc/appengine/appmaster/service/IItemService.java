package com.dc.appengine.appmaster.service;

public interface IItemService {
	Long findIdByCodeAndType(String itemCode, String type);
}
