package com.dc.cd.plugins.utils.ini;

import java.util.ArrayList;
import java.util.List;

public class Section {
	
	private String name;
	private String comment;
	private List<Item> addItems;
	private List<Item> updateItems;
	private List<Item> removeItems;
	
	public Section(String name) {
		this.name = name;
		this.addItems = new ArrayList<Item>();
		this.updateItems = new ArrayList<Item>();
		this.removeItems = new ArrayList<Item>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Item> getAddItems() {
		return addItems;
	}
	public void setAddItems(List<Item> addItems) {
		this.addItems = addItems;
	}
	public List<Item> getUpdateItems() {
		return updateItems;
	}
	public void setUpdateItems(List<Item> updateItems) {
		this.updateItems = updateItems;
	}
	public List<Item> getRemoveItems() {
		return removeItems;
	}
	public void setRemoveItems(List<Item> removeItems) {
		this.removeItems = removeItems;
	}

}
