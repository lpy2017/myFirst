package com.dc.appengine.plugins.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="root")
public class SqlFileList {
	List<SqlFile> sqlFiles;
	@XmlElement(name = "sql-file")
	public List<SqlFile> getSqlFiles() {
		return sqlFiles;
	}

	public void setSqlFiles(List<SqlFile> sqlFiles) {
		this.sqlFiles = sqlFiles;
	}
	
}
