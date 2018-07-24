package com.dc.appengine.appmaster.dao;

public interface IItemDao {
	Long findIdByCodeAndType(String itemCode, String type);
}
