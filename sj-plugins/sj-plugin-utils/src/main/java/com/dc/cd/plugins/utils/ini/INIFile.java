package com.dc.cd.plugins.utils.ini;

import java.util.ArrayList;
import java.util.List;

public class INIFile {
	
	private List<Section> sections;
	private String name;
	
	public INIFile(String name) {
		this.name = name;
		this.sections = new ArrayList<Section>();
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
