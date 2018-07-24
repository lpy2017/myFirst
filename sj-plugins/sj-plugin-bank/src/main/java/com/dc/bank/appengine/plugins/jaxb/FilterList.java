package com.dc.bank.appengine.plugins.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="filters")
public class FilterList {
	List<Filter> filter;
	@XmlElement(name = "filter")
	public List<Filter> getFilter() {
		return filter;
	}

	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}
}
