package com.dc.bank.appengine.plugins.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="root")
public class AppFiles {
	
	private List<AppFile> files=new ArrayList<AppFile>();
	@XmlElement(name="file-set")
	public List<AppFile> getFiles() {
		return files;
	}

	public void setFiles(List<AppFile> files) {
		this.files = files;
	} 
	
}
